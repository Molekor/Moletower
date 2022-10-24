package moletower;

import java.awt.Point;
import java.io.IOException;
import java.util.Vector;

public class LevelDataFactory {

	public static LevelData getLevelData(int level, LevelDataLoader loader) throws IOException {
		Vector<String> rawData = loader.getRawLevelData("/level_" + level + ".csv");
		Path path = new Path();
		Vector<EnemyGroup> enemyGroups = new Vector<>();
		LevelData levelData = new LevelData(level);
		boolean fillingPath = false;
		boolean fillingEnemies = false;
		for (String line:rawData) {
			// the # starts a new data block
			if (line.startsWith("#")) {
				// end the open block
				fillingPath = false;
				fillingEnemies = false;
				if (line.equals("#path")) {
					fillingPath = true;
				}
				if (line.equals("#waves")) {
					fillingEnemies = true;
				}
			}
			// attatch line to the open block
			if (fillingPath) {
				String[] coords = line.split(",");
				if (coords.length == 2) {
					path.addPathPoint(new Point(Integer.parseInt(coords[0]), Integer.parseInt(coords[1])));
				}
			}
			if (fillingEnemies) {
				String[] groupData = line.split(",");
				// amount,type,pause
				if (groupData.length == 3) {
					EnemyGroup group = new EnemyGroup();
					group.setEnemyType(Integer.parseInt(groupData[0]));
					group.setAmount(Integer.parseInt(groupData[1]));
					group.setPause(Integer.parseInt(groupData[2]));
					enemyGroups.add(group);
				}
			}
			
		}
		levelData.setPath(path);
		levelData.setEnemyGroups(enemyGroups);
		return levelData;
	}
	
	
	
}
