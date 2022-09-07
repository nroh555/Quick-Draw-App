package nz.ac.auckland.se206;

import ai.djl.translate.TranslateException;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class MenuController {

  /**
   * This method switches from menu to canvas
   *
   * @throws TranslateException
   */
  @FXML
  private void onSwitchToCanvas(ActionEvent event) throws IOException, TranslateException {
    // Changes the scene to canvas
    Button btnThatWasClicked = (Button) event.getSource();
    Scene sceneThatThisButtonIsIn = btnThatWasClicked.getScene();
    sceneThatThisButtonIsIn.setRoot(SceneManager.getUi(AppUi.CANVAS));

    // Runs a prediction to reduce lag
    FXMLLoader loader = SceneManager.getLoader();
    CanvasController controller = loader.getController();
    controller.updatePrediction();
  }
}
