package moletower;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Fasttower extends Tower {

	public static int basePrice = 50;
	
	public Fasttower(Point position) {
		super(position);
		this.range = 50;
		this.cooldown = 300;
		this.price = Fasttower.basePrice;
		this.radius = 8;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.DARK_GRAY);
		if (this.isActive || this.canBePlaced) {
			g.setColor(Color.BLUE);
		}
		g.fillArc(this.position.x - this.radius, this.position.y - this.radius, this.radius * 2, this.radius * 2, 0, 360);
		g.drawArc(this.position.x - this.range, this.position.y - this.range, this.range * 2, this.range * 2, 0, 360);
		g.setColor(Color.BLACK);
		g.fillArc(this.position.x - 2, this.position.y - 2, 4, 4, 0,360);
	}

}
