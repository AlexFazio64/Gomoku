package game.model;

public class AI extends Player {
	private final boolean pro;
	
	public AI(int id, boolean pro) {
		super(id);
		this.pro = pro;
		this.r = this.c = 0;
	}
	
	@Override
	public void place() {
		System.out.println("I should be choosing something now...");
		//dlv code
		//remember to set r, c so that gameloop can fetch them
	}
}
