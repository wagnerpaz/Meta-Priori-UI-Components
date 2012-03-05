package com.metapriori.ui.flexcolumns.measure;

/**
 * @author Wagner Paz
 * @since  1.0
 */
public class LengthFormat
{
	/**
	 * @param str
	 * @return
	 */
	public static FlexLength parse(String str)
	{
		for(LengthMeasure measure : LengthMeasure.values())
		{
			int indexOf = str.indexOf(measure.getId());
			if( indexOf >= 0)
			{
				String valuePart = str.substring(0, indexOf).trim();
				double value = Double.parseDouble(valuePart);
				
				return new FlexLength(value, measure);
			}
		}
		throw new IllegalArgumentException("Formato de comprimento inválido");
	}
	
	/**
	 * TODO
	 * @param length
	 * @return
	 */
	public static String format(FlexLength length)
	{
		return "";
	}
}
