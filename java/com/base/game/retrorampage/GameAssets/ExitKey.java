package com.base.game.retrorampage.GameAssets;

import com.base.game.retrorampage.LevelGeneration.Cell;
import com.base.game.retrorampage.LevelGeneration.RoomManager;
import javafx.geometry.Bounds;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.List;

/**
 * Represents an exit key in the game.
 * Manages its behavior, interaction with the player, and spawning.
 */
public class ExitKey extends Sprite {
    /** Reference to the player. */
    private final Player player;

    /** Image view of the exit key. */
    private final ImageView imageView;

    /** Root pane of the game. */
    private final Pane root;

    /** Indicates if the exit key is active. */
    private boolean active;

    /** Indicates if the player has collected the key. */
    private boolean hasKey;

    /** Manager for rooms in the level. */
    private final RoomManager roomManager;

    /**
     * Constructs an exit key with the specified parameters.
     *
     * @param size        The size of the exit key.
     * @param imagePath   The image path for the exit key.
     * @param player      The player object.
     * @param root        The root pane of the game.
     * @param roomManager The room manager object.
     */
    public ExitKey(double size, String imagePath, Player player, Pane root, RoomManager roomManager) {
        super(imagePath, size);
        this.player = player;
        this.root = root; // Initialize the root field
        this.imageView = new ImageView(imagePath);
        this.imageView.setFitWidth(size);
        this.imageView.setFitHeight(size);
        this.imageView.setPreserveRatio(true);
        this.active = false;
        this.hasKey = false;
        this.roomManager = roomManager; // Assign the roomManager parameter to the field
    }


    /**
     * Updates the exit key's behavior.
     * Checks for collision with the player and handles key collection.
     */
    public void update() {
        if (isActive() && !hasKey) { // Check if the key is not collected yet
            // Check for collision with the player
            if (collidesWith(player.getBoundsInParent())) {
                // Increase player's health and deactivate the exit key
                hasKey = true; // Set the hasKey value to true when the player collects the key
                setActive(false);
                removeFromPane(); // Remove the exit key from the root
            }
        }
    }


    /**
     * Checks if the exit key collides with the player's bounds.
     *
     * @param playerBounds The bounds of the player.
     * @return True if collision occurs, false otherwise.
     */
    private boolean collidesWith(Bounds playerBounds) {
        Bounds healthItemBounds = getBoundsInParent();
        return healthItemBounds.intersects(playerBounds);
    }

    /**
     * Removes the exit key from the root pane.
     */
    public void removeFromPane() {
        root.getChildren().remove(this.imageView);
    }

    /**
     * Draws the exit key onto the root pane.
     */
    private void draw() {
        root.getChildren().add(this.imageView);
    }

    /**
     * Spawns the exit key randomly in a room.
     */
    public void spawnRandomly() {
        draw();
        if (!isActive()) {
            Cell randomRoom = getRandomRoom(); // Get a random room from the roomManager
            if (randomRoom != null) {
                setRandomPosition(randomRoom); // Set the position randomly within the room
                setActive(true); // Activate the health item
            }
        }
    }

    /**
     * Retrieves a random room from the room manager.
     *
     * @return A randomly selected room, or null if no rooms are available.
     */
    private Cell getRandomRoom() {
        List<Cell> rooms = roomManager.getCells();
        if (!rooms.isEmpty()) {
            int randomIndex = (int) (Math.random() * rooms.size());
            return rooms.get(randomIndex);
        }
        return null;
    }

    /**
     * Sets the exit key to a random position within the specified room.
     *
     * @param room The room in which to set the exit key's position.
     */
    private void setRandomPosition(Cell room) {
        double roomX = room.getX();
        double roomY = room.getY();
        double roomWidth = room.getWidth();
        double roomHeight = room.getHeight();

        // Calculate random position within the room, ensuring it stays within bounds
        double randomX = Math.max(roomX, Math.min(roomX + roomWidth - imageView.getFitWidth(), roomX + Math.random() * roomWidth));
        double randomY = Math.max(roomY, Math.min(roomY + roomHeight - imageView.getFitHeight(), roomY + Math.random() * roomHeight));

        // Set the position of the health item
        imageView.relocate(randomX, randomY);
    }

    /**
     * Retrieves the active state of the exit key.
     *
     * @return True if the exit key is active, false otherwise.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the active state of the exit key.
     *
     * @param active The active state to set.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Checks if the player has collected the exit key.
     *
     * @return True if the player has the key, false otherwise.
     */
    public boolean hasKey() {
        return hasKey;
    }

    /**
     * Retrieves the bounds of the exit key in its parent coordinates.
     *
     * @return The bounds of the exit key.
     */
    public Bounds getBoundsInParent() {
        return imageView.getBoundsInParent();
    }

}
