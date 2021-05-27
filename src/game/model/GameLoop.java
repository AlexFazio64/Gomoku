package game.model;

import game.controller.GameController;
import javafx.concurrent.Task;
import javafx.util.Pair;

public class GameLoop extends Task<Integer> {
	private final GameController board;
	private final GomokuLogic game;
	private final Referee referee;
	
	public static final int INTERRUPTED = -1;
	public static final int STALLED = 0;
	
	public GameLoop(GameController ctrl, GomokuLogic logic, Referee ref) {
		this.board = ctrl;
		this.game = logic;
		this.referee = ref;
	}
	
	@Override
	protected Integer call() {
		//TODO make it work?
		int state = INTERRUPTED;
		
		while (true) {
			Player p = referee.getNextPlayer();
			board.pass(p.id);
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
					board.markSpot(row, col, p.color);
					System.out.println("referee responded");
					game.setCell(row, col, p.getId());
			}
			
			if ( !game.hasEmptyCell() ) {
				state = STALLED;
				break;
			}
			
			if ( referee.isWinningMove(row, col) ) {
				state = p.getId();
				board.markStroke(referee.getWinningLine());
				break;
			}
		}
		
		board.stopGame(state);
		return state;
	}
}
