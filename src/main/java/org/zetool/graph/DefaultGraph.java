/* zet evacuation tool copyright (c) 2007-14 zet evacuation team
 *
 * This program is free software; you can redistribute it and/or
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package org.zetool.graph;

import org.zetool.graph.localization.GraphLocalization;
import org.zetool.container.collection.DependingListSequence;
import org.zetool.container.collection.HidingSet;
import org.zetool.container.collection.IdentifiableCollection;
import org.zetool.container.collection.ListSequence;
import org.zetool.container.mapping.IdentifiableIntegerMapping;
import org.zetool.container.mapping.IdentifiableObjectMapping;
import org.zetool.graph.structure.Path;
import org.zetool.graph.util.GraphUtil;
import org.zetool.graph.util.OppositeNodeCollection;
import java.util.Iterator;

/**
 * Implementation of {@link org.zetool.graph.UndirectedGraph} that allows edges to be hidden.
 *
 * @author Jan-Philipp Kappmeier
 */
public class DefaultGraph implements MutableUndirectedGraph {

    /**
     * The nodes of the network. Must not be {@code null}.
     */
    protected HidingSet<Node> nodes;
    /**
     * The edges of the network. Must not be {@code null}.
     */
    protected HidingSet<Edge> edges;
    /**
     * Caches the edges incident to a node for all nodes in the graph. Must not be {@code null}.
     */
    protected IdentifiableObjectMapping<Node, DependingListSequence<Edge>> incidentEdges;
    /**
     * Caches the number of edges incident to a node for all nodes in the graph. Must not be {@code null}.
     */
    protected IdentifiableIntegerMapping<Node> degree;

    /**
     * Creates a new AbstractNetwork with the specified capacities for edges and nodes. Runtime
     * O(max(initialNodeCapacity, initialEdgeCapacity)).
     *
     * @param initialNodeCapacity the number of nodes that can belong to the graph
     * @param initialEdgeCapacity the number of edges that can belong to the graph
     */
    public DefaultGraph(int initialNodeCapacity, int initialEdgeCapacity) {
        edges = new HidingSet<>(Edge.class, initialEdgeCapacity);
        nodes = new HidingSet<>(Node.class, initialNodeCapacity);
        for (int i = 0; i < initialNodeCapacity; i++) {
            nodes.add(new Node(i));
        }
        incidentEdges = new IdentifiableObjectMapping<>(initialNodeCapacity);

        for (Node node : nodes) {
            incidentEdges.set(node, new DependingListSequence<>(edges));
        }
        degree = new IdentifiableIntegerMapping<>(initialNodeCapacity);
    }

    protected DefaultGraph(DefaultGraph network) {
        this.edges = network.edges;
        this.nodes = network.nodes;
        incidentEdges = network.incidentEdges;
        degree = network.degree;
    }

    /**
     * Checks whether the graph is directed. Runtime O(1).
     *
     * @return {@code true}.
     */
    @Override
    public boolean isDirected() {
        return false;
    }

    /**
     * Returns an {@link HidingSet} containing all the edges of this graph. The Runtime is O(1).
     *
     * @return an {@link HidingSet} containing all the edges of this graph.
     */
    @Override
    public IdentifiableCollection<Edge> edges() {
        return edges;
    }

    /**
     * Returns an {@link HidingSet} containing all the nodes of this graph. Runtime O(1).
     *
     * @return an {@link HidingSet} containing all the nodes of this graph.
     */
    @Override
    public IdentifiableCollection<Node> nodes() {
        return nodes;
    }

    /**
     * Returns the number of edges in this graph. Runtime O(1).
     *
     * @return the number of edges in this graph.
     */
    @Override
    public int edgeCount() {
        return edges().size();
    }

    /**
     * Returns the number of nodes in this graph. Runtime O(1).
     *
     * @return the number of nodes in this graph.
     */
    @Override
    public int nodeCount() {
        return nodes().size();
    }

    /**
     * Returns an {@link DependingListSequence} containing all the edges incident to the specified node. Runtime O(1).
     *
     * @param node the node whose incident are to be returned
     * @return an {@link DependingListSequence} containing all the edges incident to the specified node.
     */
    @Override
    public IdentifiableCollection<Edge> incidentEdges(Node node) {
        return incidentEdges.get(node);
    }

    /**
     * Returns an {@link IdentifiableCollection} containing all the nodes adjacent to the specified node. Runtime O(1).
     *
     * @param node the nodes which adjacent nodes are to be computed
     * @return an {@link IdentifiableCollection} containing all the nodes adjacent to the specified node.
     */
    @Override
    public IdentifiableCollection<Node> adjacentNodes(Node node) {
        return new OppositeNodeCollection(node, incidentEdges.get(node));
    }

    /**
     * Returns the degree of the specified node, i.e. the number of edges incident to it. Runtime O(1).
     *
     * @param node the node for which the degree is to be returned.
     * @return the degree of the specified node.
     */
    @Override
    public int degree(Node node) {
        return degree.get(node);
    }

    /**
     * Checks whether the specified edge is contained in this graph. Runtime O(1).
     *
     * @param edge the edge to be checked.
     * @return {@code true} if the edge is contained in this graph, {@code false} otherwise.
     */
    @Override
    public boolean contains(Edge edge) {
        return edges.contains(edge);
    }

    /**
     * Checks whether the specified node is contained in this graph. Runtime O(1).
     *
     * @param node the node to be checked.
     * @return {@code true} if the node is contained in this graph, {@code false} otherwise.
     */
    @Override
    public boolean contains(Node node) {
        return nodes.contains(node);
    }

    /**
     * Returns the edge with the specified id or {@code null} if the graph does not contain an edge with the specified
     * id. Runtime O(1).
     *
     * @param id the id of the edge to be returned.
     * @return the edge with the specified id or {@code null} if the graph does not contain an edge with the specified
     * id.
     */
    @Override
    public Edge getEdge(int id) {
        return edges.get(id);
    }

    /**
     * Returns an edge starting at {@code start} and ending at {@code end}. If no such edge exists, {@code null} is
     * returned. Runtime O(outdegree(start)).
     *
     * @param start the start node of the edge to be returned.
     * @param end the end node of the edge to be returned.
     * @return an edge starting at {@code start} and ending at {@code end}.
     */
    @Override
    public Edge getEdge(Node start, Node end) {
        for (Edge edge : incidentEdges(start)) {
            if (edge.end().equals(end)) {
                return edge;
            }
        }
        return null;
    }

    /**
     * Returns an {@link ListSequence} containing all edges between {@code start} and at {@code end}. If no such edge
     * exists, an empty list is returned. Runtime O(outdegree(start)).
     *
     * @param start the start node of the edges to be returned.
     * @param end the end node of the edges to be returned.
     * @return an {@link ListSequence} containing all edges starting at {@code start} and ending at {@code end}.
     */
    @Override
    public IdentifiableCollection<Edge> getEdges(Node start, Node end) {
        ListSequence<Edge> result = new ListSequence<>();
        for (Edge edge : incidentEdges(start)) {
            if (edge.end().equals(end)) {
                result.add(edge);
            }
        }
        return result;
    }

    /**
     * Returns the node with the specified id or {@code null} if the graph does not contain a node with the specified
     * id. Runtime O(1).
     *
     * @param id the id of the node to be returned.
     * @return the node with the specified id or {@code null} if the graph does not contain a node with the specified
     * id.
     */
    @Override
    public Node getNode(int id) {
        return nodes.get(id);
    }

    @Override
    public Iterator<Node> iterator() {
        return nodes.iterator();
    }

    /**
     * Returns a copy of this network. Runtime O(n + m).
     *
     * @return a copy of this network.
     */
    @Override
    public DefaultGraph clone() {
        DefaultGraph clone = new DefaultGraph(nodeCount(), edgeCount());
        clone.setNodes(nodes());
        clone.setEdges(edges());
        return clone;
    }

    /**
     * Compares the specified object to this object and returns whether the specified object is equivalent to this one.
     * An object is considered equivalent to this network, if and only if it is a network with an equivalent node and
     * edge set. Note that both the node and the edge set must be completely equivalent (i.e. both the visible and
     * hidden parts must be equivalent). Runtime O(n + m).
     *
     * @param o the object to compare with.
     * @return {@code true} if the specified object is a network equivalent to this network, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof DefaultGraph) {
            DefaultGraph n = (DefaultGraph) o;
            return n.edges.equals(edges) && n.nodes.equals(nodes);
        } else {
            return false;
        }
    }

    /**
     * Returns a hash code for this network. Runtime O(n + m).
     *
     * @return the sum of the hash codes of the edge and node containers.
     */
    @Override
    public int hashCode() {
        return edges.hashCode() + nodes.hashCode();
    }

    /**
     * Returns a string representation of this network. The representation is a list of all nodes and edges contained in
     * this graph. The conversion of nodes and edges to strings is done by the {@code toString} methods of their
     * classes. Runtime O(n + m).
     *
     * @return a string representation of this network
     */
    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("({");
        for (Node node : nodes()) {
            buffer.append(node.toString());
            buffer.append(", ");
        }
        if (nodeCount() > 0) {
            buffer.delete(buffer.length() - 2, buffer.length());
        }
        buffer.append("}, {");
        int counter = 0;
        for (Edge edge : edges()) {
            if (counter == 10) {
                counter = 0;
                buffer.append("\n");
            }
            buffer.append(edge.toString());
            buffer.append(", ");
            counter++;
        }
        if (edgeCount() > 0) {
            buffer.delete(buffer.length() - 2, buffer.length());
        }
        buffer.append("})");
        return buffer.toString();
    }

    public String deepToString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("V = {");
        for (Node node : nodes()) {
            buffer.append(node.toString());
            buffer.append(", ");
        }
        if (nodeCount() > 0) {
            buffer.delete(buffer.length() - 2, buffer.length());
        }
        buffer.append("\nE= {");
        int counter = 0;
        for (Edge edge : edges()) {
            if (counter == 10) {
                counter = 0;
                buffer.append("\n");
            }
            buffer.append(edge.nodesToString());
            buffer.append(", ");
            counter++;
        }
        if (edgeCount() > 0) {
            buffer.delete(buffer.length() - 2, buffer.length());
        }
        buffer.append("}\n");
        return buffer.toString();
    }

    /**
     * Returns the number of edges that can be contained in the graph. Runtime O(1).
     *
     * @return the number of edges that can be contained in the graph.
     */
    @Override
    public int getEdgeCapacity() {
        return edges.getCapacity();
    }

    /**
     * Allocates space for edges to be contained in the graph. Runtime O(newCapacity).
     *
     * @param newCapacity the number of edges that can be contained by the graph.
     */
    @Override
    public void setEdgeCapacity(int newCapacity) {
        if (getEdgeCapacity() != newCapacity) {
            edges.setCapacity(newCapacity);
        }
    }

    /**
     * Returns the number of nodes that can be contained in the graph. Runtime O(1).
     *
     * @return the number of nodes that can be contained in the graph.
     */
    @Override
    public int getNodeCapacity() {
        return nodes.getCapacity();
    }

    /**
     * Allocates space for nodes to be contained in the graph. Runtime O(newCapacity).
     *
     * @param newCapacity the number of nodes that can be contained by the graph.
     */
    @Override
    public void setNodeCapacity(int newCapacity) {
        if (getNodeCapacity() != newCapacity) {
            int oldCapacity = getNodeCapacity();
            nodes.setCapacity(newCapacity);
            incidentEdges.setDomainSize(newCapacity);
            degree.setDomainSize(newCapacity);

            for (int i = oldCapacity; i < newCapacity; i++) {
                setNode(new Node(i));
            }
        }
    }

    /**
     * Checks whether the specified edge is hidden. Runtime O(1).
     *
     * @param edge the edge to be tested.
     * @return {@code true} if the specified edge is hidden, {@code false
     * } otherwise.
     */
    //@Override
    public boolean isHidden(Edge edge) {
        return edges.isHidden(edge);
    }

    /**
     * Sets the hidden state of the specified edge to the specified value. A hidden edge is treated as if it did not
     * belong to the graph - the only difference to it being actually deleted is that it can be restored very
     * efficiently. This can be useful for residual networks amongst other things. Runtime O(1).
     *
     * @param edge the edge for which the hidden state is to be set.
     * @param value the new value of the edge's hidden state.
     */
    //@Override
    public void setHidden(Edge edge, boolean value) {
        if (isHidden(edge) != value) {
            edges.setHidden(edge, value);
            if (value) {
                degree.decrease(edge.start(), 1);
                degree.decrease(edge.end(), 1);
            } else {
                degree.increase(edge.start(), 1);
                degree.increase(edge.end(), 1);
            }
        }
    }

    /**
     * Checks whether the specified node is hidden. Runtime O(1).
     *
     * @param node the node to be tested.
     * @return {@code true} if the specified node is hidden, {@code false
     * } otherwise.
     */
    //@Override
    public boolean isHidden(Node node) {
        return nodes.isHidden(node);
    }

    /**
     * Sets the hidden state of the specified node to the specified value. A hidden node is treated as if it did not
     * belong to the graph - the only difference is to it being actually deleted is that it can be restored very
     * efficiently. Hiding a node causes all edges incident to it to be hidden as well. Runtime O(degree(node)).
     *
     * @param node the node for which the hidden state is to be set.
     * @param value the new value of the node's hidden state.
     */
    //@Override
    public void setHidden(Node node, boolean value) {
        if (isHidden(node) != value) {
            if (value) {
                for (Edge edge : incidentEdges(node)) {
                    setHidden(edge, value);
                }
            }
            nodes.setHidden(node, value);
        }
    }

    //@Override
    public void setHiddenOnlyNode(Node node, boolean value) {
        nodes.setHidden(node, value);
    }

    //@Override
    public void showAllEdges() {
        edges.showAll();
    }

    private int idOfLastCreatedEdge = -1;

    /**
     * Creates a new directed edge between the specified start and end nodes and adds it to the graph (provided the
     * graph has enough space allocated for an additional edge). Runtime O(1).
     *
     * @param start the start node of the new edge.
     * @param end the end node of the new edge.
     * @return the new edge.
     */
    @Override
    public Edge createAndSetEdge(Node start, Node end) {
        int id = idOfLastCreatedEdge + 1;
        int capacity = getEdgeCapacity();
        while (edges.getEvenIfHidden(id % capacity) != null || id == idOfLastCreatedEdge + 1 + capacity) {
            id++;
        }
        if (edges.getEvenIfHidden(id % capacity) == null) {
            Edge edge = new Edge(id % capacity, start, end);
            setEdge(edge);
            idOfLastCreatedEdge = id % capacity;
            return edge;
        } else {
            throw new IllegalStateException(GraphLocalization.LOC.getString("ds.Graph.NoCapacityException"));
        }
    }

    /**
     * Adds the specified nodes to the graph by calling {@code setNode} for each node. Runtime O(number of nodes).
     *
     * @param nodes the nodes to be added to the graph.
     */
    @Override
    public void setNodes(Iterable<Node> nodes) {
        for (Node node : nodes) {
            setNode(node);
            if (nodes instanceof HidingSet) {
                setHidden(node, ((HidingSet<Node>) nodes).isHidden(node));
            }
        }
    }

    public Path getPath(Node start, Node end) {
        // convenience method
        return GraphUtil.getPath(this, start, end);
    }

    /**
     * Checks whether at least one edge between the specified start and end nodes exists.
     *
     * @param start the start node of the edge to be checked
     * @param end the end node of the path to be checked
     * @return {@code true} if the edge between the start node and the end node exists, {@code false} otherwise
     */
    public boolean existsEdge(Node start, Node end) {
        return getEdge(start, end) != null;
    }

    /**
     * Creates a graph equal to the graph but all edges between a pair of nodes are reversed.
     *
     * @return a reversed copy of the graph
     */
    //@Override
    public DefaultGraph createReverseGraph() {
        DefaultGraph result = new DefaultGraph(nodeCount(), edgeCount());
        for (Edge edge : edges) {
            result.createAndSetEdge(edge.end(), edge.start());
        }
        return result;
    }

    /**
     * Returns the network as a {@code AbstractNetwork} object. As this graph is already static, the object itself is
     * returned.
     *
     * @return this object
     */
    public DefaultGraph getAsStaticNetwork() { // todo: what to do with this?
        return this;
    }

    public IdentifiableCollection<Edge> allEdges() {
        return edges.getAll();
    }

    public IdentifiableCollection<Node> allNodes() {
        return nodes.getAll();
    }

    public Edge allGetEdge(Node start, Node end) {

        Iterator<Edge> iter = incidentEdges(start).iterator();
        while (iter.hasNext()) {
            Edge edge = iter.next();
            if (edge.end().equals(end)) {
                return edge;
            }
        }
        return null;
    }

    /**
     * Adds the specified edge to the graph by setting it to it ID's correct position in the internal data structures.
     * The correct position must be empty. Runtime O(1).
     *
     * @param edge the edge to be added to the graph.
     * @exception IllegalArgumentException if the specified position is not empty.
     */
    public void setEdge(Edge edge) {
        if (edges.get(edge.id()) == null) {
            edges.add(edge);
            incidentEdges(edge.start()).add(edge);
            incidentEdges(edge.end()).add(edge);
            degree.increase(edge.start(), 1);
            degree.increase(edge.end(), 1);
        } else if (edges.get(edge.id()).equals(edge)) {
        } else {
            throw new IllegalArgumentException("Edge position is already occupied");
        }
    }

    /**
     * Adds the specified edges to the graph by calling {@code setEdge} for each edge. Runtime O(number of edges).
     *
     * @param edges the edges to be added to the graph.
     */
    @Override
    public void setEdges(Iterable<Edge> edges) {
        for (Edge edge : edges) {
            setEdge(edge);
            if (edges instanceof HidingSet) {
                setHidden(edge, ((HidingSet<Edge>) edges).isHidden(edge));
            }
        }
    }

    /**
     * Adds the specified node to the graph by setting it to it ID's correct position in the internal data structures.
     * If this position was occupied before it will be overwritten. Runtime O(1). Note, that the incoming and outgoing
     * edges of any replaced node will be valid for the new one.
     *
     * @param node the node to be added to the graph.
     */
    public void setNode(Node node) {
        if (nodes.get(node.id()) == null) {
            incidentEdges.set(node, new DependingListSequence<>(edges));
            degree.set(node, 0);
        }
        nodes.add(node);
    }

    public Iterator<Edge> allEdgesIterator() {
        return edges.iteratorAll();
    }

    /**
     * Returns the number of edges in this graph. Runtime O(1).
     *
     * @return the number of edges in this graph.
     */
    public int allNumberOfEdges() {
        return edges.numberOfAllElements();
    }

    /**
     * Returns the number of nodes in this graph. Runtime O(1).
     *
     * @return the number of nodes in this graph.
     */
    public int allNumberOfNodes() {
        return nodes.numberOfAllElements();
    }

    public IdentifiableCollection<Edge> getHidden() {
        return edges.getHiddenElements();
    }
}
