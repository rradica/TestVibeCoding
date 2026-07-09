package com.example.vibeapp.geometry;

/**
 * Pure easing helpers for time-based animations. UI-free (like the rest of this
 * package) so the timing math stays unit-testable without the JavaFX toolkit.
 */
public final class Easing {

    private Easing() {
    }

    /**
     * Smooth ease-in/ease-out of a normalized progress value. The input is
     * clamped to [0,1] and the classic smoothstep curve {@code 3t²-2t³} is
     * applied: it returns 0 at 0, 1 at 1, is symmetric around 0.5 and has zero
     * slope at both ends, so an animation accelerates and decelerates gently.
     */
    public static double smoothStep(double t) {
        double c = Math.max(0.0, Math.min(1.0, t));
        return c * c * (3.0 - 2.0 * c);
    }
}
