package moletower;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Shot {

	private Point position;
	private double angle;

	public Shot(Point position, double angle) {
		this.position = position;
		this.angle = angle;
	}

	public void paintComponent(Graphics g) {
		g.setColor(Color.PINK);
		int dx = (int) (150 * Math.cos(this.angle));
		int dy = (int) (150 * Math.sin(this.angle));
		g.drawLine(this.position.x, this.position.y, this.position.x + dx, this.position.y + dy);
	}

	public Point getPosition() {
		return this.position;
	}

}
