package game.model;

import javafx.util.Pair;

public abstract class Player {
	private final int id;
	
	public Player(int id) {
		this.id = id;
	}
	
	public abstract Pair<Integer, Integer> place();
	
	public int getId() {
		return id;
	}
}