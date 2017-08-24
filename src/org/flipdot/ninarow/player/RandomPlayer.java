package org.flipdot.ninarow.player;

import org.flipdot.ninarow.core.*;

import java.util.ArrayList;

/**
 * Created by anselm on 29.03.17.
 */
public class RandomPlayer<B extends Board<N>, N extends Node> extends Player<B, N> {

    public RandomPlayer(String name) {
        super(name);
    }

    @Override
    public Step step(B board) {
        N n;
        do {
            ArrayList<N> l = new ArrayList<>(board.getNodes());
            n = l.get((int) (Math.random() * l.size()));
        } while (!n.stones.isEmpty());
        return new PlaceStoneStep<>(n);
    }
}
