

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		String file = "schedule3.txt";

		Scheduler s; 
		s = new FCFS();
//		s = new SRTF();
//		s = new SJF();
//		s = new RR(5);
        Simulation.run(file, s);
	}
	
}
