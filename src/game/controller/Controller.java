package game.controller;

import game.model.GomokuLogic;
import game.settings.GS;
import javafx.concurrent.Task;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Objects;

public class Controller {
	public Canvas table;
	public Label info;
	public Label player1info;
	public Label player2info;
	
	private GomokuLogic game;
	public GraphicsContext gc;
	
	private boolean player;
	
	public void initialize() {
		//set fonts
		info.setFont(GS.FONT);
		player1info.setFont(GS.FONT);
		player2info.setFont(GS.FONT);
		
		//initialize game
		game = new GomokuLogic();
		gc = table.getGraphicsContext2D();
		table.setWidth(GS.DIMENSION);
		table.setHeight(GS.DIMENSION);
		
		//draw background board
		gc.drawImage(GS.BOARD, 0, 0);
		gc.setStroke(Color.web("black"));
		gc.setLineWidth(GS.LINESIZE);
		
		for (int i = 0; i <= GS.DIMENSION; i += GS.CELLSIZE) {
			gc.strokeLine(i, 0, i, GS.DIMENSION);
			gc.strokeLine(0, i, GS.DIMENSION, i);
		}
		
		//testing
		if ( GS.DBG_FILL ) {
			Task<Void> fill = new Task<Void>() {
				@Override
				protected Void call() {
					fill();
					return null;
				}
			};
			new Thread(fill).start();
			return;
		}
		
		//choose who goes first
		player = Math.random() > 0.5;
		pass();
	}
	
	private void fill() {
		int p = 0;
		for (int i = GS.CELLSIZE; i < GS.DIMENSION; i += GS.CELLSIZE) {
			for (int j = GS.CELLSIZE; j < GS.DIMENSION; j += GS.CELLSIZE) {
				gc.setFill(Color.web(( ( ++p % 2 ) == 0 ) ? "black" : "white"));
				gc.fillOval(j - GS.OFFSET, i - GS.OFFSET, GS.PAWNSIZE, GS.PAWNSIZE);
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if ( GS.GRIDSIZE % 2 != 0 ) {
				++p;
			}
		}
	}
	
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
		double cx;
		double cy;
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
		cx = target.getX() / GS.CELLSIZE;
		cy = target.getY() / GS.CELLSIZE;
		
		//assert legality of move
		if ( game.isLegalMove((int) cx, (int) cy, player ? 1 : 2) ) {
			gc.setFill(Color.web(player ? "black" : "white"));
			gc.fillOval(cx * GS.CELLSIZE - GS.OFFSET, cy * GS.CELLSIZE - GS.OFFSET, GS.PAWNSIZE, GS.PAWNSIZE);
			
			//draw a pawn on the board and check score
			markSpot((int) cx, (int) cy);
		}
	}
	
	private void pass() {
		player = !player;
		info.setText(player ? "<----" : "---->");
		
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
	}
	
	private void markSpot(int cx, int cy) {
		//set cell with corresponding pawn
		//and check for winning move
		ArrayList<Point2D[]> strokes = game.setCell(cy, cx, player ? 1 : 2);
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
		pass();
	}
	
	private void StopGame(int player) {
		String winner = null;
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
