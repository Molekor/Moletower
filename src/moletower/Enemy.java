package moletower;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.List;

import javax.swing.ImageIcon;

public abstract class Enemy {

	protected List<Point> pathPoints;
	protected int nextPathPoint = 1;
	protected double speed;
	protected double x;
	protected double y;
	protected boolean hasReachedExit = false;
	protected long diedAt = 0;
	protected long deadDuration;
	protected boolean isLiving = true;
	protected boolean canBeDeleted = false;

	Enemy(Path path) throws Exception {
		this.pathPoints = path.getPathPoints();
		if (pathPoints.size() < 2) {
			throw new Exception("Too few pathpoints, need at least 2!");
		}
		Point start = pathPoints.get(0);
		this.x = start.x;
		this.y = start.y;
	}

	public abstract void paintComponent(Graphics g);

	public void move() {
		if (this.hasReachedExit) {
			return;
		}
		if (!this.isLiving ) {
			if (System.currentTimeMillis() - this.diedAt > this.deadDuration) {
				this.canBeDeleted = true;
			}
			return;
		}
		Point destination = this.pathPoints.get(nextPathPoint);
		double dx = destination.x - this.x;
		double dy = destination.y - this.y;
		double angle = Moletower.calculateAngle(this.x, this.y, destination.x, destination.y);

		double xMove = speed * Math.cos(angle);
		double yMove = speed * Math.sin(angle);
		double traveled = Math.sqrt(xMove * xMove + yMove * yMove);
		double distance = Math.sqrt(dx * dx + dy * dy);

		this.x = this.x + xMove;
		this.y = this.y + yMove;

		if (distance <= traveled) {
			if (nextPathPoint == this.pathPoints.size() - 1) {
				this.hasReachedExit = true;
			} else {
				this.x = destination.x;
				this.y = destination.y;
				nextPathPoint++;
			}
		}
	}

	public Point getPosition() {
		return new Point((int) this.x, (int) this.y);
	}

	public boolean hasReachedExit() {
		return this.hasReachedExit;
	}

	public abstract double getSize();

	public void hit() {
		this.diedAt = System.currentTimeMillis();
		this.isLiving = false;
	}

	public boolean canBeDeleted() {
		return this.canBeDeleted;
	}

	public boolean isLiving() {
		return this.isLiving;
	}
}