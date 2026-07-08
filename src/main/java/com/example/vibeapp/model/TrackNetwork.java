package com.example.vibeapp.model;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * The topological track network: nodes, edges and edge-referenced fachdaten
 * (currently signals). Pure model with no UI dependency, so it can be built and
 * verified in tests.
 *
 * <p>Ids are generated per type ({@code N1}, {@code E1}, {@code S1}, …) via
 * internal counters, keeping references stable and human-readable.
 */
public final class TrackNetwork {

    private final Map<String, TrackNode> nodes = new LinkedHashMap<>();
    private final Map<String, TrackEdge> edges = new LinkedHashMap<>();
    private final Map<String, Signal> signals = new LinkedHashMap<>();

    private int nodeCounter;
    private int edgeCounter;
    private int signalCounter;

    // --- Nodes ---

    /** Adds a node at the given schematic position with an auto-generated id/label. */
    public TrackNode addNode(double x, double y) {
        String id = "N" + (++nodeCounter);
        TrackNode node = new TrackNode(id, x, y);
        nodes.put(id, node);
        return node;
    }

    /** Adds a node with an explicit label at the given schematic position. */
    public TrackNode addNode(String label, double x, double y) {
        TrackNode node = addNode(x, y);
        node.setLabel(label);
        return node;
    }

    public Optional<TrackNode> node(String id) {
        return Optional.ofNullable(nodes.get(id));
    }

    public Collection<TrackNode> nodes() {
        return Collections.unmodifiableCollection(nodes.values());
    }

    // --- Edges ---

    /**
     * Adds an edge between two existing nodes.
     *
     * @throws IllegalArgumentException if either node id is unknown
     */
    public TrackEdge addEdge(String fromNodeId, String toNodeId) {
        if (!nodes.containsKey(fromNodeId) || !nodes.containsKey(toNodeId)) {
            throw new IllegalArgumentException(
                    "Edge references unknown node: " + fromNodeId + " -> " + toNodeId);
        }
        String id = "E" + (++edgeCounter);
        TrackEdge edge = new TrackEdge(id, fromNodeId, toNodeId);
        edges.put(id, edge);
        return edge;
    }

    public Optional<TrackEdge> edge(String id) {
        return Optional.ofNullable(edges.get(id));
    }

    public Collection<TrackEdge> edges() {
        return Collections.unmodifiableCollection(edges.values());
    }

    // --- Signals ---

    /**
     * Adds a signal referenced to an existing edge.
     *
     * @param position relative position along the edge, clamped to [0,1]
     * @throws IllegalArgumentException if the edge id is unknown
     */
    public Signal addSignal(String edgeId, double position, Side side) {
        if (!edges.containsKey(edgeId)) {
            throw new IllegalArgumentException("Signal references unknown edge: " + edgeId);
        }
        String id = "S" + (++signalCounter);
        Signal signal = new Signal(id, new EdgeRef(edgeId, position, side));
        signals.put(id, signal);
        return signal;
    }

    public Collection<Signal> signals() {
        return Collections.unmodifiableCollection(signals.values());
    }

    // --- Whole-network operations ---

    public boolean isEmpty() {
        return nodes.isEmpty() && edges.isEmpty() && signals.isEmpty();
    }

    /** Removes all nodes, edges and signals and resets id generation. */
    public void clear() {
        nodes.clear();
        edges.clear();
        signals.clear();
        nodeCounter = 0;
        edgeCounter = 0;
        signalCounter = 0;
    }
}
