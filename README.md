# AdvOSSmall
simulate threading/scheduling

COSC 510: Advanced Operating Systems

Small Project

Due: 10/31/22 at 6:00 pm

Points: 200

For the small project you are to write a program that simulates the implementation of various process scheduling algorithms and prints out several performance measures based on the simulation. You may work in groups of 2 or 3, but let me know who is in your group before submitting your work. You may use C, C++, or Java, for this assignment (or see me if you wish to use another language.)
Objectives

    Learn and practice CPU scheduling algorithms by implementing them.
    Learn and practice process/thread synchronization mechanisms by using them
    Practice system calls and library functions.

Description

For this assignment, you will implement a multithreaded program that will allow you to measure the performance of four basic CPU scheduling algorithms (which are FCFS, SPN, PRI, and RR). The performance measurements are:

    CPU utilization
    Throughput
    Turnaround time
    Waiting time
    Response time

In PRI, the scheduler is given a priority for each process and will choose the job with the highest priority. For SPN, the program can add up the given CPU service times to find the expected processing time.

The program will simulate the processes whose priority, sequence of CPU service time(ms) and I/O service time(ms) will be given in an input file.

You may assume that all scheduling algorithms except RR will be non-preemptive, and all scheduling algorithms except PRI will ignore process priorities (i.e., all processes have the same priority in FCFS, SPN and RR). Also assume that there is only one IO device and all IO requests will be served using that device in a FCFS manner.
Program Requirements

The program will take the name of the scheduling algorithm, related parameters (if any), and an input file name from command line. Here how your program should be executed:

`prog -alg [FCFS|SPN|PRI|RR] [-quantum [integer(ms)]] -input [file name]`

The output of your program will be as follows:

```Input File Name : file name
CPU Scheduling Alg : FCFS|SPN|PRI|RR (quantum)
CPU utilization : ....
Throughput : ....
Turnaround time : ....
Waiting time : ....
Response time : ....
```
The input file is formatted such that each line starts with the keyword proc, sleep, stop. Following proc, there will be a sequence of integer numbers: the first one represents the priority (1: lowest, ..., 5: normal, ..., 10: highest), while the remaining ones represent CPU service times and I/O service times (ms) in an alternating manner. Following sleep, there will be an integer number representing the time (ms) after which there will be another process. One of the threads in your program would be responsible for processing this file as follows: As long as it reads proc, it will create a new process and put it in a ready queue (clearly this process is not an actual one, it will be just a simple data structure (similar to PCB) that contains the given priority and the sequence of CPU service times and I/O service times, and other fields). When this thread reads sleep x, it will sleep x ms and then try to read new processes from the file. Upon reading stop, this thread will quit.
```Sample Input File
proc 1 10 20 10 50 20 40 10
proc 1 50 10 30 20 40
sleep 50
proc 2 20 50 20
stop
```
Program Internals

You need at least two other threads to simulate the behaviors of CPU scheduler and I/O system.
```
The CPU scheduler thread will check the ready queue; if there is a process, it will pick one according to the scheduling algorithm from the ready queue and hold the CPU resource for the given CPU service time (or for quantum time if the scheduling algorithm is RR). 
That means the CPU thread will simply sleep for the given CPU service time. Then it will release the CPU resource and put this process into the IO queue (or the ready queue if RR is used) or just terminate if there is no more CPU service. Then the CPU scheduler thread will check the ready queue again and repeat.
The I/O system thread will check the IO queue; if there is a process, it will hold the IO device for the given IO service time. That is, the IO thread will sleep for the given IO service time. It then puts this process back into the ready queue. Finally it will check the IO queue and repeat.
```
Basically you will have at least the above mentioned three threads and the main one. These threads need to be synchronized as they cooperate to collect data for performance measures and share data through the the ready and IO queues. The main thread will wait until the file reading thread is done and the ready queue and the IO queue are empty. It will then print the performance evaluation results and terminate the program.

You may use any data structures in different parts of your program and share them along with new variables; but when it comes to maintaining the list of processes in the ready queue or the IO queue, you should use a doubly linked list, as this is the case in many practical OS.
Grading

First, make sure your program works for FCFS CPU scheduling. This will be 100 points. Then you can add/test the others: SPN (25 points), PRI (25 points) and RR (40 points). Write a 2-3 page report (10 points) to describe your design choices at the high level and your results. Include your source codes, instructions to compile/execute, and some output files showing your test results, etc. Submit your work via Canvas.
