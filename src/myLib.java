import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Vector;
import java.util.Random;
import java.util.Map.Entry;

public class myLib {
	/*static String ToSBC(String input) {

		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 32) {
				c[i] = (char) 12288;
				continue;
			}
			if (c[i] < 127)
				c[i] = (char) (c[i] + 65248);
		}
		return new String(c);
	}
*/
	static BufferedReader openFile(String fileInPath) {
		BufferedReader reader = null;
		try {
			File f = new File(fileInPath);
			if (f.isFile() && f.exists()) {
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(f), "utf-8");
				reader = new BufferedReader(read);
			}
		} catch (Exception e) {
			System.out.println("读取文件内容操作出错");
			e.printStackTrace();
		}
		return reader;
	}

	static void yuLiaoQieFen(String fileInPath) throws Exception {
		BufferedReader reader = myLib.openFile(fileInPath);
		String line;
		int count = 0;
		while (((line = reader.readLine()) != null)) {
			if (line.length() > 0) {
				count++;
			}
		}
		reader.close();
		Random random = new Random();
		HashSet<Integer> number = new HashSet<Integer>();
		int cout2 = 0;
		int randeomOfNumber = 0;
		while (true) {
			randeomOfNumber = random.nextInt(count);
			if (number.contains(randeomOfNumber) == false) {
				number.add(randeomOfNumber);
				cout2++;
			}
			if (cout2 > count / 8) {
				break;
			}
		}
		File fileForTrain = new File(fileInPath + ".ForTrain");
		if (!fileForTrain.exists()) {
			if (!fileForTrain.createNewFile())
				throw new Exception("The file does not exist. creating fails");
		}
		OutputStreamWriter writerForTrain = new OutputStreamWriter(
				new FileOutputStream(fileForTrain), "utf-8");
		BufferedWriter bwForTrain = new BufferedWriter(writerForTrain);
		File fileForDev = new File(fileInPath + ".ForDev");
		if (!fileForDev.exists()) {
			if (!fileForDev.createNewFile())
				throw new Exception("The file does not exist. creating fails");
		}
		OutputStreamWriter writerForDev = new OutputStreamWriter(
				new FileOutputStream(fileForDev), "utf-8");
		BufferedWriter bwForDev = new BufferedWriter(writerForDev);
	
		BufferedReader reader1 = myLib.openFile(fileInPath);
		String line1;
		int count3=-1;
		while (((line1 = reader1.readLine()) != null)) {
			if (line1.length() > 0) {
				if( number.contains(++count3)){
					bwForDev.write(line1);
					bwForDev.newLine();
				}
				else{
					bwForTrain.write(line1);
					bwForTrain.newLine();
				}
			}
		}
		reader1.close();
		bwForDev.close();
		writerForDev.close();
		bwForTrain.close();
		writerForTrain.close();
	}

}
