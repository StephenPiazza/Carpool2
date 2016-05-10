import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;

public interface ScheduleStrategy {
	
	public void schedule(Member m, Calendar date) throws SQLException, ParseException;

}
