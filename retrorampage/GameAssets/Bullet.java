package com.base.game.retrorampage.GameAssets;

import javafx.scene.layout.Pane;

public class Bullet extends Sprite {
    private double speed;
    private boolean active;
    private double screenWidth;
    private double screenHeight;
    private Pane root;
    private double dx; // Directional velocity in the x-axis
    private double dy; // Directional velocity in the y-axis
    private Input input;
    private Player player;

    public Bullet(double size, String imagePath, double speed, Pane root) {
        super(imagePath, size);
        this.speed = speed;
        this.active = false;
        this.root = root;
        this.input = new Input();
    }

    public void update(double dt) {
        if (isActive()) {
            // Move the bullet in the specified direction
            moveBy(dx * speed * dt, dy * speed * dt);

            // Check if the bullet is out of bounds
            if (isOutOfBounds()) {
                setActive(false); // Set the bullet as inactive when out of bounds
            }
        }
    }

    public void shoot(double playerX, double playerY, double playerAngle, Input input, Player player) {
        // Set the player instance
        this.player = player;

        // Set the bullet position and activate it
        setPosition(playerX, playerY);

        // Get the mouse looking direction from the player
        double angleToMouse = player.getMouseLookingDirection(input);

        // Set the rotation of the ImageView based on the calculated angle
        setRotation(angleToMouse);

        // Calculate the direction based on the mouse looking direction
        double bulletSpeed = 1.0; // You may adjust this value based on your game's requirements
        dx = bulletSpeed * Math.cos(Math.toRadians(angleToMouse));
        dy = bulletSpeed * Math.sin(Math.toRadians(angleToMouse));

        setActive(true);

        // Draw the bullet with the mouse looking direction
        draw(angleToMouse);
    }


    public void draw(double angleToMouse) {
        // Update the position of the ImageView based on the bullet's state
        imageView.relocate(getX(), getY());

        // Set the rotation of the ImageView based on the calculated angle
        imageView.setRotate(angleToMouse);

        // Add the bullet to the root
        root.getChildren().add(this.imageView);
    }

    private boolean isOutOfBounds() {
        // Check if the bullet is out of bounds based on your game's screen size
        // Adjust the conditions based on your requirements
        return getX() < 0 || getY() < 0 || getX() > screenWidth || getY() > screenHeight;
    }

    public boolean isActive() {
        return active;
    }

    private void setActive(boolean active) {
        this.active = active;
        if (!active) {
            root.getChildren().remove(this.imageView); // Remove the bullet from the root when inactive
        }
    }

    private void alignWithPlayer(double playerX, double playerY) {
        // Set the bullet position to align with the player's position
        setPosition(playerX, playerY);
    }
}
