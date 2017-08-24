package org.flipdot.ninarow.core;

/**
 * Created by anselm on 09.04.17.
 */
public abstract class WinningPattern extends Pattern {
    public final Player winner;

    public WinningPattern(Player winner) {
        this.winner = winner;
    }
}
