package com.example.vibeapp;

import java.io.IOException;
import java.util.Objects;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Application entry point. Bootstraps the JavaFX runtime, loads the main view
 * and shows the primary window.
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main-view.fxml"));
        Scene scene = new Scene(loader.load(), 480, 320);
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());

        stage.setTitle("Vibe Coding App");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
