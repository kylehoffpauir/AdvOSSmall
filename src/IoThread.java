import java.util.PriorityQueue;

public class IoThread extends Thread {
	private static int quantum = 1;
	private static PriorityQueue<Process> ioQ = new PriorityQueue<Process>();
    /*
    * The I/O system thread will check the IO queue; if there is a process,
    * it will hold the IO device for the given IO service time.
    * That is, the IO thread will sleep for the given IO service time.
    * It then puts this process back into the ready queue.
    * Finally it will check the IO queue and repeat.
     */

    //TODO Io gets it's own Queue, refactor run based on this

	public void setQuantum(int q) {
		this.quantum = q;
	}

	@Override
	public void run() {
		while (!q.isEmpty()) {
			Process currentProc = q.peek();
			while (!currentProc.getIo().isEmpty()) {
				try {
					Thread.sleep(currentProc.removeIo());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
