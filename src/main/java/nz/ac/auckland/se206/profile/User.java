package nz.ac.auckland.se206.profile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import nz.ac.auckland.se206.models.Level;

public class User implements Serializable {
  private String username;

  // User profile image
  private Integer profilePic;

  // User statistics
  private Integer wins;
  private Integer losses;
  private Integer fastestWin;

  // Difficulty information
  private Level accuracySetting;
  private Level wordsSetting;
  private Level timeSetting;
  private Level confidenceSetting;

  // Badges
  private ArrayList<Boolean> badgesArray;

  // Used words
  private ArrayList<String> usedWords;

  // Constructor for user object
  public User(String username) {
    this.username = username;
    this.profilePic = 0;
    this.wins = 0;
    this.losses = 0;
    this.fastestWin = 0;
    this.accuracySetting = Level.EASY;
    this.wordsSetting = Level.EASY;
    this.timeSetting = Level.EASY;
    this.confidenceSetting = Level.EASY;
    this.badgesArray =
        new ArrayList<Boolean>(
            Arrays.asList(
                false, false, false, false, false, false, false, false, false, false, false,
                false));
    this.usedWords = new ArrayList<String>();
  }

  /**
   * This loads user details from the file to create a user instance
   *
   * @param username The user's username
   * @param wins How many wins the user has
   * @param losses How many losses the user has
   * @param fastestWin The fastest time for which the user has won
   */
  public void loadUser(
      String username,
      Integer profilePicIndex,
      Integer wins,
      Integer losses,
      Integer fastestWin,
      Level accuracySetting,
      Level wordsSetting,
      Level timeSetting,
      Level confidenceSetting,
      ArrayList<Boolean> badgesArray) {
    // Stores the user's details into corresponding variables
    this.username = username;
    this.profilePic = profilePicIndex;
    this.wins = wins;
    this.losses = losses;
    this.fastestWin = fastestWin;
    this.accuracySetting = accuracySetting;
    this.wordsSetting = wordsSetting;
    this.timeSetting = timeSetting;
    this.confidenceSetting = confidenceSetting;
    this.badgesArray = badgesArray;
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

    // Format the badges for save
    String badgesSaveString = formatBadgesForSave(badgesArray);

    // Creates string of the users details
    String saveString =
        username
            + ":"
            + profilePic
            + ":"
            + wins.toString()
            + ":"
            + losses.toString()
            + ":"
            + fastestWin.toString()
            + ":"
            + difficultySettingsString
            + ":"
            + badgesSaveString;

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

    // Calculate the winrate
    double winrate = Double.valueOf(wins) / (Double.valueOf(wins) + Double.valueOf(losses)) * 100;

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
            + "\nWinrate: "
            + Double.toString(winrate)
            + "%\nFastest win: "
            + fastestWinDisplay;

    return displayString;
  }

  /**
   * This returns the array list of used words
   *
   * @return ArrayList<String> of all the words the user has drawn already
   */
  public ArrayList<String> getUsedWords() {
    return usedWords;
  }

  /**
   * This adds a word to the list of used words
   *
   * @param word The word to be added
   */
  public void addUsedWord(String word) {
    this.usedWords.add(word);
  }

  /**
   * This returns a string containing the used words on new lines
   *
   * @return string of all the used words
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
   * @param usedWords array list of used word
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
   * @param wordsString string of all the words
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
   * @param wins Number of times the user has won
   * @param losses Number of times the user has lost
   * @param fastestWin Fastest time in which the user has won
   */
  public void setStats(Integer wins, Integer losses, Integer fastestWin) {
    this.wins = wins;
    this.losses = losses;
    this.fastestWin = fastestWin;
  }

  /**
   * Sets the corresponding index number for the profile pic
   *
   * @param profilePicIndex Index number for the profile pic
   */
  public void setProfilePic(Integer profilePicIndex) {
    this.profilePic = profilePicIndex;
  }

  /**
   * Gets the corresponding index number for the profile pic
   *
   * @return the profile picture index number
   */
  public Integer getProfilePic() {
    return profilePic;
  }

  /**
   * Gets the user's username
   *
   * @return String of the user's username
   */
  public String getUsername() {
    return username;
  }

  /**
   * Gets the user's number of wins
   *
   * @return Integer of the number of wins
   */
  public Integer getWins() {
    return wins;
  }

  /**
   * Gets the user's number of losses
   *
   * @return Integer of the number of losses
   */
  public Integer getLosses() {
    return losses;
  }

  /**
   * Gets the user's fastest win
   *
   * @return Integer of the user's fastest win
   */
  public Integer getFastestWin() {
    return fastestWin;
  }

  /**
   * Gets the user's accuracy setting
   *
   * @return Level of the user's accuracy setting
   */
  public Level getAccuracySetting() {
    return accuracySetting;
  }

  /**
   * Gets the user's words setting
   *
   * @return Level of the user's word setting
   */
  public Level getWordsSetting() {
    return wordsSetting;
  }

  /**
   * Gets the user's time setting
   *
   * @return Level of the user's time setting
   */
  public Level getTimeSetting() {
    return timeSetting;
  }

  /**
   * Gets the user's confidence setting
   *
   * @return Level of the user's confidence setting
   */
  public Level getConfidenceSetting() {
    return confidenceSetting;
  }

  /**
   * Gets the number of badges that the user has earned
   *
   * @return number of badges earned by the user
   */
  public Integer getBadgesCount() {
    Integer badgesCount = 0;
    String toSearch = formatBadgesForSave(badgesArray);

    // Loop through all of the users badges, and count up how many they have
    // obtained
    for (int i = 0; i < toSearch.length(); i++) {
      if (toSearch.charAt(i) == 'T') {
        badgesCount++;
      }
    }

    // Reutrn the number of badges
    return badgesCount;
  }

  /**
   * @param thisLevel selected level
   * @return String of the level the user has selected
   */
  public String getDifficultyString(Level thisLevel) {
    // Check which level they selected, and return the corresponding string
    if (thisLevel == Level.EASY) {
      return "Easy";
    } else if (thisLevel == Level.MEDIUM) {
      return "Medium";
    } else if (thisLevel == Level.HARD) {
      return "Hard";
    } else {
      // In all other cases it will be master
      return "Master";
    }
  }

  /**
   * @return ArrayList<Boolean>
   */
  public ArrayList<Boolean> getBadgesArray() {
    return badgesArray;
  }

  /**
   * Set / update the user's collected badges
   *
   * @param badgesArray array of the user's earned badges
   */
  public void setBadgesArray(ArrayList<Boolean> badgesArray) {
    this.badgesArray = badgesArray;
  }

  /**
   * Sets the user's accuracy setting
   *
   * @param accuracySetting the current setting for accuracy
   */
  public void setAccuracySetting(Level accuracySetting) {
    this.accuracySetting = accuracySetting;
  }

  /**
   * Sets the user's word setting
   *
   * @param wordsSetting the current setting for words
   */
  public void setWordsSetting(Level wordsSetting) {
    this.wordsSetting = wordsSetting;
  }

  /**
   * Sets the user's time setting
   *
   * @param timeSetting the current setting for time
   */
  public void setTimeSetting(Level timeSetting) {
    this.timeSetting = timeSetting;
  }

  /**
   * Sets the user's confidence setting
   *
   * @param confidence the current setting for confidence
   */
  public void setConfidenceSetting(Level confidenceSetting) {
    this.confidenceSetting = confidenceSetting;
  }

  /**
   * Formats the difficulty settings so that it is easy for save
   *
   * @param difficultyArray array containing all the user settings for difficulty
   * @return String of all the user settings for difficulty
   */
  private String formatDifficultySettings(ArrayList<Level> difficultyArray) {
    StringBuilder difficultyStringBuilder = new StringBuilder();
    for (int i = 0; i < difficultyArray.size(); i++) {
      if (difficultyArray.get(i) == Level.EASY) {
        // Represent easy with E
        difficultyStringBuilder.append("E");
      } else if (difficultyArray.get(i) == Level.MEDIUM) {
        // Represent medium with M
        difficultyStringBuilder.append("M");
      } else if (difficultyArray.get(i) == Level.HARD) {
        // Represent hard with H
        difficultyStringBuilder.append("H");
      } else if (difficultyArray.get(i) == Level.MASTER) {
        // Represent master with S
        difficultyStringBuilder.append("S");
      } else {
        System.out.println("Error - no corresponding difficulty found");
      }
    }

    // Convert stringbuilder to string, and return
    String difficultyString = difficultyStringBuilder.toString();
    return difficultyString;
  }

  /**
   * Formats the user's badges for save
   *
   * @param badgesArray array containing all of the badges
   */
  private String formatBadgesForSave(ArrayList<Boolean> badgesArray) {
    StringBuilder badgesSaveStringBuilder = new StringBuilder();

    // Iterate through the badges array
    for (int i = 0; i < badgesArray.size(); i++) {
      if (badgesArray.get(i) == true) {
        badgesSaveStringBuilder.append("T");
      } else {
        badgesSaveStringBuilder.append("F");
      }
    }

    // Convert string builder to string, and return
    String badgesSaveString = badgesSaveStringBuilder.toString();
    return badgesSaveString;
  }
}
