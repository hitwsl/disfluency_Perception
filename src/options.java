import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
public class options {
	public static boolean m_isTrain = false;
	public static boolean m_isTest = false;
	public static String 	update_method = "";
	public static String 	m_strTrainFile = "";
	public static String  m_strEvalFile = "";				
	public static String m_strTestFile = "";
	public static String m_strDecodeFile = "";
	public static String m_strOutFile = "";
	public static String m_strModelName = "default.model";
	public static int iteration = 1;
	static void setOptions(String fileInputPath) throws NumberFormatException, IOException{
		BufferedReader reader = myLib.openFile(fileInputPath);
		String line="";
		while (((line = reader.readLine()) != null)) {
			if (line.length() > 0) {
				String a[] = line.split(" ");
				if(a[0].equals("train")){
					m_isTrain=true;
				}
				else if(a[0].equals("train-file")){
					m_strTrainFile=a[2];
				}
				else if(a[0].equals("develop-file")){
					m_strEvalFile=a[2];
				}
				else if(a[0].equals("model-name")){
					m_strModelName=a[2];
				}
				else if(a[0].equals("update")){
					update_method = a[2];
				}
				else if(a[0].equals("iteration")){
					iteration= Integer.parseInt(a[2]);
				}
				
			
				else if(a[0].equals("test")){
					m_isTest=true;
				}
				else if(a[0].equals("test-file")){
					m_strTestFile=a[2];
				}
				else if(a[0].equals("output-file")){
					m_strOutFile=a[2];
				}
			}
		}
		reader.close();
	}
	

}
