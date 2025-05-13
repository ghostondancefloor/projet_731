package MapReduce;
import java.io.*;
import java.net.*;
import java.util.*;

public class Manager {
    private final List<String> files = getSplitFiles();
    private final int port = 5000;
    private final Queue<String> filesQueue = new LinkedList<>(files);
    private int processedFiles = 0;
    private final int totalFiles = files.size();

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Manager started at port " + port);

            // === Multithreaded Socket Listener ===
            while (processedFiles < totalFiles) {
                Socket s = serverSocket.accept();
                new Thread(() -> handleMapWorker(s)).start();
            }

            // === Reduce Phase ===
            System.out.println("Waiting for reduce worker to connect...");
            Socket reduceSocket = serverSocket.accept();
            System.out.println("Reduce worker connected.");

            PrintWriter reduceOut = new PrintWriter(reduceSocket.getOutputStream(), true);
            BufferedReader reduceIn = new BufferedReader(new InputStreamReader(reduceSocket.getInputStream()));

            reduceOut.println("REDUCE");

            System.out.println("Final Results:");
            String line;
            while ((line = reduceIn.readLine()) != null && !line.equals("END")) {
                System.out.println(line);
            }

            reduceSocket.close();
            System.out.println("Job completed.");
        }
    }

    private synchronized void handleMapWorker(Socket socket) {
        try {
            System.out.println("Map worker connected.");
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            if (!filesQueue.isEmpty()) {
                String file = filesQueue.poll();
                System.out.println("Assigned file: " + file);
                out.println("MAP:" + file);
            } else {
                out.println("NO_MORE_WORK");
                socket.close();
                return;
            }

            String response = in.readLine();
            if ("MAP_DONE".equals(response)) {
                processedFiles++;
            } else {
                System.err.println("Unexpected response from map worker: " + response);
            }
            socket.close();
        } catch (IOException e) {
            System.err.println("Error handling map worker: " + e.getMessage());
        }
    }

    private List<String> getSplitFiles() {
        File dir = new File(".");
        String[] partFiles = dir.list((d, name) -> name.contains("_part") && name.endsWith(".txt"));
        return partFiles == null ? List.of() : Arrays.stream(partFiles).toList();
    }
}