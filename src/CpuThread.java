import java.util.PriorityQueue;

public class CpuThread extends Thread {
	private static int quantum = 1;
	private static PriorityQueue<Process> cpuQ = new PriorityQueue<Process>();

	/*
	 *The CPU scheduler thread will check the ready queue; if there is a process, it will pick one according to the
	 * scheduling algorithm from the ready queue and hold the CPU resource for the given CPU service time
	 * (or for quantum time if the scheduling algorithm is RR). That means the CPU thread will simply sleep for the
	 * given CPU service time. Then it will release the CPU resource and put this process into the IO queue
	 * (or the ready queue if RR is used) or just terminate if there is no more CPU service.
	 * Then the CPU scheduler thread will check the ready queue again and repeat.
	 */
	public void setQuantum(int q) {
		this.quantum = q;
	}

	//TODO cpu gets it's own Queue, refactor run based on this

	@Override
	public void run() {
		while (!q.isEmpty()) {
			Process currentProc = q.peek();
			while (!currentProc.getCpu().isEmpty()) {
				try {
					Thread.sleep(currentProc.removeCpu());
					//TODO this would start a new IoThread for every proc which is not correct
					IoThread io = new IoThread();
					io.start();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
