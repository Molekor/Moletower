package moletower.model;

import java.io.IOException;
import java.util.Vector;

abstract class LevelDataLoader {

	public abstract Vector<String> getRawLevelData(String levelFileName) throws IOException;

}
