module com.base.game.retrorampage {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.locationtech.jts;
    requires javafx.media; // Ensure this is included if you're using JavaFX Graphics components

    // Export the MainMenu package if it contains classes that need to be accessible to other modules or JavaFX.
    exports com.base.game.retrorampage.MainMenu;

    // If MainMenu contains FXML, this is correctly set.
    opens com.base.game.retrorampage.MainMenu to javafx.fxml;

    // Add exports for LevelGeneration package to make it accessible to JavaFX.
    exports com.base.game.retrorampage.LevelGeneration;
    exports com.base.game.retrorampage.GameAssets;


    // Open LevelGeneration to javafx.fxml if it contains FXML files.
    opens com.base.game.retrorampage.LevelGeneration to javafx.fxml;
    opens com.base.game.retrorampage.GameAssets to javafx.fxml;
}
