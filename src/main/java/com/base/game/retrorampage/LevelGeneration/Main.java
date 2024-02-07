package com.base.game.retrorampage.LevelGeneration;

import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Level Generator");
        LevelGenerator levelGenerator = new LevelGenerator(5);
        primaryStage.setScene(levelGenerator.generateLevel());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}