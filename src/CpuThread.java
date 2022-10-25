import java.util.PriorityQueue;

public class CpuThread extends Thread {
  private static PriorityQueue<Process> q = new PriorityQueue<Process>();

    @Override
    public void run() {
      while(!q.isEmpty()) {
        Process currentProc = q.peek();
        while (!currentProc.getCpu().isEmpty()) {
          try {
            Thread.sleep(currentProc.removeCpu());
            IoThread io = new IoThread();
            io.start();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    }
}
