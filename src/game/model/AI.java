package game.model;

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
		super(id, Math.random() < .5 ? "Perri" : "❤ Zangari ❤");
		this.r = this.c = 0;
		handler = Engine.getInstance().requestHandler(pro, id);
	}
	
	@Override
	public void choose() {
		Placed move = Engine.getInstance().guessMove(handler);
		r = move.getRow();
		c = move.getCol();
	}
	
	public static class Engine {
		private static Engine instance = null;
		private static InputProgram shared;
		private static InputProgram banned;
		
		private Engine() {
			//register Beans for mapper
			try {
				ASPMapper.getInstance().registerClass(Pawn.class);
				ASPMapper.getInstance().registerClass(Placed.class);
				shared = new ASPInputProgram();
				banned = new ASPInputProgram();
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
		
		public static void updateShared(Pawn last) {
			try {
				shared.addObjectInput(last);
			} catch (Exception ignored) {
			}
		}
		
		public static void updateBanned(Pawn last) {
			try {
				banned.addObjectInput(last);
			} catch (Exception ignored) {
			}
		}
		
		public static void clearBanned() {
			banned.clearAll();
		}
		
		public Placed guessMove(Handler handler) {
			AnswerSets as = (AnswerSets) handler.startSync();
			
			try {
				for (AnswerSet a: as.getOptimalAnswerSets()) {
					System.out.println();
					String[] strings = a.toString().split(",\\s");
					for (String line: strings) {
						if ( line.contains("\"") ) {
							System.out.println(line);
						}
					}
					
					for (Object atom: a.getAtoms())
						if ( ( atom instanceof Placed ) ) {
							return (Placed) atom;
						}
				}
			} catch (Exception ignored) {
			}
			
			Placed afforza = new Placed();
			afforza.setRow(-1);
			afforza.setCol(-1);
			afforza.setVal(-1);
			
			return afforza;
		}
		
		public Handler requestHandler(boolean pro, int id) {
			Handler h;
//			h = new DesktopHandler(new DLV2DesktopService("lib/dlv2.exe"));
			h = new DesktopHandler(new DLV2DesktopService("lib/dlv2linux"));
			
			InputProgram fixed = new ASPInputProgram();
			fixed.addFilesPath(pro ? "encodings/promoku" : "encodings/gomoku");
			fixed.addProgram(String.format("player(%d).", id));
			fixed.addProgram(String.format("size(%d).", GS.GRIDSIZE));
			
			h.addProgram(fixed);
			h.addProgram(shared);
			h.addProgram(banned);
			
			return h;
		}
	}
}
