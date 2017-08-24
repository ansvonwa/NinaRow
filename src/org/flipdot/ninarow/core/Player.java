package org.flipdot.ninarow.core;

/**
 * Created by anselm on 29.03.17.
 */
public abstract class Player<B extends Board<N>, N extends Node> {
    public final String name;

    public Player(String name) {
        this.name = name;
    }

    public abstract Step step(B board);

    @Override
    public String toString() {
        return name;
    }
}
