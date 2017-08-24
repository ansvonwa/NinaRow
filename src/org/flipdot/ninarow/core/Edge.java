package org.flipdot.ninarow.core;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by anselm on 29.03.17.
 */
public class Edge {
    public final Node from, to;
    protected Set<Edge> inSameDirection = new HashSet<>();

    public Edge(Node from, Node to) {
        this.from = from;
        this.to = to;
        from.edges.add(this);
        to.edges.add(this);
    }

    public Set<Edge> getInSameDirection() {
        return new HashSet<>(inSameDirection);
    }

    public void addInSameDirection(Edge other) {
        this.inSameDirection.add(other);
    }

    @Override
    public String toString() {
        return "Edge[from="+from+",to="+to+"]";
    }
}
