package moletower.model;

import java.util.Vector;

public class LevelData {

	private int level;
	private Path path;
	private Vector<EnemyGroup> enemyGroups;
	
	public LevelData(int level) {
		this.level = level;
	}

	public Path getPath() {
		return this.path;
	}
	public Integer getLevel() {
		return this.level;
	}
	
	public void setPath(Path path) {
		this.path = path;
	}

	public void setEnemyGroups(Vector<EnemyGroup> enemyGroups) {
		this.enemyGroups = enemyGroups;
	}

	public Vector<EnemyGroup> getEnemyGroups() {
		return enemyGroups;
	}
	
}
