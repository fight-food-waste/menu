package ffw_menu;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

import java.net.HttpURLConnection;

class Api {

    private static HttpURLConnection con;
    private String token;

    private static Api ApiInstance = new Api();

    private Api() {
    }

    static Api getInstance() {
        return ApiInstance;
    }

    void login(String email, String password) {

        String url = "http://localhost:3000/auth";

        HttpResponse<JsonNode> jsonResponse = Unirest.post(url).field("email", email).field("password", password).asJson();

        if (!jsonResponse.getBody().toString().isEmpty()) {
            JsonObject tokenJson = new JsonParser().parse(jsonResponse.getBody().toString()).getAsJsonObject();
            this.token = tokenJson.get("token").getAsString();
        }
    }

    String getToken() {
        return token;
    }
}
