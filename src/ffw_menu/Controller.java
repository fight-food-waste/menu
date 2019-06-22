package ffw_menu;

import com.google.gson.*;
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
    private ListView mealsListView;
    @FXML
    private ListView dessertsListView;
    @FXML
    private ListView recipesListView;


    private Api apiInstance = Api.getInstance();

    @FXML
    public void onLoginButtonClicked(ActionEvent event) {

        try {
            apiInstance.login(emailField.getText(), passwordField.getText());

            Parent tableViewParent = FXMLLoader.load(getClass().getResource("menu.fxml"));
            Scene tableViewScene = new Scene(tableViewParent);

            // This line gets the Stage information
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

            window.setScene(tableViewScene);
            window.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void loadMenu() {
        mealsListView.getItems().clear();
        dessertsListView.getItems().clear();

        JsonArray productsJson = Product.getFromStock();

        ArrayList<String> mealsList = new ArrayList<>();
        ArrayList<String> dessertsList = new ArrayList<>();

        // Extract information from JSON and OFF API for each product
        for (JsonElement product : productsJson) {
            JsonObject productObj = product.getAsJsonObject();
            String barcode = productObj.get("barcode").getAsString();
            String name = productObj.get("name").getAsString();

            // Get product metadata from OFF
            String productInfo = OpenFoodFacts.getProductInfo(barcode);

            // Extract categories for product
            JsonObject productInfoJson = new JsonParser().parse(productInfo).getAsJsonObject();
            JsonObject productInfoJsonProduct = productInfoJson.getAsJsonObject("product");
            JsonArray categoriesTag = productInfoJsonProduct.getAsJsonArray("categories_tags");
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
    }

    @FXML
    public void loadRecipies() {
        recipesListView.getItems().clear();

        JsonArray productsJson = Product.getFromStock();

        // Load recipes for each product
        for (JsonElement product : productsJson) {
            JsonObject productObj = product.getAsJsonObject();
            String name = productObj.get("name").getAsString();

            addRecipes(name);
        }

    }

    // Add recipes with this product to the ListView
    private void addRecipes(String product) {

        try {
            HashMap<String, ArrayList<String>> map = Recipe.search(product);

            ArrayList<String> recipesTitle = map.get("recipesTitle");
            ArrayList<String> recipesUrl = map.get("recipesUrl");

            for (int i = 0; i < recipesTitle.size(); i++) {
                final String recipeTitle = recipesTitle.get(i);
                final String recipeUrl = recipesUrl.get(i);

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

                recipesListView.getItems().add(hyperlink);
            }
        } catch (RecipeApiException e) {
            System.out.println(e.getMessage());
        }
    }
}
