package com.metapriori.ui.flexcolumns.swt;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TreeColumn;

/**
 * <p>Gerencia o comprimento de um conjunto de colunas.</p>
 * 
 * @author Wagner Paz
 * @since  1.0
 */
public class FlexTreeColumnLayout extends Layout
{
	private List<FlexColumnData> mcolumns = new LinkedList<FlexColumnData>();
	
	private int    totalFixedWidth;
	private double totalPerctWidth;
	private int    percentageMargin = 18;
	
	private Map<Composite, Listener> installedListeners = new HashMap<Composite, Listener>();

	private boolean	relayout = true;
	
	public void setColumnData(FlexColumnData column)
	{
		mcolumns.add(column);
	}
	
	public void setColumnData(TableColumn tableColumn, String preferredLength, String minLength)
	{
		mcolumns.add(new FlexColumnData(tableColumn, preferredLength, minLength));
	}
	
	public void setColumnData(TableColumn tableColumn, String preferredLength)
	{
		mcolumns.add(new FlexColumnData(tableColumn, preferredLength));
	}
	
	@Override
	protected Point computeSize(final Composite composite, int wHint, int hHint, boolean flushCache)
	{
		if(installedListeners.get(composite) == null)
		{
			Listener listener = new Listener()
			{
				@Override
				public void handleEvent(Event event)
				{
					event.doit = false;
					calculate(composite.getBounds().width);
				}
			};
			composite.addListener(SWT.Resize, listener);
			installedListeners.put(composite, listener);
		}
		
		Point result = ((Scrollable) composite.getChildren()[0]).computeSize(wHint, hHint);
		calculate(result.x);
		return result;
	}
	
	/**
	 * @param containerWidth
	 */
	public void calculate(int containerWidth)
	{
		totalFixedWidth = 0;
		totalPerctWidth = 0;
		
		//CALCULATE TOTALS
		for (FlexColumnData column : mcolumns)
		{
			FlexLength prefLength = column.getPreferredLength();
			switch (prefLength.getMeasure())
			{
				case PIXEL     : totalFixedWidth += prefLength.getValue(); break;
				case PERCENTAGE: totalPerctWidth += prefLength.getValue(); break;
			}
		}
		
		Integer precentPixelWidth = containerWidth - totalFixedWidth - percentageMargin;
		
		//SET WIDTHS
		for (FlexColumnData mcolumn : mcolumns)
		{
			FlexLength           prefLength = mcolumn.getPreferredLength();
			Double          prefLengthValue = prefLength.getValue();
			LengthMeasure prefLengthMeasure = prefLength.getMeasure();
			
			FlexLength   minLength = mcolumn.getMinLength();
			Double  minLengthValue = minLength.getValue();
			
			switch (prefLengthMeasure)
			{
				case PIXEL:
				{
					updateColumnWidth(mcolumn, Math.max(prefLengthValue, minLengthValue));
					break;
				}
				case PERCENTAGE:
				{
					double percentFromTotal = prefLengthValue / totalPerctWidth;
					int width = (int) (percentFromTotal * precentPixelWidth);
					updateColumnWidth(mcolumn, Math.max(width, minLengthValue));
					break;
				}
			}
		}
	}

	/**
	 * @param mcolumn
	 * @param width
	 */
	private void updateColumnWidth(FlexColumnData mcolumn, Double width)
	{
		TableColumn tableColumn = mcolumn.getTableColumn();
		TreeColumn  treeColumn  = mcolumn.getTreeColumn();
		
		if(tableColumn != null)
		{
			tableColumn.setWidth(width.intValue());
		}
		else if(treeColumn != null)
		{
			treeColumn.setWidth(width.intValue());
		}
		else
		{
			throw new IllegalStateException("Nenhum coluna válida para configurar o width!");
		}
	}

	public List<FlexColumnData> getManagedColumns()
	{
		return mcolumns;
	}

	@Override
	protected void layout(Composite composite, boolean flushCache)
	{
		Rectangle rect     = composite.getClientArea();
		Control[] children = composite.getChildren();
		
		int count = children.length;
		if (count == 0) return;
		
		int width  = rect.width;
		int height = rect.height;

		int x = rect.x, extra     = width % count;
		int y = rect.y, cellWidth = width / count;
		
		Control child = children[0];
		int childWidth = cellWidth;
		childWidth += extra / 2;
		child.setBounds(x, y, childWidth, height);
		
		// For the first time we need to relayout because Scrollbars are not
		// calculate appropriately
		if (relayout)
		{
			relayout = false;
			composite.layout();
		}
	}
}