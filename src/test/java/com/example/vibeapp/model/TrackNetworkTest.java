package com.example.vibeapp.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TrackNetworkTest {

    @Test
    void addNodeGeneratesSequentialIds() {
        TrackNetwork net = new TrackNetwork();
        assertEquals("N1", net.addNode(0, 0).id());
        assertEquals("N2", net.addNode(0, 0).id());
        assertEquals(2, net.nodes().size());
    }

    @Test
    void addNodeWithLabelSetsLabelAndIsLookupable() {
        TrackNetwork net = new TrackNetwork();
        TrackNode node = net.addNode("Olten", 5, 6);

        assertEquals("Olten", node.label());
        assertEquals(node, net.node(node.id()).orElseThrow());
        assertTrue(net.node("missing").isEmpty());
    }

    @Test
    void addEdgeRequiresExistingNodes() {
        TrackNetwork net = new TrackNetwork();
        TrackNode a = net.addNode(0, 0);
        assertThrows(IllegalArgumentException.class, () -> net.addEdge(a.id(), "missing"));
    }

    @Test
    void addSignalIsBoundToAnEdgeAndClampsPosition() {
        TrackNetwork net = new TrackNetwork();
        TrackNode a = net.addNode(0, 0);
        TrackNode b = net.addNode(10, 0);
        TrackEdge edge = net.addEdge(a.id(), b.id());

        Signal signal = net.addSignal(edge.id(), 1.5, Side.RIGHT);

        assertEquals(edge.id(), signal.edgeId());
        assertEquals(1.0, signal.position(), 1e-9);
        assertEquals(1, net.signals().size());
    }

    @Test
    void addSignalRejectsUnknownEdge() {
        TrackNetwork net = new TrackNetwork();
        assertThrows(IllegalArgumentException.class, () -> net.addSignal("E99", 0.5, Side.LEFT));
    }

    @Test
    void clearRemovesEverythingAndResetsIds() {
        TrackNetwork net = new TrackNetwork();
        TrackNode a = net.addNode(0, 0);
        TrackNode b = net.addNode(1, 1);
        net.addEdge(a.id(), b.id());

        net.clear();

        assertTrue(net.isEmpty());
        assertEquals("N1", net.addNode(0, 0).id());
    }
}
