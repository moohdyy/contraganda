package emotionRecognizer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class FileFunctions {

	
	/**
	 * read and store text from file
	 */
	public static String extractTextFromFile(File f)
	{
		String text = "";
		try {
		    BufferedReader in = new BufferedReader(new FileReader(f));
			String str;
		    while ((str = in.readLine()) != null)
		    {
		    	text += str+" ";
		    }
		    in.close();
		} catch (IOException e) {e.printStackTrace();}
		
		return text;
	}
	
	
	
	public static void saveTextToFile(File f, String t)
	{
    	try
    	{
    	    BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			bw.write(t);
    	    bw.close();
    	}
    	catch(IOException e)
    	{
    	    e.printStackTrace();
    	}
	}
	
	
	
	public static void saveSentencesToFile(File f,  List<String> sen)
	{
    	try
    	{
			BufferedWriter bw = new BufferedWriter(new FileWriter(f, false));
    		for (String s : sen)
    		{
    			bw.write(s+"\n");
    		}
			bw.close();
    	}
    	catch(IOException e)
    	{
    	    e.printStackTrace();
    	}
	}
	
}
