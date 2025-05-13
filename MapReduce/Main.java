// File: MapReduce/Main.java
package MapReduce;

public class Main {
    public static void main(String[] args) throws Exception {
        switch (args[0]) {
            case "manager" -> new Manager().start();
            case "map" -> new WorkerMap().start();
            case "reduce" -> new WorkerReduce().start(args[1]);
            default -> System.out.println("Usage: java Main [manager|map|reduce <logfile>]");
        }
    }
}