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
    // Stores the user's details into corresponding variables
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
    // Creates string of the users details
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
    // Creates string of the key user details
    String displayString =
        "Wins: "
            + wins.toString()
            + "\nLosses: "
            + losses.toString()
            + "\nFastest win: "
            + fastestWin.toString()
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
   * Gets the user's password
   *
   * @return String
   */
  public String getPassword() {
    return password;
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
}
