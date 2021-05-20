package game.model;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("placed")
public class Placed {
	@Param(0)
	private int row;
	@Param(1)
	private int col;
	@Param(2)
	private int val;
	
	public Placed() {
	}
	
	public int getRow() {
		return row;
	}
	
	public void setRow(int row) {
		this.row = row;
	}
	
	public int getCol() {
		return col;
	}
	
	public void setCol(int col) {
		this.col = col;
	}
	
	public int getVal() {
		return val;
	}
	
	public void setVal(int val) {
		this.val = val;
	}
	
	@Override
	public String toString() {
		return "placed(" + row + "," + col + "," + val + ')';
	}
}
