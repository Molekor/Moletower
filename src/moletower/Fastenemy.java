package moletower;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

public class Fastenemy extends Enemy {

	private Image image;

	Fastenemy(Path path) throws Exception {
		super(path);
		ImageIcon ii = new ImageIcon("resources/Fastenemy.png");
		image = ii.getImage();
		this.deadDuration = 800;
		this.speed = 3.8;
	}

	@Override
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

	@Override
	public double getSize() {
		int height = this.image.getHeight(null);
		int width = this.image.getWidth(null);
		return Math.sqrt(width * width + height * height);
	}
}
