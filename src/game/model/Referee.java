package game.model;

import game.settings.GS;
import javafx.geometry.Point2D;

public class Referee {
	private final GomokuLogic game;
	private final int[][] banned;
	private Player p1, p2;
	private Player next;
	private Player current;
	
	//implement player switching logic somehow
	
	public Referee(GomokuLogic logic, Player p1, Player p2) {
		this.game = logic;
		this.banned = new int[GS.GRIDSIZE - 1][GS.GRIDSIZE - 1];
		
		this.p1 = p1;
		this.p2 = p2;
		
		current = p2;
		next = p1;
		
		for (int[] rows: banned) {
			for (int col: rows) {
				col = -1;
			}
		}
		
		//first move is always the center
		banned[( GS.GRIDSIZE - 2 ) / 2][( GS.GRIDSIZE - 2 ) / 2] = 0;
	}
	
	public Player getNextPlayer() {
		Player swap = current;
		current = next;
		next = swap;
		return current;
	}
	
	public int judgeMove(int row, int col) {
		System.out.println("player wants this position: " + row + "," + col);
		return -1;
	}
	
	//TODO implement rule checking
	private boolean ThreeAndThree(int r, int c) {
		return false;
	}
	
	private boolean FourAndFour(int r, int c) {
		return false;
	}
	
	public Player getCurrentPlayer() {
		return current;
	}
	
	public boolean isWinningMove(int last_row, int last_col) {
		return false;
	}
	
	public Point2D[] getWinningLine() {
		return null;
	}
}
