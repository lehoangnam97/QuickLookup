/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Form;

import ClassModel.WordList;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
/**
 * FXML Controller class
 *
 * @author Phong
 */
public class WordTranslateController implements Initializable {

    @FXML
    private ComboBox cbLanguage;
    @FXML
    private TextFlow tofResult;
    @FXML
    private TextField tfSource;
    @FXML
    private ListView lvHint;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Load();
        cbLanguage.getItems().addAll(
                "EV",
                "VE"
        );
        cbLanguage.setValue("EV");

        tfSource.textProperty().addListener((obs, oldText, newText) -> {
            UpdateHint();

            // ...
        });
    }

    public void SpeechText() {
        System.setProperty("http.agent", "Chrome");

         VoiceManager vm = VoiceManager.getInstance();
         Voice voice = vm.getVoice("kevin16");
 
        voice.allocate();
 
        voice.speak(tfSource.getText());
    }
    
    public void coloredResult(String result)
    {
     tofResult.getChildren().clear();
     
     String[] paragraph = result.split("\n");
     
     for(String row : paragraph)
     {
         if(row.length() > 2)
         {
            String type = row.substring(0,2);
            Text rs ;
            
            rs = new Text(row.replace("@","").replace("$","") + "\n");
            rs.setFont(new Font(20));
            if(type.equalsIgnoreCase("* "))
            {
                rs = new Text(row.replace(type,"").replace("$","") + "\n");
                rs.setFont(new Font(15));
                rs.setFill(Color.AQUA);
            }
            if(type.equalsIgnoreCase("- "))
            {
                rs = new Text(row.replace(type," ").replace("$","") + "\n");
                rs.setFont(new Font(15));
                rs.setFill(Color.DARKKHAKI);
            }
            
            if(type.equalsIgnoreCase("!$"))
            {
               rs = new Text(row.replace(type,"").replace("$","") + "\n");
               rs.setFont(new Font(15));
            }
            
            if(type.equalsIgnoreCase("=$"))
            {
               rs = new Text(row.replace(type,"").replace("$","") + "\n");
               rs.setFont(new Font(15));
            }
            tofResult.getChildren().add(rs);   
         }
     }
     
    }

    public void btnSearchMouseClicked() {
        // TODO add your handling code here:
        String itemText = (String) cbLanguage.getValue().toString();

        if (itemText == "EV") {
           coloredResult(WordList.getInstance().getEV().Find(tfSource.getText().toLowerCase()));
                 
        }
        if (itemText == "VE") {
            //taResult.setText(WordList.getInstance().getVE().Find(tfSource.getText().toLowerCase()));
        }

//        if (itemText == "EE") {
//            toResult.setText(WordList.getInstance().getEE().Find(tfSource.getText().toLowerCase()));
//        }
    }

    public void lvHintMouseClicked() {
        // TODO add your handling code here:
        tfSource.setText(lvHint.getSelectionModel().getSelectedItem().toString());

    }

    private void Load() {

        try {
            // TODO add your handling code here:
            LoadData(1);
            LoadData(2);
            LoadData(3);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Dictionary.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void UpdateHint() {
        //update listview
        lvHint.setItems(getHint(tfSource.getText()));
    }

    public ObservableList<String> getHint(String text) {
        ObservableList<String> listModel = FXCollections.observableArrayList();
        if (cbLanguage.getValue().toString() == "EV") {
            for (String word : WordList.getInstance().getWordEV()) {
                if (word.startsWith(text)) {
                    listModel.add(word);
                }
            }
        }
        if (cbLanguage.getValue().toString() == "VE") {
            for (String word : WordList.getInstance().getWordVE()) {
                if (word.startsWith(text)) {
                    listModel.add(word);
                }
            }
        }
        if (listModel.size() == 1) {
            return FXCollections.observableArrayList();
        }
        return listModel;
    }

    void LoadData(int type) throws FileNotFoundException {
        String dictdata = new String();
        try {

            File directory = new File("");
            String fileName = directory.getAbsolutePath();

            if (type == 1) {
                fileName += "\\src\\Resource\\dictev.txt";
            }
            if (type == 2) {
                fileName += "\\src\\Resource\\dictve.txt";
            }
            if (type == 3) {
                fileName += "\\src\\Resource\\dictee.txt";
            }

            File f = new File(fileName);
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                builder.append(line);
                builder.append("\n");

            }
            fr.close();
            br.close();
            dictdata = builder.toString();
        } catch (Exception ex) {
            System.out.println("Loi doc file: " + ex);
        }

        ArrayList<String> word = new ArrayList<>();
        String[] fullWords = dictdata.split("\n\n");
        String name = new String();
        String regex = "(?<=@)[\\S\\D ]+?(?= /)";
        Pattern pattern = Pattern.compile(regex);
        for (String oneWord : fullWords) {
            try {
                Matcher m = pattern.matcher(oneWord);
                if (m.find()) {
                    name = m.group();
                }
                word.add(name);
                if (type == 1) {
                    if (WordList.getInstance().getEV().IsContain(name) == false) {
                        WordList.getInstance().getEV().Add(name, oneWord + "\n---------------");
                    }
                }
                if (type == 2) {
                    if (WordList.getInstance().getVE().IsContain(name) == false) {
                        WordList.getInstance().getVE().Add(name, oneWord + "\n---------------");
                    }
                }
                if (type == 3) {
                    if (WordList.getInstance().getEE().IsContain(name) == false) {
                        WordList.getInstance().getEE().Add(name, oneWord + "\n---------------");
                    }
                }
            } catch (Exception ex) {
                System.out.println("Loi doc file: " + ex);
            }
        }

        if (type == 1) {
            WordList.getInstance().setWordEV(word);
        }
        if (type == 2) {
            WordList.getInstance().setWordVE(word);
        }

    }

}
