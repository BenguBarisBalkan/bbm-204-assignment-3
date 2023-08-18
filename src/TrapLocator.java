import java.util.*;

public class TrapLocator {
    public List<Colony> colonies;

    public TrapLocator(List<Colony> colonies) {
        this.colonies = colonies;
    }

    public List<List<Integer>> revealTraps() {

        // Trap positions for each colony, should contain an empty array if the colony is safe.
        // I.e.:
        // 0 -> [2, 15, 16, 31]
        // 1 -> [4, 13, 22]
        // 3 -> []
        // ...
        List<List<Integer>> traps = new ArrayList<>();

        for(Colony c : this.colonies)
            findCycles(c, traps);


        // Identify the time traps and save them into the traps variable and then return it.
        // TODO: Your code here

        return traps;
    }

    public void printTraps(List<List<Integer>> traps) {
        System.out.println("Danger exploration conclusions:");

        int colNum = 1;
        for(List<Integer> trap : traps){
            if(trap.size() == 0){
                System.out.printf("Colony %d: Safe\n",colNum++);
            }
            else{
                //Collections.sort(trap);
                System.out.printf("Colony %d: Dangerous. Cities on the dangerous path: [", colNum++);
                for(int i = 0; i<trap.size(); i++){
                    System.out.print(trap.get(i));
                    if(i != trap.size()-1) System.out.print(", ");
                }
                System.out.print("]\n");
            }
        }



        // For each colony, if you have encountered a time trap, then print the cities that create the trap.
        // If you have not encountered a time trap in this colony, then print "Safe".
        // Print the your findings conforming to the given output format.
        // TODO: Your code here
    }

    private void findCycles(Colony c,List<List<Integer>> traps)
    {
        // Mark all the vertices as not visited and
        // not part of recursion stack
        Map<Integer, Boolean> visited = new HashMap<>();
        for(int city : c.cities) visited.put(city, false);

        Map<Integer, Boolean> recStack = new HashMap<>();
        for(int city : c.cities) recStack.put(city, false);

        List<Integer> trap = new ArrayList<>();


        // Call the recursive helper function to
        // detect cycle in different DFS trees
        ArrayList<Integer> path = new ArrayList<>();
        for (int i : c.cities){
            if (findCyclesUtil(i, visited, recStack, c, path)){

                int start = path.get(path.size()-1);

                boolean flag = false;
                for(int city : path){
                    if(city == start) flag = true;
                    if(flag) trap.add(city);
                }
                trap.remove(trap.size()-1);

                Collections.sort(trap);
                traps.add(trap);
                return;
            }
        }
        traps.add(trap);
    }

    private boolean findCyclesUtil(int i, Map<Integer, Boolean> visited,
                                 Map<Integer, Boolean> recStack, Colony c, ArrayList<Integer> path)
    {
        if (recStack.get(i)){
            path.add(i);
            return true;
        }

        if (visited.get(i))
            return false;

        visited.replace(i, true);
        recStack.replace(i, true);
        path.add(i);

        List<Integer> neighbours = c.roadNetwork.get(i);

        for (Integer ne: neighbours)
            if (findCyclesUtil(ne, visited, recStack, c, path))
                return true;

        recStack.replace(i, false);
        path.remove(path.size()-1);
        return false;
    }

}
