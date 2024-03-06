package com.base.game.retrorampage.GameAssets;

public interface Screen
{
    /**
     * Initialize game objects used in this particular screen.
     */
    void initialize();

    /**
     * Update game objects used in this particular screen.
     * Runs 60 times per second (when possible).
     */
    void update();
}
