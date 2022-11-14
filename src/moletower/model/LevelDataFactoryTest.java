package moletower.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;

import org.junit.Test;

public class LevelDataFactoryTest {
	
	class TestLevelDataLoader extends LevelDataLoader {

		@Override
		public Vector<String> getRawLevelData(String levelFileName) {
			// "#path" and "#waves" must be recognized, others ignored
			return new Vector<String> (
							Arrays.asList(
									"#path",
									"1,1",
									"1,2",
									"2,3",
									"#foo",
									"5,6",
									"#waves",
									"5,1,80",
									"2,2,50",
									"#bar",
									"2,3,4"
									)
							);
		}
	}
	
	@Test
	public void testGetGameData() {
		LevelData testData = null;
		try {
			testData = LevelDataFactory.getLevelData(1, new TestLevelDataLoader());
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertInstanceOf(LevelData.class, testData);
		assertEquals(1, testData.getLevel(), "Wrong level!");
		Path path = testData.getPath();
		assertEquals(3, path.getPathPoints().size(), "Wrong amount of path points!");
		Vector<EnemyGroup> enemyGroups = testData.getEnemyGroups();
		assertEquals(2, enemyGroups.size(), "Wrong amount of enemy groups!");
	}
}
