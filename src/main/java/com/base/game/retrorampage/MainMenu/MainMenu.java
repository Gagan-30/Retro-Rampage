package com.base.game.retrorampage.MainMenu;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenu {

    // Method to create and return the main menu scene
    public Scene createMainMenuScene(Stage stage) throws IOException {
        // Load the FXML file for the main menu view
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainMenu-view.fxml"));
        Parent root = fxmlLoader.load();

        // Get the controller associated with the FXML file
        MainMenuController mainMenuController = fxmlLoader.getController();

        // Pass the main stage to the controller
        mainMenuController.setMainStage(stage);

        // Create a new scene with the loaded FXML as the root, with specified dimensions
        Scene scene = new Scene(root, 640, 480);

        // Add an external CSS stylesheet to the scene for styling
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        // Set properties for the main stage
        stage.setTitle("Main Menu");
        stage.setResizable(true);
        stage.setMinWidth(640);
        stage.setMinHeight(480);

        // Return the created main menu scene
        return scene;
    }
}
