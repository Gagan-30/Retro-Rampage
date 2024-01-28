module com.base.game.retrorampage {
    requires javafx.controls;
    requires javafx.fxml;
    exports com.base.game.retrorampage.MainMenu;
    opens com.base.game.retrorampage.MainMenu to javafx.fxml;
}