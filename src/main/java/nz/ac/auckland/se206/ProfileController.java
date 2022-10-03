package nz.ac.auckland.se206;

import ai.djl.translate.TranslateException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextArea;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.models.Level;
import nz.ac.auckland.se206.profile.User;

public class ProfileController {

  @FXML private Label welcomeLabel;

  @FXML private Label infoLabel;

  @FXML private TextArea usedWordsBox;

  @FXML private MenuButton accuracySettingButton;

  @FXML private MenuButton wordsSettingButton;

  @FXML private MenuButton timeSettingButton;

  @FXML private MenuButton confidenceSettingButton;

  // Create hashmap to store all of the users.
  private HashMap<String, User> usersHashMap = new HashMap<String, User>();

  // Current user logged in
  private User currentUser = new User("None");

  // TODO these may be redundant
  public HashMap<String, User> getUsersHashMap() {
    return usersHashMap;
  }

  public void setUsersHashMap(HashMap<String, User> usersHashMap) {
    this.usersHashMap = usersHashMap;
  }

  public User getCurrentUser() {
    return currentUser;
  }

  public void setCurrentUser(User currentUser) {
    this.currentUser = currentUser;
  }

  protected void updateLabels() {
    // Get current user
    FXMLLoader menuLoader = SceneManager.getMenuLoader();
    MenuController menuController = menuLoader.getController();
    this.currentUser = menuController.getCurrentUser();
    this.usersHashMap = menuController.getUsersHashMap();

    // Update welcome label
    welcomeLabel.setText("Welcome, " + currentUser.getUsername() + "!");

    // Update info label
    infoLabel.setText(currentUser.formatUserDetails());

    // Update used words label
    usedWordsBox.setText(currentUser.getUsedWordsString());

    // Update the menu button with the current user difficulty settings
    accuracySettingButton.setText(
        "Accuracy: " + currentUser.getDifficultyString(currentUser.getAccuracySetting()));
    wordsSettingButton.setText(
        "Words: " + currentUser.getDifficultyString(currentUser.getWordsSetting()));
    timeSettingButton.setText(
        "Time: " + currentUser.getDifficultyString(currentUser.getTimeSetting()));
    confidenceSettingButton.setText(
        "Confidence: " + currentUser.getDifficultyString(currentUser.getConfidenceSetting()));
  }

  /**
   * Button to switch to the menu page
   *
   * @param event
   * @throws IOException
   * @throws TranslateException
   */
  @FXML
  private void onSwitchToMenu(ActionEvent event) throws IOException, TranslateException {
    // Changes the scene to canvas
    Button btnThatWasClicked = (Button) event.getSource();
    Scene sceneThatThisButtonIsIn = btnThatWasClicked.getScene();
    sceneThatThisButtonIsIn.setRoot(SceneManager.getUi(AppUi.MENU));
  }

  /**
   * Button to switch to the dashboard page
   *
   * @param event
   * @throws IOException
   * @throws TranslateException
   */
  @FXML
  private void onSwitchToDashboard(ActionEvent event) throws IOException, TranslateException {
    // Changes the scene to canvas
    Button btnThatWasClicked = (Button) event.getSource();
    Scene sceneThatThisButtonIsIn = btnThatWasClicked.getScene();
    sceneThatThisButtonIsIn.setRoot(SceneManager.getUi(AppUi.DASHBOARD));
  }

  @FXML
  private void onAccuracySetEasy() {
    accuracySettingButton.setText("Accuracy: Easy");
    updateCurrentUser("Accuracy", Level.EASY);
  }

  @FXML
  private void onAccuracySetMedium() {
    accuracySettingButton.setText("Accuracy: Medium");
    updateCurrentUser("Accuracy", Level.MEDIUM);
  }

  @FXML
  private void onAccuracySetHard() {
    accuracySettingButton.setText("Accuracy: Hard");
    updateCurrentUser("Accuracy", Level.HARD);
  }

  @FXML
  private void onWordsSetEasy() {
    wordsSettingButton.setText("Words: Easy");
    updateCurrentUser("Words", Level.EASY);
  }

  @FXML
  private void onWordsSetMedium() {
    wordsSettingButton.setText("Words: Medium");
    updateCurrentUser("Words", Level.MEDIUM);
  }

  @FXML
  private void onWordsSetHard() {
    wordsSettingButton.setText("Words: Hard");
    updateCurrentUser("Words", Level.HARD);
  }

  @FXML
  private void onWordsSetMaster() {
    wordsSettingButton.setText("Words: Master");
    updateCurrentUser("Words", Level.MASTER);
  }

  @FXML
  private void onTimeSetEasy() {
    timeSettingButton.setText("Time: Easy");
    updateCurrentUser("Time", Level.EASY);
  }

  @FXML
  private void onTimeSetMedium() {
    timeSettingButton.setText("Time: Medium");
    updateCurrentUser("Time", Level.MEDIUM);
  }

  @FXML
  private void onTimeSetHard() {
    timeSettingButton.setText("Time: Hard");
    updateCurrentUser("Time", Level.HARD);
  }

  @FXML
  private void onTimeSetMaster() {
    timeSettingButton.setText("Time: Master");
    updateCurrentUser("Time", Level.MASTER);
  }

  @FXML
  private void onConfidenceSetEasy() {
    confidenceSettingButton.setText("Confidence: Easy");
    updateCurrentUser("Confidence", Level.EASY);
  }

  @FXML
  private void onConfidenceSetMedium() {
    confidenceSettingButton.setText("Confidence: Medium");
    updateCurrentUser("Confidence", Level.MEDIUM);
  }

  @FXML
  private void onConfidenceSetHard() {
    confidenceSettingButton.setText("Confidence: Hard");
    updateCurrentUser("Confidence", Level.HARD);
  }

  @FXML
  private void onConfidenceSetMaster() {
    confidenceSettingButton.setText("Confidence: Master");
    updateCurrentUser("Confidence", Level.MASTER);
  }

  private void updateCurrentUser(String difficultyCategory, Level levelSetting) {
    // Get current user
    FXMLLoader menuLoader = SceneManager.getMenuLoader();
    MenuController menuController = menuLoader.getController();
    User currentUser = menuController.getCurrentUser();

    // Update appropriate difficulty category for the current user
    if (difficultyCategory == "Accuracy") {
      currentUser.setAccuracySetting(levelSetting);
    } else if (difficultyCategory == "Words") {
      currentUser.setWordsSetting(levelSetting);
    } else if (difficultyCategory == "Time") {
      currentUser.setTimeSetting(levelSetting);
    } else {
      currentUser.setConfidenceSetting(levelSetting);
    }

    try {
      saveData();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /** Updates the user hash map and current user in the menu controller */
  private void updateUserAndMap() {
    FXMLLoader menuLoader = SceneManager.getMenuLoader();
    MenuController menuController = menuLoader.getController();
    menuController.setUsersHashMap(usersHashMap);
    menuController.setCurrentUser(currentUser);
    System.out.println(usersHashMap);
    System.out.println(currentUser);
  }

  /**
   * Saves any stats data. This is a manual save that is performed via a button (as it's going to be
   * very time consuming to write the save contents all the time)
   *
   * @throws Exception
   */
  @FXML
  private void saveData() throws Exception {
    updateUserAndMap();

    // Save all the new data to the file
    BufferedWriter bf = null;

    // Overwrite existing file data for the first line we save
    try {
      System.out.println("In the try for bufferwriter");
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
  }
}
