package nz.ac.auckland.se206;

import ai.djl.ModelException;
import ai.djl.translate.TranslateException;
import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.models.Level;
import nz.ac.auckland.se206.profile.User;

public class DashboardController {
  @FXML private Label welcomeLabel;

  @FXML private MenuButton accuracySettingButton;

  @FXML private MenuButton wordsSettingButton;

  @FXML private MenuButton timeSettingButton;

  @FXML private MenuButton confidenceSettingButton;

  @FXML private Button startNormalGame;

  @FXML private Button startZenGame;

  @FXML private Button startHiddenGame;

  private String currentWord;

  // Create hashmap to store all of the users.
  private HashMap<String, User> usersHashMap = new HashMap<String, User>();

  // Current user logged in
  private User currentUser = new User("None");

  /**
   * Get the registered users hash map
   *
   * @return HashMap<String, User> of all the registered users
   */
  public HashMap<String, User> getUsersHashMap() {
    return usersHashMap;
  }

  /**
   * Set the registered users hash map
   *
   * @param usersHashMap of all the users hashmap
   */
  public void setUsersHashMap(HashMap<String, User> usersHashMap) {
    this.usersHashMap = usersHashMap;
  }

  /**
   * Get the current logged in user
   *
   * @return User that is logged in
   */
  public User getCurrentUser() {
    return currentUser;
  }

  /**
   * Set the current logged in user
   *
   * @param currentUser that is logged in
   */
  public void setCurrentUser(User currentUser) {
    this.currentUser = currentUser;
  }

  /**
   * Get the current word for the user
   *
   * @return Current word
   */
  public String getCurrentWord() {
    return currentWord;
  }

  /**
   * Set the current word for the user
   *
   * @param currentWord current word
   */
  public void setCurrentWord(String currentWord) {
    this.currentWord = currentWord;
  }

  /**
   * Button to switch to the profile page
   *
   * @param event click event
   * @throws IOException If the file is not found.
   * @throws TranslateException If error is raised during processing of input/output
   */
  @FXML
  private void onSwitchToProfile(ActionEvent event) throws IOException, TranslateException {
    // Update welcome label in profile
    FXMLLoader profileLoader = SceneManager.getProfileLoader();
    ProfileController profileController = profileLoader.getController();
    profileController.updateLabels();

    // Changes the scene to canvas
    Button btnThatWasClicked = (Button) event.getSource();
    Scene sceneThatThisButtonIsIn = btnThatWasClicked.getScene();
    sceneThatThisButtonIsIn.setRoot(SceneManager.getUi(AppUi.PROFILE));
  }

/**
 * Button to switch to canvas
 * @param event click event
 * @throws IOException If the file is not found.
 * @throws ModelException If the model cannot be found on the file system.
 * @throws CsvException Base class for all exceptions for opencsv
 * @throws URISyntaxException If string could not be parsed as a URI reference
 * @throws TranslateException If error is raised during processing of input/output
 */
  @FXML
  private void onSwitchToCanvas(ActionEvent event)
      throws IOException, ModelException, CsvException, URISyntaxException, TranslateException {
    // Runs a prediction to reduce lag
    FXMLLoader canvasLoader = SceneManager.getCanvasLoader();
    CanvasController canvasController = canvasLoader.getController();
    canvasController.updatePrediction();
    canvasController.initialize();

    // Changes the scene to canvas
    Button btnThatWasClicked = (Button) event.getSource();
    Scene sceneThatThisButtonIsIn = btnThatWasClicked.getScene();
    sceneThatThisButtonIsIn.setRoot(SceneManager.getUi(AppUi.CANVAS));
  }

/**
 * Button to switch to the zen canvas page
 * @param event click event
 * @throws IOException If the file is not found.
 * @throws ModelException If the model cannot be found on the file system.
 * @throws CsvException Base class for all exceptions for opencsv
 * @throws URISyntaxException If string could not be parsed as a URI reference
 * @throws TranslateException If error is raised during processing of input/output
 */
  @FXML
  private void onSwitchToCanvasZen(ActionEvent event)
      throws IOException, ModelException, CsvException, URISyntaxException, TranslateException {
    // Runs a prediction to reduce lag
    FXMLLoader canvasZenLoader = SceneManager.getCanvasZenLoader();
    CanvasZenController canvasZenController = canvasZenLoader.getController();
    canvasZenController.updatePrediction();
    canvasZenController.initialize();

    // Changes the scene to canvas
    Button btnThatWasClicked = (Button) event.getSource();
    Scene sceneThatThisButtonIsIn = btnThatWasClicked.getScene();
    sceneThatThisButtonIsIn.setRoot(SceneManager.getUi(AppUi.CANVAS_ZEN));
  }

/**
 * Button to switch to the hidden mode canvas page
 * @param event click event
 * @throws IOException If the file is not found.
 * @throws ModelException If there is an error in reading the input/output of the DL model.
 * @throws CsvException Base class for all exceptions for opencsv
 * @throws URISyntaxException If string could not be parsed as a URI reference
 * @throws TranslateException If error is raised during processing of input/output
 */
  @FXML
  private void onSwitchToCanvasHidden(ActionEvent event)
      throws IOException, ModelException, CsvException, URISyntaxException, TranslateException {
    // Runs a prediction to reduce lag
    FXMLLoader canvasHiddenLoader = SceneManager.getCanvasHiddenLoader();
    CanvasHiddenController canvasHiddenController = canvasHiddenLoader.getController();
    canvasHiddenController.updatePrediction();
    canvasHiddenController.initialize();

    // Changes the scene to canvas
    Button btnThatWasClicked = (Button) event.getSource();
    Scene sceneThatThisButtonIsIn = btnThatWasClicked.getScene();
    sceneThatThisButtonIsIn.setRoot(SceneManager.getUi(AppUi.CANVAS_HIDDEN));
  }

  /** Update the welcome label for the user */
  protected void updateWelcomeLabel() {
    // Get current user
    FXMLLoader menuLoader = SceneManager.getMenuLoader();
    MenuController menuController = menuLoader.getController();
    this.currentUser = menuController.getCurrentUser();
    this.usersHashMap = menuController.getUsersHashMap();

    // Update the menu button with the current user difficulty settings
    accuracySettingButton.setText(
        "Accuracy: " + currentUser.getDifficultyString(currentUser.getAccuracySetting()));
    wordsSettingButton.setText(
        "Words: " + currentUser.getDifficultyString(currentUser.getWordsSetting()));
    timeSettingButton.setText(
        "Time: " + currentUser.getDifficultyString(currentUser.getTimeSetting()));
    confidenceSettingButton.setText(
        "Confidence: " + currentUser.getDifficultyString(currentUser.getConfidenceSetting()));

    // Update welcome label
    welcomeLabel.setText("Welcome, " + currentUser.getUsername() + "!");
  }

  /** Update user accuracy level to easy */
  @FXML
  private void onAccuracySetEasy() {
    accuracySettingButton.setText("Accuracy: Easy");
    updateCurrentUser("Accuracy", Level.EASY);
  }

  /** Update user accuracy level to medium */
  @FXML
  private void onAccuracySetMedium() {
    accuracySettingButton.setText("Accuracy: Medium");
    updateCurrentUser("Accuracy", Level.MEDIUM);
  }

  /** Update user accuracy level to hard */
  @FXML
  private void onAccuracySetHard() {
    accuracySettingButton.setText("Accuracy: Hard");
    updateCurrentUser("Accuracy", Level.HARD);
  }

  /** Update user words level to easy */
  @FXML
  private void onWordsSetEasy() {
    wordsSettingButton.setText("Words: Easy");
    updateCurrentUser("Words", Level.EASY);
  }

  /** Update user words level to medium */
  @FXML
  private void onWordsSetMedium() {
    wordsSettingButton.setText("Words: Medium");
    updateCurrentUser("Words", Level.MEDIUM);
  }

  /** Update user words level to hard */
  @FXML
  private void onWordsSetHard() {
    wordsSettingButton.setText("Words: Hard");
    updateCurrentUser("Words", Level.HARD);
  }

  /** Update user words level to master */
  @FXML
  private void onWordsSetMaster() {
    wordsSettingButton.setText("Words: Master");
    updateCurrentUser("Words", Level.MASTER);
  }

  /** Update user time level to easy */
  @FXML
  private void onTimeSetEasy() {
    timeSettingButton.setText("Time: Easy");
    updateCurrentUser("Time", Level.EASY);
  }

  /** Update user time level to medium */
  @FXML
  private void onTimeSetMedium() {
    timeSettingButton.setText("Time: Medium");
    updateCurrentUser("Time", Level.MEDIUM);
  }

  /** Update user time level to hard */
  @FXML
  private void onTimeSetHard() {
    timeSettingButton.setText("Time: Hard");
    updateCurrentUser("Time", Level.HARD);
  }

  /** Update user time level to master */
  @FXML
  private void onTimeSetMaster() {
    timeSettingButton.setText("Time: Master");
    updateCurrentUser("Time", Level.MASTER);
  }

  /** Update user confidence level to easy */
  @FXML
  private void onConfidenceSetEasy() {
    confidenceSettingButton.setText("Confidence: Easy");
    updateCurrentUser("Confidence", Level.EASY);
  }

  /** Update user confidence level to medium */
  @FXML
  private void onConfidenceSetMedium() {
    confidenceSettingButton.setText("Confidence: Medium");
    updateCurrentUser("Confidence", Level.MEDIUM);
  }

  /** Update user confidence level to hard */
  @FXML
  private void onConfidenceSetHard() {
    confidenceSettingButton.setText("Confidence: Hard");
    updateCurrentUser("Confidence", Level.HARD);
  }

  /** Update user confidence level to master */
  @FXML
  private void onConfidenceSetMaster() {
    confidenceSettingButton.setText("Confidence: Master");
    updateCurrentUser("Confidence", Level.MASTER);
  }

  /**
   * Update the current user difficulty settings
   *
   * @param difficultyCategory difficulty setting to update
   * @param levelSetting the level of difficulty the user would like to choose
   */
  private void updateCurrentUser(String difficultyCategory, Level levelSetting) {

    // Update appropriate difficulty category for the current user
    if (difficultyCategory == "Accuracy") {
      currentUser.setAccuracySetting(levelSetting);
    } else if (difficultyCategory == "Words") {
      currentUser.setWordsSetting(levelSetting);
    } else if (difficultyCategory == "Time") {
      currentUser.setTimeSetting(levelSetting);
    } else {
      // In all other cases, this would be the confidence setting
      currentUser.setConfidenceSetting(levelSetting);
    }

    try {
      FXMLLoader canvasLoader = SceneManager.getCanvasLoader();
      CanvasController canvasController = canvasLoader.getController();
      canvasController.setCurrentUser(currentUser);
      canvasController.setUsersHashMap(usersHashMap);
      canvasController.saveData();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
