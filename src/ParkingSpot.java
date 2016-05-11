
public class ParkingSpot {
	int spotID;
	Boolean occupied;
	
	public ParkingSpot(int spotID, Boolean occupied) {
		this.spotID = spotID;
		this.occupied = occupied;
	}
	
	public int getSpotID() {
		return spotID;
	}
	
	public Boolean isOccupied() {
		return occupied;
	}
}
