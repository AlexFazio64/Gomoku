package game.model;

@SuppressWarnings("SynchronizeOnNonFinalField")
public class Human extends Player {
	private Thread loop;
	
	public Human(int id) {
		super(id, "John Connor");
	}
	
	@Override
	public void choose() {
		loop = Thread.currentThread();
		try {
			synchronized (loop) {
				loop.wait();
			}
		} catch (InterruptedException ignored) {
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
