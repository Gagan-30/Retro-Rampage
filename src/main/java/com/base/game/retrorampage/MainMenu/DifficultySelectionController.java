package com.base.game.retrorampage.MainMenu;

import javafx.fxml.FXML;

public class DifficultySelectionController {

    private SceneSwitcher sceneSwitcher;

    // Setter method to inject SceneSwitcher instance
    public void setSceneSwitcher(SceneSwitcher sceneSwitcher) {
        this.sceneSwitcher = sceneSwitcher;
    }

    @FXML
    private void onEasyButtonClick() {
        // Handle logic for selecting Easy difficulty
    }

    @FXML
    private void onMediumButtonClick() {
        // Handle logic for selecting Medium difficulty
    }

    @FXML
    private void onHardButtonClick() {
        // Handle logic for selecting Hard difficulty
    }

    @FXML
    public void onSurvivorButtonClick() {
        // Handle logic for selecting Survivor difficulty
    }

    @FXML
    public void onGroundedButtonClick() {
        // Handle logic for selecting Grounded difficulty
    }


    // Event handler for the "Return" button
    @FXML
    public void onReturnButtonClick() {
        sceneSwitcher.switchToScene("MainMenu-view.fxml", "Main Menu");
    }
}