import java.text.SimpleDateFormat;

public class ActiveState implements RideState {
	
	private Ride ride;
	
	public ActiveState(Ride r) {
		this.ride = r;
	}
	
	public String getInfo() {
//		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String formatted = format1.format(ride.getDate());
//		//System.out.println(ride.getStartTime() + " "+ formatted + " " + ride.getStartLocation() + " " + ride.getDriverID() + " " + ride.getRideState());
		return "Ride is currently underway";
	}
	
	public void progressState() {
		ride.setRideState(new CompleteState(ride));
	}
}
