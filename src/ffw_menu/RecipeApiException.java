package ffw_menu;

class RecipeApiException extends Exception {
    RecipeApiException() {
        System.out.println("Something went wrong while querying api.bigoven.com.");
    }
}
