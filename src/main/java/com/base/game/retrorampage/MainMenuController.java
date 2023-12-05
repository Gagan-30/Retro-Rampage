package com.base.game.retrorampage;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MainMenuController {

    @FXML
    private Label mainMenuLabel;

    @FXML
    private Button startGameButton;

    @FXML
    private Button loadGameButton;

    @FXML
    private Button settingsButton;

    @FXML
    private Button aboutButton;

    @FXML
    private Button exitButton;

    public void initialize() {
        // You can perform any initialization logic here
    }

    @FXML
    protected void onStartGameButtonClick() {
        // Add logic to switch to the game scene
    }

    @FXML
    protected void onLoadGameButtonClick() {
        // Add logic to switch to the settings scene
    }

    @FXML
    protected void onSettingsButtonClick() {
        // Add logic to switch to the settings scene
    }

    @FXML
    protected void onAboutButtonClick() {
        // Add logic to show information about the game
    }

    @FXML
    protected void onExitButtonClick() {
        // Add logic to exit the game
    }
}
