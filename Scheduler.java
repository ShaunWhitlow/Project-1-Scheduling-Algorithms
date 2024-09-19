

public interface Scheduler extends InterruptVector {

	/**
	 * @return the name of the algorithm used by the scheduler
	 */
	public String getAlgorithmName();
	
	/**
	 * @return the process currently running or scheduled to run
	 */
	public SimProcess currentProcess();
	
	
	/**
	 * @return false is no processes is currently in the queue 
	 */
	public boolean isEmpty();
	
}
