package com.base.game.retrorampage.MainMenu;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainMenu {

    public Scene createMainMenuScene(Stage stage, Config config) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainMenu-view.fxml"));
        Parent root = fxmlLoader.load();

        MainMenuController mainMenuController = fxmlLoader.getController();
        mainMenuController.setMainStage(stage);

        // Apply settings from config
        applySettingsFromConfig(stage, config);

        Scene scene = new Scene(root);
        stage.setTitle("Main Menu");
        stage.setResizable(true);

        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());
        return scene;
    }

    private void applySettingsFromConfig(Stage stage, Config config) {
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
}
