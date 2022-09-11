package nz.ac.auckland.se206.profile;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
  private String username;
  private String password;
  private Integer wins;
  private Integer losses;
  private Integer fastestWin;
  private List<String> usedWords;

  public User(String username, String password) {
    this.username = username;
    this.password = password;
    this.wins = 0;
    this.losses = 0;
    this.fastestWin = 0;
  }

  // This loads user details from the file to a user
  public void loadUser(
      String username, String password, Integer wins, Integer losses, Integer fastestWin) {
    this.username = username;
    this.password = password;
    this.wins = wins;
    this.losses = losses;
    this.fastestWin = fastestWin;
  }

  // This formats all information about the user so it can be saved to a file
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

  // This gets and formats all information (excluding password) about a user to be
  // displayed
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

  // Set user stats
  public void setStats(Integer wins, Integer losses, Integer fastestWin) {
    this.wins = wins;
    this.losses = losses;
    this.fastestWin = fastestWin;
  }

  // Get user username
  public String getUsername() {
    return username;
  }

  // Get user wins
  public Integer getWins() {
    return wins;
  }

  // Get user losses
  public Integer getLosses() {
    return losses;
  }

  // Get user fastest win
  public Integer getFastestWin() {
    return fastestWin;
  }
}
