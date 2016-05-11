import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

public class DriverScheduling implements ScheduleStrategy {
	
	@Override
	public void schedule(Member m, Calendar date) throws SQLException, ParseException {
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		Boolean exit = false;
		String[] days = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
		Ride r;
		
		while (!exit){
			System.out.println("What would you like to do?");
			System.out.println("[1] Create rides based on Week Schedule");
			System.out.println("[2] Create one-time ride");
			System.out.println("[3] Add passenger to your ride");
			System.out.println("[4] Exit");
			String response = scanner.nextLine();
			int selection = Integer.parseInt(response);
			
			switch(selection) {
			
			case 1:
				MemberSchedule ms = m.getSchedule();
				date = Calendar.getInstance();
				
				for (int i = date.get(Calendar.DAY_OF_WEEK)-1; i < 7; i++ ) {
					
					if (ms.getScheduleTime(i, 0) != null) {
						System.out.println(Integer.parseInt(ms.getScheduleTime(i, 0).substring(0,2)));
						date.set(Calendar.HOUR_OF_DAY, Integer.parseInt(ms.getScheduleTime(i, 0).substring(0,2)));
						date.set(Calendar.MINUTE, Integer.parseInt(ms.getScheduleTime(i,  0).substring(3,5)));
						date.set(Calendar.SECOND, 0);
						r = new Ride(-1, date, m.getAddress(), m.getMemberID(), null, null);
						DBController.createRide(r);
					}
					
					if (ms.getScheduleTime(i, 1) != null) {
						date.set(Calendar.HOUR_OF_DAY, Integer.parseInt(ms.getScheduleTime(i, 1).substring(0,2)));
						date.set(Calendar.MINUTE, Integer.parseInt(ms.getScheduleTime(i,  1).substring(3,5)));
						date.set(Calendar.SECOND, 0);
						r = new Ride(-1, date, "SJSU University", m.getMemberID(), null, null);
						DBController.createRide(r);
					}
					
					date.add(Calendar.DATE, 1);
				}
				
				break;
			
			case 2:
				System.out.println("Ride Creation");

				System.out.println("Enter date and time for ride [yyyy-MM-dd HH:mm:ss] (15 minute intervals)");
				String newRideDate = scanner.nextLine();
				System.out.println("Enter Location ride starts From:");
				String startLocation = scanner.nextLine();
				
				DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				date.setTime(sdf.parse(newRideDate));
				r = new Ride(-1, date, startLocation, m.getMemberID(), null, null);
				DBController.createRide(r);
				break;
			
			case 3:
				ArrayList<Ride> rides = DBController.getRidesByMember(m);
				
				System.out.println("List of your scheduled rides.");
				System.out.println("Select a ride to add Passengers:");
				for (int i = 0; i < rides.size(); i++) {
					System.out.println(String.format("[" + (i+1)+"] Start Time : %15s Start Location: %15s",  rides.get(i).getStartTime(), rides.get(i).getStartLocation()));
				}
				response = scanner.nextLine();
				selection = Integer.parseInt(response);
				
				ArrayList<Member> passengers = DBController.getPassengersByDate(rides.get(selection-1).getDate());
				System.out.println("Select passenger to add: ");
				if (passengers.size() > 0) {
					for(int i = 0; i < passengers.size(); i++) {
						System.out.println(String.format("["+(i+1)+"] MemberID: %10s  Name: %s %s", passengers.get(i).getMemberID(), passengers.get(i).getFirstName(), passengers.get(i).getLastName()));
					}
					response = scanner.nextLine();
					int selection2 = Integer.parseInt(response);
					rides.get(selection-1).addPassenger(passengers.get(selection2-1));
					DBController.updateRide(rides.get(selection-1));
				}
				else {
					System.out.println("No Available Passengers");
				}
				break;
			case 4:
				exit = true;
			default:
				break;
			}
		}
	}
}
