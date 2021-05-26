package game.model;

import game.settings.GS;

public class Referee {
	private final int[][] board;
	private final int[][] banned;
	
	public Referee(GomokuLogic logic) {
		board = logic.getGame_Table();
		banned = logic.getGame_Table();
		
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
