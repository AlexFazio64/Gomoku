package game.model;

import game.controller.GameController;
import game.settings.GS;
import javafx.concurrent.Task;
import javafx.geometry.Point2D;
import javafx.util.Pair;

public class GameLoop extends Task<Void> {
	private final GameController board;
	private final GomokuBoard game;
	private final Referee referee;
	private boolean playing = true;
	
	public static final int INTERRUPTED = -1;
	public static final int STALLED = 0;
	
	public GameLoop(GameController ctrl, GomokuBoard logic, Referee ref) {
		this.board = ctrl;
		this.game = logic;
		this.referee = ref;
	}
	
	public synchronized void stop() {
		playing = false;
	}
	
	@Override
	protected Void call() {
		int state = -2;
		Player p;
		int penalty = 0;
		
		while (playing) {
			p = referee.getCurrentPlayer();
			board.pass(p.id);
			p.choose();
			//player is choosing...
			
			Pair<Integer, Integer> position = p.getChoice();
			int row = position.getKey();
			int col = position.getValue();
			
			switch (referee.isLegalMove(row, col)) {
				case -1:
					//broke rule n1
					System.out.println(row + "," + col + " not available, retry...");
					continue;
				
				case 3:
					//broke rule n3
					System.out.println("rule 3 and 3");
					if ( GS.RULES.HANDICAP ) {
						System.out.println("Other player should place 2 pawns");
						penalty = 1;
						referee.switchPlayer();
						board.markSpot(row, col, p.color);
						game.setCell(row, col, p.id);
						AI.Engine.updateShared(new Pawn(row, col, p.id));
					} else {
						referee.updateBanned(row, col, 3);
						continue;
					}
					continue;
				
				case 4:
					//broke rule n4, ban position
					referee.updateBanned(row, col, p.id);
					System.out.println("rule 4 and 4");
					continue;
				
				default:
					board.markSpot(row, col, p.color);
					game.setCell(row, col, p.id);
					if ( penalty == 0 ) {
						referee.switchPlayer();
					} else {
						--penalty;
					}
					AI.Engine.updateShared(new Pawn(row, col, p.id));
			}
			
			if ( !game.hasEmptyCell() ) {
				state = STALLED;
				playing = false;
			}
			
			Point2D[] line = referee.isWinningMove(row, col, p.id);
			if ( line != null ) {
				//player has won
				state = p.id;
				board.markStroke(line);
				playing = false;
			}
		}
		
		board.stopGame(state);
		return null;
	}
}
