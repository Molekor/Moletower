package moletower;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.List;

import javax.swing.ImageIcon;

public class Enemy {
	private Image image;
	private List<Point> pathPoints;
	private int nextPathPoint = 1;
	private static double speed = .8;
	private double x;
	private double y;
	private Boolean hasReachedExit = false;

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
		g.drawImage(image, (int) this.x - image.getWidth(null) / 2, (int) this.y - image.getHeight(null) / 2, null);
	}

	public void move() {
		if (this.hasReachedExit) {
			return;
		}
		Point destination = this.pathPoints.get(nextPathPoint);
		double dx = destination.x - this.x;
		double dy = destination.y - this.y;
		double angle = Moletower.calculateAngle(this.x, this.y, destination.x, destination.y);

		double xMove = speed * Math.cos(angle);
		double yMove = speed * Math.sin(angle);
		double traveled = Math.sqrt(xMove * xMove + (yMove * yMove));
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
}