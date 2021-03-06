/* zet evacuation tool copyright (c) 2007-20 zet evacuation team
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
package org.zetool.algorithm.spanningtree;

import java.util.Objects;

import org.checkerframework.checker.nullness.qual.NonNull;

import org.zetool.container.mapping.IdentifiableIntegerMapping;
import org.zetool.graph.Edge;
import org.zetool.graph.Graph;

/**
 *
 * @author Jan-Philipp Kappmeier
 * @author Marlen Schwengfelder
 */
public class MinSpanningTreeProblem {

    private final Graph graph;
    private final IdentifiableIntegerMapping<Edge> distances;

    /**
     * Initializes
     * @param graph
     * @param distances 
     */
    public MinSpanningTreeProblem(@NonNull Graph graph, @NonNull IdentifiableIntegerMapping<Edge> distances) {
        this.graph = Objects.requireNonNull(graph);
        this.distances = Objects.requireNonNull(distances);
    }

    public Graph getGraph() {
        return graph;
    }

    public IdentifiableIntegerMapping<Edge> getDistances() {
        return distances;
    }
}
