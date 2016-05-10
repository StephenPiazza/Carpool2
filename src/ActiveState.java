import java.text.SimpleDateFormat;

public class ActiveState implements RideState {
	private Ride ride;
	public ActiveState(Ride r) {
		this.ride = r;
	}
	
	public void progressState() {
		ride.setRideState(new CompleteState(ride));
	}
	public void getInfo() {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd-hh-mm");
		String formatted = format1.format(ride.getDate());
		System.out.println(ride.getStartTime() + " "+ formatted + " " + ride.getStartLocation() + " " + ride.getDriverID() + " " + ride.getRideState());
	}
}
