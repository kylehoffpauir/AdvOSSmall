import java.util.PriorityQueue;

public class IoThread extends Thread {
  private static PriorityQueue<Process> q = new PriorityQueue<Process>();

    @Override
    public void run() {
      while(!q.isEmpty()) {
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
