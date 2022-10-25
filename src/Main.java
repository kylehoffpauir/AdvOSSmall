import java.util.*;
import java.io.*;


public class Main {
    public static String algo = "";
    public static int quantum = -1;


    public static void main(String[] args) {
        //Go through file and use algos
        File inFile = getIn(args);
        PriorityQueue<Process> q = readProc(inFile);
        switch(algo) {
            case "FCFS":
                FCFS(q, inFile);
                break;
            case "RR":
                RR(q, inFile);
                break;
            case "SPN":
                SPN(q, inFile);
                break;
            case "PRI":
                PRI(q, inFile);
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

    public static PriorityQueue<Process> readProc(File inFile) {
        Scanner f = null;
        try {
            f = new Scanner(inFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        PriorityQueue<Process> q = new PriorityQueue<Process>();
        while(f.hasNextLine()) {
            String[] items = f.nextLine().split(" ");
            if(items[0].toLowerCase().equals("s")) {
                q.add(new Process(true, items[1]));
            } else if(items[0].toLowerCase().equals("proc")) {
                Process p = new Process();
                //only care about the priority for PRI or SPN
                if(algo.equals("PRI") || algo.equals("SPN"))
                    p.setPriority(Integer.parseInt(items[1]));
                else
                    p.setPriority(1);

                for(int i = 2; i < items.length; i++) {
                    //cpu is even items
                    if( i % 2 == 0) {
                        p.addCpu(Integer.parseInt(items[i]));
                    }
                    //io is odd items
                    else {
                        p.addIo(Integer.parseInt(items[i]));
                    }
                }
                q.add(p);
            } else if(items[0].toLowerCase().equals("stop")) {
                return q;
            } else {
                System.err.println("Error - input file has unexpected command");
                System.exit(3);
            }
            return q;
        }
        return q;
    }

    public static void sendOut(File inFile) {
        //Create output file
        PrintWriter output = null;
        try {
            output = new PrintWriter("results.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

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
    public static void FCFS(PriorityQueue<Process> inFile, File file) {

    }

    //25
    public static void SPN(PriorityQueue<Process> inFile, File file) {

    }

    //25
    public static void PRI(PriorityQueue<Process> inFile, File file) {

    }

    //40
    public static void RR(PriorityQueue<Process> inFile, File file) {

    }
}