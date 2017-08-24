package org.flipdot.ninarow.core;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by anselm on 29.03.17.
 */
public class Node {
    public Set<Edge> edges = new HashSet<>();
    public Set<Player> stones = new HashSet<>();

    @Override
    public String toString() {
        return getClass().getSimpleName()+"@"+hashCode()+"["+edges.size()+" edges]";
    }
}
