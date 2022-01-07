package moletower;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.awt.Point;
import java.util.Vector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TowerTest {
	
	class TestTower extends Tower {

		public static final int basePrice = 10;
		public static final int baseRange = 100;
		public static final int baseCooldown = 2;
		public static final int baseRadius = 10;
		public static final Color baseColor = Color.BLACK;
		
		public TestTower(GameData gameData) {
			super(gameData, new Point(1,1),baseRange, baseCooldown, basePrice, baseRadius, baseColor);
		}		
	}
	
	class TestEnemy extends Enemy {
		
		public static int baseLives = 2;
		public static int baseValue = 3;
		public static double baseSpeed = 10;
		public static int deadDuration = 10;
		public static String imagePath = "foo/bar.png";
		public static int baseSize = 5;
		
		TestEnemy() {
			super(imagePath, baseSpeed, baseValue, baseLives, deadDuration, baseSize);
		}
	}
	
	Point towerPosition; 
	TestTower testTower;
	Vector<Enemy> enemies;
	private GameData gameData;
	
	@BeforeEach
	void setupTestTower() {
		this.towerPosition = new Point(1,1);
		this.testTower = new TestTower(this.gameData);
		this.enemies = new Vector<Enemy>();
	}
	
	@Test
	void testConstructor() {
		assertEquals(this.towerPosition, this.testTower.getPosition());
		assertEquals(TestTower.basePrice, this.testTower.getPrice());
		assertEquals(TestTower.baseRadius, this.testTower.getSize());
		assertEquals(TestTower.baseRange, this.testTower.getRange());
	}
	
	@Test
	void dontShootAtNull() {
		assertNull(testTower.shoot(this.enemies, 1),"Tower fired at an empty enemy vector!");
	}
	
	@Test
	void dontShootAtOutOfRange() {
		TestEnemy farEnemy = new TestEnemy();
		farEnemy.setPosition(new Point(1,1000));
		this.enemies.add(farEnemy);
		assertNull(this.testTower.shoot(this.enemies, 1),"Tower fired a shot at an out of range target! Range: " + TestTower.baseRange);
	}
	
	@Test
	void firstShot() {
		TestEnemy closeEnemy = new TestEnemy();
		closeEnemy.setPosition(new Point(1,10));
		this.enemies.add(closeEnemy);
		assertInstanceOf(Shot.class, this.testTower.shoot(this.enemies, 1), "Fresh tower did not shoot!");
	}
	@Test
	void shotAngle() {
		TestEnemy closeEnemy = new TestEnemy();
		closeEnemy.setPosition(new Point(2,2));
		this.enemies.add(closeEnemy);
		Shot shot = this.testTower.shoot(this.enemies, 1);
		assertEquals(45.0,Math.toDegrees(shot.getAngle()));
	}
	
	@Test
	void secondShot() {
		TestEnemy closeEnemy = new TestEnemy();
		closeEnemy.setPosition(new Point(2,2));
		this.enemies.add(closeEnemy);
		assertInstanceOf(Shot.class, this.testTower.shoot(this.enemies, (long)1), "Fresh tower did not shoot!");
		assertNull(this.testTower.shoot(this.enemies, (long)2),"Tower fired too soon!");
		assertNull(this.testTower.shoot(this.enemies, (long)3),"Tower fired too soon!");
		assertInstanceOf(Shot.class, this.testTower.shoot(this.enemies, (long)4), "Tower did not shoot after Cooldown!");
	}
}
