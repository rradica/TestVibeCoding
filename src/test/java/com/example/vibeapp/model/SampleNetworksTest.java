package com.example.vibeapp.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class SampleNetworksTest {

    @Test
    void switzerlandHasNodesEdgesAndAValidExampleSignal() {
        TrackNetwork net = SampleNetworks.switzerland();

        assertEquals(7, net.nodes().size());
        assertFalse(net.edges().isEmpty());
        assertEquals(1, net.signals().size());

        Signal signal = net.signals().iterator().next();
        assertTrue(net.edge(signal.edgeId()).isPresent(),
                "the example signal must reference an existing edge");
    }
}
