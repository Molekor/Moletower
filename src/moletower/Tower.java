package moletower;

import java.awt.Color;
import java.awt.Point;
import java.util.Iterator;
import java.util.Vector;

public class Tower {
	
	private Point position;
	private int range;
	private long cooldown;
	private int price;
	private int size;
	private boolean isActive = false;
	private boolean canBePlaced = false;
	private long lastShootingTick = -1000;
	private Color color; // @TODO Don't store paint information in the data object!
	private boolean selected = false;
	private String name;
	private int upgradePrice = -1;
	private int damage;
	private int level = 1;
	private TowerData towerData;
	private boolean canBeUpgraded = true;
	
	public Tower(TowerData towerData) {
		this.position = new Point(1,1);
		this.towerData = towerData;
		this.name = towerData.getName();
		this.range = towerData.getRange(this.level);
		this.cooldown = towerData.getCooldown(this.level);
		this.price = towerData.getPrice(this.level);
		this.size = towerData.getSize();
		this.damage = towerData.getDamage(this.level);
		this.color = Color.RED;
		this.upgradePrice = towerData.getPrice(this.level + 1);
	}

	public Shot shoot(Vector<Enemy> enemies, long tick) {
		if ((tick - this.lastShootingTick) > this.cooldown) {
			Enemy target = this.findClosestEnemy(enemies);
			if ((target == null) || !target.isLiving()) {
				return null;
			}
			if (MathHelper.getDistance(target.getPosition(), this.position) > range) {
				return null;
			}
			this.lastShootingTick  = tick;
			double angle = MathHelper.calculateAngle(this.position.x, this.position.y, target.x, target.y);
			return new Shot(position, angle, this.range, this.damage);
		}
		return null;
	}
	
	/**
	 * 
	 * @param enemies : List of all enemies on the board
	 * @return the enemy closest to the given coordinates, null if no enemy is found
	 */
	public Enemy findClosestEnemy(Vector<Enemy> enemies) {
		Enemy closestEnemy = null;
		double smallestDistance = Double.MAX_VALUE;
		Iterator<Enemy> enemyIterator = enemies.iterator();
		while (enemyIterator.hasNext()) {
			Enemy currentEnemy = enemyIterator.next();
			if (!currentEnemy.isLiving()) {
				continue;
			}
			double currentDistance = MathHelper.getDistance(this.position, currentEnemy.getPosition());
			if (currentDistance < smallestDistance) {
				smallestDistance = currentDistance;
				closestEnemy = currentEnemy;
			}
		}
		return closestEnemy;
	}
	
	public boolean canBePlaced() {
		return canBePlaced;
	}

	public void setCanBePlaced(boolean canBePlaced) {
		this.canBePlaced = canBePlaced;
	}
	
	public Point getPosition() {
		return this.position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public int getPrice() {
		return this.price;
	}

	public double getSize() {
		return this.size;
	}
	
	public int getRange() {
		return this.range;
	}

	public Color getColor() {
		return this.color;
	}

	public void setIsSelected(boolean isSelected) {
		this.selected = isSelected;
	}
	
	public boolean isSelected() {
		return this.selected;
	}

	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public long getCooldown() {
		return cooldown;
	}

	public void setCooldown(long cooldown) {
		this.cooldown = cooldown;
	}
	
	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	public void upgrade() {
		if (this.level < TowerData.MAX_LEVEL) {
			this.level++;	
			this.range = towerData.getRange(this.level);
			this.cooldown = towerData.getCooldown(this.level);
			this.damage = towerData.getDamage(this.level);
			this.price = towerData.getPrice(this.level);
			if (this.level < TowerData.MAX_LEVEL) {
				this.upgradePrice = towerData.getPrice(this.level + 1);
			} else {
				this.upgradePrice = -1;
				this.canBeUpgraded = false;
			}
		}
	}
	
	public boolean canBeUpgraded() {
		return this.canBeUpgraded;
	}
	
	public int getUpgradePrice() {
		return this.upgradePrice;
	}

	public int getLevel() {
		return this.level;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public boolean isActive() {
		return this.isActive;
	}
}
