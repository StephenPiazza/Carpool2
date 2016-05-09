
public class WaitingState implements RideState {
	protected Ride ride;
	
	public WaitingState(Ride ride){
		this.ride = ride;
	}
	
	public void progressState() {
		ride.setRideState(new ActiveState(ride));
	}
	@Override
	public void getInfo(){
		
	}

}
