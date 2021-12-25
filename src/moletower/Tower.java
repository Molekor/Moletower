package moletower;

import java.awt.Graphics;
import java.awt.Point;

public abstract class Tower {

	protected Point position;
	protected int range;
	protected int cooldown;
	protected long lastShootingTime = 0;

	public Tower(Point position) {
		this.position = position;
	}

	public abstract void paintComponent(Graphics g);

	public Shot shoot(Point target) {
		if (target == null) {
			return null;
		}
		if (Moletower.getDistance(target, position) > range) {
			return null;
		}
		if (System.currentTimeMillis() - lastShootingTime > cooldown) {
			lastShootingTime = System.currentTimeMillis();
			double angle = Moletower.calculateAngle(this.position.x, this.position.y, target.x, target.y);
			return new Shot(position, angle, this.range);
		}
		return null;
	}

	public Point getPosition() {
		return this.position;
	}

}
