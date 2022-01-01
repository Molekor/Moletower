package moletower;

import java.util.Vector;

public class GameData {
	
	// We use one copy of game data to compute the next move, and one copy to draw the results of the last move
	// Vectors can be cloned thread-safe so we use them for that purpose
	// @TODO Maybe replace with a class that only holds paint information of the enemies
	private Vector<Enemy> enemies; // The enemies that are used for computing moves
	private Vector<Tower> towers;
	private Vector<Shot> shots;
	private int lives;
	private int money;
	private long tick;
	
	public GameData() {
		this.enemies = new Vector<Enemy>();
		this.towers = new Vector<Tower>();
		this.shots = new Vector<Shot>();
		this.lives = 50;
		this.money = 90;
		this.tick = 0;
	}

	public Vector<Tower> getTowers() {
		return towers;
	}
	
	public Vector<Shot> getShots() {
		return this.shots;
	}

	public Vector<Enemy> getEnemies() {
		return this.enemies;
	}

	public void addShot(Shot shot) {
		this.shots.add(shot);
	}
	
	public void addTower(Tower tower) {
		this.towers.add(tower);
	}
	
	public void addEnemy(Enemy enemy) {
		this.enemies.add(enemy);
	}

	public void adjustLives(int livesToAdd) {
		this.lives += livesToAdd;
	}

	public int getLives() {
		return this.lives;
	}

	public void adjustMoney(int moneyToAdd) {
		this.money += moneyToAdd;
	}
	
	public int getMoney() {
		return this.money;
	}
	
	public void addTick() {
		this.tick++;
	}
	
	public long getTick() {
		return this.tick;
	}
}

