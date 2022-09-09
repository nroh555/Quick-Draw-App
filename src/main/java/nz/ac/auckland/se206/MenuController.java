package nz.ac.auckland.se206;

import ai.djl.translate.TranslateException;

import java.io.BufferedReader;
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

  @FXML
  private Button b5;

  @FXML
  private Button b6;

  @FXML
  private Button b7;

  @FXML
  private Button b8;

  // Create hashmap to store all of the users.
  HashMap<String, User> usersHashMap = new HashMap<String, User>();

  // Gets all
  private void loadUsers() {

  }

  // This handles registration of new users
  @FXML
  private void onRegister() {

    // If the username does not already exist, register user
    if (!usersHashMap.containsKey(usernameField.getText())) {
      // Create a new user profile with the inputted username and password
      User newUser = new User(usernameField.getText(), passwordField.getText());
      // Add user to hashmap
      usersHashMap.put(usernameField.getText(), newUser);

      // Save the new user to the file
      BufferedWriter bf = null;
      try {
        // Create new BufferedWriter for the output file, append mode on
        bf = new BufferedWriter(new FileWriter("users.txt", true));

        // Add key and value corresponding to the new user to the file
        bf.write(usernameField.getText() + ":"
            + usersHashMap.get(usernameField.getText()));

        bf.newLine();
        bf.flush();
      } catch (IOException e) {
        // Print exceptions
        e.printStackTrace();
      } finally {
        try {
          // Close the writer
          bf.close();
        } catch (Exception e) {
          // Print exceptions
          e.printStackTrace();
        }
      }
    } else {
      System.out.println("Sorry, that username is already taken! Please try again.");
    }

  }

  /**
   * For log in functionality - checks if a user exists.
   * 
   * @throws IOException
   */
  @FXML
  private void onLogin() throws IOException {

    String filePath = "users.txt";
    HashMap<String, String> fetchHashMap = new HashMap<String, String>();

    String line;
    BufferedReader reader = new BufferedReader(new FileReader(filePath));
    while ((line = reader.readLine()) != null) {
      System.out.println(line);
      String[] parts = line.split(":", 2);
      if (parts.length >= 2) {
        String fetchKey = parts[0];
        String fetchValue = parts[1];
        fetchHashMap.put(fetchKey, fetchValue);
      } else {
        System.out.println("ignoring line: " + line);
      }
    }

    System.out.println("new hashmap is: " + fetchHashMap);

    // for (String key : map.keySet()) {
    // System.out.println(key + ":" + map.get(key));
    // }
    reader.close();
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
  private void onb5Click() throws Exception {
    infoLabel.setText("b4");
    // writeJsonSimpleDemo("users.json");
  }

  @FXML
  private void onb6Click() throws Exception {
    infoLabel.setText("b4");
    // writeJsonSimpleDemo("users.json");
  }

  @FXML
  private void onb7Click() throws Exception {
    infoLabel.setText("b4");
    // writeJsonSimpleDemo("users.json");
  }

  @FXML
  private void onb8Click() throws Exception {
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
