package com.base.game.retrorampage.MainMenu;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainMenuController {

    private Stage mainStage;
    private GameLoop gameLoop;

    private long lastTime = System.nanoTime();
    private double delta = 0;
    private final double ns = 1000000000.0 / 60.0; // 60 times per second
    private int frames = 0;
    private long timer = System.currentTimeMillis();
    private Scene difficultySelectionScene;
    private Scene loadGameScene;
    private Scene settingsScene;
    private Scene aboutScene;
    private Scene exitScene;

    // Instances of various scenes and functionalities for the main menu
    private final DifficultySelection difficultySelection = new DifficultySelection();
    private final LoadGame loadGame = new LoadGame();
    private final Settings settings = new Settings();
    private final About about = new About();
    private final Exit exit = new Exit();

    // Method to set the main stage for the controller
    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    private void updateTitle(String newTitle) {
        if (mainStage != null) {
            mainStage.setTitle(newTitle);
        }
    }

    public MainMenuController() {
        // Initialize the game loop with update and render actions specific to the main menu
        this.gameLoop = new GameLoop(this::updateMainMenu, this::renderMainMenu);
    }

    private void updateMainMenu() {
        System.out.println("Updating Main Menu");

        long now = System.nanoTime();
        delta += (now - lastTime) / ns;
        lastTime = now;
        while (delta >= 1) {
            // Update logic for the main menu
            delta--;
        }
    }

    private void renderMainMenu() {
        System.out.println("Rendering Main Menu");

        frames++;
        if(System.currentTimeMillis() - timer > 1000) {
            timer += 1000;
            System.out.println("FPS: " + frames);
            frames = 0;
        }
        // Render logic for the main menu
    }


    // Event handler for the "Start Game" button
    @FXML
    protected void onStartGameButtonClick() {
        if (difficultySelectionScene == null) {
            // Load the difficulty selection scene if not loaded
            difficultySelectionScene = difficultySelection.createDifficultySelectionScene(mainStage.getScene(), mainStage);
        }
        updateTitle("Select Difficulty");
        mainStage.setScene(difficultySelectionScene);
    }

    // Event handler for the "Load Game" button
    @FXML
    protected void onLoadGameButtonClick() {
        if (loadGameScene == null) {
            // Load the Load Game scene if not loaded
            loadGameScene = loadGame.createLoadGameScene(mainStage.getScene(), mainStage);
        }
        updateTitle("Select Game Save");
        mainStage.setScene(loadGameScene);
    }

    // Event handler for the "Settings" button
    @FXML
    protected void onSettingsButtonClick() {
        if (settingsScene == null) {
            // Load the Settings scene if not loaded
            System.out.println("[Test] Setting scene transition at " + java.time.LocalDateTime.now());
            settingsScene = settings.createSettingsScene(mainStage.getScene(), mainStage);
        }
        updateTitle("Settings");
        mainStage.setScene(settingsScene);
    }

    // Event handler for the "About" button
    @FXML
    protected void onAboutButtonClick() {
        if (aboutScene == null) {
            // Pass the current scene as the previous scene and the stage
            aboutScene = about.createAboutScene(mainStage.getScene(), mainStage);
        }
        updateTitle("About");
        mainStage.setScene(aboutScene);
    }

    // Event handler for the "Exit" button
    @FXML
    protected void onExitButtonClick() {
        if (exitScene == null) {
            // Pass the current scene as the previous scene and the stage
            System.out.println("[Exit] Testing scene transition at " + java.time.LocalDateTime.now());

            exitScene = exit.createExitScene(mainStage.getScene(), mainStage);
        }
        updateTitle("Exit");
        mainStage.setScene(exitScene);
    }
}