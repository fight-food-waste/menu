package ffw_menu;

import com.google.gson.JsonParser;
import kong.unirest.Unirest;

class Api {

    private String token;
    private static Api ApiInstance = new Api();

    private Api() {
    }

    static Api getInstance() {
        return ApiInstance;
    }

    void login(String email, String password) {

        String url = "http://localhost:3000/auth";

        String tokenRawJson = Unirest.post(url).field("email", email).field("password", password).asJson().getBody().toString();

        if (!tokenRawJson.isEmpty()) {
            this.token = new JsonParser().parse(tokenRawJson).getAsJsonObject().get("token").getAsString();
        }
    }

    String getToken() {
        return token;
    }
}
