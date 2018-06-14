/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Form;

import ClassModel.WordList;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * FXML Controller class
 *
 * @author Phong
 */
public class HistorySearchController implements Initializable {

    @FXML
    private TextFlow tofResult;
    @FXML
    public ListView lvHint;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
     
         lvHint.setItems(WordList.getInstance().getHistoryWord());
    }    
    
    public void lvHintMouseClicked() {
        // TODO add your handling code here:
        coloredResult(WordList.getInstance().getEV().Find(lvHint.getSelectionModel().getSelectedItem().toString().toLowerCase()));             
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

}
