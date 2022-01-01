package moletower;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Shot {

	private double x;
	private double y;
	private double angle;
	private double speed =15;
	private double range;
	private double movedDistance = 0;
	private long diedAt = 0;
	private int deadDuration = 1000;
	private boolean canBeDeleted = false;
	private boolean isLiving = true;
	private int length = 20;
	private int thickness = 3;

	public Shot(Point position, double angle, double range) {
		this.x = position.x;
		this.y = position.y;
		this.angle = angle;
		this.range = range;
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

	public double getAngle() {
		return this.angle;
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
