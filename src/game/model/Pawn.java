package game.model;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("pawn")
public class Pawn {
	@Param(0)
	private int x;
	@Param(1)
	private int y;
	@Param(2)
	private int p;
	
	public Pawn() {
	}
	
	public Pawn(int xx, int yy, int pp) {
		this.x = xx;
		this.y = yy;
		this.p = pp;
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
		return "Pawn{" + "x=" + x + ", y=" + y + ", p=" + p + '}';
	}
}
