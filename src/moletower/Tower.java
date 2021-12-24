package moletower;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Tower {

	private Point position;
	private int radius = 5;
	private int range = 150;
	private int cooldown = 1000;
	private long lastShootingTime = 0;

	public Tower(Point position) {
		this.position = position;
	}

	public void paintComponent(Graphics g) {
		g.setColor(Color.red);
		g.fillArc(this.position.x - this.radius, this.position.y - this.radius, this.radius * 2, this.radius * 2, 0,
				360);
		g.drawArc(this.position.x - this.range, this.position.y - this.range, this.range * 2, this.range * 2, 0, 360);
	}

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
			return new Shot(position, angle);
		}
		return null;
	}

	public Point getPosition() {
		return this.position;
	}

}
