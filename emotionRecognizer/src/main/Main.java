package main;

import emotionRecognizer.MainEmotionRecognizer;

public class Main {

	public static void main(String[] args) {
		
		
		// TODO Auto-generated method stub
		MainEmotionRecognizer eR = new MainEmotionRecognizer("test3.txt", 
													"D:/eclipse_workspace/emotionRecognizer/out.txt", 
													"D:/eclipse_workspace/emotionRecognizer/lib/polarityDir",
													"D:/eclipse_workspace/emotionRecognizer/lib/Ratings_Warriner_et_al.csv");
		
		// output emotion analyze file
		eR.output();
		
	}
}
