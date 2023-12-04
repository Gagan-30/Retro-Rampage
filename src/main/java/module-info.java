module com.base.game.retrorampage {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.lwjgl;
    requires org.lwjgl.opengl;
    requires org.lwjgl.glfw;

    opens com.base.game.retrorampage to javafx.fxml;
    exports com.base.game.retrorampage;
}