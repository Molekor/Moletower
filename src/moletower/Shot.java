package moletower;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Shot {

	private double x;
	private double y;
	private double angle;
	private double speed = 4;
	private double range;
	private double movedDistance = 0;
	private long diedAt = 0;
	private int deadDuration = 1000;
	private boolean canBeDeleted = false;
	private boolean isLiving = true;

	public Shot(Point position, double angle, double range) {
		this.x = position.x;
		this.y = position.y;
		this.angle = angle;
		this.range = range;
	}

	public void paintComponent(Graphics g) {
		if (this.isLiving) {
			g.setColor(Color.RED);
			int dx = (int) (5 * Math.cos(this.angle));
			int dy = (int) (5 * Math.sin(this.angle));
			g.drawLine((int) this.x - dx, (int) this.y - dy, (int) this.x + dx, (int) this.y + dy);
		} else {
			g.setColor(Color.RED);
			g.fillArc((int) this.x - 2, (int) this.y - 2, 4, 4, 0, 360);

		}
	}

	public Point getPosition() {
		return new Point((int) this.x, (int) this.y);
	}

	public void move() {
		if (!this.isLiving) {
			if ((System.currentTimeMillis() - this.diedAt) > this.deadDuration) {
				this.canBeDeleted = true;
			}
			return;
		}
		if (this.movedDistance >= this.range) {
			this.isLiving = false;
			this.diedAt = System.currentTimeMillis();
		} else {
			double dx = this.speed * Math.cos(this.angle);
			double dy = this.speed * Math.sin(this.angle);
			this.x = this.x + dx;
			this.y = this.y + dy;
			this.movedDistance = this.movedDistance + this.speed;
		}
	}

	public boolean canBeDeleted() {
		return this.canBeDeleted;
	}

	public void hit() {
		this.isLiving = false;
		this.diedAt = System.currentTimeMillis();
	}

	public boolean isLiving() {
		return this.isLiving;
	}
}
