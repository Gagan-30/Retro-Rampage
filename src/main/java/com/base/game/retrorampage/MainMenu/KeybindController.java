package com.base.game.retrorampage.MainMenu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class KeybindController {
    private Scene previousScene;
    private Stage stage;
    private Button activeButton;
    private boolean readyToCaptureClick = false; // Flag to control click capturing


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
            if (activeButton != null) {
                activeButton.setText("Click to set key");
            }

            activeButton = (Button) event.getSource();
            activeButton.setText("Listening");
            readyToCaptureClick = false; // Initially, do not capture the click

            // Remove existing mouse click event filter and re-add it after a delay
            stage.removeEventFilter(MouseEvent.MOUSE_CLICKED, this::handleMouseClick);
            new Thread(() -> {
                try {
                    Thread.sleep(100); // Short delay
                    javafx.application.Platform.runLater(() -> {
                        stage.addEventFilter(MouseEvent.MOUSE_CLICKED, this::handleMouseClick);
                        readyToCaptureClick = true; // Now ready to capture the next click
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

            setupInputListeners();
        }
    }

    private void setupInputListeners() {
        // Remove existing listeners to avoid duplicates
        stage.removeEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPress);
        stage.removeEventFilter(MouseEvent.MOUSE_CLICKED, this::handleMouseClick);

        // Delay the setup of mouse click listener
        new Thread(() -> {
            try {
                Thread.sleep(100); // Delay in milliseconds
                javafx.application.Platform.runLater(() -> {
                    stage.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPress);
                    stage.addEventFilter(MouseEvent.MOUSE_CLICKED, this::handleMouseClick);
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }


    private void handleKeyPress(KeyEvent event) {
        if (activeButton != null && "Listening".equals(activeButton.getText())) {
            String keyName = event.getCode().toString();
            updateButtonAndRemoveListeners(keyName);
            event.consume();
        }
    }

    private void handleMouseClick(MouseEvent event) {
        if (readyToCaptureClick && activeButton != null) {
            MouseButton button = event.getButton();
            String clickType = getClickType(button);
            updateButtonAndRemoveListeners(clickType);
            readyToCaptureClick = false; // Reset the flag after capturing
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
            activeButton = null;
        }

        // Remove event listeners
        stage.removeEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPress);
        stage.removeEventFilter(MouseEvent.MOUSE_CLICKED, this::handleMouseClick);
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