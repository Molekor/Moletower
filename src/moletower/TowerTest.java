package moletower;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;
import java.util.Vector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TowerTest {

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
	
	private Point towerPosition; 
	private Tower testTower;
	private Vector<Enemy> enemies;
	private TowerData towerData = new TowerData();
	
	@BeforeEach
	void setupTestTower() {
		this.towerData.setPrice(10,1);
		this.towerData.setPrice(30,2);
		this.towerData.setPrice(80,3);
		this.towerData.setCooldown(2,1);
		this.towerData.setCooldown(3,2);
		this.towerData.setDamage(5,1);
		this.towerData.setDamage(10,2);
		this.towerData.setName("Tower for testing");
		this.towerData.setRange(50,1);
		this.towerData.setRange(80,2);
		this.towerData.setSize(30);
		this.towerPosition = new Point(1,1);
		this.testTower = new Tower(this.towerData);
		this.enemies = new Vector<Enemy>();
	}
	
	@Test
	void testConstructor() {
		assertEquals(this.towerPosition, this.testTower.getPosition());
		assertEquals(this.towerData.getPrice(1), this.testTower.getPrice());
		assertEquals(this.towerData.getCooldown(1), this.testTower.getCooldown());
		assertEquals(this.towerData.getRange(1), this.testTower.getRange());
		assertEquals(this.towerData.getDamage(1), this.testTower.getDamage());
		assertEquals(this.towerData.getName(), this.testTower.getName());
		assertEquals(this.towerData.getSize(), this.testTower.getSize());
		assertFalse(this.testTower.isSelected());
		assertFalse(this.testTower.isActive());
	}
	
	@Test
	void dontShootAtNull() {
		this.testTower.setActive(true);
		assertNull(testTower.shoot(this.enemies, 1),"Tower fired at an empty enemy vector!");
	}
	
	@Test
	void dontShootAtOutOfRange() {
		this.testTower.setActive(true);
		TestEnemy farEnemy = new TestEnemy();
		farEnemy.setPosition(new Point(1,1000));
		this.enemies.add(farEnemy);
		assertNull(this.testTower.shoot(this.enemies, 1),"Tower fired a shot at an out of range target! Range: " + this.testTower.getRange());
	}
	
	@Test
	void firstShot() {
		this.testTower.setActive(true);
		TestEnemy closeEnemy = new TestEnemy();
		closeEnemy.setPosition(new Point(1,10));
		this.enemies.add(closeEnemy);
		assertInstanceOf(Shot.class, this.testTower.shoot(this.enemies, 1), "Fresh tower did not shoot!");
	}
	@Test
	void shotAngle() {
		this.testTower.setActive(true);
		TestEnemy closeEnemy = new TestEnemy();
		closeEnemy.setPosition(new Point(2,2));
		this.enemies.add(closeEnemy);
		Shot shot = this.testTower.shoot(this.enemies, 1);
		assertEquals(45.0,Math.toDegrees(shot.getAngle()));
	}
	
	@Test
	void secondShot() {
		this.testTower.setActive(true);
		TestEnemy closeEnemy = new TestEnemy();
		closeEnemy.setPosition(new Point(2,2));
		this.enemies.add(closeEnemy);
		assertInstanceOf(Shot.class, this.testTower.shoot(this.enemies, (long)1), "Fresh tower did not shoot!");
		assertNull(this.testTower.shoot(this.enemies, (long)2),"Tower fired too soon!");
		assertNull(this.testTower.shoot(this.enemies, (long)3),"Tower fired too soon!");
		assertInstanceOf(Shot.class, this.testTower.shoot(this.enemies, (long)4), "Tower did not shoot after Cooldown!");
	}
	
	@Test
	void testUpgrade() {
		// Assert that a fresh tower can be upgraded
		assertTrue(this.testTower.canBeUpgraded());
		// Upgrade the tower from level 1 to 2
		this.testTower.upgrade();
		// Check current tower data for level 2
		assertEquals(this.towerData.getPrice(2), this.testTower.getPrice());
		assertEquals(this.towerData.getCooldown(2), this.testTower.getCooldown());
		assertEquals(this.towerData.getRange(2), this.testTower.getRange());
		assertEquals(this.towerData.getDamage(2), this.testTower.getDamage());
		// Upgrade cost is the level 3 price
		assertEquals(this.towerData.getPrice(3), this.testTower.getUpgradePrice());
		// Upgrade to max level
		while (this.testTower.getLevel() < TowerData.MAX_LEVEL) {
			assertTrue(this.testTower.canBeUpgraded());
			this.testTower.upgrade();
		}
		// Assert that no more upgrade is possible now
		assertFalse(this.testTower.canBeUpgraded());
	}
}
