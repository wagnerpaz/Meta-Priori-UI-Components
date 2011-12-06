package com.metapriori.ui.dateselector.swt;

import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Listener;

/**
 * @author Wagner Paz
 * @since  1.0
 */
public class ComboSwitch
{
	private Combo  cmbNative;
	private CCombo cmbFlat;

	public ComboSwitch(Combo cmb)
	{
		this.cmbNative = cmb;
	}
	
	public ComboSwitch(CCombo cmb)
	{
		this.cmbFlat = cmb;
	}
	
	public void setItems(String[] items)
	{
		if(cmbNative != null)
		{
			cmbNative.setItems(items);
		}
		else if(cmbFlat != null)
		{
			cmbFlat.setItems(items);
		}
	}

	public String getText()
	{
		if(cmbNative != null)
		{
			return cmbNative.getText();
		}
		else if(cmbFlat != null)
		{
			return cmbFlat.getText();
		}
		return null;
	}

	public void setText(String txt)
	{
		if(cmbNative != null)
		{
			cmbNative.setText(txt);
		}
		else if(cmbFlat != null)
		{
			cmbFlat.setText(txt);
		}
	}

	public String[] getItems()
	{
		if(cmbNative != null)
		{
			return cmbNative.getItems();
		}
		else if(cmbFlat != null)
		{
			return cmbFlat.getItems();
		}
		return null;
	}

	public void select(Integer index)
	{
		if(cmbNative != null)
		{
			cmbNative.select(index);
		}
		else if(cmbFlat != null)
		{
			cmbFlat.select(index);
		}
	}

	public void addListener(int modify, Listener listener)
	{
		if(cmbNative != null)
		{
			cmbNative.addListener(modify, listener);
		}
		else if(cmbFlat != null)
		{
			cmbFlat.addListener(modify, listener);
		}
	}

	public void setLayoutData(GridData gldCmbDay)
	{
		if(cmbNative != null)
		{
			cmbNative.setLayoutData(gldCmbDay);
		}
		else if(cmbFlat != null)
		{
			cmbFlat.setLayoutData(gldCmbDay);
		}
	}

	public void forceFocus()
	{
		if(cmbNative != null)
		{
			cmbNative.forceFocus();
		}
		else if(cmbFlat != null)
		{
			cmbFlat.forceFocus();
		}
	}

	public void traverse(int traverseTabNext)
	{
		if(cmbNative != null)
		{
			cmbNative.traverse(traverseTabNext);
		}
		else if(cmbFlat != null)
		{
			cmbFlat.traverse(traverseTabNext);
		}		
	}

	public void setVisibleItemCount(int i)
	{
		if(cmbNative != null)
		{
			cmbNative.setVisibleItemCount(i);
		}
		else if(cmbFlat != null)
		{
			cmbFlat.setVisibleItemCount(i);
		}		
	}
}