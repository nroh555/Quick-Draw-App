package nz.ac.auckland.se206.words;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * This class selects the category of difficulty.
 *
 * @author amanda
 */
public class CategorySelector {
  public enum Difficulty {
    E,
    M,
    H
  }

  private Map<Difficulty, List<String>> difficulty2categories;

  public CategorySelector() throws IOException, CsvException, URISyntaxException {
    difficulty2categories = new HashMap<>();

    // Creates a key for each difficulty
    for (Difficulty difficulty : Difficulty.values()) {
      difficulty2categories.put(difficulty, new ArrayList<>());
    }

    // Adds the words to their corresponding difficulty key in the map
    for (String[] line : getLines()) {
      difficulty2categories.get(Difficulty.valueOf(line[1])).add(line[0]);
    }
  }

  /**
   * This method selects a random word from the chosen difficulty
   *
   * @param difficulty the specified game difficulty
   * @return the random word
   */
  public String getRandomCategory(Difficulty difficulty) {
    return difficulty2categories
        .get(difficulty)
        .get(new Random().nextInt(difficulty2categories.get(difficulty).size()));
  }

  /**
   * This method goes reads the lines in the CSV file and puts them into a list of strings
   *
   * @return A list of strings of the lines
   * @throws IOException
   * @throws CsvException
   * @throws URISyntaxException
   */
  protected List<String[]> getLines() throws IOException, CsvException, URISyntaxException {
    File file = new File(CategorySelector.class.getResource("/category_difficulty.csv").toURI());

    try (FileReader fr = new FileReader(file, StandardCharsets.UTF_8);
        CSVReader reader = new CSVReader(fr)) {
      return reader.readAll();
    }
  }
}
