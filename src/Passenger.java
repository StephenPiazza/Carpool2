
public class Passenger extends Member {
	
	public Passenger(String memberID, String firstName, String lastName, String phoneNumber, String address, String email, String password) {
		super(memberID, firstName, lastName, phoneNumber, address, email, password);
	}
	
	public Passenger(String firstName, String lastName, String phoneNumber,String address, String email,  String password) {
		super(firstName, lastName, phoneNumber, address, email, password);
	}
}
