/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Form;

import java.awt.Graphics2D;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
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
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

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
    private ComboBox cbLanguage;

    RubberBandSelection rubberBandSelection;
    ImageView imageView;

    Stage primaryStage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cbLanguage.getItems().addAll(
                "EV",
                "VE"
                
        );
        cbLanguage.setValue("EV");
    }

    @FXML
    public void btnTranslateClicked() throws IOException {

        TranslateGoogle(tfSource.getText());
    }

    private void TranslateGoogle(String sentence) throws IOException {
        sentence = URLEncoder.encode(sentence, "UTF-8");
        String language = null;
        if(cbLanguage.getValue() == "EV")
            language = "en-vi";
        else
            language = "vi-en";
            
        String url = "https://translate.yandex.net/api/v1.5/tr/translate?key=trnsl.1.1.20170322T121646Z.547f9b757cf1e75e.1bc910cdd2391946c3c417486c51071c70ec0b08&text=" + sentence + "&lang=" + language + "&[format=plain]&[options=lang]";
        
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

    public void BtnSnapShotClicked() throws IOException {

        this.primaryStage = new Stage();
        BorderPane root = new BorderPane();

        // container for image layers
        ScrollPane scrollPane = new ScrollPane();

        // image layer: a group of images
        Group imageLayer = new Group();

        try {
            Robot robot = new Robot();
            String format = "jpg";
            String fileName = "FullScreenshot." + format;
            java.awt.Rectangle screenRect = new java.awt.Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
            ImageIO.write(screenFullImage, format, new File(fileName));

            //      Image image = new Image( getClass().getResource( "cat.jpg").toExternalForm());
            Image image = SwingFXUtils.toFXImage(screenFullImage, null);

            // the container for the image as a javafx node
            imageView = new ImageView(image);

            System.out.println("A full screenshot saved!");
        } catch (java.awt.AWTException | IOException ex) {
            System.err.println(ex);
        }
        imageLayer.getChildren().add(imageView);

        // use scrollpane for image view in case the image is large
        scrollPane.setContent(imageLayer);

        // put scrollpane in scene
        root.setCenter(scrollPane);

        // rubberband selection
        rubberBandSelection = new RubberBandSelection(imageLayer);

        // create context menu and menu items
        ContextMenu contextMenu = new ContextMenu();

        // set context menu on image layer
        imageLayer.setOnMousePressed(new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                if (event.isSecondaryButtonDown()) {
                    contextMenu.show(imageLayer, event.getScreenX(), event.getScreenY());
                }
            }
        });
        this.primaryStage.setFullScreen(true);
        primaryStage.setScene(new Scene(root, 1024, 768));
        primaryStage.show();
    }

    public static void saveToFile(Image image) {
        File outputFile = new File("E:/kienImg");
        BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
        try {
            ImageIO.write(bImage, "png", outputFile);
            System.out.println("Saved file");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void crop(Bounds bounds) {

//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Save Image");
//
//        File file = fileChooser.showSaveDialog( primaryStage);
//        if (file == null)
//            return;
        int width = (int) bounds.getWidth();
        int height = (int) bounds.getHeight();

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        parameters.setViewport(new Rectangle2D(bounds.getMinX(), bounds.getMinY(), width, height));

        WritableImage wi = new WritableImage(width, height);
        imageView.snapshot(parameters, wi);

        // save image 
        // !!! has bug because of transparency (use approach below) !!!
        // --------------------------------
//        try {
//          ImageIO.write(SwingFXUtils.fromFXImage( wi, null), "jpg", file);
//      } catch (IOException e) {
//          e.printStackTrace();
//      }
        // save image (without alpha)
        // --------------------------------
        BufferedImage bufImageARGB = SwingFXUtils.fromFXImage(wi, null);
        BufferedImage bufImageRGB = new BufferedImage(bufImageARGB.getWidth(), bufImageARGB.getHeight(), BufferedImage.OPAQUE);

        Graphics2D graphics = bufImageRGB.createGraphics();
        graphics.drawImage(bufImageARGB, 0, 0, null);

        graphics.dispose();

        primaryStage.close();
        try {

            Image image = SwingFXUtils.toFXImage(bufImageRGB, null);
            ITesseract instance = new Tesseract(); //
            File directory = new File("");
            // tfSource.setText(directory.getAbsolutePath());
            instance.setDatapath(directory.getAbsolutePath() + "\\src\\Resource\\tessdata");
            try {
                String result = instance.doOCR(bufImageRGB);

                tfSource.setText(result);
            } catch (TesseractException e) {
                System.err.println(e.getMessage());
            }
            // the container for the image as a javafx node
            saveToFile(image);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public class RubberBandSelection {

        final DragContext dragContext = new DragContext();
        Rectangle rect = new Rectangle();

        Group group;

        public Bounds getBounds() {
            return rect.getBoundsInParent();
        }

        public RubberBandSelection(Group group) {

            this.group = group;

            rect = new Rectangle(0, 0, 0, 0);
            rect.setStroke(Color.BLUE);
            rect.setStrokeWidth(1);
            rect.setStrokeLineCap(StrokeLineCap.ROUND);
            rect.setFill(Color.LIGHTBLUE.deriveColor(0, 1.2, 1, 0.6));

            group.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
            group.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
            group.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);

        }

        EventHandler<javafx.scene.input.MouseEvent> onMousePressedEventHandler = new EventHandler<javafx.scene.input.MouseEvent>() {

            @Override
            public void handle(javafx.scene.input.MouseEvent event) {

                if (event.isSecondaryButtonDown()) {
                    return;
                }

                // remove old rect
                rect.setX(0);
                rect.setY(0);
                rect.setWidth(0);
                rect.setHeight(0);

                group.getChildren().remove(rect);

                // prepare new drag operation
                dragContext.mouseAnchorX = event.getX();
                dragContext.mouseAnchorY = event.getY();

                rect.setX(dragContext.mouseAnchorX);
                rect.setY(dragContext.mouseAnchorY);
                rect.setWidth(0);
                rect.setHeight(0);

                group.getChildren().add(rect);

            }
        };

        EventHandler<javafx.scene.input.MouseEvent> onMouseDraggedEventHandler = new EventHandler<javafx.scene.input.MouseEvent>() {

            @Override
            public void handle(javafx.scene.input.MouseEvent event) {

                if (event.isSecondaryButtonDown()) {
                    return;
                }

                double offsetX = event.getX() - dragContext.mouseAnchorX;
                double offsetY = event.getY() - dragContext.mouseAnchorY;

                if (offsetX > 0) {
                    rect.setWidth(offsetX);
                } else {
                    rect.setX(event.getX());
                    rect.setWidth(dragContext.mouseAnchorX - rect.getX());
                }

                if (offsetY > 0) {
                    rect.setHeight(offsetY);
                } else {
                    rect.setY(event.getY());
                    rect.setHeight(dragContext.mouseAnchorY - rect.getY());
                }
            }
        };

        EventHandler<javafx.scene.input.MouseEvent> onMouseReleasedEventHandler = new EventHandler<javafx.scene.input.MouseEvent>() {

            @Override
            public void handle(javafx.scene.input.MouseEvent event) {

                if (event.isSecondaryButtonDown()) {
                    return;
                }

                // remove rectangle
                // note: we want to keep the ruuberband selection for the cropping => code is just commented out
                Bounds selectionBounds = rubberBandSelection.getBounds();

                // show bounds info
                System.out.println("Selected area: " + selectionBounds);

                // crop the image
                crop(selectionBounds);
                primaryStage.close();

                rect.setX(0);
                rect.setY(0);
                rect.setWidth(0);
                rect.setHeight(0);

                group.getChildren().remove(rect);

            }
        };

        private final class DragContext {

            public double mouseAnchorX;
            public double mouseAnchorY;

        }
    }
}
