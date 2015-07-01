import java.io.IOException;

public class main {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
//		myLib.yuLiaoQieFen("pku_training.utf8");
		System.out.println(args[0]);
		options.setOptions(args[0]);
		//System.out.println("zhebukexue88888");
		if (options.m_isTrain) {
			//library CreatingOfDictionary = new library();
			//CreatingOfDictionary.dictionary(options.m_strTrainFile,
			//		"dictionary");
			train wsl = new train();
			wsl.readInstanceForTrain(options.m_strTrainFile);
			wsl.readInstanceForDevelop(options.m_strEvalFile);
			System.out.println(options.m_strTrainFile+options.iteration);
			wsl.TrainModel(options.iteration);
			wsl.readInstanceForTest(options.m_strTestFile);
			wsl.DecodingForTest();
		}
		// System.out.println(options.m_isTrain);
		// System.out.println(options.m_strTrainFile);
		// TODO Auto-generated method stub
		// library CreatingOfDictionary=new library();
		// CreatingOfDictionary.dictionary(
		// "D:/wsl_workplace/SegOfPerception/data/pku_training.utf8","D:/wsl_workplace/SegOfPerception/data/dictionary");
//		train wsl = new train();
//		wsl.readInstanceForTrain("D:/wsl_workplace/SegOfPerception/data/wsl.txt");
//		wsl.TrainModel(10);
		// String raw_sen="澶ф眽鑸┖";
		// String subStr = raw_sen.substring(0,2);
		// System.out.println("澶ф眽鑸┖");
		// CharType.loadCharType();
		// String[] arr = (String[])CharType.digitSet.toArray(new String[0]);
		// for (String str:arr)
		// System.out.printf("for each : %s\n", str);
		String temp1 = "1234567";
//		int a[][] = new int[temp1.length()][4];
//		for (int i = 0; i < temp1.length(); i++) {
//			for (int j = 0; j < 4; j++) {
//				a[i][j] = i + j;
//			}
//		}
//		for (int i = 0; i < temp1.length(); i++) {
//			for (int j = 0; j < 4; j++) {
//				System.out.println(a[i][j]);
//			}
//		}

//		String temp = ToSBC("");
//		System.out.println(temp);
//		String wsl="杩欎笉绉戝寰楀埌鐨勶紝銆傘�銆傘�銆傘�銆傘�銆傘�銆傘�銆傦紝锛岋紝鐨勶紝锛�;
//		System.out.println(ToSBC(wsl));
		System.out.println("end");
	}

//	public static String ToSBC(String input) {
//
//		char[] c = input.toCharArray();
//		for (int i = 0; i < c.length; i++) {
//			if (c[i] == 32) {
//				c[i] = (char) 12288;
//				continue;
//			}
//			if (c[i] < 127)
//				c[i] = (char) (c[i] + 65248);
//		}
//		return new String(c);
//	}

}
