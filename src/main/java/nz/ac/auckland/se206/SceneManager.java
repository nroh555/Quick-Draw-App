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
    CANVAS,
    CANVAS_ZEN,
    CANVAS_HIDDEN
  }

  private static FXMLLoader canvasLoader;

  private static FXMLLoader menuLoader;

  private static FXMLLoader dashboardLoader;

  private static FXMLLoader profileLoader;

  private static FXMLLoader canvasZenLoader;

  private static FXMLLoader canvasHiddenLoader;

  private static HashMap<AppUi, Parent> sceneMap = new HashMap<AppUi, Parent>();

  public static void addUi(AppUi uiType, Parent parentNode) {
    sceneMap.put(uiType, parentNode);
  }

  public static Parent getUi(AppUi uiType) {
    return sceneMap.get(uiType);
  }

  public static FXMLLoader getMenuLoader() {
    return menuLoader;
  }

  public static void setMenuLoader(FXMLLoader menuLoader) {
    SceneManager.menuLoader = menuLoader;
  }

  public static FXMLLoader getDashboardLoader() {
    return dashboardLoader;
  }

  public static void setDashboardLoader(FXMLLoader dashboardLoader) {
    SceneManager.dashboardLoader = dashboardLoader;
  }

  public static FXMLLoader getProfileLoader() {
    return profileLoader;
  }

  public static void setProfileLoader(FXMLLoader profileLoader) {
    SceneManager.profileLoader = profileLoader;
  }

  public static FXMLLoader getCanvasLoader() {
    return canvasLoader;
  }

  public static void setCanvasLoader(FXMLLoader loader) {
    canvasLoader = loader;
  }

  public static FXMLLoader getCanvasZenLoader() {
    return canvasZenLoader;
  }

  public static void setCanvasZenLoader(FXMLLoader loader) {
    canvasZenLoader = loader;
  }

  public static FXMLLoader getCanvasHiddenLoader() {
    return canvasHiddenLoader;
  }

  public static void setCanvasHiddenLoader(FXMLLoader loader) {
    canvasHiddenLoader = loader;
  }
}
