package com.metapriori.ui.flexcolumns.swt;

/**
 * @author Wagner Paz
 * @since  1.0
 */
public enum LengthMeasure
{
	PIXEL     ("px"),
	PERCENTAGE("%");
	
	private String id;

	/**
	 * @param id
	 */
	private LengthMeasure(String id)
	{
		this.id = id;
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static LengthMeasure fromId(String id)
	{
		for(LengthMeasure measure : LengthMeasure.values())
		{
			if( measure.getId().equals(id) )
			{
				return measure;
			}
		}
		return null;
	}

	public String getId()
	{
		return id;
	}
}