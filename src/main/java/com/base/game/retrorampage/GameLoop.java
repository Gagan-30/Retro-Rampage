package com.base.game.retrorampage;

public class GameLoop implements Runnable {

    private boolean running = false;
    private Thread gameThread;
    private final Runnable updateAction;
    private final Runnable renderAction;

    public GameLoop(Runnable updateAction, Runnable renderAction) {
        this.updateAction = updateAction;
        this.renderAction = renderAction;
    }

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
            updateAction.run();
            renderAction.run();
        }
    }
}
