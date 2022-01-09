package moletower;

import java.awt.Color;
import java.awt.Point;

public class Firetower extends Tower {

	public static final int basePrice = 30;
	public static final int baseRange = 150;
	public static final int baseCooldown = 20;
	public static final int baseRadius = 30;
	public static final Color baseColor = Color.RED;
	
	public Firetower(GameData gameData, Point position) {
		super(gameData, position, baseRange, baseCooldown, basePrice, baseRadius, baseColor);
		this.name = "Firetower";
	}
	
}
