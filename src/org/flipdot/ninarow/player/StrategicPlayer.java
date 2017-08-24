package org.flipdot.ninarow.player;

import org.flipdot.ninarow.core.Node2D;
import org.flipdot.ninarow.core.Player;
import org.flipdot.ninarow.core.Step;
import org.flipdot.ninarow.games.FastFourInARow4x4x4x4;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by anselm on 29.04.17.
 */
public class StrategicPlayer<B extends FastFourInARow4x4x4x4> extends RandomPlayer<B, Node2D> {
    private static Random r = new Random();

    public StrategicPlayer(String name) {
        super(name);
    }

    @Override
    public Step step(B board) {
        System.out.println("##################################################");
        System.out.println(board);
        Player[] stones = board.getStones();
        int stoneWhereOtherPlayerCouldWin = -1;
        for (int n = 0; n < 256; n++) {
            Player p = stones[n];
            if (p == null || (stoneWhereOtherPlayerCouldWin != -1 && p != this))//(p != this)
                continue;
            outer:
            for (int base : board.getBases()) {
                int c = 1;
                for (int i = 1; i < 4; i++) {
                    Player s = stones[add(n, base, i)];
                    if (s == p)
                        c++;
                    else if (s != null || c == 1)
                        continue outer;
                }
                if (c == 3) {//3 in a row
                    for (int i = 2; i < 4; i++) {
                        int m = add(n, base, i);
                        if (stones[m] == null) {
                            if (p == this)
                                return new FastFourInARow4x4x4x4.PlaceStoneStep(m);
                            else {
                                stoneWhereOtherPlayerCouldWin = m;
                                continue outer;
                            }
                        }
                    }
                }
            }
        }
        if (stoneWhereOtherPlayerCouldWin != -1)
            return new FastFourInARow4x4x4x4.PlaceStoneStep(stoneWhereOtherPlayerCouldWin);

        // master strategy ;)
        int[] pairs = new int[256];
        int pairsCount = 0, ownStonesCount = 0;
        for (int n = 0; n < 256; n++) {
            pairs[n] = -1;
                    Player p = stones[n];
            if (p != this)
                continue;
            ownStonesCount++;
            if (stones[add(n, 0b10101010, 1)] == p) {
                pairsCount++;
                pairs[n] = add(n, 0b10101010, 1);
            }
        }
        assert pairsCount % 2 == 0;
        pairsCount /= 2;
        if (ownStonesCount == 0)
            return super.step(board);
        if (pairsCount == 0) {
            int m;
            while (stones[m = r.nextInt(256)] != this) ;
            return new FastFourInARow4x4x4x4.PlaceStoneStep(add(m, 0b10101010, 1));
        }
        ArrayList<Integer> diagonalElements = new ArrayList<>();
        int[] diagonalDirections = new int[]{0b01010101, 0b01010111, 0b01011101, 0b01011111, 0b01110101, 0b01110111, 0b01111101, 0b01111111};
        if (pairsCount >= 1) {
            for (int n = 0; n < 256; n++) {
                if (pairs[n] == -1)
                    continue;
                for (int i = 0; i < diagonalDirections.length; i++)
                    diagonalElements.add(add(n,diagonalDirections[i],1));
            }
            System.out.println(diagonalElements);
            diagonalElements.forEach(i -> stones[i] = new RandomPlayer("D"));
            System.out.println(board);
            System.exit(42);
        }
        System.out.println("strategy error");
        return super.step(board);
    }

    int add(int n, int base, int times) {
        return ((n + times * (base & 0x03)) & 0x03)
                | ((n + times * (base & 0x0C)) & 0x0C)
                | ((n + times * (base & 0x30)) & 0x30)
                | ((n + times * (base & 0xC0)) & 0xC0);
    }

}
