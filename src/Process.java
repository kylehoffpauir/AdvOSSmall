import java.util.ArrayList;

public class Process implements Comparable{
    private int priority;
    private ArrayList<Integer> cpu;
    private ArrayList<Integer> io;
    private boolean isSleep;
    private int sleepTime;

    public Process(int priority, ArrayList<Integer> cpu, ArrayList<Integer> io) {
        this.priority = priority;
        this.cpu = cpu;
        this.io = io;
        isSleep = false;
        sleepTime = 0;
    }

    public Process() {
        this.priority = 0;
        this.cpu = new ArrayList<Integer>();
        this.io = new ArrayList<Integer>();
        isSleep = false;
        sleepTime = 0;
    }

    public Process(boolean b, String item) {
        this.isSleep = b;
        this.sleepTime = Integer.parseInt(item);
    }

    public int removeCpu() {
        int x = this.cpu.get(0);
        this.cpu.remove(0);
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

    public int compareTo(Process o) {
        if(this.priority < o.getPriority()) {
            return -1;
        }
        else if(this.priority > o.getPriority()) {
            return 1;
        }
        else
            return 0;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
