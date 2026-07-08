package com.example.vibeapp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class GreeterTest {

    @Test
    void greetIncludesRunningCount() {
        Greeter greeter = new Greeter();

        assertEquals("Hello, Vibe Coding! (1)", greeter.greet());
        assertEquals("Hello, Vibe Coding! (2)", greeter.greet());
    }

    @Test
    void countReflectsNumberOfGreetings() {
        Greeter greeter = new Greeter();

        greeter.greet();
        greeter.greet();
        greeter.greet();

        assertEquals(3, greeter.count());
    }
}
