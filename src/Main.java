import java.util.*;
import java.io.*;


public class Main {
	public static String algo = "";
	public static int quantum = -1;
	private static final boolean DEBUG = true;
	protected static PriorityQueue<Process> q = new PriorityQueue<Process>();


	public static void main(String[] args) {
		//Go through file and use algos
		File inFile = getIn(args);
		switch (algo) {
			case "FCFS":
				FCFS(inFile);
				break;
			case "RR":
				RR(inFile);
				break;
			case "SPN":
				SPN(inFile);
				break;
			case "PRI":
				PRI(inFile);
				break;
			default:
				System.err.println("Error - algorithm option not valid. choose [FCFS | RR | SPN | PRI]");
				System.exit(2);
		}

	}


	public static File getIn(String[] args) {
		//Get algorithm
		algo = args[1];

		//Get quantum (if applicable)
		quantum = -1;
		if (args.length == 6) {
			quantum = Integer.parseInt(args[3]);
			if (quantum < 0) {
				System.err.println("Error - Quantum must be a positive value");
				System.exit(1);
			}
		}

		//Get input file name
		String fileName = args[args.length - 1];
		File f = new File(fileName);
		//System.out.println(quantum + " " + fileName + " " + algo);
		return f;
	}


	//a mess
	public static void readProc(File inFile) {
		Scanner f = null;
		try {
			f = new Scanner(inFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		//linecount used to give proc priorities based on FCFS
        //do we even need this? depending on the implementation yes
        //if the process enters the Q with prioirtity based on it's input, we can deQ, do something, and re-enQ
        //this will make it so that the process now have the minimum priority and can continue on.
		int lineCount = 1;
		while (f.hasNextLine()) {
		    //split contents of input into array
			String[] items = f.nextLine().split(" ");
			//if it's a sleep then we sleep the readThread
			if (items[0].toLowerCase().equals("sleep")) {
				try {
				    //TODO how? and where? do we sleep the filethread.
					fileThread.sleep(Integer.parseInt(items[1]));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else if (items[0].toLowerCase().equals("proc")) {
				Process p = new Process();
				//only care about the priority for PRI or SPN, FCFS is prioritized based on sequence order
				if (algo.equals("PRI") || algo.equals("SPN"))
					p.setPriority(Integer.parseInt(items[1]));
				if (algo.equals("FCFS")) {
					p.setPriority(lineCount);
				} else
					p.setPriority(1);
                //extract the data from the proc lines
				for (int i = 2; i < items.length; i++) {
					//cpu is even items
					if (i % 2 == 0) {
						p.addCpu(Integer.parseInt(items[i]));
					}
					//io is odd items
					else {
						p.addIo(Integer.parseInt(items[i]));
					}
				}
				if (DEBUG) System.out.println(p);
				q.add(p);
			} else if (items[0].toLowerCase().equals("stop")) {
			  //if we run into a stop then we end the reading
				return;
			} else {
				System.err.println("Error - input file has unexpected command");
				System.exit(3);
			}
			if(lineCount < 10) lineCount++;
		}
	}

	public static void sendOut(File inFile) {
		//Create output file
		PrintWriter output = null;
		try {
			output = new PrintWriter("results.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		//TODO Get actual metrics from our threads

        //fill output file
		output.println("Input File Name : " + inFile.getName());
		output.print("CPU Scheduling Alg : " + algo);
		if (quantum == -1)
			output.println(" (" + quantum + ")");
		else output.println();
		String utilization = "", throughput = "", turnaroundTime = "", waitingTime = "", responseTime = "";
		output.println("CPU utilization : " + utilization);
		output.println("Throughput : " + throughput);
		output.println("Turnaround time : " + turnaroundTime);
		output.println("Waiting time : " + waitingTime);
		output.println("Response time : " + responseTime);
		output.close();
	}

	//100
	public static void FCFS(File inFile) {
	  //TODO establish how to actually run the threads and simulate the processes
      //does this need to be it's own class as well? does readProc need to be a member of the fileThread class
      //im thinking maybe
		try {
			Thread fileThread = new Thread(new Runnable() {
				@Override
				public void run() {
					readProc(inFile);
				}
			});
			fileThread.start();
			CpuThread cpuThread = new CpuThread();
			cpuThread.start();
			//sleep to give the filethread a chance to actually put something in the queue
			cpuThread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	//25
	public static void SPN(File file) {

	}

	//25
	public static void PRI(File file) {

	}

	//40
	public static void RR(File file) {

	}
}