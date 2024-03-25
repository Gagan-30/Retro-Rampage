package com.base.game.retrorampage.GameAssets;

import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

/**
 * Represents a camera for controlling the view in the game world.
 * Provides translation and zoom functionality.
 */
public class Camera {
    /** Translation transformation for moving the camera. */
    private final Translate translation;

    /** Scale transformation for zooming the camera. */
    private final Scale zoom;

    /** Initial zoom level of the camera. */
    private final double initialZoomLevel;

    /** Speed of zooming. */
    private final double zoomSpeed;


    /**
     * Constructs a camera with the specified initial zoom level and zoom speed.
     *
     * @param initialZoomLevel The initial zoom level.
     * @param zoomSpeed        The speed of zooming.
     */
    public Camera(double initialZoomLevel, double zoomSpeed) {
        this.initialZoomLevel = initialZoomLevel;
        this.zoomSpeed = zoomSpeed;

        this.translation = new Translate();
        this.zoom = new Scale(initialZoomLevel, initialZoomLevel);
    }

    /**
     * Adjusts the camera position to follow the player.
     *
     * @param player      The player to follow.
     * @param sceneWidth  The width of the scene.
     * @param sceneHeight The height of the scene.
     */
    public void follow(Player player, double sceneWidth, double sceneHeight) {
        // Adjust the camera position to follow the player
        translation.setX(sceneWidth / 2 - player.getX());
        translation.setY(sceneHeight / 2 - player.getY());
    }

    /**
     * Gets the translation transformation of the camera.
     *
     * @return The translation transformation.
     */
    public Translate getTranslation() {
        return translation;
    }

    /**
     * Gets the scale transformation of the camera.
     *
     * @return The scale transformation.
     */
    public Scale getZoom() {
        return zoom;
    }
}

