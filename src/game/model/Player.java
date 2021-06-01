package game.model;

import javafx.scene.paint.Color;
import javafx.util.Pair;

public abstract class Player {
	protected final int id;
	protected int r, c;
	public final Color color;
	
	public Player(int id) {
		this.id = id;
		this.color = id == 1 ? Color.BLACK : Color.WHITE;
	}
	
	public abstract void choose();
	
	public int getId() {
		return id;
	}
	
	public Pair<Integer, Integer> getChoice() {
		return new Pair<>(r, c);
	}
}