package nz.ac.auckland.se206;

import java.util.HashMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

/**
 * This class manages the scenes
 *
 * @author amanda
 */
public class SceneManager {
  public enum AppUi {
    MENU,
    DASHBOARD,
    PROFILE,
    CANVAS
  }

  private static FXMLLoader canvasLoader;

  private static HashMap<AppUi, Parent> sceneMap = new HashMap<AppUi, Parent>();

  public static void addUi(AppUi uiType, Parent parentNode) {
    sceneMap.put(uiType, parentNode);
  }

  public static Parent getUi(AppUi uiType) {
    return sceneMap.get(uiType);
  }

  public static void setLoader(FXMLLoader loader) {
    canvasLoader = loader;
  }

  public static FXMLLoader getLoader() {
    return canvasLoader;
  }
}
