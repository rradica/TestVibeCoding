module com.example.vibeapp {
    requires javafx.controls;
    requires javafx.fxml;

    // Allow JavaFX to reflectively access controllers and load FXML resources.
    opens com.example.vibeapp to javafx.fxml;
    exports com.example.vibeapp;
}
