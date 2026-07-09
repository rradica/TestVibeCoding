package com.example.vibeapp.model;

/**
 * Side of a track edge, relative to its direction (from-node &rarr; to-node).
 * Used to place edge-referenced fachdaten such as signals.
 */
public enum Side {
    LEFT,
    RIGHT;

    /** The opposite side, e.g. for toggling a signal from right to left. */
    public Side opposite() {
        return this == LEFT ? RIGHT : LEFT;
    }
}
