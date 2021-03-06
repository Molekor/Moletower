package moletower;

/**
 * A fast but small enemy
 * 
 * @author Molekor
 *
 */
public class Fastenemy extends Enemy {

	private static final int baseLives = 2;
	private static final int baseValue = 3;
	private static final double baseSpeed = 3.2;
	private static final int deadDuration = 800;
	private static final String imagePath = "/Fastenemy.png";
	private static final int baseSize = 20;
	
	Fastenemy(Path path) throws Exception {
		super(imagePath, baseSpeed, baseValue, baseLives, deadDuration, baseSize);
	}

}
