/*<<<<<<< Updated upstream
=======
<<<<<<< HEAD


import java.util.Scanner;

public class Test {

	public static void main(String[] args) {

		boolean exit = false;
		int response = 0;
		while (!exit) {
			System.out.println("Welcome to Reward Screen");
			Scanner in = new Scanner(System.in);
			System.out.println("[1] Points");
			System.out.println("[2] CreditCard");
			System.out.println("[3] Cash");
			System.out.println("[4] Exit");
			response = in.nextInt();
			switch (response) {
			case 1:
				PayWithPoints();
				break;
			case 2:
				PayWithCreditCard();
				break;
			case 3: 
				PayWithCash();
			case 4:
				exit = true;
				break;
			default:
				break;
			}
		}
		
		}
		
		public static void PayWithPoints() {
			System.out.println("Enter the amount of points to be deducted: ");			
			Scanner in = new Scanner(System.in);
			int pointsUsed = in.nextInt();
			Payment p = new RidePayment(new Points("a123","b123",pointsUsed));
			p.pay();
		}
		
		public static void PayWithCreditCard(){
			System.out.println("Enter the amount to be charged on credit card: ");
			Scanner in = new Scanner(System.in);
			int creditCharged = in.nextInt();
			Payment p = new RidePayment(new CreditCard("x123","y123",creditCharged));//y is driver
			p.pay();
		}
		
		
		public static void PayWithCash(){
			System.out.println("Enter the amount you will pay in cash: ");
			Scanner in = new Scanner(System.in);
			int cashCharged = in.nextInt();
			Payment p = new RidePayment(new Cash("x123","y123",cashCharged));
			p.pay();
		
		}
		
}





	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
=======
>>>>>>> Stashed changes
import java.sql.SQLException;
import java.text.ParseException;
import java.util.GregorianCalendar;

public class Test {
	public static void main(String[] args) throws SQLException, ParseException {
		RideScheduler rs = new RideScheduler(new GregorianCalendar(2016, 5,9));
		
		Member m = DBController.getMemberInfo("Piazza3490");
		rs.schedule(m);
	}
}
<<<<<<< Updated upstream
=======
>>>>>>> origin/master
>>>>>>> Stashed changes
*/