import java.util.Calendar;
import java.util.ArrayList;

public class Ride {

	Calendar date;
	int startTime;
	String startLocation;
	Driver driver;
	ArrayList<String> passengers;
	String[] stops;
	
	public Ride(Calendar date, int startTime, String startLocation, Driver driver, ArrayList passengers, String[] stops){
		this.date = date;
		this.startTime = startTime;
		this.startLocation = startLocation;
		this.driver = driver;
		this.passengers = passengers;
		this.stops = stops;
	}

	public Calendar getDate() {
		return date;
	}

	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public String getStartLocation() {
		return startLocation;
	}

	public void setStartLocation(String startLocation) {
		this.startLocation = startLocation;
	}

	public Member getDriver() {
		return driver;
	}

	public ArrayList<String> getPassengers() {
		return passengers;
	}

	public boolean addPassenger(Member m) {
		if (passengers.size() >= driver.getVehicle().getCapacity()){
			return false;
		}
		else{
			passengers.add(m.getEmail());
		}
		return true;
	}
	
	public boolean removePassenger(Member m) {
		for (int i = 0; i < passengers.size(); i++) {
			if (passengers.get(i).equals(m.getEmail())) {
				passengers.remove(i);
				return true;
			}
		}
		return false;
	}

	public String[] getStops() {
		return stops;
	}

	public void setStops(String[] stops) {
		this.stops = stops;
	}
	
}
