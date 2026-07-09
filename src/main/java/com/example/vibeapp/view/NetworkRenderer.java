package com.example.vibeapp.view;

import com.example.vibeapp.geometry.Geometry;
import com.example.vibeapp.geometry.Vec2;
import com.example.vibeapp.model.Signal;
import com.example.vibeapp.model.TrackEdge;
import com.example.vibeapp.model.TrackNetwork;
import com.example.vibeapp.model.TrackNode;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Draws a {@link TrackNetwork} onto a JavaFX canvas. Pure rendering — it reads
 * the model and some transient highlight ids, but owns no editing state.
 */
final class NetworkRenderer {

    static final double NODE_RADIUS = 9.0;
    private static final double SIGNAL_OFFSET = 20.0;
    private static final double SIGNAL_RADIUS = 5.0;

    private static final Color BACKGROUND = Color.web("#11111b");
    private static final Color EDGE = Color.web("#7f849c");
    private static final Color NODE = Color.web("#89b4fa");
    private static final Color SIGNAL = Color.web("#f38ba8");
    private static final Color TEXT = Color.web("#cdd6f4");
    private static final Color HIGHLIGHT = Color.web("#f9e2af");
    private static final Color PENDING = Color.web("#a6e3a1");

    private final GraphicsContext gc;

    NetworkRenderer(GraphicsContext gc) {
        this.gc = gc;
        this.gc.setFont(Font.font(12));
    }

    // Guardrail exception: 5 parameters. A future refactor may bundle the
    // canvas size and highlight ids into a small view-state value object.
    @SuppressWarnings("checkstyle:ParameterNumber")
    void render(TrackNetwork network, double width, double height,
                String highlightedNodeId, String pendingEdgeNodeId) {
        gc.setFill(BACKGROUND);
        gc.fillRect(0, 0, width, height);

        drawEdges(network);
        drawSignals(network);
        drawNodes(network, highlightedNodeId, pendingEdgeNodeId);
    }

    private void drawEdges(TrackNetwork network) {
        gc.setStroke(EDGE);
        gc.setLineWidth(2.5);
        for (TrackEdge edge : network.edges()) {
            TrackNode from = network.node(edge.fromNodeId()).orElse(null);
            TrackNode to = network.node(edge.toNodeId()).orElse(null);
            if (from == null || to == null) {
                continue;
            }
            gc.strokeLine(from.x(), from.y(), to.x(), to.y());
        }
    }

    private void drawSignals(TrackNetwork network) {
        for (Signal signal : network.signals()) {
            TrackEdge edge = network.edge(signal.edgeId()).orElse(null);
            if (edge == null) {
                continue;
            }
            TrackNode from = network.node(edge.fromNodeId()).orElse(null);
            TrackNode to = network.node(edge.toNodeId()).orElse(null);
            if (from == null || to == null) {
                continue;
            }
            Vec2 a = new Vec2(from.x(), from.y());
            Vec2 b = new Vec2(to.x(), to.y());
            Vec2 base = Geometry.pointAt(a, b, signal.position());
            Vec2 marker = Geometry.offsetPoint(a, b, signal.position(), SIGNAL_OFFSET, signal.side());

            gc.setStroke(SIGNAL);
            gc.setLineWidth(1.0);
            gc.strokeLine(base.x(), base.y(), marker.x(), marker.y());

            gc.setFill(SIGNAL);
            gc.fillOval(marker.x() - SIGNAL_RADIUS, marker.y() - SIGNAL_RADIUS,
                    2 * SIGNAL_RADIUS, 2 * SIGNAL_RADIUS);

            gc.setFill(TEXT);
            gc.fillText(signal.label(), marker.x() + SIGNAL_RADIUS + 3, marker.y() + 4);
        }
    }

    private void drawNodes(TrackNetwork network, String highlightedNodeId, String pendingEdgeNodeId) {
        for (TrackNode node : network.nodes()) {
            gc.setFill(NODE);
            gc.fillOval(node.x() - NODE_RADIUS, node.y() - NODE_RADIUS,
                    2 * NODE_RADIUS, 2 * NODE_RADIUS);

            if (node.id().equals(pendingEdgeNodeId)) {
                gc.setStroke(PENDING);
                gc.setLineWidth(3.0);
                gc.strokeOval(node.x() - NODE_RADIUS, node.y() - NODE_RADIUS,
                        2 * NODE_RADIUS, 2 * NODE_RADIUS);
            } else if (node.id().equals(highlightedNodeId)) {
                gc.setStroke(HIGHLIGHT);
                gc.setLineWidth(3.0);
                gc.strokeOval(node.x() - NODE_RADIUS, node.y() - NODE_RADIUS,
                        2 * NODE_RADIUS, 2 * NODE_RADIUS);
            }

            gc.setFill(TEXT);
            gc.fillText(node.label(), node.x() + NODE_RADIUS + 4, node.y() + 4);
        }
    }
}
