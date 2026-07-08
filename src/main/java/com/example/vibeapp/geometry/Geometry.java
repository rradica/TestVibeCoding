package com.example.vibeapp.geometry;

import com.example.vibeapp.model.Side;

/**
 * Pure geometric helpers for linear referencing along straight track edges.
 *
 * <p>All methods are side-effect free and independent of JavaFX, which keeps the
 * math that positions fachdaten (e.g. signals) on edges fully unit-testable.
 */
public final class Geometry {

    private Geometry() {
    }

    /**
     * Point at parameter {@code t} in [0,1] along the segment {@code a}&rarr;{@code b}
     * (0 = a, 1 = b).
     */
    public static Vec2 pointAt(Vec2 a, Vec2 b, double t) {
        return new Vec2(a.x() + (b.x() - a.x()) * t,
                a.y() + (b.y() - a.y()) * t);
    }

    /**
     * Parameter {@code t} in [0,1] of the point on segment {@code a}&rarr;{@code b}
     * closest to {@code p}, clamped to the segment ends. Returns 0 for a
     * degenerate (zero-length) segment.
     */
    public static double nearestParam(Vec2 p, Vec2 a, Vec2 b) {
        double dx = b.x() - a.x();
        double dy = b.y() - a.y();
        double lengthSq = dx * dx + dy * dy;
        if (lengthSq == 0.0) {
            return 0.0;
        }
        double t = ((p.x() - a.x()) * dx + (p.y() - a.y()) * dy) / lengthSq;
        return Math.max(0.0, Math.min(1.0, t));
    }

    /** Shortest distance from {@code p} to the segment {@code a}&rarr;{@code b}. */
    public static double distanceToSegment(Vec2 p, Vec2 a, Vec2 b) {
        return p.subtract(pointAt(a, b, nearestParam(p, a, b))).length();
    }

    /**
     * Point at parameter {@code t} along {@code a}&rarr;{@code b}, shifted
     * perpendicular to the segment by {@code offset} to the given side (relative
     * to the a&rarr;b direction). Used to place edge attachments beside the track.
     *
     * <p>Screen coordinates (y grows downward): {@code LEFT} of travel direction
     * points to smaller y.
     */
    public static Vec2 offsetPoint(Vec2 a, Vec2 b, double t, double offset, Side side) {
        Vec2 base = pointAt(a, b, t);
        double dx = b.x() - a.x();
        double dy = b.y() - a.y();
        double len = Math.hypot(dx, dy);
        if (len == 0.0) {
            return base;
        }
        // Left-hand normal relative to travel direction.
        double nx = dy / len;
        double ny = -dx / len;
        double sign = (side == Side.LEFT) ? 1.0 : -1.0;
        return new Vec2(base.x() + nx * offset * sign,
                base.y() + ny * offset * sign);
    }
}
