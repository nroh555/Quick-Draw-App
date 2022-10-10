package nz.ac.auckland.se206;

import javafx.fxml.FXML;
import javafx.scene.paint.Color;

public class CanvasZenController extends CanvasController {
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
  
}
