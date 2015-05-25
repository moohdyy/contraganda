package emotionRecognizer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.File;
import java.io.IOException;

import com.aliasi.classify.Classification;
import com.aliasi.classify.Classified;
import com.aliasi.classify.ConditionalClassification;
import com.aliasi.classify.DynamicLMClassifier;
import com.aliasi.classify.JointClassifier;
import com.aliasi.classify.JointClassifierEvaluator;
import com.aliasi.lm.NGramProcessLM;
import com.aliasi.util.Files;

/**
 * is taken from : http://alias-i.com/lingpipe/demos/tutorial/sentiment/read-me.html
 *
 */

public class SubjectivityRecognizer {

    File mPolarityDir;
    String[] mCategories;
    DynamicLMClassifier<NGramProcessLM> mClassifier;
    JointClassifier<CharSequence> mSubjectivityClassifier;

    
    SubjectivityRecognizer(String path) throws IOException 
    {
        //System.out.println("\nBASIC SUBJECTIVITY DEMO");
        //mPolarityDir = new File(args[0]);
        mPolarityDir = new File(path);
        //System.out.println("\nData Directory=" + mPolarityDir);
        mCategories = new String[] { "plot", "quote" };
        int nGram = 8;
        mClassifier = DynamicLMClassifier.createNGramProcess(mCategories,nGram);
        train();
    }
    
    
    void train() throws IOException 
    {
    	File f = new File("subjectivity.model");
    	if(f.exists() && !f.isDirectory())
    	{
    		System.out.println("no need to train subjectivity/objectivity classifier");
    		return;
    	}
    		
    		
        int numTrainingChars = 0;
        System.out.println("\nTraining.");
        for (int i = 0; i < mCategories.length; ++i) 
        {
            String category = mCategories[i];
            Classification classification
                = new Classification(category);
            File file = new File(mPolarityDir,
                                 mCategories[i] + ".tok.gt9.5000");
            String data = Files.readFromFile(file,"ISO-8859-1");
            String[] sentences = data.split("\n");
            System.out.println("# Sentences " + category + "=" + sentences.length);
            int numTraining = (sentences.length * 9) / 10;
            for (int j = 0; j < numTraining; ++j) 
            {
                String sentence = sentences[j];
                numTrainingChars += sentence.length();
                Classified<CharSequence> classified
                    = new Classified<CharSequence>(sentence,classification);
                mClassifier.handle(classified);
            }
        }
        
        System.out.println("\nCompiling.\n  Model file=subjectivity.model");
        FileOutputStream fileOut = new FileOutputStream("subjectivity.model");
        ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
        mClassifier.compileTo(objOut);
        objOut.close();

        System.out.println("  # Training Cases=" + 9000);
        System.out.println("  # Training Chars=" + numTrainingChars);
    }

    
    
    void evaluate() throws IOException 
    {
        // classifier hasn't been compiled, so it'll be slower
        boolean storeInputs = false;
        JointClassifierEvaluator<CharSequence> evaluator
            = new JointClassifierEvaluator<CharSequence>(mClassifier, mCategories,storeInputs);
        System.out.println("\nEvaluating.");
        for (int i = 0; i < mCategories.length; ++i) {
            String category = mCategories[i];
            Classification classification
                = new Classification(category);
            File file = new File(mPolarityDir,
                                 mCategories[i] + ".tok.gt9.5000");
            String data = Files.readFromFile(file,"ISO-8859-1");
            String[] sentences = data.split("\n");
            int numTraining = (sentences.length * 9) / 10;
            for (int j = numTraining; j < sentences.length; ++j) {
                Classified<CharSequence> classified
                    = new Classified<CharSequence>(sentences[j],classification);
                evaluator.handle(classified);
            }
        }
        System.out.println();
        System.out.println(evaluator.toString());
    }

    
    
    /**
     * returns 0 if subjective, 1 if objective
     * @param sentence
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public int getClass(String sentence) throws IOException, ClassNotFoundException
    {
    	File modelFile = new File("subjectivity.model");
        //System.out.println("\nReading model from file="+ modelFile);
        FileInputStream fileIn
            = new FileInputStream(modelFile);
        ObjectInputStream objIn
            = new ObjectInputStream(fileIn);
        mSubjectivityClassifier
            = (JointClassifier<CharSequence>) objIn.readObject();
        objIn.close();
        
    	ConditionalClassification subjClassification = 
    			(ConditionalClassification)mSubjectivityClassifier.classify(sentence);

    if (subjClassification.category(0).equals("quote"))
        return 0;
    else
        return 1;
    }
    
}

