

/**
 * class for SEG item
 * a SEG item contains a instance and the score of the instance
 * this class is used in decoding
 * 
 * @author wsl
 * 
 */
public class SegItem
{
	public double score;
	public int label;

	public SegItem(double score, int label )
	{
		this.score = score;
		this.label = label;
	}

}
