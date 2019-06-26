package ffw_menu;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

public class Controller {
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;
    @FXML
    private ListView<String> mealsListView;
    @FXML
    private ListView<String> dessertsListView;
    @FXML
    private ListView<Hyperlink> recipesListView;

    private Api api = Api.getInstance();

    @FXML
    public void onLoginButtonClicked(ActionEvent event) {

        try {
            api.login(emailField.getText(), passwordField.getText()); // Can throw ApiAuthException

            // Load new scene with the products and menus
            Parent menuParent = FXMLLoader.load(getClass().getResource("menu.fxml")); // Can throw IOException
            Scene menuScene = new Scene(menuParent);

            // Get the current window/stage object
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Switch scene
            currentStage.setScene(menuScene);
            currentStage.show();
        } catch (IOException | ApiAuthException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void loadMenu() {
        mealsListView.getItems().clear();
        dessertsListView.getItems().clear();

        try {
            // Get products from FFW API
            JsonArray productsJson = Product.getFromStock(); // Can throw ProductApiException

            // Init empty lists to sort the products later
            ArrayList<String> mealsList = new ArrayList<>();
            ArrayList<String> dessertsList = new ArrayList<>();

            // Extract information from JSON and OFF API for each product
            for (JsonElement product : productsJson) {
                JsonObject productObj = product.getAsJsonObject();
                String barcode = productObj.get("barcode").getAsString();
                String name = productObj.get("name").getAsString();

                // Get product metadata from OFF
                JsonObject productInfo = OpenFoodFacts.getProductInfo(barcode);

                // Extract categories for product
                JsonArray categoriesTag = productInfo.getAsJsonArray("categories_tags");
                ArrayList categoriesList = new Gson().fromJson(categoriesTag.toString(), ArrayList.class);

                // Sort products by category
                if (categoriesList.contains("en:meals")) {
                    mealsList.add(name);
                } else if (categoriesList.contains("en:desserts")) {
                    dessertsList.add(name);
                }
                // More categories can be managed. For now other products are ignored.
            }

            // Populate ListViews with categories
            mealsListView.getItems().addAll(mealsList);
            dessertsListView.getItems().addAll(dessertsList);
        } catch (ProductApiException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void loadRecipies() {
        recipesListView.getItems().clear();

        try {
            // Get products from FFW API
            JsonArray productsJson = Product.getFromStock();

            // Load recipes for each product
            for (JsonElement product : productsJson) {
                JsonObject productObj = product.getAsJsonObject();
                String name = productObj.get("name").getAsString();

                // Add it to the ListView
                addRecipes(name);
            }
        } catch (ProductApiException e) {
            System.out.println(e.getMessage());
        }
    }

    // Add recipes with this product to the ListView
    private void addRecipes(String product) {

        try {
            HashMap<String, ArrayList<String>> map = Recipe.search(product);
            // Recipes API search return 2 lists in an map, extract them
            ArrayList<String> recipesTitle = map.get("recipesTitle");
            ArrayList<String> recipesUrl = map.get("recipesUrl");

            for (int i = 0; i < recipesTitle.size(); i++) {
                final String recipeTitle = recipesTitle.get(i);
                final String recipeUrl = recipesUrl.get(i);

                // Each recipe will be an hyperlink
                Hyperlink hyperlink = new Hyperlink();
                hyperlink.setText(recipeTitle);

                // Make each hyperlink clickable
                hyperlink.setOnAction(e -> {
                    if (Desktop.isDesktopSupported()) {
                        try {
                            Desktop.getDesktop().browse(new URI(recipeUrl));
                        } catch (IOException | URISyntaxException e1) {
                            e1.printStackTrace();
                        }
                    }
                });

                // Add hyperlink to ListView
                recipesListView.getItems().add(hyperlink);
            }
        } catch (RecipeApiException e) {
            System.out.println(e.getMessage());
        }
    }
}
