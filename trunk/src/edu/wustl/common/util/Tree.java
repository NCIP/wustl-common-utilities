package edu.wustl.common.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A rooted, labeled tree.
 * <p>
 * Each node in the tree has an associated (possibly <tt>null</tt>)label;
 * this label is to be considered the label on the edge from its parent node.
 * Thus, the label on the root node is always <tt>null</tt>.
 * 
 * Note that <tt>null</tt> nodes are not allowed. Thus, all methods throw a
 * <tt>NullPointerException</tt> if a specified node parameter is
 * <tt>null</tt>. <br>
 * Many methods in this class throw an <tt>IllegalArgumentException</tt> if a
 * specified node is not present in the tree. <br>
 * Note that modifications to the collections returned by the methods DO NOT
 * affect the tree.<br>
 * 
 * @author srinath_k
 * 
 * @param <N> the type of nodes
 * @param <L> the type of labels
 */
public class Tree<N, L> implements Serializable, Cloneable {
    private static final long serialVersionUID = 9023818990636649627L;

    private Graph<N, L> graph;

    private transient N root;

    public Tree(N root) {
        if (root == null) {
            throw new NullPointerException();
        }
        this.root = root;
        initGraph();
    }

    private void initGraph() {
        graph = new Graph<N, L>();
        graph.addVertex(root);
    }

    public Tree(Tree<? extends N, ? extends L> tree) {
        assign(tree);
    }

    public Set<N> getChildren(N node) {
        return graph.getOutNeighbours(node);
    }

    public Map<N, L> getChildrenWithLabels(N node) {
        return graph.getOutgoingEdges(node);
    }

    public N getParent(N node) {
        if (isRoot(node)) {
            return null;
        }
        return first(graph.getInNeighbours(node));
    }

    public L getLabel(N child) {
        if (isRoot(child)) {
            return null;
        }
        return graph.getEdge(getParent(child), child);
    }

    public void setLabel(N child, L label) {
        if (!isRoot(child)) {
            putChild(getParent(child), child, label);
        }
    }

    public boolean isLeaf(N node) {
        return graph.getOutgoingEdges(node).isEmpty();
    }

    public boolean isRoot(N node) {
        return root.equals(node);
    }

    public void putChild(N parent, N child, L label) {
        if (parent.equals(child)) {
            throw new IllegalArgumentException();
        }
        graph.validateVertex(parent);
        if (graph.containsVertex(child)) {
            dissociateChild(child);
        }
        graph.putEdge(parent, child, label);
    }

    private L dissociateChild(N child) {
        if (isRoot(child)) {
            return null;
        }
        return graph.removeEdge(getParent(child), child);
    }

    public boolean makeRoot(N child) {
        if (isRoot(child)) {
            return false;
        }
        // N parent = getParent(child);
        // L label = dissociateChild(child);
        // putChild(child, parent, label);
        N newRoot = child;
        while (!isRoot(child)) {
            N parent = getParent(child);
            L label = graph.removeEdge(parent, child);
            graph.putEdge(child, parent, label);
            child = parent;
        }
        this.root = newRoot;
        return true;
    }

    public void setRoot(N newRoot, L label) {
        if (newRoot == null) {
            throw new NullPointerException("tree node cannot be null.");
        }
        if (getRoot().equals(newRoot)) {
            return;
        }
        putChild(root, newRoot, label);
        makeRoot(newRoot);
    }

    public N getRoot() {
        return root;
    }

    public void addSubTree(N parent, Tree<? extends N, ? extends L> subTree, L label) {
        if (overlaps(subTree)) {
            throw new IllegalArgumentException();
        }
        addSubTree2(parent, subTree, label);
    }

    private <N1 extends N, L1 extends L> void addSubTree2(N parent, Tree<N1, L1> subTree, L label) {
        N1 subTreeRoot = subTree.getRoot();
        putChild(parent, subTreeRoot, label);
        for (Map.Entry<N1, L1> entry : subTree.getChildrenWithLabels(subTreeRoot).entrySet()) {
            addSubTree2(subTreeRoot, subTree.subTree(entry.getKey()), entry.getValue());
        }
    }

    private boolean overlaps(Tree<?, ?> other) {
        return allNodes().removeAll(other.allNodes());
    }

    public Set<N> allNodes() {
        return graph.getVertices();
    }

    public Tree<N, L> subTree(N node) {
        Tree<N, L> res = new Tree<N, L>(node);
        for (Map.Entry<N, L> entry : getChildrenWithLabels(node).entrySet()) {
            // no need for overwrite
            res.addSubTree(node, subTree(entry.getKey()), entry.getValue());
        }
        return res;
    }

    public Tree<N, L> removeSubTree(N node) {
        Tree<N, L> subTree = subTree(node);
        for (N child : getChildren(node)) {
            removeSubTree(child);
            graph.removeVertex(child);
        }
        if (!isRoot(node))
            graph.removeVertex(node);
        return subTree;
    }

    public boolean containsNode(N node) {
        return graph.containsVertex(node);
    }

    public boolean containsAnyNode(Tree<? extends N, ? extends L> other) {
        for (N node : other.graph.getVertices()) {
            if (containsNode(node)) {
                return true;
            }
        }
        return false;
    }

    public List<N> getNodesPath(N node) {
        return first(graph.getVertexPaths(getRoot(), node));
    }

    public List<L> getLabelsPath(N node) {
        return first(graph.getEdgePaths(getRoot(), node));
    }

    private static <T> T first(Set<T> set) {
        if (set.isEmpty()) {
            return null;
        } else {
            return set.iterator().next();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Tree)) {
            return false;
        }
        Tree other = (Tree) obj;
        return graph.equals(other.graph);
    }

    @Override
    public int hashCode() {
        return graph.hashCode();
    }

    @Override
    public String toString() {
        return graph.toString();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Tree<N, L> clone() {
        Tree<N, L> res = null;
        try {
            res = (Tree<N, L>) super.clone();
        } catch (CloneNotSupportedException e) {
            // can't occur
        }
        res.assign(this);
        return res;
    }

    public void assign(Tree<? extends N, ? extends L> tree) {
        if (tree == null) {
            throw new NullPointerException();
        }
        initGraph();
        graph.assign(tree.graph);
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        if (!graph.isTree()) {
            throw new IllegalArgumentException();
        }
        Set<N> roots = graph.getUnreachableVertices();
        if (roots.size() != 1) {
            throw new IllegalArgumentException();
        }
        root = first(roots);
    }
}
