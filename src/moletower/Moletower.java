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
	private static int moveInterval = 40;
	private GameWindow gameWindow;
	private Vector<Enemy> enemies; // Use a thread-safe collection that we can clone to paint
	private Vector<Enemy> enemiesToPaint; // @TODO Maybe replace with a class that only holds paint information of the
											// enemies
	private Vector<Tower> towers;
	private Vector<Tower> towersToPaint;
	private Path path;
	private long moveCounter = 0;
	private long paintCounter = 0;
	private Vector<Shot> shots;
	private Vector<Shot> shotsToPaint;

	public static void main(String[] args) {
		new Moletower();
	}

	public Moletower() {
		this.path = new Path();
		try {
			this.enemies = new Vector<Enemy>();
			this.enemiesToPaint = new Vector<Enemy>();
			this.towers = new Vector<Tower>();
			this.towersToPaint = new Vector<Tower>();
			this.shots = new Vector<Shot>();
			this.shotsToPaint = new Vector<Shot>();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gameWindow = new GameWindow(this, this.path);
		Thread gameThread = new Thread(this);
		gameThread.start();
	}

	@SuppressWarnings("unchecked")
	public void run() {

		towers.add(new Firetower(new Point(250, 100)));
		towers.add(new Firetower(new Point(580, 380)));
		towers.add(new Fasttower(new Point(180, 240)));
		towers.add(new Fasttower(new Point(590, 120)));

		this.gameWindow.repaint();
		timeSinceLastMove = System.currentTimeMillis();
		startTime = timeSinceLastMove;
		while (true) {
			try {
				timeSinceLastMove = System.currentTimeMillis() - lastMoveTime;
				if (timeSinceLastMove > moveInterval) {
					this.moveEnemies();
					this.resolveEnemyMoveResults();
					this.moveShots();
					this.shootTowers();
					moveCounter++;
					lastMoveTime = System.currentTimeMillis();
					this.enemiesToPaint = (Vector<Enemy>) this.enemies.clone();
					this.towersToPaint = (Vector<Tower>) this.towers.clone();
					this.shotsToPaint = (Vector<Shot>) this.shots.clone();

					this.gameWindow.repaint();

				} else {
					Thread.sleep(moveInterval - timeSinceLastMove);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
		}

	}

	private void shootTowers() {
		Iterator<Tower> towerIterator = this.towers.iterator();
		while (towerIterator.hasNext()) {
			Tower shootingTower = towerIterator.next();
			Enemy target = this.findClosestEnemy(shootingTower.getPosition());
			if (target != null && target.isLiving()) {
				Shot shot = shootingTower.shoot(target.getPosition());
				if (shot != null) {
					this.shots.add(shot);
				}
			}
		}
	}

	private Enemy findClosestEnemy(Point startPoint) {
		Enemy closestEnemy = null;
		double smallestDistance = Double.MAX_VALUE;
		Iterator<Enemy> enemyIterator = this.enemies.iterator();
		while (enemyIterator.hasNext()) {
			Enemy currentEnemy = enemyIterator.next();
			if (!currentEnemy.isLiving()) {
				continue;
			}
			double currentDistance = Moletower.getDistance(startPoint, currentEnemy.getPosition());
			if (currentDistance < smallestDistance) {
				smallestDistance = currentDistance;
				closestEnemy = currentEnemy;
			}
		}
		return closestEnemy;
	}

	private void resolveEnemyMoveResults() throws Exception {
		Iterator<Enemy> enemyIterator = this.enemies.iterator();
		while (enemyIterator.hasNext()) {
			Enemy currentEnemy = enemyIterator.next();
			// @TODO Implement multiple lives, game over and restart
			if (currentEnemy.hasReachedExit()) {
				System.exit(0);
			}
		}
		// @TODO develop an algorithm and data structure for spawning enemies that also
		// holds the path(s)
		if (this.moveCounter % 55 == 0) {
			this.enemies.add(new Slowenemy(path));
		}
		if ((this.moveCounter > 200) && ((this.moveCounter + 33) % 37 == 0)) {
			this.enemies.add(new Fastenemy(path));
		}
		if ((this.moveCounter > 300) && ((this.moveCounter + 33) % 33 == 0)) {
			this.enemies.add(new Slowenemy(path));
		}
		if ((this.moveCounter > 700) && ((this.moveCounter + 88) % 22 == 0)) {
			this.enemies.add(new Slowenemy(path));
		}
		if ((this.moveCounter > 1000) && ((this.moveCounter + 88) % 8 == 0)) {
			this.enemies.add(new Slowenemy(path));
		}
		if ((this.moveCounter > 1200) && ((this.moveCounter + 33) % 14 == 0)) {
			this.enemies.add(new Fastenemy(path));
		}
	}

	private void moveEnemies() {
		Iterator<Enemy> enemyIterator = this.enemies.iterator();
		while (enemyIterator.hasNext()) {
			Enemy currentEnemy = enemyIterator.next();
			if (currentEnemy.canBeDeleted()) {
				enemyIterator.remove();
				continue;
			}
			currentEnemy.move();
		}
	}

	public void draw(Graphics g) {
		this.paintCounter++;
		this.drawBackground(g);
		this.drawEnemies(g);
		this.drawTowers(g);
		this.drawShots(g);
		this.drawStatus(g);
	}

	private void moveShots() {
		Iterator<Shot> shotIterator = this.shots.iterator();
		while (shotIterator.hasNext()) {
			Shot currentShot = shotIterator.next();
			if (currentShot.canBeDeleted()) {
				shotIterator.remove();
				continue;
			}
			currentShot.move();
			if (currentShot.isLiving()) {
				Iterator<Enemy> enemyIterator = this.enemies.iterator();
				while (enemyIterator.hasNext()) {
					Enemy currentEnemy = enemyIterator.next();
					if (!currentEnemy.isLiving()) {
						continue;
					}
					if (getDistance(currentShot.getPosition(),
							currentEnemy.getPosition()) <= (currentEnemy.getSize() / 2)) {
						currentShot.hit();
						currentEnemy.hit();
						break;
					}
				}
			}
		}
	}

	private void drawShots(Graphics g) {
		Iterator<Shot> shotIterator = this.shotsToPaint.iterator();
		while (shotIterator.hasNext()) {
			Shot currentShot = shotIterator.next();
			currentShot.paintComponent(g);
		}
	}

	private void drawStatus(Graphics g) {
		long runtime = (System.currentTimeMillis() - this.startTime);
		if (runtime < 1) {
			runtime = 1;
		}
		float fps = 1000 * this.moveCounter / (float) runtime;
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 800, 20);
		g.setColor(Color.WHITE);
		g.drawString(String.format("Move # %d Paint # %d Runtime (ms): %d FPS: %3.1f ", moveCounter, paintCounter,
				runtime, fps), 10, 13);
	}

	private void drawEnemies(Graphics g) {
		Iterator<Enemy> enemyIterator = this.enemiesToPaint.iterator();
		while (enemyIterator.hasNext()) {
			Enemy currentEnemy = enemyIterator.next();
			currentEnemy.paintComponent(g);
		}
	}

	private void drawTowers(Graphics g) {
		Iterator<Tower> towerIterator = this.towersToPaint.iterator();
		while (towerIterator.hasNext()) {
			Tower currentTower = towerIterator.next();
			currentTower.paintComponent(g);
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

	public static double getDistance(Point p1, Point p2) {
		return Math.sqrt((p2.y - p1.y) * (p2.y - p1.y) + (p2.x - p1.x) * (p2.x - p1.x));
	}

	public static double calculateAngle(double ownX, double ownY, double targetX, double targetY) {
		double angle = Math.atan2(targetY - ownY, targetX - ownX);
		return angle;
	}
}