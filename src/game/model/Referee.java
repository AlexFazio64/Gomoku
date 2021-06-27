package game.model;

import game.settings.GS;

public class Referee {
	private final GomokuLogic game;
	private int[][] banned;
	private Player next;
	private Player current;
	
	public Referee(GomokuLogic logic, Player p1, Player p2) {
		this.game = logic;
		this.banned = new int[GS.GRIDSIZE - 1][GS.GRIDSIZE - 1];
		
		current = p1;
		next = p2;
		
		for (int i = 0; i < banned.length; i++) {
			for (int j = 0; j < banned.length; j++) {
				banned[i][j] = -1;
			}
		}
		
		AI.Engine.getInstance();
		banned[( GS.GRIDSIZE - 2 ) / 2][( GS.GRIDSIZE - 2 ) / 2] = 0;
		
		if ( !GS.RULES.PRO ) {
			return;
		}
		
		//first move is always the center (PRO)
		//make sure they are initialized
		AI.Engine.clearBanned();
		for (int i = 0; i < banned.length; i++)
			for (int j = 0; j < banned.length; j++)
				if ( banned[i][j] != 0 ) {
					AI.Engine.updateBanned(new Pawn(i, j, 3));
				}
	}
	
	public void switchPlayer() {
		Player swap = current;
		current = next;
		next = swap;
	}
	
	public int judgeMove(int row, int col) {
		if ( game.getCell(row, col) != 0 || ( GS.RULES.PRO && banned[row][col] == -1 ) ) {
			return -1;
		} else if ( GS.RULES.THREE && lineDetection(row, col, 3) ) {
			return 3;
		} else if ( GS.RULES.THREE && lineDetection(row, col, 4) ) {
			return 4;
		}
		return 0;
	}
	
	private boolean lineDetection(int r, int c, int SIZE) {
		int id = current.id;
		int grid_bound = GS.GRIDSIZE - 1;
		
		int cell;
		int cnt, v_cnt, h_cnt, m_cnt, s_cnt;
		v_cnt = h_cnt = m_cnt = s_cnt = -1;
		
		int lines = 0;
		
		// north check
		cell = 1;
		cnt = 1;
		while (( r - cell ) >= 0 && cell < SIZE) {
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
		while (( c + cell ) < grid_bound && cell < SIZE) {
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
		while (( r + cell ) < grid_bound && cell < SIZE) {
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
		while (( c - cell ) >= 0 && cell < SIZE) {
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
		while (( r - cell ) >= 0 && ( c - cell ) >= 0 && cell < SIZE) {
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
		while (( r - cell ) >= 0 && ( c + cell ) < grid_bound && cell < SIZE) {
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
		while (( r + cell ) < grid_bound && ( c + cell ) < grid_bound && cell < SIZE) {
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
		while (( r + cell ) < grid_bound && ( c - cell ) >= 0 && cell < SIZE) {
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
		
		if ( v_cnt == SIZE ) {
			++lines;
		}
		if ( h_cnt == SIZE ) {
			++lines;
		}
		if ( m_cnt == SIZE ) {
			++lines;
		}
		if ( s_cnt == SIZE ) {
			++lines;
		}
		
		System.out.println(SIZE + "Lines: " + lines);
		
		return lines == 2;
	}
	
	public Player getCurrentPlayer() {
		return current;
	}
	
	public void updateBanned(int turn) {
		if ( turn == 2 ) {
			banned = new int[GS.GRIDSIZE - 2][GS.GRIDSIZE - 2];
			AI.Engine.clearBanned();
		} else if ( turn == 3 ) {
			banned = new int[GS.GRIDSIZE - 2][GS.GRIDSIZE - 2];
			AI.Engine.clearBanned();
			
			int mid = ( GS.GRIDSIZE - 2 ) / 2;
			for (int i = mid - 3; i < mid + 3; ++i) {
				for (int j = mid - 3; j < mid + 3; ++j) {
					banned[i][j] = -1;
					AI.Engine.updateBanned(new Pawn(i, j, 3));
				}
			}
			
		} else if ( turn > 3 ) {
			GS.RULES.PRO = false;
			AI.Engine.clearBanned();
		}
	}
}
