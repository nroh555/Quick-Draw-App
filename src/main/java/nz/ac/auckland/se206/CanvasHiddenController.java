package nz.ac.auckland.se206;

import ai.djl.ModelException;
import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.net.URISyntaxException;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.util.Duration;
import nz.ac.auckland.se206.dict.DictionaryLookup;
import nz.ac.auckland.se206.dict.WordInfo;
import nz.ac.auckland.se206.dict.WordNotFoundException;
import nz.ac.auckland.se206.ml.DoodlePrediction;
import nz.ac.auckland.se206.models.Level;
import nz.ac.auckland.se206.panes.WordPane;

public class CanvasHiddenController extends CanvasController {

  @FXML private Accordion resultsAccordion;
  @FXML private Button hintButton;
  private String currentWord;
  private static int counter = 0;
  private String hintWord;

  /**
   * JavaFX calls this method once the GUI elements are loaded. In our case we create a listener for
   * the drawing, and we load the ML model.
   *
   * @throws ModelException If there is an error in reading the input/output of the DL model.
   * @throws IOException If the model cannot be found on the file system.
   * @throws URISyntaxException
   * @throws CsvException
   */
  @FXML
  public void initialize() throws ModelException, IOException, CsvException, URISyntaxException {	  
    graphic = canvas.getGraphicsContext2D();

    model = new DoodlePrediction();

    // Get updated current user
    FXMLLoader menuLoader = SceneManager.getMenuLoader();
    MenuController menuController = menuLoader.getController();
    this.currentUser = menuController.getCurrentUser();
    this.usersHashMap = menuController.getUsersHashMap();

    // Gets a random word depending on difficulty setting
    this.currentWord = getRandomWord();
    searchWords(currentWord); // Perhaps to minimise the issue we could use background thread

    // Displays the random word
    System.out.println(currentWord);

    // Initialises the progress bar as green
    myProgressBar.setStyle("-fx-accent:#00FF00;");
    progress = 0.0;
    myProgressBar.setProgress(progress);

    // Makes the save and play again button dissappear
    saveDrawingButton.setVisible(false);
    playAgainButton.setVisible(false);
    backButton.setVisible(true);
    backButton.setDisable(false);

    // Makes the pen, eraser, clear and hint appear
    penButton.setVisible(true);
    eraserButton.setVisible(true);
    clearButton.setVisible(true);
    hintButton.setVisible(true);

    // Resets the counter variable back to 0
    counter = 0;

    noUnderscoreWord = currentWord.replaceAll(" ", "_");

    // Set timer count depending on the difficulty value
    if (currentUser.getTimeSetting() == Level.EASY) {
      initialCount = 60;
    } else if (currentUser.getTimeSetting() == Level.MEDIUM) {
      initialCount = 45;
    } else if (currentUser.getTimeSetting() == Level.HARD) {
      initialCount = 30;
    } else if (currentUser.getTimeSetting() == Level.MASTER) {
      initialCount = 15;
    }

    count = initialCount;

    time.setText(String.valueOf(count));

    // Sets the results label to display draw prompt
    resultLabel.setText("Draw on canvas to begin game!");

    onPen();

    // Clears and enables canvas, disables eraser, hint, clear
    canvas.setDisable(false);
    eraserButton.setDisable(true);
    hintButton.setDisable(true);
    clearButton.setDisable(true);
    onClear();

    // Clears predictions
    predictionLabel.setText("");
    indicatorLabel.setText("");
  }
  
  /**
   * Runs the game (allows the user to interact with the canvas)
   *
   * @throws Exception
   */
  @FXML
  @Override
  protected void onReady() throws Exception {
    // Enables the canvas
    canvas.setDisable(false);

    // Disables the back button
    backButton.setVisible(false);

    // Enables eraser and clear buttons
    eraserButton.setDisable(false);
    clearButton.setDisable(false);
    hintButton.setDisable(false);

    // Sets the brush to pen
    onPen();

    // Runs the timer
    runTimer();

    // Adds current word to list of used words
    currentUser.addUsedWord(currentWord);

    // Sets the results label to display draw prompt
    resultLabel.setText("");

    saveData();
  }
  
  /** This method runs the timer and updates the predictions */
  @Override
  protected void runTimer() {
    Timeline timeline = new Timeline();
    KeyFrame keyframe =
        new KeyFrame(
            Duration.seconds(1),
            e -> {
              count--;
              increaseProgress();

              // Updates timer label
              time.setText(String.valueOf(count));
              try {
                // Runs predictions
                updatePrediction();
                updatePredictionText();
              } catch (Exception e1) {
                e1.printStackTrace();
              }

              // Checks if game is over
              if (count <= 0 || gameOver) {
                // Speaks the result aloud
                talk();

                // Stops the timer
                timeline.stop();

                // Disables the canvas
                canvas.setDisable(true);

                // Enables the play again, save drawing and back button
                playAgainButton.setVisible(true);
                playAgainButton.setDisable(false);
                saveDrawingButton.setVisible(true);
                saveDrawingButton.setDisable(false);
                backButton.setVisible(true);
                backButton.setDisable(false);

                // Disables the pen, eraser and clear button
                penButton.setVisible(false);
                eraserButton.setVisible(false);
                clearButton.setVisible(false);
                hintButton.setVisible(false);

                isReady = false;
                gameOver = false;
              }
            });

    // Sets up the timer
    timeline.getKeyFrames().add(keyframe);
    timeline.setCycleCount(Animation.INDEFINITE);

    // Starts the timer
    timeline.play();
  }

  /**
   * This method would search and display the definitions of the words
   *
   * @param currentWord - The word that has been randomly generated
   */
  public void searchWords(String currentWord) {
    // Refreshes the defintions pane each time
    resultsAccordion.getPanes().clear();

    /*
     * Creates a background thread in order to fetch the defintions from the
     * definitions API faster
     */
    Task<Void> backgroundTask =
        new Task<Void>() {

          @Override
          protected Void call() throws Exception {
            try {
              WordInfo wordResult = DictionaryLookup.searchWordInfo(currentWord);
              TitledPane pane = WordPane.generateWordPane("Definitions", wordResult);

              // A runnable to ensure the interface actions are done by the main thread
              Platform.runLater(
                  () -> {
                    resultsAccordion.getPanes().add(pane);
                  });
            } catch (IOException e) {
              e.printStackTrace();
            } catch (WordNotFoundException e) {
              TitledPane pane = WordPane.generateErrorPane(e);
              Platform.runLater(
                  () -> {
                    resultsAccordion.getPanes().add(pane);
                  });
            }
            return null;
          }
        };

    Thread backgroundThread = new Thread(backgroundTask);
    backgroundThread.start();
  }

  /** Provides hints to the user */
  @FXML
  private void onHint() {
    // The first hint would display the length of the word
    if (counter == 0) {
      StringBuilder sb = new StringBuilder();

        for (int i = 0; i < currentWord.length(); i++) {
          sb.append("_ ");
        }
       
        hintWord = sb.toString().strip();
    }

    // The second hint would display the first character of the word
    if (counter == 1) {
    	hintWord = currentWord.charAt(0) + hintWord.substring(1);
    }

    // The third hint would display the last character of the word
    if (counter == 2) {
    	hintWord = hintWord.substring(0, hintWord.length()-1) + currentWord.charAt(currentWord.length() - 1);
    	hintButton.setDisable(true);
    }

    resultLabel.setText("Your word is: " + hintWord);
    counter++;
  }
}
