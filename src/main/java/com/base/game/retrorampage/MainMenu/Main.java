package com.base.game.retrorampage.MainMenu;

import javafx.application.Application;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        SceneSwitcher sceneSwitcher = new SceneSwitcher(primaryStage);
        sceneSwitcher.switchToScene("MainMenu.fxml");
    }

    private void applySettingsToStage(Stage stage, Config config) {
        String resolution = config.loadResolutionSetting();
        String[] parts = resolution.split(" x ");
        if (parts.length == 2) {
            stage.setWidth(Integer.parseInt(parts[0]));
            stage.setHeight(Integer.parseInt(parts[1]));
        }
        stage.setFullScreenExitHint("");
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setFullScreen(config.loadFullscreenSetting());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
