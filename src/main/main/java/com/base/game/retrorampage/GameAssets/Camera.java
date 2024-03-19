package com.base.game.retrorampage.GameAssets;

import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

public class Camera {
    private final Translate translation;
    private final Scale zoom;
    private final double initialZoomLevel;
    private final double zoomSpeed;

    public Camera(double initialZoomLevel, double zoomSpeed) {
        this.initialZoomLevel = initialZoomLevel;
        this.zoomSpeed = zoomSpeed;

        this.translation = new Translate();
        this.zoom = new Scale(initialZoomLevel, initialZoomLevel);
    }

    public void follow(Player player, double sceneWidth, double sceneHeight) {
        // Adjust the camera position to follow the player
        translation.setX(sceneWidth / 2 - player.getX());
        translation.setY(sceneHeight / 2 - player.getY());
    }

    public void zoomIn() {
        // Zoom in by multiplying the current scale by the zoom speed
        zoom.setX(zoom.getX() * (1 + zoomSpeed));
        zoom.setY(zoom.getY() * (1 + zoomSpeed));
    }

    public void zoomOut() {
        // Zoom out by dividing the current scale by the zoom speed
        zoom.setX(zoom.getX() / (1 + zoomSpeed));
        zoom.setY(zoom.getY() / (1 + zoomSpeed));
    }

    public Translate getTranslation() {
        return translation;
    }

    public Scale getZoom() {
        return zoom;
    }
}

