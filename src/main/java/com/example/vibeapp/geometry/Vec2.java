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

    /**
     * Point fraction {@code t} of the way from this vector toward {@code target}
     * ({@code t} = 0 returns this point, {@code t} = 1 returns {@code target}).
     * Used to animate an element growing along a straight path.
     */
    public Vec2 lerp(Vec2 target, double t) {
        return new Vec2(x + (target.x - x) * t, y + (target.y - y) * t);
    }
}
