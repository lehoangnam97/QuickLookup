/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Form;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.StackPane;

/**
 *
 * @author Phong
 */
public class MainFormController implements Initializable {

    @FXML private TabPane tabPanel;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        Tab WordTranslate = new Tab();
        try {
            WordTranslate.setContent((Parent) new FXMLLoader(getClass().getResource("WordTranslate.fxml")).load());
        } catch (IOException ex) {
            Logger.getLogger(MainFormController.class.getName()).log(Level.SEVERE, null, ex);
        }

        Label l2 = new Label("Tra từ");
        l2.setRotate(90);
        StackPane stp2 = new StackPane(new Group(l2));
        stp2.setMinSize(50, 100);
        WordTranslate.setGraphic(stp2);
        tabPanel.getTabs().add(WordTranslate);
        
        
        Tab sentenceTranslate = new Tab();
        try {
            sentenceTranslate.setContent((Parent) new FXMLLoader(getClass().getResource("SentenceTranslate.fxml")).load());
        } catch (IOException ex) {
            Logger.getLogger(MainFormController.class.getName()).log(Level.SEVERE, null, ex);
        }

        Label l = new Label("Dịch Câu");
        l.setRotate(90);
        StackPane stp = new StackPane(new Group(l));
        stp.setMinSize(50, 100);
        sentenceTranslate.setGraphic(stp);
        tabPanel.getTabs().add(sentenceTranslate);
        
             
        tabPanel.setTabMinHeight(100);
        tabPanel.setTabMaxHeight(100);
    }

}
