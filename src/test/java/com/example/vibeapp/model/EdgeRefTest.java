package com.example.vibeapp.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class EdgeRefTest {

    @Test
    void clampsPositionIntoUnitRange() {
        assertEquals(0.0, new EdgeRef("E1", -0.5, Side.LEFT).position(), 1e-9);
        assertEquals(1.0, new EdgeRef("E1", 2.0, Side.LEFT).position(), 1e-9);
        assertEquals(0.25, new EdgeRef("E1", 0.25, Side.LEFT).position(), 1e-9);
    }

    @Test
    void withPositionAndWithSideReturnUpdatedCopies() {
        EdgeRef ref = new EdgeRef("E1", 0.3, Side.LEFT);

        EdgeRef moved = ref.withPosition(0.7);
        assertEquals(0.7, moved.position(), 1e-9);
        assertEquals("E1", moved.edgeId());
        assertEquals(Side.LEFT, moved.side());

        EdgeRef flipped = ref.withSide(Side.RIGHT);
        assertEquals(Side.RIGHT, flipped.side());
        assertEquals(0.3, flipped.position(), 1e-9);
    }
}
