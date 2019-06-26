package ffw_menu;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import kong.unirest.Unirest;

class OpenFoodFacts {

    private static String endpoint = "https://world.openfoodfacts.org/api/v0";

    static JsonObject getProductInfo(String bardcode) {
        String url = endpoint + "/product/" + bardcode + ".json";

        String productInfoRaw = Unirest.get(url).asJson().getBody().toString();

        // Parse JSON and extract product info as a JsonObject
        JsonObject productInfoJson = new JsonParser().parse(productInfoRaw).getAsJsonObject();
        return productInfoJson.getAsJsonObject("product");
    }
}
