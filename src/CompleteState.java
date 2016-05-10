import java.text.SimpleDateFormat;

public class CompleteState implements RideState {
	
	private Ride ride;
	
	public CompleteState(Ride r) {
		this.ride = r;
	}
	
	public String getInfo() {
//		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String formatted = format1.format(ride.getDate());
//		//System.out.println(ride.getStartTime() + " "+ formatted + " " + ride.getStartLocation() + " " + ride.getDriverID() + " " + ride.getRideState());
		return "Ride has been completed";
	}
	
	public void progressState() {
		System.out.println("End of progressState!");
	}



}
