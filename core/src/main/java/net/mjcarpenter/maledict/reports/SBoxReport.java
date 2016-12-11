package net.mjcarpenter.maledict.reports;

public class SBoxReport
{
	private final boolean isActive;
	private final int[][] table;
	private final int     selectedRow;
	private final int     selectedColumn;
	private final int     boxRow;
	private final int     boxCol;
	
	public SBoxReport(int[][] table, int selectedRow, int selectedColumn, int boxRow, int boxCol, boolean isActive)
	{
		this.table          = table;
		this.selectedRow    = selectedRow;
		this.selectedColumn = selectedColumn;
		this.isActive       = isActive;
		this.boxRow         = boxRow;
		this.boxCol         = boxCol;
	}
	
	public int[][] getTable()
	{
		return table;
	}
	
	public int getSelectedRow()
	{
		return selectedRow;
	}
	
	public int getSelectedColumn()
	{
		return selectedColumn;
	}
	
	public int getSelectedBias()
	{
		return table[selectedRow][selectedColumn];
	}
	
	public int getBoxRow()
	{
		return boxRow;
	}
	
	public int getBoxCol()
	{
		return boxCol;
	}
	
	public boolean isActive()
	{
		return isActive;
	}
	
	public String constructApproximationTable()
	{
		StringBuilder res = new StringBuilder();
		
		res.append("<table>\n")
		   .append("\t<tr>\n\t\t<th colspan=\"")
		   .append(table.length)
		   .append("\">Approximation Table</th>\n\t</tr>\n");
		
		for(int i=-1; i<table.length; i++)
		{
			res.append("\t<tr>\n")
			   .append("\t\t<td>")
			   .append("<strong>")
			   .append(i == -1 ? " " : Integer.toHexString(i))
			   .append("</strong>")
			   .append("</td>\n");
			
			for(int j=0; j<table[0].length; j++)
			{
				if(i == -1)
				{
					res.append("\t\t<td>")
					   .append("<strong>")
					   .append(Integer.toHexString(j))
					   .append("</strong>")
					   .append("</td>\n");
				}
				else
				{
					res.append("\t\t<td")
					   .append((i==selectedRow && j==selectedColumn) ? " class=\"active\"" : "")
					   .append(">")
					   .append(table[i][j])
					   .append("</td>\n");
				}
			}
			res.append("\t</tr>\n");
		}
		
		res.append("</table>");
		
		return res.toString();
	}
}
