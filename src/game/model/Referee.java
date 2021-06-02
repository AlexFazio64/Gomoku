package game.model;

import game.settings.GS;

public class Referee {
	private final GomokuLogic game;
	private final int[][] banned;
	private Player next;
	private Player current;
	
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
	
	private boolean ThreeAndThree(int r, int c) {
		int id = current.id;
		int grid_bound = GS.GRIDSIZE - 1;
		
		int cell;
		int cnt, v_cnt, h_cnt, m_cnt, s_cnt;
		v_cnt = h_cnt = m_cnt = s_cnt = -1;
		
		int lines = 0;
		
		// north check
		cell = 1;
		cnt = 1;
		while (( r - cell ) >= 0 && cell < 3) {
			if ( game.getCell(r - cell, c) == id ) {
				++cnt;
			} else {
				break;
			}
			++cell;
		}
		System.out.println("N: " + cnt);
		v_cnt += cnt;
		
		//east check
		cell = 1;
		cnt = 1;
		while (( c + cell ) < grid_bound && cell < 3) {
			if ( game.getCell(r, c + cell) == id ) {
				++cnt;
			} else {
				break;
			}
			++cell;
		}
		System.out.println("E: " + cnt);
		h_cnt += cnt;
		
		//south check
		cell = 1;
		cnt = 1;
		while (( r + cell ) < grid_bound && cell < 3) {
			if ( game.getCell(r + cell, c) == id ) {
				++cnt;
			} else {
				break;
			}
			++cell;
		}
		System.out.println("S: " + cnt);
		v_cnt += cnt;
		
		// west check
		cell = 1;
		cnt = 1;
		while (( c - cell ) >= 0 && cell < 3) {
			if ( game.getCell(r, c - cell) == id ) {
				++cnt;
			} else {
				break;
			}
			++cell;
		}
		System.out.println("W: " + cnt);
		h_cnt += cnt;
		
		// north-west check
		cell = 1;
		cnt = 1;
		while (( r - cell ) >= 0 && ( c - cell ) >= 0 && cell < 3) {
			if ( game.getCell(r - cell, c - cell) == id ) {
				++cnt;
			} else {
				break;
			}
			++cell;
		}
		System.out.println("NW: " + cnt);
		m_cnt += cnt;
		
		// north-east check
		cell = 1;
		cnt = 1;
		while (( r - cell ) >= 0 && ( c + cell ) < grid_bound && cell < 3) {
			if ( game.getCell(r - cell, c + cell) == id ) {
				++cnt;
			} else {
				break;
			}
			++cell;
		}
		System.out.println("NE: " + cnt);
		s_cnt += cnt;
		
		// south-east check
		cell = 1;
		cnt = 1;
		while (( r + cell ) < grid_bound && ( c + cell ) < grid_bound && cell < 3) {
			if ( game.getCell(r + cell, c + cell) == id ) {
				++cnt;
			} else {
				break;
			}
			++cell;
		}
		System.out.println("SE: " + cnt);
		m_cnt += cnt;
		
		// south-west check
		cell = 1;
		cnt = 1;
		while (( r + cell ) < grid_bound && ( c - cell ) >= 0 && cell < 3) {
			if ( game.getCell(r + cell, c - cell) == id ) {
				++cnt;
			} else {
				break;
			}
			++cell;
		}
		System.out.println("SW: " + cnt);
		s_cnt += cnt;
		
		System.out.println("V:" + v_cnt);
		System.out.println("H:" + h_cnt);
		System.out.println("M:" + m_cnt);
		System.out.println("S:" + s_cnt);
		
		if ( v_cnt == 3 ) {
			++lines;
		}
		if ( h_cnt == 3 ) {
			++lines;
		}
		if ( m_cnt == 3 ) {
			++lines;
		}
		if ( s_cnt == 3 ) {
			++lines;
		}
		
		System.out.println("3Lines: " + lines);
		
		return false;
	}
	
	private boolean FourAndFour(int r, int c) {
		int lines = 0;
		return false;
	}
	
	public Player getCurrentPlayer() {
		return current;
	}
	
}
