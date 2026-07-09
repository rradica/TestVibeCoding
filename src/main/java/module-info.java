module com.example.vibeapp {
    requires javafx.controls;
    requires javafx.fxml;

    // Allow JavaFX to reflectively access the FXML controller.
    opens com.example.vibeapp.view to javafx.fxml;

    exports com.example.vibeapp;
}
