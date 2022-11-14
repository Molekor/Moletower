package moletower.controller;

import java.util.Iterator;

import moletower.model.Enemy;
import moletower.model.GameData;
import moletower.model.Shot;
import moletower.model.Tower;

public class MainMover {

	private GameData gameData;

	public MainMover(GameData gameData) {
		this.gameData = gameData;
	}

	public void move() throws Exception {
		this.moveEnemies();
		this.resolveEnemyMoveResults();
		this.moveShots();
		this.shootTowers();
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
			Shot shot = shootingTower.shoot(this.gameData.getEnemies(), this.gameData.getTick());
			if (shot != null) {
				this.gameData.addShot(shot);
			}
			
			
//			Enemy target = this.findClosestEnemy(shootingTower.getPosition());
//			if (target != null && target.isLiving()) {
//				Shot shot = shootingTower.shoot(target.getPosition());
//				if (shot != null) {
//					this.gameData.addShot(shot);
//				}
//			}
		}
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
				enemyIterator.remove();
			}
		}
//		// @TODO develop an algorithm and data structure for spawning enemies that also
//		// holds the path(s)
//		if (this.gameData.getTick() % 40 == 0) {
//			this.gameData.addEnemy(new Slowenemy(path));
//		}
//		if ((this.gameData.getTick() > 500) && ((this.gameData.getTick() + 7) % 50 == 0)) {
//			this.gameData.addEnemy(new Slowenemy(path));
//		}
//		if ((this.gameData.getTick() > 700) && ((this.gameData.getTick() + 33) % 78 == 0)) {
//			this.gameData.addEnemy(new Fastenemy(path));
//		}
//		if ((this.gameData.getTick() > 1000) && ((this.gameData.getTick() + 33) % 33 == 0)) {
//			this.gameData.addEnemy(new Slowenemy(path));
//		}
//		if ((this.gameData.getTick() > 1500) && ((this.gameData.getTick() + 88) % 22 == 0)) {
//			this.gameData.addEnemy(new Slowenemy(path));
//		}
//		if ((this.gameData.getTick() > 2000) && ((this.gameData.getTick() + 88) % 18 == 0)) {
//			this.gameData.addEnemy(new Slowenemy(path));
//		}
//		if ((this.gameData.getTick() > 3000) && ((this.gameData.getTick() + 33) % 14 == 0)) {
//			this.gameData.addEnemy(new Fastenemy(path));
//		}
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
						currentEnemy.hit(currentShot);
						if (!currentEnemy.isLiving()) {
							this.gameData.adjustMoney(currentEnemy.getValue());
						}
						break;
					}
				}
			}
		}
	}

}
