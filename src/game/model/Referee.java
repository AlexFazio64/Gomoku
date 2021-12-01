package game.model;

import game.settings.GS;
import javafx.geometry.Point2D;

public class Referee {
	private final GomokuBoard board;
	private int[][] banned;
	private Player next;
	private Player current;
	private int turn = 1;

	public Referee(GomokuBoard logic, Player p1, Player p2) {
		this.board = logic;
		this.banned = new int[GS.GRIDSIZE - 1][GS.GRIDSIZE - 1];
		AI.Engine.getInstance();
		current = p1;
		next = p2;
	}

	public void switchPlayer() {
		Player swap = current;
		current = next;
		next = swap;
		openingMoveCheck();
	}

	public int isLegalMove(int row, int col) {
		if (board.getCell(row, col) != 0 || banned[row][col] == current.id) {
			return -1;
		} else if (GS.RULES.THREE && lineDetection(row, col, 3)) {
			updateBanned(row, col, current.id);
			return 3;
		} else if (GS.RULES.FOUR && lineDetection(row, col, 4)) {
			updateBanned(row, col, current.id);
			return 4;
		}
		++turn;
		AI.Engine.clearBanned();
		return 0;
	}

	public Point2D[] isWinningMove(int row, int col, int id) {
		return overlineDetection(row, col, id);
	}

	private Point2D[] overlineDetection(int r, int c, int id) {
		int grid_bound = GS.GRIDSIZE - 1;
		int cell, cnt, v_cnt, h_cnt, m_cnt, s_cnt;
		v_cnt = h_cnt = m_cnt = s_cnt = -1;

		int r_start, r_end;
		int c_start, c_end;

		// north check
		cell = 1;
		cnt = 1;
		while ((r - cell) >= 0) {
			if (board.getCell(r - cell, c) == id) {
				++cnt;
			} else {
				break;
			}
			++cell;
		}
		--cell;
		v_cnt += cnt;

		r_start = r - cell;

		// south check
		cell = 1;
		cnt = 1;
		while ((r + cell) < grid_bound) {
			if (board.getCell(r + cell, c) == id) {
				++cnt;
			} else {
				break;
			}
			++cell;
		}
		--cell;
		v_cnt += cnt;

		if (v_cnt >= 5) {
			c_start = c_end = c;
			r_end = r + cell;

			return makeCoords(r_start, r_end, c_start, c_end, v_cnt);
		}

		// west check
		cell = 1;
		cnt = 1;
		while ((c - cell) >= 0) {
			if (board.getCell(r, c - cell) == id) {
				++cnt;
			} else {
				break;
			}
			++cell;
		}
		--cell;
		h_cnt += cnt;

		c_start = c - cell;

		// east check
		cell = 1;
		cnt = 1;
		while ((c + cell) < grid_bound) {
			if (board.getCell(r, c + cell) == id) {
				++cnt;
			} else {
				break;
			}
			++cell;
		}
		--cell;
		h_cnt += cnt;

		if (h_cnt >= 5) {
			c_end = c + cell;
			r_start = r_end = r;

			return makeCoords(r_start, r_end, c_start, c_end, h_cnt);
		}

		// north-west check
		cell = 1;
		cnt = 1;
		while ((r - cell) >= 0 && (c - cell) >= 0) {
			if (board.getCell(r - cell, c - cell) == id) {
				++cnt;
			} else {
				break;
			}
			++cell;
		}
		--cell;
		m_cnt += cnt;

		r_start = r - cell;
		c_start = c - cell;

		// south-east check
		cell = 1;
		cnt = 1;
		while ((r + cell) < grid_bound && (c + cell) < grid_bound) {
			if (board.getCell(r + cell, c + cell) == id) {
				++cnt;
			} else {
				break;
			}
			++cell;
		}
		--cell;
		m_cnt += cnt;

		if (m_cnt >= 5) {
			r_end = r + cell;
			c_end = c + cell;

			return makeCoords(r_start, r_end, c_start, c_end, m_cnt);
		}

		// north-east check
		cell = 1;
		cnt = 1;
		while ((r - cell) >= 0 && (c + cell) < grid_bound) {
			if (board.getCell(r - cell, c + cell) == id) {
				++cnt;
			} else {
				break;
			}
			++cell;
		}
		--cell;
		s_cnt += cnt;

		r_start = r - cell;
		c_start = c + cell;

		// south-west check
		cell = 1;
		cnt = 1;
		while ((r + cell) < grid_bound && (c - cell) >= 0) {
			if (board.getCell(r + cell, c - cell) == id) {
				++cnt;
			} else {
				break;
			}
			++cell;
		}
		--cell;
		s_cnt += cnt;

		if (s_cnt >= 5) {
			r_end = r + cell;
			c_end = c - cell;

			return makeCoords(r_start, r_end, c_start, c_end, s_cnt);
		}

		return null;
	}

	private Point2D[] makeCoords(int r_start, int r_end, int c_start, int c_end, int size) {
		Point2D start = new Point2D(r_start, c_start);
		Point2D end = new Point2D(r_end, c_end);
		return (GS.RULES.FREESTYLE || size == 5) ? new Point2D[] { start, end } : null;
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
		while ((r - cell) >= 0 && cell < SIZE) {
			if (board.getCell(r - cell, c) == id) {
				++cnt;
			} else {
				break;
			}
			++cell;
		}
		v_cnt += cnt;

		// east check
		cell = 1;
		cnt = 1;
		while ((c + cell) < grid_bound && cell < SIZE) {
			if (board.getCell(r, c + cell) == id) {
				++cnt;
			} else {
				break;
			}
			++cell;
		}
		h_cnt += cnt;

		// south check
		cell = 1;
		cnt = 1;
		while ((r + cell) < grid_bound && cell < SIZE) {
			if (board.getCell(r + cell, c) == id) {
				++cnt;
			} else {
				break;
			}
			++cell;
		}
		v_cnt += cnt;

		// west check
		cell = 1;
		cnt = 1;
		while ((c - cell) >= 0 && cell < SIZE) {
			if (board.getCell(r, c - cell) == id) {
				++cnt;
			} else {
				break;
			}
			++cell;
		}
		h_cnt += cnt;

		// north-west check
		cell = 1;
		cnt = 1;
		while ((r - cell) >= 0 && (c - cell) >= 0 && cell < SIZE) {
			if (board.getCell(r - cell, c - cell) == id) {
				++cnt;
			} else {
				break;
			}
			++cell;
		}
		m_cnt += cnt;

		// north-east check
		cell = 1;
		cnt = 1;
		while ((r - cell) >= 0 && (c + cell) < grid_bound && cell < SIZE) {
			if (board.getCell(r - cell, c + cell) == id) {
				++cnt;
			} else {
				break;
			}
			++cell;
		}
		s_cnt += cnt;

		// south-east check
		cell = 1;
		cnt = 1;
		while ((r + cell) < grid_bound && (c + cell) < grid_bound && cell < SIZE) {
			if (board.getCell(r + cell, c + cell) == id) {
				++cnt;
			} else {
				break;
			}
			++cell;
		}
		m_cnt += cnt;

		// south-west check
		cell = 1;
		cnt = 1;
		while ((r + cell) < grid_bound && (c - cell) >= 0 && cell < SIZE) {
			if (board.getCell(r + cell, c - cell) == id) {
				++cnt;
			} else {
				break;
			}
			++cell;
		}
		s_cnt += cnt;

		if (v_cnt >= SIZE) {
			++lines;
		}
		if (h_cnt >= SIZE) {
			++lines;
		}
		if (m_cnt >= SIZE) {
			++lines;
		}
		if (s_cnt >= SIZE) {
			++lines;
		}

		return lines >= 2;
	}

	public Player getCurrentPlayer() {
		return current;
	}

	public void openingMoveCheck() {
		if (GS.RULES.PRO) {
			if (turn == 1) {
				for (int i = 0; i < banned.length; i++) {
					for (int j = 0; j < banned.length; j++) {
						banned[i][j] = 1;
					}
				}

				AI.Engine.getInstance();

				// first move is always the center (PRO)
				banned[(GS.GRIDSIZE - 2) / 2][(GS.GRIDSIZE - 2) / 2] = 0;
				dumpBannedToAI();
			} else if (turn == 2) {
				banned = new int[GS.GRIDSIZE - 1][GS.GRIDSIZE - 1];
			} else if (turn == 3) {
				banned = new int[GS.GRIDSIZE - 1][GS.GRIDSIZE - 1];

				int mid = (GS.GRIDSIZE - 2) / 2;
				for (int i = mid - 2; i < mid + 3; ++i) {
					for (int j = mid - 2; j < mid + 3; ++j) {
						banned[i][j] = 1;
						AI.Engine.updateBanned(new Pawn(i, j, 3));
					}
				}
			} else if (turn > 3) {
				GS.RULES.PRO = false;
				banned = new int[GS.GRIDSIZE - 1][GS.GRIDSIZE - 1];
			}
		} else {
			dumpBannedToAI();
		}
	}

	private void dumpBannedToAI() {
		for (int i = 0; i < banned.length; i++) {
			for (int j = 0; j < banned.length; j++) {
				if (banned[i][j] != 0) {
					AI.Engine.updateBanned(new Pawn(i, j, 3));
				}
			}
		}
	}

	public void updateBanned(int row, int col, int id) {
		banned[row][col] = id;
	}
}
