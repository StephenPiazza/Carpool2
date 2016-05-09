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
			String memberId = "invalid";
			if (results.getString(3).equals(password)) {
				 memberId = results.getString(1);
			}
			preparedStatement.close();
			connection.close();
			return memberId;

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
			if (m instanceof Driver) {
				Driver d = (Driver) m;

				statement.execute("INSERT INTO MEMBERS VALUES('" + d.getMemberID() + "','" + d.getFirstName() + "','"
						+ d.getLastName() + "','" + d.getPhoneNumber() + "','" + d.getEmail() + "','" + d.getPassword()
						+ "','" + d.getAddress() + "', 1);");
				statement.execute( "INSERT INTO VEHICLES VALUES('" + d.getMemberID() + "'," + d.getVehicle().getCapacity()+");");

			} else {
				
				statement.execute("INSERT INTO MEMBERS VALUES('" + m.getMemberID() + "','" + m.getFirstName() + "','"
						+ m.getLastName() + "','" + m.getPhoneNumber() + "','" + m.getEmail() + "','" + m.getPassword()
						+ "','" + m.getAddress() + "', 0);");
			}
			statement.execute( "INSERT INTO MemberSchedules (memberID) VALUES('"+m.getMemberID()+"');");
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
	 * 
	 * @param memberID
	 * @return
	 * @throws SQLException
	 */
	public static Member getMemberInfo(String memberID) throws SQLException {
		String query = "SELECT t1.memberID, firstname, lastname, phonenumber, email, password, address, driver, capacity "
					+ "FROM Members t1 LEFT JOIN Vehicles t2 ON t1.memberID = t2.memberID WHERE t1.memberID = '"+ memberID + "';";
		String memberId;
		String firstName;
		String lastName;
		String phoneNumber;
		String email;
		String password;
		String address;
		Boolean driver;
		String query2 = "SELECT * FROM MemberSchedules WHERE MemberID = '"+ memberID+"';";

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
			driver = results.getBoolean("driver");

			Member m;
			if (driver) {
				Vehicle v = new Vehicle(results.getInt("capacity"));
				m = new Driver(memberId, firstName, lastName, phoneNumber, email, password, address, v);
			} else {
				m = new Passenger(memberId, firstName, lastName, phoneNumber, email, password, address);
			}
			
			results.close();
			
			preparedStatement = connection.prepareStatement(query2);
			results = preparedStatement.executeQuery();
			results.first();
			String[][] scheduleArray = {{results.getString("SundayTo"), results.getString("SundayFrom")},
					{results.getString("MondayTo"), results.getString("MondayFrom")},
					{results.getString("TuesdayTo"), results.getString("TuesdayFrom")},
					{results.getString("WednesdayTo"), results.getString("WednesdayFrom")},
					{results.getString("ThursdayTo"), results.getString("ThursdayFrom")},
					{results.getString("FridayTo"), results.getString("FridayFrom")},
					{results.getString("SaturdayTo"), results.getString("SaturdayFrom")}};
			m.setSchedule(new MemberSchedule(scheduleArray));
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
			statement.execute("UPDATE MEMBERS SET FirstName = '" + m.getFirstName() + "'," + "LastName ='"
					+ m.getLastName() + "'," + "PhoneNumber='" + m.getPhoneNumber() + "'," + "Email='" + m.getEmail()
					+ "'," + "Password='" + m.getPassword() + "'," + "Address='" + m.getAddress() + "'"
					+ "WHERE MemberID = '" + m.getMemberID() + "')");
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
	 * 
	 * @param date
	 * @return
	 * @throws SQLException
	 */
	public static ArrayList<Ride> getRidesByDate(Calendar date) throws SQLException {
		ArrayList<Ride> rides = new ArrayList<Ride>();
		String query = "SELECT * FROM Rides WHERE date = '" + date + "';";

		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/carpool", dbUser, dbPassword);
			preparedStatement = connection.prepareStatement(query);
			ResultSet results = preparedStatement.executeQuery();
			// Build Result Set into ArrayList of Rides
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
			results.close();
			preparedStatement.close();
			connection.close();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return rides;
	}

	public static void updateMemberSchedule(Member m) throws SQLException {
		MemberSchedule ms = m.getSchedule();
		String query = "UPDATE MemberSchedules SET sundayTo = " + sqlFormat(ms.getScheduleTime(0, 0), "time") + ", "
					    + "sundayFrom = " + sqlFormat(ms.getScheduleTime(0, 1), "time") + ", " 
						+ "MondayTo = " + sqlFormat(ms.getScheduleTime(1, 0), "time") + ", " 
						+ "MondayFrom = " + sqlFormat(ms.getScheduleTime(1, 1), "time") + ", " 
						+ "TuesdayTo = " + sqlFormat(ms.getScheduleTime(2, 0), "time") + ", " 
						+ "TuesdayFrom = " + sqlFormat(ms.getScheduleTime(2, 1), "time") + ", " 
						+ "WednesdayTo = " + sqlFormat(ms.getScheduleTime(3, 0), "time") + ", " 
						+ "WednesdayFrom = " + sqlFormat(ms.getScheduleTime(3, 1), "time") + ", " 
						+ "ThursdayTo = " + sqlFormat(ms.getScheduleTime(4, 0), "time") + ", " 
						+ "ThursdayFrom = " + sqlFormat(ms.getScheduleTime(4, 1), "time") + ", " 
						+ "FridayTo = " + sqlFormat(ms.getScheduleTime(5, 0), "time") + ", " 
						+ "FridayFrom = " + sqlFormat(ms.getScheduleTime(5, 1), "time") + ", " 
						+ "SaturdayTo = " + sqlFormat(ms.getScheduleTime(6, 0), "time") + ", " 
						+ "SaturdayFrom = " + sqlFormat(ms.getScheduleTime(6, 1), "time") + " " 
						+ "WHERE MemberID = '" + m.getMemberID() + "';";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/carpool", dbUser, dbPassword);
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.execute();
			preparedStatement.close();
			connection.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static String sqlFormat(String s, String type){
		String formatted = "";
		switch(type){
		case "time":
			if ( s == null || s.equalsIgnoreCase("null")) {
				formatted = "NULL";
			}
			else {
				formatted = "'" + s +"'";
			}
			break;
		default:
			break;
		}
		return formatted;
	}
}
