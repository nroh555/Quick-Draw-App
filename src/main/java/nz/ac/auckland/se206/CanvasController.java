package nz.ac.auckland.se206;

import ai.djl.ModelException;
import ai.djl.modality.Classifications.Classification;
import ai.djl.translate.TranslateException;
import com.opencsv.exceptions.CsvException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.imageio.ImageIO;
import nz.ac.auckland.se206.ml.DoodlePrediction;
import nz.ac.auckland.se206.profile.User;
import nz.ac.auckland.se206.speech.TextToSpeech;
import nz.ac.auckland.se206.words.CategorySelector;
import nz.ac.auckland.se206.words.CategorySelector.Difficulty;

/**
 * This is the controller of the canvas. You are free to modify this class and the corresponding
 * FXML file as you see fit. For example, you might no longer need the "Predict" button because the
 * DL model should be automatically queried in the background every second.
 *
 * <p>!! IMPORTANT !!
 *
 * <p>Although we added the scale of the image, you need to be careful when changing the size of the
 * drawable canvas and the brush size. If you make the brush too big or too small with respect to
 * the canvas size, the ML model will not work correctly. So be careful. If you make some changes in
 * the canvas and brush sizes, make sure that the prediction works fine.
 */
public class CanvasController {

  @FXML private Canvas canvas;

  @FXML private Label wordLabel;

  @FXML private Label time;

  @FXML private Label predictionBox1;

  @FXML private Label resultLabel;

  @FXML private Button playAgainButton;

  @FXML private Button penButton;

  @FXML private Button eraserButton;

  @FXML private Button clearButton;

  @FXML private Button saveDrawingButton;

  @FXML private Button readyButton;

  private GraphicsContext graphic;

  private DoodlePrediction model;

  private String currentWord;

  private String noUnderscoreWord;

  private int initialCount = 60;

  private int count = initialCount;

  private boolean gameOver = false;

  private List<Classification> predictionResults;

  private String predictionString;

  private String endMessage;

  private boolean initialGameStart = false;

  // Create hashmap to store all of the users.
  private HashMap<String, User> usersHashMap = new HashMap<String, User>();

  // Current user logged in
  private User currentUser = new User("None", "none");

  public HashMap<String, User> getUsersHashMap() {
    return usersHashMap;
  }

  public void setUsersHashMap(HashMap<String, User> usersHashMap) {
    this.usersHashMap = usersHashMap;
  }

  public User getCurrentUser() {
    return currentUser;
  }

  public void setCurrentUser(User currentUser) {
    this.currentUser = currentUser;
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
  @FXML
  public void initialize() throws ModelException, IOException, CsvException, URISyntaxException {
    graphic = canvas.getGraphicsContext2D();

    model = new DoodlePrediction();

    // Gets a random word from the easy difficulty
    currentWord = getRandomWord();

    // Displays the random word
    wordLabel.setText(currentWord);

    noUnderscoreWord = currentWord.replaceAll(" ", "_");

    // Sets the timer to the count value
    count = initialCount;
    time.setText(String.valueOf(count));

    // Sets the results label to display no text
    resultLabel.setText("");

    // Clears and disables canvas
    canvas.setDisable(true);
    onClear();

    // Clears predictions
    predictionBox1.setText("");
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
  public void onInitialize() throws ModelException, IOException, CsvException, URISyntaxException {
    initialize();
    readyButton.setDisable(false);
    playAgainButton.setDisable(true);
    saveDrawingButton.setDisable(true);
  }

  /**
   * This method sets the brush size and colour
   *
   * @param colour Colour of the brush
   */
  private void setPen(Color colour) {
    // save coordinates when mouse is pressed on the canvas
    canvas.setOnMouseDragged(
        e -> {
          // Brush size (you can change this, it should not be too small or too large).
          final double size = 5.0;

          final double x = e.getX() - size / 2;
          final double y = e.getY() - size / 2;

          // This is the colour of the brush.
          graphic.setFill(colour);
          graphic.fillOval(x, y, size, size);
        });
  }

  /** Sets the brush to erase */
  @FXML
  private void onErase() {
    // Disable eraser button
    eraserButton.setDisable(true);

    // Enable pen button
    penButton.setDisable(false);

    // Change brush
    setPen(Color.WHITE);
  }

  /** Sets the brush to pen */
  @FXML
  private void onBlackPen() {
    // Enable eraser button
    eraserButton.setDisable(false);

    // Disable pen button
    penButton.setDisable(true);

    // Change brush
    setPen(Color.BLACK);
  }

  /** This method is called when the "Clear" button is pressed. */
  @FXML
  private void onClear() {
    graphic.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    if (initialGameStart) {
      onBlackPen();
    }
  }

  /**
   * Gets the latest predictions
   *
   * @throws TranslateException
   */
  public void updatePrediction() throws TranslateException {
    // Gets a list of the predictions
    predictionResults = model.getPredictions(getCurrentSnapshot(), 10);

    // Gets a string of the predictions
    predictionString = DoodlePrediction.makePredictionString(predictionResults);
  }

  /**
   * Updates the prediction UI
   *
   * @throws Exception
   */
  private void updatePredictionText() throws Exception {

    // Updates the GUI to display the predictions
    predictionBox1.setText(predictionString);

    // Check if user has won
    if (isWin(predictionResults)) {
      // Prints win message
      endMessage = "Congratulations, you have won! :)";
      resultLabel.setText(endMessage);
      gameOver = true;
    } else if (count <= 0) {
      // Prints lose message
      endMessage = "Sorry, you have lost :(";
      resultLabel.setText(endMessage);
      gameOver = true;
      addLoss();
    }
  }

  /**
   * Checks if the player has won. The player wins if the computer guesses the word in their top 3
   * predictions
   *
   * @param classifications the list of predictions
   * @return A boolean which indicates if the user won
   * @throws Exception
   */
  private boolean isWin(List<Classification> classifications) throws Exception {
    for (int i = 0; i < 3; i++) {
      if (classifications.get(i).getClassName().equals(noUnderscoreWord)) {
        int winTime = initialCount - count;
        addFastestWin(winTime);
        addWin();
        return true;
      }
    }
    return false;
  }

  /**
   * Get the current snapshot of the canvas.
   *
   * @return The BufferedImage corresponding to the current canvas content.
   */
  private BufferedImage getCurrentSnapshot() {
    final Image snapshot = canvas.snapshot(null, null);
    final BufferedImage image = SwingFXUtils.fromFXImage(snapshot, null);

    // Convert into a binary image.
    final BufferedImage imageBinary =
        new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);

    final Graphics2D graphics = imageBinary.createGraphics();

    graphics.drawImage(image, 0, 0, null);

    // To release memory we dispose.
    graphics.dispose();

    return imageBinary;
  }

  /**
   * Save the current snapshot on a bitmap file.
   *
   * @return The file of the saved image.
   * @throws IOException If the image cannot be saved.
   */
  @FXML
  private File saveCurrentSnapshotOnFile() throws IOException {
    // You can change the location as you see fit.
    final File tmpFolder = new File("tmp");

    if (!tmpFolder.exists()) {
      tmpFolder.mkdir();
    }

    // We save the image to a file in the tmp folder.
    final File imageToClassify =
        new File(tmpFolder.getName() + "/snapshot" + System.currentTimeMillis() + ".bmp");

    // Save the image to a file.
    ImageIO.write(getCurrentSnapshot(), "bmp", imageToClassify);

    return imageToClassify;
  }

  /**
   * Save the current snapshot on a bitmap file to a specified location.
   *
   * @throws IOException If the image cannot be saved.
   */
  @FXML
  private void onSaveCurrentSnapshotOnFileSpecific() throws IOException {
    // Set up file chooser
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save image");
    fileChooser.getExtensionFilters().add(new ExtensionFilter("BMP Files", "*.bmp"));
    fileChooser.setInitialFileName("snapshot" + System.currentTimeMillis());

    // Show save dialog
    File file = fileChooser.showSaveDialog(new Stage());

    // Save the image to a file
    if (file != null) {
      ImageIO.write(getCurrentSnapshot(), "bmp", file);
    }
  }

  /** This method runs the timer and updates the predictions */
  protected void runTimer() {
    Timeline timeline = new Timeline();
    KeyFrame keyframe =
        new KeyFrame(
            Duration.seconds(1),
            e -> {
              count--;

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

                // Enables the play again and save drawing button
                playAgainButton.setDisable(false);
                saveDrawingButton.setDisable(false);

                // Disables the pen, eraser and clear button
                penButton.setDisable(true);
                eraserButton.setDisable(true);
                clearButton.setDisable(true);
              }
            });

    // Sets up the timer
    timeline.getKeyFrames().add(keyframe);
    timeline.setCycleCount(Animation.INDEFINITE);

    // Starts the timer
    timeline.play();
  }

  /** Uses text to speech to say the results message */
  private void talk() {
    // This task uses text to speech to say the result aloud
    Task<Void> speakTask =
        new Task<Void>() {

          @Override
          protected Void call() throws Exception {
            TextToSpeech textToSpeech = new TextToSpeech();
            // Says the result message aloud
            textToSpeech.speak(endMessage);
            return null;
          }
        };

    Thread speakThread = new Thread(speakTask);
    speakThread.start();
  }

  /**
   * Runs the game (allows the user to interact with the canvas)
   *
   * @throws Exception
   */
  @FXML
  private void onReady() throws Exception {
    gameOver = false;

    // Enables the canvas
    canvas.setDisable(false);

    // Enables eraser and clear buttons
    eraserButton.setDisable(false);
    clearButton.setDisable(false);

    // Disable ready button
    readyButton.setDisable(true);

    // Sets the brush to pen
    onBlackPen();

    // Runs the timer
    runTimer();

    initialGameStart = true;

    currentUser.addUsedWord(currentWord);
    saveData();

    // System.out.println("USED WORDS: ");
    // currentUser.displayUsedWords();
  }

  /**
   * Adds 1 to the user's current wins count
   *
   * @throws Exception
   */
  @FXML
  private void addWin() throws Exception {
    currentUser.setStats(
        currentUser.getWins() + 1, currentUser.getLosses(), currentUser.getFastestWin());

    // Update users hash map
    usersHashMap.put(currentUser.getUsername(), currentUser);

    // Print user detail to console
    System.out.println("CANVAS USER DETAILS " + currentUser.formatUserDetails());

    saveData();
  }

  /**
   * Adds one to the current user's loss count
   *
   * @throws Exception
   */
  @FXML
  private void addLoss() throws Exception {
    currentUser.setStats(
        currentUser.getWins(), currentUser.getLosses() + 1, currentUser.getFastestWin());

    // Update users hash map
    usersHashMap.put(currentUser.getUsername(), currentUser);

    // Print user detail to console
    System.out.println("CANVAS USER DETAILS " + currentUser.formatUserDetails());

    saveData();
  }

  /**
   * Add a word (the word is just egg) to current user word list
   *
   * @throws Exception
   */
  @FXML
  private void addWord() throws Exception {
    // Adds egg to the current user's used words list
    usersHashMap.get(currentUser.getUsername()).addUsedWord("eggs");
  }

  protected String getRandomWord() throws IOException, CsvException, URISyntaxException {
    ArrayList<String> usedWords = currentUser.getUsedWords();
    CategorySelector categorySelector = new CategorySelector();
    String randomWord = categorySelector.getRandomCategory(Difficulty.E);

    while (usedWords.contains(randomWord)) {
      System.out.println(randomWord + " has been already used =================");
      randomWord = categorySelector.getRandomCategory(Difficulty.E);
    }

    return randomWord;
  }

  /**
   * Adds 1 to the user's current wins count
   *
   * @throws Exception
   */
  private void addFastestWin(int currentWinTime) throws Exception {

    int recordFastestWin = currentUser.getFastestWin();
    if (recordFastestWin == 0 || currentWinTime < recordFastestWin) {
      System.out.println("=======================================================================");
      System.out.println("Current record: " + recordFastestWin);
      currentUser.setStats(currentUser.getWins(), currentUser.getLosses(), currentWinTime);

      // Update users hash map
      usersHashMap.put(currentUser.getUsername(), currentUser);

      // Prints record
      System.out.println("New record: " + currentUser.getFastestWin());

      saveData();
    }
  }

  /** Updates the user hash map and current user in the menu controller */
  private void updateUserAndMap() {
    FXMLLoader menuLoader = SceneManager.getMenuLoader();
    MenuController menuController = menuLoader.getController();
    menuController.setUsersHashMap(usersHashMap);
    menuController.setCurrentUser(currentUser);
  }

  /**
   * Saves any stats data. This is a manual save that is performed via a button (as it's going to be
   * very time consuming to write the save contents all the time)
   *
   * @throws Exception
   */
  @FXML
  private void saveData() throws Exception {
    updateUserAndMap();

    // Save all the new data to the file
    BufferedWriter bf = null;

    // Overwrite existing file data for the first line we save
    try {
      // Create new BufferedWriter for the output file, append mode on
      bf = new BufferedWriter(new FileWriter("users.txt"));

      Boolean isFirst = true;
      for (String key : usersHashMap.keySet()) {
        if (isFirst == true) {
          // If user doesn't have any used words
          if (usersHashMap.get(key).getUsedWords().isEmpty()) {
            // Add information regarding first user to each first
            bf.write(usersHashMap.get(key).getSaveDetails());
          } else {
            bf.write(
                usersHashMap.get(key).getSaveDetails()
                    + ":"
                    + usersHashMap
                        .get(key)
                        .formatWordsForSave(usersHashMap.get(key).getUsedWords()));
          }
          isFirst = false;
        } else {
          break;
        }
      }
      bf.newLine();
      bf.flush();
    } catch (IOException e) {
      // Print exceptions
      e.printStackTrace();
    } finally {
      try {
        // Close the writer
        bf.close();
      } catch (Exception e) {
        // Print exceptions
        e.printStackTrace();
      }
    }

    // Every other line we save after the first one is on append mode for the file
    try {
      // Create new BufferedWriter for the output file, append mode on
      bf = new BufferedWriter(new FileWriter("users.txt", true));

      Boolean isFirst = true;
      for (String key : usersHashMap.keySet()) {
        if (isFirst == true) {
          isFirst = false;
        } else {
          // Add information regarding each user (other than the first) to the save file
          // If user doesn't have any used words
          if (usersHashMap.get(key).getUsedWords().isEmpty()) {
            // Add information regarding first user to each first
            bf.write(usersHashMap.get(key).getSaveDetails());
          } else {
            bf.write(
                usersHashMap.get(key).getSaveDetails()
                    + ":"
                    + usersHashMap
                        .get(key)
                        .formatWordsForSave(usersHashMap.get(key).getUsedWords()));
          }
          bf.newLine();
        }
      }
      bf.flush();
    } catch (IOException e) {
      // Print exceptions
      e.printStackTrace();
    } finally {
      try {
        // Close the writer
        bf.close();
      } catch (Exception e) {
        // Print exceptions
        e.printStackTrace();
      }
    }
  }
}
