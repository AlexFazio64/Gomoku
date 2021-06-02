package game.model;

import game.controller.GameController;
import game.settings.GS;
import javafx.concurrent.Task;
import javafx.geometry.Point2D;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.function.Predicate;

public class GameLoop extends Task<Void> {
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
	protected Void call() {
		//TODO make it work?
		int state = INTERRUPTED;
		ArrayList<Point2D[]> lines;
		
		while (true) {
			Player p = referee.getCurrentPlayer();
			board.pass(p.id);
			p.choose();
			//player is choosing...
			
			Pair<Integer, Integer> position = p.getChoice();
			int row = position.getKey();
			int col = position.getValue();
			
			switch (referee.judgeMove(row, col)) {
				case -1:
					//broke rule n1
					System.out.println("not available, retry...");
					continue;
				
				case 2:
					//broke rule n2
					System.out.println("rule 2");
					break;
				
				case 3:
					//broke rule n3
					System.out.println("rule 3 and 3");
					if ( GS.RULES.HANDICAP ) {
						System.out.println("other player should place 2 pawns");
					}
					continue;
				
				case 4:
					//broke rule n4
					System.out.println("rule 4 and 4");
					continue;
				
				default:
					board.markSpot(row, col, p.color);
					System.out.println("referee responded");
					game.setCell(row, col, p.id);
					referee.switchPlayer();
					AI.Engine.getInstance().updateProgram(referee.getCurrentPlayer(), new Pawn(row, col, p.id));
			}
			
			if ( !game.hasEmptyCell() ) {
				state = STALLED;
				break;
			}
			
			lines = game.checkVictory(row, col);
			lines.removeIf(Predicate.isEqual(null));
			
			if ( !lines.isEmpty() ) {
				state = p.id;
				board.markStroke(lines.get(0));
				break;
			}
		}
		
		board.stopGame(state);
		return null;
	}
}
