package com.example.vibeapp.model;

/**
 * A signal (Fachdatum) positioned by <em>linear referencing</em>: it belongs to
 * a specific {@link TrackEdge} and sits at a relative position along it, on a
 * given {@link Side}. It has no coordinates of its own — its screen position is
 * derived from the edge geometry, so it stays correct when nodes are moved.
 */
public final class Signal {

    private final String id;
    private final String edgeId;
    /** Relative position along the edge: 0.0 = from-node, 1.0 = to-node. */
    private double position;
    private Side side;
    private String label;

    public Signal(String id, String edgeId, double position, Side side, String label) {
        this.id = id;
        this.edgeId = edgeId;
        this.position = clampPosition(position);
        this.side = side;
        this.label = label;
    }

    public String id() {
        return id;
    }

    public String edgeId() {
        return edgeId;
    }

    public double position() {
        return position;
    }

    public void setPosition(double position) {
        this.position = clampPosition(position);
    }

    public Side side() {
        return side;
    }

    public void setSide(Side side) {
        this.side = side;
    }

    public String label() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    private static double clampPosition(double value) {
        return Math.max(0.0, Math.min(1.0, value));
    }
}
