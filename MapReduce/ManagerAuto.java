// File: MapReduce/ManagerAuto.java
package MapReduce;

import java.io.*;
import java.util.*;

public class ManagerAuto {
    private static final String[] PARTITIONS = {"a", "b", "c", "d", "s","w"};
    private static final String LOG_PREFIX = "log-";

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Usage: java MapReduce.ManagerAuto <input_file1> <input_file2> ...");
            return;
        }

        List<String> inputFiles = Arrays.asList(args);
        System.out.println("Creating partitioned logs from all input files...");
        generatePartitionedLogs(inputFiles);

        System.out.println("Launching reducer workers...");
        List<Process> reducers = new ArrayList<>();
        for (String part : PARTITIONS) {
            String logFile = LOG_PREFIX + part + ".txt";
            Process reducer = new ProcessBuilder("java", "MapReduce.Main", "reduce", logFile)
                    .inheritIO()
                    .start();
            reducers.add(reducer);
        }

        for (Process reducer : reducers) {
            reducer.waitFor();
        }

        System.out.println("All done.");
    }

    private static void generatePartitionedLogs(List<String> inputFiles) throws IOException {
        Map<String, PrintWriter> writers = new HashMap<>();
        for (String part : PARTITIONS) {
            writers.put(part, new PrintWriter(new FileWriter(LOG_PREFIX + part + ".txt")));
        }

        for (String inputFile : inputFiles) {
            System.out.println("Reading " + inputFile);
            try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    for (String word : line.toLowerCase().split("\\W+")) {
                        if (!word.isEmpty()) {
                            String bucket = getPartition(word);
                            PrintWriter writer = writers.get(bucket);
                            writer.println(word + ":1");
                        }
                    }
                }
            }
        }

        for (PrintWriter writer : writers.values()) {
            writer.close();
        }
    }

    private static String getPartition(String word) {
        char first = word.charAt(0);
        for (String part : PARTITIONS) {
            if (part.charAt(0) == first) return part;
        }
        return PARTITIONS[0]; // default
    }
} 
