package org.flipdot.ninarow.core;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by anselm on 29.03.17.
 */
public abstract class Board<N extends Node> {
    protected final HashSet<N> nodes = new HashSet<>();
    protected final List<Player> players;
    protected final Set<Pattern> patterns;

    protected Board(List<Player> players, Set<Pattern> patterns) {
        this.players = players;
        this.patterns = patterns;
    }

    public Set<N> getNodes() {
        return new HashSet<>(nodes);
    }

    public Player getWinner(Step s) {
        return getMatchingPatterns(s).stream().filter(p -> p instanceof WinningPattern)
                .map(p -> ((WinningPattern) p).winner).findAny().orElseGet(() -> null);
    }

    public Set<Pattern> getMatchingPatterns(Step s) {
        HashSet<HashSet<Edge>> edgeSets = getRelevantEdges(s);
        Stream<List<Node>> lists =
                edgeSets.stream()
                        .flatMap(edgeSet -> edgeSet.stream().map(e -> {
                            ArrayList<Node> nodeList = new ArrayList<>();
                            nodeList.add(e.from);
                            Optional<Edge> next = Optional.of(e);
                            while (next.isPresent() && !nodeList.contains(next.get().to)) {
                                Edge current = next.get();
                                nodeList.add(current.to);
                                next = current.getInSameDirection().stream()
                                        .filter(ed -> ed.to != current.from)
                                        .findAny();
                            }
                            return nodeList;
                        }));
        return lists.flatMap(l -> patterns.stream().filter(p -> p.matches(l) != null)).collect(Collectors.toSet());
    }

    public Set<Pattern.Match> getMatches(Step s) {
        HashSet<HashSet<Edge>> edgeSets = getRelevantEdges(s);
        Stream<List<Node>> lists =
                edgeSets.stream()
                        .flatMap(edgeSet -> edgeSet.stream().map(e -> {
                            ArrayList<Node> nodeList = new ArrayList<>();
                            nodeList.add(e.from);
                            Optional<Edge> next = Optional.of(e);
                            while (next.isPresent() && !nodeList.contains(next.get().to)) {
                                Edge current = next.get();
                                nodeList.add(current.to);
                                next = current.getInSameDirection().stream()
                                        .filter(ed -> ed.to != current.from)
                                        .findAny();
                            }
                            return nodeList;
                        }));
        return lists.flatMap(l -> patterns.stream().map(p -> p.matches(l))).collect(Collectors.toSet());
    }
//
//    public Set<Pattern> getMatchingPatterns(Step s) {
//        HashSet<HashSet<Edge>> edgeSets = getRelevantEdges(s);
//        Stream<Iterator<Edge>> iterators = edgeSets.stream()
//                .flatMap(edgeSet -> edgeSet.stream().map(e -> new Iterator<Edge>() {
//                    Edge currentEdge = e;
//
//                    @Override
//                    public boolean hasNext() {
//                        return currentEdge.getInSameDirection().stream()
//                                .filter(ed -> ed.to != currentEdge.from)
//                                .findAny().isPresent();
//                    }
//
//                    @Override
//                    public Edge next() {
//                        return currentEdge = currentEdge.getInSameDirection().stream()
//                                .filter(ed -> ed.to != currentEdge.from)
//                                .findAny().orElseGet(null);
//                    }
//                }));
//        iterators.flatMap(it -> patterns.stream().filter(p -> p.matches(it) != null));
//
//        return null;//TODO
//    }

    public HashSet<HashSet<Edge>> getRelevantEdges(Step s) {
        HashSet<HashSet<Edge>> edgeSets = new HashSet<>();
        for (Node node : s.changedNodes()) {
            for (Edge e : node.edges) {
                if (edgeSets.stream().noneMatch(es -> es.contains(e))) {
                    HashSet<Edge> newEdges = new HashSet<>(), collected = new HashSet<>(), current = new HashSet<>();
                    edgeSets.add(collected);
                    current.add(e);
                    while (!current.isEmpty()) {
                        current.forEach(c -> c.getInSameDirection().forEach(newEdges::add));
                        collected.addAll(current);
                        newEdges.removeAll(collected);
                        current.clear();
                        current.addAll(newEdges);
                        newEdges.clear();
                    }
                }
            }
        }
        return edgeSets;
    }

    public void clear() {
        for (N n : nodes)
            n.stones.clear();
    }

    @Override
    public String toString() {
        return "Board["+nodes+"]";
    }
}
