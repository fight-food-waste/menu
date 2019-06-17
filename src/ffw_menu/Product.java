package ffw_menu;

import kong.unirest.Unirest;

class Product {

    static String getFromStock() {

        Api api = Api.getInstance();

        String url = "http://localhost:3000/products/in-stock";

        return Unirest.get(url).header("token", api.getToken()).asJson().getBody().toString();
    }
}
