package moletower;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Fasttower extends Tower {

	private int radius = 3;
	
	public Fasttower(Point position) {
		super(position);
		this.range = 50;
		this.cooldown = 500;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.BLUE);
		g.fillArc(this.position.x - this.radius, this.position.y - this.radius, this.radius * 2, this.radius * 2, 0,
				360);
		g.drawArc(this.position.x - this.range, this.position.y - this.range, this.range * 2, this.range * 2, 0, 360);
	}

}
