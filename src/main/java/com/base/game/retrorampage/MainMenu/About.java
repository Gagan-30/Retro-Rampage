package com.base.game.retrorampage.MainMenu;

// Import necessary JavaFX classes.

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

// Defines the 'About' class.
public class About {

    /**
     * Creates and returns a new scene for the "About" screen.
     *
     * @param previousScene The scene that was displayed before switching to the "About" scene.
     * @param mainStage     The primary window (stage) in which the scene is displayed.
     * @return A new Scene object for the "About" screen, or null if an error occurs.
     */
    public Scene createAboutScene(Scene previousScene, Stage mainStage) {
        try {
            // Initialize FXMLLoader to load the "About-view.fxml" file.
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("About-view.fxml"));

            // Load the root element from the FXML file to use as the root of the new scene.
            Parent root = fxmlLoader.load();

            // Create a new scene with the loaded root element, specifying its width and height.
            Scene scene = new Scene(root, 640, 480); // Adjust the size as needed.

            // Check if the main stage was in full screen mode before changing scenes.
            boolean wasFullScreen = mainStage.isFullScreen();

            // Set the new scene to the main stage.
            mainStage.setScene(scene);

            // If the main stage was previously in full screen mode, re-enable full screen mode.
            if (wasFullScreen) {
                mainStage.setFullScreen(true);
            }

            // Obtain the controller associated with the "About" scene and configure it.
            AboutController aboutController = fxmlLoader.getController();
            aboutController.setPreviousScene(previousScene); // Pass the previous scene to the controller.
            aboutController.setStage(mainStage); // Pass the main stage to the controller.

            // Return the created scene.
            return scene;
        } catch (IOException e) { // Catch and handle any IOExceptions that occur during FXML loading.
            e.printStackTrace();
            // Return null if an exception occurs, indicating failure to create the scene.
            return null;
        }
    }
}