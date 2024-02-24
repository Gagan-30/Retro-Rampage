package com.base.game.retrorampage.GameAssets;

public class Camera {
    private double x, y;
    private final double width, height; // Viewport size for zooming
    private final double levelWidth, levelHeight; // Overall level dimensions

    public Camera(double x, double y, double width, double height, double levelWidth, double levelHeight) {
        this.x = x;
        this.y = y;
        this.width = width; // Smaller than levelWidth for zoom effect
        this.height = height; // Smaller than levelHeight for zoom effect
        this.levelWidth = levelWidth;
        this.levelHeight = levelHeight;
    }

    public void update(Player player) {
        // Center the camera on the protagonist, within bounds
        this.x = Math.max(0, Math.min(player.getX() - this.width / 2, levelWidth - this.width));
        this.y = Math.max(0, Math.min(player.getY() - this.height / 2, levelHeight - this.height));
    }

    // Getters for camera position, if needed
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
