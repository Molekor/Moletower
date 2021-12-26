package moletower;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Iterator;
import java.util.Vector;

/**
 * The master class of the game.
 * 
 * @author Molekor
 *
 */
public class Moletower implements Runnable {

	private long lastMoveTime; // time we last moved the shots and enemies
	private long startTime; // time the round started
	private long timeSinceLastMove = 0; // how long has it been since the last move
	private static int moveInterval = 40; // minimal interval between moves in ms, this represents overall game speed
	private GameWindow gameWindow; // Main window of the game that handles basic I/O

	// We use one copy of game data to compute the next move, and one copy to draw the results of the last move
	// Vectors can be cloned thread-safe so we use them for that purpose
	// @TODO Maybe replace with a class that only holds paint information of the enemies
	private Vector<Enemy> enemies; // The enemies that are used for computing moves
	private Vector<Enemy> enemiesToPaint; // The enemy data used for painting
	private Vector<Tower> towers;
	private Vector<Tower> towersToPaint;
	private Vector<Shot> shots;
	private Vector<Shot> shotsToPaint;
	private Path path; // The path all enemies will follow to the exit
	private long moveCounter = 0;
	private long paintCounter = 0;
	private boolean roundActive = false;
	private boolean placingTower = false; // Is the user actively placing a tower?
	private Tower towerToPlace; // The tower instance that the user is trying to place

	private int lives = 50; // The player's lives. Zero lives = game over
	private int money = 80; // The player's money pool used to buy and upgrade towers
	private boolean moneyWarning = false; // Indicator that the player tries something too expensive, so we can issue a warning on screen
	
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

		this.gameWindow.repaint();
		timeSinceLastMove = System.currentTimeMillis();
		startTime = timeSinceLastMove;
		
		/**
		 * Main game loop
		 */
		while (true) {
			try {
				// Always check what the user is doing
				// @TODO Replace with proper mouse listeners etc.
				this.checkUserAction();
				// Game element actions, if the game is active
				if (this.roundActive) {
					timeSinceLastMove = System.currentTimeMillis() - lastMoveTime;
					// Only move if at least the moveInterval time has passed, else pause
					if (timeSinceLastMove > moveInterval) {
						this.moveEnemies();
						this.resolveEnemyMoveResults();
						this.moveShots();
						this.shootTowers();
						moveCounter++;
						lastMoveTime = System.currentTimeMillis();
						this.enemiesToPaint = (Vector<Enemy>) this.enemies.clone();
						this.shotsToPaint = (Vector<Shot>) this.shots.clone();
					} else {
						Thread.sleep(moveInterval - timeSinceLastMove);
					}
				}
				// Towers may be bought before the round is started, so fill the paint data
				this.towersToPaint = (Vector<Tower>) this.towers.clone();
				// Draw all changes that may have happened
				this.gameWindow.repaint();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

	/**
	 * Iterate through all towers, find the next living target for each 
	 * tower, and shoot at it. The tower checks range and cooldown itself.
	 * If a shot is fired, it is added to the shots vector.
	 */
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

	/**
	 * 
	 * @param startPoint The coordinates from where we look
	 * @return the enemy closest to the given coordinates, null if no enemy is found
	 */
	private Enemy findClosestEnemy(Point startPoint) {
		Enemy closestEnemy = null;
		double smallestDistance = Double.MAX_VALUE;
		Iterator<Enemy> enemyIterator = this.enemies.iterator();
		while (enemyIterator.hasNext()) {
			Enemy currentEnemy = enemyIterator.next();
			if (!currentEnemy.isLiving()) {
				continue;
			}
			double currentDistance = MathHelper.getDistance(startPoint, currentEnemy.getPosition());
			if (currentDistance < smallestDistance) {
				smallestDistance = currentDistance;
				closestEnemy = currentEnemy;
			}
		}
		return closestEnemy;
	}

	/**
	 * Check if any enemies have reached the exit. They cost the player lives
	 * and then disappear from the game.
	 * @throws Exception
	 */
	private void resolveEnemyMoveResults() throws Exception {
		Iterator<Enemy> enemyIterator = this.enemies.iterator();
		while (enemyIterator.hasNext()) {
			Enemy currentEnemy = enemyIterator.next();
			if (currentEnemy.hasReachedExit()) {
				this.lives--;
				if (this.lives == 0) {
					System.exit(0);
				}
				enemyIterator.remove();
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

	/**
	 * Enemy movement and actions. Dead enemies are removed here if their death animation has been
	 * shown long enough.
	 */
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

	/**
	 * Draw the game. This is called by the GameWindow when it is repainted.
	 * 
	 * @param g The graphics object of the GameWindow
	 */
	public void draw(Graphics g) {
		this.drawBackground(g);
		if (this.roundActive) {
			this.paintCounter++;
			this.drawEnemies(g);
			this.drawShots(g);
		}
		this.drawTowers(g);
		this.drawStatus(g);
		// Draw sidebar last, so it is the top layer
		this.drawSidebar(g);
		// The tower to place hovers over all
		this.drawTowerToPlace(g);
	}

	/**
	 * Paint the tower the user is trying to place
	 * 
	 * @param g
	 */
	private void drawTowerToPlace(Graphics g) {
		if (this.placingTower) {
			this.towerToPlace.paintComponent(g);
		}
	}

	/**
	 * The tower menu etc.
	 * 
	 * @param g
	 */
	private void drawSidebar(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(700, 0, 100, 600);
		g.setColor(Color.GREEN);
		g.fillRect(710, 50, 80, 30);
		g.fillRect(710, 110, 80, 30);
		if (this.moneyWarning) {
			g.setColor(Color.RED);
		} else {
			g.setColor(Color.BLACK);
		}
		g.drawString("Firetower " + Firetower.basePrice + " $", 711, 70);
		g.drawString("Fasttower " + Fasttower.basePrice + " $", 711, 130);
		if (!this.roundActive) {
			g.setColor(Color.GREEN);
			g.fillRect(710, 500, 80, 30);
			g.setColor(Color.BLACK);
			g.drawString("START", 715, 515);
		}
	}

	/**
	 * Resolves all user action (Mouseclicks)
	 */
	private void checkUserAction() {
		
		this.moneyWarning = false;
		
		// Check for start of round button
		if (this.roundActive == false && this.gameWindow.mouseIsPressed) {
			int mouseX = this.gameWindow.mousePosition.x;
			int mouseY = this.gameWindow.mousePosition.y;
			if (mouseX > 710 && mouseX < 790 && mouseY > 500 && mouseY < 530) {
				this.roundActive = true;
			} else {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		// Check for tower placement
		// TODO: Use proper Java UI items!
		// User is placing tower and releases the mouse button: Drop the tower at the
		// mouse position
		if (this.placingTower) {
			if (!this.gameWindow.mouseIsPressed && this.gameWindow.mousePosition.x < 700) {
				this.placingTower = false;
				this.towerToPlace.setActive(true);
				this.towers.add(towerToPlace);
				this.money -= towerToPlace.getPrice();
			} else {
				this.towerToPlace.setPosition(this.gameWindow.mousePosition);
			}
		} else {
			if (this.gameWindow.mouseIsPressed) {
				int mouseX = this.gameWindow.mousePosition.x;
				int mouseY = this.gameWindow.mousePosition.y;
				if (mouseX > 710 && mouseX < 790 && mouseY > 50 && mouseY < 80) {
					this.placingTower = true;
					this.towerToPlace = new Firetower(new Point(0, 0));
				}
				if (mouseX > 710 && mouseX < 790 && mouseY > 110 && mouseY < 140) {
					this.placingTower = true;
					this.towerToPlace = new Fasttower(new Point(0, 0));
				}
				// Check if we have the money to place the selected tower
				if ((this.towerToPlace != null) && (this.towerToPlace.getPrice() > this.money)) {
					this.placingTower = false;
					this.moneyWarning = true;
					this.towerToPlace = null;
				}
			}
		}
	}

	/**
	 * All shots are processed here
	 */
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
					if (MathHelper.getDistance(currentShot.getPosition(), currentEnemy.getPosition()) <= (currentEnemy.getSize() / 2)) {
						currentShot.hit();
						currentEnemy.hit();
						if (!currentEnemy.isLiving) {
							this.money += currentEnemy.getValue();
						}
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
		// float fps = 1000 * this.moveCounter / (float) runtime;
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 800, 20);
		if (this.moneyWarning) {
			g.setColor(Color.RED);
		} else {
			g.setColor(Color.WHITE);
		}
		String action = this.placingTower ? "PLACING TOWER" : "none";
		g.drawString(String.format("Lives: %d Money: %d Move # %d Paint # %d Action: %s " + (this.moneyWarning?"NO MONEY":"OK"), this.lives, this.money, moveCounter, paintCounter, action), 10, 13);

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

}