package ffw_menu;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

public class OpenFoodFacts {

    public static String getProductInfo(String bardcode) {

        Api api = Api.getInstance();

        String url = "https://world.openfoodfacts.org/api/v0/product/" + bardcode + ".json";

        HttpResponse<JsonNode> jsonResponse = Unirest.get(url).header("token", api.getToken()).asJson();

        return jsonResponse.getBody().toString();

    }
}
