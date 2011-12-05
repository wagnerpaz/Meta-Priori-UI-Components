package com.metapriori.ui.flexcolumns.swt.snippet;

import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;

import com.metapriori.ui.flexcolumns.swt.FlexTable;
import com.metapriori.ui.flexcolumns.swt.FlexTableColumn;

/**
 * @author Wagner Paz
 * @since  1.0
 */
public class FlexColumnsSnippet
{
	private static int idCount = 1;
	
	private static String[] firstNameSet = {"Jhon"  , "Paul"     , "George"  , "Ringo"};
	private static String[] lastNameSet  = {"Lennon", "McCartney", "Harrison", "Starr"};
	private static String[] birthDateSet = {"1940"  , "1942"     , "1943"    , "1940" };
	
	private static Shell shell;
	
	private static FlexTable       tblFlex;
	private static FlexTableColumn tblcId;
	private static FlexTableColumn tblcFirstName;
	private static FlexTableColumn tblcLastName;
	private static FlexTableColumn tblcAge;

	private static Composite pnlButtons;
	private static Button    btnAdd;
	
	public static void main(String[] args)
	{
		Display display = new Display();
		
		shell = new Shell(display);
		shell.setText("FlexColumns SWT Usage Snippet");
		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		shell.setLayout(layout);
		
		createContents();
		
		//INITIAL CONTENT
		createRockstar(0);
		createRockstar(1);
		createRockstar(2);
		createRockstar(3);
		createRockstar(3);
		createRockstar(3);
		
		shell.open();
		shell.pack();
		while (!shell.isDisposed())
		{
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}

	private static void createContents()
	{
		//CREATE A FLEX TABLE
		tblFlex = new FlexTable(shell, SWT.BORDER | SWT.FULL_SELECTION);
		tblFlex.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tblFlex.setHeaderVisible(true);
		tblFlex.setLinesVisible (true);
		{
			tblcId = new FlexTableColumn(tblFlex, SWT.NONE);
			tblcId.setText ("ID");
			tblcId.setWidth("25px");
			
			tblcFirstName = new FlexTableColumn(tblFlex, SWT.NONE);
			tblcFirstName.setText ("First Name");
			tblcFirstName.setWidth("50%", "100px");
			
			tblcLastName = new FlexTableColumn(tblFlex, SWT.NONE);
			tblcLastName.setText ("Last Name");
			tblcLastName.setWidth("50%", "100px");
			
			tblcAge = new FlexTableColumn(tblFlex, SWT.NONE);
			tblcAge.setText ("Age");
			tblcAge.setWidth("60px");
		}
		
		pnlButtons = new Composite(shell, SWT.NONE);
		pnlButtons.setLayout(new GridLayout(1, false));
		pnlButtons.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		{
			btnAdd = new Button(pnlButtons, SWT.NONE);
			btnAdd.setText("Add");
			btnAdd.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(SelectionEvent e)
				{
					TableItem tbli = new TableItem(tblFlex, SWT.NONE);
					tbli.setText(0, Integer.toString(idCount++));
					tbli.setText(1, firstNameSet[new Random().nextInt(4)]);
					tbli.setText(2, lastNameSet [new Random().nextInt(4)]);
					tbli.setText(3, birthDateSet[new Random().nextInt(4)]);
					
					tblFlex.layout();
				}
			});
		}
	}
	
	private static void createRockstar(int i)
	{
		TableItem tbli = new TableItem(tblFlex, SWT.NONE);
		tbli.setText(0, Integer.toString(idCount++));
		tbli.setText(1, firstNameSet[i]);
		tbli.setText(2, lastNameSet [i]);
		tbli.setText(3, birthDateSet[i]);
	}
}