package com.base.game.retrorampage;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label MainMenu;

    @FXML
    protected void onButtonClick() {
        MainMenu.setText("Welcome to JavaFX Application!");
    }
}