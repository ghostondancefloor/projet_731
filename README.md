# MapReduce Simulation in Java

This project is a simplified MapReduce simulation built using Java. It demonstrates distributed computing concepts such as parallel processing, multithreading, and inter-process communication using sockets.

---

## 🚀 How It Works

### Components:

* *ManagerAuto.java*: Orchestrates the full process — reads input files, evenly splits words into partitions (logs), and spawns reducers.
* *Manager.java*: A multithreaded server that listens for map and reduce workers via socket connections.
* *WorkerMap.java*: A worker that connects to the Manager, processes a file (word counting), and signals completion.
* *WorkerReduce.java*: Processes a log file and writes word count results to output.txt.
* *Main.java*: Acts as a launcher for the above roles based on command-line arguments.

---

## 🧱 Project Structure


MapReduceProject/
├── MapReduce/
│   ├── Main.java
│   ├── Manager.java
│   ├── ManagerAuto.java
│   ├── WorkerMap.java
│   ├── WorkerReduce.java
│   ├── input1.txt
│   ├── input2.txt
│   └── output.txt (generated)


---

## ⚙️ How to Use

### 1. Compile All Files

bash
javac MapReduce/*.java


### 2. Run with Even Distribution (ManagerAuto)

bash
java MapReduce.ManagerAuto "input1.txt" "input2.txt"


This will:

* Read all input files
* Split and evenly distribute all words across logs like log-a.txt, log-b.txt, etc.
* Launch reducers for each partition
* Write combined results to output.txt

---

### Alternative: Socket-Based Workflow

#### Start Manager:

bash
java MapReduce.Main manager


#### Start Map Workers (1 per file):

bash
java MapReduce.Main map


#### Start Reduce Worker:

bash
java MapReduce.Main reduce


> Note: Socket-based approach requires split files and manual worker launching.

---

## 📦 Output

* *output.txt*: Final word counts
* *log-a.txt, **log-b.txt*, etc.: Intermediate files per reducer
* *<file>\_map.txt*: Optional intermediate output from WorkerMap (socket mode)

---

## 🧵 Multithreading

* Manager.java uses multithreading to handle multiple map workers concurrently
* Ensures parallelism and improved speed during the map phase

---

## ✅ Features

* Even partitioning of work using round-robin assignment
* Concurrent socket communication with workers
* Word count aggregation across multiple files
* Output consolidation into a single file

---

## 📚 Future Improvements

* Sort output.txt by frequency or alphabetically
* Add error handling and logging
* Clean up intermediate log files automatically
* Use ExecutorService for controlled thread pooling

---
Authors : Ikram Iddouch - Khadija Zaroil - Sami Sbai
This project is built as a learning tool to simulate core concepts behind Hadoop-style MapReduce using plain Java.
