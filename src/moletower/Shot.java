package moletower;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Shot {

	private double x;
	private double y;
	private double angle;
	private double speed = 5;
	private double range;
	private double movedDistance=0;
	private boolean hasReachedMaxRange = false;
	private long diedAt;
	private int deadDuration = 5000;
	private boolean canBeDeleted = false;
	
	public Shot(Point position, double angle, double range) {
		this.x = position.x;
		this.y = position.y;
		this.angle = angle;
		this.range = range;
	}

	public void paintComponent(Graphics g) {
		if(this.hasReachedMaxRange) {
			g.setColor(Color.BLACK);
			g.fillArc((int)this.x - 2, (int)this.y - 2, 4, 4, 0, 360);
		} else {
			g.setColor(Color.PINK);
			int dx = (int) (10 * Math.cos(this.angle));
			int dy = (int) (10 * Math.sin(this.angle));
			g.drawLine((int)this.x - dx, (int)this.y - dy, (int)this.x + dx, (int)this.y + dy);
		}
	}

	public Point getPosition() {
		return new Point((int)this.x, (int)this.y);
	}

	public void move() {
		if(this.hasReachedMaxRange) {
			if( (System.currentTimeMillis() - this.diedAt) > this.deadDuration) {
				this.canBeDeleted = true;
			}
			return;
		}
		if(this.movedDistance >= this.range) {
			this.hasReachedMaxRange  = true;
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
}
