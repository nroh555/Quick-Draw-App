package nz.ac.auckland.se206;

import ai.djl.translate.TranslateException;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.profile.User;

public class DashboardController {
  @FXML private Label welcomeLabel;

  /**
   * Button to switch to the profile page
   *
   * @param event
   * @throws IOException
   * @throws TranslateException
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
   * Button to switch to the canvas page
   *
   * @param event
   * @throws IOException
   * @throws TranslateException
   */
  @FXML
  private void onSwitchToCanvas(ActionEvent event) throws IOException, TranslateException {
    // Changes the scene to canvas
    Button btnThatWasClicked = (Button) event.getSource();
    Scene sceneThatThisButtonIsIn = btnThatWasClicked.getScene();
    sceneThatThisButtonIsIn.setRoot(SceneManager.getUi(AppUi.CANVAS));

    // Runs a prediction to reduce lag
    FXMLLoader canvasLoader = SceneManager.getCanvasLoader();
    CanvasController canvasController = canvasLoader.getController();
    canvasController.updatePrediction();
  }

  protected void updateWelcomeLabel() {
    // Get current user
    FXMLLoader menuLoader = SceneManager.getMenuLoader();
    MenuController menuController = menuLoader.getController();
    User currentUser = menuController.getCurrentUser();

    // Update welcome label
    welcomeLabel.setText("Welcome, " + currentUser.getUsername() + "!");
  }
}
