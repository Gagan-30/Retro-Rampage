package com.base.game.retrorampage.MainMenu;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class About {

    public Scene createAboutScene(Scene previousScene, Stage mainStage) {
        try {
            // Load the FXML for the Difficulty Selection screen
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("About-view.fxml"));
            Parent root = fxmlLoader.load();

            // Create a new scene with the loaded FXML root
            Scene scene = new Scene(root, 640, 480); // You might want to adjust the size

            // Check if the stage was in fullscreen mode
            boolean wasFullScreen = mainStage.isFullScreen();

            mainStage.setScene(scene);

            // Reapply fullscreen mode if it was previously set
            if (wasFullScreen) {
                mainStage.setFullScreen(true);
            }

            // Set up the controller
            AboutController aboutController = fxmlLoader.getController();
            aboutController.setPreviousScene(previousScene);
            aboutController.setStage(mainStage);

            return scene;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
