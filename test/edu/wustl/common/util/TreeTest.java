package edu.wustl.common.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

public class TreeTest extends TestCase {
    public void testBasic() {
        Tree<Integer, String> tree;
        try {
            tree = newTree(null);
            fail();
        } catch (NullPointerException e) {

        }
        tree = newTree(1);
        assertTrue(tree.containsNode(1));
        assertFalse(tree.containsNode(2));
        assertEquals(integer(1), tree.getRoot());
        assertTrue(tree.getChildren(1).isEmpty());
        assertNull(tree.getLabel(1));
        assertNull(tree.getParent(1));
        assertTrue(tree.isRoot(1));
        assertTrue(tree.isLeaf(1));
        tree.setLabel(1, "1");
        assertNull(tree.getLabel(1));

        tree.putChild(1, 2, "12");
        assertTrue(tree.containsNode(1));
        assertTrue(tree.containsNode(2));
        assertEquals(integer(1), tree.getRoot());
        assertEquals(asSet(2), tree.getChildren(1));
        assertNull(tree.getLabel(1));
        assertNull(tree.getParent(1));
        assertEquals("12", tree.getLabel(2));
        assertEquals(integer(1), tree.getParent(2));
        assertTrue(tree.getChildren(2).isEmpty());
        assertTrue(tree.isRoot(1));
        assertFalse(tree.isLeaf(1));
        assertFalse(tree.isRoot(2));
        assertTrue(tree.isLeaf(2));

        Tree<Integer, String> temp = tree.clone();

        tree.setLabel(2, "21");
        assertEquals("21", tree.getLabel(2));

        tree.putChild(1, 2, "12");
        assertEquals(temp, tree);
    }

    public void testMakeRoot() {
        Tree<Integer, String> tree = newTree(1);
        assertFalse(tree.makeRoot(1));
        tree.putChild(1, 2, "12");

        Tree<Integer, String> orig = tree.clone();
        assertFalse(tree.makeRoot(1));

        Tree<Integer, String> expected = newTree(2);
        expected.putChild(2, 1, "12");
        assertTrue(tree.makeRoot(2));
        assertEquals(expected, tree);

        assertTrue(tree.makeRoot(1));
        assertEquals(orig, tree);

        tree.putChild(1, 3, "13"); // 1(2, 3)
        expected.putChild(1, 3, "13");
        assertFalse(tree.makeRoot(1));
        assertTrue(tree.makeRoot(2));
        assertEquals(expected, tree); // 2(1(3))

        expected = newTree(3); // 3(1(2))
        expected.putChild(3, 1, "13");
        expected.putChild(1, 2, "12");
        assertTrue(tree.makeRoot(3));
        assertEquals(expected, tree);

        tree = expected.clone();
        expected = newTree(1);
        expected.putChild(1, 2, "12");
        expected.putChild(1, 3, "13");
        assertTrue(tree.makeRoot(1));
        assertEquals(expected, tree);
    }

    public void testSubTree() {
        Tree<Integer, String> tree = newTree(1);
        assertEquals(tree, tree.subTree(1));

        tree.putChild(1, 2, "12");
        assertEquals(tree, tree.subTree(1));
        assertEquals(newTree(2), tree.subTree(2));

        tree.putChild(1, 3, "13");
        assertEquals(tree, tree.subTree(1));
        assertEquals(newTree(2), tree.subTree(2));
        assertEquals(newTree(3), tree.subTree(3));

        tree.putChild(2, 4, "24");
        assertEquals(tree, tree.subTree(1));
        assertEquals(newTree(3), tree.subTree(3));
        assertEquals(newTree(4), tree.subTree(4));
        Tree<Integer, String> subTree2 = newTree(2);
        subTree2.putChild(2, 4, "24");
        assertEquals(subTree2, tree.subTree(2));

        tree.putChild(2, 5, "25");
        subTree2.putChild(2, 5, "25");
        assertEquals(tree, tree.subTree(1));
        assertEquals(subTree2, tree.subTree(2));
    }

    public void testAddSubTree() {
        Tree<Integer, String> tree = newTree(1);
        Tree<Integer, String> expected = newTree(1);

        tree.addSubTree(1, newTree(2), "12");
        expected.putChild(1, 2, "12");

        assertEquals(expected, tree);

        Tree<Integer, String> subTree = newTree(3);
        subTree.putChild(3, 4, "34");
        subTree.putChild(3, 5, "35");

        expected.putChild(1, 3, "13");
        expected.putChild(3, 4, "34");
        expected.putChild(3, 5, "35");

        tree.addSubTree(1, subTree, "13");
        assertEquals(expected, tree);

        subTree = newTree(6);
        subTree.putChild(6, 7, "67");
        subTree.putChild(7, 8, "78");
        subTree.putChild(6, 9, "69");

        expected.putChild(2, 6, "26");
        expected.putChild(6, 7, "67");
        expected.putChild(7, 8, "78");
        expected.putChild(6, 9, "69");

        tree.addSubTree(2, subTree, "26");
        assertEquals(expected, tree);
    }

    public void testRemoveSubtree() {
        Tree<Integer, String> tree = newTree(1);
        assertEquals(tree, tree.removeSubTree(1));

        Tree<Integer, String> removed = newTree(2);
        Tree<Integer, String> remaining = newTree(1);

        tree.putChild(1, 2, "12");
        checkRemoval(tree, 2, removed, remaining);

        tree.putChild(1, 2, "12");
        tree.putChild(1, 3, "13");
        remaining.putChild(1, 3, "13");
        checkRemoval(tree, 2, removed, remaining);

        tree.putChild(1, 2, "12");
        tree.putChild(2, 4, "24");
        tree.putChild(2, 5, "25");

        removed.putChild(2, 4, "24");
        removed.putChild(2, 5, "25");
        checkRemoval(tree, 2, removed, remaining);

        tree.putChild(1, 2, "12");
        tree.putChild(2, 4, "24");
        tree.putChild(2, 5, "25");
        tree.putChild(4, 6, "46");

        removed = newTree(4);
        removed.putChild(4, 6, "46");

        remaining.putChild(1, 2, "12");
        remaining.putChild(2, 5, "25");
        checkRemoval(tree, 4, removed, remaining);
        
        remaining = newTree(1);
        remaining.putChild(1, 2, "12");
        remaining.putChild(2, 5, "25");
        removed = newTree(3);
        checkRemoval(tree, 3, removed, remaining);
    }

    private void checkRemoval(Tree<Integer, String> tree, int nodeToRemove, Tree<Integer, String> removed,
            Tree<Integer, String> remaining) {
        assertEquals(removed, tree.removeSubTree(nodeToRemove));
        assertEquals(remaining, tree);
    }

    private Tree<Integer, String> newTree(Integer root) {
        return new Tree<Integer, String>(root);
    }

    private Integer integer(int i) {
        return new Integer(i);
    }

    private static <T> Set<T> asSet(T... elems) {
        Set<T> res = new HashSet<T>();
        for (T elem : elems) {
            res.add(elem);
        }
        return res;
    }

    private static <T> List<T> asList(T... elems) {
        List<T> res = new ArrayList<T>();
        for (T elem : elems) {
            res.add(elem);
        }
        return res;
    }
}
