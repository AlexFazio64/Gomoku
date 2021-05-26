package game.model;

import javafx.util.Pair;

public class Human extends Player {
	public Human(int id) {
		super(id);
	}
	
	@Override
	public Pair<Integer, Integer> place() {
		return null;
	}
}
