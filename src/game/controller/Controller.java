package game.controller;

import game.model.Gomoku;
import game.settings.GS;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Controller {
	public Canvas table;
	public Label info;
	public Label player1info;
	public Label player2info;
	
	private Gomoku game;
	public GraphicsContext gc;
	
	private boolean player;
	private int p1_lines = 0;
	private int p2_lines = 0;
	
	public void initialize() {
		//set fonts
		info.setFont(GS.FONT);
		player1info.setFont(GS.FONT);
		player2info.setFont(GS.FONT);
		
		//initialize game
		game = new Gomoku();
		gc = table.getGraphicsContext2D();
		table.setWidth(GS.DIMENSION);
		table.setHeight(GS.DIMENSION);
		
		//draw background board
		gc.drawImage(GS.BOARD, 0, 0);
		gc.setStroke(Color.web("black"));
		gc.setLineWidth(GS.LINESIZE);
		
		for (int i = 0; i < GS.DIMENSION; i += GS.CELLSIZE) {
			gc.strokeLine(i, 0, i, GS.DIMENSION);
			gc.strokeLine(0, i, GS.DIMENSION, i);
		}
		
		//choose who goes first
		player = ( Math.random() * 10 ) % 2 == 0;
		pass();
		
		//testing
		if ( GS.FILL ) {
			fill();
		}
	}
	
	private void fill() {
		for (int i = GS.CELLSIZE; i < GS.DIMENSION; i += GS.CELLSIZE) {
			for (int j = GS.CELLSIZE; j < GS.DIMENSION; j += GS.CELLSIZE) {
				gc.setFill(Color.web(player ? "black" : "white"));
				gc.fillOval(i - GS.OFFSET, j - GS.OFFSET, GS.PAWNSIZE, GS.PAWNSIZE);
				markSpot(i / GS.CELLSIZE, j / GS.CELLSIZE);
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
				if ( ny > 0 && nx > 0 ) {
					target = ul;
				}
				break;
			case 1:
				if ( ny > 0 && nx < GS.GRIDSIZE - 1 ) {
					target = dl;
				}
				break;
			case 2:
				if ( ny < GS.GRIDSIZE - 1 && nx > 0 ) {
					target = ur;
				}
				break;
			case 3:
				if ( ny < GS.GRIDSIZE - 1 && nx < GS.GRIDSIZE - 1 ) {
					target = dr;
				}
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
		//and check for score
		ArrayList<Point2D[]> strokes = game.setCell(cy, cx, player ? 1 : 2);
		for (Point2D[] stroke: strokes) {
			//if player scored a 5
			if ( stroke != null ) {
				double startX = stroke[0].getX();
				double startY = stroke[0].getY();
				double endX = stroke[1].getX();
				double endY = stroke[1].getY();
				
				gc.setStroke(Color.web("red"));
				gc.strokeLine(( startY + 1 ) * GS.CELLSIZE, ( startX + 1 ) * GS.CELLSIZE, ( endY + 1 ) * GS.CELLSIZE, ( endX + 1 ) * GS.CELLSIZE);
				
				//update score
				if ( player ) {
					++p1_lines;
					player1info.setText("P1: " + p1_lines);
				} else {
					++p2_lines;
					player2info.setText("P2: " + p2_lines);
				}
			}
		}
		
		//switch player
		pass();
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
