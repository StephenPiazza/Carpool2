import java.sql.SQLException;
import java.util.Scanner;

public class CarpoolSystem {
	
	private static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {

		boolean exit = false;
		int response = 0;
		while (!exit) {
			System.out.println("Welcome to SJSU Carpool");
			System.out.println("[1] Login");
			System.out.println("[2] Register");
			System.out.println("[3] Exit");
			response = scanner.nextInt();
			switch(response) {
			case 1:
				login();
				break;
			case 2:
				register();
				break;
			case 3:
				exit = true;
				break;
			default:
				break;
			}
		}
	}
	
	public static void login() {
		String email;
		String password;
		System.out.println("Enter Email: ");
		email = scanner.next();
		System.out.println("Enter Password: ");
		password = scanner.next();
		try{
		if (DBController.authenticate(email, password)) {
			mainMenu();
		} else {
			System.out.println("Invalid user info!");
			System.out.println("");
		}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void register() {
		Member newMember;
		String firstName;
		String lastName;
		String phoneNumber;
		String email;
		String password;
		String address;
		String driver;
		int vehicleCapacity = 0;

		System.out.println("Enter First Name:");
		firstName = scanner.next();
		System.out.println("Enter Last Name:");
		lastName = scanner.next();
		System.out.println("Enter Phone Number:");
		phoneNumber = scanner.next();
		System.out.println("Enter Address:");
		address = scanner.next();
		System.out.println("Enter Email:");
		email = scanner.next();
		System.out.println("Enter Password");
		password = scanner.next();
		System.out.println("Are you registering as a Driver? [Y/N]");
		driver = scanner.next();
		
		if (driver.equalsIgnoreCase("y")) {
			System.out.println("How many passengers can your vehicle hold?");
			vehicleCapacity = scanner.nextInt();
			Vehicle vehicle = new Vehicle(vehicleCapacity);
			newMember = new Driver(firstName, lastName, phoneNumber, email, address, password, vehicle);
		}
		else {
			newMember = new Passenger(firstName, lastName, phoneNumber, email, address, password);
		}
		
		try {
			DBController.registerNewMember(newMember);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	public static void mainMenu() {
		boolean exit = false;
		while (!exit) {
			System.out.println("SJSU Carpool Main Menu");
			System.out.println("[1] Edit Profile");
			System.out.println("[2] Edit Schedule");
			System.out.println("[3] Find Ride");
			System.out.println("[4] View Rides");
			System.out.println("[5] View Notifications");
			System.out.println("[6] Exit");
		}
	}
}
