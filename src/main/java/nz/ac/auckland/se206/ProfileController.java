package nz.ac.auckland.se206;

import ai.djl.translate.TranslateException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
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

  @FXML private ImageView profilePic;

  @FXML private ImageView b1;

  @FXML private ImageView b2;

  @FXML private ImageView b3;

  @FXML private ImageView b4;

  @FXML private ImageView b5;

  @FXML private ImageView b6;

  @FXML private ImageView b7;

  @FXML private ImageView b8;

  @FXML private ImageView b9;

  @FXML private ImageView b10;

  @FXML private ImageView b11;

  @FXML private ImageView b12;

  @FXML private ImageView cat;

  @FXML private ImageView dog;

  @FXML private ImageView bear;

  @FXML private ImageView panda;

  @FXML private ImageView monkey;

  @FXML private ImageView lion;

  @FXML private ImageView tiger;

  @FXML private ImageView koala;

  @FXML private PieChart pieChart;

  // Create hashmap to store details about the badges
  private HashMap<Integer, String> badgesHashMap = new HashMap<Integer, String>();

  // Create hashmap to store all of the users.
  private HashMap<String, User> usersHashMap = new HashMap<String, User>();

  // Current user logged in
  private User currentUser = new User("None");

  /**
   * Get the registered users hashmap
   *
   * @return hashmap containing all the registered users
   */
  public HashMap<String, User> getUsersHashMap() {
    return usersHashMap;
  }

  /**
   * Set the registered users hashmap
   *
   * @param usersHashMap containing all the registered users
   */
  public void setUsersHashMap(HashMap<String, User> usersHashMap) {
    this.usersHashMap = usersHashMap;
  }

  /**
   * Get the current user that is logged in
   *
   * @return the current user
   */
  public User getCurrentUser() {
    return currentUser;
  }

  /**
   * Set the current user that is logged in
   *
   * @param currentUser that is logged in
   */
  public void setCurrentUser(User currentUser) {
    this.currentUser = currentUser;
  }

  /** Update the labels on the profile page */
  protected void updateLabels() {
    // Get current user
    FXMLLoader menuLoader = SceneManager.getMenuLoader();
    MenuController menuController = menuLoader.getController();
    this.currentUser = menuController.getCurrentUser();
    this.usersHashMap = menuController.getUsersHashMap();

    // Set profile picture
    disableAllPictures();
    setProfilePicture(currentUser.getProfilePic());

    // Check if badges hashmap has been populated yet
    if (badgesHashMap.isEmpty()) {
      // If empty, populate with badge details
      // Badges for win times
      badgesHashMap.put(1, "Win in under 5 seconds.");
      badgesHashMap.put(2, "Win in under 10 seconds.");
      badgesHashMap.put(3, "Win in under 30 seconds.");
      // Badges for games won
      badgesHashMap.put(4, "Win 50 games.");
      badgesHashMap.put(5, "Win 25 games.");
      badgesHashMap.put(6, "Win 10 games.");
      // Badges for games lost
      badgesHashMap.put(7, "Lose 50 games.");
      badgesHashMap.put(8, "Lose 25 games.");
      badgesHashMap.put(9, "Lose 10 games.");
      // Badges for difficulty
      badgesHashMap.put(10, "Win with all max difficulty.");
      badgesHashMap.put(11, "Win with all hard difficulty.");
      badgesHashMap.put(12, "Win with all medium difficulty.");
    }

    // Toggle badges depending on if user has received any
    toggleBadges();

    // Update welcome label
    welcomeLabel.setText("Welcome, " + currentUser.getUsername() + "!");

    // Update info label
    infoLabel.setText(currentUser.formatUserDetails());

    // Displays the user-statistics in a pie chart form
    ObservableList<PieChart.Data> pieChartData =
        FXCollections.observableArrayList(
            new PieChart.Data("Wins", currentUser.getWins()),
            new PieChart.Data("Losses", currentUser.getLosses()));
    pieChart.setData(pieChartData);

    // Update used words label
    usedWordsBox.setText(currentUser.getUsedWordsString());
  }

  /**
   * Button to switch to the menu page
   *
   * @param event click event
   * @throws IOException If the file is not found.
   * @throws TranslateException If error is raised during processing of input/output
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
   * @param event click event
   * @throws IOException If the file is not found.
   * @throws TranslateException If error is raised during processing of input/output
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
  private void onB1Click() {
    badgeDescription.setText(badgesHashMap.get(1));
  }

  /** When badge #2 is clicked */
  @FXML
  private void onB2Click() {
    badgeDescription.setText(badgesHashMap.get(2));
  }

  /** When badge #3 is clicked */
  @FXML
  private void onB3Click() {
    badgeDescription.setText(badgesHashMap.get(3));
  }

  /** When badge #4 is clicked */
  @FXML
  private void onB4Click() {
    badgeDescription.setText(badgesHashMap.get(4));
  }

  /** When badge #5 is clicked */
  @FXML
  private void onB5Click() {
    badgeDescription.setText(badgesHashMap.get(5));
  }

  /** When badge #6 is clicked */
  @FXML
  private void onB6Click() {
    badgeDescription.setText(badgesHashMap.get(6));
  }

  /** When badge #7 is clicked */
  @FXML
  private void onB7Click() {
    badgeDescription.setText(badgesHashMap.get(7));
  }

  /** When badge #8 is clicked */
  @FXML
  private void onB8Click() {
    badgeDescription.setText(badgesHashMap.get(8));
  }

  /** When badge #9 is clicked */
  @FXML
  private void onB9Click() {
    badgeDescription.setText(badgesHashMap.get(9));
  }

  /** When badge #10 is clicked */
  @FXML
  private void onB10Click() {
    badgeDescription.setText(badgesHashMap.get(10));
  }

  /** When badge #11 is clicked */
  @FXML
  private void onB11Click() {
    badgeDescription.setText(badgesHashMap.get(11));
  }

  /** When badge #12 is clicked */
  @FXML
  private void onB12Click() {
    badgeDescription.setText(badgesHashMap.get(12));
  }

  /** Toggle display of badges based on whether the user has earned them yet */
  @FXML
  private void toggleBadges() {
    // Get current user badges
    ArrayList<Boolean> badgesArray = currentUser.getBadgesArray();

    // Iterate through the badges
    for (int i = 0; i < 12; i++) {
      if (badgesArray.get(i) == true) {
        // Brighten badge if user has obtained it
        getImageView(i).setOpacity(1);
      } else {
        // Dull badge if user has not obtained it
        getImageView(i).setOpacity(0.2);
      }
    }
  }

  /** Get the corresponding badge imageView component */
  private ImageView getImageView(int i) {
    ImageView badgeImage = new ImageView();
    // Time badges
    if (i == 0) {
      badgeImage = b1;
    } else if (i == 1) {
      badgeImage = b2;
    } else if (i == 2) {
      badgeImage = b3;
    } else if (i == 3) {
      // Games won badges
      badgeImage = b4;
    } else if (i == 4) {
      badgeImage = b5;
    } else if (i == 5) {
      badgeImage = b6;
    } else if (i == 6) {
      // Games lost badges
      badgeImage = b7;
    } else if (i == 7) {
      badgeImage = b8;
    } else if (i == 8) {
      badgeImage = b9;
    } else if (i == 9) {
      // Difficulty badges
      badgeImage = b10;
    } else if (i == 10) {
      badgeImage = b11;
    } else if (i == 11) {
      badgeImage = b12;
    }

    // Return the badge image corresponding to the badge
    return badgeImage;
  }

  /** Disable all the profile pictures initially */
  @FXML
  private void disableAllPictures() {
    // Set all profile pics invisible
    cat.setVisible(false);
    dog.setVisible(false);
    bear.setVisible(false);
    koala.setVisible(false);
    // Last four
    tiger.setVisible(false);
    lion.setVisible(false);
    monkey.setVisible(false);
    panda.setVisible(false);
  }

  /**
   * Set the user's profile picture
   *
   * @param profilePicIndex corresponding index of the profile pic
   */
  @FXML
  private void setProfilePicture(Integer profilePicIndex) {
    // Check profile picture index, and set the corresponding image
    if (profilePicIndex == 0) {
      // First four
      cat.setVisible(true);
    } else if (profilePicIndex == 1) {
      dog.setVisible(true);
    } else if (profilePicIndex == 2) {
      bear.setVisible(true);
    } else if (profilePicIndex == 3) {
      koala.setVisible(true);
    } else if (profilePicIndex == 4) {
      // Last four
      lion.setVisible(true);
    } else if (profilePicIndex == 5) {
      tiger.setVisible(true);
    } else if (profilePicIndex == 6) {
      monkey.setVisible(true);
    } else if (profilePicIndex == 7) {
      panda.setVisible(true);
    }
    ;
  }
}
