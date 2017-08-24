package org.flipdot.ninarow.core;

import java.util.Arrays;
import java.util.Random;
import java.util.Set;

/**
 * Created by anselm on 11.04.17.
 */
public abstract class TwoPlayersBoard<N extends Node> extends Board<N> {
    private static Random r = new Random();
    protected boolean curPlayerIsA = r.nextBoolean();
    public final Player a, b;

    protected TwoPlayersBoard(Player a, Player b, Set<Pattern> patterns) {
        super(Arrays.asList(a, b), patterns);
        this.a = a;
        this.b = b;
    }

    public Player playFullGame() {
        while (!boardFull()) {
            Player w = getWinner(step());
            if (w != null)
                return w;
        }
        return null;
    }

    public Step step() {
        Player p;
        if (curPlayerIsA)
            p = a;
        else
            p = b;
        Step s = p.step(this);
        try{
            apply(p, s);
        } catch (Error e) {
            e.printStackTrace();
            System.err.println("try again");
            s = p.step(this);
            apply(p, s);
        }
        curPlayerIsA = !curPlayerIsA;
        return s;
    }

    protected void apply(Player p, Step s) {
        if (s instanceof PlaceStoneStep) {
            if (((PlaceStoneStep) s).node.stones.isEmpty()) {
                ((PlaceStoneStep) s).node.stones.add(p);
            } else throw new Error();
        } else throw new Error();
    }

    public boolean boardFull() {
        return !nodes.stream().filter(n -> n.stones.isEmpty()).findAny().isPresent();
    }

    public boolean isCurPlayerA() {
        return curPlayerIsA;
    }

    @Override
    public void clear() {
        super.clear();
        curPlayerIsA = r.nextBoolean();
    }

    public void letABegin(boolean letABegin) {
        clear();
        curPlayerIsA = letABegin;
    }
}
