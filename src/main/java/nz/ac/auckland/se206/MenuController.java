package nz.ac.auckland.se206;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.models.Level;
import nz.ac.auckland.se206.profile.User;

public class MenuController {
  private Integer selectedProfile;

  @FXML private TextField usernameField;

  @FXML private Label userStatusLabel;

  @FXML private Text playerOneText;

  @FXML private Text playerTwoText;

  @FXML private Text playerThreeText;

  @FXML private Text playerFourText;

  @FXML private Text playerFiveText;

  @FXML private Text playerSixText;

  @FXML private Text playerSevenText;

  @FXML private Text playerEightText;

  @FXML private Button playerOneButton;

  @FXML private Button playerTwoButton;

  @FXML private Button playerThreeButton;

  @FXML private Button playerFourButton;

  @FXML private Button playerFiveButton;

  @FXML private Button playerSixButton;

  @FXML private Button playerSevenButton;

  @FXML private Button playerEightButton;

  @FXML private Button loginButton;

  @FXML private Button registerButton;

  // Create hashmap to store all of the users.
  private HashMap<String, User> usersHashMap = new HashMap<String, User>();

  // Current user logged in
  private User currentUser = new User("None");

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

  public void initialize() throws IOException {
    loadUsers();

    loginButton.setDisable(true);
    registerButton.setDisable(true);
    usernameField.setDisable(true);

    // Loop through every user in the usersHashMap
    for (String thisUser : usersHashMap.keySet()) {
      if (usersHashMap.get(thisUser).getProfilePic() == 0) {
        playerOneText.setText(thisUser);
      } else if (usersHashMap.get(thisUser).getProfilePic() == 1) {
        playerTwoText.setText(thisUser);
      } else if (usersHashMap.get(thisUser).getProfilePic() == 2) {
        playerThreeText.setText(thisUser);
      } else if (usersHashMap.get(thisUser).getProfilePic() == 3) {
        playerFourText.setText(thisUser);
      } else if (usersHashMap.get(thisUser).getProfilePic() == 4) {
        playerFiveText.setText(thisUser);
      } else if (usersHashMap.get(thisUser).getProfilePic() == 5) {
        playerSixText.setText(thisUser);
      } else if (usersHashMap.get(thisUser).getProfilePic() == 6) {
        playerSevenText.setText(thisUser);
      } else if (usersHashMap.get(thisUser).getProfilePic() == 7) {
        playerEightText.setText(thisUser);
      }
    }
  }

  /**
   * Gets the difficulty string from file, and puts the difficulty settings into an array
   *
   * @param difficultyString
   * @return ArrayList of the 4 different levels for the user difficulty settings
   */
  private ArrayList<Level> loadDifficultySettingsFromFile(String difficultyString) {
    ArrayList<Level> difficultyArray = new ArrayList<Level>();

    // Traverse the difficulty string
    for (int i = 0; i < difficultyString.length(); i++) {

      // Check what difficulty the current character represents
      if (difficultyString.charAt(i) == 'E') {
        difficultyArray.add(Level.EASY);
      } else if (difficultyString.charAt(i) == 'M') {
        difficultyArray.add(Level.MEDIUM);
      } else if (difficultyString.charAt(i) == 'H') {
        difficultyArray.add(Level.HARD);
      } else {
        difficultyArray.add(Level.MASTER);
      }
    }
    return difficultyArray;
  }

  /** Load the user's badges into an array from file */
  private ArrayList<Boolean> loadBadgesFromFile(String badgesSaveString) {
    ArrayList<Boolean> badgesArray = new ArrayList<Boolean>();

    // using simple for-loop
    for (int i = 0; i < badgesSaveString.length(); i++) {
      System.out.print(badgesSaveString.charAt(i));
      if (badgesSaveString.charAt(i) == 'T') {
        badgesArray.add(true);
      } else {
        badgesArray.add(false);
      }
    }

    return badgesArray;
  }

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
      User insertUser = new User(parts[0]);
      insertUser.loadUser(
          parts[0],
          Integer.valueOf(parts[1]),
          Integer.valueOf(parts[2]),
          Integer.valueOf(parts[3]),
          Integer.valueOf(parts[4]),
          loadDifficultySettingsFromFile(parts[5]).get(0),
          loadDifficultySettingsFromFile(parts[5]).get(1),
          loadDifficultySettingsFromFile(parts[5]).get(2),
          loadDifficultySettingsFromFile(parts[5]).get(3),
          loadBadgesFromFile(parts[6]));
      usersHashMap.put(parts[0], insertUser);

      // If the user has used words, add those too
      if (parts.length == 8) {
        usersHashMap.get(parts[0]).getWordsToArray(parts[7]);
      }
    }

    reader.close();
  }

  /**
   * Handles registration of new users. Will check if their username exists in the hashmap, and if
   * not, it registers them.
   *
   * @throws IOException
   */
  @FXML
  private void onRegister() throws IOException {

    loadUsers();

    // If the username does not already exist, register user
    if (!usersHashMap.containsKey(usernameField.getText())) {
      // Create a new user profile with the inputted username
      User newUser = new User(usernameField.getText());

      // Update user profile pic
      newUser.setProfilePic(selectedProfile);

      // Add user to hashmap
      usersHashMap.put(usernameField.getText(), newUser);
      userStatusLabel.setText("Successfully registered!");

      // Save the new user to the file
      BufferedWriter bf = null;
      try {
        // Create new BufferedWriter for the output file, append mode on
        bf = new BufferedWriter(new FileWriter("users.txt", true));

        // Add information regarding the new user to the save file
        bf.write(newUser.getSaveDetails());

        bf.newLine();
        bf.flush();

      } catch (IOException e) {
        // Print exceptions
        e.printStackTrace();
      } finally {
        initialize();
        try {
          // Close the writer
          bf.close();
        } catch (Exception e) {
          // Print exceptions
          e.printStackTrace();
        }
      }
    } else {
      userStatusLabel.setText("Sorry, this name is taken!");
    }
  }

  /**
   * For log in functionality - checks if a username exists in the hashmap, and if it does, it logs
   * them in (displays their main stats).
   *
   * @throws IOException
   */
  @FXML
  private void onLogin(ActionEvent event) throws IOException {

    loadUsers();

    // Check if username does exist
    if (usersHashMap.containsKey(usernameField.getText())) {

      // Set the current user ('logs them in')
      currentUser = usersHashMap.get(usernameField.getText());

      usernameField.clear();
      userStatusLabel.setText("");

      // Update welcome label
      FXMLLoader dashboardLoader = SceneManager.getDashboardLoader();
      DashboardController dashboardController = dashboardLoader.getController();
      dashboardController.updateWelcomeLabel();

      // Pass current user and user hash map to canvas
      FXMLLoader canvasLoader = SceneManager.getCanvasLoader();
      CanvasController canvasController = canvasLoader.getController();
      canvasController.setCurrentUser(currentUser);
      canvasController.setUsersHashMap(usersHashMap);

      // Changes the scene to dashboard
      Button btnThatWasClicked = (Button) event.getSource();
      Scene sceneThatThisButtonIsIn = btnThatWasClicked.getScene();
      sceneThatThisButtonIsIn.setRoot(SceneManager.getUi(AppUi.DASHBOARD));

    } else {
      userStatusLabel.setText("Sorry, incorrect login!");
    }
  }

  /** Sets the profile details on click */
  private void setProfileDetails(Text thisText) {
    // If the user profile has already been registered
    if (!thisText.getText().equals("+")) {
      // Display profile username at the bottom of the screen, and enable login button
      registerButton.setDisable(true);
      usernameField.setText(thisText.getText());
      usernameField.setDisable(true);
      loginButton.setDisable(false);
      userStatusLabel.setText("Welcome, " + thisText.getText() + "!");
    } else {
      // If the user profile is empty (not registered yet)
      loginButton.setDisable(true);
      registerButton.setDisable(false);
      usernameField.setText("");
      usernameField.setDisable(false);
      userStatusLabel.setText("You may register!");
    }
  }

  /** Handles login/register for when user 1 is clicked */
  @FXML
  private void onP1Click() {
    setProfileDetails(playerOneText);
    selectedProfile = 0;
  }

  /** Handles login/register for when user 1 is clicked */
  @FXML
  private void onP2Click() {
    setProfileDetails(playerTwoText);
    selectedProfile = 1;
  }

  /** Handles login/register for when user 1 is clicked */
  @FXML
  private void onP3Click() {
    setProfileDetails(playerThreeText);
    selectedProfile = 2;
  }

  /** Handles login/register for when user 1 is clicked */
  @FXML
  private void onP4Click() {
    setProfileDetails(playerFourText);
    selectedProfile = 3;
  }

  /** Handles login/register for when user 1 is clicked */
  @FXML
  private void onP5Click() {
    setProfileDetails(playerFiveText);
    selectedProfile = 4;
  }

  /** Handles login/register for when user 1 is clicked */
  @FXML
  private void onP6Click() {
    setProfileDetails(playerSixText);
    selectedProfile = 5;
  }

  /** Handles login/register for when user 1 is clicked */
  @FXML
  private void onP7Click() {
    setProfileDetails(playerSevenText);
    selectedProfile = 6;
  }

  /** Handles login/register for when user 1 is clicked */
  @FXML
  private void onP8Click() {
    setProfileDetails(playerEightText);
    selectedProfile = 7;
  }
}
