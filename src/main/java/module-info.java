module com.base.game.retrorampage {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.base.game.retrorampage to javafx.fxml;
    exports com.base.game.retrorampage;
}