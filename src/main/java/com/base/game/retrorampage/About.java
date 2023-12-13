package com.base.game.retrorampage;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class About {

    // Method to create the about scene
    public Scene createAboutScene(Scene previousScene, Stage mainStage) {
        try {
            // Load the About-view.fxml file using FXMLLoader
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("About-view.fxml"));
            Parent root = fxmlLoader.load();

            // Create a new stage for the about scene
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 640, 480));
            stage.setResizable(true);
            stage.setMinWidth(640);
            stage.setMinHeight(480);

            // Get the controller associated with the About-view.fxml
            AboutController aboutController = fxmlLoader.getController();

            // Set the previous scene in the AboutController
            aboutController.setPreviousScene(previousScene);

            // Set the main stage in the AboutController
            aboutController.setStage(mainStage);

            // Return the scene associated with the new stage
            return stage.getScene();
        } catch (IOException e) {
            // Handle IOException by printing the stack trace
            e.printStackTrace();
            return null;
        }
    }
}
