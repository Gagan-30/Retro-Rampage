module com.base.game.retrorampage {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;

    opens com.base.game.retrorampage to javafx.fxml;
    exports com.base.game.retrorampage;
}