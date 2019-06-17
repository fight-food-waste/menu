package ffw_menu;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

class Product {

    static String getFromStock() {

        Api api = Api.getInstance();

        String url = "http://localhost:3000/products/in-stock";

        HttpResponse<JsonNode> jsonResponse = Unirest.get(url).header("token", api.getToken()).asJson();

        return jsonResponse.getBody().toString();
    }
}
