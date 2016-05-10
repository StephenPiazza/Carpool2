import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import sun.security.jca.GetInstance;

public class CarpoolSystem {

	private static Scanner scanner = new Scanner(System.in);
	private static String loggedInID;

	public static void main(String[] args) {

		boolean exit = false;
		String response = "";
		while (!exit) {
			System.out.println("Welcome to SJSU Carpool");
			System.out.println("[1] Login");
			System.out.println("[2] Register");
			System.out.println("[3] Exit");
			response = scanner.nextLine();
			int selection = Integer.parseInt(response);
			switch (selection) {
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

	/**
	 * Login Menu with authentication
	 */
	public static void login() {
		String email;
		String password;
		System.out.println("Enter Email: ");
		email = scanner.nextLine();
		System.out.println("Enter Password: ");
		password = scanner.nextLine();
		try {
			String tempID = DBController.authenticate(email, password);
			if (!tempID.equalsIgnoreCase("invalid")) {
				loggedInID = tempID;
				mainMenu();
			} else {
				System.out.println("Invalid user info!");
				System.out.println("");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Screen for registering new members
	 */
	public static void register() {
		Member newMember;
		String memberID;
		String firstName;
		String lastName;
		String phoneNumber;
		String email;
		String password;
		String address;
		String driver;
		int vehicleCapacity = 0;

		System.out.println("Enter First Name:");
		firstName = scanner.nextLine();
		System.out.println("Enter Last Name:");
		lastName = scanner.nextLine();
		System.out.println("Enter Phone Number:");
		phoneNumber = scanner.nextLine();
		System.out.println("Enter Address:");
		address = scanner.nextLine();
		System.out.println("Enter Email:");
		email = scanner.nextLine();
		System.out.println("Enter Password");
		password = scanner.nextLine();
		System.out.println("Are you registering as a Driver? [Y/N]");
		driver = scanner.nextLine();

		memberID = lastName + phoneNumber.substring(phoneNumber.length() - 4, phoneNumber.length());
		if (driver.equalsIgnoreCase("y")) {
			System.out.println("How many passengers can your vehicle hold?");
			String response = scanner.nextLine();
			vehicleCapacity = Integer.parseInt(response);
			Vehicle vehicle = new Vehicle(vehicleCapacity);
			newMember = new Driver(memberID, firstName, lastName, phoneNumber, address, email, password, vehicle);
		} else {
			newMember = new Passenger(memberID, firstName, lastName, phoneNumber, address, email, password);
		}

		try {
			DBController.registerNewMember(newMember);
			loggedInID = newMember.getMemberID();
			mainMenu();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Main Menu once logged in
	 */
	public static void mainMenu() throws SQLException {
		boolean exit = false;
		while (!exit) {
			System.out.println("SJSU Carpool Main Menu");
			System.out.println("[1] Edit Profile");
			System.out.println("[2] Edit Schedule");
			System.out.println("[3] Schedule Ride");
			System.out.println("[4] View Rides");
			System.out.println("[5] View Notifications");
			System.out.println("[6] Exit");
			String response = scanner.nextLine();
			int selection = Integer.parseInt(response);
			switch (selection) {
			case 1:
				editProfile();
				break;
			case 2:
				editSchedule();
				break;
			case 3:
				scheduleRide();
				break;
			case 4:
				break;
			case 5:
				break;
			case 6:
				exit = true;
				break;
			default:
				break;
			}
		}
	}

	/**
	 * Edit existing member info
	 */
	public static void editProfile() throws SQLException {

		Member m = DBController.getMemberInfo(loggedInID);

		System.out.println("Enter First Name: ");
		m.setFirstName(scanner.nextLine());
		System.out.println("Enter Last Name: ");
		m.setLastName(scanner.nextLine());
		System.out.println("Enter PhoneNumber: ");
		m.setPhoneNumber(scanner.nextLine());
		System.out.println("Enter Email: ");
		m.setEmail(scanner.nextLine());
		System.out.println("Enter Password: ");
		m.setPassword(scanner.nextLine());

		if (DBController.updateMemberInfo(m)) {
			System.out.println("Update Unsuccesful!");
		}

	}

	/**
	 * Edit existing member schedule
	 */
	public static void editSchedule() throws SQLException {

		Boolean exit = false;
		Member m = DBController.getMemberInfo(loggedInID);
		while (!exit) {
			System.out.println("Current Schedule:");
			System.out.println(m.getSchedule().printSchedule());
			System.out.println("What day would you like to edit?");
			String response = scanner.nextLine();
			int selection = Integer.parseInt(response);
			System.out.println("What time will you leave to Campus [HH:MM]? (15 minute intervals)");
			String to = scanner.nextLine();
			System.out.println("What time will you leave from Campus[HH:MM]? (15 minute intervals)");
			String from = scanner.nextLine();
			System.out.println(to);
			System.out.println(from);
			m.getSchedule().setScheduleTime(selection - 1, to, from);
			System.out.println("Would you like to edit any other days? [Y/N]");
			if (scanner.nextLine().equalsIgnoreCase("N")) {
				exit = true;
			}
		}
		DBController.updateMemberSchedule(m);
	}

	public static void scheduleRide() throws SQLException {
//		System.out.println("Enter Date to schedule Ride [yyyy-MM-dd HH:mm:ss]:");
//		String dateString = scanner.nextLine();
//	
		try {
			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();
//			cal.setTime(sdf.parse(dateString));
//			System.out.println(cal.getTime().toGMTString());
			RideScheduler rs = new RideScheduler(cal);
			rs.schedule(DBController.getMemberInfo(loggedInID));
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	public static void viewRides() {

	}

}
