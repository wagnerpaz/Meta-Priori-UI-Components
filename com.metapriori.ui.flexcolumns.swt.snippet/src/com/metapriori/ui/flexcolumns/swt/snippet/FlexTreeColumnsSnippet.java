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
import org.eclipse.swt.widgets.TreeItem;

import com.metapriori.ui.flexcolumns.swt.tree.FlexTree;
import com.metapriori.ui.flexcolumns.swt.tree.FlexTreeColumn;

/**
 * @author Wagner Paz
 * @since  1.0
 */
public class FlexTreeColumnsSnippet
{
	private static int idCount = 1;
	
	private static String[] firstNameSet = {"Jhon"  , "Paul"     , "George"  , "Ringo"};
	private static String[] lastNameSet  = {"Lennon", "McCartney", "Harrison", "Starr"};
	private static String[] birthDateSet = {"1940"  , "1942"     , "1943"    , "1940" };
	
	private static Shell shell;
	
	private static FlexTree       treeFlex;
	private static FlexTreeColumn treecId;
	private static FlexTreeColumn treecFirstName;
	private static FlexTreeColumn treecLastName;
	private static FlexTreeColumn treecAge;

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
		treeFlex = new FlexTree(shell, SWT.BORDER | SWT.FULL_SELECTION);
		treeFlex.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		treeFlex.setHeaderVisible(true);
		treeFlex.setLinesVisible (true);
		{
			treecId = new FlexTreeColumn(treeFlex, SWT.NONE);
			treecId.setText ("ID");
			treecId.setWidth("25px");
			
			treecFirstName = new FlexTreeColumn(treeFlex, SWT.NONE);
			treecFirstName.setText ("First Name");
			treecFirstName.setWidth("50%", "100px");
			
			treecLastName = new FlexTreeColumn(treeFlex, SWT.NONE);
			treecLastName.setText ("Last Name");
			treecLastName.setWidth("50%", "100px");
			
			treecAge = new FlexTreeColumn(treeFlex, SWT.NONE);
			treecAge.setText ("Age");
			treecAge.setWidth("60px");
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
					TreeItem tbli = new TreeItem(treeFlex, SWT.NONE);
					tbli.setText(0, Integer.toString(idCount++));
					tbli.setText(1, firstNameSet[new Random().nextInt(4)]);
					tbli.setText(2, lastNameSet [new Random().nextInt(4)]);
					tbli.setText(3, birthDateSet[new Random().nextInt(4)]);
					
					treeFlex.layout();
				}
			});
		}
	}
	
	private static void createRockstar(int i)
	{
		TreeItem tbli = new TreeItem(treeFlex, SWT.NONE);
		tbli.setText(0, Integer.toString(idCount++));
		tbli.setText(1, firstNameSet[i]);
		tbli.setText(2, lastNameSet [i]);
		tbli.setText(3, birthDateSet[i]);
	}
}