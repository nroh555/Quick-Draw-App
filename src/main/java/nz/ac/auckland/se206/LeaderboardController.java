package nz.ac.auckland.se206;

import ai.djl.translate.TranslateException;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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

  @FXML private Label firstPlace;

  @FXML private Label secondPlace;

  @FXML private Label thirdPlace;

  @FXML private Label otherPlacings;

  // Create hashmap to store all of the users.
  private HashMap<String, User> usersHashMap = new HashMap<String, User>();

  // Current user logged in
  private User currentUser = new User("None");

  /** Fetch the updated hashmap of registered users */
  public void fetchUsersHashmap() {
    // Get updated current user and users hashmap
    FXMLLoader menuLoader = SceneManager.getMenuLoader();
    MenuController menuController = menuLoader.getController();
    this.currentUser = menuController.getCurrentUser();
    this.usersHashMap = menuController.getUsersHashMap();
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
    // Changes the scene to dashboard
    Button btnThatWasClicked = (Button) event.getSource();
    Scene sceneThatThisButtonIsIn = btnThatWasClicked.getScene();
    sceneThatThisButtonIsIn.setRoot(SceneManager.getUi(AppUi.DASHBOARD));
  }

  /** Updates the leaderboard with the ranking in accordance to fastest time */
  public void setSpeedLeaderboard() {
    System.out.println(currentUser);

    getSpeedLeaderboard();
  }

  /** Updates the leaderboard with the ranking in accordance to fastest time */
  private void getSpeedLeaderboard() {

    Map<String, Integer> userFastestWins = new HashMap<>();

    // Add all users and their fastest speeds to hashmap
    for (String user : usersHashMap.keySet()) {
      // If value is not 0 (no fastest win)
      if (usersHashMap.get(user).getFastestWin() != 0) {
        userFastestWins.put(user, usersHashMap.get(user).getFastestWin());
      }
    }

    // Convert hashmap to list to prepare for sorting
    List<Entry<String, Integer>> sortedUserFastestWins =
        new LinkedList<Entry<String, Integer>>(userFastestWins.entrySet());

    // Sorting all elements so fastest speed is at the top
    Collections.sort(
        sortedUserFastestWins,
        new Comparator<Entry<String, Integer>>() {
          public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
            // Compare both values
            return o1.getValue().compareTo(o2.getValue());
          }
        });

    // Set the text for the placings
    String firstString = "";
    String secondString = "";
    String thirdString = "";
    String otherString = "";

    // Set the text depending on what is used
    Integer numPlacing = 1;
    for (Entry<String, Integer> entry : sortedUserFastestWins) {
      if (numPlacing == 1) {
        System.out.println("First");
        firstString = entry.getKey() + ": " + entry.getValue().toString() + " seconds";
        System.out.println(firstString);
      } else if (numPlacing == 2) {
        secondString = entry.getKey() + ": " + entry.getValue().toString() + " seconds";
      } else if (numPlacing == 3) {
        thirdString = entry.getKey() + ": " + entry.getValue().toString() + " seconds";
      } else {
        otherString = entry.getKey() + ": " + entry.getValue().toString() + " seconds";
      }
      numPlacing++;
    }

    updateLeaderboardText(firstString, secondString, thirdString, otherString);
  }

  /** Updates the leaderboard with the ranking in accordance to most games won */
  public void setWinsLeaderboard() {
    System.out.println(currentUser);

    // Update the leaderboard text
    firstPlace.setText("hey2");
    secondPlace.setText("hey3");
    thirdPlace.setText("hey4");
    otherPlacings.setText("hey1");
  }

  /** Updates the leaderboard with the ranking in accordance to most badges obtained */
  public void setBadgesLeaderboard() {
    System.out.println(currentUser);

    // Update the leaderboard text
    firstPlace.setText("hsdfey");
    secondPlace.setText("hesdfy");
    thirdPlace.setText("hesdfy");
    otherPlacings.setText("hsdfey");
  }

  /** Updates the leaderboard with the ranking in accordance to most badges obtained */
  private void updateLeaderboardText(
      String firstPlaceText, String secondPlaceText, String thirdPlaceText, String otherPlaceText) {
    // Update the leaderboard text
    firstPlace.setText(firstPlaceText);
    secondPlace.setText(secondPlaceText);
    thirdPlace.setText(thirdPlaceText);
    otherPlacings.setText(otherPlaceText);
  }
}
