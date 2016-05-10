import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

public class PassengerScheduling implements ScheduleStrategy{
	
	@Override
	public void schedule(Member m, Calendar date) throws SQLException, ParseException{
		Scanner scanner = new Scanner(System.in);
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		System.out.println("Enter Date to schedule Ride [yyyy-MM-dd HH:mm:ss]:");
		String dateString = scanner.nextLine();
		date.setTime(sdf.parse(dateString));
		//System.out.println(cal.getTime().toGMTString());

		ArrayList<Ride> rides = DBController.getRidesByDate(date);
		
		System.out.println("Select ride to become a passenger of: ");
		for(int i = 0; i < rides.size(); i++) {
			System.out.println(String.format("["+(i+1)+"] %10s %10s %10s", rides.get(i).getDriverID(), rides.get(i).getStartTime(), rides.get(i).getStartLocation()));
		}
		String response = scanner.nextLine();
		int selection = Integer.parseInt(response);
		rides.get(selection-1).addPassenger(m);
		DBController.updateRide(rides.get(selection-1));
		scanner.close();
	}

}
