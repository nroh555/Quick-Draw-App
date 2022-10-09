package nz.ac.auckland.se206;

import ai.djl.translate.TranslateException;
import java.io.IOException;
import java.util.HashMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.profile.User;

public class ProfileController {

  @FXML private Label welcomeLabel;

  @FXML private Label infoLabel;

  @FXML private TextArea usedWordsBox;

  @FXML private Text badgeDescription;

  @FXML private Button backButton;

  @FXML private Button logoutButton;

  @FXML private Button b1b;

  @FXML private Button b2b;

  @FXML private Button b3b;

  @FXML private Button b4b;

  @FXML private Button b5b;

  @FXML private Button b6b;

  @FXML private Button b7b;

  @FXML private Button b8b;

  @FXML private Button b9b;

  @FXML private Button b10b;

  @FXML private Button b11b;

  @FXML private Button b12b;

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

    System.out.println(currentUser.getUsedWordsString());
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

  /** When badge #1 is clicked */
  @FXML
  private void onB1Click() {}

  /** When badge #2 is clicked */
  @FXML
  private void onB2Click() {}

  /** When badge #3 is clicked */
  @FXML
  private void onB3Click() {}

  /** When badge #4 is clicked */
  @FXML
  private void onB4Click() {}

  /** When badge #5 is clicked */
  @FXML
  private void onB5Click() {}

  /** When badge #6 is clicked */
  @FXML
  private void onB6Click() {}

  /** When badge #7 is clicked */
  @FXML
  private void onB7Click() {}

  /** When badge #8 is clicked */
  @FXML
  private void onB8Click() {}

  /** When badge #9 is clicked */
  @FXML
  private void onB9Click() {}

  /** When badge #10 is clicked */
  @FXML
  private void onB10Click() {}

  /** When badge #11 is clicked */
  @FXML
  private void onB11Click() {}

  /** When badge #12 is clicked */
  @FXML
  private void onB12Click() {}
}
