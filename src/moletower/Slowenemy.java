package moletower;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

/**
 * A big and slow enemy
 * 
 * @author Molekor
 *
 */
public class Slowenemy extends Enemy {
	
	private Image image;
	public static int baseLives = 4;
	
	Slowenemy(Path path) throws Exception {
	
		super(path);
		ImageIcon ii = new ImageIcon("resources/Worg.png");
		image = ii.getImage();
		this.deadDuration = 500;
		this.speed = 1.8;
		this.value = 2;
		this.lives = baseLives;
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
			g.fillRect((int) this.x - 4, (int) this.y - 4, 8, 8);
		}
	}

	@Override
	public double getSize() {
		int height = this.image.getHeight(null);
		int width = this.image.getWidth(null);
		return Math.sqrt(width * width + height * height);
	}

}
