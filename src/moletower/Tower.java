package moletower;

import java.awt.Color;
import java.awt.Point;
import java.util.Iterator;
import java.util.Vector;

public class Tower {
	
	protected static final int MAX_LEVEL = 2;

	protected Point position;
	protected int range;
	protected long cooldown;
	protected int price;
	protected boolean isActive = false;
	protected int radius;
	protected boolean canBePlaced = false;
	protected long lastShootingTick = -1000;
	protected Color color; // @TODO Don't store paint information in the data object!
	protected boolean selected = false;
	protected String name;
	protected int upgradePrice = -1;
	protected int level = 0;
	protected TowerData towerData;
	protected boolean canBeUpgraded = true;
	
	public Tower(TowerData towerData) {
		this.towerData = towerData;
		this.name = towerData.getName();
		this.position = new Point(1,1);
		this.range = towerData.getRange(this.level);
		this.cooldown = towerData.getCooldown(this.level);
		this.price = towerData.getPrice(this.level);
		this.radius = towerData.getSize();
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
			return new Shot(position, angle, this.range);
		}
		return null;
	}
	
	/**
	 * 
	 * @param startPoint The coordinates from where we look
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

	public void setActive(boolean active) {
		this.isActive = active;
	}

	public int getPrice() {
		return this.price;
	}

	public double getSize() {
		return this.radius;
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

	public void upgrade() {
		if (this.level < Tower.MAX_LEVEL) {
			this.level++;	
			this.range = towerData.getRange(this.level);
			this.cooldown = towerData.getCooldown(this.level);
			if (this.level < Tower.MAX_LEVEL) {
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
		// TODO Auto-generated method stub
		return this.level;
	}
}
