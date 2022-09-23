package nz.ac.auckland.se206;

import ai.djl.translate.TranslateException;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.profile.User;

public class ProfileController {

  @FXML private Label welcomeLabel;

  @FXML private Label infoLabel;
  
  @FXML private TextArea usedWordsBox;

  protected void updateLabels() {
    // Get current user
    FXMLLoader menuLoader = SceneManager.getMenuLoader();
    MenuController menuController = menuLoader.getController();
    User currentUser = menuController.getCurrentUser();

    // Update welcome label
    welcomeLabel.setText("Welcome, " + currentUser.getUsername() + "!");

    // Update info label
    infoLabel.setText(currentUser.formatUserDetails());
    
    // Update used words label
    usedWordsBox.setText(currentUser.getUsedWordsString());
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
}
