package game.settings;

import game.Main;
import javafx.scene.image.Image;
import javafx.scene.text.Font;

public abstract class GS {
	public static int GRIDSIZE = 20;
	public static int CELLSIZE = 30;
	public static double PAWNSIZE = CELLSIZE * 0.8;
	public static double OFFSET = PAWNSIZE / 2;
	public static int DIMENSION = GRIDSIZE * CELLSIZE;
	public static double LINESIZE = 1;
	public static final Font FONT = new Font("Arial", 24);
	
	public static Image getBOARD() {
		return new Image(Main.getRes("board.jpg"), DIMENSION, DIMENSION, true, true);
	}
	
	public static class RULES {
		public static boolean THREE = false;
		public static boolean FOUR = false;
		public static boolean HANDICAP = false;
		public static boolean SWAP = false;
		public static boolean PRO = false;
		public static boolean FREESTYLE = false;
		public static boolean RENJU = false;
		public static boolean OMOK = false;
		public static boolean PLUS = false;
	}
}
