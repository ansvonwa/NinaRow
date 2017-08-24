package org.flipdot.ninarow.tests;

import org.flipdot.ninarow.core.Player;
import org.flipdot.ninarow.core.TwoPlayersBoard;
import org.flipdot.ninarow.games.FastFourInARow4x4x4x4;
import org.flipdot.ninarow.player.AvoidWinningPlayer;
import org.flipdot.ninarow.player.RandomPlayer;
import org.flipdot.ninarow.player.ThreeInARowSeeker;
import org.flipdot.ninarow.player.TwoAndThreeInARowSeeker;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * Created by anselm on 27.04.17.
 */

public class TestAllPlayers {

    /*

        100000         RandomPlayer   ThreeInARowSee TwoAndThreeInA
        RandomPlayer   49.8           0.0            0.0
        ThreeInARowSee 99.9           49.9           10.1
        TwoAndThreeInA 100.0          89.8           50.0


        RandomPlayer vs RandomPlayer: 5007:4993
        RandomPlayer vs ThreeInARowSeeker: 9:9991
        RandomPlayer vs TwoAndThreeInARowSeeker: 0:10000
        RandomPlayer vs AvoidWinningPlayer: 10000:0
        ThreeInARowSeeker vs ThreeInARowSeeker: 5035:4965
        ThreeInARowSeeker vs TwoAndThreeInARowSeeker: 1033:8967
        ThreeInARowSeeker vs AvoidWinningPlayer: 10000:0
        TwoAndThreeInARowSeeker vs TwoAndThreeInARowSeeker: 4912:5088
        TwoAndThreeInARowSeeker vs AvoidWinningPlayer: 10000:0
        AvoidWinningPlayer vs AvoidWinningPlayer: 5013:4985

        10000          RandomPlayer   ThreeInARowSee TwoAndThreeInA AvoidWinningPl
        RandomPlayer   49.93          0.09           0.0            100.0
        ThreeInARowSee 99.91          49.65          10.33          100.0
        TwoAndThreeInA 100.0          89.67          50.88          100.0
        AvoidWinningPl 0.0            0.0            0.0            49.85
     */

    static List<Function<String, Player>> supplier = Arrays.asList(
            (String n) -> new RandomPlayer(n),
            (String n) -> new ThreeInARowSeeker(n),
            (String n) -> new TwoAndThreeInARowSeeker(n),
            (String n) -> new AvoidWinningPlayer(n)
    );

    public static int[] test(Player a, Player b, int count) {
        System.out.print(a.getClass().getSimpleName() + " vs " + b.getClass().getSimpleName() + ": ");
        TwoPlayersBoard ttt = new FastFourInARow4x4x4x4(a, b);
        int aWins = 0, bWins = 0, games = 0;
        for (int i = 0; i < count; i++) {
            ttt.letABegin(i % 2 == 0);
            games++;
            Player w = ttt.playFullGame();
            if (w == null)
                continue;
            if (a == w)
                aWins++;
            else if (b == w)
                bWins++;
        }
        System.out.println(aWins + ":" + bWins);
        return new int[]{games, aWins, bWins};
    }

    public static int[][] testAll(int count) {
        int size = supplier.size();
        int[][] allResults = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = i; j < size; j++) {
                int[] game = test(supplier.get(i).apply("O"), supplier.get(j).apply("X"), count);
                allResults[i][j] = game[1];
                allResults[j][i] = game[2];
            }
        }
        return allResults;
    }

    public static void testAndPrintAll(int count) {
        int[][] allResults = testAll(count);
        String space = "              ";
        int size = supplier.size();
        System.out.println();
        System.out.print((count + space).substring(0, space.length()) + " ");
        for (int i = 0; i < size; i++) {
            System.out.print((supplier.get(i).apply("X").getClass().getSimpleName() + space).substring(0, space.length()) + " ");
        }
        System.out.println();
        for (int i = 0; i < size; i++) {
            System.out.print((supplier.get(i).apply("X").getClass().getSimpleName() + space).substring(0, space.length()) + " ");
            for (int j = 0; j < size; j++) {
                System.out.print(((allResults[i][j] * 10000 / count / 100.0) + space).substring(0, space.length()) + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
//        test(new RandomPlayer("O"), new ThreeInARowSeeker("X"), 100000);
        testAndPrintAll(1000);
    }
}

