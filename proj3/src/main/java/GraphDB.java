import example.CSCourseDB;
import org.eclipse.jetty.xml.XmlParser;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.*;

/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    /** Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc. */
    private final Map<Long, GraphDB.Node> nodes = new LinkedHashMap<>();
    private final Map<Long, GraphDB.Edge> edges = new LinkedHashMap<>();
    private Map<Long,ArrayList<Long>> adj = new HashMap<>();
    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            FileInputStream inputStream = new FileInputStream(inputFile);
            // GZIPInputStream stream = new GZIPInputStream(inputStream);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputStream, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    static class Node {
        long id;
        double lon;
        double lat;
        double priority;
        String name;


        Node(long id, double lon, double lat) {
            this.id = id;
            this.lon = lon;
            this.lat = lat;
        }
    }

    class NodeComparator implements Comparator<Long> {
        @Override
        public int compare(Long id1, Long id2) {
            return Double.compare(nodes.get(id1).priority, nodes.get(id2).priority);
        }
    }

    public Comparator<Long> getNodeComparator() {
        return new NodeComparator();
    }

    void setPriority(long id, double p) {
        nodes.get(id).priority = p;
    }
    Edge getWay(long node1, long node2) {
        for(Map.Entry<Long,Edge> entry: edges.entrySet()) {
            if (entry.getValue().nodesOfway.contains(node1) &&
                    entry.getValue().nodesOfway.contains(node2)) {
                return entry.getValue();
            }
        }
        return null;
    }


    Boolean nodeInWay(long node, Edge way) {
        return way.nodesOfway.contains(node);
    }


    static class Edge {
        long id;
        String name = "";
        String maxspeed;
        ArrayList<Long> nodesOfway = new ArrayList<>();

        boolean highway;

        Edge(long id) {
            this.id = id;
            highway = false;
        }
    }

    void addNode(GraphDB.Node n) {
        this.nodes.put(n.id, n);
    }

    void addEdge(GraphDB.Edge e) {
        this.edges.put(e.id, e);
    }

    void connectNodes(GraphDB.Edge current) {
        ArrayList<Long> lstOfNodes= current.nodesOfway;
        ArrayList<Long> adjOfNodes;
        if (lstOfNodes.size() > 1) {
            for (int i = 0; i < lstOfNodes.size(); i++) {
                adjOfNodes = adj.get(lstOfNodes.get(i));
                if (i == 0) {
                    if (adjOfNodes == null) {
                        adjOfNodes = new ArrayList<>();
                    }
                    adjOfNodes.add(lstOfNodes.get(i + 1));
                } else if (i == lstOfNodes.size() - 1) {
                    if (adjOfNodes == null) {
                        adjOfNodes = new ArrayList<>();
                    }
                    adjOfNodes.add(lstOfNodes.get(i - 1));
                } else {
                    if (adjOfNodes == null) {
                        adjOfNodes = new ArrayList<>();
                    }
                    adjOfNodes.add(lstOfNodes.get(i - 1));
                    adjOfNodes.add(lstOfNodes.get(i + 1));
                }
                adj.put(lstOfNodes.get(i), adjOfNodes);
            }
        }
    }


    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        ArrayList<Long> nodesTobeDeleted = new ArrayList<>();
        for (long id : nodes.keySet()){
            if(!adj.containsKey(id)) {
                nodesTobeDeleted.add(id);
            }
        }
        for (long id : nodesTobeDeleted) {
            nodes.remove(id);
        }
    }

    /**
     * Returns an iterable of all vertex IDs in the graph.
     * @return An iterable of id's of all vertices in the graph.
     */
    Iterable<Long> vertices() {
        //YOUR CODE HERE, this currently returns only an empty list.
        return adj.keySet();
    }

    /**
     * Returns ids of all vertices adjacent to v.
     * @param v The id of the vertex we are looking adjacent to.
     * @return An iterable of the ids of the neighbors of v.
     */
    Iterable<Long> adjacent(long v) {
        return adj.get(v);
    }

    /**
     * Returns the great-circle distance between vertices v and w in miles.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The great-circle distance between the two locations from the graph.
     */
    double distance(long v, long w) {
        return distance(lon(v), lat(v), lon(w), lat(w));
    }

    static double distance(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double dphi = Math.toRadians(latW - latV);
        double dlambda = Math.toRadians(lonW - lonV);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 3963 * c;
    }

    /**
     * Returns the initial bearing (angle) between vertices v and w in degrees.
     * The initial bearing is the angle that, if followed in a straight line
     * along a great-circle arc from the starting point, would take you to the
     * end point.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The initial bearing between the vertices.
     */
    double bearing(long v, long w) {
        return bearing(lon(v), lat(v), lon(w), lat(w));
    }

    static double bearing(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double lambda1 = Math.toRadians(lonV);
        double lambda2 = Math.toRadians(lonW);

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    long closest(double lon, double lat) {
        long closetNode = -1;
        double minDis = Double.MAX_VALUE;
        double dis;
        for(long id : adj.keySet()) {
            dis = Math.abs(distance(nodes.get(id).lon, nodes.get(id).lat, lon, lat ));
            if( dis < minDis) {
                minDis = dis;
                closetNode = id;
            }
        }
        return closetNode;
    }

    /**
     * Gets the longitude of a vertex.
     * @param v The id of the vertex.
     * @return The longitude of the vertex.
     */
    double lon(long v) {
        return nodes.get(v).lon;
    }

    /**
     * Gets the latitude of a vertex.
     * @param v The id of the vertex.
     * @return The latitude of the vertex.
     */
    double lat(long v) {
        return nodes.get(v).lat;
    }
}
