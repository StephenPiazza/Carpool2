import java.text.SimpleDateFormat;

public class WaitingState implements RideState {
	
	private Ride ride;
	
	public WaitingState(Ride r) {
		this.ride = r;
	}
	
	public String getInfo() {
//		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String formatted = format1.format(ride.getDate());
//		//System.out.println(ride.getStartTime() + " "+ formatted + " " + ride.getStartLocation() + " " + ride.getDriverID() + " " + ride.getRideState());
		return "Currently waiting to Start Ride.";
	}
	
	public void progressState() {
		ride.setRideState(new ActiveState(ride));
	}

}
