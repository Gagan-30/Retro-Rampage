package com.base.game.retrorampage;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        MainMenu mainMenu = new MainMenu();
        mainMenu.show(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}