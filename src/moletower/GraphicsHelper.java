package moletower;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;

import javax.imageio.ImageIO;

public class GraphicsHelper {

	private static class ImageHash {
		
		private String imagePath;
		private double brightness;

		public ImageHash(String imagePath, double brightness) {
			this.imagePath = imagePath;
			this.brightness = brightness;
		}
		
		@Override
		public boolean equals(Object other) {
			if (this == other) {
				return true;
			}
			if(other == null || other.getClass() != this.getClass()) {
	            return false;
			}
			ImageHash otherHash = (ImageHash) other;
			return (this.imagePath.equals(otherHash.imagePath) && (this.brightness == otherHash.brightness));
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(this.imagePath, this.brightness);
		}
	}
	
	private static HashMap<String, BufferedImage> baseImages = new HashMap<String, BufferedImage>();
	private static HashMap<ImageHash, BufferedImage> darkenedImages = new HashMap<ImageHash, BufferedImage>();
	
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
		GraphicsHelper.drawThickLine(g, point, new Point(point.x + dx, point.y + dy),thickness);
	}

	public static BufferedImage getDarkenedImage(String imagePath, float brightness) {
		// limit to 10 different shades
		brightness = Math.round(brightness * 10) /10f;
		ImageHash hash = new ImageHash(imagePath, brightness);
		BufferedImage image = GraphicsHelper.darkenedImages.get(hash);
		if (image == null) {
			image = GraphicsHelper.getImage(imagePath);
			RescaleOp rescaleOp = new RescaleOp(brightness, 0, null);
			image = rescaleOp.filter(image, null);
			GraphicsHelper.darkenedImages.put(new ImageHash(imagePath, brightness), image);
		}
		return image;
	}

	public static BufferedImage getImage(String imagePath) {
		BufferedImage image = GraphicsHelper.baseImages.get(imagePath);
		if (image == null) {		
			try {
				URL url = GraphicsHelper.class.getResource(imagePath);
				image = ImageIO.read(url);
				GraphicsHelper.baseImages.put(imagePath, image);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return image;
	}
}
