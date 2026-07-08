package com.example.vibeapp.geometry;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.vibeapp.model.Side;
import org.junit.jupiter.api.Test;

class GeometryTest {

    private static final double EPS = 1e-9;

    @Test
    void pointAtInterpolatesLinearly() {
        Vec2 p = Geometry.pointAt(new Vec2(0, 0), new Vec2(10, 20), 0.5);
        assertEquals(5.0, p.x(), EPS);
        assertEquals(10.0, p.y(), EPS);
    }

    @Test
    void nearestParamClampsToSegmentEnds() {
        Vec2 a = new Vec2(0, 0);
        Vec2 b = new Vec2(10, 0);
        assertEquals(0.0, Geometry.nearestParam(new Vec2(-5, 3), a, b), EPS);
        assertEquals(1.0, Geometry.nearestParam(new Vec2(15, -3), a, b), EPS);
        assertEquals(0.5, Geometry.nearestParam(new Vec2(5, 4), a, b), EPS);
    }

    @Test
    void distanceToSegmentMeasuresPerpendicularDistance() {
        double d = Geometry.distanceToSegment(new Vec2(5, 4), new Vec2(0, 0), new Vec2(10, 0));
        assertEquals(4.0, d, EPS);
    }

    @Test
    void offsetPointShiftsToOppositeSidesForLeftAndRight() {
        Vec2 a = new Vec2(0, 0);
        Vec2 b = new Vec2(10, 0); // direction +x; left normal points to -y
        Vec2 left = Geometry.offsetPoint(a, b, 0.5, 4.0, Side.LEFT);
        Vec2 right = Geometry.offsetPoint(a, b, 0.5, 4.0, Side.RIGHT);

        assertEquals(5.0, left.x(), EPS);
        assertEquals(-4.0, left.y(), EPS);
        assertEquals(5.0, right.x(), EPS);
        assertEquals(4.0, right.y(), EPS);
    }

    @Test
    void degenerateSegmentIsHandledGracefully() {
        Vec2 a = new Vec2(3, 3);
        assertEquals(0.0, Geometry.nearestParam(new Vec2(9, 9), a, a), EPS);
        assertEquals(a, Geometry.offsetPoint(a, a, 0.5, 5.0, Side.LEFT));
    }
}
