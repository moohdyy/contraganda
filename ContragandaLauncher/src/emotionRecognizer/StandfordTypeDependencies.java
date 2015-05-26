package emotionRecognizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;

public class StandfordTypeDependencies {
	
	LexicalizedParser lp;
	TreebankLanguagePack tlp;
	GrammaticalStructureFactory gsf;
	
	public StandfordTypeDependencies()
	{
		lp = LexicalizedParser.loadModel(
				"edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz",
				"-maxLength", "80", "-retainTmpSubcategories");
		
		tlp = new PennTreebankLanguagePack();
		
		// Uncomment the following line to obtain original Stanford Dependencies
		//tlp.setGenerateOriginalDependencies(true);
		
		gsf = tlp.grammaticalStructureFactory();
	}
	
	
	public HashMap<Integer, Double> getModiefiers(List<String> inList, HashMap<String, Double> modHM)
	{
		HashMap<String, Double> ret = null;
		String[] sent = list2Array(inList);
		Tree parse = lp.apply(Sentence.toWordList(sent));
		GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
		Collection<TypedDependency> tdl = gs.typedDependenciesCCprocessed();
		
		HashMap<Integer, Double> modifiers = parseTypeDepTree(tdl, modHM);		
		return modifiers;
	}
	
	
	private HashMap<Integer, Double> parseTypeDepTree(Collection<TypedDependency> tdl, HashMap<String, Double> modHM)
	{
		HashMap<Integer, Double> modifiers = new HashMap<Integer, Double>();
		String tmp = tdl.toString().substring(1, tdl.toString().length()-1);
		
		//String neg[] = tmp.split("),");
		List <String> negList = new ArrayList<String>();
		
		int i1 = 0;
		int i2=0;
		int escape = 0;
		// find negotiators
		while (escape<100)
		{
			escape ++;
			try
			{
				i1 = tmp.indexOf("neg", i2);
				if (i1 == -1) break;
				i2 = tmp.indexOf(",", i1);
				//System.out.println("neg:"+tmp.substring(i1, i2).split("-")[1]);
				modifiers.put(Integer.parseInt(tmp.substring(i1, i2).split("-")[1]), 0.0);
			}
			catch(Exception e)
			{
				System.out.println("exception while finding modifiers");
			}			
		}
		
		// find modifiers
		i1 = 0;
		i2 = 0;
		escape = 0;
		while (escape<100)
		{
			escape ++;
			try
			{
				i1 = tmp.indexOf("advmod", i2);
				if (i1 == -1) break;
				i2 = tmp.indexOf(",", i1);
				//System.out.println("word:"+tmp.substring(i1, i2).split("-")[1]);
				int i3 = tmp.indexOf(")", i1);
				String tmpStr = tmp.substring(i2, i3);
				String tmpStr2 = tmpStr.substring(tmpStr.indexOf(" ")+1, tmpStr.indexOf("-"));
				//System.out.println(tmpStr2);
				
				// try to find the word in our dictionary
				if (modHM.containsKey(tmpStr2))
				{
					// look if we already have a negotiator for that word
					if (modifiers.containsKey(Integer.parseInt(tmp.substring(i1, i2).split("-")[1])))
					{
						modifiers.put(Integer.parseInt(tmp.substring(i1, i2).split("-")[1]), modHM.get(tmpStr2)*-1);
					}
					else
					{
						modifiers.put(Integer.parseInt(tmp.substring(i1, i2).split("-")[1]), modHM.get(tmpStr2));
					}
				}
			}
			catch(Exception e)
			{
				System.out.println("exception while finding modifiers");
			}			
		}
		
		return modifiers;
	}
	
	
	private String[] list2Array(List<String> inList)
	{
		String[] ret = new String[inList.size()];
		for (int i=0; i<inList.size(); i++)
		{
			ret[i] = inList.get(i);
		}
		return ret;
	}
	
	
}
