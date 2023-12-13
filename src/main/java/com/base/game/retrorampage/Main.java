package com.base.game.retrorampage;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        MainMenu mainMenu = new MainMenu();
        showMainMenu(stage, mainMenu.createMainMenuScene(stage));
    }

    private void showMainMenu(Stage stage, Scene scene) {
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
