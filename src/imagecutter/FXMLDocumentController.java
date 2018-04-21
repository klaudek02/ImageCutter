/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagecutter;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;

/**
 *
 * @author Ada
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private Button directoryButton;

    @FXML
    private Button fileButton;

    @FXML
    private Label directoryLabel;

    @FXML
    private Label fileLabel;

    @FXML
    private RadioButton vertical;

    @FXML
    private ToggleGroup RadioGroup;

    @FXML
    private RadioButton horizontal;

    @FXML
    private RadioButton grid;

    @FXML
    private Spinner<Integer> columnsSpinner;

    @FXML
    private Spinner<Integer> rowsSpinner;

    @FXML
    private void verticalRadio() {
        rowsSpinner.setDisable(true);
        columnsSpinner.setDisable(false);

    }

    @FXML
    private void horizontalRadio() {
        rowsSpinner.setDisable(false);
        columnsSpinner.setDisable(true);
    }

    @FXML
    private void gridRadio() {
        rowsSpinner.setDisable(false);
        columnsSpinner.setDisable(false);
    }
    File file;
    File directory;

    @FXML
    private void selectDirectory() {

        DirectoryChooser directoryChooser = new DirectoryChooser();

        directory = directoryChooser.showDialog(fileButton.getScene().getWindow());
        if (directory != null) {
            directoryLabel.setText(directory.getPath());
        }

    }

    @FXML
    private void selectFile() {
        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser);

        file = fileChooser.showOpenDialog(fileButton.getScene().getWindow());
        if (file != null) {
            fileLabel.setText(file.getName());
        }
    }

    @FXML
    private void generateImages() {
        Node selectedRadio = (Node) RadioGroup.getSelectedToggle();

        if (file != null && directory != null && selectedRadio != null) {
            String radioId = selectedRadio.getId();
            Image image = new Image(file.toURI().toString());
            int height = (int) image.getHeight();
            int width = (int) image.getWidth();
            int squareHeight;
            int squareWidth;
            switch (radioId) {
                case "vertical": {
                    int x = 0;
                    int y = 0;
                    squareHeight = height;
                    squareWidth = width / columnsSpinner.getValue();
                    for (int i = 1; i <= columnsSpinner.getValue(); i++) {
                        PixelReader reader = image.getPixelReader();
                        WritableImage imageToSave = new WritableImage(reader, x, y, squareWidth, squareHeight);
                        x += squareWidth;
                        try {
                            saveFile(imageToSave, i++);
                        } catch (IOException e) {
                        };
                    }
                    break;
                }
                case "horizontal": {
                    int x = 0;
                    int y = 0;
                    squareHeight = height / rowsSpinner.getValue();
                    squareWidth = width;
                    for (int i = 1; i <= columnsSpinner.getValue(); i++) {
                        PixelReader reader = image.getPixelReader();
                        WritableImage imageToSave = new WritableImage(reader, x, y, squareWidth, squareHeight);
                        y += squareHeight;
                        try {
                            saveFile(imageToSave, i++);
                        } catch (IOException e) {
                        };
                    }
                    break;
                }
                case "grid": {
                    int x = 0;
                    int y = 0;
                    int k = 1;
                    squareHeight = height / rowsSpinner.getValue();
                    squareWidth = width / columnsSpinner.getValue();
                    for (int i = 1; i <= rowsSpinner.getValue(); i++) {
                        for (int j = 1; j <= columnsSpinner.getValue(); j++) {
                            PixelReader reader = image.getPixelReader();
                            WritableImage imageToSave = new WritableImage(reader, x, y, squareWidth, squareHeight);
                            x += squareWidth;
                            try {
                                saveFile(imageToSave, k++);
                            } catch (IOException e) {
                            };
                        }
                        y += squareHeight;
                        x = 0;
                    }
                    break;
                }
                default:
                    break;
            }
        }
    }

    private void saveFile(Image image, int nazwa) throws IOException {
        //complicated due to some bugs with colours
        File saveFile = new File(directory.getAbsolutePath() + "/imageCutter" + file.getName() + "_KD" + nazwa + ".jpg");
        BufferedImage img = SwingFXUtils.fromFXImage(image, null);
        BufferedImage imgRGB = new BufferedImage(img.getWidth(), img.getHeight(),
                BufferedImage.OPAQUE);
        Graphics2D graphics = imgRGB.createGraphics();
        graphics.drawImage(img, 0, 0, null);
        ImageIO.write(imgRGB, "jpeg", saveFile);
        graphics.dispose();

    }

    private static void configureFileChooser(
            FileChooser fileChooser) {
        fileChooser.setTitle("View Pictures");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image", "*.jpg", "*.png")
        );
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        SpinnerValueFactory<Integer> v
                = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
        SpinnerValueFactory<Integer> v2
                = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
        columnsSpinner.setValueFactory(v);
        rowsSpinner.setValueFactory(v2);

    }

}
