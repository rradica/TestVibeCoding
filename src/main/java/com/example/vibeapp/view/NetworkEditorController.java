package com.example.vibeapp.view;

import java.util.Optional;

import com.example.vibeapp.geometry.Geometry;
import com.example.vibeapp.geometry.Vec2;
import com.example.vibeapp.model.SampleNetworks;
import com.example.vibeapp.model.Side;
import com.example.vibeapp.model.TrackEdge;
import com.example.vibeapp.model.TrackNetwork;
import com.example.vibeapp.model.TrackNode;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 * Controller for the track-network editor.
 *
 * <p>Holds the transient editing state (active tool, drag/edge-pending
 * selection) and translates mouse input into operations on the {@link
 * TrackNetwork}. All drawing is delegated to {@link NetworkRenderer} and all
 * geometry to {@link Geometry}, so this class stays focused on interaction.
 */
public class NetworkEditorController {

    /** The active editing tool selected in the toolbar. */
    private enum Tool { SELECT, NODE, EDGE, SIGNAL }

    private static final double NODE_HIT_RADIUS = 12.0;
    private static final double EDGE_HIT_DISTANCE = 8.0;

    @FXML private Pane canvasHolder;
    @FXML private Canvas canvas;
    @FXML private ToggleGroup toolGroup;
    @FXML private ToggleButton selectButton;
    @FXML private ToggleButton nodeButton;
    @FXML private ToggleButton edgeButton;
    @FXML private ToggleButton signalButton;
    @FXML private Label statusLabel;

    private TrackNetwork network = SampleNetworks.switzerland();
    private NetworkRenderer renderer;

    private Tool tool = Tool.SELECT;
    private TrackNode draggedNode;
    private TrackNode pendingEdgeNode;
    private String highlightedNodeId;

    @FXML
    private void initialize() {
        renderer = new NetworkRenderer(canvas.getGraphicsContext2D());

        // A Canvas is not resizable, so as a managed child it would pin the
        // holder's minimum size and stop the window from shrinking. Take it out
        // of layout and drive its size from the holder manually instead.
        canvas.setManaged(false);
        canvasHolder.widthProperty().addListener((obs, old, now) -> {
            canvas.setWidth(now.doubleValue());
            redraw();
        });
        canvasHolder.heightProperty().addListener((obs, old, now) -> {
            canvas.setHeight(now.doubleValue());
            redraw();
        });

        selectButton.setUserData(Tool.SELECT);
        nodeButton.setUserData(Tool.NODE);
        edgeButton.setUserData(Tool.EDGE);
        signalButton.setUserData(Tool.SIGNAL);
        toolGroup.selectedToggleProperty().addListener((obs, old, now) -> onToolChanged(old, now));
        toolGroup.selectToggle(selectButton);

        canvas.setOnMousePressed(this::onMousePressed);
        canvas.setOnMouseDragged(this::onMouseDragged);
        canvas.setOnMouseReleased(event -> draggedNode = null);

        updateStatus();
        redraw();
    }

    private void onToolChanged(Toggle old, Toggle now) {
        // Never allow a fully deselected toolbar.
        if (now == null) {
            toolGroup.selectToggle(old);
            return;
        }
        tool = (Tool) now.getUserData();
        pendingEdgeNode = null;
        highlightedNodeId = null;
        updateStatus();
        redraw();
    }

    private void onMousePressed(MouseEvent event) {
        // Ignore secondary/middle clicks so they can't create elements by accident.
        if (event.getButton() != MouseButton.PRIMARY) {
            return;
        }
        double x = event.getX();
        double y = event.getY();
        switch (tool) {
            case NODE -> addNodeOnEmptySpace(x, y);
            case EDGE -> handleEdgeClick(x, y);
            case SIGNAL -> handleSignalClick(x, y);
            case SELECT -> {
                draggedNode = findNodeAt(x, y).orElse(null);
                highlightedNodeId = draggedNode != null ? draggedNode.id() : null;
            }
        }
        updateStatus();
        redraw();
    }

    private void onMouseDragged(MouseEvent event) {
        if (tool == Tool.SELECT && draggedNode != null) {
            draggedNode.moveTo(event.getX(), event.getY());
            redraw();
        }
    }

    private void addNodeOnEmptySpace(double x, double y) {
        // Per the editing model, the NODE tool only adds nodes on empty space —
        // clicking an existing node must not stack an overlapping duplicate.
        if (findNodeAt(x, y).isEmpty()) {
            network.addNode(x, y);
        }
    }

    private void handleEdgeClick(double x, double y) {
        Optional<TrackNode> hit = findNodeAt(x, y);
        if (hit.isEmpty()) {
            pendingEdgeNode = null;
            return;
        }
        TrackNode node = hit.get();
        if (pendingEdgeNode == null) {
            pendingEdgeNode = node;
        } else if (!pendingEdgeNode.id().equals(node.id())) {
            network.addEdge(pendingEdgeNode.id(), node.id());
            pendingEdgeNode = null;
        }
    }

    private void handleSignalClick(double x, double y) {
        Vec2 p = new Vec2(x, y);
        TrackEdge closestEdge = null;
        double closestDistance = EDGE_HIT_DISTANCE;
        double closestParam = 0.0;

        for (TrackEdge edge : network.edges()) {
            TrackNode from = network.node(edge.fromNodeId()).orElse(null);
            TrackNode to = network.node(edge.toNodeId()).orElse(null);
            if (from == null || to == null) {
                continue;
            }
            Vec2 a = new Vec2(from.x(), from.y());
            Vec2 b = new Vec2(to.x(), to.y());
            double distance = Geometry.distanceToSegment(p, a, b);
            if (distance <= closestDistance) {
                closestDistance = distance;
                closestParam = Geometry.nearestParam(p, a, b);
                closestEdge = edge;
            }
        }

        if (closestEdge != null) {
            network.addSignal(closestEdge.id(), closestParam, Side.RIGHT);
        }
    }

    private Optional<TrackNode> findNodeAt(double x, double y) {
        TrackNode closest = null;
        double closestDistance = NODE_HIT_RADIUS;
        for (TrackNode node : network.nodes()) {
            double distance = Math.hypot(node.x() - x, node.y() - y);
            if (distance <= closestDistance) {
                closestDistance = distance;
                closest = node;
            }
        }
        return Optional.ofNullable(closest);
    }

    @FXML
    private void onLoadSample() {
        network = SampleNetworks.switzerland();
        resetInteraction();
    }

    @FXML
    private void onClear() {
        network.clear();
        resetInteraction();
    }

    private void resetInteraction() {
        pendingEdgeNode = null;
        draggedNode = null;
        highlightedNodeId = null;
        updateStatus();
        redraw();
    }

    private void redraw() {
        renderer.render(network, canvas.getWidth(), canvas.getHeight(),
                highlightedNodeId,
                pendingEdgeNode != null ? pendingEdgeNode.id() : null);
    }

    private void updateStatus() {
        String hint = switch (tool) {
            case SELECT -> "Auswahl: Knoten anklicken und ziehen, um ihn zu verschieben.";
            case NODE -> "Knoten: In die Fläche klicken, um einen Knoten zu setzen.";
            case EDGE -> pendingEdgeNode == null
                    ? "Kante: Startknoten anklicken."
                    : "Kante: Zielknoten anklicken (Start: " + pendingEdgeNode.label() + ").";
            case SIGNAL -> "Signal: Auf eine Gleiskante klicken, um ein Signal zu setzen.";
        };
        statusLabel.setText(String.format("%s   |   Knoten: %d  Kanten: %d  Signale: %d",
                hint, network.nodes().size(), network.edges().size(), network.signals().size()));
    }
}
