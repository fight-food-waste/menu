package ffw_menu;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import kong.unirest.Unirest;

class Product {

    static JsonArray getFromStock() {

        Api api = Api.getInstance();

        String url = "http://localhost:3000/products/in-stock";

        String rawJson = Unirest.get(url).header("token", api.getToken()).asJson().getBody().toString();

        return new JsonParser().parse(rawJson).getAsJsonArray();
    }
}
