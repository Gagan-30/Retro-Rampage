package com.base.game.retrorampage.GameAssets;

import javafx.geometry.Bounds;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Bullet {
    private static final double BULLET_SPEED = 350.0; // Adjust the bullet speed as needed
    private final Pane root;
    private final ImageView imageView;
    private final Player player;
    private final Input input;
    private boolean active;
    private double dx; // Directional velocity in the x-axis
    private double dy; // Directional velocity in the y-axis
    private Bounds boundsInParent;
    private double x; // Corrected to double
    private double y; // Corrected to double
    private double width;
    private double height;

    public Bullet(double size, String image, Pane root, Player player, Input input) {
        this.active = false;
        this.root = root;
        this.player = player;
        this.input = input;
        this.imageView = new ImageView(image);
        this.imageView.setFitWidth(size);
        this.imageView.setFitHeight(size);
        this.imageView.setPreserveRatio(true);
    }

    public void update(double dt) {
        if (isActive()) {
            // Move the bullet in the specified direction
            imageView.setTranslateX(imageView.getTranslateX() + dx * BULLET_SPEED * dt);
            imageView.setTranslateY(imageView.getTranslateY() + dy * BULLET_SPEED * dt);

            // Set the rotation of the ImageView based on the direction
            imageView.setRotate(Math.toDegrees(Math.atan2(dy, dx)));

            // Check if the bullet is out of bounds
            if (isOutOfBounds()) {
                setActive(false); // Set the bullet as inactive when out of bounds
            }
        }
    }

    public void shoot(double playerX, double playerY, double playerAngle) {
        // Set the bullet position and activate it
        imageView.setTranslateX(playerX);
        imageView.setTranslateY(playerY);

        playerAngle = player.getMouseLookingDirection(input);
        // Calculate the direction based on the player's looking direction
        double radians = Math.toRadians(playerAngle); // Convert to radians
        dx = Math.cos(radians);
        dy = Math.sin(radians);

        setActive(true);

        // Draw the bullet
        draw(playerAngle);
    }

    public void draw(double angleToMouse) {
        // Set the rotation of the ImageView based on the calculated angle
        imageView.setRotate(angleToMouse);

        // Add the bullet to the root if it's not already a child
        if (!root.getChildren().contains(imageView)) {
            root.getChildren().add(this.imageView);
        }
    }

    private boolean isOutOfBounds() {
        // Check if the bullet is out of bounds based on your game's screen size
        // Adjust the conditions based on your requirements
        return imageView.getTranslateX() < 0 || imageView.getTranslateY() < 0 ||
                imageView.getTranslateX() > root.getWidth() || imageView.getTranslateY() > root.getHeight();
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        if (!active && root.getChildren().contains(imageView)) {
            root.getChildren().remove(this.imageView); // Remove the bullet from the root when inactive
        }
    }

    public Bounds getBoundsInParent() {
        return imageView.getBoundsInParent();
    }

    public double getX() {
        return imageView.getBoundsInParent().getMinX();
    }

    public double getY() {
        return imageView.getBoundsInParent().getMinY();
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void setPosition(double x, double y) {
        // Set the bullet's position
        this.x = x;
        this.y = y;

        // Update the ImageView's position
        imageView.setTranslateX(x);
        imageView.setTranslateY(y);
    }

}