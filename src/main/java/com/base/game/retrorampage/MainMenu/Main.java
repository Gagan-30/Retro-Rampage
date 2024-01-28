package com.base.game.retrorampage.MainMenu;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Create an instance of the MainMenu class
        System.out.println("[Main] Starting main application scene at " + java.time.LocalDateTime.now());

        MainMenu mainMenu = new MainMenu();

        // Call createMainMenuScene to initialize the main menu scene
        Scene mainMenuScene = mainMenu.createMainMenuScene(stage);

        // Show the main menu scene on the provided stage
        showMainMenu(stage, mainMenuScene);
    }

    // Method to set the provided scene on the given stage and display it
    private void showMainMenu(Stage stage, Scene scene) {
        stage.setScene(scene);
        stage.show();
    }

    // The main method to launch the JavaFX application
    public static void main(String[] args) {
        launch(args);
    }
}