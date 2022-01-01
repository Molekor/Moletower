package moletower;

import java.awt.Color;
import java.awt.Point;

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

	public Shot shoot(Point target) {
		if (target == null) {
			return null;
		}
		if (MathHelper.getDistance(target, position) > range) {
			return null;
		}
		if ((this.gameData.getTick() - this.lastShootingTick) > this.cooldown) {

			this.lastShootingTick  = this.gameData.getTick();
			double angle = MathHelper.calculateAngle(this.position.x, this.position.y, target.x, target.y);
			return new Shot(position, angle, this.range);
		}
		return null;
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
