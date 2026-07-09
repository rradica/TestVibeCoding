package com.example.vibeapp.view;

import com.example.vibeapp.geometry.Vec2;

/**
 * Geometry of a freshly created element, handed to the {@link GiraffeAnimator}
 * so the giraffe can trace it into existence.
 *
 * <p>{@code start} and {@code end} bound the stroke that gets drawn: a
 * {@link Kind#NODE} uses the same point for both, a {@link Kind#EDGE} spans its
 * two nodes, and a {@link Kind#SIGNAL} runs from its base on the edge to its
 * offset marker.
 */
record DrawTarget(Kind kind, Vec2 start, Vec2 end) {

    /** The kind of element being drawn, which selects how it is animated. */
    enum Kind { NODE, EDGE, SIGNAL }
}
