package game.controller;

import game.Main;
import game.model.*;
import game.settings.GS;
import it.unical.mat.embasp.base.Handler;
import it.unical.mat.embasp.base.InputProgram;
import it.unical.mat.embasp.base.OptionDescriptor;
import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import it.unical.mat.embasp.languages.asp.AnswerSet;
import it.unical.mat.embasp.languages.asp.AnswerSets;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Objects;

public class GameController {
	public Canvas table;
	public Label info;
	public Label player1info;
	public Label player2info;
	
	public GraphicsContext gc;
	
	private boolean player;
	
	private Handler handler;
	private GomokuLogic game;
	private GameLoop loop;
	private Referee referee;
	private Thread gameThread;
	
	public void initialize() {
		handler = Main.handler;
		game = Main.logic;
		referee = Main.referee;
		loop = new GameLoop(this, game, referee);
		
		//set fonts
		info.setFont(GS.FONT);
		player1info.setFont(GS.FONT);
		player2info.setFont(GS.FONT);
		
		//draw background board
		gc = table.getGraphicsContext2D();
		table.setWidth(GS.DIM);
		table.setHeight(GS.DIM);
		
		gc.drawImage(GS.getBOARD(), 0, 0);
		gc.setStroke(Color.web("black"));
		gc.setLineWidth(GS.LINESIZE);
		
		for (int i = 0; i <= GS.DIM; i += GS.CELLSIZE) {
			gc.strokeLine(i, 0, i, GS.DIM);
			gc.strokeLine(0, i, GS.DIM, i);
		}
		
		//choose who goes first
		player = Math.random() > 0.5;
//		pass();
		
		gameThread = new Thread(loop);
		gameThread.start();
	}
	
	/**
	 * only for human players
	 */
	public void place(MouseEvent e) {
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
			default:
				return;
		}
		
		//no candidate position found, abort
		if ( target == null ) {
			return;
		}
		
		//position tracked down
		row_from_Y = (int) ( target.getY() / GS.CELLSIZE ) - 1;
		col_from_X = (int) ( target.getX() / GS.CELLSIZE ) - 1;
		
		( (Human) referee.getCurrentPlayer() ).setChoice(row_from_Y, col_from_X);
	}
	
	private void addFacts() {
		int[][] cells = game.getGame_Table();
		InputProgram extended = new ASPInputProgram();
		
		for (int x = 0; x < cells.length; x++) {
			for (int y = 0; y < cells.length; y++) {
				if ( cells[x][y] == 0 ) {
					continue;
				}
				try {
					Pawn p = new Pawn(x, y, cells[x][y]);
					System.out.println("Adding: " + p);
					extended.addObjectInput(p);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		extended.addProgram("player(" + ( player ? 1 : 2 ) + ").");
		extended.addProgram("size(" + GS.GRIDSIZE + ").");
		handler.addProgram(extended);
	}
	
	private void pass() {
		player = !player;
		info.setText(player ? "<(ﾟヮﾟ<)" : "(>ﾟヮﾟ)>");
		//Tie
		if ( !game.hasEmptyCell() ) {
			StopGame(0);
		}
		
		if ( player ) {
			player1info.setTextFill(Color.RED);
			player2info.setTextFill(Color.BLACK);
		} else {
			player2info.setTextFill(Color.RED);
			player1info.setTextFill(Color.BLACK);
		}
		
		if ( !player ) {
			placeAI();
		}
	}
	
	/**
	 * only for robot players
	 */
	private void placeAI() {
		OptionDescriptor all = new OptionDescriptor("-n 0");
//		OptionDescriptor fil = new OptionDescriptor("--filter=placed/3,pawn/3");
//		OptionDescriptor fil = new OptionDescriptor("--filter=placed/3");
//		handler.addOption(fil);
		
		InputProgram p = new ASPInputProgram();
		p.addFilesPath("encodings/gomoku");
		handler.addProgram(p);
		addFacts();
		
		AnswerSets as = (AnswerSets) handler.startSync();
		AnswerSet a = as.getAnswersets().get(0);
		
		try {
			for (Object atom: a.getAtoms()) {
				if ( ( atom instanceof Placed ) ) {
					Placed pawn = (Placed) atom;
					System.out.println(pawn);
					
					int guessed_row_Y, guessed_col_X;
					guessed_row_Y = pawn.getRow();
					guessed_col_X = pawn.getCol();
					
					if ( game.isLegalMove(guessed_row_Y, guessed_col_X, pawn.getVal()) ) {
						gc.setFill(Color.web(player ? "black" : "white"));
						gc.fillOval(( guessed_col_X + 1 ) * GS.CELLSIZE - GS.OFFSET, ( guessed_row_Y + 1 ) * GS.CELLSIZE - GS.OFFSET, GS.PAWNSIZE, GS.PAWNSIZE);
						
						//draw a pawn on the board and check score
						markSpot(guessed_row_Y, guessed_col_X);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		handler.removeAll();
	}
	
	public void markSpot(int game_row, int game_column) {
		//set cell with corresponding pawn
		//and check for winning move
		gc.setFill(Color.web(player ? "black" : "white"));
		gc.fillOval(( game_column + 1 ) * GS.CELLSIZE - GS.OFFSET, ( game_row + 1 ) * GS.CELLSIZE - GS.OFFSET, GS.PAWNSIZE, GS.PAWNSIZE);
		
		ArrayList<Point2D[]> strokes = game.setCell(game_row, game_column, player ? 1 : 2);
		strokes.removeIf(Objects::isNull);
		Point2D[] stroke;
		
		//if player scored a five-in-a-row
		if ( !strokes.isEmpty() ) {
			stroke = strokes.get(0);
			
			double startX = stroke[0].getX();
			double startY = stroke[0].getY();
			double endX = stroke[1].getX();
			double endY = stroke[1].getY();
			
			//mark line
			gc.setStroke(Color.web("red"));
			gc.strokeLine(( startY + 1 ) * GS.CELLSIZE, ( startX + 1 ) * GS.CELLSIZE, ( endY + 1 ) * GS.CELLSIZE, ( endX + 1 ) * GS.CELLSIZE);
			
			//someone has won, stop current game
			StopGame(player ? 1 : 2);
			return;
		}
		
		//switch player
//		pass();
	}
	
	private void StopGame(int player) {
		String winner;
		switch (player) {
			case 1:
				winner = "Player 1";
				break;
			case 2:
				winner = "DLV2";
				break;
			default:
				winner = "Nobody";
		}
		table.setOnMouseClicked(null);
		info.setText(winner + " won");
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
}
