package nz.ac.auckland.se206.dict;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class DictionaryLookup {

  // Get the URL for the API
  private static final String API_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/";

  /**
   * Search for information regarding a word, if it exists.
   *
   * @param query of the word we are looking for
   * @return information about the word
   * @throws IOException
   * @throws WordNotFoundException
   */
  public static WordInfo searchWordInfo(String query) throws IOException, WordNotFoundException {

    // Ready a new request for word info
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder().url(API_URL + query).build();
    Response response = client.newCall(request).execute();
    ResponseBody responseBody = response.body();

    // Get the response to a string
    String jsonString = responseBody.string();

    try {
      // Try to retrive information regarding the word
      JSONObject jsonObj = (JSONObject) new JSONTokener(jsonString).nextValue();
      String title = jsonObj.getString("title");
      String subMessage = jsonObj.getString("message");
      throw new WordNotFoundException(query, title, subMessage);
    } catch (ClassCastException e) {
      // Class cast error
      System.out.println("Class cast exception.");
    }

    // Create a new json array to store the word meanings
    JSONArray jsonArray = (JSONArray) new JSONTokener(jsonString).nextValue();
    List<WordEntry> entries = new ArrayList<WordEntry>();

    // Iterate through the array, and get the meanings
    for (int e = 0; e < jsonArray.length(); e++) {
      JSONObject jsonEntryObj = jsonArray.getJSONObject(e);
      JSONArray jsonMeanings = jsonEntryObj.getJSONArray("meanings");
      String partOfSpeech = "[not specified]";

      // Get a list of all the word definitions
      List<String> definitions = new ArrayList<String>();

      for (int m = 0; m < jsonMeanings.length(); m++) {
        // Get the object, and separate out the part of speech
        JSONObject jsonMeaningObj = jsonMeanings.getJSONObject(m);
        String pos = jsonMeaningObj.getString("partOfSpeech");

        if (!pos.isEmpty()) {
          partOfSpeech = pos;
        }

        // Store definitions in a json array
        JSONArray jsonDefinitions = jsonMeaningObj.getJSONArray("definitions");
        for (int d = 0; d < jsonDefinitions.length(); d++) {
          // Get the definition
          JSONObject jsonDefinitionObj = jsonDefinitions.getJSONObject(d);

          String definition = jsonDefinitionObj.getString("definition");
          if (!definition.isEmpty()) {
            // If no definitions already exist, add the definition
            definitions.add(definition);
          }
        }
      }

      // Add the word entry
      WordEntry wordEntry = new WordEntry(partOfSpeech, definitions);
      entries.add(wordEntry);
    }

    // Return all the word info
    return new WordInfo(query, entries);
  }
}
