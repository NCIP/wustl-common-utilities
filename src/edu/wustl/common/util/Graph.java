package edu.wustl.common.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Mandar Shidhore
 * @version 1.0
 * @created 31-Oct-2006 12.46.04 PM
 */

// TODO check getEdge(). edgeObj may be null.
// TODO check Edge.hashCode() no equals
// TODO check isConnected()... self-edge causes problem. ASSUMES ROOTED GRAPH.
// TODO getReachablePaths etc... incorrect if cycles present.
public class Graph<V, E> implements Serializable {

    private static final long serialVersionUID = 2744129191470144562L;

    private Map<V, List<Edge>> incommingEdgeMap = new HashMap<V, List<Edge>>();

    private Map<V, List<Edge>> outgoingEdgeMap = new HashMap<V, List<Edge>>();

    public Graph() {
    }

    // Internal Edge class to create the edge object
    class Edge implements Serializable {

        private static final long serialVersionUID = 2747448736801686112L;

        private V sourceVertex;

        private V targetVertex;

        private E edgeObj;

        public Edge(V sourceVertex, V targetVertex, E edgeobj) {
            this.sourceVertex = sourceVertex;
            this.targetVertex = targetVertex;
            this.edgeObj = edgeobj;
        }

        public V getSourceVertex() {
            return sourceVertex;
        }

        public V getTargetVertex() {
            return targetVertex;
        }

        public E getEdgeObject() {
            return edgeObj;
        }

        public void setEdgeObject(E edgeObj) {
            this.edgeObj = edgeObj;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "[" + sourceVertex + "--" + edgeObj + "-->" + targetVertex
                    + "]";
        }

    }

    /**
     * Add a vertex to the list of vertices if same does not exist in the list
     * @param vertex
     * @return true upon adding vertex to existing vertex list; false otherwise
     */
    public boolean addVertex(V vertex) {
        if (containsVertex(vertex))
            return false;
        else {
            incommingEdgeMap.put(vertex, new ArrayList<Edge>());
            outgoingEdgeMap.put(vertex, new ArrayList<Edge>());
            return true;
        }
    }

    /**
     * Checks if specified vertex is present in this graph.
     * @param vertex
     *            the vertex whose presence is to be checked.
     * @return <tt>true</tt> if this vertex is present in this graph;
     *         <tt>false</tt> otherwise.
     */
    public boolean containsVertex(V vertex) {
        return incommingEdgeMap.containsKey(vertex);
    }

    /**
     * Get the edge from the list of edges, if one exists between source vertex
     * and target vertex.
     * @param sourceVertex
     * @param targetVertex
     * @return edge object if it exists; null otherwise
     */
    public E getEdge(V sourceVertex, V targetVertex) {
        List<Edge> edges = outgoingEdgeMap.get(sourceVertex);

        if (edges != null) {
            Edge edge = getEdgeFromList(edges, targetVertex, false);

            if (edge != null)
                return edge.getEdgeObject();
        }
        return null;
    }

    /**
     * This method checks whether an edge lies between the source vertex and
     * target vertex.
     * @param sourceVertex
     * @param targetVertex
     * @return true if the edge exists; false otherwise
     */
    public boolean containsEdge(V sourceVertex, V targetVertex) {
        return getEdge(sourceVertex, targetVertex) != null;
    }

    /**
     * Remove the edge if it exists in the list of edges.
     * @param sourceVertex
     * @param targetVertex
     * @return removed edge if edge object is not null; null otherwise
     */
    public E removeEdge(V sourceVertex, V targetVertex) {
        List<Edge> incommingEdges = incommingEdgeMap.get(targetVertex);
        List<Edge> outgoingEdges = outgoingEdgeMap.get(sourceVertex);

        if (incommingEdges != null && outgoingEdges != null) {
            Edge edge = (Edge) getEdgeFromList(incommingEdges, sourceVertex,
                                               true);

            if (edge != null) {
                incommingEdges.remove(edge);
                outgoingEdges.remove(edge);
                return edge.getEdgeObject();
            }
        }
        return null;
    }

    /**
     * Put the edge into the list of edges if it does not exist. If the edge
     * exists return the old edge and replace it with a new edge.
     * @param sourceVertex
     * @param targetVertex
     * @param edge
     * @return the old edge if it exists; null otherwise
     */
    public E putEdge(V sourceVertex, V targetVertex, E edge) {
        Edge theEdge = (Edge) getEdgeFromList(
                                              outgoingEdgeMap.get(sourceVertex),
                                              targetVertex, false);

        if (theEdge == null) // The edge does not exist!!
        {
            // Adding vertex in the vertex list if not present.
            addVertex(sourceVertex);
            addVertex(targetVertex);

            // Add this Edge in outgoing & incomming Edges.
            Edge newEdge = new Edge(sourceVertex, targetVertex, edge);
            outgoingEdgeMap.get(sourceVertex).add(newEdge);
            incommingEdgeMap.get(targetVertex).add(newEdge);

            return null;
        } else
        // The edge already exists, so replace the existing edge and return it.
        {
            E oldEdge = theEdge.getEdgeObject();
            theEdge.setEdgeObject(edge);
            return oldEdge;
        }
    }

    /**
     * Checks if the graph is weakly connected. Graph will be connected if 1.
     * after Depth first traversing (without considering edge Direction) through
     * graph from (the only one)unreachable node, results into
     * @return true if graph is connected; false if graph is disjoint
     */
    public boolean isConnected() {
        boolean isConnected = true;

        List<V> unreachableNodes = getUnreachableNodeList();
        // if (unreachableNodes.size() > 1)
        // return false;

        Set<V> allVertexSet = new HashSet<V>();
        allVertexSet.addAll(incommingEdgeMap.keySet());

        dfs(unreachableNodes.get(0), allVertexSet);
        if (allVertexSet.isEmpty())
            isConnected = true;
        else
            // after traversing if the allVertexSet is not empty means its a
            // disconnected graph.
            isConnected = false;

        return isConnected;
    }

    /**
     * This method will return the list of vertices having no incomming Edges.
     * The node having no incomming edges will be treated as Root node.
     * @return list of vertices having no incomming Edges.
     */
    public List<V> getUnreachableNodeList() {
        List<V> list = new ArrayList<V>();

        Set<V> vertices = incommingEdgeMap.keySet();
        for (Iterator<V> iter = vertices.iterator(); iter.hasNext();) {
            V vertex = iter.next();
            List<Edge> incommingEdges = incommingEdgeMap.get(vertex);
            if (incommingEdges == null || incommingEdges.isEmpty())
                list.add(vertex);
        }
        return list;
    }

    /**
     * Removes the specified vertex from the list of vertices if one exists
     * @param vertex
     * @return true upon removing specified existing vertex; false otherwise
     */
    public boolean removeVertex(V vertex) {
        boolean flag = false;
        if (incommingEdgeMap.containsKey(vertex)) {
            removeAllEdgesOfVertex(vertex);

            // removing refernce of vertex from edge maps.
            incommingEdgeMap.remove(vertex);
            outgoingEdgeMap.remove(vertex);
            flag = true;
        }
        return flag;
    }

    /**
     * To remove all edges of Vertex. This will remove all incoming & outgoing
     * edges of given vertex.
     * @param vertex
     *            the reference to vertex.
     */
    private void removeAllEdgesOfVertex(V vertex) {
        // removing all incomming edges for the given vertex.
        List<Edge> incommingEdges = incommingEdgeMap.get(vertex);
        for (int index = 0; index < incommingEdges.size(); index++) {
            Edge edge = incommingEdges.get(index);
            removeEdge(edge.sourceVertex, edge.targetVertex);
        }

        // removing all outgoing edges for given vertex.
        List<Edge> outgoingEdges = outgoingEdgeMap.get(vertex);
        for (int index = 0; index < outgoingEdges.size(); index++) {
            Edge edge = outgoingEdges.get(index);
            removeEdge(edge.sourceVertex, edge.targetVertex);
        }
    }

    /**
     * This method checks whether adding node from sourceVertex to targetVertex
     * will result into cyclic graph or not. Adding an edge will result into
     * cycle only when the target vertex is reachable from source vertex i.e.
     * Vertex B is reachable from vertex A if and only if 1. There is direct
     * edge from vertex C to B 2. and C is reachable from A.
     * @param sourceVertex
     *            The source vertex of edge to be added.
     * @param targetVertex
     *            The target vertex of edge to be added.
     * @return true if the
     */
    private boolean isReverseReachable(V sourceVertex, V targetVertex) {
        if (sourceVertex.equals(targetVertex))
            return true; // finaly reached from source to target!!!
        List<Edge> edges = incommingEdgeMap.get(sourceVertex);
        for (int i = 0; i < edges.size(); i++) {
            boolean isCyclic = isReverseReachable(edges.get(i).sourceVertex,
                                                  targetVertex);
            if (isCyclic)
                return true;
        }

        return false;
    }

    /**
     * Method to traverse using Depth First algorithm. It removes the vertex
     * from allVertexSet while visiting each vertex. dfs of connected graph
     * should result into the allVetrexSet empty.
     * @param vertex
     *            The vertex to be visited.
     * @param allVertexSet
     *            Set of all nodes not visited yet.
     */
    private void dfs(V vertex, Set<V> allVertexSet) {
        allVertexSet.remove(vertex);
        List<Edge> edges = new ArrayList<Edge>();
        edges.addAll(outgoingEdgeMap.get(vertex));
        edges.addAll(incommingEdgeMap.get(vertex));

        if (edges != null) {
            for (int i = 0; i < edges.size(); i++) {
                Edge edge = edges.get(i);
                if (allVertexSet.contains(edge.targetVertex))
                    dfs(edge.targetVertex, allVertexSet); // this vertex is
                // not yet visited.
                if (allVertexSet.contains(edge.sourceVertex))
                    dfs(edge.sourceVertex, allVertexSet); // this vertex is
                // not yet visited.
            }
        }
    }

    /**
     * Get the edge for the specified target vertex.
     * @param edges
     * @param targetVertex
     * @return the edge if it exists; null otherwise
     */
    private Edge getEdgeFromList(List<Edge> edges, V targetVertex,
                                 boolean isIncommingEdge) {
        if (edges != null) {
            for (int i = 0; i < edges.size(); i++) {
                Edge edge = edges.get(i);
                if (isIncommingEdge) {
                    if (edge.getSourceVertex().equals(targetVertex))
                        return edge;
                } else {
                    if (edge.getTargetVertex().equals(targetVertex))
                        return edge;
                }
            }
        }
        return null;
    }

    /**
     * To get the list directly reachable Vertices from the given vertex.
     * @return List of Vertices directly reachable from the given vertex.
     *         Returns null if vertex is not present in graph, Returns empty
     *         list if vertex has no directly reachable node.
     */
    public List<V> getDirectSuccessorOf(V vertex) {
        List<Edge> edges = outgoingEdgeMap.get(vertex);
        List<V> vertices = null;
        if (edges != null) {
            vertices = new ArrayList<V>();
            Iterator<Edge> iter = edges.iterator();
            while (iter.hasNext()) {
                vertices.add(iter.next().targetVertex);

            }
        }
        return vertices;
    }

    /**
     * To get the list of vertices from which the given vertex is directly
     * reachable.
     * @return List of Vertices from which the given vertex is directly
     *         reachable. Returns null if vertex is not present in graph,
     *         Returns empty list if vertex has no incomming Edges.
     */
    public List<V> getDirectPredecessorOf(V vertex) {
        List<Edge> edges = incommingEdgeMap.get(vertex);
        List<V> vertices = null;
        if (edges != null) {
            vertices = new ArrayList<V>();
            Iterator<Edge> iter = edges.iterator();
            while (iter.hasNext()) {
                vertices.add(iter.next().sourceVertex);
            }
        }
        return vertices;
    }

    /**
     * All possible path between two vertices.
     * @param fromVertex
     *            the begining vertex.
     * @param toVetrex
     *            the ending vertix.
     * @return List of all paths, where path is again List of Vertices.
     * @throws IllegalArgumentException
     *             when the fromVetrex or toVetrex is not in the graph.
     */
    public List<List<V>> getReachablePaths(V fromVertex, V toVetrex) {
        if (!outgoingEdgeMap.containsKey(fromVertex)) {
            throw new IllegalArgumentException(
                    "fromVertex is not present in graph!!!");
        }
        if (!incommingEdgeMap.containsKey(toVetrex)) {
            throw new IllegalArgumentException(
                    "toVetrex is not present in graph!!!");
        }
        List<List<V>> paths = new ArrayList<List<V>>();
        List<Edge> edges = incommingEdgeMap.get(toVetrex);
        for (int i = 0; i < edges.size(); i++) {
            Edge edge = edges.get(i);
            if (fromVertex.equals(edge.sourceVertex)) {
                List<V> path = new ArrayList<V>();
                path.add(fromVertex);
                path.add(toVetrex);
                paths.add(path);
                continue;
            }

            List<List<V>> thePaths = getReachablePaths(fromVertex,
                                                       edge.sourceVertex);
            if (!thePaths.isEmpty()) {
                for (int j = 0; j < thePaths.size(); j++) {
                    List<V> path = thePaths.get(j);
                    path.add(toVetrex);
                }
                paths.addAll(thePaths);
            }
        }
        return paths;
    }

    /**
     * All possible path Edges between two vertices.
     * @param fromVertex
     *            the begining vertex.
     * @param toVetrex
     *            the ending vertix.
     * @return List of all path Edges, where path is again List of Vertices.
     * @throws IllegalArgumentException
     *             when the fromVetrex or toVetrex is not in the graph.
     */
    public List<List<E>> getReachableEdgePaths(V fromVertex, V toVetrex) {
        List<List<E>> edgePaths = new ArrayList<List<E>>();
        List<List<V>> verticesPaths = getReachablePaths(fromVertex, toVetrex);
        for (int i = 0; i < verticesPaths.size(); i++) {
            List<V> thePath = verticesPaths.get(i);
            List<E> theEdgePath = new ArrayList<E>();
            for (int j = 1; j < thePath.size(); j++) {
                theEdgePath.add(getEdge(thePath.get(j - 1), thePath.get(j)));
                edgePaths.add(theEdgePath);
            }
        }
        return edgePaths;
    }

    /**
     * To get the List of Vertices having outgoing Edges from given vertex.
     * @param vertex
     * @return
     * @throws IllegalArgumentException
     *             if the vertex does not exists in graph.
     */
    public List<V> getOutgoingVertices(V vertex) {
        if (!outgoingEdgeMap.containsKey(vertex)) {
            throw new IllegalArgumentException(
                    "vertex is not present in graph!!!");
        }
        List<Edge> edges = outgoingEdgeMap.get(vertex);
        List<V> vertices = new ArrayList<V>();

        for (int i = 0; i < edges.size(); i++) {
            vertices.add(edges.get(i).targetVertex);
        }
        return vertices;
    }

    /**
     * To get List of all vertices present in graph.
     * @return List of all vertices present in graph.
     */
    public Set<V> getVertices() {
        Set<V> vertices = new HashSet<V>();
        vertices.addAll(incommingEdgeMap.keySet());
        return vertices;
    }

    /**
     * @return list of all edges in this graph.
     */
    public Set<E> getEdges() {
        Set<E> res = new HashSet<E>();
        for (List<Edge> edges : incommingEdgeMap.values()) {
            for (Edge edge : edges) {
                res.add(edge.edgeObj);
            }
        }
        return res;
    }

    public boolean isTree() {
        if (!isConnected()) {
            return false;
        }
        boolean foundRoot = false;
        for (V vertex : getVertices()) {
            List<Edge> in = incommingEdgeMap.get(vertex);
            if (in.size() > 1) {
                return false;
            }
            if (in.isEmpty()) {
                if (foundRoot) {
                    return false;
                }
                foundRoot = true;
            }
        }
        return true;
    }

    /**
     * Checks if adding specified edge will cause a new cycle in the graph.
     * @param src
     *            src vertex
     * @param target
     *            target vertex
     * @return <tt>true</tt> if this edge will cause a new cycle in this
     *         graph;<tt>false</tt> otherwise.
     */
    public boolean willCauseNewCycle(V src, V target) {
        if (!(containsVertex(src) && containsVertex(target))) {
            return false;
        }
        return isReverseReachable(src, target);
    }

    /**
     * Checks if adding specified edge will violate the tree constraint "every
     * node must have atmost one parent".
     * @param src
     *            the src vertex
     * @param target
     *            the target vertex
     * @return <tt>true</tt> is adding the specified edge will violate the
     *         tree constraint; <tt>false</tt> otherwise.
     */
    public boolean willViolateTreeConstraint(V src, V target) {
        if (!containsVertex(target)) {
            return false;
        }
        if (containsEdge(src, target)) {
            return false;
        }
        return !(getDirectPredecessorOf(target).isEmpty() && getReachablePaths(
                                                                               target,
                                                                               src).isEmpty());
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return outgoingEdgeMap.toString();
    }

}