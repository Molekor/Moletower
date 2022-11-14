package moletower.controller;

import java.awt.Point;
import java.util.Iterator;
import java.util.Vector;

import moletower.model.Path;
import moletower.model.Tower;

public class MathHelper {
	
	public static double getDistance(Point p1, Point p2) {
		return Math.sqrt((p2.y - p1.y) * (p2.y - p1.y) + (p2.x - p1.x) * (p2.x - p1.x));
	}

	public static double calculateAngle(double ownX, double ownY, double targetX, double targetY) {
		double angle = Math.atan2(targetY - ownY, targetX - ownX);
		return angle;
	}
	
	private static double sqr(double x) { 
		return x * x;
	}
	
	private static double dist2(Point v, Point w) { 
		return sqr(v.x - w.x) + sqr(v.y - w.y);
	}
	
	private static double distToSegmentSquared(Point p, Point v, Point w) {
	  double l2 = dist2(v, w);
	  if (l2 == 0) { 
		  return dist2(p, v);
	  }
	  double t = ((p.x - v.x) * (w.x - v.x) + (p.y - v.y) * (w.y - v.y)) / l2;
	  t = Math.max(0, Math.min(1, t));
	  return dist2(p, new Point( (int)(v.x + t * (w.x - v.x)), (int)(v.y + t * (w.y - v.y))) );
	}
	
	public static double getDistancePointToSegment(Point p, Point v, Point w) { 
		return Math.sqrt(distToSegmentSquared(p, v, w)); 
	}
	
	public static boolean checkDistance(Tower towerToPlace, Path path, Vector<Tower> towers) {
		double minDistance = Double.MAX_VALUE;
		Iterator<Point> pathPointIterator = path.getPathPoints().iterator();
		Point thisPathPoint = pathPointIterator.next();
		Point nextPathPoint;
		while (pathPointIterator.hasNext()) {
			nextPathPoint = pathPointIterator.next();
			double distance = MathHelper.getDistancePointToSegment(towerToPlace.getPosition(), thisPathPoint, nextPathPoint) - (path.getThickness() / 2);
			if (distance < minDistance) {
				minDistance = distance;
			}
			thisPathPoint = nextPathPoint;
		}
		Iterator<Tower> towerIterator = towers.iterator();
		while (towerIterator.hasNext()) {
			Tower otherTower = towerIterator.next();
			double distance = MathHelper.getDistance(towerToPlace.getPosition(), otherTower.getPosition()) - otherTower.getSize();
			if (distance < minDistance) {
				minDistance = distance;
			}
		}
		return minDistance > (towerToPlace.getSize());
	}
}
