import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Shortest Remaining Time First.
 */
public class SRTF implements Scheduler {
	private PriorityQueue<SimProcess> processQueue;
	private SimProcess currentProcess;
	private int totalWaitingTime;
	private int completedProcesses;

	public SRTF() {
		//sort process based on remaining time (SRTF)
		processQueue = new PriorityQueue<>(new Comparator<SimProcess>() {
			@Override
			public int compare(SimProcess p1, SimProcess p2) {
				return Integer.compare(p1.getRemainingTime(), p2.getRemainingTime());
			}
		});
		currentProcess = null;
		totalWaitingTime = 0;
		completedProcesses = 0;
	}

	@Override
	public void onProcessArrival(SimProcess p, int time) {
		if (currentProcess == null){
			//no process waiting, start this one!!
			currentProcess = p;
			System.out.println("Start running process {ID=' " + currentProcess.getId() + " ' , Arrival time=" + currentProcess.getTimeOfArrival() + ", Burst time =" + currentProcess.getBurstTime() + ", Current time =" + time + "}");
		} else {
			//check if new process has a srt
			if (p.getBurstTime() < currentProcess.getBurstTime()){
				//preempt current process
				processQueue.add(currentProcess);
				currentProcess = p;
				System.out.println("Preempt and run process {Id='" + currentProcess.getId() + "', Arrival Time=" + currentProcess.getTimeOfArrival() + "', Burst Time=" + currentProcess.getBurstTime() + "', Current Time=" + time + "}");
			}
		}
	}

	@Override
	public void onProcessExit(SimProcess p, int time) {
		//calc wait time of process that just finished
		int waitingTime = time - p.getTimeOfArrival() - p.getBurstTime();
		totalWaitingTime += waitingTime;
		completedProcesses++;

		System.out.println(p.getId() + " finished at time " + time + ". Its waiting time is " + waitingTime);
		System.out.println("Current average waiting time: " + (totalWaitingTime / (double) completedProcesses));

		currentProcess = null;

		//start new process
		if (!processQueue.isEmpty()){
			currentProcess = processQueue.poll();
			System.out.println("Start running Process {Id='" + currentProcess.getId() + "', Arrival Time=" + currentProcess.getTimeOfArrival() + "', Burst Time=" + currentProcess.getBurstTime() + "', Current Time=" + time + "}");
		}
	}

	@Override
	public void onClockInterrupt(int timeElapsed, int time) {
		if (currentProcess != null){
			currentProcess.run(); //decrease remaining time of current process

			if (currentProcess.isFinished()){
				onProcessExit(currentProcess, time);
			}else{
				//check if there is a with a srt
				if (!processQueue.isEmpty() && processQueue.peek().getRemainingTime() < currentProcess.getRemainingTime()){
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
