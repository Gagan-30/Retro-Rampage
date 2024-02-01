package com.base.game.retrorampage.MainMenu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

// Controller class for handling keybind configurations in a JavaFX application.
public class KeybindController {
    // Fields for storing the previous scene, the application's main stage, and the currently selected button for keybinding.
    private Scene previousScene;
    private Stage stage;
    private Button activeButton;
    // Flags to indicate whether the application is ready to capture a key/mouse click and whether input listeners have been set up.
    private boolean readyToCaptureClick = false;
    private boolean inputListenersSetup = false;

    // FXML-annotated buttons for various game actions.
    @FXML
    private Button moveUp1Button, moveUp2Button, moveDown1Button, moveDown2Button;
    @FXML
    private Button moveLeft1Button, moveLeft2Button, moveRight1Button, moveRight2Button;
    @FXML
    private Button shoot1Button, shoot2Button, aim1Button, aim2Button, reload1Button, reload2Button;
    @FXML
    private Button inventory1Button, inventory2Button;

    // A map to associate each button with a specific action string.
    private final Map<Button, String> buttonToActionMap = new HashMap<>();
    // Configuration object for managing keybinds, assumed to read from a "config.txt" file.
    private final Config config;

    // Constructor initializes the configuration object.
    public KeybindController() {
        config = new Config("config.txt");
    }

    // Initial setup method to map buttons to actions and load keybinds from the configuration.
    public void initialize() {
        mapButtonsToActions();
        loadKeybindsIntoScene();
    }

    // Maps each button to its corresponding action string.
    private void mapButtonsToActions() {
        // Each put() call associates a button with an action identifier.
        buttonToActionMap.put(moveUp1Button, "MoveUp1");
        buttonToActionMap.put(moveUp2Button, "MoveUp2");
        buttonToActionMap.put(moveDown1Button, "MoveDown1");
        buttonToActionMap.put(moveDown2Button, "MoveDown2");
        buttonToActionMap.put(moveLeft1Button, "MoveLeft1");
        buttonToActionMap.put(moveLeft2Button, "MoveLeft2");
        buttonToActionMap.put(moveRight1Button, "MoveRight1");
        buttonToActionMap.put(moveRight2Button, "MoveRight2");
        buttonToActionMap.put(shoot1Button, "Shoot1");
        buttonToActionMap.put(shoot2Button, "Shoot2");
        buttonToActionMap.put(aim1Button, "Aim1");
        buttonToActionMap.put(aim2Button, "Aim2");
        buttonToActionMap.put(reload1Button, "Reload1");
        buttonToActionMap.put(reload2Button, "Reload2");
        buttonToActionMap.put(inventory1Button, "Inventory1");
        buttonToActionMap.put(inventory2Button, "Inventory2");
    }

    // Loads the current keybinds from the configuration into the scene by setting each button's text.
    private void loadKeybindsIntoScene() {
        buttonToActionMap.forEach((button, action) -> button.setText(config.getKeybind(action)));
    }

    // Event handler for button clicks, preparing to listen for a new keybind.
    @FXML
    public void onActionButtonClick(ActionEvent event) {
        if (event.getSource() instanceof Button clickedButton) {
            prepareForNewKeybind(clickedButton);
        }
    }

    // Prepares the UI for capturing a new keybind and sets up input listeners if not already done.
    private void prepareForNewKeybind(Button clickedButton) {
        // Reverts any previously active button to its original text before setting up a new active button.
        if (activeButton != null && !activeButton.equals(clickedButton)) {
            revertButtonToOriginalText(activeButton);
        }
        setupInputListeners(); // This method is mentioned but not implemented in the provided code.
        activeButton = clickedButton;
        activeButton.setText("Listening"); // Visual feedback that the application is ready to capture a new keybind.
        delayReadyToCaptureClick(); // Delays setting the flag to capture clicks, preventing immediate capture.
    }

    // Introduces a short delay before the application is ready to capture a keybind or mouse click.
    private void delayReadyToCaptureClick() {
        new Thread(() -> {
            try {
                Thread.sleep(200); // Waits for 200 milliseconds.
                javafx.application.Platform.runLater(() -> readyToCaptureClick = true);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // Reverts a button's text to display its current keybind after attempting to set a new one.
    private void revertButtonToOriginalText(Button button) {
        String action = buttonToActionMap.get(button);
        if (action != null) {
            button.setText(config.getKeybind(action));
        }
    }

    // Sets up key and mouse event filters on the stage if they haven't been set up already.
    private void setupInputListeners() {
        if (!inputListenersSetup) {
            // Adds a key press filter to the entire stage to capture key events.
            stage.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPress);
            // Adds a mouse click filter to capture mouse click events.
            stage.addEventFilter(MouseEvent.MOUSE_CLICKED, this::handleMouseClick);
            // Ensures input listeners are only set up once.
            inputListenersSetup = true;
        }
    }

    // Handles key press events while waiting for a new keybind.
    private void handleKeyPress(KeyEvent event) {
        if (activeButton != null && "Listening".equals(activeButton.getText())) {
            if (event.getCode() == KeyCode.DELETE) {
                // Special handling for the Delete key to remove the current keybind.
                removeKeybindForCurrentAction();
            } else if (readyToCaptureClick) {
                // Captures the pressed key and processes it as a new keybind.
                String keyName = event.getCode().toString();
                processInput(keyName);
                // Consumes the event to prevent further handling.
                event.consume();
            }
        }
    }

    // Removes the keybind associated with the currently active button.
    private void removeKeybindForCurrentAction() {
        String action = buttonToActionMap.get(activeButton);
        if (action != null) {
            // Removes the keybind from the configuration.
            config.removeKeybind(action);
            // Updates the button's text to indicate no keybind.
            activeButton.setText("None");
        }
        // Resets the state to not ready to capture clicks and clears the active button.
        readyToCaptureClick = false;
        activeButton = null;
    }

    // Handles mouse click events, excluding clicks on the active button itself.
    private void handleMouseClick(MouseEvent event) {
        if (readyToCaptureClick && isListening() && event.getSource() != activeButton) {
            // Processes the mouse click as input if it's not on the active button.
            processInput(getClickType(event.getButton()));
        }
    }

    // Checks if the controller is currently in a state to listen for new keybinds.
    private boolean isListening() {
        return activeButton != null && "Listening".equals(activeButton.getText());
    }

    // Determines the type of mouse click based on the button pressed.
    private String getClickType(MouseButton button) {
        return switch (button) {
            case PRIMARY -> "Left Click";
            case SECONDARY -> "Right Click";
            case MIDDLE -> "Middle Click";
            default -> "Other Click";
        };
    }

    // Processes the input (key press or mouse click) as a new keybind.
    private void processInput(String input) {
        String currentAction = buttonToActionMap.get(activeButton);
        // Checks for duplicate keybinds and handles them accordingly.
        if (config.isDuplicateKeybind(input, currentAction)) {
            System.out.println("This key is already in use. Please choose another.");
            revertButtonToOriginalText(activeButton);
        } else {
            // Updates the configuration with the new keybind and updates the button text.
            config.removeKeybindIfAssigned(input);
            config.saveKeybind(currentAction, input);
            activeButton.setText(input);
        }
        // Resets the state and clears the active button after processing the input.
        readyToCaptureClick = false;
        activeButton = null;
    }

    // Handles the action of returning to the previous scene, saving settings before doing so.
    @FXML
    public void onReturnButtonClick() {
        // Saves the current settings to a file.
        config.saveSettingsToFile();

        // Checks and preserves the fullscreen mode of the stage.
        boolean wasFullScreen = stage.isFullScreen();
        if (previousScene != null && stage != null) {
            // Sets the stage's scene back to the previous one.
            stage.setScene(previousScene);
            if (wasFullScreen) {
                // Re-enables fullscreen mode if it was previously enabled.
                stage.setFullScreen(true);
            }

            // Optionally updates the stage title, indicating a context change.
            updateTitle("Settings");
        }
    }

    // Sets the previous scene, allowing for a return navigation.
    public void setPreviousScene(Scene previousScene) {
        this.previousScene = previousScene;
    }

    // Sets the main application stage, used for scene transitions and event filtering.
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // Updates the stage title, typically to reflect the current application state or context.
    private void updateTitle(String newTitle) {
        if (stage != null) {
            stage.setTitle(newTitle);
        }
    }
}