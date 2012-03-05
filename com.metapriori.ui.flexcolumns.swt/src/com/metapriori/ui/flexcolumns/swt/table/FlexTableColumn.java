package com.metapriori.ui.flexcolumns.swt.table;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableColumn;

import com.metapriori.ui.flexcolumns.measure.FlexLength;
import com.metapriori.ui.flexcolumns.measure.LengthMeasure;
import com.metapriori.ui.flexcolumns.swt.FlexColumnData;
import com.metapriori.ui.flexcolumns.swt.FlexColumnLayout;

/**
 * @author Wagner Paz
 * @since  1.0
 */
public class FlexTableColumn extends TableColumn
{
	private boolean	originalResizable;

	public FlexTableColumn(final FlexTable parent, int style)
	{
		super(parent, style);
		
		//CONFIG THE PREFERRED LENGTH OF COLUMNS
		addListener(SWT.Resize, new Listener()
		{
			@Override
			public void handleEvent(Event event)
			{
				if(getResizable())
				{
					FlexColumnLayout flexLayout = getParent().getLayout();
					
					FlexColumnData data = flexLayout.getColumnData(FlexTableColumn.this);
					FlexLength prfLength = data.getPreferredLength();
					
					if(prfLength.getMeasure() == LengthMeasure.PIXEL)
					{
						prfLength.setValue((double)getWidth());
					}
					
					getParent().layout();
				}
			}
		});
	}
	
	public FlexTableColumn(FlexTable parent, int style, int index)
	{
		super(parent, style, index);
	}
	
	public void setWidth(String preferredLength)
	{
		getParent().getLayout().setColumnData(this, preferredLength);
	}

	public void setWidth(String preferredLength, String minLength)
	{
		FlexColumnData columnData = new FlexColumnData(this, preferredLength, minLength);
		getParent().getLayout().setColumnData(columnData);
		setResizable(originalResizable);
	}
	
	@Override
	public void setResizable(boolean resizable)
	{
		this.originalResizable = resizable;
		
		FlexColumnLayout flexLayout = getParent().getLayout();
		FlexColumnData data = flexLayout.getColumnData(FlexTableColumn.this);
		
		super.setResizable(originalResizable && data.getPreferredLength().getMeasure() != LengthMeasure.PERCENTAGE);
	}
	
	@Override
	public FlexTable getParent()
	{
		return (FlexTable) super.getParent();
	}
	
	@Override protected void checkSubclass() {}
}