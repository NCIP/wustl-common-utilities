/**
 * 
 */

package edu.wustl.common.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.wustl.common.util.Graph;

import junit.framework.TestCase;

/**
 * @author prafull_kadam Test Case class for the Graph class.
 */
public class GraphTestCase extends TestCase {

    Graph<Integer, String> graph;

    final Integer one = new Integer(1);

    final Integer two = new Integer(2);

    final Integer three = new Integer(3);

    final Integer four = new Integer(4);

    final Integer five = new Integer(5);

    final Integer six = new Integer(6);

    final String oneTwo = "12";

    final String oneThree = "13";

    final String ThreeFour = "34";

    final String fourFive = "45";

    final String sixFive = "65";

    final String fiveFour = "54";

    /**
     * @see junit.framework.TestCase#setUp() creates Graph as: 1---> 2 \--> 3
     *      --> 4
     */
    @Override
    protected void setUp() throws Exception {
        graph = new Graph<Integer, String>();
        graph.putEdge(one, two, oneTwo);
        graph.putEdge(one, three, oneThree);
        graph.putEdge(three, four, ThreeFour);
        super.setUp();
    }

    /**
     * by Adding edge between 4 & 5 shold not throw CyclicException
     */
    public void testPutEdge2() {
        String edge = graph.putEdge(four, five, fourFive);
        assertNull(
                   "Expected null value from putEdge method since edge does not Exist!!!",
                   edge);
        edge = graph.getEdge(four, five);
        assertEquals("Unequal edge got from Graph!!!", edge, fourFive);
    }

    /**
     * The existing graph is connected graph.
     */
    public void testIsConnected1() {
        assertTrue("Expected true value from isConnected method!!!",
                   graph.isConnected());
    }

    /**
     * Adding edge 5 to 6 in the existing graph will result into disconnected
     * graph.
     */
    public void testIsConnected2() {
        graph.putEdge(six, five, sixFive);
        assertFalse("Expected false value from isConnected method!!!",
                    graph.isConnected());
    }

    /**
     * Adding edge 5 to 4 in the existing graph will not result into
     * disconnected graph. 1---> 2 \--> 3 --> 4 <-- 5
     */
    public void testIsConnected3() {
        graph.putEdge(five, four, fiveFour);
        assertTrue("Expected true value from isConnected method!!!",
                   graph.isConnected());
    }

    /**
     * To test UnreachablNode list methos for graph. In the actual graph only
     * vertex "1" is unreachable. After adding edge in 5 -> 4,
     * UnreachableNodeList will contain 1 & 5
     */
    public void testGetUnreachableList() {
        List<Integer> actualList = new ArrayList<Integer>();
        actualList.add(one);
        assertEquals("incorrect unreachableNodeList!!!",
                     graph.getUnreachableNodeList(), actualList);

        actualList.add(five);
        graph.putEdge(five, four, fiveFour);
        assertEquals("incorrect unreachableNodeList!!!",
                     graph.getUnreachableNodeList(), actualList);
    }

    /**
     * To test getReachablePaths(V,V) method. modifies Graph as: 1--->
     * 2------->\ \ \ \--------->\ \ \ \ \ \--> 3 --> 4 --> 5
     * graph.getReachablePaths(1,5) should give following paths: 1. [1, 3, 4, 5]
     * 2. [1, 4, 5] 3. [1, 2, 5] graph.getReachablePaths(5,1) should return
     * empty path list.
     */

    public void testgetReachablePaths1() {
        graph.putEdge(four, five, fourFive);
        graph.putEdge(two, five, "25");
        graph.putEdge(one, four, "14");
        List<List<Integer>> actualPaths = graph.getReachablePaths(one, five);

        List<List<Integer>> paths = new ArrayList<List<Integer>>();
        List<Integer> path1 = new ArrayList<Integer>();
        path1.add(one);
        path1.add(three);
        path1.add(four);
        path1.add(five);
        paths.add(path1);
        List<Integer> path2 = new ArrayList<Integer>();
        path2.add(one);
        path2.add(four);
        path2.add(five);
        paths.add(path2);
        List<Integer> path3 = new ArrayList<Integer>();
        path3.add(one);
        path3.add(two);
        path3.add(five);
        paths.add(path3);

        // System.out.println("Graph:"+graph.toString());
        // displayPaths(paths);
        // displayPaths(actualPaths);

        assertEquals("Expected paths not found!!!", paths, actualPaths);

        paths = new ArrayList<List<Integer>>();
        actualPaths = graph.getReachablePaths(five, one);
        assertEquals("Expected no paths between two vertices!!!", paths,
                     actualPaths);
    }

    /**
     * To test getReachablePaths(V,V) method. Call to method
     * graph.getReachablePaths(vertex1,vertex2) should throw an Exception when
     * any of vertex1 or vertex2 is not present in the graph. if There is no
     * path between two vertices then cal to this method should return empty
     * list.
     */
    public void testGetReachablePaths2() {
        try {
            List<List<Integer>> actualPaths = graph.getReachablePaths(one, five);
            assertTrue("Expected IllegalArgument Exception !!!", false);
        } catch (IllegalArgumentException e) {
        }
        List<List<Integer>> actualPaths = graph.getReachablePaths(three, two);
        List<List<Integer>> paths = new ArrayList<List<Integer>>();
        assertEquals("Expected no paths between two vertices!!!", paths,
                     actualPaths);
    }

    /**
     * method to display paths
     * @param paths
     */
    private void displayPaths(List<List<Integer>> paths) {
        for (Iterator<List<Integer>> iter = paths.iterator(); iter.hasNext();) {
            System.out.println("path::" + iter.next());
        }
    }

    public void testIsTree() {
        assertTrue(graph.isTree());
        graph.putEdge(1, 1, "oneFour");
        assertFalse(graph.isTree());
    }
}
