package com.example.vibeapp.geometry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class EasingTest {

    private static final double EPS = 1e-9;

    @Test
    void endpointsAreExact() {
        assertEquals(0.0, Easing.smoothStep(0.0), EPS);
        assertEquals(1.0, Easing.smoothStep(1.0), EPS);
    }

    @Test
    void clampsOutsideUnitInterval() {
        assertEquals(0.0, Easing.smoothStep(-0.5), EPS);
        assertEquals(1.0, Easing.smoothStep(2.0), EPS);
    }

    @Test
    void midpointIsHalf() {
        assertEquals(0.5, Easing.smoothStep(0.5), EPS);
    }

    @Test
    void easesInThenOutSymmetrically() {
        double early = Easing.smoothStep(0.25);
        double late = Easing.smoothStep(0.75);
        // Eased-in: slower than linear near the start, faster near the end.
        assertTrue(early < 0.25, "expected eased-in value below linear");
        assertTrue(late > 0.75, "expected eased-out value above linear");
        // Smoothstep is point-symmetric about (0.5, 0.5).
        assertEquals(1.0, early + late, EPS);
    }
}
