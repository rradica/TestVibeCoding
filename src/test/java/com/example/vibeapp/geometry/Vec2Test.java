package com.example.vibeapp.geometry;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class Vec2Test {

    private static final double EPS = 1e-9;

    @Test
    void subtractComponentwise() {
        Vec2 d = new Vec2(7, 4).subtract(new Vec2(2, 1));
        assertEquals(5.0, d.x(), EPS);
        assertEquals(3.0, d.y(), EPS);
    }

    @Test
    void lengthIsEuclidean() {
        assertEquals(5.0, new Vec2(3, 4).length(), EPS);
    }

    @Test
    void lerpAtEndpointsReturnsStartAndEnd() {
        Vec2 a = new Vec2(0, 0);
        Vec2 b = new Vec2(10, 20);
        assertEquals(a, a.lerp(b, 0.0));
        assertEquals(b, a.lerp(b, 1.0));
    }

    @Test
    void lerpInterpolatesMidpoint() {
        Vec2 m = new Vec2(0, 0).lerp(new Vec2(10, 20), 0.5);
        assertEquals(5.0, m.x(), EPS);
        assertEquals(10.0, m.y(), EPS);
    }
}
