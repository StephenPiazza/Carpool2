import java.text.SimpleDateFormat;

public class WaitingState implements RideState {
	private Ride ride;
	public WaitingState(Ride r) {
		this.ride = r;
	}
	
	public void getInfo() {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd-hh-mm");
		String formatted = format1.format(ride.getDate());
		System.out.println(ride.getStartTime() + " "+ formatted + " " + ride.getStartLocation() + " " + ride.getDriver() + " " + ride.getRideState());
	}
	public void progressState() {
		ride.setRideState(new ActiveState(ride));
	}
}
