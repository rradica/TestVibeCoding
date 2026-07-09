package com.example.vibeapp.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class SignalTest {

    @Test
    void labelDefaultsToIdAndCanBeChanged() {
        Signal signal = new Signal("S1", new EdgeRef("E1", 0.5, Side.RIGHT));
        assertEquals("S1", signal.label());

        signal.setLabel("Einfahrsignal");
        assertEquals("Einfahrsignal", signal.label());
    }

    @Test
    void setPositionAndSideUpdateTheLinearReference() {
        Signal signal = new Signal("S1", new EdgeRef("E1", 0.5, Side.RIGHT));

        signal.setPosition(1.5); // clamped by EdgeRef
        signal.setSide(Side.LEFT);

        assertEquals(1.0, signal.position(), 1e-9);
        assertEquals(Side.LEFT, signal.side());
        assertEquals("E1", signal.edgeId());
        assertEquals(1.0, signal.reference().position(), 1e-9);
    }
}
