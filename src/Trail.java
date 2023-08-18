public class Trail implements Comparable{
    public Location source;
    public Location destination;
    public int danger;

    public Trail(Location source, Location destination, int danger) {
        this.source = source;
        this.destination = destination;
        this.danger = danger;
    }


    @Override public int compareTo(Object o){
        Trail o1 = (Trail) o;

        int self_danger, object_danger;
        self_danger = this.danger;
        object_danger = o1.danger;

        return Integer.compare(self_danger,object_danger);
    }
}
