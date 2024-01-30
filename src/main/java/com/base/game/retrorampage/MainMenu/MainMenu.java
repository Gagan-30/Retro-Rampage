package com.base.game.retrorampage.MainMenu;


import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

public class MainMenu {

    private SceneSwitcher sceneSwitcher;

    public MainMenu(SceneSwitcher sceneSwitcher) {
        this.sceneSwitcher = sceneSwitcher;
    }

    private void applySettingsFromConfig(Stage stage, Config config) {
        String resolution = config.loadResolutionSetting();
        String[] parts = resolution.split(" x ");
        if (parts.length == 2) {
            stage.setWidth(Integer.parseInt(parts[0]));
            stage.setHeight(Integer.parseInt(parts[1]));
        }
        stage.setFullScreenExitHint("");
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setFullScreen(config.loadFullscreenSetting());
    }
}
