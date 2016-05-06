
public class MemberSchedule {

	int[][] schedule;
	
	public MemberSchedule(){
		schedule = new int[7][2];
	}
	
	public MemberSchedule(int[][] schedule) {
		this.schedule = schedule;
	}
	
	public void setScheduleTime(int day, int to, int from) {
		schedule[day][0] = to;
		schedule[day][1] = from;
	}
}
