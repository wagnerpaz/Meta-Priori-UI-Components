package com.metapriori.ui.dateselector.swt.snippet;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.metapriori.ui.dateselector.swt.DateSelectorCombo;
import com.metapriori.ui.dateselector.swt.DateSelectorComboObservableValue;

/**
 * @author Wagner Paz
 * @since  1.0
 */
public class DateSelectorComboBindingSnippet
{
	private static Shell  shell;
	
	private static DateSelectorCombo dsc;
	
	private static Label lblDateSelectorCombo;
	private static Label lblSeparator1;
	private static Label lblSelectedValue;
	private static Label lblSelectedValueDisplay;

	public static void main(String[] args)
	{
		final Display display = new Display();
		Realm.runWithDefault(SWTObservables.getRealm(display), new Runnable()
		{
			@Override
			public void run()
			{
				shell = new Shell(display);
				shell.setText("Binding Snippet");
				shell.setLayout(new GridLayout(2, false));
				
				createContents();
				initBindings();
				dsc.setDate(new Date());
				
				shell.pack();
				shell.open();
				while (!shell.isDisposed())
				{
					if (!display.readAndDispatch()) display.sleep();
				}
				display.dispose();
			}

			private void createContents()
			{
				lblDateSelectorCombo = new Label(shell, SWT.NONE);
				lblDateSelectorCombo.setText("DateSelectorCombo:");

				dsc = new DateSelectorCombo(shell, SWT.FLAT, "HH:mm:ss");
				
				lblSeparator1 = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
				lblSeparator1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
				
				lblSelectedValue = new Label(shell, SWT.NONE);
				lblSelectedValue.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
				lblSelectedValue.setText("Selected Value:");
				
				lblSelectedValueDisplay = new Label(shell, SWT.BORDER);
				lblSelectedValueDisplay.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
				lblSelectedValueDisplay.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
			}

			private void initBindings()
			{
				DataBindingContext dbc = new DataBindingContext();
				IObservableValue target = null;
				IObservableValue model  = null;
				
				//BIND DATESELECTORCOMBO TO THE BEAN PROPERTY MYDATE
				target = new DateSelectorComboObservableValue(dsc);
				model  = SWTObservables.observeText(lblSelectedValueDisplay);
				dbc.bindValue(target, model, new UpdateValueStrategy()
				{
					@Override
					public Object convert(Object value)
					{
						return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format((Date)value);
					}
				}, null);
			}
		});
	}
}