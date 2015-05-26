
package emotionRecognizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.process.DocumentPreprocessor;


public class OLD_MainEmotionRecognizer{
	
	final static int HAPPY 		= 0;
	final static int ANGRY 		= 1;
	final static int SAD 		= 2;
	final static int RELAXED 	= 3;

    File   			fileIn;
    File   			fileOut;
    
    String 			pathPolarityDir;
    String 			pathAffectiveValues;
    String 			pathIntensifier;
    
    String		 	textWhole;
    int 			subjSentencesCounter = 0;
    int 			objSentencesCounter = 0;
    
    EmotionParser 	ep;
    Porter 			porterAlgo;
    
	public OLD_MainEmotionRecognizer(String pathIn, String pathOut, String pPD, String pAD, String pAI)
	{
		fileIn = new File(pathIn);
		fileOut = new File(pathOut);
		
		//create output file
		this.fileOut.getParentFile().mkdirs();
		
		// store polarity directory
		this.pathPolarityDir = pPD;
		
		// store path to affective values database
		this.pathAffectiveValues = pAD;
		this.pathIntensifier = pAI;
		ep = new EmotionParser(this.pathAffectiveValues, this.pathIntensifier);
		
		// create porter algorithm object
		porterAlgo = new Porter();
	}
	
	
	public void output()
	{	
		// step 1: extract text from file
		textWhole = FileFunctions.extractTextFromFile(fileIn);
		
		// step 2: partiate sentences
		List<String> 	sentences1 = sentenceSplitter(textWhole);
		
		// step 3: filter out objective sentences
		List<String>	sentences2 =  filterObjectiveSentences(sentences1);
		
		// step 4: identify opinion words
		// step 6: investigate modifiers and negations
		// step 7: compute opinion
		double [] opinionSums1 = ep.getEmotion(sentences2);
		
		// output result
		opinionPrint(opinionSums1);

	}
	
	
	
	/**
	 * filters subjective sentences by recognizing and getting rid of the objectiv sentences,
	 * also stores the amount of subjectiv sentences into the global variable
	 * @param inList
	 * @return
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	private List<String> filterObjectiveSentences(List <String> inList) 
	{
		SubjectivityRecognizer sr = null;
		List<String> retList = new ArrayList<String>();
		
		// create classifier
		try 
		{			
			sr = new SubjectivityRecognizer(pathPolarityDir);
		
		// filter sentences
		for (String str : inList)
		{
			if (sr.getClass(str) == 0) 
			{
				// subjectiv
				retList.add(str);
				this.subjSentencesCounter++;
				//System.out.println("subjectiv sentence: "+str);
			}
			else
			{
				this.objSentencesCounter++;
				//System.out.println("objectiv sentence: "+str);
			}
		}
		} 
		catch (IOException | ClassNotFoundException e) 
		{
			System.out.println("failed to create Subjectivity Recognizer");
		}
		return retList;
	}
	
	
	private List<String> sentenceSplitter(String text)
	{
		Reader reader = new StringReader(text);
		DocumentPreprocessor dp = new DocumentPreprocessor(reader);
		List<String> sentenceList = new ArrayList<String>();

		for (List<HasWord> sentence : dp) 
		{
		   String sentenceString = Sentence.listToString(sentence);
		   sentenceList.add(sentenceString.toString());
		}
		
		return sentenceList;
	}		
	
	
	
	private int getWordsAmount()
	{
		return 0;
	}



	public void opinionPrint(double[] op)
	{
		System.out.println("HAPPY:"+op[HAPPY]);
		System.out.println("ANGRY:"+op[ANGRY]);
		System.out.println("SAD:"+op[SAD]);
		System.out.println("RELAXED:"+op[RELAXED]);
		System.out.println("obj:"+subjSentencesCounter);
		System.out.println("subj:"+objSentencesCounter);
	}
	
	
}
