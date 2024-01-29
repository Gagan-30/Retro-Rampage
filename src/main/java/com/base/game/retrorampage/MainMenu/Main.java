package com.base.game.retrorampage.MainMenu;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Config config = new Config("config.txt"); // Ensure correct path

        System.out.println("[Main] Starting main application scene at " + java.time.LocalDateTime.now());
        MainMenu mainMenu = new MainMenu();

        // Pass the config object to createMainMenuScene
        Scene mainMenuScene = mainMenu.createMainMenuScene(stage, config);

        showMainMenu(stage, mainMenuScene);
    }

    private void showMainMenu(Stage stage, Scene scene) {
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
