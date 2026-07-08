package com.example.vibeapp.geometry;

/**
 * Minimal immutable 2D vector, deliberately free of any UI-framework types so
 * geometry logic stays unit-testable without the JavaFX toolkit.
 */
public record Vec2(double x, double y) {

    public Vec2 subtract(Vec2 other) {
        return new Vec2(x - other.x, y - other.y);
    }

    public double length() {
        return Math.hypot(x, y);
    }
}
