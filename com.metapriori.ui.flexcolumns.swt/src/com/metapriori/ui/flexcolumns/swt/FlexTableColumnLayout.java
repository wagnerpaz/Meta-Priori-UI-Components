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
public class FlexTableColumnLayout extends Layout
{
	private List<FlexColumnData> mcolumns = new LinkedList<FlexColumnData>();
	private List<FlexColumnData> pcolumns = new LinkedList<FlexColumnData>();
	private Map<TableColumn, FlexColumnData> mapColumnData = new HashMap<TableColumn, FlexColumnData>();
	
	private int    totalFixedWidth;
	private double totalPerctWidth;
	
	private Map<Composite, Listener> installedListeners = new HashMap<Composite, Listener>();
	
	public void setColumnData(FlexColumnData column)
	{
		mapColumnData.put(column.getTableColumn(), column);
		mcolumns.add(column);
		if(column.getPreferredLength().getMeasure() == LengthMeasure.PERCENTAGE)
		{
			pcolumns.add(column);
		}
	}
	
	public void setColumnData(TableColumn tableColumn, String preferredLength, String minLength)
	{
		setColumnData(new FlexColumnData(tableColumn, preferredLength, minLength));
	}
	
	public void setColumnData(TableColumn tableColumn, String preferredLength)
	{
		setColumnData(new FlexColumnData(tableColumn, preferredLength));
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
					composite.layout();
				}
			};
			composite.addListener(SWT.Resize, listener);
			installedListeners.put(composite, listener);
		}
		
		Control child = composite.getChildren()[0];
		
		Point childSize = child.computeSize(wHint, hHint);
		int width  = Math.max(0, childSize.x);
		int height = Math.max(0, childSize.y);
		
		if (wHint != SWT.DEFAULT)  width = wHint;
		if (hHint != SWT.DEFAULT) height = hHint;
		
		return new Point(width, height);
	}
	
	@Override
	protected void layout(Composite composite, boolean flushCache)
	{
		Rectangle containerArea = composite.getClientArea();
		
		Scrollable table = getControl(composite);
		table.setBounds(containerArea);
		
		Rectangle tableArea = table.getClientArea();
		
		totalFixedWidth = 0;
		totalPerctWidth = 0;
		
		//CALCULATE TOTALS
		for (FlexColumnData column : mcolumns)
		{
			if(column.isVisible())
			{
				FlexLength prefLength = column.getPreferredLength();
				FlexLength minLength  = column.getMinLength();
				if(prefLength.getValue() != 0)
				{
					addTotalLength(prefLength);
				}
				else
				{
					addTotalLength(minLength);
				}
			}
		}
		
		Integer precentPixelWidth = tableArea.width - totalFixedWidth;
		
		//SET WIDTHS
		for (FlexColumnData mcolumn : mcolumns)
		{
			if(mcolumn.isVisible())
			{
				FlexLength           prefLength = mcolumn.getPreferredLength();
				Double          prefLengthValue = prefLength.getValue();
				LengthMeasure prefLengthMeasure = prefLength.getMeasure();
				
				FlexLength   minLength = mcolumn.getMinLength();
				Double minLengthValue = minLength.getValue();
				
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
			else
			{
				updateColumnWidth(mcolumn, 0d);
			}
		}
	}
	
	private boolean addTotalLength(FlexLength length)
	{
		switch (length.getMeasure())
		{
			case PIXEL     : totalFixedWidth += length.getValue(); break;
			case PERCENTAGE: totalPerctWidth += length.getValue(); break;
		}
		
		return length.getValue() != 0;
	}

	public void redistributePercentage(TableColumn clmn, Double value)
	{
		int qtdCols = pcolumns.size() - 1;
		for (FlexColumnData data : pcolumns)
		{
			FlexLength preferredLength = data.getPreferredLength();
			if(clmn != data.getTableColumn())
			{
				preferredLength.setValue(preferredLength.getValue() + (value/qtdCols));
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
	
	public FlexColumnData getColumnData(TableColumn column)
	{
		return mapColumnData.get(column);
	}
	
	Scrollable getControl(Composite composite)
	{
		return (Scrollable) composite.getChildren()[0];
	}
}