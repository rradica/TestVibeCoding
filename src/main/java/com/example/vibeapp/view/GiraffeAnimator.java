package com.example.vibeapp.view;

import java.util.Objects;

import com.example.vibeapp.geometry.Easing;
import com.example.vibeapp.geometry.Vec2;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Plays a short, playful animation whenever a new element is created: a cartoon
 * {@link Giraffe} appears and traces the element into existence, and only once
 * the stroke is complete is the element committed to the model.
 *
 * <p>View-only and owns no domain state. Each frame it lets the caller repaint
 * the network beneath ({@code baseRedraw}) — which does not yet contain the new
 * element — then draws the growing preview and the giraffe on top. The commit
 * runs from {@link #complete()}, so an interrupted animation never loses its
 * element (see {@link #finishNow()}).
 */
final class GiraffeAnimator {

    private static final double DURATION_NANOS = 1_100_000_000.0;
    private static final double BOB_NANOS = 90_000_000.0;
    private static final double FADE = 0.15;
    private static final Color TRACE = Color.web("#f9e2af");

    private final GraphicsContext gc;
    private final Runnable baseRedraw;
    private final Giraffe giraffe;
    private final AnimationTimer timer;

    private DrawTarget target;
    private Runnable onComplete;
    private long startNanos = -1L;

    GiraffeAnimator(GraphicsContext gc, Runnable baseRedraw) {
        this.gc = Objects.requireNonNull(gc, "gc must not be null");
        this.baseRedraw = Objects.requireNonNull(baseRedraw, "baseRedraw must not be null");
        this.giraffe = new Giraffe(gc);
        this.timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                frame(now);
            }
        };
    }

    /**
     * Animates the drawing of {@code target}; once the giraffe finishes,
     * {@code onComplete} runs to commit the element. If another animation is
     * still running it is finished first so its element is not lost.
     */
    void play(DrawTarget target, Runnable onComplete) {
        Objects.requireNonNull(target, "target must not be null");
        Objects.requireNonNull(onComplete, "onComplete must not be null");
        finishNow();
        this.target = target;
        this.onComplete = onComplete;
        this.startNanos = -1L;
        timer.start();
    }

    /** If an animation is in progress, end it immediately and commit its element. */
    void finishNow() {
        if (target != null) {
            complete();
        }
    }

    private void frame(long now) {
        if (startNanos < 0L) {
            startNanos = now;
        }
        double elapsed = now - startNanos;
        double t = Easing.smoothStep(elapsed / DURATION_NANOS);
        baseRedraw.run();
        Vec2 tip = drawGrowingElement(t);
        drawGiraffe(tip, t, elapsed);
        if (elapsed >= DURATION_NANOS) {
            complete();
        }
    }

    private void complete() {
        timer.stop();
        Runnable commit = onComplete;
        target = null;
        onComplete = null;
        startNanos = -1L;
        if (commit != null) {
            commit.run();
        }
    }

    /** Draws the element grown to progress {@code t} and returns the pen tip. */
    private Vec2 drawGrowingElement(double t) {
        return switch (target.kind()) {
            case NODE -> drawGrowingNode(t);
            case EDGE -> drawGrowingEdge(t);
            case SIGNAL -> drawGrowingSignal(t);
        };
    }

    private Vec2 drawGrowingNode(double t) {
        Vec2 c = target.start();
        double r = NetworkRenderer.NODE_RADIUS * t;
        gc.setFill(NetworkRenderer.NODE);
        gc.fillOval(c.x() - r, c.y() - r, 2 * r, 2 * r);
        double ring = NetworkRenderer.NODE_RADIUS + 5.0 * (1.0 - t);
        gc.setStroke(TRACE);
        gc.setLineWidth(2.0);
        gc.strokeOval(c.x() - ring, c.y() - ring, 2 * ring, 2 * ring);
        return c;
    }

    private Vec2 drawGrowingEdge(double t) {
        Vec2 a = target.start();
        Vec2 tip = a.lerp(target.end(), t);
        gc.setStroke(NetworkRenderer.EDGE);
        gc.setLineWidth(2.5);
        gc.strokeLine(a.x(), a.y(), tip.x(), tip.y());
        return tip;
    }

    private Vec2 drawGrowingSignal(double t) {
        Vec2 base = target.start();
        Vec2 tip = base.lerp(target.end(), t);
        gc.setStroke(NetworkRenderer.SIGNAL);
        gc.setLineWidth(1.0);
        gc.strokeLine(base.x(), base.y(), tip.x(), tip.y());
        double r = NetworkRenderer.SIGNAL_RADIUS * t;
        gc.setFill(NetworkRenderer.SIGNAL);
        gc.fillOval(tip.x() - r, tip.y() - r, 2 * r, 2 * r);
        return tip;
    }

    private void drawGiraffe(Vec2 tip, double t, double elapsed) {
        Vec2 feet = giraffeFeet(tip);
        gc.save();
        gc.setGlobalAlpha(fade(t));
        drawPenGuide(feet, tip);
        giraffe.draw(feet, elapsed / BOB_NANOS);
        gc.restore();
    }

    /** Dashed "pen" line from the giraffe's snout to the growing tip, plus a sparkle. */
    private void drawPenGuide(Vec2 feet, Vec2 tip) {
        Vec2 snout = new Vec2(feet.x() + 23.0, feet.y() - 48.0);
        gc.setStroke(TRACE);
        gc.setLineWidth(1.0);
        gc.setLineDashes(3.0, 3.0);
        gc.strokeLine(snout.x(), snout.y(), tip.x(), tip.y());
        gc.setLineDashes(null);
        gc.setFill(TRACE);
        gc.fillOval(tip.x() - 2.0, tip.y() - 2.0, 4.0, 4.0);
    }

    /** Stands the giraffe just left of and below the tip, kept inside the canvas. */
    private Vec2 giraffeFeet(Vec2 tip) {
        double w = gc.getCanvas().getWidth();
        double h = gc.getCanvas().getHeight();
        double x = Math.max(22.0, Math.min(w - 16.0, tip.x() - 30.0));
        double y = Math.max(52.0, Math.min(h - 6.0, tip.y() + 42.0));
        return new Vec2(x, y);
    }

    /** Fades the giraffe in over the first slice of the animation and out over the last. */
    private static double fade(double t) {
        double in = Math.min(1.0, t / FADE);
        double out = Math.min(1.0, (1.0 - t) / FADE);
        return Math.max(0.0, Math.min(in, out));
    }
}
