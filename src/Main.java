import java.util.*;
import java.io.*;
public class Main {

    public static void main(String[] args) {
	   //Get algorithm
        String algo = args[1];

        //Get quantum (if applicable)
        int quantum = NULL;
        if (args.length == 5) {
            quantum = Integer.parseInt(args[3]);
        }

        //Get input file name
        String fileName = args[args.length - 1];



        //Go through file and use algos


        //Create output file
        PrintWriter output = new PrintWriter("results.txt");
        output.println("Input File Name : " + fileName);
        output.print("CPU Scheduling Alg : " + algo);
        if (quantum == NULL)
            output.println("(" + quantum + ")");
        else output.println();
        output.println("CPU utilization : " + utilization);
        output.println("Throughput : " + throughput);
        output.println("Turnaround time : " + turnaroundTime);
        output.println("Waiting time : " + waitingTime);
        output.println("Response time : " + responseTime);
        output.close();
    }
}
