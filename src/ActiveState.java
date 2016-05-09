
public class ActiveState implements RideState {
	private Ride ride;
	public ActiveState(Ride r) {
		this.ride = r;
	}
	@Override
	public void getInfo(){
		
	}
	
	public void progressState() {
		ride.setRideState(new CompleteState(ride));
	}

}
