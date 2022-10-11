package nz.ac.auckland.se206;

import ai.djl.ModelException;
import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.net.URISyntaxException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import nz.ac.auckland.se206.dict.DictionaryLookup;
import nz.ac.auckland.se206.dict.WordInfo;
import nz.ac.auckland.se206.dict.WordNotFoundException;
import nz.ac.auckland.se206.ml.DoodlePrediction;
import nz.ac.auckland.se206.models.Level;
import nz.ac.auckland.se206.panes.WordPane;

public class CanvasHiddenController extends CanvasController {

  @FXML private Accordion resultsAccordion;

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
    currentWord = getRandomWord();
    searchWords(currentWord);

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
  }

  public void searchWords(String currentWord) {
    resultsAccordion.getPanes().clear();

    try {
      WordInfo wordResult = DictionaryLookup.searchWordInfo(currentWord);
      TitledPane pane = WordPane.generateWordPane("Definitions", wordResult);
      resultsAccordion.getPanes().add(pane);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (WordNotFoundException e) {
      TitledPane pane = WordPane.generateErrorPane(e);
      resultsAccordion.getPanes().add(pane);
    }
  }
}
