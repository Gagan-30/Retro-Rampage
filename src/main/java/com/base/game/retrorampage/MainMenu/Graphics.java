// Package declaration, indicating the class's location within the project structure.
package com.base.game.retrorampage.MainMenu;

// Importing necessary JavaFX classes for scene management and exception handling.
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

// Defines the Graphics class.
public class Graphics {

    /**
     * Creates and returns a Scene for the graphics settings screen.
     * @param previousScene The scene that was displayed before transitioning to this graphics settings screen.
     * @param mainStage The primary window (stage) where the scene is to be displayed.
     * @return A new Scene object for the graphics settings screen, or null if an error occurs.
     */
    public Scene createGraphicsScene(Scene previousScene, Stage mainStage) {
        try {
            // Initializes an FXMLLoader with the path to the "Graphics-view.fxml" file.
            // This FXML file defines the layout of the graphics settings screen.
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Graphics-view.fxml"));
            Parent root = fxmlLoader.load(); // Loads the FXML file and returns the root node.

            // Creates a new Scene using the root node from the FXML file.
            // The scene is set to a predefined size, which can be adjusted if necessary.
            Scene scene = new Scene(root, 640, 480);

            // Checks if the main stage was previously in fullscreen mode.
            boolean wasFullScreen = mainStage.isFullScreen();

            // Sets the newly created scene to the main stage.
            mainStage.setScene(scene);

            // If the stage was previously in fullscreen mode, re-enables fullscreen mode.
            if (wasFullScreen) {
                mainStage.setFullScreen(true);
            }

            // Retrieves the controller associated with the FXML file loaded.
            // This controller will manage user interactions within the graphics settings screen.
            GraphicsController graphicsController = fxmlLoader.getController();
            // Passes references of the previous scene and the main stage to the controller.
            // This allows the controller to perform actions like returning to the previous scene.
            graphicsController.setPreviousScene(previousScene);
            graphicsController.setStage(mainStage);

            // Returns the newly created scene, ready to be displayed.
            return scene;
        } catch (IOException e) {
            // Catches any IOExceptions that occur during the loading of the FXML file.
            e.printStackTrace(); // Prints the stack trace of the exception to the console.
            return null; // Returns null to indicate that an error occurred and the scene could not be created.
        }
    }
}