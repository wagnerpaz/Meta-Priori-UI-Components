package com.metapriori.ui.dateselector.swt;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

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
	private static final int POS_MONTH = 1;
	
	private static final int[] CALENDAR_TYPES = {Calendar.DAY_OF_MONTH, Calendar.MONTH, Calendar.YEAR
	                                            ,Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND};
	
	private static final String ID_DAY_MED   = "dd";
	private static final String ID_MONTH_MED = "MM";
	private static final String ID_YEAR_MED  = "yyyy";
	private static final String ID_HOUR_MED  = "HH";
	private static final String ID_MIN_MED   = "mm";
	private static final String ID_SEC_MED   = "ss";
	
	private static final String[] PATTERN_FRAGMENTS = {ID_DAY_MED, ID_MONTH_MED, ID_YEAR_MED
	                                                  ,ID_HOUR_MED, ID_MIN_MED, ID_SEC_MED};
	
	private static final DecimalFormat FMT_DAY_MED   = new DecimalFormat("00");
	private static final DecimalFormat FMT_MONTH_MED = new DecimalFormat("00");
	private static final DecimalFormat FMT_YEAR_MED  = new DecimalFormat("0000");
	private static final DecimalFormat FMT_HOUR_MED  = new DecimalFormat("00");
	private static final DecimalFormat FMT_MIN_MED   = new DecimalFormat("00");
	private static final DecimalFormat FMT_SEC_MED   = new DecimalFormat("00");
	
	private static final DecimalFormat[] PATTERN_FORMATS = {FMT_DAY_MED, FMT_MONTH_MED, FMT_YEAR_MED
                                                           ,FMT_HOUR_MED, FMT_MIN_MED, FMT_SEC_MED};
	
	private static final int WDT_DAY_MED_NATIVE   = 15;
	private static final int WDT_MONTH_MED_NATIVE = 15;
	private static final int WDT_YEAR_MED_NATIVE  = 30;
	private static final int WDT_HOUR_MED_NATIVE  = 15;
	private static final int WDT_MIN_MED_NATIVE   = 15;
	private static final int WDT_SEC_MED_NATIVE   = 15;
	
	private static final int[] PATTERN_WIDTHS_NATIVE = {WDT_DAY_MED_NATIVE, WDT_MONTH_MED_NATIVE, WDT_YEAR_MED_NATIVE
                                                       ,WDT_HOUR_MED_NATIVE, WDT_MIN_MED_NATIVE, WDT_SEC_MED_NATIVE};
	
	private static final int WDT_DAY_MED_FLAT   = 42;
	private static final int WDT_MONTH_MED_FLAT = 42;
	private static final int WDT_YEAR_MED_FLAT  = 55;
	private static final int WDT_HOUR_MED_FLAT  = 42;
	private static final int WDT_MIN_MED_FLAT   = 42;
	private static final int WDT_SEC_MED_FLAT   = 42;
	
	private static final int[] PATTERN_WIDTHS_FLAT = {WDT_DAY_MED_FLAT, WDT_MONTH_MED_FLAT, WDT_YEAR_MED_FLAT
                                                     ,WDT_HOUR_MED_FLAT, WDT_MIN_MED_FLAT, WDT_SEC_MED_FLAT};
	
	private static final Range<Integer> RNG_DAY   = new Range<Integer>(0,  1, 31);
	private static final Range<Integer> RNG_MONTH = new Range<Integer>(0,  1, 12);
	private static final Range<Integer> RNG_YEAR  = new Range<Integer>(new GregorianCalendar().get(Calendar.YEAR),-20, 20); //RELATIVE TO THE CURRENT DATE BEFORE/AFTER
	private static final Range<Integer> RNG_HOUR  = new Range<Integer>(0,  0, 23);
	private static final Range<Integer> RNG_MIN   = new Range<Integer>(0,  0, 59);
	private static final Range<Integer> RNG_SEG   = new Range<Integer>(0,  0, 59);
	
	private static final int[]	PATTERN_VALUE_INTERVALS	= {1, 1, 1, 1, 5, 5};
	
	@SuppressWarnings("rawtypes")
	private static final Range[] PATTERN_RANGES = {RNG_DAY, RNG_MONTH, RNG_YEAR
                                                  ,RNG_HOUR, RNG_MIN, RNG_SEG};
	
	private List<ComboSwitch> widgets = new ArrayList<ComboSwitch>();
	
	private Calendar cal = new GregorianCalendar();
	
	private boolean flat;
	private String	pattern;

	private String [] patternSplit;
	private Integer[] patternFrags;
	
	public DateSelectorCombo(Composite parent, int style)
	{
		this(parent, style, "dd/MM/yyyy HH:mm:ss");
	}
	
	public DateSelectorCombo(Composite parent, int style, String format)
	{
		super(parent, style);
		flat = (style & SWT.FLAT) == SWT.FLAT;
		pattern = format;
		
		patternSplit = splitMatchFragments   (pattern     , PATTERN_FRAGMENTS);
		patternFrags = selectPatternFragments(patternSplit, PATTERN_FRAGMENTS);
		
		for(int i = 0; i < patternFrags.length; i++)
		{
			widgets.add(null);
		}
		
		createContents();
	}
	
	private Integer[] selectPatternFragments(String[] split, String[] avaibleFrags)
	{
		List<Integer> returnList = new LinkedList<Integer>();
		
		for(String splitCurr : split)
		{
			if(splitCurr != null && !splitCurr.isEmpty())
			{
				for(int j = 0; j < avaibleFrags.length; j++)
				{
					if(splitCurr.equals(avaibleFrags[j]))
					{
						returnList.add(j);
						break;
					}
				}
			}
		}
		return returnList.toArray(new Integer[]{});
	}

	private void createContents()
	{
		GridLayout gridLayout = new GridLayout(patternSplit.length, false);
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth  = 0;
		setLayout(gridLayout);
		
		for (String split : patternSplit)
		{
			int fragIndex = Arrays.asList(PATTERN_FRAGMENTS).indexOf(split);
			if(fragIndex >= 0)
			{
				createDateFragmentCombo(this, fragIndex);
			}
			else
			{
				Label lblDelimiter = new Label(this, SWT.NONE);
				lblDelimiter.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
				lblDelimiter.setText(split);
			}
		}
	}
	
	public static String[] splitMatchFragments(String original, String... frags)
	{
		List<String> returnList = new LinkedList<String>();
		
		String matchFrag;
		int minIndex;
		do
		{
			matchFrag = null;
			minIndex  = original.length();
			
			for(String frag : frags)
			{
				int fragIndex = original.indexOf(frag);
				if(fragIndex != -1 && fragIndex < minIndex)
				{
					minIndex  = fragIndex;
					matchFrag = frag;
				}
			}
			
			if(matchFrag != null)
			{
				String firstPart = original.substring(0, minIndex);
				if(!firstPart.isEmpty())
				{
					returnList.add(firstPart);
				}
				
				returnList.add(matchFrag);
				
				original = original.substring(minIndex + matchFrag.length());
			}
		}
		while(matchFrag != null);
		
		return returnList.toArray(new String[]{});
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
		
		for(ComboSwitch widget : widgets)
		{
			widget.setText("");
		}
	}
	
	public void addModifyListener(ModifyListener listener)
	{
		checkWidget();
		TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.Modify, typedListener);
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
	
	@SuppressWarnings("unchecked")
	private void createDateFragmentCombo(Composite parent, final int fragIndex)
	{
		GridData gldCmbDay = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		
		ComboSwitch cmbFragment = null;
		if(!flat)
		{
			cmbFragment = new ComboSwitch(new Combo(parent, SWT.NONE));
			gldCmbDay.widthHint = PATTERN_WIDTHS_NATIVE[fragIndex];
		}
		else
		{
			cmbFragment = new ComboSwitch(new CCombo(parent, SWT.NONE));
			gldCmbDay.widthHint = PATTERN_WIDTHS_FLAT[fragIndex];
		}
		widgets.set(Arrays.binarySearch(patternFrags, fragIndex), cmbFragment);
		
		cmbFragment.setLayoutData(gldCmbDay);
		
		cmbFragment.setVisibleItemCount(20);
		cmbFragment.setItems(createNumStrRange(PATTERN_RANGES[fragIndex], PATTERN_VALUE_INTERVALS[fragIndex], PATTERN_FORMATS[fragIndex]));
		cmbFragment.addListener(SWT.Modify, new Listener()
		{
			@Override
			public void handleEvent(Event e)
			{
				int index = Arrays.binarySearch(patternFrags, fragIndex);
				if(widgets.get(index).getText().length() >= PATTERN_FORMATS[fragIndex].getMinimumIntegerDigits())
				{
					ComboSwitch next = null;
					if(index >= (widgets.size() - 1))
					{
						next = widgets.get(0);
					}
					else
					{
						next = widgets.get(index + 1);
					}
					next.forceFocus();
				}
			}
		});
		cmbFragment.addListener(SWT.FocusOut, new Listener()
		{
			@Override
			public void handleEvent(Event e)
			{
				int item = Arrays.binarySearch(patternFrags, fragIndex);
				ComboSwitch widget = widgets.get(item);
				select(widget, !widget.getText().isEmpty() ? FMT_DAY_MED.format( Integer.parseInt(widget.getText()) ) : "", false);
			}
		});
		cmbFragment.addListener(SWT.KeyDown, new Listener()
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
	
	private void formatDate()
	{
		for(int i = 0; i < patternFrags.length; i++)
		{
			int index = patternFrags[i];
			String fmted = PATTERN_FORMATS[index].format(cal.get(CALENDAR_TYPES[index]) + (index == POS_MONTH ? 1 : 0));
			select(widgets.get(i), fmted, true);
		}
	}
	
	private void parseDate()
	{
		try
		{
			Date old = cal.getTime();
			
			for(int i = 0; i < patternFrags.length; i++)
			{
				int index = patternFrags[i];
				cal.set(CALENDAR_TYPES[index], !widgets.get(i).getText().isEmpty() ? PATTERN_FORMATS[index].parse(widgets.get(i).getText()).intValue() + (index == POS_MONTH ? -1 : 0) : 0);
			}
			
			changeSupport.firePropertyChange(PROP_DATE, old, cal.getTime());
		}
		catch (ParseException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	private void select(ComboSwitch cmb, String txt, boolean formating)
	{
		Integer index = retrieveSelectionIndex(cmb, txt);
		if(index != null) cmb.select(index); else cmb.setText(txt);
		if(!formating) parseDate();
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
	
	private static String[] createNumStrRange(Range<Integer> range, int increment, DecimalFormat format)
	{
		String[] result = new String[(range.getUpperBound()/increment)-range.getLowerBound()+1];
		for(int i = 0; i < result.length; i++)
		{
			result[i] = format.format(range.getRefereceValue() + range.getLowerBound() + i * increment);
		}
		return result;
	}
}