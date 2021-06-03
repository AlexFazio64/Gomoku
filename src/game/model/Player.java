package game.model;

import javafx.scene.paint.Color;
import javafx.util.Pair;

public abstract class Player {
	protected final String name;
	protected final int id;
	protected int r, c;
	public final Color color;
	
	public Player(int id, String name) {
		this.id = id;
		this.name = name;
		this.color = id == 1 ? Color.BLACK : Color.WHITE;
	}
	
	public abstract void choose();
	
	public Pair<Integer, Integer> getChoice() {
		return new Pair<>(r, c);
	}
	
	public String getName() {
		return name;
	}
}