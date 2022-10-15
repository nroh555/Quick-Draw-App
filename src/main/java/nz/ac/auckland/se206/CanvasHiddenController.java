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
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.util.Duration;
import nz.ac.auckland.se206.dict.DictionaryLookup;
import nz.ac.auckland.se206.dict.WordInfo;
import nz.ac.auckland.se206.dict.WordNotFoundException;
import nz.ac.auckland.se206.panes.WordPane;

public class CanvasHiddenController extends CanvasController {

  // Set variables
  private static int counter = 0;
  private String hintWord;

  @FXML private Accordion resultsAccordion;

  @FXML private Button hintButton;

  /**
   * JavaFX calls this method once the GUI elements are loaded. In our case we create a listener for
   * the drawing, and we load the ML model.
   *
   * @throws ModelException If there is an error in reading the input/output of the DL model.
   * @throws IOException If the model cannot be found on the file system.
   * @throws URISyntaxException If string could not be parsed as a URI reference
   * @throws CsvException Base class for all exceptions for opencsv
   */
  @FXML
  public void initialize() throws ModelException, IOException, CsvException, URISyntaxException {
    generalInitialise();

    // Gets a random word depending on difficulty setting
    currentWord = getRandomWord();
    searchWords(currentWord);

    // Displays the random word
    System.out.println(currentWord);

    // Format words appropriately for display
    underscoreWord = currentWord.replaceAll(" ", "_");

    // Disables hint button and makes it appear
    hintButton.setVisible(true);
    hintButton.setDisable(true);

    // Resets the counter variable back to 0
    counter = 0;
  }

  /**
   * Runs the game (allows the user to interact with the canvas)
   *
   * @throws Exception General exception
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
              // Search for the word info in the dictionary
              WordInfo wordResult = DictionaryLookup.searchWordInfo(currentWord);
              TitledPane pane = WordPane.generateWordPane("Definitions", wordResult);

              // A runnable to ensure the interface actions are done by the main thread
              Platform.runLater(
                  () -> {
                    resultsAccordion.getPanes().add(pane);
                  });
            } catch (IOException e) {
              // catch IO exception error
              e.printStackTrace();
            } catch (WordNotFoundException e) {
              // The word was not found
              TitledPane pane = WordPane.generateErrorPane(e);
              Platform.runLater(
                  () -> {
                    resultsAccordion.getPanes().add(pane);
                  });
            }
            return null;
          }
        };

    // Start background thread
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
      hintWord =
          hintWord.substring(0, hintWord.length() - 1)
              + currentWord.charAt(currentWord.length() - 1);
      hintButton.setDisable(true);
    }

    resultLabel.setText("Your word is: " + hintWord);
    counter++;
  }
}
