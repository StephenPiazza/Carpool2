import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

public class RideScheduler {
	
	ArrayList<Ride> rides;
	ScheduleStrategy strategy;
	Calendar date;
	
	public RideScheduler(Calendar date) throws SQLException, ParseException {
		rides = DBController.getRidesByDate(date);
		this.date = date;
	}
	
	public void schedule(Member m) throws SQLException, ParseException{
		setScheduleStrategy(m);
		strategy.schedule(m, date);
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
