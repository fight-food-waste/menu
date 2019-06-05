package sample;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import java.io.IOException;

public class Controller {
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;

    @FXML
    public void onLoginButtonClicked() {
        System.out.println("email: " + emailField.getText());
        System.out.println("password: " + passwordField.getText());

        try {
            String tokenJsonString = JavaPostRequest.login(emailField.getText(), passwordField.getText());

            JsonObject tokenJson = new JsonParser().parse(tokenJsonString).getAsJsonObject();

            String token = tokenJson.get("token").getAsString();

            System.out.println(token);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
}
