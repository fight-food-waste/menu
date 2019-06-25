package ffw_menu;

import com.google.gson.JsonParser;
import kong.unirest.Unirest;

class Api {

    private String token;
    private static Api instance;
    private static String endpoint = "http://localhost:3000";

    // Singleton
    private Api() {
    }

    static Api getInstance() {
        if (instance == null) {
            instance = new Api();
        }
        return instance;
    }

    void login(String email, String password) throws ApiAuthException {

        String tokenRawJson = Unirest.post(endpoint + "/auth").
                field("email", email).
                field("password", password)
                .asJson().getBody().toString();

        if (!tokenRawJson.isEmpty()) {
            // Extract token from JSON
            this.token = new JsonParser().parse(tokenRawJson).getAsJsonObject().get("token").getAsString();
        } else {
            throw new ApiAuthException();
        }
    }

    String getToken() {
        return token;
    }
}
