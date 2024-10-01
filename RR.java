import java.util.ArrayList;
import java.util.List;


/**
 
Round-Robin*/
public class RR implements Scheduler {
    private List<SimProcess> queue;
    private List<Integer> waitingTimes;
    private int timeQuantum;

    public RR(int timeQuantumArg){
        queue = new ArrayList<SimProcess>();
        waitingTimes = new ArrayList<Integer>();
        timeQuantum = timeQuantumArg;

        Clock.ENABLE_INTERRUPT = true;
        Clock.INTERRUPT_INTERVAL = timeQuantum;

    }

    @Override
    public void onProcessArrival(SimProcess p, int time) {
        queue.add(p);
    }

    @Override
    public void onProcessExit(SimProcess p, int time) {
        //remove current process
        queue.remove(0);
        //get waiting time of process adn return
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
        queue.add(currentProcess());
        queue.remove(0);
    }

    @Override
    public String getAlgorithmName() {

        return ("RR with quantum:"+ timeQuantum);
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