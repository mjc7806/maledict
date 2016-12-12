/*
 * Maledict - An Interactive Tool for Learning Linear and Differential Cryptanalysis of SPNs
 * Copyright (C) 2016  Mike Carpenter
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
		
		res.append("<h4>S-box (")
		   .append(boxRow)
		   .append(", ")
		   .append(boxCol)
		   .append(")</h4>\n")
		   .append("<table>\n")
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
