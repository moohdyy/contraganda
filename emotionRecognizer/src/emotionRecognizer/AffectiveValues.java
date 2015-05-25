package emotionRecognizer;

public class AffectiveValues {

	double arousal;
	double valence;
	double dominance;
		
	public AffectiveValues(double a, double v, double d)
	{
		this.arousal = a;
		this.valence = v;
		this.dominance = d;
	}
	
	
	/**
	 * simple model from Emotional Valence and Arousal Interact in the Control of Attention
	 * Lisa N. Jefferies (1), Daniel Smilek (2), Eric Eich (1) & James T. Enns (1)
	 * (1) University of British Columbia
	 * (2) University of Waterloo
	 * 
	 * @return
	 */
	public int getEmotion()
	{
		if ((arousal>=5.0) && (valence>=5.0)) 		return MainEmotionRecognizer.HAPPY;
		else if ((arousal>=5.0) && (valence<5.0))   return MainEmotionRecognizer.ANGRY;
		else if ((arousal<5.0) && (valence>=5.0))  	return MainEmotionRecognizer.RELAXED;
		else if ((arousal<5.0) && (valence<=5.0))   return MainEmotionRecognizer.SAD;
		else return -1;
	}
}
