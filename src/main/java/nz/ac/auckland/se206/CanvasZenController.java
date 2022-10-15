package nz.ac.auckland.se206;

import ai.djl.ModelException;
import ai.djl.translate.TranslateException;
import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.net.URISyntaxException;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.ml.DoodlePrediction;

public class CanvasZenController extends CanvasController {
  protected boolean runPredictions = false;

  /** Change pen colour */
  private void changePen(Color colour) {
    // Enable eraser button
    eraserButton.setDisable(false);

    // Disable pen button
    penButton.setDisable(true);

    // Update current colour
    setCurrentColor(colour);

    // Change brush
    setPen(colour, 5.0);
  }

  /** Change pen colour to red */
  @FXML
  private void onSetRed() {
    changePen(Color.RED);
  }

  /** Change pen colour to orange */
  @FXML
  private void onSetOrange() {
    changePen(Color.ORANGE);
  }

  /** Change pen colour to yellow */
  @FXML
  private void onSetYellow() {
    changePen(Color.YELLOW);
  }

  /** Change pen colour to lawn green */
  @FXML
  private void onSetLawnGreen() {
    changePen(Color.LAWNGREEN);
  }

  /** Change pen colour to green */
  @FXML
  private void onSetGreen() {
    changePen(Color.GREEN);
  }

  /** Change pen colour to blue */
  @FXML
  private void onSetBlue() {
    changePen(Color.BLUE);
  }

  /** Change pen colour to deep sky blue */
  @FXML
  private void onSetDeepSkyBlue() {
    changePen(Color.DEEPSKYBLUE);
  }

  /** Change pen colour to blue violet */
  @FXML
  private void onSetBlueViolet() {
    changePen(Color.BLUEVIOLET);
  }

  /** Change pen colour to hot pink */
  @FXML
  private void onSetHotPink() {
    changePen(Color.HOTPINK);
  }

  /** Change pen colour to brown */
  @FXML
  private void onSetBrown() {
    changePen(Color.BROWN);
  }

  /** Change pen colour to black */
  @FXML
  private void onSetBlack() {
    changePen(Color.BLACK);
  }

  /**
   * JavaFX calls this method once the GUI elements are loaded. In our case we create a listener for
   * the drawing, and we load the ML model.
   *
   * @throws ModelException If there is an error in reading the input/output of the DL model.
   * @throws IOException If the model cannot be found on the file system.
   * @throws URISyntaxException
   * @throws CsvException
   */
  @Override
  @FXML
  public void initialize() throws ModelException, IOException, CsvException, URISyntaxException {
    graphic = canvas.getGraphicsContext2D();

    model = new DoodlePrediction();

    // Get updated current user
    FXMLLoader menuLoader = SceneManager.getMenuLoader();
    MenuController menuController = menuLoader.getController();
    currentUser = menuController.getCurrentUser();
    usersHashMap = menuController.getUsersHashMap();

    // Gets a random word depending on difficulty setting
    currentWord = getRandomWord();

    // Displays the random word
    wordLabel.setText(currentWord);

    noUnderscoreWord = currentWord.replaceAll(" ", "_");

    // Sets the results label to display draw prompt
    resultLabel.setText("Draw on canvas to begin game!");

    onSetBlack();

    // Enables all buttons and clears canvas
    backButton.setDisable(false);
    eraserButton.setDisable(false);
    clearButton.setDisable(false);
    playAgainButton.setDisable(false);
    saveDrawingButton.setDisable(false);
    onClear();

    // Clears predictions
    predictionLabel.setText("");
    indicatorLabel.setText("");

    // Stops predictions/timer
    runPredictions = false;
  }

  /**
   * Initialises the game
   *
   * @throws ModelException
   * @throws IOException
   * @throws CsvException
   * @throws URISyntaxException
   */
  @FXML
  @Override
  protected void onInitialize()
      throws ModelException, IOException, CsvException, URISyntaxException {
    // initialize the game for the user
    initialize();
  }

  /**
   * Set the pen details: colour and brush size
   *
   * @param colour of the pen
   * @param brushSize of the pen
   */
  @Override
  protected void setPen(Color colour, double brushSize) {
    // save coordinates when mouse is pressed on the canvas
    canvas.setOnMouseDragged(
        e -> {
          // Begin game if user clicks canvas for the first time
          if (!runPredictions) {
            try {
              onReady();
              isReady = true;
            } catch (Exception e1) {
              e1.printStackTrace();
            }
          }

          // Brush size (you can change this, it should not be too small or too large).
          final double size = brushSize;

          final double x = e.getX() - size / 2;
          final double y = e.getY() - size / 2;

          // This is the colour of the brush.
          graphic.setFill(colour);
          graphic.fillOval(x, y, size, size);
        });
  }

  /** Gets ready for the user to start drawing */
  @Override
  @FXML
  protected void onReady() {
    // Sets the results label to display draw prompt
    resultLabel.setText("");

    // Runs predictions/timer
    if (!runPredictions) {
      runTimer();
      runPredictions = true;
    }
  }

  /**
   * Button to switch to the dashboard page *
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

    // Stops predictions/timer
    runPredictions = false;
  }

  /**
   * Updates the prediction text for the user's drawing
   *
   * @throws Exception
   */
  @Override
  protected void updatePredictionText() throws Exception {
    // Updates the GUI to display the predictions
    predictionLabel.setText(predictionString);

    // Get the probability that the user's drawing is right (confidence)
    double newProbability = currentClassification.getProbability();

    // Message of user whether their drawing is becoming more or less accurate
    if (newProbability > currentProbability) {
      indicatorMessage = "Closer to top 10";
    } else if (newProbability < currentProbability) {
      indicatorMessage = "Further from top 10";
    }

    // UPdate the indicator text
    indicatorLabel.setText(indicatorMessage);

    // Set new probability
    currentProbability = newProbability;
  }

  /** This method runs the timer and updates the predictions */
  @Override
  protected void runTimer() {
    Timeline timeline = new Timeline();

    // Create a new keyframe
    KeyFrame keyframe =
        new KeyFrame(
            Duration.seconds(1),
            e -> {
              try {
                // Runs predictions
                updatePrediction();
                updatePredictionText();
              } catch (Exception e1) {
                e1.printStackTrace();
              }

              // Stops predictions/timer
              if (!runPredictions) {
                // Stops the timer
                timeline.stop();
                // Clears prediction label
                predictionLabel.setText("");
              }
            });

    // Sets up the timer
    timeline.getKeyFrames().add(keyframe);
    timeline.setCycleCount(Animation.INDEFINITE);

    // Starts the timer
    timeline.play();
  }
}
