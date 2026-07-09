package com.example.vibeapp.model;

/**
 * A linear reference onto a track edge: the edge, a relative position along it
 * (0.0 = from-node … 1.0 = to-node) and the side.
 *
 * <p>This is the reusable "where on the network" value object shared by
 * edge-referenced fachdaten such as {@link Signal}. Immutable; the position is
 * clamped to [0,1] on construction.
 */
public record EdgeRef(String edgeId, double position, Side side) {

    public EdgeRef {
        position = Math.max(0.0, Math.min(1.0, position));
    }

    public EdgeRef withPosition(double newPosition) {
        return new EdgeRef(edgeId, newPosition, side);
    }

    public EdgeRef withSide(Side newSide) {
        return new EdgeRef(edgeId, position, newSide);
    }
}
