package com.base.game.retrorampage.GameAssets;

import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

/**
 * A structure for storing and updating keyboard and mouse state.
 */
public class Input {
    /**
     * A list to store keys that were just pressed.
     */
    public ArrayList<String> justPressedQueue;

    /**
     * A list to store keys that were just released.
     */
    public ArrayList<String> justReleasedQueue;

    /**
     * A list to store keys that were just pressed.
     */
    public ArrayList<String> justPressedList;

    /**
     * A list to store keys that are currently pressed.
     */
    public ArrayList<String> stillPressedList;

    /**
     * A list to store keys that were just released.
     */
    public ArrayList<String> justReleasedList;

    /**
     * The X coordinate of the mouse.
     */
    private double mouseX;

    /**
     * The Y coordinate of the mouse.
     */
    private double mouseY;

    /**
     * A flag indicating whether the mouse button is pressed.
     */
    private boolean isMousePressed;

    /**
     * Initialize object and activate event listeners.
     *
     * @param listeningScene the window Scene that has focus during the game
     */
    public Input(Scene listeningScene) {
        // Initialization of lists
        justPressedQueue = new ArrayList<>();
        justReleasedQueue = new ArrayList<>();
        justPressedList = new ArrayList<>();
        stillPressedList = new ArrayList<>();
        justReleasedList = new ArrayList<>();

        // Event listeners for keyboard input
        listeningScene.setOnKeyPressed(
                (KeyEvent event) -> {
                    String keyName = event.getCode().toString();
                    justPressedQueue.add(keyName);
                }
        );

        listeningScene.setOnKeyReleased(
                (KeyEvent event) -> {
                    String keyName = event.getCode().toString();
                    justReleasedQueue.add(keyName);
                }
        );

        // Event listeners for mouse input
        listeningScene.setOnMousePressed(
                (MouseEvent event) -> {
                    isMousePressed = true;
                }
        );

        listeningScene.setOnMouseReleased(
                (MouseEvent event) -> {
                    isMousePressed = false;
                }
        );

        listeningScene.setOnMouseDragged(
                (MouseEvent event) -> {
                    mouseX = event.getX();
                    mouseY = event.getY();
                }
        );
    }

    /**
     * Update input state information.
     * Automatically called by {@link Game} class during the game loop.
     */
    public void update() {
        // clear previous discrete event status
        justPressedList.clear();
        justReleasedList.clear();

        // update current event status
        for (String keyName : justPressedQueue) {
            // avoid multiple keypress events while holding key
            // avoid duplicate entries in key pressed list
            if (!stillPressedList.contains(keyName)) {
                justPressedList.add(keyName);
                stillPressedList.add(keyName);
            }
        }

        for (String keyName : justReleasedQueue) {
            stillPressedList.remove(keyName);
            justReleasedList.add(keyName);
        }

        // clear the queues used to store events
        justPressedQueue.clear();
        justReleasedQueue.clear();
    }

    /**
     * Determine if key has been pressed / moved to down position (a discrete action).
     *
     * @param keyName name of corresponding key (examples: "LEFT", "A", "DIGIT1", "SPACE", "SHIFT")
     * @return true if key was just pressed
     */
    public boolean isKeyJustPressed(String keyName) {
        return justPressedList.contains(keyName);
    }

    /**
     * Determine if key is currently being pressed / held down (a continuous action).
     *
     * @param keyName name of corresponding key (examples: "LEFT", "A", "DIGIT1", "SPACE", "SHIFT")
     * @return true if key is currently pressed
     */
    public boolean isKeyPressed(String keyName) {
        return stillPressedList.contains(keyName);
    }

    /**
     * Determine if key has been released / returned to up position (a discrete action).
     *
     * @param keyName name of corresponding key (examples: "LEFT", "A", "DIGIT1", "SPACE", "SHIFT")
     * @return true if key was just released
     */
    public boolean isKeyJustReleased(String keyName) {
        return justReleasedList.contains(keyName);
    }

    /**
     * Get the X coordinate of the mouse.
     *
     * @return the X coordinate of the mouse
     */
    public double getMouseX() {
        return mouseX;
    }

    /**
     * Get the Y coordinate of the mouse.
     *
     * @return the Y coordinate of the mouse
     */
    public double getMouseY() {
        return mouseY;
    }

    /**
     * Set the position of the mouse.
     *
     * @param x the X coordinate of the mouse
     * @param y the Y coordinate of the mouse
     */
    public void setMousePosition(double x, double y) {
        this.mouseX = x;
        this.mouseY = y;
    }

    /**
     * Update the mouse position based on MouseEvent.
     *
     * @param event the MouseEvent containing the new mouse position
     */
    public void updateMousePosition(MouseEvent event) {
        mouseX = event.getX();
        mouseY = event.getY();
    }

    /**
     * Reset all input states.
     */
    public void resetInput() {
        // Clear all lists
        justPressedQueue.clear();
        justReleasedQueue.clear();
        justPressedList.clear();
        stillPressedList.clear();
        justReleasedList.clear();

        // Reset mouse state
        mouseX = 0;
        mouseY = 0;
        isMousePressed = false;
    }
}
