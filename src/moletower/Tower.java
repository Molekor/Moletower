package moletower;

import java.awt.Color;
import java.awt.Point;
import java.util.Iterator;
import java.util.Vector;

public abstract class Tower {

	protected Point position;
	protected int range;
	protected long cooldown;
	protected int price;
	protected boolean isActive = false;
	protected int radius;
	protected boolean canBePlaced = false;
	protected GameData gameData;
	protected long lastShootingTick = -1000;
	protected Color color; // @TODO Don't store paint information in the data object!

	protected Tower(GameData gameData, Point position, int range, int cooldown, int price, int radius, Color color) {
		this.gameData = gameData;
		this.position = position;
		this.range = range;
		this.cooldown = cooldown;
		this.price = price;
		this.radius = radius;
		this.color = color;
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
	
	public boolean checkDistance(Path path, Vector<Enemy> enemies) {
		double minDistance = Double.MAX_VALUE;
		Iterator<Point> pathPointIterator = path.getPathPoints().iterator();
		Point thisPathPoint = pathPointIterator.next();
		Point nextPathPoint;
		while (pathPointIterator.hasNext()) {
			nextPathPoint = pathPointIterator.next();
			double distance = MathHelper.getDistancePointToSegment(this.position, thisPathPoint, nextPathPoint) - (path.getThickness() / 2);
			if (distance < minDistance) {
				minDistance = distance;
			}
			thisPathPoint = nextPathPoint;
		}
		Iterator<Tower> towerIterator = this.gameData.getTowers().iterator();
		while (towerIterator.hasNext()) {
			Tower otherTower = towerIterator.next();
			double distance = MathHelper.getDistance(this.position, otherTower.getPosition()) - otherTower.getSize();
			if (distance < minDistance) {
				minDistance = distance;
			}
		}
		return minDistance > (this.gameData.getTowerToPlace().getSize());
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
}
