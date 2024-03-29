package game.model;

import game.settings.GS;
import it.unical.mat.embasp.base.Handler;
import it.unical.mat.embasp.base.InputProgram;
import it.unical.mat.embasp.base.OptionDescriptor;
import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import it.unical.mat.embasp.languages.asp.ASPMapper;
import it.unical.mat.embasp.languages.asp.AnswerSet;
import it.unical.mat.embasp.languages.asp.AnswerSets;
import it.unical.mat.embasp.platforms.desktop.DesktopHandler;
import it.unical.mat.embasp.specializations.dlv2.desktop.DLV2DesktopService;

public class AI extends Player {
	private final Handler handler;

	public AI(int id) {
		super(id, "Terminator");
		this.r = this.c = 0;
		handler = Engine.getInstance().requestHandler(id);
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
			// register Beans for mapper
			try {
				ASPMapper.getInstance().registerClass(Pawn.class);
				ASPMapper.getInstance().registerClass(Placed.class);
				shared = new ASPInputProgram();
				banned = new ASPInputProgram();
			} catch (Exception ignored) {
			}
		}

		public static void stopAI() {
			clearBanned();
			instance = null;
			shared = null;
			banned = null;
			try {
				ASPMapper.getInstance().unregisterClass(Pawn.class);
				ASPMapper.getInstance().unregisterClass(Placed.class);
			} catch (Exception ignored) {
			}
		}

		public static Engine getInstance() {
			if (instance == null) {
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
				for (AnswerSet a : as.getOptimalAnswerSets()) {
					System.out.println();
					System.out.println(a.getLevelWeight());
					String aString = a.toString();
					String[] atoms = aString.substring(1, aString.length() - 1).split("\\, ");
					for (String atom : atoms) {
						System.out.println(atom);
					}
				}

				int idx = (int) (Math.random() * 10 % as.getOptimalAnswerSets().size());
				// System.out.println("chosen AS: " + idx);
				AnswerSet chosen = as.getOptimalAnswerSets().get(idx);

				for (Object atom : chosen.getAtoms())
					if ((atom instanceof Placed)) {
						return (Placed) atom;
					}

			} catch (Exception ignored) {
			}

			Placed afforza = new Placed();
			afforza.setRow(-1);
			afforza.setCol(-1);
			afforza.setVal(-1);

			return afforza;
		}

		public Handler requestHandler(int id) {
			Handler h;
			h = new DesktopHandler(new DLV2DesktopService("lib/dlv2.exe"));

			InputProgram fixed = new ASPInputProgram();
			fixed.addFilesPath("encodings/gomoku");
			fixed.addProgram(String.format("player(%d).", id));
			fixed.addProgram(String.format("enemy(%d).", (id == 1) ? 2 : 1));
			fixed.addProgram(String.format("size(%d).", GS.GRIDSIZE));

			h.addProgram(fixed);
			h.addProgram(shared);
			h.addProgram(banned);

			OptionDescriptor filter = new OptionDescriptor(
					"--filter=placed/3,advantage/0,disadvantage/0,bestmove/0,pos/3");
			// h.addOption(new OptionDescriptor("-n 3 "));
			h.addOption(filter);

			return h;
		}
	}
}
