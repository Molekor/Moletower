package moletower;

/**
 * A big and slow enemy
 * 
 * @author Molekor
 *
 */
public class Slowenemy extends Enemy {
	
	public static int baseLives = 4;
	public static int baseValue = 2;
	public static double baseSpeed = 1.8;
	public static int deadDuration = 500;
	public static String imagePath = "/Worg.png";
	public static String imagePathDamaged = "/Worg_damaged.png";
	private static int baseSize = 25;
	
	Slowenemy(Path path) throws Exception {
		super(path, imagePath, imagePathDamaged, baseSpeed, baseValue, baseLives, deadDuration, baseSize);
	}

}
