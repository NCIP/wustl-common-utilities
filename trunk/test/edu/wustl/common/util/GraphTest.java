package edu.wustl.common.util;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

/**
 * @author chandrakant_talele
 */
public class GraphTest extends TestCase {
    //vertex = city and edge = length
    
    public void testEmptyGraph() {
        Graph<City, Integer> g = new Graph<City, Integer>();
        assertFalse(g.addVertices(new HashSet<City>()));
        assertEquals(0, g.getVertices().size());
        assertEquals(0, g.getEdges().size());
        assertFalse(g.isConnected());
        assertFalse(g.isTree());
    }

    public void testOnlyNodes() {
        Set<City> cities = new HashSet<City>();
        cities.add(new City("Nasik"));
        cities.add(new City("Pune"));
        cities.add(new City("Mumbai"));

        Graph<City, Integer> g = new Graph<City, Integer>();
        assertTrue(g.addVertices(cities));
        assertEquals(cities, g.getVertices());

        assertEquals(cities.size(), g.getVertices().size());
        assertEquals(0, g.getEdges().size());
        assertFalse(g.isConnected());
        assertFalse(g.isTree());
    }

    public void testPutEdge() {
        City nsk = new City("Nasik");
        City pune = new City("Pune");
        Set<City> cities = new HashSet<City>();
        cities.add(nsk);
        cities.add(pune);

        Graph<City, Integer> g = new Graph<City, Integer>();
        assertTrue(g.addVertices(cities));

        assertNull(g.putEdge(nsk, pune, 220));
        assertEquals(220, g.putEdge(nsk, pune, 220).intValue());
        assertTrue(g.isConnected());
        assertTrue(g.isTree());
    }

    public void testPutEdgeAndDisconnectedNode() {
        City nsk = new City("Nasik");
        City pune = new City("Pune");

        Set<City> cities = new HashSet<City>();
        cities.add(nsk);
        cities.add(pune);
        cities.add(new City("Mumbai"));

        Graph<City, Integer> g = new Graph<City, Integer>();
        assertTrue(g.addVertices(cities));

        assertNull(g.putEdge(nsk, pune, 220));
        assertEquals(220, g.putEdge(nsk, pune, 220).intValue());
        assertFalse(g.isConnected());
        assertFalse(g.isTree());
    }

    public void testFullyConnected() {
        int max = 6;

        Set<City> cities = new HashSet<City>(max);
        for (int i = 0; i < max; i++) {
            cities.add(new City("City:" + i));
        }

        Graph<City, Integer> graph = new Graph<City, Integer>();
        int j = 0;
        for (City c1 : cities) {
            for (City c2 : cities) {
                Integer res = graph.putEdge(c1, c2, j);
                assertNull(res);
                j++;
            }
        }
        assertTrue(graph.isConnected());
        int p = 100;
        j = 0;
        for (City c1 : cities) {
            for (City c2 : cities) {
                Integer res = graph.putEdge(c1, c2, p);
                assertEquals(j, res.intValue());
                j++;
                p++;
            }
        }
        for (City c1 : cities) {
            assertTrue(graph.containsVertex(c1));
            for (City c2 : cities) {
                assertTrue(graph.containsEdge(c1, c2));
            }
        }

        assertEquals(max*max, graph.getEdges().size());
    }

    public void testSelf() {
        City nsk = new City("Nasik");
        City pune = new City("Pune");

        Graph<City, Integer> g = new Graph<City, Integer>();
        g.putEdge(nsk, nsk, 0);
        g.putEdge(pune, pune, 0);
        g.putEdge(nsk, pune, 220);

        assertEquals(1, g.getIncomingEdges(nsk).size());
        assertEquals(2, g.getIncomingEdges(pune).size());

        assertEquals(2, g.getOutgoingEdges(nsk).size());
        assertEquals(1, g.getOutgoingEdges(pune).size());

        assertEquals(1, g.getInNeighbours(nsk).size());

    }

}

class City {
    private String name;

    public City(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof City)) {
            return false;
        }
        City city = (City) obj;
        return name.equals(city.name);
    }

    @Override
    public String toString() {
        return name;
    }
}

//class Route {
//    private Integer length;
//
//    private String label;
//
//    public Route(Integer length, String label) {
//        this.length = length;
//        this.label = label;
//    }
//    @Override
//    public int hashCode() {
//        return has;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (obj == this) {
//            return true;
//        }
//        if (!(obj instanceof Edge)) {
//            return false;
//        }
//        Edge e = (Edge) obj;
//        return id == e.getId() && weight.equals(e.getWeight());
//    }
//}
