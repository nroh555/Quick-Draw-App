package nz.ac.auckland.se206.profile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import nz.ac.auckland.se206.models.Level;

public class User implements Serializable {
  private String username;

  // User statistics
  private Integer wins;
  private Integer losses;
  private Integer fastestWin;

  // Difficulty information
  private Level accuracySetting;
  private Level wordsSetting;
  private Level timeSetting;
  private Level confidenceSetting;

  // Used words
  private ArrayList<String> usedWords;

  public User(String username) {
    this.username = username;
    this.wins = 0;
    this.losses = 0;
    this.fastestWin = 0;
    this.accuracySetting = Level.EASY;
    this.wordsSetting = Level.EASY;
    this.timeSetting = Level.EASY;
    this.confidenceSetting = Level.EASY;
    this.usedWords = new ArrayList<String>();
  }

  /**
   * This loads user details from the file to create a user instance
   *
   * @param username
   * @param wins
   * @param losses
   * @param fastestWin
   */
  public void loadUser(
      String username,
      Integer wins,
      Integer losses,
      Integer fastestWin,
      Level accuracySetting,
      Level wordsSetting,
      Level timeSetting,
      Level confidenceSetting) {
    // Stores the user's details into corresponding variables
    this.username = username;
    this.wins = wins;
    this.losses = losses;
    this.fastestWin = fastestWin;
    this.accuracySetting = accuracySetting;
    this.wordsSetting = wordsSetting;
    this.timeSetting = timeSetting;
    this.confidenceSetting = confidenceSetting;
  }

  /**
   * This formats all information about the user so it can be saved to a file
   *
   * @return String in the format to go into the save file
   */
  public String getSaveDetails() {
    // Format the difficulty settings for save
    ArrayList<Level> difficultyArray =
        new ArrayList<Level>(
            Arrays.asList(accuracySetting, wordsSetting, timeSetting, confidenceSetting));
    String difficultySettingsString = formatDifficultySettings(difficultyArray);

    // Creates string of the users details
    String saveString =
        username
            + ":"
            + wins.toString()
            + ":"
            + losses.toString()
            + ":"
            + fastestWin.toString()
            + ":"
            + difficultySettingsString;

    /**
     * Returns the user details which includes username, number of wins and losses as well as
     * fastest win time, and all the difficulty settings
     */
    return saveString;
  }

  /**
   * This gets and formats all key information (excluding words) about a user to be displayed
   *
   * @return String to display the key details
   */
  public String formatUserDetails() {
    String fastestWinDisplay;

    // Change fastest win display text depending on if the user has won yet
    if (fastestWin == 0) {
      fastestWinDisplay = "N/A";
    } else {
      fastestWinDisplay = fastestWin.toString() + "s";
    }

    // Creates and formats string of the key user details
    String displayString =
        "Wins: "
            + wins.toString()
            + "\nLosses: "
            + losses.toString()
            + "\nFastest win: "
            + fastestWinDisplay
            + "\n"
            + formatSettingForDisplay(accuracySetting)
            + "\n"
            + formatSettingForDisplay(wordsSetting)
            + "\n"
            + formatSettingForDisplay(timeSetting)
            + "\n"
            + formatSettingForDisplay(confidenceSetting)
            + "\nUsed words: ";

    return displayString;
  }

  /**
   * This returns the array list of used words
   *
   * @return ArrayList<String>
   */
  public ArrayList<String> getUsedWords() {
    return usedWords;
  }

  /**
   * This adds a word to the list of used words
   *
   * @param word
   */
  public void addUsedWord(String word) {
    this.usedWords.add(word);
  }

  /**
   * This returns a string containing the used words on new lines
   *
   * @return
   */
  public String getUsedWordsString() {
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < usedWords.size(); i++) {
      sb.append(usedWords.get(i) + "\n");
    }

    return sb.toString();
  }

  /**
   * Takes the list of used words and chucks it all into a string so it can be saved to a file
   *
   * @param usedWords
   * @return String format of the used words
   */
  public String formatWordsForSave(ArrayList<String> usedWords) {
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < usedWords.size(); i++) {
      sb.append(";" + usedWords.get(i));
    }

    return sb.toString().substring(1, sb.length());
  }

  /**
   * This gets the string version of all the used words and chucks them all into an array
   *
   * @param wordsString
   */
  public void getWordsToArray(String wordsString) {

    String[] parts = wordsString.split(";");

    for (String part : parts) {
      this.usedWords.add(part);
    }
  }

  /**
   * Sets either of the wins, losses, or fastestWins stats for the user
   *
   * @param wins
   * @param losses
   * @param fastestWin
   */
  public void setStats(Integer wins, Integer losses, Integer fastestWin) {
    this.wins = wins;
    this.losses = losses;
    this.fastestWin = fastestWin;
  }

  /**
   * Gets the user's username
   *
   * @return String
   */
  public String getUsername() {
    return username;
  }

  /**
   * Gets the user's number of wins
   *
   * @return Integer
   */
  public Integer getWins() {
    return wins;
  }

  /**
   * Gets the user's number of losses
   *
   * @return Integer
   */
  public Integer getLosses() {
    return losses;
  }

  /**
   * Gets the user's fastest win
   *
   * @return Integer
   */
  public Integer getFastestWin() {
    return fastestWin;
  }

  /**
   * Gets the user's accuracy setting
   *
   * @return Level
   */
  public Level getAccuracySetting() {
    return accuracySetting;
  }

  /**
   * Gets the user's words setting
   *
   * @return Level
   */
  public Level getWordsSetting() {
    return wordsSetting;
  }

  /**
   * Gets the user's time setting
   *
   * @return Level
   */
  public Level getTimeSetting() {
    return timeSetting;
  }

  /**
   * Gets the user's confidence setting
   *
   * @return Level
   */
  public Level getConfidenceSetting() {
    return confidenceSetting;
  }

  public String getDifficultyString(Level thisLevel) {
    if (thisLevel == Level.EASY) {
      return "Easy";
    } else if (thisLevel == Level.MEDIUM) {
      return "Medium";
    } else if (thisLevel == Level.HARD) {
      return "Hard";
    } else {
      return "Master";
    }
  }

  /** Sets the user's accuracy setting */
  public void setAccuracySetting(Level accuracySetting) {
    this.accuracySetting = accuracySetting;
  }

  /** Sets the user's words setting */
  public void setWordsSetting(Level wordsSetting) {
    this.wordsSetting = wordsSetting;
  }

  /** Sets the user's time setting */
  public void setTimeSetting(Level timeSetting) {
    this.timeSetting = timeSetting;
  }

  /** Sets the user's confidence setting */
  public void setConfidenceSetting(Level confidenceSetting) {
    this.confidenceSetting = confidenceSetting;
  }

  private String formatDifficultySettings(ArrayList<Level> difficultyArray) {
    String difficultyString = "";
    for (int i = 0; i < difficultyArray.size(); i++) {
      System.out.println(difficultyArray.get(i));
      if (difficultyArray.get(i) == Level.EASY) {
        difficultyString = difficultyString + "E";
      } else if (difficultyArray.get(i) == Level.MEDIUM) {
        difficultyString = difficultyString + "M";
      } else if (difficultyArray.get(i) == Level.HARD) {
        difficultyString = difficultyString + "H";
      } else if (difficultyArray.get(i) == Level.MASTER) {
        difficultyString = difficultyString + "S";
      } else {
        System.out.println("Error - no corresponding difficulty found");
      }
    }

    return difficultyString;
  }

  /**
   * Takes the difficulty level and returns the string of the difficulty level the setting
   * corresponds to
   *
   * @param difficultyLevel
   * @return String of what the difficulty level corresponds to
   */
  private String formatSettingForDisplay(Level difficultyLevel) {
    String difficultyAsString = "";
    if (difficultyLevel == Level.EASY) {
      difficultyAsString = "Easy";
    } else if (difficultyLevel == Level.MEDIUM) {
      difficultyAsString = "Medium";
    } else if (difficultyLevel == Level.HARD) {
      difficultyAsString = "Hard";
    } else if (difficultyLevel == Level.MASTER) {
      difficultyAsString = "Master";
    } else {
      System.out.println("Error - no corresponding difficulty found");
    }
    return difficultyAsString;
  }
}
