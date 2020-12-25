package game.settings;

import game.Main;
import javafx.scene.image.Image;
import javafx.scene.text.Font;

public abstract class GS {
	public static final int GRIDSIZE = 15;
	public static final int CELLSIZE = 50;
	public static final double PAWNSIZE = CELLSIZE * 0.8;
	public static final double OFFSET = PAWNSIZE / 2;
	public static final int DIMENSION = GRIDSIZE * CELLSIZE;
	public static final double LINESIZE = 3;
	public static final Image BOARD = new Image(Main.getRes("board.jpg"), DIMENSION, DIMENSION, true, true);
	public static boolean FILL = false;
	public static Font FONT = new Font("Arial", 24);
}
