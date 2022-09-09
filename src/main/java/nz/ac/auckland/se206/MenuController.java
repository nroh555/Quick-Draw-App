package nz.ac.auckland.se206;

import ai.djl.translate.TranslateException;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.profile.Person;

public class MenuController {

  /**
   * This method switches from menu to canvas
   *
   * @throws TranslateException
   */

  @FXML
  private Label infoLabel;

  @FXML
  private TextField usernameField;

  @FXML
  private TextField passwordField;

  @FXML
  private Button b1;

  @FXML
  private Button b2;

  @FXML
  private Button b3;

  @FXML
  private Button b4;

  /** Updates the info text */
  private void updateLabelInfo() {
    // Updates the GUI to display the predictions
    infoLabel.setText("set");
  }

  /** This method is called when the "Clear" button is pressed. */
  @FXML
  private void onb1Click() {
    infoLabel.setText(usernameField.getText());
  }

  /** This method is called when the "Clear" button is pressed. */
  @FXML
  private void onb2Click() {
    infoLabel.setText(passwordField.getText());
  }

  /** This method is called when the "Clear" button is pressed. */
  @FXML
  private void onb3Click() {
    infoLabel.setText("b3");
  }

  /**
   * This method is called when the "Clear" button is pressed.
   * 
   * @throws Exception
   */
  @FXML
  private void onb4Click() throws Exception {
    infoLabel.setText("b4");
    // writeJsonSimpleDemo("users.json");
  }

  @FXML
  private void onSwitchToCanvas(ActionEvent event) throws IOException, TranslateException {
    // Changes the scene to canvas
    Button btnThatWasClicked = (Button) event.getSource();
    Scene sceneThatThisButtonIsIn = btnThatWasClicked.getScene();
    sceneThatThisButtonIsIn.setRoot(SceneManager.getUi(AppUi.CANVAS));

    // Runs a prediction to reduce lag
    FXMLLoader loader = SceneManager.getLoader();
    CanvasController controller = loader.getController();
    controller.updatePrediction();
  }
}
