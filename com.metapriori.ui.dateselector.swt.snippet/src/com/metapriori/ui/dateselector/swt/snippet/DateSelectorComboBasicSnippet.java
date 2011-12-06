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

		DateSelectorCombo dtNative = new DateSelectorCombo(shell, SWT.NONE);
		dtNative.setDate(new Date());
		
		DateSelectorCombo dtFlat = new DateSelectorCombo(shell, SWT.FLAT|SWT.BORDER);
		dtFlat.setDate(new Date());

		shell.pack();
		shell.open();
		while (!shell.isDisposed())
		{
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}
}