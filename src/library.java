import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
// import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;
import java.io.*;

//Map<String, Double> pi = new HashMap<String, Double>();
//if ((pre_Processing.tag_Pro.containsKey("**" + tags[m]) == false))
//	pre_Processing.tag_Pro.put("**" + tags[m], 0.0);
//int wsl = word_Tag_Cont.get(a[i]);
//for (Entry<String, Integer> entry : word_Cont.entrySet())
//Map<String, Integer>
//	String gram_3 = entry.getKey();
public class library {
	void dictionary(String fileInPath, String fileOutPath) throws Exception {

		File file = new File(fileOutPath);
		if (!file.exists()) {
			if (!file.createNewFile())
				throw new Exception("The file does not exist. creating fails");
		}
		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file),"utf-8"); 
//		FileWriter writer = new FileWriter(fileOutPath);
		BufferedWriter bw = new BufferedWriter(writer);
		BufferedReader reader = myLib.openFile(fileInPath);
		String line;
		Map<String, Integer> dic = new HashMap<String, Integer>();
		while (((line = reader.readLine()) != null)) {
			if (line.length() > 0) {
				String a[] = line.split(" ");
				for (int i = 0; i < a.length; i++) {
					String StrTrans=a[i];
					if (dic.containsKey(StrTrans)) {
						int temp = dic.get(StrTrans);
						dic.put(StrTrans, temp + 1);
					} else {
						dic.put(StrTrans, 1);
					}
				}

			}
		}
		for (Entry<String, Integer> entry : dic.entrySet()) {
			if (entry.getValue() > 2) {
				bw.write(entry.getKey());
				bw.newLine();
			}
		}
		reader.close();
		bw.close();
		writer.close();
	}

}
