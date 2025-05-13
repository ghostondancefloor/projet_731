package MapReduce;
import java.io.*;
import java.net.*;
import java.util.*;

public class WorkerMap {
    public void start() throws IOException {
        while (true) {
            try (Socket socket = new Socket("localhost", 5000)) {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                String cmd = in.readLine();
                if ("NO_MORE_WORK".equals(cmd)) {
                    System.out.println("No more work. Exiting...");
                    break;
                }

                if (cmd != null && cmd.startsWith("MAP:")) {
                    String file = cmd.substring(4);
                    System.out.println("Processing file: " + file);
                    Map<String, Integer> counts = countWords(file);
                    writeToLog(counts);  // ✅ new logging method
                    out.println("MAP_DONE");

                    try {
                        Thread.sleep(3000); // Delay to simulate processing time
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Finished: " + file);
                }
            } catch (IOException e) {
                System.err.println("Worker error: " + e.getMessage());
                break;
            }
        }
    }

    private Map<String, Integer> countWords(String file) throws IOException {
        Map<String, Integer> map = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                for (String word : line.toLowerCase().split("\\W+")) {
                    if (!word.isEmpty()) map.put(word, map.getOrDefault(word, 0) + 1);
                }
            }
        }
        return map;
    }

    private void writeToLog(Map<String, Integer> counts) throws IOException {
        synchronized (WorkerMap.class) {
            try (PrintWriter writer = new PrintWriter(new FileWriter("log.txt", true))) {
                for (var e : counts.entrySet()) {
                    writer.println(e.getKey() + ":" + e.getValue());
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            new WorkerMap().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
