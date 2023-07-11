# SE206 - 2022 - Beta & Final Releases

### Quick, Draw! 

A Game to improve young children's speed drawing abilities through a range of words with varying difficulty using Java (JavaFX), SceneBuilder and CSS.

## Home
![image](https://github.com/nroh555/Quick-Draw-App/assets/100507962/e23d88b6-7059-4a24-8410-d5dde0ea09b7)

## Dashboard 
The user then has the ability to create their own account and would then be able to choose their own settings in terms of accuracy, words, time and confidence. 
There are three or four levels for each categories.

![image](https://github.com/nroh555/Quick-Draw-App/assets/100507962/1b28ce11-003b-444d-92f7-2d7f3d7ab8fb)

### Accuracy
1. Easy - the player wins the game if the word to draw is in the ML’s top 3 guesses.
2. Medium - the player wins the game if the word to draw is in the ML’s top 2 guesses.
3. Hard - the player wins the game if the word to draw is the ML’s top 1 guess.

### Words
1. Easy - the game only selects those words classified as Easy (E).
2. Medium - the game only selects those words classified as Easy (E) or Medium (M).
3. Hard - the game selects any word (E, M, or H)
4. Master - the game only selects those words classified as Hard (H).

### Time
1. Easy - the player gets 60 seconds to draw the picture correctly.
2. Medium - the player gets 45 seconds to draw the picture correctly.
3. Hard - the player gets 30 seconds to draw the picture correctly.
4. Master - the player gets 15 seconds to draw the picture correctly.

### Confidence
1. Easy - the confidence level of the correct prediction should be at least 1%.
2. Medium - the confidence level of the correct prediction should be at least 10%.
3. Hard - the confidence level of the correct prediction should be at least 25%.
4. Master - the confidence level of the correct prediction should be at least 50%.

## Normal Mode 
For the normal mode, the user would be able to draw on the canvas and a machine learning model would predict the user's drawing. 

![image](https://github.com/nroh555/Quick-Draw-App/assets/100507962/4c765e0d-a8a6-4038-bc38-c905ce7724ff)

## Zen Mode
For the zen mode, the user would be able to draw with a variety of colours on the canvas with unlimited amount of time.

![image](https://github.com/nroh555/Quick-Draw-App/assets/100507962/4f65fedf-4d81-4652-8d05-ac9484b6ed26)

## Hidden Mode
For the hidden mode, the user would have to read the defintion provided and determine what the word would be and then draw it on the canvas. The user would be provided with hints if needed. 

![image](https://github.com/nroh555/Quick-Draw-App/assets/100507962/5ec42d97-5b3e-49fe-b462-e2ed8cf8960d)

## Profile
The users are also able to view their profile and collect badges based on the achievements they got while playing the game.

![image](https://github.com/nroh555/Quick-Draw-App/assets/100507962/42726282-97bb-4541-9cc9-fe94db39aa4f)

## Leaderboard 
Finally, the user would be able to compete with their friends to via a leaderboard that can be sorted by speed, wins and badges.

![image](https://github.com/nroh555/Quick-Draw-App/assets/100507962/12088eca-2de1-4de1-ba0f-de199cf9cb7f)

**Requirements**

- Java JDK 17.0.2 (download
  it [https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) )
  and JAVA_HOME env variable properly set
- Scene Builder (download it
  here [https://gluonhq.com/products/scene-builder/#download](https://gluonhq.com/products/scene-builder/#download))


**What to do first?**

Make sure that the provided tests pass.

Unix/MacOsX:  
`./mvnw clean test`

Windows:  
`.\mvnw.cmd clean test`

This will also install the GIT pre-commit hooks to activate the auto-formatting at every GIT commit.

**How to run the game?**

Unix/MacOsX:  
`./mvnw clean javafx:run`

Windows:  
`.\mvnw.cmd clean javafx:run`

**How to format the Java code?**

You can format the code at any time by running the command:

Unix/MacOsX:  
`./mvnw git-code-format:format-code `

Windows:  
`.\mvnw.cmd git-code-format:format-code `
