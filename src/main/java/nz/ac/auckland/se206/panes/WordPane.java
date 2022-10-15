package nz.ac.auckland.se206.panes;

import java.util.List;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import nz.ac.auckland.se206.dict.WordEntry;
import nz.ac.auckland.se206.dict.WordInfo;
import nz.ac.auckland.se206.dict.WordNotFoundException;

public class WordPane {

  /**
   * Generate out a word pane
   *
   * @param word we want
   * @param wordResult we want
   * @return TitledPane
   */
  public static TitledPane generateWordPane(String word, WordInfo wordResult) {
    // Get all the word entries
    List<WordEntry> entries = wordResult.getWordEntries();

    // Create a new titled pane in a vertical box
    VBox boxForEntries = new VBox(entries.size());
    TitledPane pane = new TitledPane(word, boxForEntries);

    // Iterate through each entry, and populate definitions with the word entry
    for (int e = 0; e < entries.size(); e++) {

      // Prepare the display for the definitions
      WordEntry entry = entries.get(e);
      TextFlow textFlow = new TextFlow();
      StringBuffer definitions = new StringBuffer();
      boxForEntries.getChildren().add(textFlow);

      // Iterate through every entry, and get all the definitions
      int i = 0;
      for (String definition : entry.getDefinitions()) {
        if (i < 3) {
          definitions.append("  ‣ ").append(definition).append(System.lineSeparator());
          i++;
        }
      }

      // Get defintions and add them all to the text flow element
      Text definitionText = new Text(definitions.toString());
      textFlow.getChildren().addAll(definitionText);
    }

    return pane;
  }

  /**
   * Generate an error pane if a word is not found
   *
   * @param error of word not found
   * @return TitledPane with the error information
   */
  public static TitledPane generateErrorPane(WordNotFoundException error) {

    // Create new textflow
    TextFlow textFlow = new TextFlow();

    // Display error on titled pane
    TitledPane pane =
        new TitledPane(error.getWord() + " (" + error.getMessage().toLowerCase() + ")", textFlow);

    Text text = new Text(error.getMessage() + System.lineSeparator());
    text.setFont(Font.font("Verdana", FontWeight.BOLD, 13));

    // Get and add the error message
    Text message = new Text(error.getSubMessage());
    textFlow.getChildren().addAll(text, message);

    // Return this pane with the error
    return pane;
  }
}
