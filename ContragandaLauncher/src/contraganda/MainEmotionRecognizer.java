import org.json.simple.JSONObject;

import java.util.Random;

/**
 * Created by moohdyy on 5/26/15.
 */
public class MainEmotionRecognizer {

    //from original src
    final static int HAPPY 		= 0;
    final static int ANGRY 		= 1;
    final static int SAD 		= 2;
    final static int RELAXED 	= 3;
    int 			subjSentencesCounter;
    int 			objSentencesCounter;

    //DUMMY
    Random rndTMP;
    double op[];

    public MainEmotionRecognizer(String text){
        rndTMP = new Random();
        op = new double[] {rndTMP.nextInt(30)+5,rndTMP.nextInt(30)+5,rndTMP.nextInt(30)+5,rndTMP.nextInt(30)+5};
        subjSentencesCounter = rndTMP.nextInt(20);
        objSentencesCounter = rndTMP.nextInt(20);
    }
    public JSONObject output(){
        JSONObject ret = new JSONObject();
        ret.put("HAPPY",op[HAPPY]);
        ret.put("ANGRY",op[ANGRY]);
        ret.put("SAD",op[SAD]);
        ret.put("RELAXED",op[RELAXED]);
        ret.put("obj",subjSentencesCounter);
        ret.put("subj",objSentencesCounter);
        return ret;
    }
}
