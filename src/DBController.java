import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;

public class DBController {
	static ArrayList<String> array = new ArrayList<String>();
	private static Connection connection = null;
	private static Statement statement = null;
	private static PreparedStatement preparedStatement = null;
	private static String dbUser = "root";
	private static String dbPassword = "password";

	/**
	 * Authenticate member credentials against database table.
	 * 
	 * @param name
	 * @param password
	 * @return
	 */
	public static String authenticate(String email, String password) throws SQLException {
		String query = "SELECT MemberId, Email, password FROM Members WHERE email = '" + email + "';";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/carpool", dbUser, dbPassword);
			preparedStatement = connection.prepareStatement(query);
			ResultSet results = preparedStatement.executeQuery();
			results.first();
			if (results.getString(3).equals(password)){
				preparedStatement.close();
				connection.close();
				return results.getString(1);
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return "invalid";
	}

	/**
	 * Input new Member into the database
	 * 
	 * @param m
	 * @return
	 * @throws SQLException
	 */
	public static boolean registerNewMember(Member m) throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/carpool", dbUser, dbPassword);
			statement = connection.createStatement();
			statement.execute("INSERT INTO MEMBERS VALUES('" + m.getMemberID() + "','" + m.getFirstName() + "','"
					+ m.getLastName() + "','" + m.getPhoneNumber() + "','" + m.getEmail() + "','" + m.getPassword()
					+ "','" + m.getAddress() + "')");
			statement.close();
			connection.close();
			return true;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Retrieve Member info from database based on Member ID
	 * @param memberID
	 * @return
	 * @throws SQLException
	 */
	public static Member getMemberInfo(String memberID) throws SQLException {
		String query = "SELECT MemberId, firstname, lastname, phonenumber, email, password, address FROM Members WHERE memberID = '" + memberID + "';";
		String memberId;
		String firstName;
		String lastName;
		String phoneNumber;
		String email;
		String password;
		String address;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/carpool", dbUser, dbPassword);
			preparedStatement = connection.prepareStatement(query);
			ResultSet results = preparedStatement.executeQuery();
			results.first();
			
			memberId = results.getString(1);
			firstName = results.getString(2);
			lastName = results.getString(3);
			phoneNumber = results.getString(4);
			email = results.getString(5);
			password = results.getString(6);
			address = results.getString(7);
			
			Member m = new Passenger(memberId, firstName, lastName, phoneNumber, email, password, address);
			preparedStatement.close();
			connection.close();
			return m;

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return new Member();
	}
	
	public static boolean updateMemberInfo(Member m) throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/carpool", dbUser, dbPassword);
			statement = connection.createStatement();
			statement.execute("UPDATE MEMBERS SET FirstName = '" + m.getFirstName() + "',"
							+ "LastName ='"+ m.getLastName() + "',"
							+ "PhoneNumber='" + m.getPhoneNumber() + "',"
							+ "Email='" + m.getEmail() + "',"
							+ "Password='" + m.getPassword() + "',"
							+ "Address='" + m.getAddress() + "'"
							+ "WHERE MemberID = '"+ m.getMemberID() +"')");
			statement.close();
			connection.close();
			return true;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Returns all Rides that are scheduled for a given date.
	 * @param date
	 * @return
	 * @throws SQLException
	 */
	public static ArrayList<Ride> getRidesByDate(Calendar date) throws SQLException {
		ArrayList<Ride> rides = new ArrayList<Ride>();
		String query = "SELECT * FROM Rides WHERE date = '"+date+"';";
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/carpool", dbUser, dbPassword);
			preparedStatement = connection.prepareStatement(query);
			ResultSet results = preparedStatement.executeQuery();
			//Build Result Set into ArrayList of Rides
			while (results.next()) {
				String rideDate = results.getString("Date");
				String startLocation = results.getString("StartLocation");
				int startTime = results.getInt("StartTime");
				String driver = results.getString("Driver");
				ArrayList<String> passengers = new ArrayList<>();
				ArrayList<String> stops = new ArrayList<>();
				Ride ride = new Ride(date, startTime, startLocation, driver, passengers, stops);
				rides.add(ride);
			}
			
			
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		
		return rides;
		
		
	}

}
