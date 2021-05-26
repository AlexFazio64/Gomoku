package game.model;

import game.settings.GS;

public class Referee {
	private final int[][] board;
	private final int[][] banned;
	private Player p1, p2;
	private Player next;
	
	public Referee(GomokuLogic logic, Player p1, Player p2) {
		this.board = logic.getGame_Table();
		this.banned = new int[GS.GRIDSIZE - 1][GS.GRIDSIZE - 1];
		
		this.p1 = p1;
		this.p2 = p2;
		next = p1;
		
		for (int[] rows: banned) {
			for (int col: rows) {
				col = -1;
			}
		}
		
		//first move is always the center
		banned[( GS.GRIDSIZE - 2 ) / 2][( GS.GRIDSIZE - 2 ) / 2] = 0;
	}
	
	public int judgeMove(int row, int col) {
		return 0;
	}
	
	private boolean ThreeAndThree(int r, int c) {
		return false;
	}
	
	private boolean FourAndFour(int r, int c) {
		return false;
	}
}
