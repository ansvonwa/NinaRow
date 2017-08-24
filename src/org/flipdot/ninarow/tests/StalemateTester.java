package org.flipdot.ninarow.tests;

import org.flipdot.ninarow.core.Board;
import org.flipdot.ninarow.core.Player;
import org.flipdot.ninarow.core.Step;
import org.flipdot.ninarow.games.FastFourInARow4x4x4x4;

import java.util.Random;

/**
 * Created by anselm on 02.05.17.
 */
public class StalemateTester {
    PassivePlayer a = new PassivePlayer("X"), b = new PassivePlayer("O");
    FastFourInARow4x4x4x4 ttt = new FastFourInARow4x4x4x4(a, b);
    Player[] stones = ttt.getStones();
    int[] bases = ttt.getBases();
    int c = 0, mdepth = 0;
    Random r = new Random();

    public static void main(String[] args) {
        new StalemateTester();
    }

    public StalemateTester() {
        try {
            testRec(0);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            System.out.println(ttt);
        }
    }

    void testRec(int n) {
        if (n > mdepth) {
            mdepth = n;
            System.out.println(ttt.toString());
        } else if (c++ % 100000000 == 0) {
            System.out.println("=======================================");
            System.out.println(ttt.toString());
        }
        if (r.nextBoolean()) {
            stones[n] = a;
            if (!hasWinner(n))
                testRec(n + 1);
            stones[n] = b;
            if (!hasWinner(n))
                testRec(n + 1);
        } else {
            stones[n] = b;
            if (!hasWinner(n))
                testRec(n + 1);
            stones[n] = a;
            if (!hasWinner(n))
                testRec(n + 1);
        }
        stones[n] = null;
    }

    public boolean hasWinner(int n) {
        Player p = stones[n];
        outer:
        for (int base : bases) {
            for (int i = 1; i < 4; i++)
                if (stones[((n + i * (base & 0x03)) & 0x03)
                        | ((n + i * (base & 0x0C)) & 0x0C)
                        | ((n + i * (base & 0x30)) & 0x30)
                        | ((n + i * (base & 0xC0)) & 0xC0)] != p)
                    continue outer;
            return true;
        }
        return false;
    }


    static class PassivePlayer extends Player {

        public PassivePlayer(String name) {
            super(name);
        }

        @Override
        public Step step(Board board) {
            throw new Error();
        }
    }
}
