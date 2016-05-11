

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.mysql.jdbc.PreparedStatement;


public class Points implements PaymentMethods {
	int value;
	String driverID;
	String riderID;

	
	public Points(String driverID, String riderID, int value) {
		super();
		this.value = value;
		this.driverID = driverID;
		this.riderID = riderID;
	}
	
	public void processPayment() {
		Connection connection = null;
        Statement stmt = null;
        Statement stmt2 = null;
        Statement stmt3 = null;
        Statement stmt4 = null;
        try
        {
          Class.forName("com.mysql.jdbc.Driver");
          connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/carpool", "root", "password");  
          stmt = connection.createStatement();
          stmt2 = connection.createStatement();
          stmt3 = connection.createStatement();
          stmt4 = connection.createStatement();
          ResultSet driver = stmt.executeQuery("SELECT point from member_balance where memberID = '" + driverID + "'");
          ResultSet rider = stmt3.executeQuery("SELECT point from member_balance where memberID = '" + riderID + "'");
        
          
          while(rider.next()){
          	System.out.println("Current points value in Rider's account is: "+rider.getInt(1));
          		
         	   int riderNewValue= rider.getInt(1)-value;
         	   stmt4.execute("UPDATE member_balance SET point = '" + riderNewValue + "' where memberID = '" + riderID + "'");
         	   System.out.println("New points balance in Rider's account is:"+ riderNewValue);  
         	   stmt4.close();
            }
            
          
          
           while(driver.next()){
        	   System.out.println("Current points value in Driver's account is: "+driver.getInt(1));
        	   int driverNewValue= driver.getInt(1)+value;
        	   stmt2.execute("UPDATE member_balance SET point = '" + driverNewValue + "' where memberID = '" + driverID + "'");
        	   System.out.println("Current points balance in Driver's account is:"+ driverNewValue);  
        	   stmt2.close(); 
           }
         
          
        System.out.println("Payment successful");
        }
        catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                stmt.close();
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

	}

}
