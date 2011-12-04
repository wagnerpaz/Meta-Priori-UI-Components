package com.metapriori.ui.dateselector.swt;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TypedListener;

/**
 * @author Wagner Paz
 * @since  1.0
 */
public class DateSelectorCombo extends Composite
{
	private static final DecimalFormat FMT_DAY   = new DecimalFormat("00");
	private static final DecimalFormat FMT_MONTH = new DecimalFormat("00");
	private static final DecimalFormat FMT_YEAR  = new DecimalFormat("0000");
	
	private Calendar cal = Calendar.getInstance();
	
	private Combo cmbDay;
	private Label lblSeparator1;
	private Combo cmbMonth;
	private Label lblSeparator2;
	private Combo cmbYear;
	
	public DateSelectorCombo(Composite parent, int style)
	{
		super(parent, style);
		createContents();
	}
	
	protected PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
	
	public    static final String   PROP_DATE = "DateSelectorCombo.DATE";
	protected static final String[] SUPPORTED_PROPS = {PROP_DATE};
	
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
	{
		if(Arrays.binarySearch(SUPPORTED_PROPS, propertyName) >= 0)
		{
			changeSupport.addPropertyChangeListener(propertyName, listener);
		}
	}

	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener)
	{
		changeSupport.removePropertyChangeListener(propertyName, listener);
	}
	
	public synchronized void setDate(Date date)
	{
		checkWidget();
		
		if (date == null)
		{
			clear();
		}
		else
		{
			Date old = cal.getTime();
			cal.setTime(date);
			changeSupport.firePropertyChange(PROP_DATE, old, date);
		}
		
		formatDate();
	}
	
	public synchronized Date getDate()
	{
		checkWidget();
		return cal.getTime();
	}
	
	public void clear()
	{
		checkWidget();
		
		Date old = cal.getTime();
		cal.setTime(null);
		changeSupport.firePropertyChange(PROP_DATE, old, null);
		
		cmbDay  .setText("");
		cmbMonth.setText("");
		cmbYear .setText("");
	}
	
	private void createContents()
	{
		GridLayout gridLayout = new GridLayout(5, false);
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth  = 0;
		setLayout(gridLayout);
		
		cmbDay = new Combo(this, SWT.NONE);
		cmbDay.setItems(createNumStrRange(1, 31, FMT_DAY));
		GridData gldCmbDay = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gldCmbDay.widthHint = 15;
		cmbDay.setLayoutData(gldCmbDay);
		cmbDay.addModifyListener(new ModifyListener()
		{
			@Override
			public void modifyText(ModifyEvent e)
			{
				if(cmbDay.getText().length() >= 2)
				{
					cmbMonth.forceFocus();
				}
			}
		});
		cmbDay.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusLost(FocusEvent e)
			{
				select(cmbDay, !cmbDay.getText().isEmpty() ? FMT_DAY.format( Integer.parseInt(cmbDay.getText()) ) : "");
			}
		});
		cmbDay.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if(!isValidInput(e.character))
				{
					e.doit = false;
				}
			}
		});
		
		lblSeparator1 = new Label(this, SWT.NONE);
		lblSeparator1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSeparator1.setText("/");
		
		cmbMonth = new Combo(this, SWT.NONE);
		cmbMonth.setItems(createNumStrRange(1, 12, FMT_MONTH));
		GridData gldCmbMounth = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gldCmbMounth.widthHint = 15;
		cmbMonth.setLayoutData(gldCmbMounth);
		cmbMonth.addModifyListener(new ModifyListener()
		{
			@Override
			public void modifyText(ModifyEvent e)
			{
				if(cmbMonth.getText().length() >= 2)
				{
					cmbYear.forceFocus();
				}
			}
		});
		cmbMonth.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusLost(FocusEvent e)
			{
				select(cmbMonth, !cmbMonth.getText().isEmpty() ? FMT_MONTH.format( Integer.parseInt(cmbMonth.getText()) ) : "");
			}
		});
		cmbMonth.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if(!isValidInput(e.character))
				{
					e.doit = false;
				}
			}
		});
		
		lblSeparator2 = new Label(this, SWT.NONE);
		lblSeparator2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSeparator2.setText("/");
		
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		
		cmbYear = new Combo(this, SWT.NONE);
		cmbYear.setItems(createNumStrRange(currentYear - 50, currentYear + 10, FMT_YEAR));
		GridData gldCmbYear = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gldCmbYear.widthHint = 30;
		cmbYear.setLayoutData(gldCmbYear);
		cmbYear.addModifyListener(new ModifyListener()
		{
			@Override
			public void modifyText(ModifyEvent e)
			{
				if(cmbYear.getText().length() >= 4)
				{
					cmbYear.traverse(SWT.TRAVERSE_TAB_NEXT);
				}
			}
		});
		cmbYear.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusLost(FocusEvent e)
			{
				select(cmbYear, !cmbYear.getText().isEmpty() ? FMT_YEAR.format( Integer.parseInt(cmbYear.getText()) ) : "");
			}
		});
		cmbYear.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if(!isValidInput(e.character))
				{
					e.doit = false;
				}
			}
		});
	}
	
	protected boolean isValidInput(char c)
	{
		return Character.isDigit(c)
		    || c == SWT.DEL
		    || c == SWT.BS
		    || c == SWT.CR
		    || c == SWT.ESC
		    || c == SWT.TAB
		    || c == SWT.LF
		    || c == SWT.ARROW_DOWN
		    || c == SWT.ARROW_LEFT
		    || c == SWT.ARROW_UP
		    || c == SWT.ARROW_RIGHT;
	}

	public void addModifyListener(ModifyListener listener)
	{
		checkWidget();
		TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.Modify, typedListener);
	}
	
	private void formatDate()
	{
		String day   = FMT_DAY  .format(cal.get(Calendar.DAY_OF_MONTH) );
		String month = FMT_MONTH.format(cal.get(Calendar.MONTH       ) );
		String year  = FMT_YEAR .format(cal.get(Calendar.YEAR        ) );
		
		select(cmbDay  , day);
		select(cmbMonth, month);
		select(cmbYear , year);
	}
	
	private void parseDate()
	{
		try
		{
			Date old = cal.getTime();
			
			cal.set(Calendar.DAY_OF_MONTH, !cmbDay  .getText().isEmpty() ? FMT_DAY  .parse(cmbDay  .getText()).intValue() : 0);
			cal.set(Calendar.MONTH       , !cmbMonth.getText().isEmpty() ? FMT_MONTH.parse(cmbMonth.getText()).intValue() : 0);
			cal.set(Calendar.YEAR        , !cmbYear .getText().isEmpty() ? FMT_YEAR .parse(cmbYear .getText()).intValue() : 0);
			
			changeSupport.firePropertyChange(PROP_DATE, old, cal.getTime());
		}
		catch (ParseException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	private void select(Combo cmb, String txt)
	{
		Integer index = retrieveSelectionIndex(cmb, txt);
		if(index != null) cmb.select(index); else cmb.setText(txt);
		parseDate();
	}
	
	private Integer retrieveSelectionIndex(Combo cmb, String txt)
	{
		int count = 0;
		for (String item : cmb.getItems())
		{
			if(item != null && item.equals(txt))
			{
				return count;
			}
			count++;
		}
		return null;
	}

	public static void main(String[] args)
	{
		Display display = new Display();
		Shell shell = new Shell(display);
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
	
	public String[] createNumStrRange(int init, int end, DecimalFormat format)
	{
		String[] result = new String[end-init+1];
		for(int i = 0; i < result.length; i++)
		{
			result[i] = format.format(init + i);
		}
		return result;
	}

	public Combo getCmbDay()
	{
		return cmbDay;
	}

	public Combo getCmbMonth()
	{
		return cmbMonth;
	}

	public Combo getCmbYear()
	{
		return cmbYear;
	}
}