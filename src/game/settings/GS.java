package game.settings;

import game.Main;
import javafx.scene.image.Image;
import javafx.scene.text.Font;

public abstract class GS {
	public static int GRIDSIZE = 20;
	public static final int CELLSIZE = 35;
	public static int DIM = GRIDSIZE * CELLSIZE;
	public static final double PAWNSIZE = CELLSIZE * 0.8;
	public static final double OFFSET = PAWNSIZE / 2;
	public static final double LINESIZE = 2;
	public static final Font FONT = new Font("Arial", 22);

	public static final String HUMAN = "Human";
	public static final String AI = "AI";

	private static Image image = null;
	private static Image image_sm = null;
	private static Image menu = null;

	public static Image getBG() {
		if (image == null) {
			image = new Image(Main.getRes("board.jpg"), 20 * CELLSIZE, 20 * CELLSIZE, true, true);
			image_sm = new Image(Main.getRes("board.jpg"), 16 * CELLSIZE, 16 * CELLSIZE, true, true);
		}
		return RULES.RENJU ? image_sm : image;
	}

	public static Image getMenuBG() {
		if (menu == null) {
			menu = new Image(Main.getRes("bg_blur.jpg"), DIM, DIM, true, true);
		}
		return menu;
	}

	public static class RULES {
		public static boolean THREE = false;
		public static boolean FOUR = false;
		public static boolean HANDICAP = false;
		public static boolean PRO = false;
		public static boolean FREESTYLE = false;
		public static boolean RENJU = false;
		public static boolean OMOK = false;
	}
}
