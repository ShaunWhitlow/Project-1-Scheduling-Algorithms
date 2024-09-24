import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Shortest Job First.
 */
public class SJF implements Scheduler {
	private PriorityQueue<SimProcess> processQueue;
	private SimProcess currentProcess;
	private int totalWaitingTime;
	private int completedProcesses;

	public SJF() {
		processQueue = new PriorityQueue<>(new Comparator<SimProcess>() {
			@Override
			public int compare(SimProcess p1, SimProcess p2) {
				return Integer.compare(p1.getBurstTime(), p2.getBurstTime());
			}
		});
		currentProcess = null;
		totalWaitingTime = 0;
		completedProcesses = 0;
	}

	@Override
	public void onProcessArrival(SimProcess p, int time) {
		processQueue.add(p);

		if (currentProcess == null){
			startNextProcess(time);
		}
	}

	@Override
	public void onProcessExit(SimProcess p, int time) {
		int waitingTime = time - p.getTimeOfArrival() - p.getBurstTime();
		totalWaitingTime += waitingTime;
		completedProcesses++;

		System.out.println(p.getId() + " finished at time " + time + ". Its waiting time is " + waitingTime);
		System.out.println("Current average waiting time: " + (totalWaitingTime / (double) completedProcesses));

		currentProcess = null;
		startNextProcess(time);
	}

	@Override
	public void onClockInterrupt(int timeElapsed, int time) {
		if (currentProcess != null){
			currentProcess.run();

			if (currentProcess.isFinished()){
				onProcessExit(currentProcess, time);
			}
		}
	}

	@Override
	public String getAlgorithmName() {
		return "SJF";
	}

	@Override
	public SimProcess currentProcess() {
		return currentProcess;
	}

	@Override
	public boolean isEmpty() {
		return currentProcess == null && processQueue.isEmpty();
	}

	private void startNextProcess(int time){
		if (!processQueue.isEmpty()){
			currentProcess = processQueue.poll();
			System.out.println(currentProcess.getId() + " starts at time " + time);
		}
	}
}
