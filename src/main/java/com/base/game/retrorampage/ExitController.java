package com.base.game.retrorampage;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ExitController {

    private Scene previousScene;
    private Stage stage; // Add a stage variable

    public void setPreviousScene(Scene previousScene) {
        this.previousScene = previousScene;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void onConfirmationButtonClick() {
        System.exit(0);
    }

    @FXML
    public void onCancelButtonClick() {
        System.out.println("Previous Scene: " + previousScene);
        System.out.println("Stage: " + stage);

        if (previousScene != null && stage != null) {
            stage.setScene(previousScene);
        }
    }

}
