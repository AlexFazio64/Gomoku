package game.model;

public class Human extends Player {
	private Thread loop;
	
	public Human(int id) {
		super(id, "Fazio");
	}
	
	@Override
	public void choose() {
		loop = Thread.currentThread();
		try {
			System.out.println("waiting...");
			synchronized (loop) {
				loop.wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void setChoice(int row, int col) {
		this.r = row;
		this.c = col;
		synchronized (loop) {
			loop.notify();
		}
	}
}
