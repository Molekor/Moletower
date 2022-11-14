package moletower.model;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.awt.Point;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import moletower.controller.MathHelper;

public class EnemyTest {
		
	private int baseLives = 10;
	private int baseValue = 3;
	private float baseSpeed = 10;
	private String imagePath = "foo/bar.png";
	private int baseSize = 5;
	
	private Enemy enemy;
	private Path path;
	private Point first = new Point(10,20);
	private Point second = new Point (20, 30);
	private Point third = new Point (20, 25);
	
	@BeforeEach
	void setupTestEnemy() {
		EnemyData enemyData = new EnemyData();
		enemyData.setHealth(baseLives);
		enemyData.setSize(baseSize);
		enemyData.setSpeed(baseSpeed);
		enemyData.setImage(imagePath);
		enemyData.setValue(baseValue);
		this.path = new Path();
		path.addPathPoint(first);
		path.addPathPoint(second);
		path.addPathPoint(third);
		enemy = new Enemy(enemyData, path);
	}
	
	@Test
	public void testConstructor() {
		assertEquals(this.enemy.getValue(), baseValue);
		assertEquals(this.enemy.getLives(), baseLives);
		assertEquals(this.enemy.getSpeed(), baseSpeed);
		assertEquals(this.enemy.getImagePath(), imagePath);
		assertEquals(this.enemy.getSize(), baseSize);
		assertTrue(this.enemy.isLiving());
		assertFalse(this.enemy.hasReachedExit());
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
