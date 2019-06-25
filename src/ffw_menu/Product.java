package ffw_menu;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import kong.unirest.Unirest;

class Product {

    private static String endpoint = "http://localhost:3000";

    static JsonArray getFromStock() throws ProductApiException {

        Api api = Api.getInstance();

        String rawJson = Unirest.get(endpoint + "/products/in-stock")
                .header("token", api.getToken())
                .asJson().getBody().toString();

        if (rawJson.isEmpty()) {
            throw new ProductApiException();
        } else {
            return new JsonParser().parse(rawJson).getAsJsonArray();
        }
    }
}
