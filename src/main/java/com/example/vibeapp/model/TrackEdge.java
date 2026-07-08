package com.example.vibeapp.model;

/**
 * A track edge (Gleiskante) connecting two nodes. The direction from-node
 * &rarr; to-node defines the reference axis along which fachdaten are placed
 * (see {@link Signal}).
 */
public final class TrackEdge {

    private final String id;
    private final String fromNodeId;
    private final String toNodeId;
    private String label;

    public TrackEdge(String id, String fromNodeId, String toNodeId, String label) {
        this.id = id;
        this.fromNodeId = fromNodeId;
        this.toNodeId = toNodeId;
        this.label = label;
    }

    public String id() {
        return id;
    }

    public String fromNodeId() {
        return fromNodeId;
    }

    public String toNodeId() {
        return toNodeId;
    }

    public String label() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
