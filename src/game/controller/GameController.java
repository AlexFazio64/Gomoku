package game.controller;

import game.Main;
import game.model.GameLoop;
import game.model.Human;
import game.model.Player;
import game.settings.GS;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class GameController {
	@FXML
	private Canvas table;
	private GraphicsContext gc;
	@FXML
	private Label turnLbl;
	@FXML
	private Label p1Lbl;
	@FXML
	private Label p2Lbl;
	
	@FXML
	private void initialize() {
		//set fonts
		turnLbl.setFont(GS.FONT);
		p1Lbl.setText(Main.referee.getCurrentPlayer().getName());
		Main.referee.switchPlayer();
		p2Lbl.setText(Main.referee.getCurrentPlayer().getName());
		Main.referee.switchPlayer();
		p1Lbl.setFont(GS.FONT);
		p2Lbl.setFont(GS.FONT);
		
		//draw background board
		gc = table.getGraphicsContext2D();
		table.setWidth(GS.DIM);
		table.setHeight(GS.DIM);
		
		gc.drawImage(GS.getBOARD(), 0, 0);
		gc.setLineWidth(GS.LINESIZE);
		
		for (int i = 0; i <= GS.DIM; i += GS.CELLSIZE) {
			gc.strokeLine(i, 0, i, GS.DIM);
			gc.strokeLine(0, i, GS.DIM, i);
		}
		
		GameLoop loop = new GameLoop(this, Main.logic, Main.referee);
		Thread thread = new Thread(loop);
		thread.setDaemon(true);
		thread.start();
	}
	
	public void place(MouseEvent e) {
		Player player = Main.referee.getCurrentPlayer();
		//current player is not human
		if ( !( player instanceof Human ) ) {
			return;
		}
		
		//capture click position
		double x = e.getX();
		double y = e.getY();
		
		//normalize to matrix coords
		int nx = (int) ( x / GS.CELLSIZE );
		int ny = (int) ( y / GS.CELLSIZE );
		
		//create candidate pawn
		Point2D p = new Point2D(x, y);
		
		//get positions surrounding clicked cell
		Point2D ul = new Point2D(nx * GS.CELLSIZE, ny * GS.CELLSIZE);
		Point2D dl = new Point2D(nx * GS.CELLSIZE, ( ny + 1 ) * GS.CELLSIZE);
		Point2D ur = new Point2D(( nx + 1 ) * GS.CELLSIZE, ny * GS.CELLSIZE);
		Point2D dr = new Point2D(( nx + 1 ) * GS.CELLSIZE, ( ny + 1 ) * GS.CELLSIZE);
		
		//choose where the pawn should go
		//between the possible positions
		int col_from_X;
		int row_from_Y;
		Point2D target = null;
		
		switch (intersects(p, ul, dl, ur, dr)) {
			case 0:
				if ( nx == 0 || ny == 0 ) {
					break;
				}
				target = ul;
				break;
			case 1:
				if ( ny == GS.GRIDSIZE - 1 || nx == 0 ) {
					break;
				}
				target = dl;
				break;
			case 2:
				if ( ny == 0 || nx == GS.GRIDSIZE - 1 ) {
					break;
				}
				target = ur;
				break;
			case 3:
				if ( ny == GS.GRIDSIZE - 1 || nx == GS.GRIDSIZE - 1 ) {
					break;
				}
				target = dr;
				break;
		}
		
		//no candidate position found, abort
		if ( target == null ) {
			//wake sleeping thread with unavailable position
			( (Human) player ).setChoice(-1, -1);
			return;
		}
		
		//position tracked down
		row_from_Y = (int) ( target.getY() / GS.CELLSIZE ) - 1;
		col_from_X = (int) ( target.getX() / GS.CELLSIZE ) - 1;
		
		( (Human) player ).setChoice(row_from_Y, col_from_X);
	}
	
	public void pass(int id) {
		Platform.runLater(() -> turnLbl.setText(id == 1 ? "<(ﾟヮﾟ<)" : "(>ﾟヮﾟ)>"));
	}
	
	public void markSpot(int game_row, int game_column, Color col) {
		gc.setFill(col);
		gc.fillOval(( game_column + 1 ) * GS.CELLSIZE - GS.OFFSET, ( game_row + 1 ) * GS.CELLSIZE - GS.OFFSET, GS.PAWNSIZE, GS.PAWNSIZE);
	}
	
	public void markStroke(Point2D[] stroke) {
		//if player scored a five-in-a-row
		double startX = stroke[0].getX();
		double startY = stroke[0].getY();
		double endX = stroke[1].getX();
		double endY = stroke[1].getY();
		
		//mark line
		gc.setStroke(Color.RED);
		gc.strokeLine(( startY + 1 ) * GS.CELLSIZE, ( startX + 1 ) * GS.CELLSIZE, ( endY + 1 ) * GS.CELLSIZE, ( endX + 1 ) * GS.CELLSIZE);
	}
	
	public void stopGame(int state) {
		String winner = "Player ";
		
		switch (state) {
			case GameLoop.STALLED:
				winner = "Nobody";
				break;
			case GameLoop.INTERRUPTED:
				break;
			default:
				winner += state;
		}
		table.setOnMouseClicked(event -> Main.restart());
		
		String finalWinner = winner;
		Platform.runLater(() -> turnLbl.setText(finalWinner + " won"));
		System.out.println(winner);
	}
	
	private int intersects(Point2D point, Point2D... intersections) {
		for (int i = 0; i < intersections.length; i++) {
			if ( isWithin(point, intersections[i]) ) {
				return i;
			}
		}
		return -1;
	}
	
	private boolean isWithin(Point2D p, Point2D c) {
		return Math.hypot(distance(p.getX(), c.getX()), distance(p.getY(), c.getY())) <= GS.OFFSET;
	}
	
	private double distance(double x1, double x2) {
		return Math.abs(x1 - x2);
	}
	
	//TODO find a way to stop the game manually
	public void stop(KeyEvent keyEvent) {
		if ( keyEvent.getCode().equals(KeyCode.R) ) {
			stopGame(GameLoop.INTERRUPTED);
		}
	}
}
