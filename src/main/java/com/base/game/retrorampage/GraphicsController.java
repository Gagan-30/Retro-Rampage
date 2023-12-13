package com.base.game.retrorampage;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GraphicsController {

    private Stage mainStage;
    private Scene settingsScene;

    private Settings settings = new Settings();

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    private void updateTitle(String newTitle) {
        if (mainStage != null) {
            mainStage.setTitle(newTitle);
        }
    }


}
