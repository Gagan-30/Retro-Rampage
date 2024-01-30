package com.base.game.retrorampage.MainMenu;

import javafx.fxml.FXML;

public class MainMenuController {

    private SceneSwitcher sceneSwitcher;

    // Setter method to inject SceneSwitcher instance
    public void setSceneSwitcher(SceneSwitcher sceneSwitcher) {
        this.sceneSwitcher = sceneSwitcher;
    }

    @FXML
    protected void onStartGameButtonClick() {
        sceneSwitcher.switchToScene("DifficultySelection-view.fxml", "Select Difficulty");
    }

    @FXML
    protected void onLoadGameButtonClick() {
        sceneSwitcher.switchToScene("LoadGame-view.fxml", "Select Game Save");
    }

    @FXML
    protected void onSettingsButtonClick() {
        sceneSwitcher.switchToScene("Settings-view.fxml", "Settings");
    }

    @FXML
    protected void onAboutButtonClick() {
        sceneSwitcher.switchToScene("About-view.fxml", "About");
    }

    @FXML
    protected void onExitButtonClick() {
        sceneSwitcher.switchToScene("Exit-view.fxml", "Exit");
    }
}
