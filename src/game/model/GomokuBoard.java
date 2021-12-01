package game.model;

import java.util.ArrayList;

import game.settings.GS;

public final class GomokuBoard {
	private final int[][] game_table;
	private int pawns = (GS.GRIDSIZE - 1) * (GS.GRIDSIZE - 1);

	public GomokuBoard() {
		this.game_table = new int[GS.GRIDSIZE - 1][GS.GRIDSIZE - 1];
	}

	public boolean hasEmptyCell() {
		return pawns > 0;
	}

	public int getCell(int row, int col) {
		if (row < 0 || row >= GS.GRIDSIZE) {
			return -1;
		}
		if (col < 0 || col >= GS.GRIDSIZE) {
			return -1;
		}

		return game_table[row][col];
	}

	public void setCell(int row, int col, int p) {
		game_table[row][col] = p;
		--pawns;
	}

	public ArrayList<Pawn> printState() {
		ArrayList<Pawn> state = new ArrayList<>();
		for (int row = 0; row < game_table.length; row++) {
			for (int col = 0; col < game_table.length; col++) {
				if (game_table[row][col] > 0)
					state.add(new Pawn(row, col, game_table[row][col]));
			}
		}

		return state;
	}
}
