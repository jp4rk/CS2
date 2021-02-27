package edu.caltech.cs2.datastructures;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.IDictionary;
import edu.caltech.cs2.interfaces.ISet;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class BeaverMapsGraph extends Graph<Long, Double> {
    private static JsonParser JSON_PARSER = new JsonParser();

    private IDictionary<Long, Location> ids;
    private ISet<Location> buildings;

    public BeaverMapsGraph() {
        super();
        this.buildings = new ChainingHashSet<>();
        this.ids = new ChainingHashDictionary<>(MoveToFrontDictionary::new);
    }

    /**
     * Reads in buildings, waypoinnts, and roads file into this graph.
     * Populates the ids, buildings, vertices, and edges of the graph
     * @param buildingsFileName the buildings filename
     * @param waypointsFileName the waypoints filename
     * @param roadsFileName the roads filename
     */
    public BeaverMapsGraph(String buildingsFileName, String waypointsFileName, String roadsFileName) {
        this();
        JsonElement bs = fromFile(buildingsFileName);
        for (JsonElement b : bs.getAsJsonArray()) {
            Location loc = new Location(b.getAsJsonObject());
            buildings.add(loc);
            ids.put(loc.id, loc);
            this.addVertex(loc.id);
            }
        JsonElement ws = fromFile(waypointsFileName);
        for (JsonElement w : ws.getAsJsonArray()) {
            Location loc = new Location(w.getAsJsonObject());
            ids.put(loc.id, loc);
            this.addVertex(loc.id);
        }
        JsonElement rs = fromFile(roadsFileName);
        for (JsonElement b : rs.getAsJsonArray()) {
            Long prev = null;
            for(JsonElement r: b.getAsJsonArray()){
                if(prev != null){
                    this.addUndirectedEdge(prev, r.getAsLong(), ids.get(prev).getDistance(ids.get(r.getAsLong())));
                }
                prev = r.getAsLong();
            }
        }

    }

    /**
     * Returns a deque of all the locations with the name locName.
     * @param locName the name of the locations to return
     * @return a deque of all location with the name locName
     */
    public IDeque<Location> getLocationByName(String locName) {
        IDeque<Location> locs = new LinkedDeque<>();
        for(Location l : ids.values()){
            if(l.name != null && l.name.contains(locName)){
                locs.add(l);
            }
        }
        return locs;
    }

    /**
     * Returns the Location object corresponding to the provided id
     * @param id the id of the object to return
     * @return the location identified by id
     */
    public Location getLocationByID(long id) {
        return ids.get(id);
    }

    /**
     * Adds the provided location to this map.
     * @param n the location to add
     * @return true if n is a new location and false otherwise
     */
    public boolean addVertex(Location n) {
        Location t = ids.put(n.id, n);
        return t == null;
    }

    /**
     * Returns the closest building to the location (lat, lon)
     * @param lat the latitude of the location to search near
     * @param lon the longitute of the location to search near
     * @return the building closest to (lat, lon)
     */
    public Location getClosestBuilding(double lat, double lon) {
        Location close = buildings.iterator().next();
        for(Location l: buildings){
            if(l.getDistance(lat, lon) < close.getDistance(lat, lon)){
                close = l;
            }
        }
        return close;
    }

    /**
     * Returns a set of locations which are no more than threshold feet
     * away from start.
     * @param start the location to search around
     * @param threshold the number of feet in the search radius
     * @return
     */
    public ISet<Location> dfs(Location start, double threshold) {
        ISet<Location> locs = new ChainingHashSet<>();
        for(Location l: ids.values()){
            if(l.getDistance(start) < threshold){
                locs.add(l);
            }
        }
        return locs;
    }

    /**
     * Returns a list of Locations corresponding to
     * buildings in the current map.
     * @return a list of all building locations
     */
    public ISet<Location> getBuildings() {
        return this.buildings;
    }

    /**
     * Returns a shortest path (i.e., a deque of vertices) between the start
     * and target locations (including the start and target locations).
     * @param start the location to start the path from
     * @param target the location to end the path at
     * @return a shortest path between start and target
     */
    public IDeque<Location> dijkstra(Location start, Location target) {
        // TODO (student): Write This
        return null;
    }

    /**
     * Returns a JsonElement corresponding to the data in the file
     * with the filename filename
     * @param filename the name of the file to return the data from
     * @return the JSON data from filename
     */
    private static JsonElement fromFile(String filename) {
        try {
            return JSON_PARSER.parse(
                    new FileReader(
                            new File(filename)
                    )
            );
        } catch (IOException e) {
            return null;
        }
    }

    public static void main(String[] args){
        BeaverMapsGraph temp = new BeaverMapsGraph();
    }
}
