package com.metapriori.ui.flexcolumns.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * @author Wagner Paz
 * @since  1.0
 */
public class FlexTable extends Table
{
	private Composite parent;
	private Composite container;
	
	private Menu headerMenu;
	private FlexTableColumnLayout flexLayout;
	
	public FlexTable(final Composite parent, int style)
	{
		super(new Composite(parent, SWT.NONE)
		{
			@Override
			public void reskin(int flags)
			{
				super.reskin(flags);
			}
		}, style);
		
		this.parent    = parent;
		this.container = super.getParent();
		
		flexLayout = new FlexTableColumnLayout()
		{
			@Override
			public void setColumnData(FlexColumnData column)
			{
				super.setColumnData(column);
				createMenuItem(headerMenu, column);
			}
		};
		container.setLayout(flexLayout);
		
		headerMenu = new Menu(container.getShell(), SWT.POP_UP);
		
		addListener(SWT.MenuDetect, new Listener()
		{
			public void handleEvent(Event event)
			{
				setMenu(isMouseOverHeader(event.x, event.y) ? headerMenu : null);
			}
		});
		addListener(SWT.Dispose, new Listener()
		{
			public void handleEvent(Event event)
			{
				headerMenu.dispose();
			}
		});
	}
	
	protected boolean isMouseOverHeader(int x, int y)
	{
		Point pt = Display.getDefault().map(null, FlexTable.this, new Point(x, y));
		Rectangle clientArea = getClientArea();
		
		return clientArea.y <= pt.y && pt.y < (clientArea.y + getHeaderHeight());
	}

	private void createMenuItem(Menu menu, final FlexColumnData flexColumn)
	{
		final TableColumn tableColumn = flexColumn.getTableColumn();
		
		final MenuItem itemName = new MenuItem(menu, SWT.CHECK);
		itemName.setText     (tableColumn.getText());
		itemName.setSelection(tableColumn.getResizable());
		itemName.addListener(SWT.Selection, new Listener()
		{
			public void handleEvent(Event event)
			{
				boolean checked = itemName.getSelection();
				flexColumn .setVisible  (checked);
				tableColumn.setResizable(checked);
				layout();
			}
		});
	}
	
	@Override
	public FlexTableColumnLayout getLayout()
	{
		return (FlexTableColumnLayout) container.getLayout();
	}
	
	@Override
	public void setLayout(Layout layout)
	{
		throw new IllegalStateException();
	}
	
	@Override
	public void setLayoutData(Object layoutData)
	{
		container.setLayoutData(layoutData);
	}
	
	@Override
	public void layout()
	{
		container.layout();
	}
	
	@Override
	public Composite getParent()
	{
		return this.parent;
	}
	
	@Override protected void checkSubclass() { }
}