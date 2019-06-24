package ffw_menu;

class ProductApiException extends Exception {
    ProductApiException() {
        System.out.println("Something went wrong while gettings products from the FFW API.");
    }
}
