package com.base.game.retrorampage.LevelGeneration;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Level Generator");
        LevelGenerator dungeonGenerator = new LevelGenerator(5);
        primaryStage.setScene(dungeonGenerator.generateLevel());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}