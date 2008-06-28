package edu.wustl.common.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A weighted, directed graph. This class can be used to superimpose a graph
 * structure on any set of classes that logically represent vertices and weights
 * e.g. if an application has a class called <tt>City</tt>, then the
 * <tt>Graph</tt> class can be used to create a route map across all cities by
 * specifying vertices that of type <tt>City</tt> and weights that are of type
 * <tt>Long</tt> (indicating distance).<br>
 * Note that several methods in this class throw an
 * <tt>IllegalArgumentException</tt> if a specified vertex is not present in
 * the graph. All methods throw a <tt>NullPointerException</tt> if a specified
 * vertex parameter is <tt>null</tt>.
 * 
 * @param <V> the type of vertices
 * @param <E> the type of weights on edges
 */

public class Graph<V, E> implements Serializable, Cloneable {

    private static final long serialVersionUID = 2744129191470144562L;

    private transient HashMap<V, Map<V, E>> incomingEdgeMap;

    private HashMap<V, Map<V, E>> outgoingEdgeMap;

    public Graph() {
        initMaps();
    }

    public Graph(Graph<? extends V, ? extends E> graph) {
        assign(graph);
    }

    private void initMaps() {
        incomingEdgeMap = new HashMap<V, Map<V, E>>();
        outgoingEdgeMap = new HashMap<V, Map<V, E>>();
    }

    private class VerticesIterator implements Iterator<V> {
        // TODO fail-fast
        private Iterator<V> iter = outgoingEdgeMap.keySet().iterator();

        private V current;

        public boolean hasNext() {
            return iter.hasNext();
        }

        public V next() {
            return current = iter.next();
        }

        public void remove() {
            if (current == null) {
                throw new IllegalStateException();
            }
            current = null;
            removeVertex(current);
        }

    }

    private class EntryIterator implements Iterator<Entry<V, E>> {
        // TODO fail-fast
        private Iterator<Map.Entry<V, Map<V, E>>> outerIter = outgoingEdgeMap.entrySet().iterator();

        private Iterator<Map.Entry<V, E>> innerIter = new EmptyIterator<Map.Entry<V, E>>();

        private Entry<V, E> current;

        public boolean hasNext() {
            return outerIter.hasNext() || innerIter.hasNext();
        }

        public Entry<V, E> next() {
            V source = (current != null ? current.getSource() : null);
            while (!innerIter.hasNext() && outerIter.hasNext()) {
                Map.Entry<V, Map<V, E>> outerEntry = outerIter.next();
                source = outerEntry.getKey();
                innerIter = outerEntry.getValue().entrySet().iterator();
            }
            return current = entry(innerIter.next(), source);
        }

        public void remove() {
            if (current == null) {
                throw new IllegalStateException();
            }
            removeEdge(current.getSource(), current.getTarget());
        }

        private Entry<V, E> entry(Map.Entry<V, E> mapEntry, V source) {
            return new Entry<V, E>(source, mapEntry.getKey(), mapEntry.getValue());
        }
    }

    private Iterator<V> newVertexIter() {
        return new VerticesIterator();
    }

    private Iterator<Entry<V, E>> newEntryIter() {
        return new EntryIterator();
    }

    public static class Entry<V, E> implements Serializable {
        private static final long serialVersionUID = 4871557642444241179L;

        private V src, target;

        private E edge;

        private Entry(V src, V target, E edge) {
            this.src = src;
            this.target = target;
            this.edge = edge;
        }

        public E getEdge() {
            return edge;
        }

        public V getSource() {
            return src;
        }

        public V getTarget() {
            return target;
        }

    }

    private class VerticesSet extends AbstractSet<V> {

        @Override
        public Iterator<V> iterator() {
            return newVertexIter();
        }

        @Override
        public int size() {
            return outgoingEdgeMap.size();
        }

        @Override
        public boolean add(V o) {
            return addVertex(o);
        }

        @Override
        public boolean contains(Object o) {
            return outgoingEdgeMap.containsKey(o);
        }

        @Override
        public void clear() {
            Graph.this.clear();
        }

        @Override
        public boolean remove(Object o) {
            if (!contains(o)) {
                return false;
            }
            removeVertex((V) o);
            return true;
        }
    }

    /* DATA-RELATED METHODS */
    // ACCESSORS
    /**
     * @return set of all vertices present in graph.
     */
    public Set<V> getVertices() {
        return new VerticesSet();
    }

    /**
     * Checks if specified vertex is present in this graph.
     * 
     * @param vertex the vertex whose presence is to be checked.
     * @return <tt>true</tt> if this vertex is present in this graph;
     *         <tt>false</tt> otherwise.
     */
    public boolean containsVertex(V vertex) {
        return outgoingEdgeMap.containsKey(vertex);
    }

    /**
     * @return list of all edges in this graph.
     */
    public Set<E> getEdges() {
        Set<E> res = new HashSet<E>();
        for (Map<V, E> edges : outgoingEdgeMap.values()) {
            res.addAll(edges.values());
        }
        return res;
    }

    public Map<V, E> getOutgoingEdges(V source) {
        validateVertex(source);
        return copy(outgoingEdgeMap.get(source));
    }

    public Map<V, E> getIncomingEdges(V target) {
        validateVertex(target);
        return copy(incomingEdgeMap.get(target));
    }

    /**
     * This method checks whether an edge lies between the source vertex and
     * target vertex.
     * 
     * @return true if the edge exists; false otherwise
     */
    public boolean containsEdge(V source, V target) {
        validateVertex(source);
        validateVertex(target);
        return outgoingEdgeMap.get(source).containsKey(target);
    }

    /**
     * Get the edge from the list of edges, if one exists between source vertex
     * and target vertex.
     * 
     * @return edge object if it exists; null otherwise
     */
    public E getEdge(V source, V target) {
        validateVertex(source);
        validateVertex(target);
        return outgoingEdgeMap.get(source).get(target);
    }

    // MODIFIERS
    /**
     * Remove the edge if it exists in the list of edges.
     * 
     * @param sourceVertex
     * @param targetVertex
     * @return removed edge if edge object is not null; null otherwise
     */
    public E removeEdge(V source, V target) {
        validateVertex(source);
        validateVertex(target);
        outgoingEdgeMap.get(source).remove(target);
        return incomingEdgeMap.get(target).remove(source);
    }

    /**
     * Put the edge into the list of edges if it does not exist. If the edge
     * exists return the old edge and replace it with a new edge.
     * 
     * @param sourceVertex
     * @param targetVertex
     * @param edge
     * @return the old edge if it exists; null otherwise
     */
    public E putEdge(V source, V target, E edge) {
        // if (edge == null) {
        // throw new NullPointerException("null edge not allowed.");
        // }
        addVertex(source);
        addVertex(target);

        outgoingEdgeMap.get(source).put(target, edge);
        return incomingEdgeMap.get(target).put(source, edge);
    }

    /**
     * Add a vertex to the list of vertices if same does not exist in the list
     * 
     * @param vertex the vertex to add to the graph.
     * @return <tt>false</tt> if this vertex already existed.
     * @throws NullPointerException if the specified vertex is null.
     */
    public boolean addVertex(V vertex) {
        checkNull(vertex);
        if (containsVertex(vertex))
            return false;
        else {
            incomingEdgeMap.put(vertex, new HashMap<V, E>());
            outgoingEdgeMap.put(vertex, new HashMap<V, E>());
            return true;
        }
    }

    /**
     * @param vertices the vertices to add to the graph
     * @return <tt>true</tt> if atleast one new vertex was added in this call.
     */
    public boolean addVertices(Set<? extends V> vertices) {
        if (vertices == null) {
            return false;
        }
        boolean modified = false;
        for (V v : vertices) {
            if (addVertex(v))
                modified = true;
        }
        return modified;
    }

    /**
     * Removes the specified vertex from the list of vertices if one exists
     * 
     * @param vertex
     * @return true upon removing specified existing vertex; false otherwise
     */
    public boolean removeVertex(V vertex) {
        if (!containsVertex(vertex)) {
            return false;
        }
        for (V src : incomingEdgeMap.get(vertex).keySet()) {
            removeEdge(src, vertex);
        }
        for (V target : outgoingEdgeMap.get(vertex).keySet()) {
            removeEdge(vertex, target);
        }
        incomingEdgeMap.remove(vertex);
        outgoingEdgeMap.remove(vertex);
        return true;
    }

    public void clear() {
        incomingEdgeMap.clear();
        outgoingEdgeMap.clear();
    }

    // HELPERS
    void checkNull(V vertex) {
        if (vertex == null) {
            throw new NullPointerException("null vertex.");
        }
    }

    void validateVertex(V vertex) {
        checkNull(vertex);
        if (!containsVertex(vertex)) {
            throw new IllegalArgumentException("specified vertex is not present in graph.");
        }
    }

    private static <K, V> Map<K, V> copy(Map<K, V> map) {
        return new HashMap<K, V>(map);
    }

    // ////////////////////////////////////////////////////////////////////////////////
    /* GRAPH STRUCTURE BASED OPERATIONS */
    /**
     * To get the list directly reachable Vertices from the given vertex.
     * 
     * @return List of Vertices directly reachable from the given vertex.
     *         Returns null if vertex is not present in graph, Returns empty
     *         list if vertex has no directly reachable node.
     */
    public Set<V> getOutNeighbours(V v) {
        return getOutgoingEdges(v).keySet();
    }

    /**
     * To get the list of vertices from which the given vertex is directly
     * reachable.
     * 
     * @return List of Vertices from which the given vertex is directly
     *         reachable. Returns null if vertex is not present in graph,
     *         Returns empty list if vertex has no incomming Edges.
     */
    public Set<V> getInNeighbours(V v) {
        return getIncomingEdges(v).keySet();
    }

    /**
     * This method will return the list of vertices having no incomming Edges.
     * The node having no incoming edges will be treated as Root node.
     * 
     * @return list of vertices having no incomming Edges.
     */
    public Set<V> getUnreachableVertices() {
        Set<V> res = new HashSet<V>();
        for (Map.Entry<V, Map<V, E>> entry : incomingEdgeMap.entrySet()) {
            if (entry.getValue().isEmpty()) {
                res.add(entry.getKey());
            }
        }
        return res;
    }

    /**
     * This method checks whether adding node from sourceVertex to targetVertex
     * will result into cyclic graph or not. Adding an edge will result into
     * cycle only when the target vertex is reachable from source vertex i.e.
     * Vertex B is reachable from vertex A if and only if 1. There is direct
     * edge from vertex C to B 2. and C is reachable from A.
     * 
     * @param sourceVertex The source vertex of edge to be added.
     * @param targetVertex The target vertex of edge to be added.
     * @return true if the
     */
    private boolean isReverseReachable(V sourceVertex, V targetVertex) {
        if (sourceVertex.equals(targetVertex))
            return true; // finally reached from source to target!!!
        for (V v : incomingEdgeMap.get(sourceVertex).keySet()) {
            if (isReverseReachable(v, targetVertex))
                return true;
        }

        return false;
    }

    /**
     * All possible (acyclic) paths between two vertices. If source and target
     * are the same, then the resulting path will have no vertices.
     * 
     * @param source the begining vertex.
     * @param target the ending vertix.
     * @return List of all paths, where path is again List of Vertices.
     * @throws IllegalArgumentException when the fromVetrex or toVetrex is not
     *             in the graph.
     */
    public Set<List<V>> getVertexPaths(V source, V target) {
        validateVertex(source);
        validateVertex(target);
        return getVertexPaths(source, target, new HashSet<V>());
    }

    // TODO check source == target
    private Set<List<V>> getVertexPaths(V source, V target, Set<V> verticesToIgnore) {
        Set<List<V>> res = new HashSet<List<V>>();
        verticesToIgnore.add(target);
        for (Map.Entry<V, E> entry : incomingEdgeMap.get(target).entrySet()) {
            V srcSrc = entry.getKey();
            if (verticesToIgnore.contains(srcSrc)) {
                continue;
            }
            if (source.equals(srcSrc)) {
                List<V> path = new ArrayList<V>();
                path.add(source);
                path.add(target);
                res.add(path);
                continue;
            }
            Set<List<V>> thePaths = getVertexPaths(source, srcSrc, verticesToIgnore);

            for (List<V> thePath : thePaths) {
                thePath.add(target);
            }
            res.addAll(thePaths);
        }
        verticesToIgnore.remove(target);
        return res;
    }

    /**
     * All possible path Edges between two vertices.
     * 
     * @param fromVertex the begining vertex.
     * @param toVetrex the ending vertix.
     * @return List of all path Edges, where path is again List of Vertices.
     * @throws IllegalArgumentException when the fromVetrex or toVetrex is not
     *             in the graph.
     */
    public Set<List<E>> getEdgePaths(V fromVertex, V toVetrex) {
        Set<List<E>> edgePaths = new HashSet<List<E>>();
        Set<List<V>> verticesPaths = getVertexPaths(fromVertex, toVetrex);
        for (List<V> thePath : verticesPaths) {
            List<E> theEdgePath = new ArrayList<E>();
            for (int j = 1; j < thePath.size(); j++) {
                theEdgePath.add(getEdge(thePath.get(j - 1), thePath.get(j)));
                edgePaths.add(theEdgePath);
            }
        }
        return edgePaths;
    }

    // ///////////////////////////////////////////////////////////
    /* STRUCTURAL CONSTRAINT CHECKERS */
    /**
     * Checks if the graph is weakly connected. Graph will be connected if 1.
     * after Depth first traversing (without considering edge Direction) through
     * graph from (the only one)unreachable node, results into
     * 
     * @return true if graph is connected; false if graph is disjoint
     */
    public boolean isConnected() {
        Set<V> vertices = getVertices();
        if (vertices.isEmpty()) {
            return false;
        }

        dfs(vertices.iterator().next(), vertices);
        return vertices.isEmpty();
    }

    /**
     * Method to traverse using Depth First algorithm. It removes the vertex
     * from allVertexSet while visiting each vertex. dfs of connected graph
     * should result into the allVetrexSet empty.
     * 
     * @param vertex The vertex to be visited.
     * @param allVertexSet Set of all nodes not visited yet.
     */
    private void dfs(V vertex, Set<V> allVertexSet) {
        allVertexSet.remove(vertex);
        Set<V> adjacentVertices = new HashSet<V>();
        adjacentVertices.addAll(outgoingEdgeMap.get(vertex).keySet());
        adjacentVertices.addAll(incomingEdgeMap.get(vertex).keySet());
        // insure against self-edge
        adjacentVertices.remove(vertex);
        for (V adjacentVertex : adjacentVertices) {
            if (allVertexSet.contains(adjacentVertex))
                dfs(adjacentVertex, allVertexSet);
        }
    }

    public boolean isTree() {
        if (!isConnected()) {
            return false;
        }
        Set<V> roots = getUnreachableVertices();
        if (roots.size() != 1) {
            return false;
        }
        V root = roots.iterator().next();
        Set<V> vertices = getVertices();
        vertices.remove(root);
        for (V vertex : vertices) {
            Map<V, E> in = incomingEdgeMap.get(vertex);
            if (in.size() > 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if adding specified edge will cause a new cycle in the graph.
     * 
     * @param src src vertex
     * @param target target vertex
     * @return <tt>true</tt> if this edge will cause a new cycle in this
     *         graph;<tt>false</tt> otherwise.
     */
    public boolean willCauseNewCycle(V src, V target) {
        if (!(containsVertex(src) && containsVertex(target))) {
            return false;
        }
        return isReverseReachable(src, target);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Graph<V, E> clone() {
        // TODO maybe clone inner map is required?
        Graph<V, E> res = null;
        try {
            res = (Graph<V, E>) super.clone();
        } catch (CloneNotSupportedException e) {
            // can't occur
        }
        res.assign(this);
        return res;
    }

    public void assign(Graph<? extends V, ? extends E> graph) {
        assign2(graph);
    }

    private <V1 extends V, E1 extends E> void assign2(Graph<V1, E1> graph) {
        initMaps();
        for (V1 src : graph.getVertices()) {
            for (Map.Entry<V1, E1> entry : graph.getOutgoingEdges(src).entrySet()) {
                putEdge(src, entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return outgoingEdgeMap.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Graph)) {
            return false;
        }
        Graph<V, E> other = (Graph<V, E>) obj;
        return outgoingEdgeMap.equals(other.outgoingEdgeMap);
    }

    @Override
    public int hashCode() {
        return outgoingEdgeMap.hashCode();
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        incomingEdgeMap = new HashMap<V, Map<V, E>>();
        for (V v : outgoingEdgeMap.keySet()) {
            incomingEdgeMap.put(v, new HashMap<V, E>());
        }
        for (Map.Entry<V, Map<V, E>> outgoingEdgesEntry : outgoingEdgeMap.entrySet()) {
            V src = outgoingEdgesEntry.getKey();
            for (Map.Entry<V, E> outgoingEdge : outgoingEdgesEntry.getValue().entrySet()) {
                V target = outgoingEdge.getKey();
                E edge = outgoingEdge.getValue();
                incomingEdgeMap.get(target).put(src, edge);
            }
        }
    }
}