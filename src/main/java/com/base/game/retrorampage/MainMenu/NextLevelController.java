package com.base.game.retrorampage.MainMenu;

import com.base.game.retrorampage.GameAssets.Enemy;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class NextLevelController {

    @FXML
    private Label mainMenuLabel;

    @FXML
    private Label totalEnemiesLabel;

    @FXML
    private Button mainMenuButton;

    @FXML
    private Button nextLevelButton;

    private Scene previousScene;
    private Stage stage;
    private Scene mainMenuScene;
    private Enemy enemy; // Instance of the Enemy class
    private final MainMenu mainMenu = new MainMenu();
    private final Config config = new Config("config.txt");

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
    public void onNextLevelButtonClick() {
        // Implement functionality to proceed to the next level
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
        return 10; // Replace with actual logic
    }

}
