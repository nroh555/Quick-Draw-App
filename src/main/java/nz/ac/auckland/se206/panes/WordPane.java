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

  public static TitledPane generateWordPane(String word, WordInfo wordResult) {
    List<WordEntry> entries = wordResult.getWordEntries();

    VBox boxForEntries = new VBox(entries.size());
    TitledPane pane = new TitledPane(word, boxForEntries);

    for (int e = 0; e < entries.size(); e++) {
      WordEntry entry = entries.get(e);

      TextFlow textFlow = new TextFlow();

      StringBuffer definitions = new StringBuffer();
      boxForEntries.getChildren().add(textFlow);

      int i = 0;
      for (String definition : entry.getDefinitions()) {
        if (i < 3) {
          definitions.append("  ‣ ").append(definition).append(System.lineSeparator());
          i++;
        }
      }

      Text definitionText = new Text(definitions.toString());
      textFlow.getChildren().addAll(definitionText);
    }

    return pane;
  }

  public static TitledPane generateErrorPane(WordNotFoundException error) {

    TextFlow textFlow = new TextFlow();
    TitledPane pane =
        new TitledPane(error.getWord() + " (" + error.getMessage().toLowerCase() + ")", textFlow);

    Text text = new Text(error.getMessage() + System.lineSeparator());
    text.setFont(Font.font("Verdana", FontWeight.BOLD, 13));

    Text message = new Text(error.getSubMessage());

    textFlow.getChildren().addAll(text, message);
    return pane;
  }
}
