package nz.ac.auckland.se206;

import ai.djl.translate.TranslateException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

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
import nz.ac.auckland.se206.profile.User;

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

  HashMap<String, User> usersHashMap = new HashMap<String, User>();

  /** Updates the info text */
  private void updateLabelInfo() {
    // Updates the GUI to display the predictions
    infoLabel.setText("set");
  }

  /** This method is called when the "Clear" button is pressed. */
  @FXML
  private void onb1Click() {

    infoLabel.setText(usernameField.getText());
    User newUser = new User(usernameField.getText(), passwordField.getText());
    System.out.println(newUser.getDetails());
    usersHashMap.put(usernameField.getText(), newUser);
    System.out.println("hashmap is " + usersHashMap);

    // new file object
    File file = new File("users.txt");

    BufferedWriter bf = null;

    try {

      // create new BufferedWriter for the output file
      bf = new BufferedWriter(new FileWriter(file));

      // iterate map entries
      for (Entry<String, User> entry : usersHashMap.entrySet()) {

        // put key and value separated by a colon
        bf.write(entry.getKey() + ":"
            + entry.getValue());

        // new line
        bf.newLine();
      }

      bf.flush();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {

      try {

        // always close the writer
        bf.close();
      } catch (Exception e) {
      }
    }
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
