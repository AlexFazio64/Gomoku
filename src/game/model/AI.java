package game.model;

import game.Main;
import game.settings.GS;
import it.unical.mat.embasp.base.InputProgram;
import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import it.unical.mat.embasp.languages.asp.AnswerSet;
import it.unical.mat.embasp.languages.asp.AnswerSets;

public class AI extends Player {
	private final boolean pro;
	
	public AI(int id, boolean pro) {
		super(id);
		this.pro = pro;
		this.r = this.c = 0;
	}
	
	@Override
	public void place() {
		//dlv code
		InputProgram p = new ASPInputProgram();
		p.addFilesPath("encodings/gomoku");
		Main.handler.addProgram(p);
//		addFacts();
		
		AnswerSets as = (AnswerSets) Main.handler.startSync();
		AnswerSet a = as.getAnswersets().get(0);
		
		try {
			for (Object atom: a.getAtoms()) {
				if ( ( atom instanceof Placed ) ) {
					Placed pawn = (Placed) atom;
					System.out.println(pawn);
					
					int guessed_row_Y, guessed_col_X;
					r = guessed_row_Y = pawn.getRow();
					c = guessed_col_X = pawn.getCol();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		Main.handler.removeAll();
	}
	
	private void addFacts() {
		//optimize
		int[][] cells = Main.logic.getGame_Table();
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
		
		extended.addProgram("player(" + ( this.id ) + ").");
		extended.addProgram("size(" + GS.GRIDSIZE + ").");
		Main.handler.addProgram(extended);
	}
	
	//TODO maybe make an engine class for dlv?
}
