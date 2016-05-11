import java.util.ArrayList;

public class ParkingLot {
	ArrayList<ParkingSpot> lot;
	
	public ParkingLot() {
		this.lot = new ArrayList<ParkingSpot>();
	}
	
	public ParkingLot(ArrayList<ParkingSpot> lot) {
		this.lot = lot;
	}
	
	public ParkingSpot getUnoccupiedSpot() {
		for(ParkingSpot ps : lot) {
			if (!ps.isOccupied()) {
				return ps;
			}
		}
		return new ParkingSpot(-1, true);
	}
	
}
