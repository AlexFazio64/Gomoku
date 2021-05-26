package game.model;

import javafx.util.Pair;

public class AI extends Player {
	private final boolean pro;
	
	public AI(int id, boolean pro) {
		super(id);
		this.pro = pro;
	}
	
	@Override
	public Pair<Integer, Integer> place() {
		return null;
	}
}
