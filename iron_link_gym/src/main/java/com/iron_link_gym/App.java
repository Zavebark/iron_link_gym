package com.iron_link_gym;

import com.iron_link_gym.db.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Create tables if they don't exist yet
        DBConnection.initSchema();

        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/com/iron_link_gym/main.fxml"));
        Scene scene = new Scene(loader.load(), 1100, 700);
        stage.setTitle("Iron-Link Gym Management System");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        DBConnection.closeConnection();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
