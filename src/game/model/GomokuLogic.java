package game.model;

import game.settings.GS;
import javafx.geometry.Point2D;

import java.util.ArrayList;

public final class GomokuLogic {
	private final int[][] game_table;
	private int pawns = ( GS.GRIDSIZE - 1 ) * ( GS.GRIDSIZE - 1 );
	
	public GomokuLogic() {
		this.game_table = new int[GS.GRIDSIZE - 1][GS.GRIDSIZE - 1];
	}
	
	public int[][] getGame_Table() {
		return game_table;
	}
	
	public boolean hasEmptyCell() {
		return pawns > 0;
	}
	
	public int getCell(int row, int col) {
		if ( row < 0 || row >= GS.GRIDSIZE ) {
			return 1;
		}
		if ( col < 0 || col >= GS.GRIDSIZE ) {
			return 1;
		}
		return game_table[col][row];
	}
	
	public ArrayList<Point2D[]> setCell(int row, int col, int p) {
		--row;
		--col;
		
		game_table[row][col] = p;
		--pawns;
		
		return checkVictory(row, col);
	}
	
	public ArrayList<Point2D[]> checkVictory(int row, int col) {
		ArrayList<Point2D[]> result = new ArrayList<>();
		result.add(getVerticalCount(row, col));
		result.add(getHorizontalCount(row, col));
		result.add(getMainDiagCount(row, col));
		result.add(getSecDiagCount(row, col));
		return result;
	}
	
	/**
	 * searches for similar pawns from the starting position
	 * starts from starting position, then goes up the vertical line and automatically
	 * stops when there are no more pawns or cells in the matrix.
	 * it goes straight down from the highest position previously found and counts the
	 * similar pawns.
	 *
	 * @param row starting position
	 * @param col starting position
	 *
	 * @return null if player hasn't scored 5 in a row, or the start and end position
	 * for the line
	 */
	private Point2D[] getVerticalCount(int row, int col) {
		int cr = row;
		int count = 0;
		
		while (true) {
			--cr;
			if ( cr < 0 || count == 4 ) {
				break;
			}
			if ( game_table[row][col] != game_table[cr][col] ) {
				break;
			} else {
				++count;
			}
		}
		
		cr = count == 0 ? row : Math.max(0, cr + 1);
		
		Point2D start = new Point2D(cr, col);
		Point2D end = new Point2D(cr, col);
		
		count = 0;
		for (int i = 0; i < Math.min(5, ( GS.GRIDSIZE - 1 ) - cr); i++) {
			if ( game_table[row][col] == game_table[cr + i][col] ) {
				++count;
				end = end.multiply(0);
				end = end.add(cr + i, col);
			} else {
				break;
			}
		}
		return count == 5 ? new Point2D[]{start, end} : null;
	}
	
	/**
	 * searches for similar pawns from the starting position
	 * starts from starting position, then goes up the primary diagonal and
	 * automatically stops when there are no more pawns or cells in the matrix.
	 * it goes down the primary diagonal from the highest position previously found
	 * and counts the similar pawns.
	 *
	 * @param row starting position
	 * @param col starting position
	 *
	 * @return null if player hasn't scored 5 in a row, or the start and end position
	 * for the line
	 */
	private Point2D[] getMainDiagCount(int row, int col) {
		int cr = row;
		int cc = col;
		int count = 0;
		
		while (true) {
			--cr;
			--cc;
			if ( cr < 0 || cc < 0 || count == 4 ) {
				break;
			}
			if ( game_table[row][col] != game_table[cr][cc] ) {
				break;
			} else {
				++count;
			}
		}
		
		cr = count == 0 ? row : Math.max(0, cr + 1);
		cc = count == 0 ? col : Math.max(0, cc + 1);
		
		Point2D start = new Point2D(cr, cc);
		Point2D end = new Point2D(cr, cc);
		
		count = 0;
		for (int i = 0; i < Math.min(Math.min(5, ( GS.GRIDSIZE - 1 ) - cr), Math.min(5, ( GS.GRIDSIZE - 1 ) - cc)); i++) {
			if ( game_table[row][col] == game_table[cr + i][cc + i] ) {
				++count;
				end = end.multiply(0);
				end = end.add(cr + i, cc + i);
			} else {
				break;
			}
		}
		return count == 5 ? new Point2D[]{start, end} : null;
	}
	
	/**
	 * searches for similar pawns from the starting position
	 * starts from starting position, then goes up the secondary diagonal and
	 * automatically stops when there are no more pawns or cells in the matrix.
	 * it goes down the secondary diagonal from the highest position previously found
	 * and counts the similar pawns.
	 *
	 * @param row starting position
	 * @param col starting position
	 *
	 * @return null if player hasn't scored 5 in a row, or the start and end position
	 * for the line
	 */
	private Point2D[] getSecDiagCount(int row, int col) {
		int cr = row;
		int cc = col;
		int count = 0;
		
		while (true) {
			--cr;
			++cc;
			if ( cr < 0 || cc >= ( GS.GRIDSIZE - 1 ) || count == 4 ) {
				break;
			}
			if ( game_table[row][col] != game_table[cr][cc] ) {
				break;
			} else {
				++count;
			}
		}
		cr = count == 0 ? row : Math.max(0, cr + 1);
		cc = count == 0 ? col : Math.min(GS.GRIDSIZE - 2, cc - 1);
		
		Point2D start = new Point2D(cr, cc);
		Point2D end = new Point2D(cr, cc);
		
		count = 0;
		
		int i = 0;
		while (true) {
			if ( ( cr + i ) >= GS.GRIDSIZE - 1 || cc - i < 0 || count == 5 ) {
				break;
			}
			if ( game_table[row][col] == game_table[cr + i][cc - i] ) {
				++count;
				end = end.multiply(0);
				end = end.add(cr + i, cc - i);
			} else {
				break;
			}
			++i;
		}
		
		return count == 5 ? new Point2D[]{start, end} : null;
	}
	
	/**
	 * searches for similar pawns from the starting position
	 * starts from starting position, then goes left in the horizontal line and
	 * automatically stops when there are no more pawns or cells in the matrix.
	 * it goes right the highest position previously found and counts the
	 * similar pawns.
	 *
	 * @param row starting position
	 * @param col starting position
	 *
	 * @return null if player hasn't scored 5 in a row, or the start and end position
	 * for the line
	 */
	private Point2D[] getHorizontalCount(int row, int col) {
		int cc = col;
		int count = 0;
		
		while (true) {
			--cc;
			if ( cc < 0 || count == 4 ) {
				break;
			}
			if ( game_table[row][col] != game_table[row][cc] ) {
				break;
			} else {
				++count;
			}
		}
		
		cc = count == 0 ? col : Math.max(0, cc + 1);
		
		Point2D start = new Point2D(row, cc);
		Point2D end = new Point2D(row, cc);
		
		count = 0;
		for (int i = 0; i < Math.min(5, ( GS.GRIDSIZE - 1 ) - cc); i++) {
			if ( game_table[row][col] == game_table[row][cc + i] ) {
				++count;
				end = end.multiply(0);
				end = end.add(row, cc + i);
			} else {
				break;
			}
		}
		return count == 5 ? new Point2D[]{start, end} : null;
	}
	
	public boolean isLegalMove(int cx, int cy, int player) {
//		System.out.println("checking with " + cx + " " + cy);
		return getCell(cx - 1, cy - 1) == 0;
	}
}
