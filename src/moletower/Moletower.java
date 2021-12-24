package moletower;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Iterator;
import java.util.Vector;

public class Moletower implements Runnable {
	private long lastMoveTime;
	private long startTime;
	private long timeSinceLastMove = 0;
	private static int moveInterval = 10;
	private GameWindow gameWindow;
	private Vector<Enemy> enemies; // Use a thread-safe collection that we can clone to paint
	private Vector<Enemy> enemiesToPaint; // @TODO Maybe replace with a class that only holds paint information of the enemies
	private Path path;
	private long moveCounter = 1;
	private long paintCounter = 1;
	
	public static void main(String[] args) {
		new Moletower();
	}

	public Moletower() {
		this.path = new Path();
		try {
			this.enemies = new Vector<Enemy>();
			this.enemiesToPaint = new Vector<Enemy>();
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
		startTime = timeSinceLastMove;
		while (true) {
			try {
				timeSinceLastMove = System.currentTimeMillis() - lastMoveTime;
				if (timeSinceLastMove > moveInterval) {
					this.move();
					this.resolveMoveResults();
					moveCounter++;
					lastMoveTime = System.currentTimeMillis();
					this.enemiesToPaint = (Vector<Enemy>) this.enemies.clone();
					this.gameWindow.repaint();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
		}

	}

	private void resolveMoveResults() throws Exception {
		Iterator<Enemy> enemyIterator = this.enemies.iterator();
		while(enemyIterator.hasNext()) {
			Enemy currentEnemy = enemyIterator.next();
			// @TODO Implement multiple lives, game over and restart
			if (currentEnemy.hasReachedExit()) {
				System.exit(0);
			}
		}
		// @TODO develop an algorithm and data structure for spawning enemies that also holds the path(s)
		if(this.moveCounter % 50 == 0) {
			this.enemies.add(new Enemy(path));
		}
	}

	public void move() {
		Iterator<Enemy> enemyIterator = this.enemies.iterator();
		while(enemyIterator.hasNext()) {
			Enemy currentEnemy = enemyIterator.next();
			currentEnemy.move();
		}
	}
	
	public void draw(Graphics g) {
		this.paintCounter++;
		this.drawBackground(g);
		
		this.drawEnemies(g, this.enemiesToPaint);
		this.drawStatus(g);
	}

	private void drawStatus(Graphics g) {
		long runtime = (System.currentTimeMillis() - this.startTime);
		if (runtime < 1 ) {
			runtime = 1;
		}
		float fps = 1000 * this.moveCounter / (float)runtime;
		g.drawString(
				String.format("Move # %d Paint # %d Runtime (ms): %d FPS: %3.1f ", moveCounter, paintCounter, runtime, fps),
			10, 10 );
	}

	private void drawEnemies(Graphics g, Vector<Enemy> enemiesToPaint) {
		Iterator<Enemy> enemyIterator = enemiesToPaint.iterator();
		while(enemyIterator.hasNext()) {
			Enemy currentEnemy = enemyIterator.next();
			currentEnemy.paintComponent(g);
		}
	}

	private void drawBackground(Graphics g) {
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
	}

}