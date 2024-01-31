package com.base.game.retrorampage.MainMenu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    private Button shoot1Button, shoot2Button, aim1Button, aim2Button;
    @FXML
    private Button inventory1Button, inventory2Button;

    private final Map<Button, String> buttonToActionMap = new HashMap<>();
    private Config config;

    public void initialize() {
        config = new Config("config.txt"); // Adjust the path as needed

        // Map buttons to their corresponding actions
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
        buttonToActionMap.put(inventory1Button, "Inventory1");
        buttonToActionMap.put(inventory2Button, "Inventory2");

        loadKeybindsIntoScene();
    }

    private void loadKeybindsIntoScene() {
        for (Map.Entry<Button, String> entry : buttonToActionMap.entrySet()) {
            String keybind = config.getKeybind(entry.getValue());
            entry.getKey().setText(keybind);
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

    @FXML
    public void onActionButtonClick(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            if (activeButton != null && !activeButton.equals(event.getSource())) {
                revertButtonToOriginalText(activeButton);
            }
            setupInputListeners();
            activeButton = (Button) event.getSource();
            activeButton.setText("Listening");

            // Introduce a delay before setting readyToCaptureClick to true
            new Thread(() -> {
                try {
                    Thread.sleep(200); // Delay for 200 milliseconds
                    javafx.application.Platform.runLater(() -> readyToCaptureClick = true);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private void revertButtonToOriginalText(Button button) {
        String action = buttonToActionMap.get(button);
        if (action != null) {
            String keybind = config.getKeybind(action);
            button.setText(keybind);
        }
    }


    private void setupInputListeners() {
        if (!inputListenersSetup) {
            System.out.println("Setting up input listeners");

            stage.removeEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPress);
            stage.removeEventFilter(MouseEvent.MOUSE_CLICKED, this::handleMouseClick);

            stage.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPress);
            stage.addEventFilter(MouseEvent.MOUSE_CLICKED, this::handleMouseClick);

            inputListenersSetup = true;
        }
    }


    private void handleKeyPress(KeyEvent event) {
        System.out.println("Key Pressed: " + event.getCode());

        if (readyToCaptureClick && activeButton != null && "Listening".equals(activeButton.getText())) {
            String keyName = event.getCode().toString();
            updateButtonAndRemoveListeners(keyName);
            event.consume();
            readyToCaptureClick = false;
        }
    }

    private void handleMouseClick(MouseEvent event) {
        System.out.println("Mouse Clicked: " + event.getButton());

        // Check if we're ready to capture a click, and the click is not on the activeButton itself
        if (readyToCaptureClick && activeButton != null && "Listening".equals(activeButton.getText()) && event.getSource() != activeButton) {
            MouseButton button = event.getButton();
            String clickType = getClickType(button);
            updateButtonAndRemoveListeners(clickType);
            readyToCaptureClick = false;
        }
    }


    private String getClickType(MouseButton button) {
        return switch (button) {
            case PRIMARY -> "Left Click";
            case SECONDARY -> "Right Click";
            case MIDDLE -> "Middle Click";
            default -> "Other Click";
        };
    }

    private void updateButtonAndRemoveListeners(String input) {
        if (activeButton != null) {
            activeButton.setText(input);

            // Save the new keybind
            String action = buttonToActionMap.get(activeButton);
            if (action != null) {
                config.saveKeybind(action, input);
            }

            activeButton = null;
            // No need to remove listeners here as they are shared for all buttons
        }
    }

    @FXML
    public void onReturnButtonClick() {
        boolean wasFullScreen = stage.isFullScreen();

        if (previousScene != null && stage != null) {
            stage.setScene(previousScene);

            if (wasFullScreen) {
                stage.setFullScreen(true);
            }

            updateTitle("Settings");
        }
    }
}