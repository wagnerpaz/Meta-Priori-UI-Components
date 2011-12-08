#Meta Priori UI Components

This project consists in a set of independent high-level GUI components built,
with SWT and JFace atop, to assist in the development of Eclipse RCP applications.

###Avaible components (documented below):

* FlexColumns
* DateSelectorCombo


##FlexColumns

   A table extension that provides additional features for column definition and user interaction.

###Features:
   
   * Dynamic column width. It's possible to define the column's preferred and minimum size using percent or pixel values.
   * Dynamic column resize. The dynamic widths are recalculated as the table or other columns are resized.
   * Show/hide control. The user can choose to show or hide the avaible columns from a popup menu on the table header.
   * The table state is automatically persisted in a file and loaded when the table is showed for the first time.

##DateSelectorCombo

   A date selection component based on three separeted combos, one for day, month and year input.

###Features:
   
   * Keyboard input (digit-only).
   * Pre populated list of suggested values for each combo.
   * Automatic focus on the next combo when input is complete.
   * JFace Databinding support.