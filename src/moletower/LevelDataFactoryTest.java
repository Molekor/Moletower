package moletower;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.Test;

public class LevelDataFactoryTest {

	@Test
	public void testGetGameData() {
		LevelData testData = LevelDataFactory.getLevelData(1);
		assertInstanceOf(LevelData.class, testData);
		assertEquals(testData.getLevel(), 1);
	}
}
