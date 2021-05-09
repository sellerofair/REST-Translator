package sellerofair.Translator;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class YandexAPIManager {

    private final String iamName = "\"iamToken\": \"";
    private final String textName = "\"text\": \"";
    private final String iamKey;
    private final String folderID;

    private String iamRequest(HttpURLConnection iamGetter, String params) throws IOException {

        iamGetter.setRequestProperty("Content-Type", "application/json; utf-8");
        iamGetter.setRequestProperty("Accept", "application/json");
        iamGetter.setRequestMethod("POST");
        iamGetter.setDoOutput(true);
        OutputStream os = iamGetter.getOutputStream();

        byte[] output = params.getBytes(StandardCharsets.UTF_8);
        os.write(output, 0, output.length);
        os.close();

        BufferedReader br = new BufferedReader(new InputStreamReader(iamGetter.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder response = new StringBuilder();
        String responseLine;
        while ((responseLine = br.readLine()) != null) {
            response.append(responseLine.trim());
        }
        br.close();

        return response.toString();
    }

    YandexAPIManager(String token, String folderID) throws IOException {

        URL url = new URL("https://iam.api.cloud.yandex.net/iam/v1/tokens");
        HttpURLConnection iamGetter = (HttpURLConnection) url.openConnection();

        String jsonOutputString = "{\"yandexPassportOauthToken\": \"" + token + "\"}";

        String responseString = iamRequest(iamGetter, jsonOutputString);

        int iamStartIndex = responseString.indexOf(iamName) + iamName.length();
        int iamEndIndex = responseString.indexOf('\"', iamStartIndex);

        this.folderID = folderID;
        iamKey = responseString.substring(iamStartIndex, iamEndIndex);

        iamGetter.disconnect();
    }

    private String jsonOutputCreate (String from, String to, String text) {

        StringBuilder jsonOutput = new StringBuilder();

        jsonOutput.append("{\"sourceLanguageCode\": \"")
                .append(from)
                .append("\", \"targetLanguageCode\": \"")
                .append(to)
                .append("\", \"texts\": ");

        String[] words = text.split("\\s");

        jsonOutput.append("[\"");
        for (int i = 0; i < words.length - 1; i++) {
            jsonOutput.append(words[i])
                    .append("\", \"");
        }
        jsonOutput.append(words[words.length - 1])
                .append("\"]");

        jsonOutput.append(", \"folderId\": \"")
                .append(folderID)
                .append("\"}");

        return jsonOutput.toString();
    }

    String translate(String from, String to, String text) throws IOException {

        URL url = new URL("https://translate.api.cloud.yandex.net/translate/v2/translate");

        HttpURLConnection translationGetter = (HttpURLConnection) url.openConnection();
        translationGetter.setRequestProperty("Content-Type", "application/json; utf-8");
        translationGetter.setRequestProperty("Accept", "application/json");
        translationGetter.setRequestMethod("POST");
        translationGetter.setRequestProperty("Authorization", " Bearer " + iamKey);
        translationGetter.setDoOutput(true);
        OutputStream os = translationGetter.getOutputStream();
        String jsonOutputString = jsonOutputCreate(from, to, text);

        byte[] output = jsonOutputString.getBytes(StandardCharsets.UTF_8);
        os.write(output, 0, output.length);
        os.close();

        BufferedReader br = new BufferedReader(new InputStreamReader(translationGetter.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder result = new StringBuilder();
        StringBuilder response = new StringBuilder();
        String responseLine;
        while ((responseLine = br.readLine()) != null) {
            response.append(responseLine.trim());
        }
        br.close();

        int i = response.indexOf(textName);

        while (i > -1) {
            i += textName.length();
            int j = response.indexOf("\"", i);
            result.append(response.substring(i, j))
            .append(' ');
            i = response.indexOf(textName, j);
        }

        return result.toString().trim();
    }
}