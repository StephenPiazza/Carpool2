
public class MemberSchedule {

	String[][] schedule;
	String[] days = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
	
	public MemberSchedule(){
		schedule = new String[7][2];
	}
	
	public MemberSchedule(String[][] schedule) {
		this.schedule = schedule;
	}
	
	public void setScheduleTime(int day, String to, String from) {
		schedule[day][0] = to;
		schedule[day][1] = from;
	}
	
	public String getScheduleTime(int day, int spot) {
		return schedule[day][spot];
	}
	
	public String printSchedule() {
		String print = "";
		for(int i = 0; i < 7; i++) {
			print += "["+(i+1)+"]"+ days[i] + " : To Campus:" + schedule[i][0] + "\tFrom Campus" + schedule[i][1] + "\n";
		}
		return print;
	}
}
