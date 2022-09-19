package nz.ac.auckland.se206;

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
  @FXML private TextField usernameField;

  @FXML private TextField passwordField;

  @FXML private Label userStatusLabel;

  // Create hashmap to store all of the users.
  private HashMap<String, User> usersHashMap = new HashMap<String, User>();

  // Current user logged in
  private User currentUser = new User("None", "none");

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

  /**
   * Handles registration of new users. Will check if their username exists in the hashmap, and if
   * not, it registers them. This currently doesn't care about the password.
   *
   * @throws IOException
   */
  @FXML
  private void onRegister() throws IOException {

    loadUsers();

    // If the username does not already exist, register user
    if (!usersHashMap.containsKey(usernameField.getText())) {
      // Create a new user profile with the inputted username and password
      User newUser = new User(usernameField.getText(), passwordField.getText());
      // Add user to hashmap

      usersHashMap.put(usernameField.getText(), newUser);
      userStatusLabel.setText("Successfully registered! You may now log in.");

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
        try {
          // Close the writer
          bf.close();
        } catch (Exception e) {
          // Print exceptions
          e.printStackTrace();
        }
      }
    } else {
      userStatusLabel.setText(
          "Sorry, this username is already taken! Please try a different username.");
      System.out.println("This username is already taken. Please try again.");
    }
  }

  /**
   * For log in functionality - checks if a username exists in the hashmap, and if it does, it logs
   * them in (displays their main stats). Currently doesn't check the password.
   *
   * @throws IOException
   */
  @FXML
  private void onLogin(ActionEvent event) throws IOException {

    loadUsers();

    // Check if username does exist
    if (usersHashMap.containsKey(usernameField.getText())) {

      // Check if password is correct
      if (usersHashMap.get(usernameField.getText()).getPassword().equals(passwordField.getText())) {
        // Set the current user ('logs them in')
        currentUser = usersHashMap.get(usernameField.getText());

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
      }
    } else {
      userStatusLabel.setText(
          "Sorry, those login details are incorrect! Please try again or register.");
    }
  }
}
