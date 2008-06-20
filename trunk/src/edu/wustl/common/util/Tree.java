package edu.wustl.common.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Tree<N, L> implements Serializable, Cloneable {
    private static final long serialVersionUID = 9023818990636649627L;

    private Graph<N, L> graph;

    private N root;

    public Tree(N root) {
        setRoot(root);
        initGraph();
        graph.addVertex(root);
    }

    private void initGraph() {
        graph = new Graph<N, L>();
    }

    public Tree(Tree<? extends N, ? extends L> tree) {
        assign(tree);
    }

    public Set<N> getChildren(N node) {
        return graph.getChildren(node);
    }

    public Map<N, L> getChildrenWithLabels(N node) {
        return graph.getOutgoingEdges(node);
    }

    public N getParent(N node) {
        if (isRoot(node)) {
            return null;
        }
        return first(graph.getIncomingEdges(node).keySet());
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
        N parent = getParent(child);
        L label = dissociateChild(child);
        putChild(child, parent, label);
        setRoot(child);
        return true;
    }

    public void setRoot(N root) {
        if (root == null) {
            throw new NullPointerException("tree node cannot be null.");
        }
        this.root = root;
    }

    public N getRoot() {
        return root;
    }

    public <N1 extends N, L1 extends L> void addSubTree(N parent, Tree<N1, L1> subTree, L label,
            final boolean overWrite) {
        N1 subTreeRoot = subTree.getRoot();
        if (!overWrite && getChildren(parent).contains(subTreeRoot)) {
            return;
        }
        putChild(parent, subTreeRoot, label);
        for (Map.Entry<N1, L1> entry : subTree.getChildrenWithLabels(subTreeRoot).entrySet()) {
            addSubTree(subTreeRoot, subTree.subTree(entry.getKey()), entry.getValue(), overWrite);
        }
    }

    public Tree<N, L> subTree(N node) {
        Tree<N, L> res = new Tree<N, L>(node);
        for (Map.Entry<N, L> entry : getChildrenWithLabels(node).entrySet()) {
            // no need for overwrite
            res.addSubTree(node, subTree(entry.getKey()), entry.getValue(), false);
        }
        return res;
    }

    public Tree<N, L> removeSubTree(N node) {
        Tree<N, L> subTree = subTree(node);
        for (N child : getChildren(node)) {
            removeSubTree(child);
            dissociateChild(child);
        }
        return subTree;
    }

    public List<N> getNodesPath(N node) {
        // TODO check TODO on vertexPaths
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
        initGraph();
        graph.assign(tree.graph);
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        if (root == null) {
            throw new NullPointerException("tree node cannot be null.");
        }
    }
}
