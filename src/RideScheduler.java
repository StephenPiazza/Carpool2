import java.util.ArrayList;
import java.util.Calendar;

public class RideScheduler {
	
	ArrayList<Ride> rides;
	ScheduleStrategy strategy;
	Calendar date;
	
	public RideScheduler(Calendar date) {
		
	}
	
	public void schedule(Member m){
		setScheduleStrategy(m);
		strategy.schedule(m);
	}
	
	public void setScheduleStrategy(Member m) {
		if (m instanceof Driver) {
			strategy = new DriverScheduling();
		}
		else if (m instanceof Passenger) {
			strategy = new PassengerScheduling();
		}
		else{
			strategy = new SystemScheduling();
		}
	}

}
