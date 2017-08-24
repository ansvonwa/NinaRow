package org.flipdot.ninarow.games;

import org.flipdot.ninarow.core.*;
import org.flipdot.ninarow.player.RandomPlayer;
import org.flipdot.ninarow.player.ThreeInARowSeeker;

import java.util.*;

/*

FIXME :
Beginner: O
n=115,base=81
Winner: O
_ X _ O   _ _ _ _   _ _ O _   _ _ _ _
_ X _ _   _ X _ _   _ X _ _   O _ _ _
_ _ X _   _ O _ O   _ _ _ _   _ _ X _
_ _ O _   _ X _ X   _ _ _ _   _ X _ _

_ O _ _   X O _ _   _ X _ _   _ _ X O
_ _ O _   _ _ _ _   _ _ _ _   _ O _ _
X _ _ _   _ _ _ _   _ O _ _   _ _ _ _
_ _ _ _   O _ _ O   _ _ _ _   _ _ _ _

O _ _ _   _ _ _ _   X _ _ O   _ X O _
_ X _ _   X _ _ _   _ _ X _   _ _ X _
_ _ _ _   _ _ _ _   O X _ _   _ _ _ _
_ _ _ _   _ _ _ _   _ _ _ _   _ O O _

_ X X _   _ O _ _   _ X _ _   _ _ _ _
_ _ _ _   _ _ _ _   _ _ _ _   _ _ O _
_ _ X _   _ _ _ O   _ X X _   _ _ O _
O O _ _   _ _ _ _   _ _ _ O   _ _ _ _


_ X _ O   _ _ _ _   _ _ O _   _ _ _ _
_ X _ _   _ X _ _   _ X _ _   W _ _ _
_ _ X _   _ O _ W   _ _ _ _   _ _ X _
_ _ W _   _ X _ X   _ _ _ _   _ X _ _

_ O _ _   X W _ _   _ X _ _   _ _ X O
_ _ O _   _ _ _ _   _ _ _ _   _ O _ _
X _ _ _   _ _ _ _   _ O _ _   _ _ _ _
_ _ _ _   O _ _ O   _ _ _ _   _ _ _ _

O _ _ _   _ _ _ _   X _ _ O   _ X O _
_ X _ _   X _ _ _   _ _ X _   _ _ X _
_ _ _ _   _ _ _ _   O X _ _   _ _ _ _
_ _ _ _   _ _ _ _   _ _ _ _   _ O O _

_ X X _   _ O _ _   _ X _ _   _ _ _ _
_ _ _ _   _ _ _ _   _ _ _ _   _ _ O _
_ _ X _   _ _ _ O   _ X X _   _ _ O _
O O _ _   _ _ _ _   _ _ _ O   _ _ _ _


FIXED: replaced (a) with (b)

(a)
(((n & 0x03) + i * base) & 0x03)
| (((n & 0x0C) + i * base) & 0x0C)
| (((n & 0x30) + i * base) & 0x30)
| (((n & 0xC0) + i * base) & 0xC0)

(b)
(((n & 0x03) + i * (base & 0x03)) & 0x03)
| (((n & 0x0C) + i * (base & 0x0C)) & 0x0C)
| (((n & 0x30) + i * (base & 0x30)) & 0x30)
| (((n & 0xC0) + i * (base & 0xC0)) & 0xC0)
 */

public class FastFourInARow4x4x4x4 extends FourInARow4x4x4x4 {
    private final Player[] stones = new Player[4 * 4 * 4 * 4];
    private final IntNode[] nodeArr = new IntNode[4 * 4 * 4 * 4];
    private final int[] bases = new int[40];
    private static Random r = new Random();

    public static void main(String[] args) {
        FastFourInARow4x4x4x4 ttt = new FastFourInARow4x4x4x4(new FastRandomPlayer("X"), new ThreeInARowSeeker("O"));
        System.out.println("Beginner: " + (ttt.curPlayerIsA ? "X" : "O"));
        Player w = ttt.playFullGame();
        System.out.println("Winner: " + w);
        Node2D.Printer p = new Node2D.Printer(ttt.getNodes());
        p.print();
        boolean[] wss = ttt.getWinnerStones();
//        System.out.println(Arrays.toString(wss));
        System.out.println('\n');
        for (int i = 0; i < 256; i++) {
            if (wss[i])
                ttt.stones[i] = new RandomPlayer("W");
        }
        p = new Node2D.Printer(ttt.getNodes());
        p.print();
//        ttt.nodes.stream().flatMap(n -> ttt.getMatches(new org.flipdot.ninarow.core.PlaceStoneStep(n)).stream())
//                .filter(Objects::nonNull).findAny().ifPresent(p::printHighlighted);
    }

    public FastFourInARow4x4x4x4(Player a, Player b) {
        super(a, b);
        int[][] baseVectors = genAllBaseVectors(4);
        for (int i = 0; i < bases.length; i++) {
            for (int j = 0; j < 4; j++) {
                bases[i] += ((baseVectors[i][j] + 4) % 4) << 2 * j;
            }
        }
        nodes.clear();
        for (int i = 0; i < 256; i++) {
            nodeArr[i] = new IntNode(i);
            nodes.add(nodeArr[i]);
        }
    }

    public static class FastRandomPlayer extends RandomPlayer<FastFourInARow4x4x4x4, Node2D> {
        public FastRandomPlayer(String name) {
            super(name);
        }

        @Override
        public Step step(FastFourInARow4x4x4x4 board) {
            int n;
            do {
                n = r.nextInt(256);
            } while (board.stones[n] != null);
            return new PlaceStoneStep(n);
        }
    }

    public static class PlaceStoneStep extends org.flipdot.ninarow.core.PlaceStoneStep {
        final int node;

        public PlaceStoneStep(int node) {
            super(null);
            this.node = node;
        }
    }

    private class IntNode extends Node2D {
        final int n;

        public IntNode(int n) {
            super(get2DNodeCoords(new int[]{n % 4, (n / 4) % 4, (n / 4 / 4) % 4, (n / 4 / 4 / 4)})[0],
                    get2DNodeCoords(new int[]{n % 4, (n / 4) % 4, (n / 4 / 4) % 4, (n / 4 / 4 / 4)})[1]);
            this.n = n;
        }
    }

    @Override
    public HashSet<HashSet<Edge>> getRelevantEdges(Step s) {
        throw new Error();
    }

    @Override
    public Set<Node2D> getNodes() {
        for (int i = 0; i < nodeArr.length; i++) {
            nodeArr[i].stones.clear();
            if (stones[i] != null)
                nodeArr[i].stones.add(stones[i]);
        }
        return new HashSet<>(Arrays.asList(nodeArr));
    }

    @Override
    public Set<Pattern.Match> getMatches(Step s) {
        boolean[] winnerStones = getWinnerStones();
        return new HashSet(Arrays.asList(new Pattern() {
            @Override
            public Match matches(List<Node> nodes) {
                nodes = new ArrayList();
                for (int i = 0; i < 256; i++) {
                    if (winnerStones[i])
                        nodes.add(nodeArr[i]);
                }
                return new Match(nodes);
            }
        }.matches(null)));
//        throw new Error();
    }

    @Override
    public Set<Pattern> getMatchingPatterns(Step s) {
        throw new Error();
    }

    @Override
    protected void apply(Player p, Step s) {
        if (s instanceof PlaceStoneStep) {
            if (stones[((PlaceStoneStep) s).node] == null) {
                stones[((PlaceStoneStep) s).node] = p;
            } else throw new Error();
        } else if (s instanceof org.flipdot.ninarow.core.PlaceStoneStep) {
            if (stones[((IntNode) ((org.flipdot.ninarow.core.PlaceStoneStep) s).node).n] == null) {
                stones[((IntNode) ((org.flipdot.ninarow.core.PlaceStoneStep) s).node).n] = p;
            } else throw new Error();
        } else throw new Error();
    }

    @Override
    public Player getWinner(Step s) {
        int n;
        if (s instanceof PlaceStoneStep)
            n = ((PlaceStoneStep) s).node;
        else if (s instanceof org.flipdot.ninarow.core.PlaceStoneStep)
            n = ((IntNode) ((org.flipdot.ninarow.core.PlaceStoneStep) s).node).n;
        else throw new Error();
        Player p = stones[n];
        outer:
        for (int base : bases) {
            for (int i = 1; i < 4; i++)
                if (stones[((n + i * (base & 0x03)) & 0x03)
                        | ((n + i * (base & 0x0C)) & 0x0C)
                        | ((n + i * (base & 0x30)) & 0x30)
                        | ((n + i * (base & 0xC0)) & 0xC0)] != p)
                    continue outer;
            return p;
        }
        return null;
    }

    public boolean[] getWinnerStones() {
        boolean[] winnerStones = new boolean[256];
        for (int n = 0; n < 256; n++) {
            Player p = stones[n];
            if (p == null)
                continue;
            outer:
            for (int base : bases) {
                for (int i = 1; i < 4; i++)
                    if (stones[((n + i * (base & 0x03)) & 0x03)
                            | ((n + i * (base & 0x0C)) & 0x0C)
                            | ((n + i * (base & 0x30)) & 0x30)
                            | ((n + i * (base & 0xC0)) & 0xC0)] != p)
                        continue outer;
                for (int i = 0; i < 4; i++)
                    winnerStones[((n + i * (base & 0x03)) & 0x03)
                            | ((n + i * (base & 0x0C)) & 0x0C)
                            | ((n + i * (base & 0x30)) & 0x30)
                            | ((n + i * (base & 0xC0)) & 0xC0)] = true;
                return winnerStones;
            }
        }
        return winnerStones;
    }

    @Override
    public boolean boardFull() {
        for (Player p : stones)
            if (p == null)
                return false;
        return true;
    }

    @Override
    public void clear() {
        for (int i = 0; i < 256; i++) {
            stones[i] = null;
            nodeArr[i].stones.clear();
        }
    }

    public Player[] getStones() {
        return stones;
    }

    public int[] getBases() {
        return bases;
    }
}
