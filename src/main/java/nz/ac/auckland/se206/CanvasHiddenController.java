package nz.ac.auckland.se206;

import ai.djl.ModelException;
import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.net.URISyntaxException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
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

    // Makes the pen, eraser and clear appear
    penButton.setVisible(true);
    eraserButton.setVisible(true);
    clearButton.setVisible(true);
    hintButton.setVisible(true);
    hintButton.setDisable(false);

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

    // Clears and enables canvas, disables eraser
    canvas.setDisable(false);
    eraserButton.setDisable(true);
    onClear();

    // Clears predictions
    predictionLabel.setText("");
    indicatorLabel.setText("");
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
      resultLabel.setText("Your word has " + currentWord.length() + " characters");
    }

    // The second hint would display the first character of the word
    if (counter == 1) {
      resultLabel.setText("Your word starts with " + currentWord.charAt(0));
    }

    // The third hint would display the last character of the word
    if (counter == 2) {
      resultLabel.setText("Your word ends with " + currentWord.charAt(currentWord.length() - 1));
    }

    // The final hint would display all of the above hints
    if (counter >= 3) {
      hintButton.setVisible(false);
      resultLabel.setText(
          "Your word has "
              + currentWord.length()
              + " characters, starts with "
              + currentWord.charAt(0)
              + " and ends with "
              + currentWord.charAt(currentWord.length() - 1));
    }
    counter++;
  }
}
