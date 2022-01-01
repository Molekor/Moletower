package moletower;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.awt.Point;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TowerTest {
	
	class TestTower extends Tower {

		public static final int basePrice = 10;
		public static final int baseRange = 100;
		public static final int baseCooldown = 2;
		public static final int baseRadius = 10;
		
		public static final Color baseColor = Color.BLACK;
		
		public TestTower(GameData gameData, Point position) {
			super(gameData, position, baseRange, baseCooldown, basePrice, baseRadius, baseColor);
		}		
	}
	
	Point towerPosition; 
	TestTower testTower;
	private GameData gameData;
	
	@BeforeEach
	void setupTestTower() {
		this.gameData = new GameData();
		this.towerPosition = new Point(1,1);
		this.testTower = new TestTower(this.gameData, this.towerPosition);
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
		assertNull(testTower.shoot(null),"Tower fired at a null value!");
	}
	
	@Test
	void dontShootAtOutOfRange() {
		int distance = TestTower.baseRange + 1;
		assertNull(this.testTower.shoot(new Point(2,200)),"Tower fired a shot at an out of range target! Dist: " + distance + " Range: " + TestTower.baseRange);
	}
	
	@Test
	void firstShot() {
		assertInstanceOf(Shot.class, this.testTower.shoot(new Point(2,2)), "Fresh tower did not shoot!");
	}
	@Test
	void shotAngle() {
		Shot shot = this.testTower.shoot(new Point(2,2));
		assertEquals(45.0,Math.toDegrees(shot.getAngle()));
	}
	
	@Test
	void secondShot() {
		assertInstanceOf(Shot.class, this.testTower.shoot(new Point(2,2)), "Fresh tower did not shoot!");
		this.gameData.addTick();
		assertNull(this.testTower.shoot(new Point(2,2)),"Tower fired too soon! Cooldown: " + TestTower.baseCooldown + ", Ticks since last shot: " + gameData.getTick());
		this.gameData.addTick();
		assertNull(this.testTower.shoot(new Point(2,2)),"Tower fired too soon! Cooldown: " + TestTower.baseCooldown + ", Ticks since last shot: " + gameData.getTick());
		this.gameData.addTick();
		assertInstanceOf(Shot.class, this.testTower.shoot(new Point(2,2)), "Tower did not shoot after Cooldown! Cooldown: " + TestTower.baseCooldown + ", Ticks since last shot: " + gameData.getTick());
	}
}
