package com.base.game.retrorampage.MainMenu;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class DifficultySelection {
    private SceneSwitcher sceneSwitcher;

    public DifficultySelection(SceneSwitcher sceneSwitcher) {
        this.sceneSwitcher = sceneSwitcher;
    }

    public void display(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DifficultySelection.fxml"));
        Parent root = loader.load();

        DifficultySelectionController controller = loader.getController();
        controller.setSceneSwitcher(sceneSwitcher); // Make sure to set SceneSwitcher here

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
