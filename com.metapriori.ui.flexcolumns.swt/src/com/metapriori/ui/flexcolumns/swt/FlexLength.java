package com.metapriori.ui.flexcolumns.swt;

/**
 * @author Wagner Paz
 * @since  1.0
 */
public class FlexLength
{
	private Double        value;
	private LengthMeasure measure;
	
	public FlexLength(Double value, LengthMeasure measure)
	{
		this.value = value;
		this.measure = measure;
	}
	
	@Override
	public String toString()
	{
		if(value == null || measure == null)
		{
			return null;
		}
		
		return value + "" + measure.getId();
	}

	public Double getValue()
	{
		return value;
	}

	public void setValue(Double value)
	{
		this.value = value;
	}

	public LengthMeasure getMeasure()
	{
		return measure;
	}

	public void setMeasure(LengthMeasure measure)
	{
		this.measure = measure;
	}
}