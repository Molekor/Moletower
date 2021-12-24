package moletower;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Path {

	List<Point> pathPoints;

	public Path() {
		this.pathPoints = new ArrayList<Point>();
		this.pathPoints.add(new Point(50, 50));
		this.pathPoints.add(new Point(100, 50));
		this.pathPoints.add(new Point(200, 150));
		this.pathPoints.add(new Point(200, 250));
		this.pathPoints.add(new Point(650, 550));
		this.pathPoints.add(new Point(600, 50));
	}

	public List<Point> getPathPoints() {
		return this.pathPoints;
	}
}
