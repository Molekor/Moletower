package moletower;

import java.awt.Graphics;
import java.awt.Point;

public class GraphicsHelper {

	public static void drawThickLine(Graphics g, Point p1, Point p2, int thickness) {
		double angle = MathHelper.calculateAngle(p1.x, p1.y, p2.x, p2.y);
		double dx = (-(thickness/2) * Math.sin(angle));
		double dy = ((thickness/2) * Math.cos(angle));
		dx += (dx > 0) ? .5:-.5;
		dy += (dy > 0) ? .5:-.5;
		int xPoints[] = new int[4];
		int yPoints[] = new int[4];
		xPoints[0] = p1.x + (int)dx;
		yPoints[0] = p1.y + (int)dy;
		xPoints[1] = p1.x - (int)dx;
		yPoints[1] = p1.y - (int)dy;
		xPoints[2] = p2.x - (int)dx;
		yPoints[2] = p2.y - (int)dy;
		xPoints[3] = p2.x + (int)dx;
		yPoints[3] = p2.y + (int)dy;
		g.fillPolygon(xPoints, yPoints, 4);
	}

	public static void drawThickLineFromAngle(Graphics g, Point point, double angle, int thickness, int length) {
		int dx = (int) ((length/2) * Math.cos(angle));
		int dy = (int) ((length/2) * Math.sin(angle));
		drawThickLine(g, point, new Point(point.x + dx, point.y + dy),thickness);
	}
}
