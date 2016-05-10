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
