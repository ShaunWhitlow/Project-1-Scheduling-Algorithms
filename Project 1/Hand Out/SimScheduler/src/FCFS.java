
import java.util.ArrayList;
import java.util.List;

public class FCFS implements Scheduler {

	private List<SimProcess> queue;
	private List<Integer> waitingTimes;

	public FCFS() {
		queue = new ArrayList<SimProcess>();
		waitingTimes = new ArrayList<Integer>();
		
		// Configure a clock interrupt
//		Clock.ENABLE_INTERRUPT = true;
//		Clock.INTERRUPT_INTERVAL = 50;
	}
	
	@Override
	public String getAlgorithmName() {
		return "FCFS";
	}

	@Override
	public void onProcessArrival(SimProcess p, int time) {
		queue.add(p);
	}

	@Override
	public void onProcessExit(SimProcess p, int time) {
		queue.remove(0);
		int waitingTime = time - p.getTimeOfArrival() - p.getBurstTime();
		System.out.println(p.getId() + " finished at time " + time + ". Its waiting time is " + waitingTime);
		waitingTimes.add(waitingTime);
		System.out.println("Current average waiting time: " + calculateAvgWaiting());
	}
	
	private double calculateAvgWaiting() {
		double totalWaiting = 0;
		for (int waitingTime: waitingTimes) {
			totalWaiting += waitingTime;
		}
		return totalWaiting / waitingTimes.size();
	}

	@Override
	public void onClockInterrupt(int timeElapsed, int time) {
		System.out.println("Clock interrupt! Current time: " + time + " Time Elapsed: " +timeElapsed );
	}

	@Override
	public SimProcess currentProcess() {
		if (queue.size() == 0) {
			return null;
		}
		return queue.get(0);
	}

	@Override
	public boolean isEmpty() {
		return queue.isEmpty();
	}

}
