package emotionRecognizer;

import contraganda.MainEmotionRecognizer;

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
	
	
	public int getEmotionModified(Double mod)
	{
		if (mod > 0)
		{
			if ((arousal+mod>=5.0) && (valence+mod>=5.0)) 		return MainEmotionRecognizer.HAPPY;
			else if ((arousal+mod>=5.0) && (valence+mod<5.0))   return MainEmotionRecognizer.ANGRY;
			else if ((arousal+mod<5.0) && (valence+mod>=5.0))  	return MainEmotionRecognizer.RELAXED;
			else if ((arousal+mod<5.0) && (valence+mod<=5.0))   return MainEmotionRecognizer.SAD;
			else return -1;
		}		
		else
		{
			if ((arousal>=5.0) && (valence>=5.0)) 		return MainEmotionRecognizer.SAD;
			else if ((arousal>=5.0) && (valence<5.0))   return MainEmotionRecognizer.RELAXED;
			else if ((arousal<5.0) && (valence>=5.0))  	return MainEmotionRecognizer.ANGRY;
			else if ((arousal<5.0) && (valence<=5.0))   return MainEmotionRecognizer.HAPPY;
			else return -1;	
		}
	}
	
	
	public int getEmotionNegative()
	{
		if ((arousal>=5.0) && (valence>=5.0)) 		return MainEmotionRecognizer.SAD;
		else if ((arousal>=5.0) && (valence<5.0))   return MainEmotionRecognizer.RELAXED;
		else if ((arousal<5.0) && (valence>=5.0))  	return MainEmotionRecognizer.ANGRY;
		else if ((arousal<5.0) && (valence<=5.0))   return MainEmotionRecognizer.HAPPY;
		else return -1;		
	}
}
