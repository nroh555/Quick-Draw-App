package nz.ac.auckland.se206;

import ai.djl.ModelException;
import ai.djl.modality.Classifications;
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
import java.util.Random;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.imageio.ImageIO;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.ml.DoodlePrediction;
import nz.ac.auckland.se206.models.Level;
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

  @FXML protected Canvas canvas;

  @FXML protected Label wordLabel;

  @FXML protected Label time;

  @FXML protected Label predictionLabel;

  @FXML protected Label resultLabel;

  @FXML protected Button playAgainButton;

  @FXML protected Button penButton;

  @FXML protected Button eraserButton;

  @FXML protected Button clearButton;

  @FXML protected Button backButton;

  @FXML protected Button saveDrawingButton;

  @FXML protected ProgressBar myProgressBar;

  @FXML protected Button hintButton;

  @FXML protected Label myLabel;

  @FXML protected Label indicatorLabel;

  protected double progress;

  protected GraphicsContext graphic;

  protected DoodlePrediction model;

  protected String currentWord;

  protected String underscoreWord;

  protected int initialCount = 15;

  protected int count = initialCount;

  protected boolean gameOver = false;

  protected List<Classification> predictionResults;

  protected String predictionString;

  protected String endMessage;

  protected boolean isReady = false;

  protected Color currentColor = Color.BLACK;

  protected Classification currentClassification;

  protected double currentProbability;

  protected String indicatorMessage;

  protected boolean isHiddenMode = false;

  // Create hashmap to store all of the users.
  protected HashMap<String, User> usersHashMap = new HashMap<String, User>();

  // Current user logged in
  protected User currentUser = new User("None");

  /**
   * Get the hashmap with all registered users
   *
   * @return HashMap<String, User> of all registered users
   */
  public HashMap<String, User> getUsersHashMap() {
    return usersHashMap;
  }

  /**
   * Set the hashmap with all registered users
   *
   * @param usersHashMap of all registered users
   */
  public void setUsersHashMap(HashMap<String, User> usersHashMap) {
    this.usersHashMap = usersHashMap;
  }

  /**
   * Get the current logged in user
   *
   * @return User that is logged in
   */
  public User getCurrentUser() {
    return currentUser;
  }

  /**
   * Set the current logged in user
   *
   * @param currentUser that is logged in
   */
  public void setCurrentUser(User currentUser) {
    this.currentUser = currentUser;
  }

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

    // Displays the random word
    wordLabel.setText(currentWord);

    // Format words appropriately for display
    underscoreWord = currentWord.replaceAll(" ", "_");
  }

  /**
   * General method that initialises game
   *
   * @throws ModelException If there is an error in reading the input/output of the DL model.
   * @throws IOException If the model cannot be found on the file system.
   */
  protected void generalInitialise() throws ModelException, IOException {
    graphic = canvas.getGraphicsContext2D();

    model = new DoodlePrediction();

    // Get updated current user
    FXMLLoader menuLoader = SceneManager.getMenuLoader();
    MenuController menuController = menuLoader.getController();
    this.currentUser = menuController.getCurrentUser();
    this.usersHashMap = menuController.getUsersHashMap();

    // Initialises the progress bar as green
    myProgressBar.setStyle("-fx-accent:#00FF00;");
    progress = 0.0;
    myProgressBar.setProgress(progress);

    // Makes the save and play again button disappear
    saveDrawingButton.setVisible(false);
    playAgainButton.setVisible(false);
    backButton.setVisible(true);
    backButton.setDisable(false);

    // Makes the pen, eraser and clear appear
    penButton.setVisible(true);
    eraserButton.setVisible(true);
    clearButton.setVisible(true);

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
    clearButton.setDisable(true);
    onClear();

    // Clears predictions
    predictionLabel.setText("");
    indicatorLabel.setText("");
  }

  /**
   * Initialises the game before playing
   *
   * @throws ModelException If there is an error in reading the input/output of the DL model.
   * @throws IOException If the file is not found.
   * @throws CsvException Base class for all exceptions for opencsv
   * @throws URISyntaxException If string could not be parsed as a URI reference
   */
  @FXML
  protected void onInitialize()
      throws ModelException, IOException, CsvException, URISyntaxException {
    initialize();
  }

  /**
   * This method sets the brush size and colour
   *
   * @param colour Colour of the brush
   */
  protected void setPen(Color colour, double brushSize) {
    // save coordinates when mouse is pressed on the canvas
    canvas.setOnMouseDragged(
        e -> {
          // Begin game if user clicks canvas for the first time
          if (!isReady && count == initialCount) {
            try {
              // Check if user is ready
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

  /**
   * Set the current color of the brush
   *
   * @param colour we wish to set
   */
  public void setCurrentColor(Color colour) {
    this.currentColor = colour;
  }

  /** Sets the brush to erase */
  @FXML
  private void onErase() {
    // Disable eraser button
    eraserButton.setDisable(true);

    // Enable pen button
    penButton.setDisable(false);

    // Change brush
    setPen(Color.WHITE, 12.0);
  }

  /** Sets the brush to pen */
  @FXML
  protected void onPen() {
    // Enable eraser button
    eraserButton.setDisable(false);

    // Disable pen button
    penButton.setDisable(true);

    // Change brush
    setPen(currentColor, 5.0);
  }

  /** This method is called when the "Clear" button is pressed. */
  @FXML
  protected void onClear() {
    graphic.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    if (isReady) {
      onPen();
    }
  }

  /**
   * Button to switch to the dashboard page *
   *
   * @param event click event
   * @throws IOException If the file is not found.
   * @throws TranslateException If error is raised during processing of input/output
   * @throws URISyntaxException If string could not be parsed as a URI reference
   */
  @FXML
  private void onSwitchToDashboard(ActionEvent event)
      throws IOException, TranslateException, URISyntaxException {
    MenuController.buttonSound();
    // Changes the scene to canvas
    Button btnThatWasClicked = (Button) event.getSource();
    Scene sceneThatThisButtonIsIn = btnThatWasClicked.getScene();
    sceneThatThisButtonIsIn.setRoot(SceneManager.getUi(AppUi.DASHBOARD));
  }

  /** This function would increase the progress bar to match with the timer */
  protected void increaseProgress() {
    // Increases the progress bar by a frequency per initial count number of seconds
    progress += (1.0 / initialCount);
    myProgressBar.setProgress(progress);

    // The progress bar changes from green to orange under 30 seconds
    if (progress >= 0.5 && progress < 0.85) {
      myProgressBar.setStyle("-fx-accent:orange;");
    }

    // The progress bar changes from orange to red under 10 seconds
    if (progress >= 0.85) {
      myProgressBar.setStyle("-fx-accent:red;");
    }
  }

  /**
   * Gets the latest predictions every second
   *
   * @throws TranslateException If error is raised during processing of input/output
   */
  public void updatePrediction() throws TranslateException {
    // Gets a list of the predictions
    Classifications classifications = model.getClassifications(getCurrentSnapshot());
    predictionResults = model.getPredictionsNew(classifications, 10);
    currentClassification = model.getKeywordClassification(classifications, underscoreWord);

    // Gets a string of the predictions
    predictionString = DoodlePrediction.makePredictionString(predictionResults);
  }

  /**
   * Updates the prediction UI
   *
   * @throws Exception General exception
   */
  protected void updatePredictionText() throws Exception {
    // Updates the GUI to display the predictions
    predictionLabel.setText(predictionString);

    double newProbability = currentClassification.getProbability();

    // Message of user whether their drawing is becoming more or less accurate
    if (newProbability > currentProbability) {
      indicatorMessage = "Closer to top 10";
    } else if (newProbability < currentProbability) {
      indicatorMessage = "Further from top 10";
    }

    // Continuously update the indicator message
    indicatorLabel.setText(indicatorMessage);
    currentProbability = newProbability;

    // Check if user has won
    if (isWin(predictionResults)) {
      // Prints win message
      endMessage = "Congratulations, you have won! :)";
      resultLabel.setText(endMessage);
      MediaPlayer winSound = MenuController.setSound("win.wav");
      winSound.play();
      gameOver = true;
    } else if (count <= 0) {
      // Prints lose message
      endMessage = "Nice try! The word was " + currentWord;
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
   * @throws Exception General exception
   */
  private boolean isWin(List<Classification> classifications) throws Exception {

    // If accuracy setting is easy, player could win if word is in top 1
    if (currentUser.getAccuracySetting() == Level.EASY) {
      // Loops through top 3 predictions
      for (int i = 0; i < 3; i++) {
        // Checks if a prediction equals the keyword, if so stops game
        if (classifications.get(i).getClassName().equals(underscoreWord)) {
          if (checkConfidenceLevel(classifications, i) == true) {
            // Update the user details with stats after this game
            Integer winTime = initialCount - count;
            updateBadges(winTime);
            addFastestWin(winTime);
            addWin();
            return true;
          }
        }
      }
      // If accuracy setting is medium, player could win if word is in top 2
    } else if (currentUser.getAccuracySetting() == Level.MEDIUM) {
      // Loops through top 2 predictions
      for (int i = 0; i < 2; i++) {
        // Checks if a prediction equals the keyword, if so stops game
        if (classifications.get(i).getClassName().equals(underscoreWord)) {
          if (checkConfidenceLevel(classifications, i) == true) {
            // Update the user stats
            Integer winTime = initialCount - count;
            updateBadges(winTime);
            addFastestWin(winTime);
            addWin();
            return true;
          }
        }
      }
      // If accuracy setting is hard, player could win if word is in top 1
    } else if (currentUser.getAccuracySetting() == Level.HARD) {
      // Check top prediction only
      if (classifications.get(0).getClassName().equals(underscoreWord)) {
        if (checkConfidenceLevel(classifications, 0) == true) {
          // Update the user stats
          Integer winTime = initialCount - count;
          updateBadges(winTime);
          addFastestWin(winTime);
          addWin();
          return true;
        }
      }
    }

    return false;
  }

  /**
   * Checks if the confidence level of the top predictions are of sufficient percentage for a win
   *
   * @param classifications of the predictions
   * @param wordIndex index of the word we are looking for
   * @return boolean of whether the confidence level is sufficient
   */
  private boolean checkConfidenceLevel(List<Classification> classifications, Integer wordIndex) {
    // If user confidence level setting is easy
    if (currentUser.getConfidenceSetting() == Level.EASY) {
      if (classifications.get(wordIndex).getProbability() >= 0.01) {
        return true;
      }
    } else if (currentUser.getConfidenceSetting() == Level.MEDIUM) {
      // If user confidence level setting is medium
      if (classifications.get(wordIndex).getProbability() >= 0.10) {
        return true;
      }
    } else if (currentUser.getConfidenceSetting() == Level.HARD) {
      // If user confidence level setting is hard
      if (classifications.get(wordIndex).getProbability() >= 0.25) {
        return true;
      }
    } else if (currentUser.getConfidenceSetting() == Level.MASTER) {
      // If user confidence level setting is master
      if (classifications.get(wordIndex).getProbability() >= 0.50) {
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
        new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);

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

  /**
   * This method runs the timer and updates the predictions
   *
   * @throws URISyntaxException
   */
  protected void runTimer() throws URISyntaxException {
    Timeline timeline = new Timeline();
    MediaPlayer timerSound = MenuController.setSound("timer.wav");
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

                // Once the game ends the music player will stop
                timerSound.stop();
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

                // Hides hint button if current canvas is hidden mode
                if (isHiddenMode) {
                  hintButton.setVisible(false);
                }

                isReady = false;
                gameOver = false;
              } else {
                try {
                  MenuController.playback(timerSound);
                } catch (URISyntaxException e1) {
                  // TODO Auto-generated catch block
                  e1.printStackTrace();
                }
              }
            });

    // Sets up the timer
    timeline.getKeyFrames().add(keyframe);
    timeline.setCycleCount(Animation.INDEFINITE);

    // Starts the timer
    timeline.play();
  }

  /** Uses text to speech to say the results message */
  protected void talk() {
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
   * @throws Exception General exception
   */
  @FXML
  protected void onReady() throws Exception {
    // Enables the canvas
    canvas.setDisable(false);

    // Disables the back button
    backButton.setVisible(false);

    // Enables eraser and clear buttons
    eraserButton.setDisable(false);
    clearButton.setDisable(false);

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

  /**
   * Adds 1 to the current user's wins count
   *
   * @throws Exception General exception
   */
  @FXML
  private void addWin() throws Exception {
    currentUser.setStats(
        currentUser.getWins() + 1, currentUser.getLosses(), currentUser.getFastestWin());

    // Update users hash map
    usersHashMap.put(currentUser.getUsername(), currentUser);

    // Print user detail to console
    System.out.println("CANVAS USER DETAILS");
    System.out.println(currentUser.formatUserDetails());

    saveData();
  }

  /**
   * Adds 1 to the current user's loss count
   *
   * @throws Exception General exception
   */
  @FXML
  private void addLoss() throws Exception {
    currentUser.setStats(
        currentUser.getWins(), currentUser.getLosses() + 1, currentUser.getFastestWin());

    // Update users hash map
    usersHashMap.put(currentUser.getUsername(), currentUser);

    saveData();
  }

  /**
   * This method generates the random word that the user has to draw
   *
   * @return
   * @throws IOException If the file is not found.
   * @throws CsvException Base class for all exceptions for opencsv
   * @throws URISyntaxException If string could not be parsed as a URI reference
   */
  protected String getRandomWord() throws IOException, CsvException, URISyntaxException {
    ArrayList<String> usedWords = currentUser.getUsedWords();
    String randomWord = "";
    Boolean firstSelection = true;

    // Get random word
    CategorySelector categorySelector = new CategorySelector();

    // While the random word is already in the list of used words, choose another
    // random word
    while (firstSelection == true || usedWords.contains(randomWord)) {
      // If words difficulty setting is easy, select only easy words
      if (currentUser.getWordsSetting() == Level.EASY) {
        firstSelection = false;
        randomWord = categorySelector.getRandomCategory(Difficulty.E);
      } else if (currentUser.getWordsSetting() == Level.MEDIUM) {
        // If words difficulty setting is medium, select only easy or medium words
        firstSelection = false;
        String[] possibleWords =
            new String[] {
              categorySelector.getRandomCategory(Difficulty.E),
              categorySelector.getRandomCategory(Difficulty.M)
            };

        // randomly selects a number out of 2
        Random random = new Random();
        int select = random.nextInt(2);

        randomWord = possibleWords[select];
      } else if (currentUser.getWordsSetting() == Level.HARD) {
        // If words difficulty setting is hard, select only easy, medium, or hard words
        firstSelection = false;
        String[] possibleWords =
            new String[] {
              categorySelector.getRandomCategory(Difficulty.E),
              categorySelector.getRandomCategory(Difficulty.M),
              categorySelector.getRandomCategory(Difficulty.H)
            };

        // randomly selects a number out of 3
        Random random = new Random();
        int select = random.nextInt(3);

        randomWord = possibleWords[select];
      } else if (currentUser.getWordsSetting() == Level.MASTER) {
        // If words difficulty setting is master, select only hard words
        firstSelection = false;
        randomWord = categorySelector.getRandomCategory(Difficulty.H);
      }
    }

    return randomWord;
  }

  /**
   * Updates the user's fastest win time if the current win time is faster than the record time.
   *
   * @throws Exception General exception
   */
  private void addFastestWin(int currentWinTime) throws Exception {
    int recordFastestWin = currentUser.getFastestWin();

    // Check if user has no recorded win time or if the current win time is faster
    // than the record
    // win time
    if (recordFastestWin == 0 || currentWinTime < recordFastestWin) {
      // Updates users stats
      currentUser.setStats(currentUser.getWins(), currentUser.getLosses(), currentWinTime);

      // Update users hash map
      usersHashMap.put(currentUser.getUsername(), currentUser);

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
   * @throws Exception General exception
   */
  protected void saveData() throws Exception {
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

  /**
   * Update the badges that the user has earned
   *
   * @throws Exception General exception
   */
  private void updateBadges(Integer winTime) throws Exception {
    // Get current user badges
    ArrayList<Boolean> badgesArray = currentUser.getBadgesArray();
    // Loop through each of the user badges, and check if need to
    // be updated
    for (Boolean badge : badgesArray) {
      if (badge == false) {
        // Check if player has won in under 5 seconds
        if (winTime <= 5) {
          badgesArray.set(0, true);
          badgesArray.set(1, true);
          badgesArray.set(2, true);
        }
      } else if (badge == false) {
        // Check if player has won in under 10 seconds
        if (winTime <= 10) {
          badgesArray.set(1, true);
          badgesArray.set(2, true);
        }
      } else if (badge == false) {
        // Check if player has won in under 30 seconds
        if (winTime <= 30) {
          badgesArray.set(2, true);
        }
      } else if (badge == false) {
        // Check if player has won 50 games
        if (currentUser.getWins() >= 50) {
          badgesArray.set(3, true);
        }
      } else if (badge == false) {
        // Check if player has won 25 games
        if (currentUser.getWins() >= 25) {
          badgesArray.set(4, true);
        }
      } else if (badge == false) {
        // Check if player has won 10 games
        if (currentUser.getWins() >= 10) {
          badgesArray.set(5, true);
        }
      } else if (badge == false) {
        // Check if player has lost 50 games
        if (currentUser.getLosses() >= 50) {
          badgesArray.set(6, true);
        }
      } else if (badge == false) {
        // Check if player has lost 25 games
        if (currentUser.getLosses() >= 25) {
          badgesArray.set(7, true);
        }
      } else if (badge == false) {
        // Check if player has lost 10 games
        if (currentUser.getLosses() >= 10) {
          badgesArray.set(8, true);
        }
      } else if (badge == false) {
        // Check if player has won a game with all max difficulty
        if (currentUser.getAccuracySetting() == Level.HARD
            && currentUser.getConfidenceSetting() == Level.MASTER
            && currentUser.getTimeSetting() == Level.MASTER
            && currentUser.getWordsSetting() == Level.MASTER) {
          badgesArray.set(9, true);
        }
      } else if (badge == false) {
        // Check if player has won a game with all hard difficulty
        if (currentUser.getAccuracySetting() == Level.HARD
            && currentUser.getConfidenceSetting() == Level.HARD
            && currentUser.getTimeSetting() == Level.HARD
            && currentUser.getWordsSetting() == Level.HARD) {
          badgesArray.set(10, true);
        }
      } else if (badge == false) {
        // Check if player has won a game with all medium difficulty
        if (currentUser.getAccuracySetting() == Level.MEDIUM
            && currentUser.getConfidenceSetting() == Level.MEDIUM
            && currentUser.getTimeSetting() == Level.MEDIUM
            && currentUser.getWordsSetting() == Level.MEDIUM) {
          badgesArray.set(11, true);
        }
      }
    }
    // Update user and save to file
    currentUser.setBadgesArray(badgesArray);
    saveData();
  }
}
