package org.flipdot.ninarow.core;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by anselm on 29.03.17.
 */
public class PlaceStoneStep<N extends Node> extends Step {
    public final N node;
    public PlaceStoneStep(N node) {
        this.node = node;
    }

    @Override
    public Set<Node> changedNodes() {
        return new HashSet<>(Arrays.asList(node));
    }
}
