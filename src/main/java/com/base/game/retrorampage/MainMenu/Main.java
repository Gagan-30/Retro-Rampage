package com.base.game.retrorampage.MainMenu;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Config config = new Config("config.txt");
        applySettingsToStage(stage, config);

        MainMenu mainMenu = new MainMenu();
        Scene mainMenuScene = mainMenu.createMainMenuScene(stage, config);

        showMainMenu(stage, mainMenuScene);
    }

    private void showMainMenu(Stage stage, Scene scene) {
        stage.setScene(scene);
        stage.show();
    }

    private static void applySettingsToStage(Stage stage, Config config) {
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