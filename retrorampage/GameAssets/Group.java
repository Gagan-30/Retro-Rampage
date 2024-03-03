package com.base.game.retrorampage.GameAssets;

import java.util.ArrayList;

/**
 * A collection of sprite objects.
 */
public class Group {
    /**
     * The collection underlying this list.
     */
    private final ArrayList<Sprite> list;

    /**
     * Initialize this object.
     */
    public Group() {
        this.list = new ArrayList<>();
    }

    /**
     * Add a sprite to this collection.
     *
     * @param sprite The sprite being added to this collection.
     */
    public void add(Sprite sprite) {
        this.list.add(sprite);
    }

    /**
     * Remove a sprite from this collection.
     *
     * @param sprite The sprite being removed from this collection.
     */
    public void remove(Sprite sprite) {
        this.list.remove(sprite);
    }

    /**
     * Retrieve a (shallow) copy of this list.
     * Especially useful in for loops.
     * Avoids concurrent modification exceptions
     * when adding/removing from this collection during iteration.
     *
     * @return a copy of the list underlying this collection
     */
    public ArrayList<Sprite> getList() {
        return new ArrayList<>(list);
    }

    /**
     * Determine the number of sprites in this collection.
     *
     * @return the size of this collection
     */
    public int size() {
        return this.list.size();
    }


    /**
     * Update all sprite objects in this collection.
     */
    public void update(double dt) {
        for (Sprite sprite : this.getList())
            sprite.update(dt);
    }
}
