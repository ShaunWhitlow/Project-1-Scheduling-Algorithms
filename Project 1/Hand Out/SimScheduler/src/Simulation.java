
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Simulation {

	public static void run(String file, Scheduler scheduler) {
		List<SimProcess> processesToArrive = null;
		try {
			processesToArrive = readProcesses(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Algorithm: " + scheduler.getAlgorithmName());
		System.out.println("File name: " + file);
		System.out.println("Starting simulation...");

		int time = 0;
		int lastClockInterruptTime = 0;
		SimProcess previousProcess = null;
		SimProcess currentProcess = null;

		while (!processesToArrive.isEmpty() || !scheduler.isEmpty()) {

			// Checks if any new process arrived and generates new process interrupt
			Iterator<SimProcess> iterator = processesToArrive.iterator();
			while (iterator.hasNext()) {
				SimProcess p = iterator.next();
				if (time >= p.getTimeOfArrival()) {
					scheduler.onProcessArrival(p, time);
					iterator.remove();
				}
			}

			// Generate clock interrupt if configured
			if (Clock.ENABLE_INTERRUPT && (time - lastClockInterruptTime) == Clock.INTERRUPT_INTERVAL) {
				scheduler.onClockInterrupt(time - lastClockInterruptTime, time);
				lastClockInterruptTime = time;
			}
			
			currentProcess = scheduler.currentProcess();

			if (previousProcess != currentProcess) {
				if(previousProcess != null && !previousProcess.isFinished()) {
					System.out.println("Stop running Process {"
							+ "Id='" + previousProcess.getId() 
							+ ", Current Time=" + time + "}");
				}
				System.out.println("Start running Process {"
						+ "Id='" + currentProcess.getId() 
						+ "', Arrival Time=" + currentProcess.getTimeOfArrival()
						+ ", Burst Time=" + currentProcess.getBurstTime()
						+ ", Current Time=" + time + "}");
			}

			// Increments unit of time
			time++;

			if (currentProcess != null) {
				previousProcess = currentProcess;
				// Runs the CPU for a unit of time
				CPU.run(scheduler.currentProcess());

				// Generates a process exit interrupt if the process has finished.
				if (scheduler.currentProcess().isFinished()) {
					scheduler.onProcessExit(scheduler.currentProcess(), time);
				}
			}
		}

		System.out.println("Final Time: " + time);
		System.out.println("End of simulation.");
	}

	public static List<SimProcess> readProcesses(String file) throws IOException {
		BufferedReader inFile = new BufferedReader(new FileReader(file));
		List<SimProcess> processList = new ArrayList<>();
		String line;

		// read in the tasks and populate the Ready queue
		while ((line = inFile.readLine()) != null) {
			String[] params = line.split(",\\s*");
			processList.add(new SimProcess(params[0], Integer.parseInt(params[1]), Integer.parseInt(params[2])));
		}

		inFile.close();
		return processList;
	}

}
