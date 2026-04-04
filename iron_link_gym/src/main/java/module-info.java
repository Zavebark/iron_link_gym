module com.iron_link_gym {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.oracle.database.jdbc;

    exports com.iron_link_gym to javafx.graphics, javafx.fxml;
    opens com.iron_link_gym to javafx.fxml;
    opens com.iron_link_gym.controller to javafx.fxml;
    opens com.iron_link_gym.model to javafx.base;
}