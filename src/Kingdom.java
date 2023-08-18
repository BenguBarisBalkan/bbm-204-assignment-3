import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Kingdom {

    Map<Integer, ArrayList<Integer>> roads = new HashMap<>();

    // TODO: You should add appropriate instance variables.
    public void initializeKingdom(String filename) {
        File file = new File(filename);
        try {
            Scanner sc = new Scanner(file);
            int city_number = 1;
            while (sc.hasNextLine()){
                roads.put(city_number, new ArrayList<>());

                String line = sc.nextLine().strip();
                ArrayList<Integer> neighbours = new ArrayList<>();

                for(String i : line.split(" ")){
                    neighbours.add(Integer.parseInt(i));
                }

                int neighbour_num = 1;
                for(int i : neighbours){
                    if(i == 1) roads.get(city_number).add(neighbour_num);

                    neighbour_num++;
                }
                city_number++;
            }

            sc.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Read the txt file and fill your instance variables
        // TODO: Your code here
    }

    public List<Colony> getColonies() {
        List<Colony> colonies = new ArrayList<>();
        Map<Integer, Integer> colony_numbers = new HashMap<>();
        for (int key : roads.keySet()) colony_numbers.put(key,0);

        int current;
        for(int key : colony_numbers.keySet()){
            if(colony_numbers.get(key) == 0){
                current = key;
                Map<Integer, Boolean> visited = new HashMap<>();
                for(int vis : roads.keySet()) visited.put(vis,false);

                Queue<Integer> queue = new LinkedList<>();
                queue.add(key);

                while (!queue.isEmpty()){
                    int pop = queue.poll();
                    visited.replace(pop,true);
                    colony_numbers.replace(pop,current);

                    for(int ne : roads.get(pop)){
                        if(!visited.get(ne)) queue.add(ne);
                    }
                    for(int ne : roads.keySet()){
                        if(roads.get(ne).contains(pop) && !visited.get(ne)) queue.add(ne);
                    }
                }
            }
        }

        Map<Integer, ArrayList<Integer>> colonies_map = new HashMap<>();

        for(int key : colony_numbers.keySet()){
            if(colonies_map.containsKey(colony_numbers.get(key))){
                colonies_map.get(colony_numbers.get(key)).add(key);
            }
            else{
                colonies_map.put(colony_numbers.get(key),new ArrayList<>());
                colonies_map.get(colony_numbers.get(key)).add(key);
            }
        }

        for(int key : colonies_map.keySet()){
            Colony colony = new Colony();
            for(int city : colonies_map.get(key)){
                colony.cities.add(city);
                colony.roadNetwork.put(city, roads.get(city));
            }

            colonies.add(colony);
        }

        // TODO: DON'T READ THE .TXT FILE HERE!
        // Identify the colonies using the given input file.
        // TODO: Your code here
        return colonies;
    }

    public void printColonies(List<Colony> discoveredColonies) {
        // Print the given list of discovered colonies conforming to the given output format.
        System.out.println("Discovered colonies are:");
        int num = 1;
        for(Colony colony : discoveredColonies){
            System.out.print("Colony " + num++ + ": [");
            for(int i = 0; i<colony.cities.size();i++){
                System.out.print(colony.cities.get(i));
                if(i!= colony.cities.size() -1) System.out.print(", ");
            }
            System.out.print("]\n");
        }
        // TODO: Your code here
    }
}
