import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * Shortest Remaining Time First.
 */
public class SRTF implements Scheduler {
	private PriorityQueue<SimProcess> processQueue;
	private SimProcess currentProcess;
	
	private List<Integer> waitingTimes;

	public SRTF() {
		//sort process based on remaining time (SRTF)
		processQueue = new PriorityQueue<>(new Comparator<SimProcess>() {
			@Override
			public int compare(SimProcess p1, SimProcess p2) {
				return Integer.compare(p1.getBurstTime(), p2.getBurstTime());
			}
		});
		currentProcess = null;
		waitingTimes = new ArrayList<Integer>();
		
		
	}

	@Override
	public void onProcessArrival(SimProcess p, int time) {
		if (currentProcess == null){
			//no process waiting, start this one!!
			currentProcess = p;
			processQueue.add(p);
			
		} else {
			//check if new process has a short burst
			if (p.getBurstTime() < currentProcess.getBurstTime()){
				//preempt current process
				
				processQueue.add(p);
				currentProcess = p;
				
			}else{
				
				currentProcess = p;
				
			}
		}
	}

	@Override
	public void onProcessExit(SimProcess p, int time) {
		//calc wait time of process that just finished
		int waitingTime = time - p.getTimeOfArrival() - p.getBurstTime();
		System.out.println(p.getId() + " finished at time " + time + ". Its waiting time is " + waitingTime);
		waitingTimes.add(waitingTime);
		System.out.println("Current average waiting time: " + calculateAvgWaiting());

		currentProcess = null;

		//start new process
		if (!processQueue.isEmpty()){
			currentProcess = processQueue.poll();
			System.out.println("Start running Process {Id='" + currentProcess.getId() + "', Arrival Time=" + currentProcess.getTimeOfArrival() + "', Burst Time=" + currentProcess.getBurstTime() + "', Current Time=" + time + "}");
		}
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
		if (currentProcess != null){
			currentProcess.run(); //decrease remaining time of current process

			if (currentProcess.isFinished()){
				onProcessExit(currentProcess, time);
			}else{
				//check if there is a with a srt
				if (!processQueue.isEmpty() && processQueue.peek().getBurstTime() < currentProcess.getBurstTime()){
					processQueue.add(currentProcess);
					currentProcess = processQueue.poll(); //start process with srt
					System.out.println("Preempt and start running Process {Id='" + currentProcess.getId() + "', Arrival Time=" + currentProcess.getTimeOfArrival() + "', Burst Time=" + currentProcess.getBurstTime() + "', Current Time=" + time + "}");
				}
			}
		}
	}

	@Override
	public String getAlgorithmName() {
		return "SRTF";
	}

	@Override
	public SimProcess currentProcess() {
		return currentProcess;
	}

	@Override
	public boolean isEmpty() {
		return currentProcess == null && processQueue.isEmpty();
	}

}