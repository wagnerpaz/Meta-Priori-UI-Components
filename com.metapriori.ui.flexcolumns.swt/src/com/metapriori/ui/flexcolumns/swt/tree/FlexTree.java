package com.metapriori.ui.flexcolumns.swt.tree;

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
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import com.metapriori.ui.flexcolumns.swt.FlexColumnData;
import com.metapriori.ui.flexcolumns.swt.FlexColumnLayout;

/**
 * @author Wagner Paz
 * @since  1.0
 */
public class FlexTree extends Tree
{
	private Composite parent;
	private Composite container;
	
	private Menu headerMenu;
	private FlexColumnLayout flexLayout;
	
	public FlexTree(final Composite parent, int style)
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
		
		flexLayout = new FlexColumnLayout()
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
		Point pt = Display.getDefault().map(null, FlexTree.this, new Point(x, y));
		Rectangle clientArea = getClientArea();
		
		return clientArea.y <= pt.y && pt.y < (clientArea.y + getHeaderHeight());
	}

	private void createMenuItem(Menu menu, final FlexColumnData flexColumn)
	{
		final TreeColumn treeColumn = flexColumn.getTreeColumn();

		final MenuItem itemName = new MenuItem(menu, SWT.CASCADE);
		itemName.setText(treeColumn.getText());

		final Menu submenu = new Menu(menu.getShell(), SWT.DROP_DOWN);
		itemName.setMenu(submenu);
		
		final MenuItem check = new MenuItem(submenu, SWT.CHECK);
		check.setText("Visível");
		check.setSelection(treeColumn.getResizable());
		check.addListener(SWT.Selection, new Listener()
		{
			public void handleEvent(Event event)
			{
				boolean checked = check.getSelection();
				flexColumn.setVisible  (checked);
				treeColumn.setResizable(checked);
				layout();
			}
		});
		
		new MenuItem(submenu, SWT.SEPARATOR);
		
		final MenuItem fixo = new MenuItem(submenu, SWT.PUSH);
		fixo.setText("Tamanho fixo (px)");
		
		final MenuItem perc = new MenuItem(submenu, SWT.PUSH);
		perc.setText("Tamanho percentual (%)");
		
		new MenuItem(submenu, SWT.SEPARATOR);
		
		final MenuItem preso = new MenuItem(submenu, SWT.PUSH);
		preso.setText("Personalizar");
	}
	
	@Override
	public FlexColumnLayout getLayout()
	{
		return (FlexColumnLayout) container.getLayout();
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