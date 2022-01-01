package moletower;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Fasttower extends Tower {

	public static final int basePrice = 50;
	public static final int baseRange = 50;
	public static final int baseCooldown = 7;
	public static final int baseRadius = 8;
	
	public static final Color baseColor = Color.BLUE;
	
	public Fasttower(GameData gameData, Point position) {
		super(gameData, position, baseRange, baseCooldown, basePrice, baseRadius, baseColor);
	}
	
}
