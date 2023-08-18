import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.*;

public class TravelMap {

    // Maps a single Id to a single Location.
    public Map<Integer, Location> locationMap = new HashMap<>();

    // List of locations, read in the given order
    public List<Location> locations = new ArrayList<>();

    // List of trails, read in the given order
    public List<Trail> trails = new ArrayList<>();

    // Neighbour list
    public Map<Location, List<Location>> neighbours = new HashMap<Location, List<Location>>();

    // TODO: You are free to add more variables if necessary.

    public void initializeMap(String filename) {
        // Read the XML file and fill the instance variables locationMap, locations and trails.
        // TODO: Your code here
        try{
            File file = new File(filename);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();

            NodeList locations = doc.getElementsByTagName("Locations").item(0).getChildNodes();
            NodeList trails = doc.getElementsByTagName("Trails").item(0).getChildNodes();

            for(int i=0;i<locations.getLength();i++){
                Node n = locations.item(i);
                if(n.getNodeType() == Node.ELEMENT_NODE){
                    Element e = (Element) n;

                    String name = e.getElementsByTagName("Name").item(0).getTextContent();
                    int id = Integer.parseInt(e.getElementsByTagName("Id").item(0).getTextContent());

                    Location loc = new Location(name,id);

                    this.locationMap.put(id,loc);
                    this.locations.add(loc);

                    this.neighbours.put(loc, new ArrayList<Location>());
                }
            }

            for(int i = 0; i<trails.getLength();i++){
                Node n = trails.item(i);

                if(n.getNodeType() == Node.ELEMENT_NODE){
                    Element e = (Element) n;

                    int source = Integer.parseInt(e.getElementsByTagName("Source").item(0).getTextContent());
                    int dest = Integer.parseInt(e.getElementsByTagName("Destination").item(0).getTextContent());
                    int danger = Integer.parseInt(e.getElementsByTagName("Danger").item(0).getTextContent());

                    Trail tr = new Trail(locationMap.get(source),locationMap.get(dest),danger);
                    this.trails.add(tr);

                    this.neighbours.get(locationMap.get(source)).add(locationMap.get(dest));
                    this.neighbours.get(locationMap.get(dest)).add(locationMap.get(source));

                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<Trail> getSafestTrails() {
        List<Trail> safestTrails = new ArrayList<>();

        Map<Location, Boolean> visited = new HashMap<Location, Boolean>();
        for(Location loc : this.locations) visited.put(loc, false);

        PriorityQueue<Trail> trails_pq = new PriorityQueue<Trail>();
        Location start = this.locations.get(0);
        visited.replace(start,true);


        for(Trail t : this.trails){
            if(t.source == start || t.destination == start){
                trails_pq.add(t);
            }
        }

        while(safestTrails.size() < this.locations.size() -1){
            Trail best_trail = trails_pq.poll();

            // doesn't pick trail if creates loops
            while(visited.get(best_trail.source) && visited.get(best_trail.destination)){
                best_trail = trails_pq.poll();
            }
            visited.replace(best_trail.source,true);
            visited.replace(best_trail.destination,true);

            safestTrails.add(best_trail);

            for(Trail t : this.trails){
                // trail is not already in the pq
                if(!trails_pq.contains(t)){

                    //trail is connected to mst
                    if(t.source == best_trail.source || t.source == best_trail.destination
                            || t.destination == best_trail.destination || t.destination == best_trail.source){

                        // trail does not create loops
                        if(!visited.get(t.source) || !visited.get(t.destination)){
                            trails_pq.add(t);
                        }
                    }
                }
            }
        }

        // Fill the safestTrail list and return it.
        // Select the optimal Trails from the Trail list that you have read.
        // TODO: Your code here
        return safestTrails;
    }

    public void printSafestTrails(List<Trail> safestTrails) {
        // Print the given list of safest trails conforming to the given output format.
        // TODO: Your code here
        int total_danger = 0;
        System.out.println("Safest Trails are:");
        for(Trail t : safestTrails){
            System.out.printf("The trail from %s to %s with danger %d\n",
                    t.source.name,
                    t.destination.name,
                    t.danger);
            total_danger += t.danger;
        }
        System.out.println("Total danger: " + total_danger);
    }
}
