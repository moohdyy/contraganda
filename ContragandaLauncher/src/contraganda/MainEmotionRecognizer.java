package contraganda;


import org.json.simple.JSONObject;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.process.DocumentPreprocessor;
import emotionRecognizer.EmotionParser;
import emotionRecognizer.FileFunctions;
import emotionRecognizer.Porter;
import emotionRecognizer.SubjectivityRecognizer;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by moohdyy on 5/26/15.
 */
public class MainEmotionRecognizer {

    //from original src
    public final static int HAPPY 		= 0;
    public final static int ANGRY 		= 1;
    public final static int SAD 		= 2;
    public final static int RELAXED 	= 3;
    int 			subjSentencesCounter = 0;
    int 			objSentencesCounter = 0;
    
    String 			pathPolarityDir;
    String 			pathAffectiveValues;
    String 			pathIntensifier;
    
    String		 	textWhole;
    
    EmotionParser 	ep;
    Porter 			porterAlgo;
    
    double opinionSum[] = new double[4];


	public MainEmotionRecognizer(String article)
	{	
		textWhole = article;
		
		// store polarity directory
		this.pathPolarityDir = "D:/eclipse_workspace/emotionRecognizer/lib/polarityDir";
		
		// store path to affective values database
		this.pathAffectiveValues = "D:/eclipse_workspace/emotionRecognizer/lib/Ratings_Warriner_et_al.csv";
		this.pathIntensifier = "D:/eclipse_workspace/emotionRecognizer/lib/intensfiers.txt";
		ep = new EmotionParser(this.pathAffectiveValues, this.pathIntensifier);
		
		// create porter algorithm object
		porterAlgo = new Porter();
		
		// run the analyzer
		run();
	}
	
	
	public void run()
	{	
		// step 1: extract text from file
		//textWhole = FileFunctions.extractTextFromFile(fileIn);
		
		// step 2: partiate sentences
		List<String> 	sentences1 = sentenceSplitter(textWhole);
		
		// step 3: filter out objective sentences
		List<String>	sentences2 =  filterObjectiveSentences(sentences1);
		
		// step 4: identify opinion words
		// step 6: investigate modifiers and negations
		// step 7: compute opinion
		this.opinionSum = ep.getEmotion(sentences2);
		
		// output result
		//opinionPrint(this.opinionSum);
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



	public void opinionPrint(double[] op)
	{
		System.out.println("HAPPY:"+op[HAPPY]);
		System.out.println("ANGRY:"+op[ANGRY]);
		System.out.println("SAD:"+op[SAD]);
		System.out.println("RELAXED:"+op[RELAXED]);
		System.out.println("obj:"+subjSentencesCounter);
		System.out.println("subj:"+objSentencesCounter);
	}

    
    public JSONObject output(){
        JSONObject ret = new JSONObject();
        ret.put("HAPPY",this.opinionSum[HAPPY]);
        ret.put("ANGRY",this.opinionSum[ANGRY]);
        ret.put("SAD",this.opinionSum[SAD]);
        ret.put("RELAXED",this.opinionSum[RELAXED]);
        ret.put("obj",subjSentencesCounter);
        ret.put("subj",objSentencesCounter);
        return ret;
    }
}
