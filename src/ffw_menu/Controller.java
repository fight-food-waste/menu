package ffw_menu;

import com.google.gson.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class Controller {
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;

    private Api apiInstance = Api.getInstance();

    @FXML
    public void onLoginButtonClicked(ActionEvent event) {

        try {
            apiInstance.login(emailField.getText(), passwordField.getText());

            Parent tableViewParent = FXMLLoader.load(getClass().getResource("menu.fxml"));
            Scene tableViewScene = new Scene(tableViewParent);

            //This line gets the Stage information
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

            window.setScene(tableViewScene);
            window.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void loadMenu() {
        try {
            String products = Product.getFromStock(apiInstance.getToken());

            System.out.println(products);

            JsonArray productsJson = new JsonParser().parse(products).getAsJsonArray();

            for (JsonElement product : productsJson) {
                JsonObject productObj = product.getAsJsonObject();
                String barcode = productObj.get("barcode").getAsString();
                String productInfo = OpenFoodFacts.getProductInfo(barcode);
                JsonObject productInfoJson = new JsonParser().parse(productInfo).getAsJsonObject();
                JsonObject productInfoJsonProduct = productInfoJson.getAsJsonObject("product");
                JsonArray categoriesTag = productInfoJsonProduct.getAsJsonArray("categories_tags");
                Gson gson = new Gson();
                ArrayList categoriesList = gson.fromJson(categoriesTag.toString(), ArrayList.class);

                if (categoriesList.contains("en:meals")) {
                    System.out.println("It's a meal");
                } else if (categoriesList.contains("en:desserts")) {
                    System.out.println("It's a dessert");
                }
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
