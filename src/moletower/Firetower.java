package moletower;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Firetower extends Tower {


	public static int basePrice = 30;
	
	public Firetower(Point position) {
		super(position);
		this.range = 150;
		this.cooldown = 800;
		this.price = basePrice;
		this.radius = 30;
	}
	
	public void paintComponent(Graphics g) {
		g.setColor(Color.DARK_GRAY);
		if (this.isActive || this.canBePlaced) {
			g.setColor(Color.RED);
		}
		g.fillArc(this.position.x - this.radius, this.position.y - this.radius, this.radius * 2, this.radius * 2, 0,
				360);
		g.drawArc(this.position.x - this.range, this.position.y - this.range, this.range * 2, this.range * 2, 0, 360);
		g.setColor(Color.BLACK);
		g.fillArc(this.position.x - 5, this.position.y - 5, 10, 10, 0,360);
	}

}
