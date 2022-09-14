package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This is the entry point of the JavaFX application, while you can change this class, it should
 * remain as the class that runs the JavaFX application.
 */
public class App extends Application {
  public static void main(final String[] args) {
    launch();
  }

  /**
   * Returns the node associated to the input file.
   *
   * @param loader The name of the FXML file loader .
   * @return The node of the input file.
   * @throws IOException If the file is not found.
   */
  protected static Parent loadFxml(final FXMLLoader loader) throws IOException {
    return loader.load();
  }

  /**
   * Creates the loader for the specified fxml file. The method expects that the file is located in
   * "src/main/resources/fxml"
   *
   * @param fxml The name of the FXML file (without extension)
   * @return The created FXML loader
   * @throws IOException
   */
  protected static FXMLLoader makeLoader(final String fxml) throws IOException {
    return new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml"));
  }

  /**
   * This method is invoked when the application starts. It loads and shows the "Canvas" scene.
   *
   * @param stage The primary stage of the application.
   * @throws IOException If "src/main/resources/fxml/canvas.fxml" is not found.
   */
  @Override
  public void start(final Stage stage) throws IOException {
    // Adds the UI to the scene manager
    SceneManager.addUi(SceneManager.AppUi.MENU, loadFxml(makeLoader("menu")));
    FXMLLoader canvasLoader = makeLoader("canvas");

    // Saves the canvas loader to scene manager
    SceneManager.setLoader(canvasLoader);
    SceneManager.addUi(SceneManager.AppUi.CANVAS, loadFxml(canvasLoader));

    // Saves the dashboard loader to scene manager
    SceneManager.addUi(SceneManager.AppUi.DASHBOARD, loadFxml(makeLoader("dashboard")));

    // Saves the profile loader to scene manager
    SceneManager.addUi(SceneManager.AppUi.PROFILE, loadFxml(makeLoader("profile")));

    final Scene scene = new Scene(SceneManager.getUi(SceneManager.AppUi.MENU), 480, 745);

    stage.setScene(scene);
    stage.show();
  }
}
