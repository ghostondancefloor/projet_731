package MapReduce;

import java.io.*;
import java.util.*;

public class WorkerReduce {
    public void start(String logFileName) throws IOException {
        File logFile = new File(logFileName);
        if (!logFile.exists()) {
            System.out.println("Log file " + logFileName + " not found.");
            return;
        }

        Map<String, Integer> finalCounts = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String word = parts[0];
                    int count = Integer.parseInt(parts[1]);
                    finalCounts.put(word, finalCounts.getOrDefault(word, 0) + count);
                }
            }
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter("output3.txt", true))) {
            for (var e : finalCounts.entrySet()) {
                writer.println(e.getKey() + ": " + e.getValue());
            }
        }

        System.out.println("Reducer output written to output.txt from " + logFileName);
    }
} 
