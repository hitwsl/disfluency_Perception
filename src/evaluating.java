import java.util.HashSet;
import java.util.Vector;

public class evaluating {
	int numOfgoldWords = 0;
	int numOfpredictWords = 0;
	int numOfrigthWords = 0;
	int wrongLabel=0;
	String[] DisfluencyOfLabel = { "B", "I", "E",  "S"};
	HashSet<String> DisLabel = new HashSet<String>();
	public evaluating() {
		for (int i = 0; i < DisfluencyOfLabel.length; i++)
			DisLabel.add(DisfluencyOfLabel[i]);
	}
    void printResult(int N){
    	double P=numOfrigthWords*1.0/numOfpredictWords;
    	double R=numOfrigthWords*1.0/numOfgoldWords;
    	double F=numOfrigthWords*2.0/(numOfpredictWords+numOfgoldWords);
    	System.out.println("the result of "+Integer.toString(N+1) +" iteration:");
    	System.out.println("P = "+Double.toString(P));
    	System.out.println("R = "+Double.toString(R));
    	System.out.println("F = "+Double.toString(F));
    	System.out.println("wrongLabel: "+wrongLabel);
    }
	
	void getScore(Vector<String> sentenceOfTags, pipe instance) {// 根据传递的参数进行处理。
		//String goldout = "";
		//String predictout = "";
		//boolean sta = false;
		for(int i=0;i<sentenceOfTags.size();i++)
		{
			if(DisLabel.contains(instance.sentenceOfTags.elementAt(i)))
			{
				//goldout = goldout + " " + instance.sentence.elementAt(i) + "/" +instance.sentenceOfTags.elementAt(i);
				numOfgoldWords += 1;
				if(DisLabel.contains(sentenceOfTags.elementAt(i)))
				{
					numOfrigthWords++;
				}
				//else{
				//	sta = true;
				//}
			}
			//else
			//{
			//	goldout = goldout + " " + instance.sentence.elementAt(i);
		//	}
			if(DisLabel.contains(sentenceOfTags.elementAt(i)))
			{
				//predictout = predictout + " " + instance.sentence.elementAt(i) + "/" + sentenceOfTags.elementAt(i);
				numOfpredictWords += 1;
			}	
			//else
			//{
				//predictout = predictout + " " + instance.sentence.elementAt(i) ;
			//}
		}
		//if (sta == true){
			//System.out.println(goldout);
			//System.out.println(predictout);
			//System.out.println();
		//}
		
	}

}
