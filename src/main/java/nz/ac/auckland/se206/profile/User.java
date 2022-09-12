package nz.ac.auckland.se206.profile;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
  private String username;
  private String password;
  private Integer wins;
  private Integer losses;
  private Integer fastestWin;
  private ArrayList<String> usedWords;

  public User(String username, String password) {
    this.username = username;
    this.password = password;
    this.wins = 0;
    this.losses = 0;
    this.fastestWin = 0;
    this.usedWords = new ArrayList<String>();
  }

  /**
   * This loads user details from the file to create a user instance
   *
   * @param username
   * @param password
   * @param wins
   * @param losses
   * @param fastestWin
   */
  public void loadUser(
      String username, String password, Integer wins, Integer losses, Integer fastestWin) {
    this.username = username;
    this.password = password;
    this.wins = wins;
    this.losses = losses;
    this.fastestWin = fastestWin;
  }

  /**
   * This formats all information about the user so it can be saved to a file
   *
   * @return String in the format to go into the save file
   */
  public String getSaveDetails() {

    String saveString =
        username
            + ":"
            + password
            + ":"
            + wins.toString()
            + ":"
            + losses.toString()
            + ":"
            + fastestWin.toString();

    return saveString;
  }

  /**
   * This gets and formats all key information (excluding password and words) about a user to be
   * displayed
   *
   * @return String to display the key details
   */
  public String formatUserDetails() {

    String displayString =
        "Username: "
            + username
            + "\nWins: "
            + wins.toString()
            + "\nLosses: "
            + losses.toString()
            + "\nFastest win: "
            + fastestWin.toString();

    return displayString;
  }

  /**
   * @return ArrayList<String>
   */
  public ArrayList<String> getUsedWords() {
    return usedWords;
  }

  /**
   * @param word
   */
  public void addUsedWord(String word) {
    this.usedWords.add(word);
  }

  public void displayUsedWords() {
    for (int i = 0; i < usedWords.size(); i++) {
      System.out.println(usedWords.get(i));
    }
  }

  /**
   * @param usedWords
   * @return String
   */
  public String formatWordsForSave(ArrayList<String> usedWords) {
    String wordsString = "";

    for (int i = 0; i < usedWords.size(); i++) {
      wordsString = wordsString + ";" + usedWords.get(i);
    }

    wordsString = wordsString.substring(1, wordsString.length());

    return wordsString;
  }

  /**
   * @param wordsString
   */
  public void getWordsToArray(String wordsString) {

    String[] parts = wordsString.split(";");

    for (String part : parts) {
      this.usedWords.add(part);
    }

    System.out.println(usedWords);
  }

  /**
   * @param wins
   * @param losses
   * @param fastestWin
   */
  // Set user stats
  public void setStats(Integer wins, Integer losses, Integer fastestWin) {
    this.wins = wins;
    this.losses = losses;
    this.fastestWin = fastestWin;
  }

  /**
   * @return String
   */
  // Get user username
  public String getUsername() {
    return username;
  }

  /**
   * @return Integer
   */
  // Get user wins
  public Integer getWins() {
    return wins;
  }

  /**
   * @return Integer
   */
  // Get user losses
  public Integer getLosses() {
    return losses;
  }

  /**
   * @return Integer
   */
  // Get user fastest win
  public Integer getFastestWin() {
    return fastestWin;
  }
}
