package moletower;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.awt.Point;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EnemyTest {

	class TestEnemy extends Enemy {
		
		public static int baseLives = 10;
		public static int baseValue = 3;
		public static double baseSpeed = 10;
		public static int deadDuration = 10;
		public static String imagePath = "foo/bar.png";
		public static int baseSize = 5;
		
		TestEnemy() {
			super(imagePath, baseSpeed, baseValue, baseLives, deadDuration, baseSize);
		}
	}
	
	private TestEnemy enemy;
	private Path path;
	private Point first = new Point(10,20);
	private Point second = new Point (20, 30);
	private Point third = new Point (20, 25);
	
	@BeforeEach
	void setupTestEnemy() {
		enemy = new TestEnemy();
	}
	
	@Test
	public void testConstructor() {
		assertEquals(this.enemy.getValue(), TestEnemy.baseValue);
		assertEquals(this.enemy.getLives(), TestEnemy.baseLives);
		assertEquals(this.enemy.getSpeed(), TestEnemy.baseSpeed);
		assertTrue(this.enemy.isLiving());
		assertFalse(this.enemy.hasReachedExit());
		assertEquals(this.enemy.getImagePath(), TestEnemy.imagePath);
		assertEquals(this.enemy.getSize(), TestEnemy.baseSize);
	}
	
	@Test
	public void testHit() {
		Shot shot = new Shot(new Point(1,1), 45, 10, 5);
		this.enemy.hit(shot);
		assertEquals(this.enemy.getLives(), 5);
		this.enemy.hit(shot);
		assertFalse(this.enemy.isLiving());
	}
	
	@Test
	public void testMove() {
		this.path = new Path();
		path.addPathPoint(first);
		path.addPathPoint(second);
		path.addPathPoint(third);
		enemy.setPath(path);
		assertEquals(this.enemy.getPosition(), first, "Enemy must start at first path point!");
		double startDistance = MathHelper.getDistance(this.enemy.getPosition(), second);
		this.enemy.move();
		double newDistance = MathHelper.getDistance(this.enemy.getPosition(), second);
		assertTrue("Distance to the next point should be reduced now!", newDistance < startDistance);
		// Distance to second point is less than 2x enemy speed, so the second move should reach the point
		this.enemy.move();
		assertEquals(this.enemy.getPosition(), second, "Enemy should be at the second waypoint now!");
		// Distance from the second to the last point is less than the enemy speed
		this.enemy.move();
		assertTrue("Enemy should have reached exit now!", this.enemy.hasReachedExit());
	}
}
