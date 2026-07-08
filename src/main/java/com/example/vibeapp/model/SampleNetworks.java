package com.example.vibeapp.model;

/**
 * Factory for demo networks. Provides a small, schematic topological map of
 * Switzerland as a starting point — positions are illustrative, not geographic.
 */
public final class SampleNetworks {

    private SampleNetworks() {
    }

    /**
     * A small topological Swiss network: a handful of stations, connecting
     * edges and one example signal referenced to an edge.
     */
    public static TrackNetwork switzerland() {
        TrackNetwork net = new TrackNetwork();

        TrackNode basel = net.addNode("Basel SBB", 170, 70);
        TrackNode olten = net.addNode("Olten", 250, 210);
        TrackNode zuerich = net.addNode("Zürich HB", 480, 150);
        TrackNode luzern = net.addNode("Luzern", 400, 320);
        TrackNode bern = net.addNode("Bern", 210, 360);
        TrackNode lausanne = net.addNode("Lausanne", 120, 480);
        TrackNode chur = net.addNode("Chur", 660, 330);

        net.addEdge(basel.id(), olten.id());
        net.addEdge(olten.id(), zuerich.id());
        net.addEdge(olten.id(), bern.id());
        net.addEdge(olten.id(), luzern.id());
        net.addEdge(zuerich.id(), luzern.id());
        net.addEdge(zuerich.id(), chur.id());
        TrackEdge bernLausanne = net.addEdge(bern.id(), lausanne.id());

        // Example fachdatum: a signal 40 % along the Bern–Lausanne edge, right side.
        net.addSignal(bernLausanne.id(), 0.4, Side.RIGHT);

        return net;
    }
}
