package nz.ac.auckland.se206;

import ai.djl.translate.TranslateException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.profile.User;

public class MenuController {

  /**
   * This method switches from menu to canvas
   *
   * @throws TranslateException
   */
  @FXML private Label infoLabel;

  @FXML private TextField usernameField;

  @FXML private TextField passwordField;

  @FXML private Button b1;

  @FXML private Button b2;

  @FXML private Button b3;

  @FXML private Button b4;

  @FXML private Button b5;

  @FXML private Button b6;

  @FXML private Button b7;

  @FXML private Button b8;

  // Create hashmap to store all of the users.
  // HashMap<String, User> usersHashMap;
  HashMap<String, User> usersHashMap = new HashMap<String, User>();

  // Current user logged in
  User currentUser = new User("None", "none");

  // Gets users data from file and populates users hash map
  private void loadUsers() throws IOException {
    String line;
    BufferedReader reader = new BufferedReader(new FileReader("users.txt"));
    // Create hashmap to store all of the users.
    // HashMap<String, User> usersHashMap = new HashMap<String, User>();
    while ((line = reader.readLine()) != null) {

      String[] parts = line.split(":"); // Should have 5 parts, or 6 if words

      // System.out.println("THIS PART SI WORKING : " + line);
      // System.out.println("printing " + parts[0]);
      // System.out.println("printing " + parts[1]);
      // System.out.println("printing " + parts[2]);
      // System.out.println("printing " + parts[3]);
      // System.out.println("printing " + parts[4]);

      // Create user based of information in file, and insert into hashmap
      User insertUser = new User(parts[0], parts[1]);
      insertUser.loadUser(
          parts[0],
          parts[1],
          Integer.valueOf(parts[2]),
          Integer.valueOf(parts[3]),
          Integer.valueOf(parts[4]));
      usersHashMap.put(parts[0], insertUser);

      // If the user has used words, add those too
      if (parts.length == 6) {
        System.out.println("GOT WORDS!");
        usersHashMap.get(parts[0]).getWordsToArray(parts[5]);
      }
    }

    // System.out.println("hashmap is: " + usersHashMap);
    reader.close();
  }

  // This handles registration of new users
  @FXML
  private void onRegister() throws IOException {

    loadUsers();

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

        // Add information regarding the new user to the save file
        bf.write(newUser.getSaveDetails());
        System.out.println("Should have just written......");

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
      System.out.println("This username is already taken. Please try again.");
    }
  }

  /**
   * For log in functionality - checks if a user exists.
   *
   * @throws IOException
   */
  @FXML
  private void onLogin() throws IOException {

    loadUsers();

    // Check if username does exist
    if (usersHashMap.containsKey(usernameField.getText())) {
      currentUser = usersHashMap.get(usernameField.getText());
      // System.out.println("username exists");
      // Update user details on UI
      infoLabel.setText(currentUser.formatUserDetails());
    }
  }

  /** Adds one to wins count */
  @FXML
  private void onb3Click() {
    currentUser.setStats(
        currentUser.getWins() + 1, currentUser.getLosses(), currentUser.getFastestWin());

    // Update users hash map
    usersHashMap.put(currentUser.getUsername(), currentUser);

    // Update user details on UI
    infoLabel.setText(currentUser.formatUserDetails());
  }

  /**
   * Adds one to loss count
   *
   * @throws Exception
   */
  @FXML
  private void onb4Click() throws Exception {
    currentUser.setStats(
        currentUser.getWins(), currentUser.getLosses() + 1, currentUser.getFastestWin());

    // Update users hash map
    usersHashMap.put(currentUser.getUsername(), currentUser);

    // Update user details on UI
    infoLabel.setText(currentUser.formatUserDetails());
  }

  // Add a word to current user word list
  @FXML
  private void onb5Click() throws Exception {
    infoLabel.setText("b4");

    usersHashMap.get(currentUser.getUsername()).addUsedWord("eggs");
    System.out.println(usersHashMap.get(currentUser.getUsername()).getUsedWords());
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

  // Save data
  @FXML
  private void saveData() throws Exception {
    infoLabel.setText("save data");

    // Save all the new data to the file
    BufferedWriter bf = null;

    // Overwrite existing file data
    try {
      // Create new BufferedWriter for the output file, append mode on
      bf = new BufferedWriter(new FileWriter("users.txt"));

      Boolean isFirst = true;
      for (String key : usersHashMap.keySet()) {
        if (isFirst == true) {
          // If user doesn't have any used words
          if (usersHashMap.get(key).getUsedWords().isEmpty()) {
            // Add information regarding first user to each first
            bf.write(usersHashMap.get(key).getSaveDetails());
          } else {
            bf.write(
                usersHashMap.get(key).getSaveDetails()
                    + ":"
                    + usersHashMap
                        .get(key)
                        .formatWordsForSave(usersHashMap.get(key).getUsedWords()));
          }
          isFirst = false;
        } else {
          break;
        }
      }
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

    try {
      // Create new BufferedWriter for the output file, append mode on
      bf = new BufferedWriter(new FileWriter("users.txt", true));

      Boolean isFirst = true;
      for (String key : usersHashMap.keySet()) {
        if (isFirst == true) {
          isFirst = false;
        } else {
          // Add information regarding each user (other than the first) to the save file
          // If user doesn't have any used words
          if (usersHashMap.get(key).getUsedWords().isEmpty()) {
            // Add information regarding first user to each first
            bf.write(usersHashMap.get(key).getSaveDetails());
          } else {
            bf.write(
                usersHashMap.get(key).getSaveDetails()
                    + ":"
                    + usersHashMap
                        .get(key)
                        .formatWordsForSave(usersHashMap.get(key).getUsedWords()));
          }
          bf.newLine();
        }
      }
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
