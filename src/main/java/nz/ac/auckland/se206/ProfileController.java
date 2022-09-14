package nz.ac.auckland.se206;

import ai.djl.translate.TranslateException;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class ProfileController {

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
