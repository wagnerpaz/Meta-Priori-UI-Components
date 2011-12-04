package com.metapriori.ui.dateselector.swt;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;

import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.value.AbstractObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;

/**
 * @author Wagner Paz
 * @since  1.0
 */
public class DateSelectorComboObservableValue extends AbstractObservableValue
{
	/**
     * The Control being observed here.
     */
	protected final DateSelectorCombo dateSelectorCombo;

	/**
	 * Flag to prevent infinite recursion in {@link #doSetValue(Object)}.
	 */
	protected boolean updating = false;

	/**
	 * The "old" selection before a selection event is fired.
	 */
	protected Date currentSelection;

	/**
	 * Old value
	 */
	private Date oldValue;

	/**
	 * Observe the selection property of the provided CDateTime control.
	 * 
	 * @param dateSelectorCombo the control to observe
	 */
	public DateSelectorComboObservableValue(DateSelectorCombo dateChooserCombo)
	{
		super(SWTObservables.getRealm(dateChooserCombo.getDisplay()));
		
		this.dateSelectorCombo = dateChooserCombo;
		this.currentSelection  = dateChooserCombo.getDate();
		
		dateChooserCombo.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e)
			{
				DateSelectorComboObservableValue.this.dispose();
			}
		});
		dateChooserCombo.addPropertyChangeListener(DateSelectorCombo.PROP_DATE, new PropertyChangeListener()
		{
			@Override
			public void propertyChange(PropertyChangeEvent e)
			{
				if (!updating)
				{
					Date newValue = dateSelectorCombo.getDate();

					if (!newValue.equals(oldValue))
					{
						fireValueChange(Diffs.createValueDiff(oldValue, newValue));
						oldValue = newValue;
					}
				}
			}
		});
	}

	@Override
	public Object getValueType()
	{
		return Date.class;
	}

	@Override
	protected Object doGetValue()
	{
		return dateSelectorCombo.getDate();
	}

	@Override
	protected void doSetValue(Object value)
	{
		try
		{
			updating = true;
			
			oldValue = dateSelectorCombo.getDate();
			Date newValue = (Date) value;
			dateSelectorCombo.setDate(newValue);
			currentSelection = newValue;
			
			fireValueChange(Diffs.createValueDiff(oldValue, newValue));
		}
		finally
		{
			updating = false;
		}
	}

}