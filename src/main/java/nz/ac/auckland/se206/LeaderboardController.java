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
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.profile.User;

public class LeaderboardController {
  @FXML private Label welcomeLabel;

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
   * Button to switch to the dashboard page
   *
   * @param event click event
   * @throws IOException If the file is not found.
   * @throws TranslateException If error is raised during processing of input/output
   */
  @FXML
  private void onSwitchToDashboard(ActionEvent event) throws IOException, TranslateException {
    System.out.println("SWITCH");
    // Update welcome label in profile
    FXMLLoader dashboardLoader = SceneManager.getDashboardLoader();
    DashboardController dashboardController = dashboardLoader.getController();

    // Changes the scene to canvas
    Button btnThatWasClicked = (Button) event.getSource();
    Scene sceneThatThisButtonIsIn = btnThatWasClicked.getScene();
    sceneThatThisButtonIsIn.setRoot(SceneManager.getUi(AppUi.DASHBOARD));
  }

  /** Update the welcome label for the user */
  protected void updateWelcomeLabel() {
    // Get current user
    FXMLLoader menuLoader = SceneManager.getMenuLoader();
    MenuController menuController = menuLoader.getController();
    this.currentUser = menuController.getCurrentUser();
    this.usersHashMap = menuController.getUsersHashMap();

    // Update welcome label
    welcomeLabel.setText("Welcome, " + currentUser.getUsername() + "!");
  }
}
