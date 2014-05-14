package de.tu_berlin.coga.graph;

import ds.graph.Edge;
import ds.graph.Node;
import java.util.Iterator;

/**
 * Given a predecessor tree, iterates from a given end node along all preceeding edges.
 * @author Jan-Philipp Kappmeier
 */
public class PredecessorIterator implements Iterator<Edge> {
  private Node current;
  private Edge next;
  private PredecessorMap pred;

  public PredecessorIterator( Node current, PredecessorMap pred ) {
    this.current = current;
    this.pred = pred;
  }

  @Override
  public boolean hasNext() {
    next = pred.getPredecessor( current );
    if( next != null ) {
      current = next.start();
      return true;
    }
    return false;
  }

  @Override
  public Edge next() {
    return next;
  }
}
