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

/**
 * Represents the player character in the game.
 */
public class Player extends Sprite {
    /** The square representing the player's avatar. */
    private final Rectangle square;

    /** The size of the player's avatar. */
    private final double size;

    /** The translation animation for damage label. */
    private final TranslateTransition translateTransition;

    /** The X coordinate of the player's previous position. */
    private double prevPlayerX;

    /** The Y coordinate of the player's previous position. */
    private double prevPlayerY;

    /** The timestamp of the last update. */
    private long lastUpdateTime = System.nanoTime();

    /** The manager for corridors. */
    private CorridorManager corridorManager;

    /** The current health of the player. */
    private int health;

    /** The root pane where the player is drawn. */
    private Pane root;

    /** A flag indicating whether the player has the key. */
    private boolean hasKey = false;

    /** A flag indicating whether the player can move. */
    private final boolean canMove = true;

    /** The camera used for the game. */
    private final Camera camera;

    /** The text label for displaying damage taken. */
    private final Text damageLabel;

    /** The fade transition for damage label. */
    private final FadeTransition fadeTransition;

    /** The bounds of the player in the parent node. */
    private Bounds boundsInParent;

    /** The manager for rooms. */
    private final RoomManager roomManager;

    /**
     * Constructs a new Player object.
     *
     * @param size The size of the player's avatar.
     * @param imagePath The path to the player's avatar image.
     * @param health The initial health of the player.
     * @param root The root pane where the player is drawn.
     * @param camera The camera used for the game.
     * @param roomManager The manager for rooms.
     */
    public Player(double size, String imagePath, int health, Pane root, Camera camera, RoomManager roomManager) {
        super(imagePath, size);
        this.square = new Rectangle(size, size);
        this.square.setFill(Color.TRANSPARENT);
        this.health = health;
        this.size = size;
        this.root = root;
        this.camera = camera;
        this.roomManager = roomManager;

        damageLabel = new Text();
        damageLabel.setFont(new Font("Arial", 20));
        damageLabel.setFill(Color.RED);
        damageLabel.setVisible(false);
        root.getChildren().add(damageLabel);

        translateTransition = new TranslateTransition(Duration.seconds(0.5), damageLabel);
        translateTransition.setFromY(0);
        translateTransition.setToY(-20);
        translateTransition.setCycleCount(1);

        fadeTransition = new FadeTransition(Duration.seconds(0.5), damageLabel);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
    }

    /**
     * Sets the corridor manager.
     *
     * @param corridorManager The corridor manager to set.
     */
    public void setCorridorManager(CorridorManager corridorManager) {
        this.corridorManager = corridorManager;
    }

    /**
     * Draws the player in the spawn cell.
     *
     * @param center The center cell where the player should spawn.
     * @param root The root pane where the player is drawn.
     */
    public void drawInSpawn(Cell center, Pane root) {
        double spawnCenterX = center.getCenterX();
        double spawnCenterY = center.getCenterY();

        if (!root.getChildren().contains(square)) {
            this.square.setX(spawnCenterX - square.getWidth() / 2);
            this.square.setY(spawnCenterY - square.getHeight() / 2);
            this.setPosition(spawnCenterX - this.size / 2, spawnCenterY - this.size / 2);
            this.addToPane(root); // Adding the Player object to the root Pane again
        } else {
            // Player is already spawned, check if it's at the spawn center
            double playerCenterX = getX() + square.getWidth() / 2;
            double playerCenterY = getY() + square.getHeight() / 2;

            if (playerCenterX != spawnCenterX || playerCenterY != spawnCenterY) {
                // Move the player to the spawn center
                this.square.setX(spawnCenterX - square.getWidth() / 2);
                this.square.setY(spawnCenterY - square.getHeight() / 2);
                this.setPosition(spawnCenterX - this.size / 2, spawnCenterY - this.size / 2);
            }
        }
    }

    /**
     * Updates the position of the player based on input and config.
     *
     * @param input The input object handling keyboard and mouse input.
     * @param config The configuration object containing key bindings.
     * @param movementSpeed The movement speed of the player.
     */
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

    /**
     * Moves the player by the specified deltaX and deltaY.
     *
     * @param deltaX The change in the X coordinate.
     * @param deltaY The change in the Y coordinate.
     */
    private void movePlayer(double deltaX, double deltaY) {
        prevPlayerX = getX(); // Store previous position
        prevPlayerY = getY(); // Store previous position
        square.setX(square.getX() + deltaX);
        square.setY(square.getY() + deltaY);
        setPosition(getX() + deltaX, getY() + deltaY);
    }
    /**
     * Calculates the direction the player is looking based on the mouse position.
     *
     * @param input The input object handling mouse input.
     * @return The angle in degrees representing the direction the player is looking.
     */
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
    /**
     * Checks if a color is allowed based on predefined thresholds.
     *
     * @param color The color to check.
     * @return True if the color is allowed, false otherwise.
     */
    private boolean isColorAllowed(Color color) {
        // Define the thresholds for light grey, grey, black, and dark red colors
        double lightGreyRedThreshold = 0.8;
        double lightGreyGreenThreshold = 0.8;
        double lightGreyBlueThreshold = 0.8;

        double greyRedThreshold = 0.5;
        double greyGreenThreshold = 0.5;
        double greyBlueThreshold = 0.5;

        double blackThreshold = 0.1; // Adjust as needed

        double darkRedRedThreshold = 0.5; // Adjust as needed
        double darkRedGreenThreshold = 0.0; // Adjust as needed
        double darkRedBlueThreshold = 0.0; // Adjust as needed

        // Get the RGB components of the color
        double red = color.getRed();
        double green = color.getGreen();
        double blue = color.getBlue();

        // Check if the color is within the thresholds for light grey, grey, black, or dark red
        boolean isLightGrey = (red >= lightGreyRedThreshold && green >= lightGreyGreenThreshold && blue >= lightGreyBlueThreshold);
        boolean isGrey = (red >= greyRedThreshold && green >= greyGreenThreshold && blue >= greyBlueThreshold);
        boolean isBlack = (red <= blackThreshold && green <= blackThreshold && blue <= blackThreshold);
        boolean isDarkRed = (red >= darkRedRedThreshold && green <= darkRedGreenThreshold && blue <= darkRedBlueThreshold);

        // Return true if the color is light grey, grey, black, or dark red, false otherwise
        return isLightGrey || isGrey || isBlack || isDarkRed;
    }

    /**
     * Gets the color of a pixel at the specified position on the screen.
     *
     * @param x The X coordinate of the position.
     * @param y The Y coordinate of the position.
     * @return The color of the pixel at the specified position.
     */    private Color getColorAtPosition(double x, double y) {
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
    /**
     * Retrieves the size of the player's avatar.
     *
     * @return The size of the player's avatar.
     */
    public double getSize() {
        return size;
    }

    /**
     * Decreases the player's health by the specified amount and triggers animations.
     *
     * @param amount The amount by which to decrease the player's health.
     */    public void decreaseHealth(int amount) {
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
    /**
     * Updates the health label with the specified amount and triggers animations.
     *
     * @param amount The amount to update the health label with.
     */
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


    /**
     * Sets the root pane where the player is drawn.
     *
     * @param root The root pane to set.
     */
    public void setRoot(Pane root) {
        this.root = root;
    }
    /**
     * Retrieves the current health of the player.
     *
     * @return The current health of the player.
     */
    public int getHealth() {
        return health;
    }
    /**
     * Sets the health of the player to the specified value.
     *
     * @param health The health value to set.
     */
    public void setHealth(int health) {
        this.health = health;
    }
    /**
     * Retrieves the square representing the player's avatar.
     *
     * @return The square representing the player's avatar.
     */
    public Rectangle getSquare() {
        return square;
    }
    /**
     * Increases the player's health by the specified amount, up to a maximum value.
     *
     * @param amount The amount by which to increase the player's health.
     */
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
    /**
     * Retrieves the bounds of the player in the parent node.
     *
     * @return The bounds of the player in the parent node.
     */
    public Bounds getBoundsInParent() {
        return boundsInParent;
    }
    /**
     * Checks if the player is currently in a room with a red color.
     *
     * @return True if the player is in a room with a red color, false otherwise.
     */
    public boolean isInRedRoom() {
        // Get the color at the player's position
        Color currentColor = getColorAtPosition(getX(), getY());

        // Check if the color at the player's position is red
        boolean inRedRoom = currentColor.equals(Color.DARKRED);

        return inRedRoom;
    }
    /**
     * Sets whether the player has the key.
     *
     * @param hasKey True if the player has the key, false otherwise.
     */
    public void setHasKey(boolean hasKey) {
        this.hasKey = hasKey;
    }

    /**
     * Retrieves whether the player has the key.
     *
     * @return True if the player has the key, false otherwise.
     */    public boolean hasKey() {
        return hasKey;
    }

}
