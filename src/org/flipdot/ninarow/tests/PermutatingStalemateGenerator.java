package org.flipdot.ninarow.tests;

import org.flipdot.ninarow.core.Board;
import org.flipdot.ninarow.core.Player;
import org.flipdot.ninarow.core.Step;
import org.flipdot.ninarow.games.FastFourInARow4x4x4x4;

import java.util.Random;

/**
 * <code>
 * X O O O   O O X O   O O X O   O X X X
 * O O O X   X O O O   X X X O   X O O O
 * O X X X   O X X X   X X O X   O O X O
 * O O O X   O O X O   X X X O   O O X O
 * <p>
 * O X O O   X X X O   X X X O   X O X X
 * X X X O   O O X O   X X X O   X X O X
 * X X X O   O O O X   O X O O   O X O O
 * X O X X   O O X O   X O X X   X X O X
 * <p>
 * O X X X   O O X O   X X O X   O X X X
 * O X O O   X X O X   X O X X   X X O X
 * X O O O   O X X X   O O X O   O O X O
 * O X O O   O X X X   X O X X   O X X X
 * <p>
 * O X O O   O O O X   X X X O   O X O O
 * O X O O   X O O O   O X O O   O X X X
 * X X X O   X X X O   O X O O   X O X X
 * O O O X   X O O O   O O O X   O X X X
 * </code>
 * <p>
 * Created by anselm on 02.05.17.
 */
public class PermutatingStalemateGenerator {
    PassivePlayer a = new PassivePlayer("X"), b = new PassivePlayer("O");
    FastFourInARow4x4x4x4 ttt = new FastFourInARow4x4x4x4(a, b);
    Player[] stones = ttt.getStones();
    int[] bases = ttt.getBases();
    int c = 0;
    Random r = new Random();
    int aCount = 0;
    static final boolean verbose = false;

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            new PermutatingStalemateGenerator();
        }
        System.out.println(System.currentTimeMillis() - start);
    }

    public PermutatingStalemateGenerator() {
//        int[] chars = nearly.replaceAll("\\W+", "").chars().toArray();
//        for (int i = 0; i < 256; i++) {
//            if (chars[i] == 'X') {
//                stones[i] = a;
//            } else {
//                stones[i] = b;
//            }
//        }
        try {
            fill();
            if (verbose)
                System.out.println(ttt);
            if (verbose)
                System.out.println(countAllRows());
            int rows = countAllRows();
            int unchanged = 0;
            do {
                for (int j = 0; j < unchanged; j++) {
                    stones[r.nextInt(256)] = r.nextBoolean() ? a : b;
                }
                for (int i = 0; i < 10; i++) {
                    stones[r.nextInt(256)] = r.nextBoolean() ? a : b;
                    permute(1000);
                }
                int newRows = countAllRows();
                unchanged++;
                if (newRows != rows) {
                    rows = newRows;
                    if (newRows < rows)
                        unchanged = 0;
//                    System.out.println(ttt);
                    if (verbose)
                        System.out.println(rows);
                }
                if (verbose)
                    System.out.println(rows + " " + c++ + "   " + unchanged + " " + ttt.toString().chars().filter(c -> c == 'X').count());
            } while (rows != 0);
            if (verbose)
                System.out.println(ttt);
            if (verbose)
                System.out.println(ttt.toString().chars().filter(c -> c == 'X').count());
            aCount = 0;
            for (int n = 0; n < 256; n++) {
                if (stones[n] == a)
                    aCount++;
            }
            errCorrLoop:
            if (aCount != 128) {
                Player toMuch = aCount > 128 ? a : b, toFew = aCount < 128 ? a : b;
                int err = aCount > 128 ? aCount - 128 : 128 - aCount;
                for (int i = 0; i < 10000; i++) {
                    int n = r.nextInt(256);
                    if (stones[n] == toMuch) {
                        stones[n] = toFew;
                        if (countRows(n) == 0) {
                            err--;
                        } else {
                            stones[n] = toMuch;
                        }
                    }
                    if (err == 0)
                        break errCorrLoop;
                }
                throw new Error();
            }
            System.out.println(ttt);
            System.out.println(ttt.toString().chars().filter(c -> c == 'X').count());
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            System.out.println(ttt);
        }
    }

    private void permute(int count) {
        for (int i = 0; i < count; i++) {
            int n = r.nextInt(256);
            Player p = stones[n], other;
            if (p == a) {
                other = b;
            } else {
                other = a;
            }
            int oldRowCount = countRows(n);
            stones[n] = other;
            int newRowCount = countRows(n);
            if (oldRowCount < newRowCount) {
                stones[n] = p;
            }
//            if (oldRowCount == newRowCount) {
//                if (r.nextBoolean()) {
//                    if (p.name.equals("X") && aCount < 128) {
//                        stones[n] = p;
//                    }
//                }
//            }
        }
    }

    void fill() {
        for (int n = 0; n < 256; n++) {
            if (r.nextBoolean()) {
                stones[n] = a;
            } else {
                stones[n] = b;
            }
        }
    }

    int countAllRows() {
        int r = 0;
        for (int i = 0; i < 256; i++) {
            r += countRows(i);
        }
        return r / 4;
    }

//    void testRec(int n) {
//        if (n > mdepth) {
//            mdepth = n;
//            System.out.println(ttt.toString());
//        } else if (c++ % 100000000 == 0) {
//            System.out.println("=======================================");
//            System.out.println(ttt.toString());
//        }
//        if (r.nextBoolean()) {
//            stones[n] = a;
//            if (!hasWinner(n))
//                testRec(n + 1);
//            stones[n] = b;
//            if (!hasWinner(n))
//                testRec(n + 1);
//        } else {
//            stones[n] = b;
//            if (!hasWinner(n))
//                testRec(n + 1);
//            stones[n] = a;
//            if (!hasWinner(n))
//                testRec(n + 1);
//        }
//        stones[n] = null;
//    }

    public int countRows(int n) {
        int rows = 0;
        Player p = stones[n];
        outer:
        for (int base : bases) {
            for (int i = 1; i < 4; i++)
                if (stones[((n + i * (base & 0x03)) & 0x03)
                        | ((n + i * (base & 0x0C)) & 0x0C)
                        | ((n + i * (base & 0x30)) & 0x30)
                        | ((n + i * (base & 0xC0)) & 0xC0)] != p)
                    continue outer;
            rows++;
//            return true;
        }
        return rows;
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
