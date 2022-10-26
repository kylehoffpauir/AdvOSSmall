import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.*;
import java.io.*;


public class Main {
	public static String algo = "";
	public static int quantum = -1;
	public static Boolean reading = true;
	public static Boolean ioQEmpty = true;
	public static Boolean readyQEmpty = true;
	private static final boolean DEBUG = true;
	private static LinkedList<Process> readyQueue = new LinkedList<Process>();
	private static LinkedList<Process> ioQueue = new LinkedList<Process>();
	private static LinkedList<Process> completedProc = new LinkedList<Process>();
	private static long projectStartTime;
	private static long projectEndTime;
	private static long throughput;
	private static long utilization;
	private static long turnaroundTime;
	private static long waitingTime;
	private static long responseTime;


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

	public static void sendOut(File inFile) {
		//Create output file
		PrintWriter output = null;
		try {
			output = new PrintWriter("results.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		throughput = completedProc.size() / (projectEndTime - projectStartTime);
		for (Process x : completedProc) {
			utilization += x.getCpuTotal();
			utilization += x.getIoTotal();
			turnaroundTime += x.getTurnaround();
			waitingTime += x.getWaitTime();
			responseTime += x.getRespTime();
		}

        //fill output file
		output.println("Input File Name : " + inFile.getName());
		output.print("CPU Scheduling Alg : " + algo);
		if (quantum == -1)
			output.println(" (" + quantum + ")");
		else output.println();
		//String utilization = "", throughput = "", turnaroundTime = "", waitingTime = "", responseTime = "";
		output.println("CPU utilization : " + utilization + "ms");
		output.println("Throughput : " + throughput + "ms");
		output.println("Turnaround time : " + turnaroundTime + "ms");
		output.println("Waiting time : " + waitingTime + "ms");
		output.println("Response time : " + responseTime + "ms");
		output.close();
	}

	//100
	public static void FCFS(File inFile) {
		//TODO establish how to actually run the threads and simulate the processes
		//does this need to be it's own class as well? does readProc need to be a member of the fileThread class
		//im thinking maybe
		FileThread ft = new FileThread(inFile);
		CpuThread cput = new CpuThread();
		IoThread iot = new IoThread();
		ft.start();
		projectStartTime = System.currentTimeMillis();
		cput.start();
		iot.start();
		// wait for threads to end
		try {
			ft.join();
			cput.join();
			iot.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		projectEndTime = System.currentTimeMillis();
		if(DEBUG) System.out.println("FILETHREAD EXITED AND QUEUES EMPTY");
		if(DEBUG) System.out.println("Exits?: " + FileThread.exit + IoThread.exit + CpuThread.exit);
		cput.interrupt();
		iot.interrupt();
		ft.interrupt();
		if(DEBUG) System.out.println("Finished");
		//TODO get output out of this?
		sendOut(inFile);
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

	public static class FileThread extends Thread {
		public static File inFile;
		public static boolean exit = false;

		public FileThread(File inFile) {
			this.inFile = inFile;
		}

		@Override
		public void run() {
			if(DEBUG) System.out.println("FileThread started");
			while(!exit) {
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
					//if it's a sleep then we sleep the fileThread
					if (items[0].toLowerCase().equals("sleep")) {
						try {
							//TODO how? and where? do we sleep the filethread.
							this.sleep(Integer.parseInt(items[1]));
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} else if (items[0].toLowerCase().equals("proc")) {
						Process p = new Process();
						//only care about the priority for PRI or SPN, FCFS is prioritized based on sequence order
						if (algo.equals("PRI") || algo.equals("SPN"))
							p.setPriority(Integer.parseInt(items[1]));
						else if (algo.equals("FCFS")) {
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
						//if (DEBUG) System.out.println(p);
						synchronized (readyQueue) {
							readyQueue.add(p);
						}
					} else if (items[0].toLowerCase().equals("stop")) {
						//if we run into a stop then we end the reading
						this.exit = true;
						synchronized (reading) { reading = false; }
						if(DEBUG) System.out.println("STOPPING ReadThread");
					} else {
						System.err.println("Error - input file has unexpected command");
						reading = false;
						System.exit(3);
					}
					if (lineCount < 10) lineCount++;
				}

			}//end the !exit loop
			return;
		}
	}


	public static class CpuThread extends Thread {
		/*
		 * The CPU scheduler thread will check the ready queue; if there is a process, it will pick one according to the
		 * scheduling algorithm from the ready queue and hold the CPU resource for the given CPU service time
		 * (or for quantum time if the scheduling algorithm is RR). That means the CPU thread will simply sleep for the
		 * given CPU service time. Then it will release the CPU resource and put this process into the IO queue
		 * (or the ready queue if RR is used) or just terminate if there is no more CPU service.
		 * Then the CPU scheduler thread will check the ready queue again and repeat.
		 */
		public static boolean exit = false;


		@Override
		public void run() {
			if(DEBUG) System.out.println("CpuThread started");
			while (!exit) {
				synchronized (readyQueue) {
					if (!readyQueue.isEmpty()) {
						synchronized (readyQEmpty) {
							readyQEmpty = false;
						}
						//if(DEBUG) System.out.println(readyQueue);
						Process currentProc = algoRemove();
						if(!currentProc.getCpu().isEmpty()) {
							//if (DEBUG) System.out.println("CPUTHREAD\nCurrent Proc: " + currentProc);
							try {
								//remove the first element from the process' cpu queue
								int sleeptime = currentProc.removeCpu();
								sleep(sleeptime);
								if (DEBUG) System.out.println("Cpu thread sleeping for " + sleeptime);
								if(!currentProc.getIo().isEmpty())
									ioQueue.add(currentProc);
								else if(currentProc.getCpu().isEmpty()) {
									//if there is no more io and after being service this proc has no more cpu
									//then it is complete
									synchronized (completedProc) {
										currentProc.stop();
										completedProc.add(currentProc);
									}
								}
							} catch (InterruptedException e) {
								//e.printStackTrace();
								exit = true;
							}
						} else if(currentProc.getIo().isEmpty()) {
							synchronized (completedProc) {
								currentProc.stop();
								completedProc.add(currentProc);
							}
						}
					}
					readyQEmpty = true;
					synchronized (reading) {
						synchronized (ioQEmpty) {
							if (!reading && ioQEmpty) {
								exit = true;
							}
						}
					}
				}
			}//end !exit loop
		}

		public static Process algoRemove() {
			switch (algo) {
				case "FCFS":
					return readyQueue.remove(0);
				case "RR":
					return readyQueue.remove(0);
				case "SPN":
					return readyQueue.remove(0);
				case "PRI":
					return readyQueue.remove(0);
				default:
					System.err.println("Error - cpu error when attempting to dequeue a process");
					System.exit(4);
					return new Process();
			}
		}
	}//end CpuThread


	public static class IoThread extends Thread {
		/*
		 * The I/O system thread will check the IO queue; if there is a process,
		 * it will hold the IO device for the given IO service time.
		 * That is, the IO thread will sleep for the given IO service time.
		 * It then puts this process back into the ready queue.
		 * Finally it will check the IO queue and repeat.
		 */
		public static boolean exit = false;

		@Override
		public void run() {
			if(DEBUG) System.out.println("IoThread started");
			while (!exit) {
				synchronized (ioQueue) {
					if (!ioQueue.isEmpty()) {
						synchronized (ioQEmpty) {
							ioQEmpty = false;
						}
						Process currentProc = ioQueue.remove(0);
						//if (DEBUG) System.out.println("IOTHREAD\nCurrent Proc: " + currentProc);
						try {
							//remove the first element from the process' cpu queue
							if (!currentProc.getIo().isEmpty()) {
								int sleeptime = currentProc.removeIo();
								sleep(sleeptime);
								if (DEBUG) System.out.println("Io thread sleeping for " + sleeptime);
								//synchronized (readyQueue) {
								if (!currentProc.getCpu().isEmpty()) {
									readyQueue.add(currentProc);
								}
								else if(currentProc.getIo().isEmpty()) {
									//if after being service there is no more io and there is no more cpu
									//stop the process
									synchronized (completedProc) {
										currentProc.stop();
										completedProc.add(currentProc);
									}
								}
							} else if(currentProc.getCpu().isEmpty()) {
								//if we dequeue an empty io process and there is no cpu stop the process
								synchronized (completedProc) {
									currentProc.stop();
									completedProc.add(currentProc);
								}
							}
						} catch (InterruptedException e) {
							//e.printStackTrace();
							exit = true;
						}
					}
				}
					ioQEmpty = true;
					synchronized (reading) {
						synchronized (readyQEmpty) {
							if (!reading && readyQEmpty) {
								exit = true;
							}
						}
					}
			}//end !exit loop
		}
	}//end IoThread



}//end class