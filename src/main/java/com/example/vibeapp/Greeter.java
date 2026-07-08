package com.example.vibeapp;

/**
 * Pure domain logic for producing greeting messages.
 *
 * <p>Deliberately free of JavaFX types so it can be unit-tested without
 * starting the UI toolkit. This is the pattern to follow: keep business logic
 * in plain classes and let controllers only wire it to the view.
 */
public class Greeter {

    private int count;

    /**
     * Records a greeting and returns the message to display.
     *
     * @return the greeting message, including how many times it has been called
     */
    public String greet() {
        count++;
        return "Hello, Vibe Coding! (" + count + ")";
    }

    /**
     * @return how many greetings have been produced so far
     */
    public int count() {
        return count;
    }
}
