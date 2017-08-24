package org.flipdot.ninarow.games;

import org.flipdot.ninarow.core.*;
import org.flipdot.ninarow.player.RandomPlayer;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by anselm on 12.04.17.
 */
public class TorusSurface extends TwoPlayersBoard<Node2D> {
    protected int[] dimensions;


    public static void main(String[] args) {
        TorusSurface ts = new TorusSurface(new int[]{3, 3}, new boolean[]{true, true}, new int[][]{{0, 1}, {1, 0}, {-1, 1}, {1, 1}}, new RandomPlayer("O"), new RandomPlayer("X"), 3);
        Node2D.Printer p = new Node2D.Printer(ts.nodes);
        p.print();
        System.out.println("Winner: " + ts.playFullGame());
        p.print();
    }

    public TorusSurface(int[] dimensions, boolean closed, Player a, Player b) {
        this(dimensions, genBooleanArr(dimensions.length, closed), a, b, Arrays.stream(dimensions).min().getAsInt());
    }

    private static boolean[] genBooleanArr(int size, boolean val) {
        boolean[] ret = new boolean[size];
        Arrays.fill(ret, val);
        return ret;
    }

    public TorusSurface(int[] dimensions, boolean[] closed, Player a, Player b) {
        this(dimensions, closed, a, b, Arrays.stream(dimensions).min().getAsInt());
    }

    public TorusSurface(int[] dimensions, boolean[] closed, Player a, Player b, int rowLen) {
        this(dimensions, closed, genAllBaseVectors(dimensions.length), a, b, rowLen);
    }

    public static int[][] genAllBaseVectors(int dimensions) {
        int numVs = (int) (Math.pow(3, dimensions)-1)/2;
        int[][] v = new int[3*(numVs/2)][dimensions];
        for (int i = 1; i < 3*(numVs/2)+1; i++) {
            int id = i;
            for (int j = 0; j < dimensions; j++) {
                v[i - 1][j] = id % 3 == 2 ? -1 : id % 3;
                id /= 3;
            }
        }
        int[][] nonDuplicated = new int[numVs][dimensions];
        int j = 0;
        outer:
        for (int i = 0; i < 3*(numVs/2); i++) {
            middle:
            for (int k = 0; k < i; k++) {
                for (int l = 0; l < dimensions; l++)
                    if (v[i][l] != -v[k][l])
                        continue middle;
                continue outer;
            }
            nonDuplicated[j++] = v[i];
        }
        assert j == numVs;
        return nonDuplicated;
    }


    public TorusSurface(int[] dimensions, boolean[] closed, int[][] baseVectors, Player a, Player b, int rowLen) {
        super(a, b, new HashSet<>(Arrays.asList(genPattern(a, rowLen), genPattern(b, rowLen))));
        this.dimensions = dimensions;
        int count = multiplyTo(dimensions, dimensions.length - 1);
        Node2D[] ns = new Node2D[count];
        for (int i = 0; i < count; i++) {
            int[] coords = id2Coords(i);
            ns[i] = new Node2D(get2DNodeCoords(coords));
            nodes.add(ns[i]);
        }

        for (int id = 0; id < count; id++) {
            vectors:
            for (int[] base : baseVectors) {
                assert base.length == dimensions.length;
                int[] coordsTo = id2Coords(id), beforeCoords = id2Coords(id), afterCoords = id2Coords(id);
                for (int j = 0; j < dimensions.length; j++) {
                    coordsTo[j] += base[j];
                    if ((coordsTo[j] < 0 || coordsTo[j] >= dimensions[j])) {
                        if (closed[j])
                            coordsTo[j] = (coordsTo[j] + dimensions[j]) % dimensions[j];
                        else continue vectors;
                    }
                    beforeCoords[j] = (beforeCoords[j] - base[j] + dimensions[j]) % dimensions[j];
                    afterCoords[j] = (afterCoords[j] + 2 * base[j] + dimensions[j]) % dimensions[j];
                }
                int idTo = coords2id(coordsTo);
                SymmetricEdge newEdge = new SymmetricEdge(ns[id], ns[idTo]);
                int before = coords2id(beforeCoords), after = coords2id(afterCoords);
                ns[id].edges.stream().filter(e -> e.from == ns[before] || e.to == ns[before])
                        .forEach(newEdge::addInSameDirection);
                ns[idTo].edges.stream().filter(e -> e.from == ns[after] || e.to == ns[after])
                        .forEach(newEdge::addInSameDirection);
            }
        }
    }

    private int coords2id(int[] coords) {
        int id = 0;
        for (int i = coords.length - 1; i >= 0; i--) {
            id *= dimensions[i];
            id += coords[i];
        }
        return id;
    }

    protected int[] id2Coords(int id) {
        int[] coords = new int[dimensions.length];
        for (int i = 0; i < coords.length; i++) {
            coords[i] = id % dimensions[i];
            id /= dimensions[i];
        }
        assert id == 0;
        return coords;
    }

    protected int[] get2DNodeCoords(int[] nDcoords) {
        int bordersize = 1;
        int x = 0, y = 0, xStepSize = 1, yStepSize = 1;
        for (int i = 0; i < nDcoords.length; i++) {
            if (i % 2 == 0) {
                x += nDcoords[i] * xStepSize;
                xStepSize *= dimensions[i];
                xStepSize += bordersize;
            } else {
                y += nDcoords[i] * yStepSize;
                yStepSize *= dimensions[i];
                yStepSize += bordersize;
            }
        }
        return new int[]{x, y};
    }

    protected static int multiplyTo(int[] list, int to) {
        return to < 0 ? 1 : list[to] * multiplyTo(list, to - 1);
    }

    private static Pattern genPattern(Player player, int len) {
        return new RegexPattern(genPatternString(player, len), player);
    }

    private static String genPatternString(Player player, int len) {
        return len <= 0 ? "" : player.name + genPatternString(player, len - 1);
    }

    @Override
    public String toString() {
        return new Node2D.Printer(getNodes()).toString();
    }
}
