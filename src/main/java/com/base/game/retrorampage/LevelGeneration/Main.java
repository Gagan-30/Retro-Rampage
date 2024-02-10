package com.base.game.retrorampage.LevelGeneration;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Set the title of the primary stage
        primaryStage.setTitle("Level Generator");

        // Initialize the LevelGenerator with the desired number of cells
        LevelGenerator levelGenerator = new LevelGenerator(7);

        // Generate the level and obtain the Scene object
        Scene scene = levelGenerator.generateLevel();

        // Set the scene to the primary stage and show it
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }
}