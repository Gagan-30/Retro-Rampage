package com.base.game.retrorampage.MainMenu;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import java.io.IOException;

public class MainMenu {

    private SceneSwitcher sceneSwitcher;

    public MainMenu(SceneSwitcher sceneSwitcher) {
        this.sceneSwitcher = sceneSwitcher;
    }

    public void display(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
        Parent root = loader.load();
        MainMenuController controller = loader.getController();
        controller.setSceneSwitcher(sceneSwitcher);
        stage.setScene(new Scene(root));
        stage.show();
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
