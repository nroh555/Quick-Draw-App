package nz.ac.auckland.se206;

import ai.djl.translate.TranslateException;
import java.io.IOException;
import java.util.ArrayList;
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
    Map<String, Integer> userFastestWins = new HashMap<>();

    // Add all users and their fastest speeds to hashmap
    for (String user : usersHashMap.keySet()) {
      // If value is not 0 (no fastest win)
      if (usersHashMap.get(user).getFastestWin() != 0) {
        userFastestWins.put(user, usersHashMap.get(user).getFastestWin());
      }
    }

    // Sort the user scores
    List<Entry<String, Integer>> sortedUserFastestWins = sortRankings(userFastestWins, true);

    // Get the appropriate formatting for the text to display on the leaderboard
    ArrayList<String> displayText = generateText(sortedUserFastestWins, " seconds");

    // Update the leaderboard UI
    updateLeaderboardText(
        displayText.get(0), displayText.get(1), displayText.get(2), displayText.get(3));
  }

  /** Updates the leaderboard with the ranking in accordance to most games won */
  public void setWinsLeaderboard() {
    Map<String, Integer> userWins = new HashMap<>();

    // Add all users and their win counts to hashmap
    for (String user : usersHashMap.keySet()) {
      userWins.put(user, usersHashMap.get(user).getWins());
    }

    // Sort the user scores
    List<Entry<String, Integer>> sortedUserWins = sortRankings(userWins, false);

    // Get the appropriate formatting for the text to display on the leaderboard
    ArrayList<String> displayText = generateText(sortedUserWins, " wins");

    // Update the leaderboard UI
    updateLeaderboardText(
        displayText.get(0), displayText.get(1), displayText.get(2), displayText.get(3));
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

  /**
   * Updates the text (top users) on the leaderboard
   *
   * @param firstPlaceText text to display for first place
   * @param secondPlaceText text to display for second place
   * @param thirdPlaceText text to display for third place
   * @param otherPlaceText text to display for other places
   */
  private void updateLeaderboardText(
      String firstPlaceText, String secondPlaceText, String thirdPlaceText, String otherPlaceText) {
    // Update the leaderboard text
    firstPlace.setText(firstPlaceText);
    secondPlace.setText(secondPlaceText);
    thirdPlace.setText(thirdPlaceText);
    otherPlacings.setText(otherPlaceText);
  }

  /**
   * Sort the list of user scores either ascending or descending
   *
   * @param rankingsMap hashmap of all the user scores, unsorted
   * @return List of sorted user rankings on selected category
   */
  private List<Entry<String, Integer>> sortRankings(
      Map<String, Integer> rankingsMap, Boolean order) {
    // Convert hashmap to list to prepare for sorting
    List<Entry<String, Integer>> sortedMap =
        new LinkedList<Entry<String, Integer>>(rankingsMap.entrySet());

    // Sorting all elements so fastest speed is at the top
    Collections.sort(
        sortedMap,
        new Comparator<Entry<String, Integer>>() {
          public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
            // Compare both values
            if (order) {
              // Ascending order
              return o1.getValue().compareTo(o2.getValue());
            } else {
              // Descending order
              return o2.getValue().compareTo(o1.getValue());
            }
          }
        });

    return sortedMap;
  }

  /**
   * Generate the text to display on the leaderboard
   *
   * @param sortedRankings list of hashmap of all the user scores, sorted
   * @param units the string of the units for the category to be displayed
   * @return array of the strings of the text to be displayed on the leaderboard
   */
  private ArrayList<String> generateText(
      List<Entry<String, Integer>> sortedRankings, String units) {
    // Set the text for the placings
    String firstString = "";
    String secondString = "";
    String thirdString = "";
    String otherString = "";

    // Set the text depending on what is used
    Integer numPlacing = 1;
    for (Entry<String, Integer> entry : sortedRankings) {
      if (numPlacing == 1) {
        // First place
        firstString = entry.getKey() + ": " + entry.getValue().toString() + units;
      } else if (numPlacing == 2) {
        // Second place
        secondString = entry.getKey() + ": " + entry.getValue().toString() + units;
      } else if (numPlacing == 3) {
        // Third place
        thirdString = entry.getKey() + ": " + entry.getValue().toString() + units;
      } else {
        // All other places
        otherString = entry.getKey() + ": " + entry.getValue().toString() + units + "\n";
      }
      numPlacing++;
    }

    // Generate arraylist containing all strings to be returned
    ArrayList<String> displayText = new ArrayList<String>();
    displayText.add(firstString);
    displayText.add(secondString);
    displayText.add(thirdString);
    displayText.add(otherString);

    // Return the array of text to display
    return displayText;
  }
}
