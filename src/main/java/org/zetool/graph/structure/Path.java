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

package org.zetool.graph.structure;

import org.zetool.graph.Edge;
import org.zetool.container.collection.IdentifiableCollection;
import org.zetool.container.collection.ListSequence;
import org.zetool.graph.Node;
import java.util.Iterator;

/**
 * The {@code Path} interface unites the common methods of paths in graphs.
 * It can be implemented by static paths as well as by dynamic paths.
 */
public interface Path extends Iterable<Edge> {

    /**
     * Extends the path by adding an edge at the start.
     * The edge must be consistent to the current first edge of the path,
     * i.e. i.e. the startnode of the current first edge must be
     * equal to the endnode of {@code edge}.
     * @param edge the edge to insert at the end of the path
     * @return {@code true} if the insertion was successfull,
     *         {@code false} else.
     */
    boolean addFirstEdge(Edge edge);

    /**
     * Extends the path by adding an edge at the end.
     * The edge must be consistent to the current last edge of the path,
     * i.e. i.e. the endnode of the current last edge must be
     * equal to the startnode of {@code edge}.
     * @param edge the edge to insert at the end of the path
     * @return {@code true} if the insertion was successful,
     * {@code false} else.
     */
    boolean addLastEdge(Edge edge);

    /**
     * Returns the first edge of the path or null if the path is empty.
     * @return the first edge of the path or null if the path is empty.
     */
    Edge first();

    /**
     * Returns an iterator for the edges of this path.
     * With the iterator one can iterate comfortable through all the
     * edges of the path.
     * @return an iterator for the edges of this path.
     */
    @Override
    Iterator<Edge> iterator();

    /**
     * Returns all edges of this path.
     * The returntype implements the interface {@link IdentifiableCollection}.
     * The interface specifies methods to address the content.
     * @return all edges of this path in a {@link IdentifiableCollection}.
     */
    ListSequence<Edge> getEdges();

    /**
     * Returns the last edge of the path or null if the path is empty.
     * @return the last edge of the path or null if the path is empty.
     */
    Edge last();

    /**
     * Returns the length of this path, i.e. the number of edges.
     * @return the length of this path, i.e. the number of edges.
     */
    int length();

    /**
     * Returns a String containing all edges. An edge e=(a,b) will be
     * represented by (a,b) in the string.
     * @return a String containing all edges,
     *         where the edges are identified by their nodes
     */
    String nodesToString();

    /**
     * Shortens the path by removing the first edge.
     * If the path is empty, nothing happens.
     * @return {@code false} if there was no element to be removed,
     *         {@code true} else.
     */
    boolean removeFirstEdge();

    /**
     * Shortens the path by removing the last edge.
     * If the path is empty, nothing happens.
     * @return {@code false} if there was no element to be removed,
     *         {@code true} else.
     */
    boolean removeLastEdge();

    /**
     * The start node.
     * @return the start node.
     */
    Node start();

    /**
     * The end node.
     * @return the end node.
     */
    Node end();
}
