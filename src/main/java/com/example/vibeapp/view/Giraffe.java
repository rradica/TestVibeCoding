package com.example.vibeapp.view;

import com.example.vibeapp.geometry.Vec2;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Draws a small cartoon giraffe on a canvas — the friendly mascot that appears
 * and "draws" newly added elements. Pure rendering: it holds a graphics context
 * and paints on request; a bob {@code phase} gives its head a bit of life. The
 * giraffe faces right, toward the element it is drawing.
 */
final class Giraffe {

    private static final Color COAT = Color.web("#f2c14e");
    private static final Color COAT_DARK = Color.web("#e0a92e");
    private static final Color SPOT = Color.web("#a86a2c");
    private static final Color HOOF = Color.web("#5b4327");
    private static final Color MANE = Color.web("#b5651d");
    private static final Color EYE = Color.web("#2b2118");

    private final GraphicsContext gc;

    Giraffe(GraphicsContext gc) {
        this.gc = gc;
    }

    /**
     * Draws the giraffe standing on {@code feet} (its bottom-centre point), with
     * {@code phase} (radians) driving a gentle head bob so it looks alive while
     * it is drawing. The snout ends up near {@code feet} + (23, -48).
     */
    void draw(Vec2 feet, double phase) {
        double bob = Math.sin(phase) * 2.0;
        double bodyCy = feet.y() - 26.0;
        drawLegs(feet, bodyCy);
        drawTail(feet.x() - 12.0, bodyCy);
        drawBody(feet.x(), bodyCy);
        drawNeckAndHead(feet.x(), bodyCy, bob);
    }

    private void drawLegs(Vec2 feet, double bodyCy) {
        double top = bodyCy + 6.0;
        double footY = feet.y();
        double[] xs = {feet.x() - 8.0, feet.x() - 3.0, feet.x() + 4.0, feet.x() + 9.0};
        gc.setLineWidth(3.5);
        gc.setStroke(COAT);
        for (double x : xs) {
            gc.strokeLine(x, top, x, footY);
        }
        gc.setStroke(HOOF);
        for (double x : xs) {
            gc.strokeLine(x, footY - 2.5, x, footY);
        }
    }

    private void drawBody(double cx, double cy) {
        gc.setFill(COAT);
        gc.fillOval(cx - 13.0, cy - 8.0, 26.0, 16.0);
        gc.setFill(SPOT);
        gc.fillOval(cx - 8.0, cy - 4.0, 4.0, 4.0);
        gc.fillOval(cx - 1.0, cy - 1.0, 5.0, 5.0);
        gc.fillOval(cx + 5.0, cy - 5.0, 4.0, 4.0);
        gc.fillOval(cx + 3.0, cy + 2.0, 3.5, 3.5);
    }

    private void drawTail(double x, double cy) {
        gc.setStroke(COAT_DARK);
        gc.setLineWidth(1.5);
        gc.strokeLine(x, cy, x - 5.0, cy + 8.0);
        gc.setFill(MANE);
        gc.fillOval(x - 7.0, cy + 7.0, 3.5, 4.5);
    }

    private void drawNeckAndHead(double cx, double cy, double bob) {
        double neckBaseX = cx + 7.0;
        double neckBaseY = cy - 6.0;
        double headX = cx + 15.0 + bob;
        double headY = cy - 24.0 + bob * 0.5;
        gc.setStroke(COAT);
        gc.setLineWidth(6.0);
        gc.strokeLine(neckBaseX, neckBaseY, headX, headY + 4.0);
        gc.setStroke(MANE);
        gc.setLineWidth(2.0);
        gc.strokeLine(neckBaseX - 2.0, neckBaseY, headX - 2.0, headY + 4.0);
        drawHead(headX, headY);
    }

    private void drawHead(double x, double y) {
        // Ossicones (the little horns) with knobbed tips.
        gc.setStroke(COAT_DARK);
        gc.setLineWidth(1.5);
        gc.strokeLine(x - 2.0, y - 2.0, x - 3.0, y - 7.0);
        gc.strokeLine(x + 3.0, y - 2.0, x + 3.0, y - 7.0);
        gc.setFill(MANE);
        gc.fillOval(x - 4.5, y - 9.0, 3.0, 3.0);
        gc.fillOval(x + 1.5, y - 9.0, 3.0, 3.0);
        // Head and snout (facing right).
        gc.setFill(COAT);
        gc.fillOval(x - 5.0, y - 3.0, 10.0, 8.0);
        gc.fillOval(x + 2.0, y - 1.0, 8.0, 6.0);
        // Ear on the far side.
        gc.setFill(COAT_DARK);
        gc.fillOval(x - 7.0, y - 3.0, 4.0, 2.5);
        // Eye, nostril and a friendly smile.
        gc.setFill(EYE);
        gc.fillOval(x - 1.0, y - 1.0, 2.0, 2.0);
        gc.fillOval(x + 8.0, y + 1.5, 1.3, 1.3);
        gc.setStroke(EYE);
        gc.setLineWidth(0.8);
        gc.strokeLine(x + 6.0, y + 3.5, x + 9.0, y + 3.0);
    }
}
