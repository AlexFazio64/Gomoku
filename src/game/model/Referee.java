package game.model;

import game.settings.GS;

public class Referee {
	private final GomokuLogic game;
	private final int[][] banned;
	private Player next;
	private Player current;
	
	//implement player switching logic somehow
	
	public Referee(GomokuLogic logic, Player p1, Player p2) {
		this.game = logic;
		this.banned = new int[GS.GRIDSIZE - 1][GS.GRIDSIZE - 1];
		
		current = p1;
		next = p2;
		
		for (int[] rows: banned) {
			for (int col: rows) {
				col = -1;
			}
		}
		
		//first move is always the center
		banned[( GS.GRIDSIZE - 2 ) / 2][( GS.GRIDSIZE - 2 ) / 2] = 0;
	}
	
	public void switchPlayer() {
		Player swap = current;
		current = next;
		next = swap;
	}
	
	public int judgeMove(int row, int col) {
		if ( game.getCell(row, col) != 0 ) {
			return -1;
		} else if ( ThreeAndThree(row, col) ) {
			return 3;
		} else if ( FourAndFour(row, col) ) {
			return 4;
		}
		return 0;
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
}
