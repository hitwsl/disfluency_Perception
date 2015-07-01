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

public class pipe {

	//Map<String, Integer> dic = new HashMap<String, Integer>();
	Vector<String> sentence = new Vector<String>();
	Vector<String> postag = new Vector<String>();
	Vector<String> sentenceOfTags = new Vector<String>();
	Vector<Vector<String>> features = new Vector<Vector<String>>();
	//Vector<String> charType = new Vector<String>();
	//Vector<Integer> begin = new Vector<Integer>();//下面三个变量保存字典特征相关的内容,现在用不着
	//Vector<Integer> middle = new Vector<Integer>();
	//Vector<Integer> end = new Vector<Integer>();
	//String raw_sen = "";
	//Vector<String> words = new Vector<String>();

	/*
	 * 每次读取一句话，放置在sentence里面，并提取标注序列放到sentenceOfTags里面
	 */

	boolean readInstance(BufferedReader reader) throws IOException {
		sentence.clear();
		postag.clear();
		sentenceOfTags.clear();
		String strLine = "";
		while ((strLine = reader.readLine()) != null) {
			if (strLine.equals("")) {
				continue;
			} else {
				//raw_sen = myLib.ToSBC(strLine.replaceAll(" ", ""));
				String words_pos_tag[] = strLine.split(" ");
				for (int i = 0; i < words_pos_tag.length; i++) {
					String temp[] = words_pos_tag[i].split("/");
					//System.out.println(temp[0]);
					sentence.addElement(temp[0]);
					postag.addElement(temp[1]);
					sentenceOfTags.addElement(temp[2]);					
				}
				//ChartypeAndDicCharactor();
				return true;
			}
		}
		return false;
	}

	/*
	 * 特征提取函数。每次提取一句话的特征用features保存
	 */
	void initSentenceFeatures() {
		features.clear();
		int sentLength = sentence.size();
		// features.setSize(sentLength);
		for (int i = 0; i < sentLength; i++) {
			Vector<String> temp = new Vector<String>();
			features.addElement(temp);
		}
		for (int i = 0; i < sentLength; i++) {
			features.elementAt(i).clear();
			String prevChar = i - 1 >= 0 ? sentence.elementAt(i - 1)
					: PublicVarible.startChar;
			String prev2Char = i - 2 >= 0 ? sentence.elementAt(i - 2)
					: PublicVarible.startChar;
			String prev3Char = i - 3 >= 0 ? sentence.elementAt(i - 3)
					: PublicVarible.startChar;
			String prev4Char = i - 4 >= 0 ? sentence.elementAt(i - 4)
					: PublicVarible.startChar;
			String nextChar = i + 1 < sentence.size() ? sentence
					.elementAt(i + 1) : PublicVarible.endChar;
			String next2Char = i + 2 < sentence.size() ? sentence
					.elementAt(i + 2) : PublicVarible.endChar;
			String next3Char = i + 3 < sentence.size() ? sentence
						.elementAt(i + 3) : PublicVarible.endChar;
			String next4Char = i + 4 < sentence.size() ? sentence
						.elementAt(i + 4) : PublicVarible.endChar;
			String next5Char = i + 5 < sentence.size() ? sentence
						.elementAt(i + 5) : PublicVarible.endChar;
					
					
					
			String curChar = sentence.elementAt(i);
			String feat = "W0=" + prev2Char;
			features.elementAt(i).addElement(feat);
			feat = "W1=" + prevChar;
			features.elementAt(i).addElement(feat);
			feat = "W2=" + curChar;
			features.elementAt(i).addElement(feat);
			feat = "W3=" + nextChar;
			features.elementAt(i).addElement(feat);
			feat = "W4=" + next2Char;
			features.elementAt(i).addElement(feat);
			feat = "W5=" + prevChar + PublicVarible.seperateCH + curChar;
			features.elementAt(i).addElement(feat);
			feat = "W6=" + curChar + PublicVarible.seperateCH + nextChar;
			features.elementAt(i).addElement(feat);
		
					
			String prevPos = i - 1 >= 0 ? postag.elementAt(i - 1)
					: PublicVarible.startChar;
			String prev2Pos = i - 2 >= 0 ? postag.elementAt(i - 2)
					: PublicVarible.startChar;
			String prev3Pos = i - 3 >= 0 ? postag.elementAt(i - 3)
					: PublicVarible.startChar;
			String prev4Pos = i - 4 >= 0 ? postag.elementAt(i - 4)
					: PublicVarible.startChar;
			String nextPos = i + 1 < postag.size() ? postag
					.elementAt(i + 1) : PublicVarible.endChar;
			String next2Pos = i + 2 < postag.size() ? postag
					.elementAt(i + 2) : PublicVarible.endChar;
			String next3Pos = i + 3 < postag.size() ? postag
					.elementAt(i + 3) : PublicVarible.endChar;
			String next4Pos = i + 4 < postag.size() ? postag
					.elementAt(i + 4) : PublicVarible.endChar;
			String next5Pos = i + 5 < postag.size() ? postag
					.elementAt(i + 5) : PublicVarible.endChar;
			
			String curPos = postag.elementAt(i);
			
			
			
			feat = "P0=" + prev2Pos;
			features.elementAt(i).addElement(feat);
			feat = "P1=" + prevPos;
			features.elementAt(i).addElement(feat);
			feat = "P2=" + curPos;
			features.elementAt(i).addElement(feat);
			feat = "P3=" + nextPos;
			features.elementAt(i).addElement(feat);
			feat = "P4=" + next2Pos;
			features.elementAt(i).addElement(feat);
			feat = "P5=" + prevPos + PublicVarible.seperateCH + curPos;
			features.elementAt(i).addElement(feat);
			feat = "P6=" + curPos + PublicVarible.seperateCH + nextPos;
			features.elementAt(i).addElement(feat);
			feat = "P7=" + prev2Pos + PublicVarible.seperateCH + prevPos
					+ PublicVarible.seperateCH + curPos;
			features.elementAt(i).addElement(feat);
			feat = "P8=" + prevPos + PublicVarible.seperateCH + curPos
					+ PublicVarible.seperateCH + nextPos;
			features.elementAt(i).addElement(feat);
			feat = "P9=" + curPos + PublicVarible.seperateCH + nextPos
					+ PublicVarible.seperateCH + next2Pos;
			features.elementAt(i).addElement(feat);
			feat = "WP=" + curChar + PublicVarible.seperateCH + curPos;		
			features.elementAt(i).addElement(feat);
			
			
			if (prev4Char.equals(curChar))
				features.elementAt(i).addElement("-4ABABT");
			if (prev3Char.equals(curChar))
				features.elementAt(i).addElement("-3ABABT");
			if (prev2Char.equals(curChar))
				features.elementAt(i).addElement("-2ABABT");
			if (prevChar.equals(curChar))
				features.elementAt(i).addElement("-1ABABT");
			if (nextChar.equals(curChar))
				features.elementAt(i).addElement("1AABBT");
			if (next2Char.equals(curChar))
				features.elementAt(i).addElement("2AABBT");
			if (next3Char.equals(curChar))
				features.elementAt(i).addElement("3AABBT");
			if (next4Char.equals(curChar))
				features.elementAt(i).addElement("4AABBT");
			
			if (prev4Pos.equals(curPos))
				features.elementAt(i).addElement("-4ABABP");
			if (prev3Pos.equals(curPos))
				features.elementAt(i).addElement("-3ABABP");
			if (prev2Pos.equals(curPos))
				features.elementAt(i).addElement("-2ABABP");
			if (prevPos.equals(curPos))
				features.elementAt(i).addElement("-1ABABP");
			if (nextPos.equals(curPos))
				features.elementAt(i).addElement("1AABBP");
			if (next2Pos.equals(curPos))
				features.elementAt(i).addElement("2AABBP");
			if (next3Pos.equals(curPos))
				features.elementAt(i).addElement("3AABBP");
			if (next4Pos.equals(curPos))
				features.elementAt(i).addElement("4AABBP");
			
			String word_dumple = curChar + "#" + nextChar;
			String prev4CharDumple = prev4Char + "#" + prev3Char;
			String prev3CharDumple = prev3Char + "#" + prev2Char;
			String prev2CharDumple = prev2Char + "#" + prevChar;
			String prevCharDumple = prevChar + "#" + curChar;
			String nextCharDumple = nextChar + "#" + next2Char;
			String next2CharDumple = next2Char + "#" + next3Char;
			String next3CharDumple = next3Char + "#" + next4Char;
			String next4CharDumple = next4Char + "#" + next5Char;
					
			if (prev4CharDumple.equals(word_dumple))
				features.elementAt(i).addElement("-4DBABT");
			if (prev3CharDumple.equals(word_dumple))
				features.elementAt(i).addElement("-3DBABT");
			if (prev2CharDumple.equals(word_dumple))
				features.elementAt(i).addElement("-2DBABT");
			if (prevCharDumple.equals(word_dumple))
				features.elementAt(i).addElement("-1DBABT");
			if (nextCharDumple.equals(word_dumple))
				features.elementAt(i).addElement("1DBABT");
			if (next2CharDumple.equals(word_dumple))
				features.elementAt(i).addElement("2DBABT");
			if (next3CharDumple.equals(word_dumple))
				features.elementAt(i).addElement("3DBABT");
			if (next4CharDumple.equals(word_dumple))
				features.elementAt(i).addElement("4DBABT");

			String pos_dumple = curPos + "#" + nextPos;
			String prev4PosDumple = prev4Pos + "#" + prev3Pos;
			String prev3PosDumple = prev3Pos + "#" + prev2Pos;
			String prev2PosDumple = prev2Pos + "#" + prevPos;
			String prevPosDumple = prevPos + "#" + curPos;
			String nextPosDumple = nextPos + "#" + next2Pos;
			String next2PosDumple = next2Pos + "#" + next3Pos;
			String next3PosDumple = next3Pos + "#" + next4Pos;
			String next4PosDumple = next4Pos + "#" + next5Pos;
					
			if (prev4PosDumple.equals(pos_dumple))
				features.elementAt(i).addElement("-4DBABP");
			if (prev3PosDumple.equals(pos_dumple))
				features.elementAt(i).addElement("-3DBABP");
			if (prev2PosDumple.equals(pos_dumple))
				features.elementAt(i).addElement("-2DBABP");
			if (prevPosDumple.equals(pos_dumple))
				features.elementAt(i).addElement("-1DBABP");
			if (nextPosDumple.equals(pos_dumple))
				features.elementAt(i).addElement("1DBABP");
			if (next2PosDumple.equals(pos_dumple))
				features.elementAt(i).addElement("2DBABP");
			if (next3PosDumple.equals(pos_dumple))
				features.elementAt(i).addElement("3DBABP");
			if (next4PosDumple.equals(pos_dumple))
				features.elementAt(i).addElement("4DBABP");
	

			
			String preLabel = i > 0 ? sentenceOfTags.elementAt(i - 1) : "_BL_";
			features.elementAt(i).addElement("BiLabels=" + preLabel);
	
		}
	}

	
}
