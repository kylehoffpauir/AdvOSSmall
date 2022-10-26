import java.util.ArrayList;

public class Process implements Comparable{
    private int priority;
    private ArrayList<Integer> cpu;
    private ArrayList<Integer> io;
    private boolean isSleep;
    private int sleepTime;
    private long startTime;
    private long stopTime;
    private int cpuTotal;
    private int ioTotal;
    private long respTime;
    private boolean first;
    private long turnaround;
    private long waitTime;

    public Process(int priority, ArrayList<Integer> cpu, ArrayList<Integer> io) {
        this.priority = priority;
        this.cpu = cpu;
        this.io = io;
        isSleep = false;
        sleepTime = 0;
        startTime = System.currentTimeMillis();
        first = false;
        addUpCpu();
        addupIo();
    }

    public Process() {
        this.priority = 0;
        this.cpu = new ArrayList<Integer>();
        this.io = new ArrayList<Integer>();
        isSleep = false;
        sleepTime = 0;
        startTime = System.currentTimeMillis();
        first = false;
        addUpCpu();
        addupIo();
    }

    public Process(boolean b, String item) {
        this.isSleep = b;
        this.sleepTime = Integer.parseInt(item);
    }

    public int removeCpu() {
        int x = this.cpu.get(0);
        this.cpu.remove(0);
        if (first == false) {
            first = true;
            respTime = System.currentTimeMillis() - startTime;
        }
        return x;
    }

    public int removeIo() {
        int x = this.io.get(0);
        this.io.remove(0);
        return x;
    }

    public void addIo(int i) {
        this.io.add(i);
    }

    public void addCpu(int c) {
        this.cpu.add(c);
    }

    public void setPriority(int p) {
        this.priority = p;
    }

    public int getPriority() {
        return priority;
    }

    public ArrayList<Integer> getCpu() {
        return cpu;
    }

    public ArrayList<Integer> getIo() {
        return io;
    }

    public int compareTo(Proess o) {
        if(this.priority < o.getPriority()) {
            return -1;
        }
        else if(this.priority > o.getPriority()) {
            return 1;
        }
        else
            return 0;
    }
    public void addUpCpu() {
        for (int x : cpu)
            cpuTotal += x;
    }
    public void addUpIo() {
        for (int x : io)
            ioTotal += x;
    }
    public void stop() {
        stopTime = System.currentTimeMillis();
        turnaround = stopTime - startTime;
        waitTime = turnaround + (cpuTotal + ioTotal);
    }

    @Override
    public String toString() {
        if(this.isSleep) {
            return "isSleep: " + isSleep + "\nsleeptime: " + sleepTime + "\n";
        } else {
            return "priority: " + priority + "\ncpu: " + cpu.toString() + "\nio: " + io.toString() + "\nisSleep: " + isSleep + "\n";
        }
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
