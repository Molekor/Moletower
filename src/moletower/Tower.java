package moletower;

import java.awt.Graphics;
import java.awt.Point;

public abstract class Tower {

	protected Point position;
	protected int range;
	protected int cooldown;
	protected long lastShootingTime = 0;
	protected int price;
	protected boolean isActive = false;
	protected int radius;
	protected boolean canBePlaced = false;
	


	public Tower(Point position) {
		this.position = position;
	}

	public abstract void paintComponent(Graphics g);

	public Shot shoot(Point target) {
		if (target == null) {
			return null;
		}
		if (MathHelper.getDistance(target, position) > range) {
			return null;
		}
		if (System.currentTimeMillis() - lastShootingTime > cooldown) {
			lastShootingTime = System.currentTimeMillis();
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
}
