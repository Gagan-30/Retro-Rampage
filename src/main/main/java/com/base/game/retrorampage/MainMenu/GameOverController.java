package com.base.game.retrorampage.MainMenu;

import com.base.game.retrorampage.GameAssets.Enemy;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class GameOverController {

    private final MainMenu mainMenu = new MainMenu();
    private final Config config = new Config("config.txt");
    @FXML
    private Label mainMenuLabel;
    @FXML
    private Label totalEnemiesLabel;
    private Scene previousScene;
    private Stage stage;
    private Scene mainMenuScene;
    private Enemy enemy; // Instance of the Enemy class

    public void setPreviousScene(Scene previousScene) {
        this.previousScene = previousScene;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setEnemy(Enemy enemy) {
        this.enemy = enemy;
        updateTotalEnemiesLabel(); // Call the update method when the enemy is set
    }

    public void initialize() {
        mainMenuLabel.setText("Level Complete");
        updateTotalEnemiesLabel();
    }

    @FXML
    public void onMainMenuButtonClick() throws IOException {
        boolean wasFullScreen = stage.isFullScreen();

        if (mainMenuScene == null) {
            mainMenuScene = mainMenu.createMainMenuScene(stage, config);
        }
        stage.setScene(mainMenuScene);

        if (wasFullScreen) {
            stage.setFullScreen(true);
        }

        updateTitle("Main Menu");
    }

    @FXML
    public void onRetryButtonClick() {
        // Implement functionality to proceed to the retry level
    }

    private void updateTitle(String newTitle) {
        if (stage != null) {
            stage.setTitle(newTitle);
        }
    }

    private void updateTotalEnemiesLabel() {
        int totalEnemiesKilled = getTotalEnemiesKilled(); // Retrieve or calculate total enemies killed
        totalEnemiesLabel.setText("Enemies Killed: " + totalEnemiesKilled);
    }

    private int getTotalEnemiesKilled() {
        return Enemy.getTotalEnemiesKilled(); // Replace with actual logic
    }

}
