
public class Driver extends Member {
	
	Vehicle vehicle;
	
	public Driver(String memberID, String firstName, String lastName, String phoneNumber, String email, String address, String password, Vehicle vehicle){
		super(memberID, firstName, lastName, phoneNumber, email, address, password);
		this.vehicle = vehicle;
	}
	
	public Vehicle getVehicle(){
		return vehicle;
	}

}
