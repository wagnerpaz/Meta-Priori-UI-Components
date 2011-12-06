package com.metapriori.ui.dateselector.swt;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
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
	
	private ComboSwitch cmbDay;
	private Label       lblSeparator1;
	private ComboSwitch cmbMonth;
	private Label       lblSeparator2;
	private ComboSwitch cmbYear;
	private boolean	    flat;
	
	public DateSelectorCombo(Composite parent, int style)
	{
		super(parent, style);
		flat = (style & SWT.FLAT) == SWT.FLAT;
		
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
		
		createDayCombo(this);
		
		lblSeparator1 = new Label(this, SWT.NONE);
		lblSeparator1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSeparator1.setText("/");
		
		createMonthCombo(this);
		
		lblSeparator2 = new Label(this, SWT.NONE);
		lblSeparator2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSeparator2.setText("/");
		
		createYearCombo(this);
	}
	
	private void createDayCombo(Composite parent)
	{
		GridData gldCmbDay = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		
		if(!flat)
		{
			cmbDay = new ComboSwitch(new Combo(parent, SWT.NONE));
			gldCmbDay.widthHint = 15;
		}
		else
		{
			cmbDay = new ComboSwitch(new CCombo(parent, SWT.NONE));
			gldCmbDay.widthHint = 41;
		}
		
		cmbDay.setLayoutData(gldCmbDay);
		
		cmbDay.setVisibleItemCount(20);
		cmbDay.setItems(createNumStrRange(1, 31, FMT_DAY));
		cmbDay.addListener(SWT.Modify, new Listener()
		{
			@Override
			public void handleEvent(Event e)
			{
				if(cmbDay.getText().length() >= 2)
				{
					cmbMonth.forceFocus();
				}
			}
		});
		cmbDay.addListener(SWT.FocusOut, new Listener()
		{
			@Override
			public void handleEvent(Event e)
			{
				select(cmbDay, !cmbDay.getText().isEmpty() ? FMT_DAY.format( Integer.parseInt(cmbDay.getText()) ) : "");
			}
		});
		cmbDay.addListener(SWT.KeyDown, new Listener()
		{
			@Override
			public void handleEvent(Event e)
			{
				if(!isValidInput(e.character))
				{
					e.doit = false;
				}
			}
		});
	}

	private void createMonthCombo(DateSelectorCombo parent)
	{
		GridData gldCmbMounth = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		
		if(!flat)
		{
			cmbMonth = new ComboSwitch(new Combo(parent, SWT.NONE));
			gldCmbMounth.widthHint = 15;
		}
		else
		{
			cmbMonth = new ComboSwitch(new CCombo(parent, SWT.NONE));
			gldCmbMounth.widthHint = 41;
		}
		
		cmbMonth.setLayoutData(gldCmbMounth);
		
		cmbMonth.setVisibleItemCount(12);
		cmbMonth.setItems(createNumStrRange(1, 12, FMT_MONTH));
		cmbMonth.addListener(SWT.Modify, new Listener()
		{
			@Override
			public void handleEvent(Event e)
			{
				if(cmbMonth.getText().length() >= 2)
				{
					cmbYear.forceFocus();
				}
			}
		});
		cmbMonth.addListener(SWT.FocusOut, new Listener()
		{
			@Override
			public void handleEvent(Event e)
			{
				select(cmbMonth, !cmbMonth.getText().isEmpty() ? FMT_MONTH.format( Integer.parseInt(cmbMonth.getText()) ) : "");
			}
		});
		cmbMonth.addListener(SWT.KeyDown, new Listener()
		{
			@Override
			public void handleEvent(Event e)
			{
				if(!isValidInput(e.character))
				{
					e.doit = false;
				}
			}
		});
	}

	private void createYearCombo(DateSelectorCombo parent)
	{
		GridData gldCmbYear = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		
		if(!flat)
		{
			cmbYear = new ComboSwitch(new Combo(parent, SWT.NONE));
			gldCmbYear.widthHint = 30;
		}
		else
		{
			cmbYear = new ComboSwitch(new CCombo(parent, SWT.NONE));
			gldCmbYear.widthHint = 55;
		}
		
		cmbYear.setLayoutData(gldCmbYear);
		
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		
		cmbYear.setVisibleItemCount(20);
		cmbYear.setItems(createNumStrRange(currentYear - 50, currentYear + 10, FMT_YEAR));
		cmbYear.addListener(SWT.Modify, new Listener()
		{
			@Override
			public void handleEvent(Event e)
			{
				if(cmbYear.getText().length() >= 4)
				{
					cmbYear.traverse(SWT.TRAVERSE_TAB_NEXT);
				}
			}
		});
		cmbYear.addListener(SWT.FocusOut, new Listener()
		{
			@Override
			public void handleEvent(Event e)
			{
				select(cmbYear, !cmbYear.getText().isEmpty() ? FMT_YEAR.format( Integer.parseInt(cmbYear.getText()) ) : "");
			}
		});
		cmbYear.addListener(SWT.KeyDown, new Listener()
		{
			@Override
			public void handleEvent(Event e)
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
		String day   = FMT_DAY  .format(cal.get(Calendar.DAY_OF_MONTH)     );
		String month = FMT_MONTH.format(cal.get(Calendar.MONTH       ) + 1 );
		String year  = FMT_YEAR .format(cal.get(Calendar.YEAR        )     );
		
		select(cmbDay  , day);
		select(cmbMonth, month);
		select(cmbYear , year);
	}
	
	private void parseDate()
	{
		try
		{
			Date old = cal.getTime();
			
			cal.set(Calendar.DAY_OF_MONTH, !cmbDay  .getText().isEmpty() ? FMT_DAY  .parse(cmbDay  .getText()).intValue()    : 0);
			cal.set(Calendar.MONTH       , !cmbMonth.getText().isEmpty() ? FMT_MONTH.parse(cmbMonth.getText()).intValue() -1 : 0);
			cal.set(Calendar.YEAR        , !cmbYear .getText().isEmpty() ? FMT_YEAR .parse(cmbYear .getText()).intValue()    : 0);
			
			changeSupport.firePropertyChange(PROP_DATE, old, cal.getTime());
		}
		catch (ParseException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	private void select(ComboSwitch cmb, String txt)
	{
		Integer index = retrieveSelectionIndex(cmb, txt);
		if(index != null) cmb.select(index); else cmb.setText(txt);
		parseDate();
	}
	
	private Integer retrieveSelectionIndex(ComboSwitch cmb, String txt)
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

	public String[] createNumStrRange(int init, int end, DecimalFormat format)
	{
		String[] result = new String[end-init+1];
		for(int i = 0; i < result.length; i++)
		{
			result[i] = format.format(init + i);
		}
		return result;
	}

	public ComboSwitch getCmbDay()
	{
		return cmbDay;
	}

	public ComboSwitch getCmbMonth()
	{
		return cmbMonth;
	}

	public ComboSwitch getCmbYear()
	{
		return cmbYear;
	}
}