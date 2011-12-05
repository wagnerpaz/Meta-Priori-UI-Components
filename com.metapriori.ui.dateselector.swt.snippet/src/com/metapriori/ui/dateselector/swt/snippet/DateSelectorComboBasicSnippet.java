package com.metapriori.ui.dateselector.swt.snippet;

import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.metapriori.ui.dateselector.swt.DateSelectorCombo;

/**
 * @author Wagner Paz
 * @since  1.0
 */
public class DateSelectorComboBasicSnippet
{
	public static void main(String[] args)
	{
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Snippet");
		shell.setLayout(new GridLayout(1, false));

		DateSelectorCombo dt = new DateSelectorCombo(shell, SWT.NONE);
		dt.setDate(new Date());

		shell.pack();
		shell.open();
		while (!shell.isDisposed())
		{
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}
}