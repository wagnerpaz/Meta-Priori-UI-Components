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

import com.metapriori.ui.flexcolumns.measure.FlexLength;
import com.metapriori.ui.flexcolumns.measure.LengthMeasure;

/**
 * <p>Gerencia o comprimento de um conjunto de colunas.</p>
 * 
 * @author Wagner Paz
 * @since  1.0
 */
public class FlexColumnLayout extends Layout
{
	/** Managed Columns: todas as colunas gerenciadas por esse layout */
	private List<FlexColumnData> mcolumns = new LinkedList<FlexColumnData>();
	
	/** Fixed Managed Columns: apenas as colunas com comprimento definido de forma fixa */
	private List<FlexColumnData> fcolumns = new LinkedList<FlexColumnData>();
	
	/** Percentage Managed Columns: apenas as colunas com comprimento definido em porcentagem */
	private List<FlexColumnData> pcolumns = new LinkedList<FlexColumnData>();
	
	/** Relação entre a implementação da coluna (tabela) e as definições flexíveis */
	private Map<TableColumn, FlexColumnData> mapTableColumnData = new HashMap<TableColumn, FlexColumnData>();
	
	/** Relação entre a implementação da coluna (árvore) e as definições flexíveis */
	private Map<TreeColumn, FlexColumnData> mapTreeColumnData = new HashMap<TreeColumn, FlexColumnData>();
	
	private int    totalFixedWidth;
	private double totalPerctWidth;
	
	private Map<Composite, Listener> installedListeners = new HashMap<Composite, Listener>();
	
	public void setColumnData(FlexColumnData column)
	{
		if(column.getTableColumn() != null)
		{
			mapTableColumnData.put(column.getTableColumn(), column);
		}
		else if(column.getTreeColumn() != null)
		{
			mapTreeColumnData.put(column.getTreeColumn(), column);
		}
		else
		{
			throw new IllegalStateException("Não possui a implementação da coluna configurada");
		}
		
		LengthMeasure measure = column.getPreferredLength().getMeasure();
		switch(measure)
		{
			case PERCENTAGE: pcolumns.add(column); break;
			case PIXEL     : fcolumns.add(column); break;
		}
		mcolumns.add(column);
	}
	
	public void setColumnData(TableColumn tableColumn, String preferredLength, String minLength)
	{
		setColumnData(new FlexColumnData(tableColumn, preferredLength, minLength));
	}
	
	public void setColumnData(TableColumn tableColumn, String preferredLength)
	{
		setColumnData(new FlexColumnData(tableColumn, preferredLength));
	}
	
	public void setColumnData(TreeColumn treeColumn, String preferredLength, String minLength)
	{
		setColumnData(new FlexColumnData(treeColumn, preferredLength, minLength));
	}
	
	public void setColumnData(TreeColumn treeColumn, String preferredLength)
	{
		setColumnData(new FlexColumnData(treeColumn, preferredLength));
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
		
		Scrollable scrollable = getControl(composite);
		scrollable.setBounds(containerArea);
		
		Rectangle tableArea = scrollable.getClientArea();
		
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
		return mapTableColumnData.get(column);
	}
	
	public FlexColumnData getColumnData(TreeColumn column)
	{
		return mapTreeColumnData.get(column);
	}
	
	Scrollable getControl(Composite composite)
	{
		return (Scrollable) composite.getChildren()[0];
	}
}