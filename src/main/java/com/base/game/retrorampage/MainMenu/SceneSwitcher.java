package com.base.game.retrorampage.MainMenu;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class SceneSwitcher {

    private Stage stage;

    public SceneSwitcher(Stage stage) {
        this.stage = stage;
    }

    public void switchToScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately
        }
    }

    // Optionally, you can include methods to adjust the stage properties
    public void setFullScreen(boolean fullScreen) {
        stage.setFullScreen(fullScreen);
    }

    public void setResolution(int width, int height) {
        stage.setWidth(width);
        stage.setHeight(height);
    }
}

