package com.metapriori.ui.flexcolumns.swt;

import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TreeColumn;

import com.metapriori.ui.flexcolumns.measure.FlexLength;
import com.metapriori.ui.flexcolumns.measure.LengthFormat;
import com.metapriori.ui.flexcolumns.measure.LengthMeasure;

/**
 * <p>Define as propriedades de uma coluna flexível.</p>
 * <p>Os objetos desta classe devem ser registrados e
 * manipulados por uma instância de <code>FlexColumnManager</code></p>
 * 
 * @author Wagner Paz
 * @since  1.0
 * @see com.metapriori.ui.flexcolumns.swt.FlexColumnLayout
 */
public class FlexColumnData
{
	private FlexLength preferredLength;
	private FlexLength minLength;
	private boolean    visible = true;
	
	private TableColumn tableColumn;
	private TreeColumn  treeColumn;
	
	/**
	 * <p>Cria uma definição de coluna flexível afim de especificar
	 * as medidas desejadas para uma <code>TableColumn</code>.</p>
	 * 
	 * <p><b>Definição de comprimento</b>
	 * <br>Os comprimentos são especificados utilizando uma <code>String</code>, contendo
	 * a quantidade, seguida de um identificador de medida.
	 * <br>Ex.: "355px", "100%"</p>
	 * 
	 * @param tableColumn     A coluna (de uma tabela) que se deseja controlar o comprimento.
	 * @param preferredLength String que define o <i>comprimento desejado</i> da coluna.
	 * @param minLength       String que define o <i>comprimento mínimo</i> da coluna.
	 * 
	 * @see org.eclipse.swt.widgets.TableColumn
	 */
	public FlexColumnData(TableColumn tableColumn, String preferredLength, String minLength)
	{
		this(tableColumn, LengthFormat.parse(preferredLength), LengthFormat.parse(minLength));
	}
	
	/**
	 * <p>Cria uma definição de coluna flexível afim de especificar
	 * as medidas desejadas para uma <code>TableColumn</code>.</p>
	 * <p><i>!Construtor alternativo que evita definição de comprimento mínimo,
	 * utilizando como padrão 0px !</i></p>
	 * 
	 * <p><b>Definição de comprimento</b>
	 * <br>Os comprimentos são especificados utilizando uma <code>String</code>, contendo
	 * a quantidade, seguida de um identificador de medida.
	 * <br>Ex.: "355px", "100%"</p>
	 * 
	 * @param tableColumn     A coluna (de uma tabela) que se deseja controlar o comprimento.
	 * @param preferredLength String que define o <i>comprimento desejado</i> da coluna.
	 * 
	 * @see org.eclipse.swt.widgets.TableColumn
	 */
	public FlexColumnData(TableColumn tableColumn, String preferredLength)
	{
		this(tableColumn, LengthFormat.parse(preferredLength), new FlexLength(0d, LengthMeasure.PIXEL));
	}
	
	/**
	 * <p>Cria uma definição de coluna flexível afim de especificar
	 * as medidas desejadas para uma <code>TreeColumn</code>.</p>
	 * 
	 * <p><b>Definição de comprimento</b>
	 * <br>Os comprimentos são especificados utilizando uma <code>String</code>, contendo
	 * a quantidade, seguida de um identificador de medida.
	 * <br>Ex.: "355px", "100%"</p>
	 * 
	 * @param treeColumn      A coluna (de uma árvore) que se deseja controlar o comprimento.
	 * @param preferredLength String que define o <i>comprimento desejado</i> da coluna.
	 * @param minLength       String que define o <i>comprimento mínimo</i> da coluna.
	 * 
	 * @see org.eclipse.swt.widgets.TreeColumn
	 */
	public FlexColumnData(TreeColumn treeColumn, String preferredLength, String minLength)
	{
		this(treeColumn, LengthFormat.parse(preferredLength), LengthFormat.parse(minLength));
	}
	
	/**
	 * <p>Cria uma definição de coluna flexível afim de especificar
	 * as medidas desejadas para uma <code>TreeColumn</code>.</p>
	 * <p><i>!Construtor alternativo que evita definição de comprimento mínimo,
	 * utilizando como padrão 0px !</i></p>
	 * 
	 * <p><b>Definição de comprimento</b>
	 * <br>Os comprimentos são especificados utilizando uma <code>String</code>, contendo
	 * a quantidade, seguida de um identificador de medida.
	 * <br>Ex.: "355px", "100%"</p>
	 * 
	 * @param treeColumn     A coluna (de uma árvore) que se deseja controlar o comprimento.
	 * @param preferredLength String que define o <i>comprimento desejado</i> da coluna.
	 * 
	 * @see org.eclipse.swt.widgets.TreeColumn
	 */
	public FlexColumnData(TreeColumn treeColumn, String preferredLength)
	{
		this(treeColumn, LengthFormat.parse(preferredLength), new FlexLength(0d, LengthMeasure.PIXEL));
	}
	
	/**
	 * Cria uma definição de coluna flexível afim de especificar
	 * as medidas desejadas para uma <code>TableColumn</code>.
	 * 
	 * @param tableColumn      A coluna (de uma tabela) que se deseja controlar o comprimento.
	 * @param preferredLength  O <i>comprimento desejado</i> da coluna.
	 * @param minLength        O <i>comprimento mínimo</i> da coluna.
	 * 
	 * @see org.eclipse.swt.widgets.TableColumn
	 */
	FlexColumnData(TableColumn tableColumn, FlexLength preferredLength, FlexLength minLength)
	{
		this.tableColumn     = tableColumn;
		this.preferredLength = preferredLength;
		this.minLength       = minLength;
	}
	
	/**
	 * Cria uma definição de coluna flexível afim de especificar
	 * as medidas desejadas para uma <code>TreeColumn</code>.
	 * 
	 * @param treeColumn       A coluna (de uma árvore) que se deseja controlar o comprimento.
	 * @param preferredLength  O <i>comprimento desejado</i> da coluna.
	 * @param minLength        O <i>comprimento mínimo</i> da coluna.
	 * 
	 * @see org.eclipse.swt.widgets.TreeColumn
	 */
	FlexColumnData(TreeColumn treeColumn, FlexLength preferredLength, FlexLength minLength)
	{
		this.treeColumn       = treeColumn;
		this.preferredLength  = preferredLength;
		this.minLength        = minLength;
	}
	
	public FlexLength getPreferredLength()
	{
		return preferredLength;
	}

	public void setPreferredLength(FlexLength preferredLength)
	{
		this.preferredLength = preferredLength;
	}

	public FlexLength getMinLength()
	{
		return minLength;
	}

	public void setMinLength(FlexLength minLength)
	{
		this.minLength = minLength;
	}

	public TableColumn getTableColumn()
	{
		return tableColumn;
	}

	public void setTableColumn(TableColumn tableColumn)
	{
		this.tableColumn = tableColumn;
	}

	public TreeColumn getTreeColumn()
	{
		return treeColumn;
	}

	public void setTreeColumn(TreeColumn treeColumn)
	{
		this.treeColumn = treeColumn;
	}

	public boolean isVisible()
	{
		return visible;
	}

	public void setVisible(boolean visible)
	{
		this.visible = visible;
	}
}