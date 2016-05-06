
public class Member {
	String memberID;
	String firstName;
	String lastName;
	String phoneNumber;
	String email;
	String address;
	String password;
	MemberSchedule schedule;
	
	/**
	 * Empty Constructors
	 */
	public Member() {
		this.memberID = "";
		this.firstName = "";
		this.lastName = "";
		this.email = "";
		this.address = "";
		this.password = "";
	}
	
	/**
	 * Constructor for existing members;
	 * @param memberID
	 * @param firstName
	 * @param lastName
	 * @param phoneNumber
	 * @param email
	 * @param address
	 * @param password
	 */
	public Member(String memberID, String firstName, String lastName, String phoneNumber, String email, String address, String password) {
		this.memberID = memberID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.address = address;
		this.password = password;
	}

	/**
	 * Constructor for new Members. Will parse new Member ID;
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param address
	 * @param password
	 */
	public Member(String firstName, String lastName, String phoneNumber, String email, String address, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.address = address;
		this.password = password;
		this.memberID = lastName + phoneNumber.substring(phoneNumber.length()-4, phoneNumber.length());
	}

	public String getMemberID(){
		return memberID;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public MemberSchedule getSchedule(){
		return schedule;
	}

}
