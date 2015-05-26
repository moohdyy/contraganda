import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;

public class Main {


    final static JProgressBar pb = new JProgressBar();


    public static void main(String[] args) {
        JSONArray outputJSON = new JSONArray();
        JFrame frame = new JFrame("Launch Contraganda Analysis");
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON format only", "json");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                // creates progress bar
                pb.setMinimum(0);
                pb.setMaximum(100);
                pb.setStringPainted(true);
                pb.setString("Reading File...");
                // add progress bar
                frame.setLayout(new FlowLayout());
                frame.getContentPane().add(pb);

                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(300, 200);
                frame.setVisible(true);


                File input = chooser.getSelectedFile();
                System.out.println("You chose to open this file: " + chooser.getSelectedFile().getCanonicalPath());
                FileReader fr = new FileReader(input);
                BufferedReader br = new BufferedReader(fr);
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();
                while (line != null) {
                    sb.append(line);
                    line = br.readLine();
                }
                String inputString = sb.toString();
                br.close();
                fr.close();
                if(inputString.startsWith("{")){  // if just one entry
                    inputString = "[" + inputString + "]";
                }
                JSONArray allTexts = (JSONArray) JSONValue.parse(inputString);
                pb.setValue(10);
                pb.setString("Runnning Analysis...");

                outputJSON = runAnalysis(allTexts);
                FileWriter fw = new FileWriter(input.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                pb.setValue(90);
                pb.setString("Writing to File...");
                JSONValue.writeJSONString(outputJSON,bw);
                bw.close();
                fw.close();

                pb.setValue(100);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        frame.setVisible(false);
        System.exit(0);
    }

    private static JSONArray runAnalysis(JSONArray in){
        JSONArray out = new JSONArray();
        for (int i = 0; i < in.size(); i++) {
            JSONObject entry = (JSONObject) in.get(i);
            pb.setValue(10+(i/in.size())*80);
            pb.setString("Processing: " +(i+1)+"/"+in.size() +"("+entry.get("title")+")");
            String text = StringEscapeUtils.unescapeJava((String) entry.get("text"));
            MainEmotionRecognizer eR = new MainEmotionRecognizer(text);
            JSONObject results = eR.output();
            entry.put("RESULTS",results);
            out.add(entry);
        }
        return out;
    }
}
