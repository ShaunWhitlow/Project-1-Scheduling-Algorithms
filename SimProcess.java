

public class SimProcess {

	private String id;
	private int timeOfArrival;
	private int burstTime;
	private int remainingTime;

	public SimProcess(String id, int timeOfArrival, int burstTime) {
		super();
		this.id = id;
		this.timeOfArrival = timeOfArrival;
		this.burstTime = remainingTime = burstTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getTimeOfArrival() {
		return timeOfArrival;
	}

	public void setTimeOfArrival(int timeOfArrival) {
		this.timeOfArrival = timeOfArrival;
	}

	public int getBurstTime() {
		return burstTime;
	}

	public void setBurstTime(int burstTime) {
		this.burstTime = burstTime;
	}

	public void run() {
		remainingTime--;
	}

	public boolean isFinished() {
		return remainingTime == 0;
	}

}
