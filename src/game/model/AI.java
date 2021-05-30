package game.model;

import game.Main;
import game.settings.GS;
import it.unical.mat.embasp.base.Handler;
import it.unical.mat.embasp.base.InputProgram;
import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import it.unical.mat.embasp.languages.asp.ASPMapper;
import it.unical.mat.embasp.languages.asp.AnswerSet;
import it.unical.mat.embasp.languages.asp.AnswerSets;
import it.unical.mat.embasp.platforms.desktop.DesktopHandler;
import it.unical.mat.embasp.specializations.dlv2.desktop.DLV2DesktopService;

public class AI extends Player {
	private final Handler handler;
	
	public AI(int id, boolean pro) {
		super(id);
		this.r = this.c = 0;
		handler = Engine.getInstance().getHandler(pro, id);
	}
	
	@Override
	public void place() {
		Placed move = Engine.getInstance().guessMove(handler);
		r = move.getRow();
		r = move.getCol();
	}
	
	public static class Engine {
		private static Engine instance = null;
		private static InputProgram variable;
		
		private Engine() {
			variable = new ASPInputProgram();
			//register Beans for mapper
			try {
				ASPMapper.getInstance().registerClass(Pawn.class);
				ASPMapper.getInstance().registerClass(Placed.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public static Engine getInstance() {
			if ( instance == null ) {
				instance = new Engine();
			}
			return instance;
		}
		
		public void updateProgram(Pawn last) {
			try {
				variable.addObjectInput(last);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public Placed guessMove(Handler handler) {
			AnswerSets as = (AnswerSets) handler.startSync();
			AnswerSet a = as.getAnswersets().get(0);
			
			try {
				for (Object atom: a.getAtoms()) {
					if ( ( atom instanceof Placed ) ) {
						return (Placed) atom;
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			Placed bho = new Placed();
			bho.setRow(-1);
			bho.setCol(-1);
			bho.setVal(-1);
			
			return bho;
		}
		
		public Handler getHandler(boolean pro, int id) {
			Handler h;
//			h = new DesktopHandler(new DLV2DesktopService("lib/dlv2.exe"));
			h = new DesktopHandler(new DLV2DesktopService("lib/dlv2linux"));
			
			InputProgram fixed = new InputProgram();
			fixed.addFilesPath(pro ? "encodings/promoku" : "encodings/gomoku");
			
			h.addProgram(fixed);
			h.addProgram(new InputProgram("player(" + id + ")."));
			h.addProgram(new InputProgram("size(" + GS.GRIDSIZE + ")."));
			
			h.addProgram(variable);
			
			return h;
		}
	}
}
