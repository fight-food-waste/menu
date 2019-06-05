package sample;

import javafx.fxml.FXML;

import javafx.scene.control.TextField;

public class Controller {
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;

    @FXML
    public void onLoginButtonClicked() {
        System.out.println("username: " + usernameField.getText());
        System.out.println("password: " + passwordField.getText());
    }
}
