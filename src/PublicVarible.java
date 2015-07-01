import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Vector;
import java.io.IOException;
public class PublicVarible {
	public static HashSet<String> dic = new HashSet<String>();
	public final static int MAX_LENGTH = 8;
	public static final String SUNDAY = "SUNDAY";
	public static final String startChar = "#STARTC#";
	public static final String endChar = "#ENDC#";
	public static final String seperateCH = "#SP#";
	public static void DicGeting() throws IOException{
		BufferedReader reader = myLib
				.openFile("dictionary");
		String line;
		while (((line = reader.readLine()) != null)) {
			if (line.length() > 0) {
				dic.add(line);
			}
		}
		reader.close();
		CharType.loadCharType();
	}
}
