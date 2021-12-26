package moletower;

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
	private long timeSinceLastMove = 0; // how long has it been since the last move
	private static int moveInterval = 40; // minimal interval between moves in ms, this represents overall game speed
	private GameWindow gameWindow; // Main window of the game that handles basic I/O

	// We use one copy of game data to compute the next move, and one copy to draw the results of the last move
	// Vectors can be cloned thread-safe so we use them for that purpose
	// @TODO Maybe replace with a class that only holds paint information of the enemies
	private Vector<Enemy> enemies; // The enemies that are used for computing moves
	private Vector<Tower> towers;
	private Vector<Shot> shots;
	private Path path; // The path all enemies will follow to the exit
	private long moveCounter = 0;
	private boolean roundActive = false;
	private boolean placingTower = false; // Is the user actively placing a tower?
	private Tower towerToPlace; // The tower instance that the user is trying to place

	private int lives = 50; // The player's lives. Zero lives = game over
	private int money = 80; // The player's money pool used to buy and upgrade towers
	private boolean moneyWarning = false; // Indicator that the player tries something too expensive, so we can issue a warning on screen
	
	private GamePainter gamePainter;
	
	public static void main(String[] args) {
		new Moletower();
	}

	public Moletower() {
		this.path = new Path();
		this.enemies = new Vector<Enemy>();
		this.towers = new Vector<Tower>();
		this.shots = new Vector<Shot>();
		this.gamePainter = new GamePainter(this, this.path);
		this.gameWindow = new GameWindow(this.gamePainter);
		Thread gameThread = new Thread(this);
		gameThread.start();
	}


	@SuppressWarnings("unchecked")
	public void run() {
		this.gameWindow.repaint();
		timeSinceLastMove = System.currentTimeMillis();
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
						this.gamePainter.setEnemiesToPaint((Vector<Enemy>) this.enemies.clone());
						this.gamePainter.setShotsToPaint((Vector<Shot>) this.shots.clone());
					} else {
						Thread.sleep(moveInterval - timeSinceLastMove);
					}
				}
				// Towers may be bought before the round is started, so fill the paint data
				this.gamePainter.setTowerToPlace(this.towerToPlace);
				this.gamePainter.setTowersToPaint((Vector<Tower>) this.towers.clone());
				// Draw all changes that may have happened
				this.gameWindow.repaint();
			} catch (Exception e) {
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
		if ((this.moveCounter > 500) && ((this.moveCounter + 33) % 33 == 0)) {
			this.enemies.add(new Slowenemy(path));
		}
		if ((this.moveCounter > 1000) && ((this.moveCounter + 88) % 22 == 0)) {
			this.enemies.add(new Slowenemy(path));
		}
		if ((this.moveCounter > 1500) && ((this.moveCounter + 88) % 18 == 0)) {
			this.enemies.add(new Slowenemy(path));
		}
		if ((this.moveCounter > 2000) && ((this.moveCounter + 33) % 14 == 0)) {
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
					Thread.sleep(10);
				} catch (InterruptedException e) {
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

	public boolean isRoundActive() {
		return this.roundActive;
	}

	public boolean isMoneyWarningActive() {
		return this.moneyWarning;
	}

	public boolean isPlacingTower() {
		return this.placingTower;
	}

	public Object getMoney() {
		return this.money;
	}

	public Object getLives() {
		return this.lives;
	}

	public Object getMoveCounter() {
		return this.moveCounter;
	}

	public Object getUserAction() {
		if(this.isPlacingTower()) {
			return "placing tower";
		};
		return "-";
	}
}