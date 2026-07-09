package com.example.vibeapp.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class TrackElementsTest {

    @Test
    void nodeLabelDefaultsToIdAndTracksMovement() {
        TrackNode node = new TrackNode("N1", 10, 20);
        assertEquals("N1", node.label());

        node.setLabel("Bern");
        node.moveTo(30, 40);

        assertEquals("Bern", node.label());
        assertEquals(30, node.x(), 1e-9);
        assertEquals(40, node.y(), 1e-9);
    }

    @Test
    void edgeExposesEndpointsAndDefaultLabel() {
        TrackEdge edge = new TrackEdge("E1", "N1", "N2");

        assertEquals("N1", edge.fromNodeId());
        assertEquals("N2", edge.toNodeId());
        assertEquals("E1", edge.label());

        edge.setLabel("Hauptgleis");
        assertEquals("Hauptgleis", edge.label());
    }

    @Test
    void sideOppositeToggles() {
        assertEquals(Side.RIGHT, Side.LEFT.opposite());
        assertEquals(Side.LEFT, Side.RIGHT.opposite());
    }
}
