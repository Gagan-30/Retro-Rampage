package com.base.game.retrorampage;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoadGame {

    public Scene createLoadGameScene(Scene previousScene, Stage mainStage) {
        try {
            // Load the LoadGame-view.fxml file using FXMLLoader
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LoadGame-view.fxml"));
            Parent root = fxmlLoader.load();

            // Create a new stage for the load game scene
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 640, 480));
            stage.setResizable(true);
            stage.setMinWidth(640);
            stage.setMinHeight(480);

            // Get the controller associated with the LoadGame-view.fxml
            LoadGameController loadGameController = fxmlLoader.getController();

            // Set the previous scene in the LoadGameController
            loadGameController.setPreviousScene(previousScene);

            // Set the main stage in the LoadGameController
            loadGameController.setStage(mainStage);

            // Return the scene associated with the new stage
            return stage.getScene();
        } catch (IOException e) {
            // Handle IOException by printing the stack trace
            e.printStackTrace();
            return null;
        }
    }
}