package moletower;

import java.util.Vector;

public class GameData {
	
	private Vector<Enemy> enemies; // The enemies that are used for computing moves
	private Vector<Tower> towers;
	private Vector<Shot> shots;
	private int lives;
	private int money;
	private long tick;
	private boolean gameActive = false;
	private boolean moneyWarning;
	private Tower towerToPlace;
	private Tower selectedTower;
	
	public GameData() {
		this.enemies = new Vector<Enemy>();
		this.towers = new Vector<Tower>();
		this.shots = new Vector<Shot>();
		this.lives = 50;
		this.money = 100;
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

	public void setGameActive(boolean gameActive) {
		this.gameActive = gameActive;
	}
	public boolean isGameActive() {
		return this.gameActive;
	}

	public void setMoneyWarning(boolean warningActive) {
		this.moneyWarning = warningActive;
	}
	
	public boolean isMoneyWarningActive() {
		return this.moneyWarning;
	}

	public void setTowerToPlace(Tower tower) {
		this.towerToPlace = tower;
	}
	
	public Tower getTowerToPlace() {
		return this.towerToPlace;
	}

	public void setSelectedTower(Tower selectedTower) {
		// Unselect the last selected tower
		if (this.selectedTower != null){
			this.selectedTower.setIsSelected(false);
		}
		if (selectedTower != null ) {
			this.selectedTower = selectedTower;
			this.selectedTower.setIsSelected(true);
		} else {
			this.selectedTower = null;
		}
	}

	public Tower getSelectedTower() {
		return this.selectedTower;
	}
}

