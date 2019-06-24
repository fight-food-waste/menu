package ffw_menu;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import kong.unirest.Unirest;

class Product {

    static JsonArray getFromStock() throws ProductApiException {

        Api api = Api.getInstance();

        String url = "http://localhost:3000/products/in-stock";

        String rawJson = Unirest.get(url).header("token", api.getToken()).asJson().getBody().toString();

        if (rawJson.isEmpty()) {
            throw new ProductApiException();
        }

        return new JsonParser().parse(rawJson).getAsJsonArray();
    }
}
