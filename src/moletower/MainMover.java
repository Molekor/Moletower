package moletower;

import java.awt.Point;
import java.util.Iterator;

public class MainMover {

	private GameData gameData;
	private int moveCounter = 0;
	private Path path;

	public MainMover(GameData gameData, Path path) {
		this.gameData = gameData;
		this.path = path;
	}

	public void move() throws Exception {
		this.moveEnemies();
		this.resolveEnemyMoveResults();
		this.moveShots();
		this.shootTowers();
		this.moveCounter++;
	}
	
	/**
	 * Iterate through all towers, find the next living target for each 
	 * tower, and shoot at it. The tower checks range and cooldown itself.
	 * If a shot is fired, it is added to the shots vector.
	 */
	private void shootTowers() {
		Iterator<Tower> towerIterator = this.gameData.getTowers().iterator();
		while (towerIterator.hasNext()) {
			Tower shootingTower = towerIterator.next();
			Enemy target = this.findClosestEnemy(shootingTower.getPosition());
			if (target != null && target.isLiving()) {
				Shot shot = shootingTower.shoot(target.getPosition());
				if (shot != null) {
					this.gameData.addShot(shot);
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
		Iterator<Enemy> enemyIterator = this.gameData.getEnemies().iterator();
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
		Iterator<Enemy> enemyIterator = this.gameData.getEnemies().iterator();
		while (enemyIterator.hasNext()) {
			Enemy currentEnemy = enemyIterator.next();
			if (currentEnemy.hasReachedExit()) {
				this.gameData.adjustLives(-1);
				if (this.gameData.getLives() <= 0) {
					System.exit(0);
				}
				enemyIterator.remove();
			}
		}
		// @TODO develop an algorithm and data structure for spawning enemies that also
		// holds the path(s)
		if (this.moveCounter % 40 == 0) {
			this.gameData.addEnemy(new Slowenemy(path));
		}
		if ((this.moveCounter > 500) && ((this.moveCounter + 7) % 50 == 0)) {
			this.gameData.addEnemy(new Slowenemy(path));
		}
		if ((this.moveCounter > 700) && ((this.moveCounter + 33) % 78 == 0)) {
			this.gameData.addEnemy(new Fastenemy(path));
		}
		if ((this.moveCounter > 1000) && ((this.moveCounter + 33) % 33 == 0)) {
			this.gameData.addEnemy(new Slowenemy(path));
		}
		if ((this.moveCounter > 1500) && ((this.moveCounter + 88) % 22 == 0)) {
			this.gameData.addEnemy(new Slowenemy(path));
		}
		if ((this.moveCounter > 2000) && ((this.moveCounter + 88) % 18 == 0)) {
			this.gameData.addEnemy(new Slowenemy(path));
		}
		if ((this.moveCounter > 3000) && ((this.moveCounter + 33) % 14 == 0)) {
			this.gameData.addEnemy(new Fastenemy(path));
		}
	}

	/**
	 * Enemy movement and actions. Dead enemies are removed here if their death animation has been
	 * shown long enough.
	 */
	private void moveEnemies() {
		Iterator<Enemy> enemyIterator = this.gameData.getEnemies().iterator();
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
	 * All shots are processed here
	 */
	private void moveShots() {
		Iterator<Shot> shotIterator = this.gameData.getShots().iterator();
		while (shotIterator.hasNext()) {
			Shot currentShot = shotIterator.next();
			if (currentShot.canBeDeleted()) {
				shotIterator.remove();
				continue;
			}
			currentShot.move();
			if (currentShot.isLiving()) {
				Iterator<Enemy> enemyIterator = this.gameData.getEnemies().iterator();
				while (enemyIterator.hasNext()) {
					Enemy currentEnemy = enemyIterator.next();
					if (!currentEnemy.isLiving()) {
						continue;
					}
					if (MathHelper.getDistance(currentShot.getPosition(), currentEnemy.getPosition()) <= (currentEnemy.getSize() / 2)) {
						currentShot.hit();
						currentEnemy.hit();
						if (!currentEnemy.isLiving) {
							this.gameData.adjustMoney(currentEnemy.getValue());
						}
						break;
					}
				}
			}
		}
	}

}
