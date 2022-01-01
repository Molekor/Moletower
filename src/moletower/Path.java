package moletower;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Path {

	private static int thickness = 20;
	List<Point> pathPoints;

	public Path() {
		this.pathPoints = new ArrayList<Point>();
	}

	public List<Point> getPathPoints() {
		return this.pathPoints;
	}

	public int getThickness() {
		return Path.thickness ;
	}

	public void addPathPoint(Point point) {
		this.pathPoints.add(point);
		
	}

}
