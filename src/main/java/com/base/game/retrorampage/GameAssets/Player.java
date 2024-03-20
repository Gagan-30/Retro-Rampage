package com.base.game.retrorampage.GameAssets;

import com.base.game.retrorampage.LevelGeneration.Cell;
import com.base.game.retrorampage.LevelGeneration.CorridorManager;
import com.base.game.retrorampage.LevelGeneration.RoomManager;
import com.base.game.retrorampage.MainMenu.Config;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Bounds;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Player extends Sprite {
    private final Rectangle square;
    private final double size;
    private final TranslateTransition translateTransition;
    private double prevPlayerX;
    private double prevPlayerY;
    private long lastUpdateTime = System.nanoTime();
    private CorridorManager corridorManager;
    private int health;  // New field to store player health
    private Pane root;  // Add the root field
    private boolean hasKey = false; // Add a boolean variable to track if the player has the key
    private final boolean canMove = true;  // Add a flag to control movement based on color
    private final Camera camera;
    private final Text damageLabel;
    private final FadeTransition fadeTransition;
    private Bounds boundsInParent;
    private final RoomManager roomManager;

    public Player(double size, String imagePath, int health, Pane root, Camera camera, RoomManager roomManager) {
        super(imagePath, size);
        this.square = new Rectangle(size, size);
        this.square.setFill(Color.TRANSPARENT);
        this.health = health;  // Initialize player health
        this.size = size;  // Initialize player size
        this.root = root;  // Initialize the root field
        this.camera = camera;
        this.roomManager = roomManager;

        // Initialize damage label first
        damageLabel = new Text();
        damageLabel.setFont(new Font("Arial", 20));
        damageLabel.setFill(Color.RED);
        damageLabel.setVisible(false);
        root.getChildren().add(damageLabel);

        // Initialize translate transition first
        translateTransition = new TranslateTransition(Duration.seconds(0.5), damageLabel);
        translateTransition.setFromY(0);
        translateTransition.setToY(-20);
        translateTransition.setCycleCount(1);

        // Initialize fade transition after damageLabel
        fadeTransition = new FadeTransition(Duration.seconds(0.5), damageLabel);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
    }


    public void setCorridorManager(CorridorManager corridorManager) {
        this.corridorManager = corridorManager;
    }

    public void drawInSpawn(Cell center, Pane root) {
        if (!root.getChildren().contains(square)) {
            this.square.setX(center.getCenterX() - square.getWidth() / 2);
            this.square.setY(center.getCenterY() - square.getHeight() / 2);
            this.setPosition(center.getCenterX() - this.size / 2, center.getCenterY() - this.size / 2);
            this.addToPane(root); // Adding the Player object to the root Pane again
        }
    }


    public void updatePosition(Input input, Config config, double movementSpeed) {
        // Update input state
        input.update();

        // Move the square based on arrow key input
        double speed = movementSpeed;

        long now = System.nanoTime();
        double dt = (now - lastUpdateTime) / 1e9; // Convert nanoseconds to seconds
        lastUpdateTime = now;
        double movement = speed * dt;

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
            movePlayer(0, -movement);
        }
        if (input.isKeyPressed(moveDownKey1) || input.isKeyPressed(moveDownKey2)) {
            movePlayer(0, movement);
        }
        if (input.isKeyPressed(moveLeftKey2) || input.isKeyPressed(moveLeftKey1)) {
            movePlayer(-movement, 0);
        }
        if (input.isKeyPressed(moveRightKey1) || input.isKeyPressed(moveRightKey2)) {
            movePlayer(movement, 0);
        }

        double angleToMouse = getMouseLookingDirection(input);
        setRotation(angleToMouse);

        // Update the player's position based on input
        square.setX(getX());
        square.setY(getY());

        // Check the color at the player's current position
        Color pixelColor = getColorAtPosition(getX(), getY());

        // Check if the pixel color is in the allowed range
        if (!isColorAllowed(pixelColor)) {
            // Optionally, handle the case where the player is in a restricted area
            System.out.println("Player is in a restricted area!");

            // Revert to the previous position
            square.setX(prevPlayerX);
            square.setY(prevPlayerY);
            setPosition(prevPlayerX, prevPlayerY);
        }

    }

    private void movePlayer(double deltaX, double deltaY) {
        prevPlayerX = getX(); // Store previous position
        prevPlayerY = getY(); // Store previous position
        square.setX(square.getX() + deltaX);
        square.setY(square.getY() + deltaY);
        setPosition(getX() + deltaX, getY() + deltaY);
    }

    public double getMouseLookingDirection(Input input) {
        double mouseX = input.getMouseX();
        double mouseY = input.getMouseY();

        // Adjust the mouse coordinates based on the camera translation
        double adjustedMouseX = mouseX - camera.getTranslation().getX();
        double adjustedMouseY = mouseY - camera.getTranslation().getY();

        double angle = Math.toDegrees(Math.atan2(adjustedMouseY - (getY() + size / 2), adjustedMouseX - (getX() + size / 2)));

        // Normalize the angle to [0, 360)
        angle = (angle + 360) % 360;

        return angle;
    }

    private boolean isColorAllowed(Color color) {
        // Define the allowed color ranges
        double redThreshold = 0.8;
        double greenThreshold = 0.9;
        double blueThreshold = 0.9;

        // Check if the color is within the allowed range
        return color.getRed() <= redThreshold && color.getGreen() <= greenThreshold && color.getBlue() <= blueThreshold;
    }


    // New method to get the color of a pixel at a specific position
    private Color getColorAtPosition(double x, double y) {
        double sceneWidth = root.getWidth();
        double sceneHeight = root.getHeight();

        if (x < 0 || x >= sceneWidth || y < 0 || y >= sceneHeight) {
            // Position is outside the bounds of the root, consider it as out-of-bounds
            return Color.WHITE;
        }

        // Create a snapshot of the root as a WritableImage
        WritableImage snapshot = new WritableImage((int) sceneWidth, (int) sceneHeight);
        root.snapshot(null, snapshot);

        // Read the color at the specified position
        int pixelX = (int) x;
        int pixelY = (int) y;
        Color pixelColor = snapshot.getPixelReader().getColor(pixelX, pixelY);

        return pixelColor;
    }

    public double getSize() {
        return size;
    }

    // New method to decrease player health
    public void decreaseHealth(int amount) {
        if (health > 0) {
            health -= amount;

            if (health <= 0) {
                // Player is defeated, remove the player object from the pane
                removeFromPane(); // Remove the Player object from the pane
                health = 0; // Ensure health doesn't go below 0
                System.out.println("Player health: 0");
            } else {
                System.out.println("Player health: " + health);
                updateHealthLabel(amount); // Update the health label with the amount
                fadeTransition.play(); // Start the fade-out animation
                translateTransition.play(); // Start the translate animation
            }
        }
    }

    public void updateHealthLabel(int amount) {
        if (amount == 20) {
            damageLabel.setText("-" + amount);
            damageLabel.setX(getX() - size / 2); // Adjust position to center the label horizontally
            damageLabel.setY(getY() - size / 2 - 30); // Adjust position to place the label above the player

            // Set initial opacity and translation
            damageLabel.setOpacity(1.0);
            damageLabel.setTranslateY(0);

            // Set an event handler to hide the label when the fade-out animation is finished
            fadeTransition.setOnFinished(event -> {
                damageLabel.setVisible(false);
                damageLabel.setOpacity(1.0); // Reset opacity after animation
                damageLabel.setTranslateY(0); // Reset translation after animation
            });

            // Set an event handler to reset translate transition after its completion
            translateTransition.setOnFinished(event -> {
                damageLabel.setTranslateY(0); // Reset translation after animation
            });

            // Ensure the label is in front of the room and player by setting its Z-order
            damageLabel.toFront();

            // Start the fade-out and translate animations
            fadeTransition.play();
            translateTransition.play();
            damageLabel.setVisible(true); // Make the label visible before starting the animations
        } else {
            damageLabel.setText("+" + amount);
            damageLabel.setFill(Color.GREEN);
            damageLabel.setX(getX() - size / 2); // Adjust position to center the label horizontally
            damageLabel.setY(getY() - size / 2 - 30); // Adjust position to place the label above the player

            // Set initial opacity and translation
            damageLabel.setOpacity(1.0);
            damageLabel.setTranslateY(0);

            // Set an event handler to hide the label when the fade-out animation is finished
            fadeTransition.setOnFinished(event -> {
                damageLabel.setVisible(false);
                damageLabel.setOpacity(1.0); // Reset opacity after animation
                damageLabel.setTranslateY(0); // Reset translation after animation
            });

            // Set an event handler to reset translate transition after its completion
            translateTransition.setOnFinished(event -> {
                damageLabel.setTranslateY(0); // Reset translation after animation
            });

            // Ensure the label is in front of the room and player by setting its Z-order
            damageLabel.toFront();
            // Start the fade-out and translate animations
            fadeTransition.play();
            translateTransition.play();
            damageLabel.setVisible(true); // Make the label visible before starting the animations
        }
    }


    // New method to set the root
    public void setRoot(Pane root) {
        this.root = root;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Rectangle getSquare() {
        return square;
    }

    public void heal(int amount) {
        int maxHealth = 100; // Adjust this based on your maximum health
        if (health < maxHealth) {
            health += amount;
            if (health > maxHealth) {
                health = maxHealth; // Ensure health doesn't exceed the maximum
            }
            System.out.println("Player healed: " + amount);
        }
    }

    public Bounds getBoundsInParent() {
        return boundsInParent;
    }

    public boolean isInRedRoom() {
        // Get the color at the player's position
        Color currentColor = getColorAtPosition(getX(), getY());

        // Check if the color at the player's position is red
        boolean inRedRoom = currentColor.equals(Color.DARKRED);

        return inRedRoom;
    }

    public void setHasKey(boolean hasKey) {
        this.hasKey = hasKey;
    }

    // Getter method to retrieve the value of the hasKey variable
    public boolean hasKey() {
        return hasKey;
    }

}
