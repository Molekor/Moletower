package moletower;

import java.awt.Point;

public class MathHelper {
	public static double getDistance(Point p1, Point p2) {
		return Math.sqrt((p2.y - p1.y) * (p2.y - p1.y) + (p2.x - p1.x) * (p2.x - p1.x));
	}

	public static double calculateAngle(double ownX, double ownY, double targetX, double targetY) {
		double angle = Math.atan2(targetY - ownY, targetX - ownX);
		return angle;
	}
}
