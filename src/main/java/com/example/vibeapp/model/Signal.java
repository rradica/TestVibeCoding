package com.example.vibeapp.model;

/**
 * A signal (Fachdatum) positioned by <em>linear referencing</em> via an
 * {@link EdgeRef}: it belongs to a specific edge and sits at a relative position
 * along it, on a given {@link Side}. It has no coordinates of its own — its
 * screen position is derived from the edge geometry, so it stays correct when
 * nodes are moved.
 */
public final class Signal {

    private final String id;
    private EdgeRef reference;
    private String label;

    public Signal(String id, EdgeRef reference) {
        this.id = id;
        this.reference = reference;
        this.label = id;
    }

    public String id() {
        return id;
    }

    /** The linear reference (edge + position + side) locating this signal. */
    public EdgeRef reference() {
        return reference;
    }

    public String edgeId() {
        return reference.edgeId();
    }

    public double position() {
        return reference.position();
    }

    public Side side() {
        return reference.side();
    }

    public void setPosition(double position) {
        this.reference = reference.withPosition(position);
    }

    public void setSide(Side side) {
        this.reference = reference.withSide(side);
    }

    public String label() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
