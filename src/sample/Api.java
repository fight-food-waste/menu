package sample;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

class Api {

    private static HttpURLConnection con;
    private String token;

    private static Api ApiInstance = new Api();

    private Api() {
    }

    static Api getInstance() {
        return ApiInstance;
    }

    void login(String email, String password) throws IOException {

        String url = "http://localhost:3000/auth";
        String urlParameters = "email=" + email + "&password=" + password;
        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);

        try {

            URL myurl = new URL(url);
            con = (HttpURLConnection) myurl.openConnection();

            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "FFW Menu client");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(postData);
            }

            StringBuilder content;

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {

                String line;
                content = new StringBuilder();

                while ((line = in.readLine()) != null) {
                    content.append(line);
                    content.append(System.lineSeparator());
                }
            }

            JsonObject tokenJson = new JsonParser().parse(content.toString()).getAsJsonObject();

            this.token = tokenJson.get("token").getAsString();

        } finally {

            con.disconnect();
        }
    }

    String getToken() {
        return token;
    }
}
