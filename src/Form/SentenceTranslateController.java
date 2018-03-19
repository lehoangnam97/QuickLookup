/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Form;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 *
 * @author Phong
 */
public class SentenceTranslateController implements Initializable {

    @FXML
    private TextField tfSource;
    @FXML
    private TextField tfResult;
    @FXML
    private Button btnTranslate;
    @FXML
    private Label label;
    @FXML
    private ComboBox<?> cbLanguage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

    }

    @FXML
    public void btnTranslateClicked() throws IOException {

        TranslateGoogle(tfSource.getText());
    }

    private void TranslateGoogle(String sentence) throws IOException {
        sentence = URLEncoder.encode(sentence, "UTF-8");
        String url = "https://translate.yandex.net/api/v1.5/tr/translate?key=trnsl.1.1.20170322T121646Z.547f9b757cf1e75e.1bc910cdd2391946c3c417486c51071c70ec0b08&text=" + sentence + "&lang=vi-en&[format=plain]&[options=lang]";
        String result = null;
        URLConnection connection = null;
        try {
            connection = new URL(url).openConnection();
            Scanner scanner = new Scanner(connection.getInputStream());
            scanner.useDelimiter("\\Z");
            result = scanner.next();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        result = result.replace("/", "");
        result = result.replace("<text>", "~");
        result = result.split("~")[1];
        tfResult.setText(result);

    }

}
