package com.base.game.retrorampage.GameAssets;

import com.base.game.retrorampage.LevelGeneration.Cell;
import com.base.game.retrorampage.MainMenu.Config;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Player extends Sprite {

    private double prevPlayerX;
    private double prevPlayerY;
    private Input input;
    private Config config;
    private double speed;
    private long lastUpdateTime;
    private ImageView texture;

    public Player(Image image, double x, double y, double speed, Input input, Config config) {
        super(image, x, y);
        this.imageView.setFitWidth(image.getWidth());
        this.imageView.setFitHeight(image.getHeight());
        this.speed = speed;
        this.input = input;
        this.config = config;
        this.texture = new ImageView(image); // Initialize the texture
    }


    public void drawInCenter(Cell center, Pane root) {
        double playerSize = 50; // Adjust dimensions as needed

        // Use class field for the player ImageView
        Image image = new Image("player.png");
        this.imageView = new ImageView(image);
        this.imageView.setFitWidth(playerSize);
        this.imageView.setFitHeight(playerSize);
        this.imageView.setX(center.getCenterX() - playerSize / 2);
        this.imageView.setY(center.getCenterY() - playerSize / 2);
        root.getChildren().add(this.imageView);

        handleMovementInput();
    }

    public void handleMovementInput() {
        long now = System.nanoTime();
        double dt = (now - lastUpdateTime) / 1e9; // Convert nanoseconds to seconds
        lastUpdateTime = now;

        double movement = speed * dt; // Calculate movement based on speed and time

        String moveUpKey1 = config.getKeybind("MoveUp1");
        String moveUpKey2 = config.getKeybind("MoveUp2");
        String moveDownKey1 = config.getKeybind("MoveDown1");
        String moveDownKey2 = config.getKeybind("MoveDown2");
        String moveLeftKey1 = config.getKeybind("MoveLeft1");
        String moveLeftKey2 = config.getKeybind("MoveLeft2");
        String moveRightKey1 = config.getKeybind("MoveRight1");
        String moveRightKey2 = config.getKeybind("MoveRight2");

        // Handle key presses and update player's position
        if (input.isKeyPressed(moveUpKey1) || input.isKeyPressed(moveUpKey2)) {
            prevPlayerY = getPosition().getY();
            moveBy(0, -movement);
        }
        if (input.isKeyPressed(moveDownKey1) || input.isKeyPressed(moveDownKey2)) {
            prevPlayerY = getPosition().getY();
            moveBy(0, movement);
        }
        if (input.isKeyPressed(moveLeftKey2) || input.isKeyPressed(moveLeftKey1)) {
            prevPlayerX = getPosition().getX();
            moveBy(-movement, 0);
        }
        if (input.isKeyPressed(moveRightKey1) || input.isKeyPressed(moveRightKey2)) {
            prevPlayerX = getPosition().getX();
            moveBy(movement, 0);
        }

        double mouseX = input.getMouseX();
        double mouseY = input.getMouseY();

        Vector position = getPosition();
        if (position != null) {
            double playerCenterX = position.getX() + getWidth() / 2;
            double playerCenterY = position.getY() + getHeight() / 2;

            double angleToMouse = Math.toDegrees(Math.atan2(mouseY - playerCenterY, mouseX - playerCenterX));

            setAngle(angleToMouse);

            // Update the player's position vector based on the movement
            position.setValues(getX(), getY());
        }
    }

    public Image getTexture() {
        return texture.getImage();
    }
}