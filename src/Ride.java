import java.util.Calendar;
import java.util.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Ride {

	int rideID;
	Calendar date;
	String startLocation;
	String driverID;
	ArrayList<String> passengers;
	ArrayList<String> stops;
	RideState state;
	
	public Ride(int rideID, Calendar date, String startLocation, String driverID, ArrayList<String> passengers, ArrayList<String> stops){
		this.rideID = rideID;
		this.date = date;
		this.startLocation = startLocation;
		this.driverID = driverID;
		this.passengers = passengers;
		this.stops = stops;
	}

	public Calendar getDate() {
		return date;
	}

	public String getStartTime() {
		Date tempDate = date.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdf.format(tempDate);
	}

	public void setStartTime(long time) {
		this.date.setTime(new Date(time));
	}

	public String getStartLocation() {
		return startLocation;
	}

	public void setStartLocation(String startLocation) {
		this.startLocation = startLocation;
	}

	public String getDriverID() {
		return driverID;
	}

	public ArrayList<String> getPassengers() {
		return passengers;
	}

	public boolean addPassenger(Member m) throws SQLException {
		Driver d = (Driver) DBController.getMemberInfo(driverID);
		if (passengers.size() < d.getVehicle().getCapacity() && !passengers.contains(m.getMemberID())){
			passengers.add(m.getMemberID());
			return true;
		}

		return false;
	}
	
	public boolean removePassenger(Member m) {
		for (int i = 0; i < passengers.size(); i++) {
			if (passengers.get(i).equals(m.getMemberID())) {
				passengers.remove(i);
				return true;
			}
		}
		return false;
	}

	public ArrayList<String> getStops() {
		return stops;
	}

	public void addStop(String stop) {
		stops.add(stop);
	}
	
	public void removeStop(String stop) {
		stops.remove(stop);
	}
	
	public RideState getRideState() {
		return state;
	}
	
	public void setRideState(RideState state) {
		this.state = state;
	}

}
