package moletower;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.List;

import javax.swing.ImageIcon;

public class Enemy {
	private Image image;
	private List<Point> pathPoints;
	private int nextPathPoint = 1;
	private static double speed = 1.1;
	private double x;
	private double y;
	private boolean hasReachedExit = false;
	private long diedAt = 0;
	private long deadDuration = 2000;
	private boolean isLiving = true;
	private boolean canBeDeleted = false;

	Enemy(Path path) throws Exception {
		this.pathPoints = path.getPathPoints();
		if (pathPoints.size() < 2) {
			throw new Exception("Too few pathpoints, need at least 2!");
		}
		Point start = pathPoints.get(0);
		this.x = start.x;
		this.y = start.y;
		ImageIcon ii = new ImageIcon("resources/Worg.png");
		image = ii.getImage();
	}

	public void paintComponent(Graphics g) {
		if (this.hasReachedExit) {
			return;
		}
		if (this.isLiving) {
			g.drawImage(image, (int) this.x - image.getWidth(null) / 2, (int) this.y - image.getHeight(null) / 2, null);
		} else {
			g.setColor(Color.BLACK);
			g.fillRect((int) this.x - 10, (int) this.y - 10, 20, 20);
		}
	}

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

	public double getSize() {
		int height = this.image.getHeight(null);
		int width = this.image.getWidth(null);
		return Math.sqrt(width * width + height * height);
	}

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