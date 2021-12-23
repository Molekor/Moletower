package moletower;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Iterator;

public class Moletower implements Runnable {
	private long lastMoveTime = System.currentTimeMillis();
	private long timeSinceLastMove = 0;
	private static int moveInterval = 10;
	private GameWindow gameWindow;
	private Enemy mover = null;
	private Path path;

	public static void main(String[] args) {
		new Moletower();
	}

	public Moletower() {
		this.path = new Path();
		try {
			this.mover = new Enemy(this.path);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gameWindow = new GameWindow(this, this.path);
		Thread gameThread = new Thread(this);
		gameThread.start();
	}

	public void run() {
		this.gameWindow.repaint();
		timeSinceLastMove = System.currentTimeMillis();
		while (true) {
			timeSinceLastMove = System.currentTimeMillis() - lastMoveTime;
			if (timeSinceLastMove > moveInterval) {
				this.move();
				this.resolveMoveResults();
				lastMoveTime = System.currentTimeMillis();
				this.gameWindow.repaint();
			}
		}

	}

	private void resolveMoveResults() {
		if (this.mover.hasReachedExit()) {
			System.exit(0);
		}
	}

	public void move() {
		this.mover.move();
	}

	public void draw(Graphics g) {
		g.setColor(Color.YELLOW);
		g.fillRect(0, 0, 800, 600);
		g.setColor(Color.BLACK);
		Iterator<Point> pathIterator = path.getPathPoints().iterator();
		Point lastPoint = null;
		Point nextPoint = null;
		while (pathIterator.hasNext()) {
			if (lastPoint != null) {
				nextPoint = pathIterator.next();
				g.drawLine(lastPoint.x, lastPoint.y, nextPoint.x, nextPoint.y);
				lastPoint = nextPoint;
			} else {
				lastPoint = pathIterator.next();
			}
		}
		mover.paintComponent(g);
	}

}