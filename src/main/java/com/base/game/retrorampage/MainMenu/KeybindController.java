package com.base.game.retrorampage.MainMenu;


import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class KeybindController {
    private Scene previousScene;
    private Stage stage; // Add a stage variable

    @FXML
    private VBox mainMenu;

    // Method to set the previous scene
    public void setPreviousScene(Scene previousScene) {
        this.previousScene = previousScene;
    }

    // Method to set the main stage
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // Method to update the title of the main stage
    private void updateTitle(String newTitle) {
        if (stage != null) {
            stage.setTitle(newTitle);
        }
    }

    @FXML
    private void initialize() {
        for (Node node : mainMenu.getChildren()) {
            if (node instanceof GridPane) {
                GridPane grid = (GridPane) node;
                for (Node gridNode : grid.getChildren()) {
                    if (gridNode instanceof TextField) {
                        TextField textField = (TextField) gridNode;
                        textField.setOnKeyPressed(this::handleKeyPress);
                        textField.setOnMouseClicked(this::handleMouseClick);
                    }
                }
            }
        }
    }

    private void handleKeyPress(KeyEvent event) {
        // Handle key press event
        System.out.println("Key Pressed in TextField: " + ((TextField) event.getSource()).getId());
    }

    private void handleMouseClick(MouseEvent event) {
        // Handle mouse click event
        System.out.println("Mouse Clicked in TextField: " + ((TextField) event.getSource()).getId());
    }

    // Event handler for the "Return" button
    @FXML
    public void onReturnButtonClick() {
        // Print the previous scene and stage for debugging purposes
        System.out.println("Previous Scene: " + previousScene);
        System.out.println("Stage: " + stage);

        // Check if both previous scene and stage are not null
        if (previousScene != null && stage != null) {
            // Set the main stage's scene back to the previous scene
            stage.setScene(previousScene);
            updateTitle("Main Menu");
        }
    }
}
