
public class Driver extends Member {
	
	Vehicle vehicle;
	
	public Driver(String memberID, String firstName, String lastName, String phoneNumber, String address, String email,  String password, Vehicle vehicle){
		super(memberID, firstName, lastName, phoneNumber, address, email, password);
		this.vehicle = vehicle;
	}
	
	public Vehicle getVehicle(){
		return vehicle;
	}

}
