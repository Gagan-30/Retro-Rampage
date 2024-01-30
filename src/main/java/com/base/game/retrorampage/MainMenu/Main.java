package com.base.game.retrorampage.MainMenu;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private Stage primaryStage;
    private SceneSwitcher sceneSwitcher;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Config config = new Config("config.txt");
        SceneSwitcher sceneSwitcher = new SceneSwitcher(primaryStage, config);

        applySettings(primaryStage, config);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu-view.fxml"));
        Parent root = loader.load();
        MainMenuController controller = loader.getController();
        controller.setSceneSwitcher(sceneSwitcher);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    private void applySettings(Stage stage, Config config) {
        // Read and apply resolution and fullscreen settings from config
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