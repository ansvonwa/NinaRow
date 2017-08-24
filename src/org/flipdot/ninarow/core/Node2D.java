package org.flipdot.ninarow.core;

import java.util.Collection;

/**
 * Created by anselm on 29.03.17.
 */
public class Node2D extends Node {
    public final int x, y;

    public Node2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Node2D(int[] coords) {
        assert coords.length == 2;
        this.x = coords[0];
        this.y = coords[1];
    }

    @Override
    public String toString() {
        return "Node[" + x + "," + y + "]";
    }

    public static class Printer {
        final Collection<Node2D> nodes;
        final String[][] fields;

        public Printer(Collection<Node2D> nodes) {
            this.nodes = nodes;
            this.fields = new String[1 + nodes.stream().mapToInt(n -> n.x).max().getAsInt()]
                    [1 + nodes.stream().mapToInt(n -> n.y).max().getAsInt()];
            for (int i = 0; i < fields.length; i++)
                for (int j = 0; j < fields[0].length; j++)
                    fields[i][j] = " ";
        }

        private void updatefields() {
            for (Node2D n : nodes)
                fields[n.x][n.y] = n.stones.isEmpty() ? "_" :
                        n.stones.size() == 1 ? n.stones.iterator().next().name : n.stones.toString();
        }

        public void print() {
            updatefields();
            for (int i = 0; i < fields[0].length; i++) {
                System.out.print(fields[0][i]);
                for (int j = 1; j < fields.length; j++) {
                    System.out.print(" " + fields[j][i]);
                }
                System.out.println();
            }
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            updatefields();
            for (int i = 0; i < fields[0].length; i++) {
                sb.append(fields[0][i]);
                for (int j = 1; j < fields.length; j++) {
                    sb.append(" " + fields[j][i]);
                }
                sb.append('\n');
            }
            return sb.toString();
        }

        public void printHighlighted(Pattern.Match highlight) {
            for (Node2D n : nodes)
                fields[n.x][n.y] = n.stones.isEmpty() ? "_" :
                        n.stones.size() == 1 ? (
                                highlight.getNodes().contains(n) ?
                                        n.stones.iterator().next().name.toUpperCase() :
                                        n.stones.iterator().next().name.toLowerCase()
                        ) : n.stones.toString();

            for (int i = 0; i < fields[0].length; i++) {
                System.out.print(fields[0][i]);
                for (int j = 1; j < fields.length; j++) {
                    System.out.print(" " + fields[j][i]);
                }
                System.out.println();
            }
        }

    }
}
