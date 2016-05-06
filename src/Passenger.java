
public class Passenger extends Member {
	
	public Passenger(String memberID, String firstName, String lastName, String phoneNumber, String email, String address, String password) {
		super(memberID, firstName, lastName, phoneNumber, email, address, password);
	}
	
	public Passenger(String firstName, String lastName, String phoneNumber, String email, String address, String password) {
		super(firstName, lastName, phoneNumber, email, address, password);
	}
}
