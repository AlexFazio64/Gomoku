package game.model;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("placed")
public class Placed {
	@Param(0)
	private int x;
	@Param(1)
	private int y;
	@Param(2)
	private int p;
	
	public Placed() {
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getP() {
		return p;
	}
	
	public void setP(int p) {
		this.p = p;
	}
	
	@Override
	public String toString() {
		return "Placed{" + "x=" + x + ", y=" + y + ", p=" + p + '}';
	}
}
