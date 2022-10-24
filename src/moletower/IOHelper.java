package moletower;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

public class IOHelper {
	
	public static Vector<String> parseFileToLines(String filename) throws IOException {
		InputStream inputStream = IOHelper.class.getResourceAsStream(filename);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		Vector<String> lines = new Vector<String>();
		String line;
		while ((line = reader.readLine()) != null) {
			lines.add(line);
		}
		return lines;
	}
	
}
