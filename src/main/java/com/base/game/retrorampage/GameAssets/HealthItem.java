package com.base.game.retrorampage.GameAssets;

import com.base.game.retrorampage.LevelGeneration.Cell;
import com.base.game.retrorampage.LevelGeneration.RoomManager;
import javafx.geometry.Bounds;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.List;

/**
 * Represents a health item in the game.
 */
public class HealthItem extends Sprite {
    /**
     * The amount of health increase provided by this health item.
     */
    private static final int HEALTH_INCREASE = 60;

    /**
     * The player associated with the health item.
     */
    private final Player player;

    /**
     * The image view representing the health item.
     */
    private final ImageView imageView;

    /**
     * The root pane where the health item will be added.
     */
    private final Pane root;

    /**
     * A flag indicating whether the health item is currently active.
     */
    private boolean active;

    /**
     * The room manager managing the rooms in the game.
     */
    private final RoomManager roomManager;

    /**
     * Constructs a HealthItem object with the specified parameters.
     *
     * @param size        The size of the health item.
     * @param imagePath   The path to the image file representing the health item.
     * @param player      The player associated with the health item.
     * @param root        The root pane where the health item will be added.
     * @param roomManager The room manager managing the rooms in the game.
     */
    public HealthItem(double size, String imagePath, Player player, Pane root, RoomManager roomManager) {
        super(imagePath, size);
        this.player = player;
        this.root = root;
        this.imageView = new ImageView(imagePath);
        this.imageView.setFitWidth(size);
        this.imageView.setFitHeight(size);
        this.imageView.setPreserveRatio(true);
        this.active = false;
        this.roomManager = roomManager;
    }

    /**
     * Updates the state of the health item.
     * Checks for collision with the player and increases the player's health if collided.
     */
    public void update() {
        if (isActive() && collidesWith(player.getBoundsInParent())) {
            player.heal(HEALTH_INCREASE);
            setActive(false);
            removeFromPane();
        }
    }

    /**
     * Checks if the health item collides with the player.
     *
     * @param playerBounds The bounds of the player.
     * @return True if the health item collides with the player, false otherwise.
     */
    private boolean collidesWith(Bounds playerBounds) {
        Bounds healthItemBounds = getBoundsInParent();
        return healthItemBounds.intersects(playerBounds);
    }

    /**
     * Removes the health item from the root pane.
     */
    public void removeFromPane() {
        root.getChildren().remove(this.imageView);
    }

    /**
     * Draws the health item on the root pane.
     */
    private void draw() {
        root.getChildren().add(this.imageView);
    }

    /**
     * Spawns the health item randomly in a room.
     */
    public void spawnRandomly() {
        draw();
        if (!isActive()) {
            Cell randomRoom = getRandomRoom();
            if (randomRoom != null) {
                setRandomPosition(randomRoom);
                setActive(true);
            }
        }
    }

    /**
     * Retrieves a random room from the room manager.
     *
     * @return A random room if available, otherwise null.
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
     * Sets a random position for the health item within a given room.
     *
     * @param room The room where the health item will be placed.
     */
    private void setRandomPosition(Cell room) {
        double roomX = room.getX();
        double roomY = room.getY();
        double roomWidth = room.getWidth();
        double roomHeight = room.getHeight();
        double randomX = Math.max(roomX, Math.min(roomX + roomWidth - imageView.getFitWidth(), roomX + Math.random() * roomWidth));
        double randomY = Math.max(roomY, Math.min(roomY + roomHeight - imageView.getFitHeight(), roomY + Math.random() * roomHeight));
        imageView.relocate(randomX, randomY);
    }

    /**
     * Checks if the health item is active.
     *
     * @return True if the health item is active, false otherwise.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the active status of the health item.
     *
     * @param active True to activate the health item, false to deactivate.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Returns the bounds of the health item in its parent coordinate space.
     *
     * @return The bounds of the health item.
     */
    public Bounds getBoundsInParent() {
        return imageView.getBoundsInParent();
    }

    /**
     * Returns the health increase amount provided by this health item.
     *
     * @return The amount of health increase.
     */
    public int getHealthIncrease() {
        return HEALTH_INCREASE;
    }
}
