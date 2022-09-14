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

public class DashboardController {

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
  HashMap<String, User> usersHashMap = new HashMap<String, User>();

  // Current user logged in
  User currentUser = new User("None", "none");

  /**
   * Gets the saved users data from file and loads this data onto the users hash map. This is done
   * before every register/login operation
   *
   * @throws IOException
   */
  private void loadUsers() throws IOException {
    String line;
    BufferedReader reader = new BufferedReader(new FileReader("users.txt"));

    // Read every line of the file, and insert into users hash map
    while ((line = reader.readLine()) != null) {

      String[] parts = line.split(":"); // Should have 5 parts, or 6 if words

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
        usersHashMap.get(parts[0]).getWordsToArray(parts[5]);
      }
    }

    reader.close();
  }

  /** Adds 1 to the user's current wins count */
  @FXML
  private void addWin() {
    currentUser.setStats(
        currentUser.getWins() + 1, currentUser.getLosses(), currentUser.getFastestWin());

    // Update users hash map
    usersHashMap.put(currentUser.getUsername(), currentUser);

    // Update user details on UI
    infoLabel.setText(currentUser.formatUserDetails());
  }

  /**
   * Adds one to the current user's loss count
   *
   * @throws Exception
   */
  @FXML
  private void addLoss() throws Exception {
    currentUser.setStats(
        currentUser.getWins(), currentUser.getLosses() + 1, currentUser.getFastestWin());

    // Update users hash map
    usersHashMap.put(currentUser.getUsername(), currentUser);

    // Update user details on UI
    infoLabel.setText(currentUser.formatUserDetails());
  }

  /**
   * Add a word (the word is just egg) to current user word list
   *
   * @throws Exception
   */
  @FXML
  private void addWord() throws Exception {
    // Adds egg to the current user's used words list
    usersHashMap.get(currentUser.getUsername()).addUsedWord("eggs");
  }

  /**
   * Saves any stats data. This is a manual save that is performed via a button (as it's going to be
   * very time consuming to write the save contents all the time)
   *
   * @throws Exception
   */
  @FXML
  private void saveData() throws Exception {

    // Save all the new data to the file
    BufferedWriter bf = null;

    // Overwrite existing file data for the first line we save
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

    // Every other line we save after the first one is on append mode for the file
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

  /**
   * Button to switch to the profile page
   *
   * @param event
   * @throws IOException
   * @throws TranslateException
   */
  @FXML
  private void onSwitchToProfile(ActionEvent event) throws IOException, TranslateException {
    // Changes the scene to canvas
    Button btnThatWasClicked = (Button) event.getSource();
    Scene sceneThatThisButtonIsIn = btnThatWasClicked.getScene();
    sceneThatThisButtonIsIn.setRoot(SceneManager.getUi(AppUi.PROFILE));
  }

  /**
   * Button to switch to the canvas page
   *
   * @param event
   * @throws IOException
   * @throws TranslateException
   */
  @FXML
  private void onSwitchToCanvas(ActionEvent event) throws IOException, TranslateException {
    // Changes the scene to canvas
    Button btnThatWasClicked = (Button) event.getSource();
    Scene sceneThatThisButtonIsIn = btnThatWasClicked.getScene();
    sceneThatThisButtonIsIn.setRoot(SceneManager.getUi(AppUi.CANVAS));

    // Runs a prediction to reduce lag
    FXMLLoader loader = SceneManager.getCanvasLoader();
    CanvasController controller = loader.getController();
    controller.updatePrediction();
  }
}
