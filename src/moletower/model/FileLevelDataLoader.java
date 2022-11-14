package moletower.model;

import java.io.IOException;
import java.util.Vector;

import moletower.controller.IOHelper;

public class FileLevelDataLoader extends LevelDataLoader {

	public Vector<String> getRawLevelData(String levelFileName) throws IOException {
		return IOHelper.parseFileToLines(levelFileName);
	}
	
}
