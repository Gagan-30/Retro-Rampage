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

public class KeybindController {
    private Scene previousScene;
    private Stage stage;
    private Button activeButton;
    private boolean readyToCaptureClick = false;
    private boolean inputListenersSetup = false;

    @FXML
    private Button moveUp1Button, moveUp2Button, moveDown1Button, moveDown2Button;
    @FXML
    private Button moveLeft1Button, moveLeft2Button, moveRight1Button, moveRight2Button;
    @FXML
    private Button shoot1Button, shoot2Button, aim1Button, aim2Button, reload1Button, reload2Button;
    @FXML
    private Button inventory1Button, inventory2Button;

    private final Map<Button, String> buttonToActionMap = new HashMap<>();
    private final Config config;

    public KeybindController() {
        config = new Config("config.txt");
    }

    public void initialize() {
        mapButtonsToActions();
        loadKeybindsIntoScene();
    }

    private void mapButtonsToActions() {
        buttonToActionMap.put(moveUp1Button, "MoveUp1");
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

    private void loadKeybindsIntoScene() {
        buttonToActionMap.forEach((button, action) -> button.setText(config.getKeybind(action)));
    }

    @FXML
    public void onActionButtonClick(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            prepareForNewKeybind(clickedButton);
        }
    }

    private void prepareForNewKeybind(Button clickedButton) {
        if (activeButton != null && !activeButton.equals(clickedButton)) {
            revertButtonToOriginalText(activeButton);
        }
        setupInputListeners();
        activeButton = clickedButton;
        activeButton.setText("Listening");
        delayReadyToCaptureClick();
    }

    private void delayReadyToCaptureClick() {
        new Thread(() -> {
            try {
                Thread.sleep(200); // Delay for 200 milliseconds
                javafx.application.Platform.runLater(() -> readyToCaptureClick = true);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void revertButtonToOriginalText(Button button) {
        String action = buttonToActionMap.get(button);
        if (action != null) {
            button.setText(config.getKeybind(action));
        }
    }

    private void setupInputListeners() {
        if (!inputListenersSetup) {
            stage.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPress);
            stage.addEventFilter(MouseEvent.MOUSE_CLICKED, this::handleMouseClick);
            inputListenersSetup = true;
        }
    }

    private void handleKeyPress(KeyEvent event) {
        if (activeButton != null && "Listening".equals(activeButton.getText())) {
            if (event.getCode() == KeyCode.DELETE) {
                // If Delete is pressed, remove the keybind for this action
                removeKeybindForCurrentAction();
            } else if (readyToCaptureClick) {
                // Otherwise, process other keys as before
                String keyName = event.getCode().toString();
                processInput(keyName);
                event.consume();
            }
        }
    }

    private void removeKeybindForCurrentAction() {
        String action = buttonToActionMap.get(activeButton);
        if (action != null) {
            config.removeKeybind(action);
            activeButton.setText("None"); // Or any default text you prefer
        }
        readyToCaptureClick = false;
        activeButton = null;
    }


    private void handleMouseClick(MouseEvent event) {
        if (readyToCaptureClick && isListening() && event.getSource() != activeButton) {
            processInput(getClickType(event.getButton()));
        }
    }

    private boolean isListening() {
        return activeButton != null && "Listening".equals(activeButton.getText());
    }

    private String getClickType(MouseButton button) {
        return switch (button) {
            case PRIMARY -> "Left Click";
            case SECONDARY -> "Right Click";
            case MIDDLE -> "Middle Click";
            default -> "Other Click";
        };
    }

    private void processInput(String input) {
        String currentAction = buttonToActionMap.get(activeButton);

        if (config.isDuplicateKeybind(input, currentAction)) {
            System.out.println("This key is already in use. Please choose another.");
            revertButtonToOriginalText(activeButton);
        } else {
            config.removeKeybindIfAssigned(input);
            config.saveKeybind(currentAction, input);
            activeButton.setText(input);
        }

        readyToCaptureClick = false;
        activeButton = null;
    }

    @FXML
    public void onReturnButtonClick() {
        config.saveSettingsToFile();

        // Check if the stage is in fullscreen mode
        boolean wasFullScreen = stage.isFullScreen();

        // Set the main stage's scene back to the previous scene
        if (previousScene != null && stage != null) {
            stage.setScene(previousScene);

            // Re-enable fullscreen if it was previously set
            if (wasFullScreen) {
                stage.setFullScreen(true);
            }

            updateTitle("Settings");
        }
    }

    public void setPreviousScene(Scene previousScene) {
        this.previousScene = previousScene;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void updateTitle(String newTitle) {
        if (stage != null) {
            stage.setTitle(newTitle);
        }
    }
}