package game.model;

import game.controller.GameController;
import javafx.concurrent.Task;
import javafx.util.Pair;

public class GameLoop extends Task<Integer> {
	private final GameController board;
	private final GomokuLogic game;
	private final Referee referee;
	
	private static final int INTERRUPTED = -1;
	private static final int STALLED = 0;
	private static final int PLAYER1 = 1;
	private static final int PLAYER2 = 2;
	
	public GameLoop(GameController ctrl, GomokuLogic logic, Referee ref) {
		this.board = ctrl;
		this.game = logic;
		this.referee = ref;
	}
	
	@Override
	protected Integer call() throws Exception {
		int state = INTERRUPTED;
		
		while (true) {
			Player p = referee.getNextPlayer();
			p.place();
			
			//player is choosing...
			
			Pair<Integer, Integer> position = p.getChoice();
			int row = position.getKey();
			int col = position.getValue();
			
			switch (referee.judgeMove(row, col)) {
				case 0:
					//broke rule n0
					System.out.println("rule 0");
					break;
				case 1:
					//broke rule n1
					System.out.println("rule 1");
					break;
				case 2:
					//broke rule n2
					System.out.println("rule 2");
					break;
				default:
//					game.setCell(row, col, p.getId());
					board.markSpot(row, col);
					System.out.println("referee responded");
			}
			
			if ( !game.hasEmptyCell() ) {
				state = STALLED;
				break;
			}
			
			if ( referee.isWinningMove(row, col) ) {
				state = p.getId();
				break;
			}
		}
		
		return state;
	}
}
