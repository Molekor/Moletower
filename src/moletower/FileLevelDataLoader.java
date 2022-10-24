package moletower;

import java.io.IOException;
import java.util.Vector;

public class FileLevelDataLoader extends LevelDataLoader {

	public Vector<String> getRawLevelData(String levelFileName) throws IOException {
		return IOHelper.parseFileToLines(levelFileName);
	}
	
}
