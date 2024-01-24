package com.base.game.retrorampage;

public class GameLoop implements Runnable {

    private boolean running = false;
    private Thread gameThread;

    public synchronized void start() {
        if (running) return;
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public synchronized void stop() {
        if (!running) return;
        running = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (running) {
            update();
            render();
        }
    }

    private void update() {
        // Game update logic
    }

    private void render() {
        // Game render logic
    }
}
