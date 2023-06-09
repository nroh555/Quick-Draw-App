package nz.ac.auckland.se206;

import ai.djl.ModelException;
import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.net.URISyntaxException;
import javafx.application.Application;
import javafx.application.Platform;
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
   * @throws IOException If the file is not found
   */
  protected static FXMLLoader makeLoader(final String fxml) throws IOException {
    return new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml"));
  }

  /**
   * This method is invoked when the application starts. It loads and shows the "Canvas" scene.
   *
   * @param stage The primary stage of the application.
   * @throws ModelException If there is an error in reading the input/output of the DL model.
   * @throws CsvException Base class for all exceptions for opencsv
   * @throws URISyntaxException If string could not be parsed as a URI reference
   * @throws IOException If the file is not found.
   */
  @Override
  public void start(final Stage stage)
      throws ModelException, CsvException, URISyntaxException, IOException {
    // Saves the menu loader to scene manager
    FXMLLoader menuLoader = makeLoader("menu");
    SceneManager.setMenuLoader(menuLoader);
    SceneManager.addUi(SceneManager.AppUi.MENU, loadFxml(menuLoader));
    MenuController menuController = menuLoader.getController();
    menuController.initialize();

    // Saves the canvas loader to scene manager
    FXMLLoader canvasLoader = makeLoader("canvas");
    SceneManager.setCanvasLoader(canvasLoader);
    SceneManager.addUi(SceneManager.AppUi.CANVAS, loadFxml(canvasLoader));

    // Saves the zen canvas loader to scene manager
    FXMLLoader canvasZenLoader = makeLoader("canvasZen");
    SceneManager.setCanvasZenLoader(canvasZenLoader);
    SceneManager.addUi(SceneManager.AppUi.CANVAS_ZEN, loadFxml(canvasZenLoader));

    // Saves the hidden mode canvas loader to scene manager
    FXMLLoader canvasHiddenLoader = makeLoader("canvasHidden");
    SceneManager.setCanvasHiddenLoader(canvasHiddenLoader);
    SceneManager.addUi(SceneManager.AppUi.CANVAS_HIDDEN, loadFxml(canvasHiddenLoader));

    // Saves the dashboard loader to scene manager
    FXMLLoader dashboardLoader = makeLoader("dashboard");
    SceneManager.setDashboardLoader(dashboardLoader);
    SceneManager.addUi(SceneManager.AppUi.DASHBOARD, loadFxml(dashboardLoader));

    // Saves the profile loader to scene manager
    FXMLLoader profileLoader = makeLoader("profile");
    SceneManager.setProfileLoader(profileLoader);
    SceneManager.addUi(SceneManager.AppUi.PROFILE, loadFxml(profileLoader));

    // Saves the leaderboard loader to scene manager
    FXMLLoader leaderboardLoader = makeLoader("leaderboard");
    SceneManager.setLeaderboardLoader(leaderboardLoader);
    SceneManager.addUi(SceneManager.AppUi.LEADERBOARD, loadFxml(leaderboardLoader));

    final Scene scene = new Scene(SceneManager.getUi(SceneManager.AppUi.MENU), 1150, 800);

    // Close all threads when app is closed
    stage.setOnCloseRequest(
        e -> {
          Platform.exit();
          System.exit(0);
        });

    stage.setScene(scene);
    stage.show();
  }
}
