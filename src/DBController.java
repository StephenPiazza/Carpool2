import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DBController {
	static ArrayList<String> array = new ArrayList<String>();
	private static Connection connection = null;
	private static Statement statement = null;
	private static PreparedStatement preparedStatement = null;
	private static String connectionString = "jdbc:mysql://localhost:3306/carpool";
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
			connection = DriverManager.getConnection(connectionString, dbUser, dbPassword);
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
	 * @param m Member to register
	 * @return
	 * @throws SQLException
	 */
	public static boolean registerNewMember(Member m) throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(connectionString, dbUser, dbPassword);
			statement = connection.createStatement();
			if (m instanceof Driver) {
				Driver d = (Driver) m;

				statement.execute("INSERT INTO MEMBERS VALUES('" + d.getMemberID() + "','" + d.getFirstName() + "','"
						+ d.getLastName() + "','" + d.getPhoneNumber() + "','" + d.getEmail() + "','" + d.getPassword()
						+ "','" + d.getAddress() + "', 1);");
				statement.execute(
						"INSERT INTO VEHICLES VALUES('" + d.getMemberID() + "'," + d.getVehicle().getCapacity() + ");");

			} else {

				statement.execute("INSERT INTO MEMBERS VALUES('" + m.getMemberID() + "','" + m.getFirstName() + "','"
						+ m.getLastName() + "','" + m.getPhoneNumber() + "','" + m.getEmail() + "','" + m.getPassword()
						+ "','" + m.getAddress() + "', 0);");
			}
			statement.execute("INSERT INTO MemberSchedules (memberID) VALUES('" + m.getMemberID() + "');");
			statement.execute("INSERT INTO Member_balance (memberID, cc, point, cash) VALUES('" + m.getMemberID() + "', 50, 1000, 50);");
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
				+ "FROM Members t1 LEFT JOIN Vehicles t2 ON t1.memberID = t2.memberID WHERE t1.memberID = '" + memberID
				+ "';";
		String memberId;
		String firstName;
		String lastName;
		String phoneNumber;
		String email;
		String password;
		String address;
		Boolean driver;
		String query2 = "SELECT * FROM MemberSchedules WHERE MemberID = '" + memberID + "';";

		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(connectionString, dbUser, dbPassword);
			preparedStatement = connection.prepareStatement(query);
			ResultSet results = preparedStatement.executeQuery();
			results.first();

			memberId = results.getString("memberID");
			firstName = results.getString("firstName");
			lastName = results.getString("LastName");
			phoneNumber = results.getString("phoneNumber");
			email = results.getString("email");
			password = results.getString("password");
			address = results.getString("address");
			driver = results.getBoolean("driver");

			Member m;
			if (driver) {
				Vehicle v = new Vehicle(results.getInt("capacity"));
				m = new Driver(memberId, firstName, lastName, phoneNumber, address, email, password,  v);
			} else {
				m = new Passenger(memberId, firstName, lastName, phoneNumber, address, email, password);
			}

			results.close();

			preparedStatement = connection.prepareStatement(query2);
			results = preparedStatement.executeQuery();
			results.first();
			String[][] scheduleArray = { { results.getString("SundayTo"), results.getString("SundayFrom") },
					{ results.getString("MondayTo"), results.getString("MondayFrom") },
					{ results.getString("TuesdayTo"), results.getString("TuesdayFrom") },
					{ results.getString("WednesdayTo"), results.getString("WednesdayFrom") },
					{ results.getString("ThursdayTo"), results.getString("ThursdayFrom") },
					{ results.getString("FridayTo"), results.getString("FridayFrom") },
					{ results.getString("SaturdayTo"), results.getString("SaturdayFrom") } };
			m.setSchedule(new MemberSchedule(scheduleArray));
			preparedStatement.close();
			connection.close();

			return m;

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return new Member();
	}
	
	/**
	 * Update Member info in the database
	 * @param m Member to update
	 * @return
	 * @throws SQLException
	 */
	public static boolean updateMemberInfo(Member m) throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(connectionString, dbUser, dbPassword);
			statement = connection.createStatement();
			statement.execute("UPDATE MEMBERS SET FirstName = '" + m.getFirstName() + "'," + "LastName ='"
					+ m.getLastName() + "'," + "PhoneNumber='" + m.getPhoneNumber() + "'," + "Email='" + m.getEmail()
					+ "'," + "Password='" + m.getPassword() + "'," + "Address='" + m.getAddress() + "'"
					+ "WHERE MemberID = '" + m.getMemberID() + "'");
			statement.close();
			connection.close();
			return true;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Get all passengers available for a given date/time
	 * @param date
	 * @return
	 * @throws SQLException
	 */
	public static ArrayList<Member> getPassengersByDate(Calendar date) throws SQLException {
		String[] dayOfWeek = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };
		ArrayList<Member> passengers = new ArrayList<>();
		int day = date.get(Calendar.DAY_OF_WEEK) -1;
		String query = "SELECT  t1.memberId, firstname, lastName " 
				+ "FROM Members t1 INNER JOIN MemberSchedules t2 ON t1.MemberID = t2.MemberID "
				+ "WHERE driver = 0 " 
				+ "AND (" + dayOfWeek[day] + "To = '"+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date.getTime()) + "'" + " OR " + dayOfWeek[day]
				+ "From = '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date.getTime()) + "')";
		String memberID;
		String firstName;
		String lastName;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(connectionString, dbUser, dbPassword);
			preparedStatement = connection.prepareStatement(query);
			ResultSet results = preparedStatement.executeQuery();
			results.first();
			while (results.next()) {

				memberID = results.getString(1);
				firstName = results.getString(2);
				lastName = results.getString(3);

				Passenger p = new Passenger(memberID, firstName, lastName, "", "", "", "");
				passengers.add(p);
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return passengers;
	}

	/**
	 * Returns all Rides that are scheduled for a given member
	 * 
	 * @param m
	 * @return
	 * @throws SQLException
	 * @throws ParseException
	 */
	public static ArrayList<Ride> getRidesByMember(Member m) throws SQLException, ParseException {
		ArrayList<Ride> rides = new ArrayList<Ride>();
		String query = "SELECT * FROM Rides WHERE DriverID = '" + m.getMemberID() + "';";

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(connectionString, dbUser, dbPassword);
			preparedStatement = connection.prepareStatement(query);
			ResultSet results = preparedStatement.executeQuery();
			// Build Result Set into ArrayList of Rides
			while (results.next()) {
				int rideID = results.getInt("RideID");
				String startLocation = results.getString("StartLocation");
				String startTime = results.getString("StartTime");
				String driverID = results.getString("DriverID");
				ArrayList<String> passengers = parseCSV(results.getString("Passengers"));
				ArrayList<String> stops = parseCSV(results.getString("stops"));

				Calendar calendar = Calendar.getInstance();
				calendar.setTime(formatter.parse(startTime));
				Ride ride = new Ride(rideID, calendar, startLocation, driverID, passengers, stops);
				int state = results.getInt("State");

				RideState rs = new WaitingState(ride);
				
				if (state == 2) {
					rs = new CompleteState(ride);
				} else if (state == 1) {
					rs = new ActiveState(ride);
				} else if (state == 0) {
					rs = new WaitingState(ride);
				}
				ride.setRideState(rs);

				
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

	/**
	 * Returns all Rides that are scheduled for a given date.
	 * 
	 * @param date
	 * @return
	 * @throws SQLException
	 * @throws ParseException
	 */
	public static ArrayList<Ride> getRidesByDate(Calendar date) throws SQLException, ParseException {
		ArrayList<Ride> rides = new ArrayList<Ride>();
		String query = "SELECT * FROM Rides WHERE startTime BETWEEN '" + date.get(Calendar.YEAR) + "-"
				+ date.get(Calendar.MONTH) + "-" + (date.get(Calendar.DAY_OF_MONTH) + 1) + " 00:00:00'" + " AND '"
				+ date.get(Calendar.YEAR) + "-" + (date.get(Calendar.MONTH) + 1) + "-" + date.get(Calendar.DAY_OF_MONTH)
				+ " 23:59:59';";

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(connectionString, dbUser, dbPassword);
			preparedStatement = connection.prepareStatement(query);
			ResultSet results = preparedStatement.executeQuery();
			// Build Result Set into ArrayList of Rides
			while (results.next()) {
				int rideID = results.getInt("RideID");
				String startLocation = results.getString("StartLocation");
				String startTime = results.getString("StartTime");
				String driverID = results.getString("DriverID");
				ArrayList<String> passengers = parseCSV(results.getString("Passengers"));
				ArrayList<String> stops = parseCSV(results.getString("Stops"));
				int state = results.getInt("State");


				Calendar calendar = Calendar.getInstance();
				calendar.setTime(formatter.parse(startTime));
				Ride ride = new Ride(rideID, calendar, startLocation, driverID, passengers, stops);
				RideState rs = new WaitingState(ride);
				
				if (state == 2) {
					rs = new CompleteState(ride);
				} else if (state == 1) {
					rs = new ActiveState(ride);
				} else if (state == 0) {
					rs = new WaitingState(ride);
				}
				ride.setRideState(rs);

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

	/**
	 * Input new ride into the database;
	 * @param r
	 * @throws SQLException
	 */
	public static void createRide(Ride r) throws SQLException {
		String query = "INSERT INTO Rides (startTime, StartLocation, DriverID, state) "
				+ "VALUES ('" + r.getStartTime() + "','" + r.getStartLocation() + "', '" + r.getDriverID() +"', 0);";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(connectionString, dbUser, dbPassword);
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.execute();
			preparedStatement.close();
			connection.close();
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Update Ride in the Database
	 * @param r
	 * @throws SQLException
	 */
	public static void updateRide(Ride r) throws SQLException {
		// Set Ride state as a int flag in database
		int stateInt = 0;
		if (r.getRideState() instanceof WaitingState) {
			stateInt = 0;
		} else if (r.getRideState() instanceof ActiveState) {
			stateInt = 1;
		} else if (r.getRideState() instanceof CompleteState) {
			stateInt = 2;
		}

		// Set Passenger list as Comma Separated Value
		String passengerCSV = "";
		for (int i = 0; i < r.getPassengers().size(); i++) {
			if (i != r.getPassengers().size()-1){
			passengerCSV += r.getPassengers().get(i) + ";";
			}
			else {
				passengerCSV += r.getPassengers().get(i);
			}
		}

		// Set stop list as Colon Separated Value
		String stopCSV = "";
		for (int i = 0; i < r.getStops().size(); i++) {
			
			stopCSV += r.getStops().get(i) + ",";
		}

		String query = "UPDATE Rides SET Passengers = '" + passengerCSV + "', Stops = '" + stopCSV + "', state = "
				+ stateInt + " WHERE RideID = "+ r.getRideID()+";";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(connectionString, dbUser, dbPassword);
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.execute();
			preparedStatement.close();
			connection.close();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Update MemberSchedule in Database
	 * @param m
	 * @throws SQLException
	 */
	public static void updateMemberSchedule(Member m) throws SQLException {
		MemberSchedule ms = m.getSchedule();
		String query = "UPDATE MemberSchedules SET sundayTo = " + sqlFormat(ms.getScheduleTime(0, 0), "time") + ", "
				+ "sundayFrom = " + sqlFormat(ms.getScheduleTime(0, 1), "time") + ", " + "MondayTo = "
				+ sqlFormat(ms.getScheduleTime(1, 0), "time") + ", " + "MondayFrom = "
				+ sqlFormat(ms.getScheduleTime(1, 1), "time") + ", " + "TuesdayTo = "
				+ sqlFormat(ms.getScheduleTime(2, 0), "time") + ", " + "TuesdayFrom = "
				+ sqlFormat(ms.getScheduleTime(2, 1), "time") + ", " + "WednesdayTo = "
				+ sqlFormat(ms.getScheduleTime(3, 0), "time") + ", " + "WednesdayFrom = "
				+ sqlFormat(ms.getScheduleTime(3, 1), "time") + ", " + "ThursdayTo = "
				+ sqlFormat(ms.getScheduleTime(4, 0), "time") + ", " + "ThursdayFrom = "
				+ sqlFormat(ms.getScheduleTime(4, 1), "time") + ", " + "FridayTo = "
				+ sqlFormat(ms.getScheduleTime(5, 0), "time") + ", " + "FridayFrom = "
				+ sqlFormat(ms.getScheduleTime(5, 1), "time") + ", " + "SaturdayTo = "
				+ sqlFormat(ms.getScheduleTime(6, 0), "time") + ", " + "SaturdayFrom = "
				+ sqlFormat(ms.getScheduleTime(6, 1), "time") + " " + "WHERE MemberID = '" + m.getMemberID() + "';";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(connectionString, dbUser, dbPassword);
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.execute();
			preparedStatement.close();
			connection.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<ParkingSpot> getParkingLot() throws SQLException {
		ArrayList<ParkingSpot> ps = new ArrayList<>();
		String query ="SELECT * FROM ParkingLot";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(connectionString, dbUser, dbPassword);
			preparedStatement = connection.prepareStatement(query);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				int occupied = rs.getInt("Occupied");
				Boolean occ = (occupied == 1) ? true : false;
				ParkingSpot p = new ParkingSpot(rs.getInt("spotID"), occ);
				ps.add(p);
			}
			preparedStatement.close();
			connection.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return ps;
	}
	
	public static void updateParkingSpot(ParkingSpot ps) throws SQLException {
		int occupied = (ps.isOccupied()) ? 1: 0;
		String query = "UPDATE ParkingLot SET Occupied = " + occupied + " WHERE spotID = " + ps.getSpotID() + ";";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(connectionString, dbUser, dbPassword);
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.execute();
			preparedStatement.close();
			connection.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Format strings for database insertion
	 * @param s
	 * @param type
	 * @return
	 */
	private static String sqlFormat(String s, String type) {
		String formatted = "";
		switch (type) {
		case "time":
			if (s == null || s.equalsIgnoreCase("null")) {
				formatted = "NULL";
			} else {
				formatted = "'" + s + "'";
			}
			break;
		default:
			break;
		}
		return formatted;
	}

	/**
	 * Parse a comma separated value into array list s
	 * @param s
	 * @return
	 */
	private static ArrayList<String> parseCSV(String s) {
		ArrayList<String> parsed = new ArrayList<>();
		if (s != null) {
			String[] split = s.split(";");
			for (String st : split) {
				if (!st.equals("")) {
					parsed.add(st);
				}
			}
		}
		return parsed;
	}

}
