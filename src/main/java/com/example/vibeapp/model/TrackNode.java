package com.example.vibeapp.model;

/**
 * A node (Knoten) in the topological track network — for example a station or a
 * junction. Its coordinates are schematic (canvas pixels), not geographic: the
 * layout expresses topology, not real-world position.
 *
 * <p>The label defaults to the id and can be set afterwards via
 * {@link #setLabel(String)}.
 */
public final class TrackNode {

    private final String id;
    private String label;
    private double x;
    private double y;

    public TrackNode(String id, double x, double y) {
        this.id = id;
        this.label = id;
        this.x = x;
        this.y = y;
    }

    public String id() {
        return id;
    }

    public String label() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    /** Moves the node to a new schematic position. */
    public void moveTo(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
