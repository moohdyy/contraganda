package emotionRecognizer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class EmotionParser 
{		
	HashMap<String, AffectiveValues> dictMap = new HashMap <String, AffectiveValues>();	
	
	public EmotionParser(String path)
	{
		// read dictionary from file
		try {
		    BufferedReader in = new BufferedReader(new FileReader(path));
			String str;
			boolean first = true;;
		    while ((str = in.readLine()) != null)
		    {	
		    	if (first == true)
		    	{
		    		first = false;
		    		continue;
		    	}
		    	String parts[] = str.split(",");
		    	dictMap.put(parts[1], new AffectiveValues(	Double.parseDouble(parts[2]), 
		    									 			Double.parseDouble(parts[5]), 
		    									 			Double.parseDouble(parts[8])));
		    }
		    in.close();
		} catch (IOException e) {e.printStackTrace();}
	}
	
	
	
	/**
	 * simple model: returns the amount of words for each of 4 emotions
	 * @param str
	 * @return
	 */
	private double[] getEmotion1Sentence(String str)
	{
		String words[] = str.split(" ");
		double ret[] = {0,0,0,0};
				
		for (String s:words)
		{
			if (this.dictMap.containsKey(s))
			{
				ret[this.dictMap.get(s).getEmotion()]++;
			}
		}
		return ret;
	}
	
	
	
	/**
	 * returns emotion object, containing class and strength of emotion
	 * @param s
	 * @return
	 */
	private Emotion getEmotion1Word(String s)
	{
		int cls = -1;
		double strength = 1;
		if (this.dictMap.containsKey(s))
		{
			cls = this.dictMap.get(s).getEmotion();
		}
		
		return new Emotion(cls, strength);
	}
	
	
	
	/**
	 * returns sum of words for each emotion
	 * @param inList
	 * @return
	 */
	public double[] getEmotion(List<String> inList)
	{
		double sums[] = {0,0,0,0};
		StanfordLemmatizer slem = new StanfordLemmatizer();
		
		// create porter algorithm object
		Porter porterAlgo = new Porter();
		
		// loop over all sentences
		for (String sent : inList)
		{
			// first lemmatize the sentence
			List<String> words = slem.lemmatize(sent);
			
			// get modifier
			// get all modifier and negotiations from the sentence
			
			
			// loop over all words in a sentence
			for (String word : words)
			{
				// try to get emotion value 
				Emotion emo = getEmotion1Word(word);
				
				if (emo.emoClass == -1)
				{
					// apply Porter's algorithm and try again
					String tmpStr = porterAlgo.stripAffixes(word);
					emo = getEmotion1Word(tmpStr);
				}
				
				// add word to corresponding emotion sum
				if (emo.emoClass!=-1) sums[emo.emoClass]+=emo.emoStrength;
			}
		}
		
		return sums;
	}
	
	
	
	public class Emotion
	{
		int 	emoClass;
		double 	emoStrength;
		
		public Emotion (int c, double s)
		{
			this.emoClass = c;
			this.emoStrength = s;
		}
	}
}


