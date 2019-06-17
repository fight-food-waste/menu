package ffw_menu;

import kong.unirest.Unirest;

class OpenFoodFacts {

    static String getProductInfo(String bardcode) {

        Api api = Api.getInstance();

        String url = "https://world.openfoodfacts.org/api/v0/product/" + bardcode + ".json";

        return Unirest.get(url).header("token", api.getToken()).asJson().getBody().toString();
    }
}
