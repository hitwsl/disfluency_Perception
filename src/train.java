import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
//Map<String, Double> pi = new HashMap<String, Double>();
//if ((pre_Processing.tag_Pro.containsKey("**" + tags[m]) == false))
//	pre_Processing.tag_Pro.put("**" + tags[m], 0.0);
//int wsl = word_Tag_Cont.get(a[i]);
//for (Entry<String, Integer> entry : word_Cont.entrySet())
//Map<String, Integer>
//	String gram_3 = entry.getKey();

public class train {
	final int NUMBEROFLABEl = 5;

	int lenOfw = 0 - NUMBEROFLABEl;
	int lenOfv = 0 - NUMBEROFLABEl;
	int lenOfvOfAverage = 0 - NUMBEROFLABEl;
	Map<String, Integer> w = new HashMap<String, Integer>();
	Map<String, Integer> v = new HashMap<String, Integer>();
	Map<String, Integer> vOfAverage = new HashMap<String, Integer>();
	Vector<Double> ValueOfw = new Vector<Double>();
	Vector<Double> ValueOfv = new Vector<Double>();
	Vector<Double> ValueOfvOfAverage = new Vector<Double>();
	Vector<pipe> AllOfSentences = new Vector<pipe>();
	Vector<String> allLabels = new Vector<String>();
	Map<String, Integer> valueOfLabels = new HashMap<String, Integer>();
	Vector<pipe> AllOfDevSentences = new Vector<pipe>();
	Map<String, Integer> updateOfv = new HashMap<String, Integer>();

	public train() {

		allLabels.add("B");
		allLabels.add("I");
		allLabels.add("E");
		allLabels.add("S");
		allLabels.add("O");
		valueOfLabels.put("B", 0);
		valueOfLabels.put("I", 1);
		valueOfLabels.put("E", 2);
		valueOfLabels.put("S", 3);
		valueOfLabels.put("O", 4);

	}

	void TrainModel(int N) throws Exception {

		int totaltime = 0;
		int vUpdateTime = 0;
		double valueOnDevelopment = 0.0;
		int t3 = (int) System.currentTimeMillis();
		for (int i = 0; i < N; i++) {
			for (Entry<String, Integer> entry1 : updateOfv.entrySet()) {
				updateOfv.put(entry1.getKey(), 0);
			}

			for (int j = 0; j < AllOfSentences.size(); j++) {
				if ((j % 5000) == 0) {
					System.out.println(j);
				}
				Vector<String> sentenceOfTags = new Vector<String>();
				sentenceOfTags = viterbiForTrain(AllOfSentences.elementAt(j));
				boolean isSame = compare(sentenceOfTags,
						AllOfSentences.elementAt(j).sentenceOfTags);
				if (isSame == false) {
					if (options.update_method.equals("PA")) {
						PAUpdate(sentenceOfTags, AllOfSentences.elementAt(j), j);
					} else {
						GeneralUpdate(sentenceOfTags,
								AllOfSentences.elementAt(j), j);
					}
				}
			}
			int t1 = (int) System.currentTimeMillis();
			vOfGeneralUpdate(AllOfSentences.size() - 1);
			int t2 = (int) System.currentTimeMillis();
			vUpdateTime += t2 - t1;

			// 在这里需要将v加起来
			vOfAverageOfUpdate(i + 1, AllOfSentences.size());
			double temp = DecodingForDevelop(i);
			if (i == 0) {
				valueOnDevelopment = temp;
				WriteModel(options.m_strModelName);
			} else {
				if (valueOnDevelopment < temp) {
					valueOnDevelopment = temp;
					boolean flag = (new File(options.m_strModelName)).delete();
					// System.out.println(flag);
					WriteModel(options.m_strModelName);
				}
			}
		}
		int t4 = (int) System.currentTimeMillis();
		System.out.println(valueOnDevelopment);
		totaltime = t4 - t3;
		System.out.println("totaltime is " + totaltime);
		System.out.println("vUpdateTime is " + vUpdateTime);
	}

	void WriteModel(String fileOutPath) throws IOException, Exception {
		File file = new File(fileOutPath);
		if (!file.exists()) {
			if (!file.createNewFile())
				throw new Exception("The file does not exist. creating fails");
		}
		OutputStreamWriter writer = new OutputStreamWriter(
				new FileOutputStream(file), "utf-8");
		BufferedWriter bw = new BufferedWriter(writer);
		bw.newLine();
		for (Entry<String, Integer> entry : vOfAverage.entrySet()) {
			bw.write(entry.getKey());
			for (int k = 0; k < allLabels.size(); k++) {
				bw.write(" "
						+ ValueOfvOfAverage.elementAt(entry.getValue() + k));
			}
			bw.newLine();
		}
		bw.close();
		writer.close();

	}

	void vOfAverageOfUpdate(int N, int T) {
		vOfAverage.clear();
		ValueOfvOfAverage.clear();
		lenOfvOfAverage = 0 - NUMBEROFLABEl;
		for (Entry<String, Integer> entry : v.entrySet()) {
			lenOfvOfAverage = lenOfvOfAverage + NUMBEROFLABEl;
			vOfAverage.put(entry.getKey(), lenOfvOfAverage);
			for (int k = 0; k < allLabels.size(); k++) {
				ValueOfvOfAverage.add(ValueOfv.elementAt(entry.getValue() + k)
						* 1.0 / (N * T));
			}
		}
	}

	void vOfGeneralUpdate(int T) {
		for (Entry<String, Integer> entry : updateOfv.entrySet()) {

			for (int k = 0; k < allLabels.size(); k++) {
				ValueOfv.setElementAt(
						ValueOfv.elementAt(v.get(entry.getKey()) + k)
								+ ValueOfw.elementAt(w.get(entry.getKey()) + k)
								* (T - entry.getValue() + 1),
						v.get(entry.getKey()) + k);
			}
		}
	}



	void GetUpdatedFeatures(Map<String, Integer> features,
			Vector<Double> ValueOfFeatures, Vector<String> sentenceOfTags,
			pipe instance) {
		int lenth = 0 - NUMBEROFLABEl;
		for (int i = 0; i < sentenceOfTags.size(); i++) {
			String preLabel = i > 0 ? sentenceOfTags.elementAt(i - 1) : "_BL_";
			preLabel = "BiLabels=" + preLabel;
			for (int j = 0; j < (instance.features.elementAt(i).size()); j++) {
				if (features.containsKey(instance.features.elementAt(i)
						.elementAt(j)) == false) {
					lenth = lenth + NUMBEROFLABEl;
					features.put(instance.features.elementAt(i).elementAt(j),
							lenth);
					for (int k = 0; k < allLabels.size(); k++) {
						ValueOfFeatures.add(0.0);
					}
				}
			}
			if (features.containsKey(preLabel) == false) {
				lenth = lenth + NUMBEROFLABEl;
				features.put(preLabel, lenth);
				for (int k = 0; k < allLabels.size(); k++) {
					ValueOfFeatures.add(0.0);
				}
			}

			for (int j = 0; j < (instance.features.elementAt(i).size()); j++) {
				if (j < instance.features.elementAt(i).size() - 1) {
					ValueOfFeatures.setElementAt(
							ValueOfFeatures.elementAt(features
									.get(instance.features.elementAt(i)
											.elementAt(j))
									+ valueOfLabels.get(instance.sentenceOfTags
											.elementAt(i))) + 1.0,
							features.get(instance.features.elementAt(i)
									.elementAt(j))
									+ valueOfLabels.get(instance.sentenceOfTags
											.elementAt(i)));
					ValueOfFeatures.setElementAt(
							ValueOfFeatures.elementAt(features
									.get(instance.features.elementAt(i)
											.elementAt(j))
									+ valueOfLabels.get(sentenceOfTags
											.elementAt(i))) - 1.0,
							features.get(instance.features.elementAt(i)
									.elementAt(j))
									+ valueOfLabels.get(sentenceOfTags
											.elementAt(i)));
				} else {
					ValueOfFeatures.setElementAt(
							ValueOfFeatures.elementAt(features
									.get(instance.features.elementAt(i)
											.elementAt(j))
									+ valueOfLabels.get(instance.sentenceOfTags
											.elementAt(i))) + 1.0,
							features.get(instance.features.elementAt(i)
									.elementAt(j))
									+ valueOfLabels.get(instance.sentenceOfTags
											.elementAt(i)));
					ValueOfFeatures.setElementAt(
							ValueOfFeatures.elementAt(features.get(preLabel)
									+ valueOfLabels.get(sentenceOfTags
											.elementAt(i))) - 1.0,
							features.get(preLabel)
									+ valueOfLabels.get(sentenceOfTags
											.elementAt(i)));
				}
			}
		}
	}

	/**
	 * 
	 * @param sentenceOfTags
	 *            : predicted labels
	 * @param instance
	 *            : global sentence instance
	 * @param T
	 *            : the T-th sentence of train
	 */

	int geterror(Vector<String> sentenceOfTags, pipe instance) {
		int error = 0;
		for (int i = 0; i < sentenceOfTags.size(); i++) {
			if (instance.sentenceOfTags.elementAt(i).equals(
					sentenceOfTags.elementAt(i)) == false) {
				error += 1;
			}
		}
		return error;
	}

	double getscore(Map<String, Integer> features,
			Vector<Double> ValueOfFeatures) {
		double score = 0.0;
		for (Entry<String, Integer> entry : features.entrySet()) {
			boolean temp = false;
			for (int j = 0; j < allLabels.size(); j++) {
				if (ValueOfFeatures.elementAt(entry.getValue() + j) != 0.0) {
					temp = true;
					break;
				}
			}
			if (temp == true) {
				if (w.containsKey(entry.getKey())) {
					for (int j = 0; j < allLabels.size(); j++) {
						score = score
								+ ValueOfw.elementAt(w.get(entry.getKey()) + j)
								* ValueOfFeatures.elementAt(entry.getValue()
										+ j);
					}
				}
			}
		}
		return score;
	}
	
	double getnorm(Map<String, Integer> features,
			Vector<Double> ValueOfFeatures) {
		double norm = 0.0;
		for (Entry<String, Integer> entry : features.entrySet()) {
			for (int j = 0; j < allLabels.size(); j++) {
				double temp = ValueOfFeatures.elementAt(entry.getValue() + j);
				if (temp != 0.0) {
					norm = norm + temp*temp;
					
				}
			}
		}		
		return norm;
	}

	void PAUpdate(Vector<String> sentenceOfTags, pipe instance, int T) {
		int lenth = 0 - NUMBEROFLABEl;
		Map<String, Integer> features = new HashMap<String, Integer>();
		Vector<Double> ValueOfFeatures = new Vector<Double>();
		GetUpdatedFeatures(features, ValueOfFeatures, sentenceOfTags, instance);
		int error = geterror(sentenceOfTags, instance);
		double score = getscore(features, ValueOfFeatures);
		double norm = getnorm(features, ValueOfFeatures);
		double step =0.0;
	    if (norm < 1e-8) {
	      step = 0;
	    }
	    else {
	      step = (error - score) / norm;
	    }
	    
		
	    
	  //  double upd = scale * itx->second;
	    //  double cur_val = _W[idx];

	     // _W[idx] = cur_val + upd;
	    
	    
		
		for (Entry<String, Integer> entry : features.entrySet()) {
			boolean temp = false;
			for (int j = 0; j < allLabels.size(); j++) {
				if (ValueOfFeatures.elementAt(entry.getValue() + j) != 0.0) {
					temp = true;
					break;
				}
			}
			if (temp == true) {
				if (w.containsKey(entry.getKey()) == false) {
					updateOfv.put(entry.getKey(), T);
					lenOfw = lenOfw + NUMBEROFLABEl;
					w.put(entry.getKey(), lenOfw);
					for (int k = 0; k < allLabels.size(); k++) {
						ValueOfw.add(ValueOfFeatures.elementAt(entry.getValue()
								+ k)*step);
					}
					lenOfv = lenOfv + NUMBEROFLABEl;
					v.put(entry.getKey(), lenOfv);
					for (int k = 0; k < allLabels.size(); k++) {
						ValueOfv.add(0.0);
					}
				} else {
					for (int k = 0; k < allLabels.size(); k++) {
						ValueOfv.setElementAt(
								ValueOfv.elementAt(v.get(entry.getKey()) + k)
										+ ValueOfw.elementAt(w.get(entry
												.getKey()) + k)
										* (T - updateOfv.get(entry.getKey())),
								v.get(entry.getKey()) + k);
						ValueOfw.setElementAt(
								ValueOfw.elementAt(w.get(entry.getKey()) + k)
										+ ValueOfFeatures.elementAt(entry
												.getValue() + k)*step,
								w.get(entry.getKey()) + k);
					}
					updateOfv.put(entry.getKey(), T);
				}
			}
		}
	}

	void GeneralUpdate(Vector<String> sentenceOfTags, pipe instance, int T) {
		int lenth = 0 - NUMBEROFLABEl;
		Map<String, Integer> features = new HashMap<String, Integer>();
		Vector<Double> ValueOfFeatures = new Vector<Double>();
		GetUpdatedFeatures(features, ValueOfFeatures, sentenceOfTags, instance);

		for (Entry<String, Integer> entry : features.entrySet()) {
			boolean temp = false;
			for (int j = 0; j < allLabels.size(); j++) {
				if (ValueOfFeatures.elementAt(entry.getValue() + j) != 0.0) {
					temp = true;
					break;
				}
			}
			if (temp == true) {
				if (w.containsKey(entry.getKey()) == false) {
					updateOfv.put(entry.getKey(), T);
					lenOfw = lenOfw + NUMBEROFLABEl;
					w.put(entry.getKey(), lenOfw);
					for (int k = 0; k < allLabels.size(); k++) {
						ValueOfw.add(ValueOfFeatures.elementAt(entry.getValue()
								+ k));
					}
					lenOfv = lenOfv + NUMBEROFLABEl;
					v.put(entry.getKey(), lenOfv);
					for (int k = 0; k < allLabels.size(); k++) {
						ValueOfv.add(0.0);
					}
				} else {
					for (int k = 0; k < allLabels.size(); k++) {
						ValueOfv.setElementAt(
								ValueOfv.elementAt(v.get(entry.getKey()) + k)
										+ ValueOfw.elementAt(w.get(entry
												.getKey()) + k)
										* (T - updateOfv.get(entry.getKey())),
								v.get(entry.getKey()) + k);
						ValueOfw.setElementAt(
								ValueOfw.elementAt(w.get(entry.getKey()) + k)
										+ ValueOfFeatures.elementAt(entry
												.getValue() + k),
								w.get(entry.getKey()) + k);
					}
					updateOfv.put(entry.getKey(), T);
				}
			}
		}
	}

	boolean compare(Vector<String> sentenceOfTags1,
			Vector<String> sentenceOfTags2) {// 判断两个向量内容是否一样
		boolean temp = true;
		for (int i = 0; i < sentenceOfTags1.size(); i++) {
			if (sentenceOfTags1.elementAt(i).equals(
					sentenceOfTags2.elementAt(i)) == false) {
				temp = false;
				break;
			}
		}
		return temp;
	}

	Vector<String> viterbiForTrain(pipe instance) {
		/*
		 * B:1 M:2 E:3 S:4
		 */
		Vector<String> resultOfDecoder = new Vector<String>();

		SegItem[][] itemMatrix = new SegItem[instance.sentence.size()][allLabels
				.size()];
		for (int i = 0; i < instance.sentence.size(); i++) {
			if (i == 0) {
				for (int j = 0; j < allLabels.size(); j++) {
					double score = getScoreForTrain(0, "_BL_", j, instance);
					itemMatrix[0][j] = new SegItem(score, 3);// score代表分数，3位置代表的是标签，由于此时的前一个标签没有意义，所以就随便赋了一个值
				}
			} else {
				for (int j = 0; j < allLabels.size(); j++) {
					int max = 0;
					double max_score = -10000000000.0;
					for (int k = 0; k < allLabels.size(); k++) {
						double score = getScoreForTrain(i,
								allLabels.elementAt(k), j, instance);
						score += itemMatrix[i - 1][k].score;
						if (max_score < score) {
							max = k;
							max_score = score;
						}
					}

					itemMatrix[i][j] = new SegItem(max_score, max);
				}
			}
		}
		resultOfDecoder = getViterbiResult(itemMatrix, instance.sentence.size());
		return resultOfDecoder;
	}

	Vector<String> viterbiForTest(pipe instance) {
		/*
		 * B:1 M:2 E:3 S:4
		 */
		Vector<String> resultOfDecoder = new Vector<String>();

		SegItem[][] itemMatrix = new SegItem[instance.sentence.size()][allLabels
				.size()];
		for (int i = 0; i < instance.sentence.size(); i++) {
			if (i == 0) {
				for (int j = 0; j < allLabels.size(); j++) {
					double score = getScoreForTest(0, "_BL_", j, instance);
					itemMatrix[0][j] = new SegItem(score, 3);
				}
			} else {

				for (int j = 0; j < allLabels.size(); j++) {
					int max = 0;
					double max_score = -1000000000.0;
					for (int k = 0; k < allLabels.size(); k++) {
						double score = getScoreForTest(i,
								allLabels.elementAt(k), j, instance);
						score += itemMatrix[i - 1][k].score;
						if (max_score < score) {
							max = k;
							max_score = score;
						}
					}

					itemMatrix[i][j] = new SegItem(max_score, max);
				}
			}
		}
		resultOfDecoder = getViterbiResult(itemMatrix, instance.sentence.size());
		return resultOfDecoder;

	}

	// BiLabels=_BL_
	Vector<String> getViterbiResult(SegItem[][] itemMatrix, int lenthOfSentence) {
		Vector<Integer> resultOfTemp = new Vector<Integer>();
		Vector<String> resultOfDecoder = new Vector<String>();
		int position = 0;
		double max_score = -10000000.0;
		for (int i = 0; i < allLabels.size(); i++) {
			if (itemMatrix[lenthOfSentence - 1][i].score > max_score) {
				position = i;
				max_score = itemMatrix[lenthOfSentence - 1][i].score;
			}
		}
		if (lenthOfSentence == 1) {
			resultOfTemp.add(4);
		} else {
			resultOfTemp.add(position);
			for (int j = lenthOfSentence - 1; j > 0; j--) {
				position = itemMatrix[j][position].label;
				resultOfTemp.add(position);
			}
		}

		for (int k = (resultOfTemp.size() - 1); k > -1; k--) {
			resultOfDecoder.add(allLabels.elementAt(resultOfTemp.elementAt(k)));
		}

		return resultOfDecoder;
	}

	double getScoreForTrain(int i, String preLabel, int curPosition,
			pipe instance) {
		double score = 0.0;
		for (int j = 0; j < (instance.features.elementAt(i).size() - 1); j++) {

			score += scoreComputingForTrain(curPosition, instance.features
					.elementAt(i).elementAt(j));
		}
		String preFeature = "BiLabels=" + preLabel;
		score += scoreComputingForTrain(curPosition, preFeature);
		return score;
	}

	double getScoreForTest(int i, String preLabel, int curPosition,
			pipe instance) {
		double score = 0;
		for (int j = 0; j < (instance.features.elementAt(i).size() - 1); j++) {

			score += scoreComputingForTest(curPosition, instance.features
					.elementAt(i).elementAt(j));
		}
		String preFeature = "BiLabels=" + preLabel;
		score += scoreComputingForTest(curPosition, preFeature);
		return score;
	}

	double scoreComputingForTrain(int curPosition, String feature) {
		double score = 0.0;
		if (w.containsKey(feature)) {
			score = ValueOfw.elementAt(w.get(feature) + curPosition);
		}
		return score;
	}

	double scoreComputingForTest(int curPosition, String feature) {
		double score = 0;
		if (vOfAverage.containsKey(feature)) {
			score = ValueOfvOfAverage.elementAt(vOfAverage.get(feature)
					+ curPosition);
		}
		return score;
	}

	// ValueOfw ValueOfvOfAverage
	double DecodingForDevelop(int N) throws Exception {
		double score = 0.0;
		int count = 0;
		evaluating precision_evaluating = new evaluating();
		for (int j = 0; j < AllOfDevSentences.size(); j++) {
			if ((j % 500) == 0) {
				System.out.println(j);
			}
			Vector<String> result = viterbiForTest(AllOfDevSentences
					.elementAt(j));
			precision_evaluating.getScore(result,
					AllOfDevSentences.elementAt(j));
		}
		precision_evaluating.printResult(N);
		score = precision_evaluating.numOfrigthWords
				* 2.0
				/ (precision_evaluating.numOfpredictWords + precision_evaluating.numOfgoldWords);
		return score;
	}

	void DecodingForTest() throws Exception {
		lenOfvOfAverage = 0 - NUMBEROFLABEl;
		vOfAverage.clear();
		ValueOfvOfAverage.clear();
		BufferedReader reader = myLib.openFile(options.m_strModelName);
		String line;
		while (((line = reader.readLine()) != null)) {
			if (line.length() > 0) {
				String[] temp = line.split(" ");
				lenOfvOfAverage += NUMBEROFLABEl;
				vOfAverage.put(temp[0], lenOfvOfAverage);
				for (int i = 0; i < allLabels.size(); i++) {
					ValueOfvOfAverage.add(Double.valueOf(temp[i + 1]));
				}
			}
		}
		reader.close();
		double score = 0.0;
		int count = 0;
		evaluating precision_evaluating = new evaluating();
		for (int j = 0; j < AllOfDevSentences.size(); j++) {
			if ((j % 500) == 0) {
				System.out.println(j);
			}
			Vector<String> result = viterbiForTest(AllOfDevSentences
					.elementAt(j));
			precision_evaluating.getScore(result,
					AllOfDevSentences.elementAt(j));
		}

		double P = precision_evaluating.numOfrigthWords * 1.0
				/ precision_evaluating.numOfpredictWords;
		double R = precision_evaluating.numOfrigthWords * 1.0
				/ precision_evaluating.numOfgoldWords;
		double F = precision_evaluating.numOfrigthWords
				* 2.0
				/ (precision_evaluating.numOfpredictWords + precision_evaluating.numOfgoldWords);
		System.out.println("the result On test is ");
		System.out.println("P = " + Double.toString(P));
		System.out.println("R = " + Double.toString(R));
		System.out.println("F = " + Double.toString(F));
		System.out.println("wrongLabel: " + precision_evaluating.wrongLabel);

	}

	void readInstanceForDevelop(String fileInPath) throws Exception {
		BufferedReader reader = myLib.openFile(fileInPath);
		if (reader != null) {
			int cont = 0;
			while (true) {
				pipe instance = new pipe();
				if (instance.readInstance(reader)) {
					instance.initSentenceFeatures();
					AllOfDevSentences.addElement(instance);
				} else
					break;
			}
		}
		reader.close();

	}

	void readInstanceForTest(String fileInPath) throws Exception {
		AllOfDevSentences.clear();
		BufferedReader reader = myLib.openFile(fileInPath);
		if (reader != null) {
			int cont = 0;
			while (true) {
				pipe instance = new pipe();
				if (instance.readInstance(reader)) {
					instance.initSentenceFeatures();
					AllOfDevSentences.addElement(instance);
				} else
					break;
			}
		}
		reader.close();
	}

	void readInstanceForTrain(String fileInPath) throws Exception {
		BufferedReader reader = myLib.openFile(fileInPath);
		if (reader != null) {
			int cont = 0;
			while (true) {
				pipe instance = new pipe();
				if (instance.readInstance(reader)) {
					instance.initSentenceFeatures();
					AllOfSentences.addElement(instance);
				} else
					break;
			}
		}
		reader.close();
	}
}
