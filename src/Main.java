import java.util.*;
import java.io.*;
import java.lang.Thread;


public class Main {
    public static String algo = "";
    public static int quantum = -1;


    public static void main(String[] args) {
        //Go through file and use algos
        File inFile = getIn(args);

        switch(algo) {
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

    }

    //25
    public static void SPN(File inFile) {

    }

    //25
    public static void PRI(File inFile) {

    }

    //40
    public static void RR(File inFile) {

    }
}
