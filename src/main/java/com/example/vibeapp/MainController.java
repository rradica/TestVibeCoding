package com.example.vibeapp;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controller for {@code main-view.fxml}.
 *
 * <p>Keeps only view wiring here; the actual logic lives in {@link Greeter} so
 * it can be tested independently of the JavaFX toolkit.
 */
public class MainController {

    @FXML
    private Label messageLabel;

    private final Greeter greeter = new Greeter();

    @FXML
    private void onHelloButtonClick() {
        messageLabel.setText(greeter.greet());
    }
}
