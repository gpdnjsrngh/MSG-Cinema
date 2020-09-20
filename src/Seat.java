
public class Seat {
	private String seatNum;
	private String scheduleNum;
	private String seatCheck;
		
	public Seat(String seatNum, String seatCheck) {
		this.seatNum = seatNum;
		this.seatCheck = seatCheck;
	}
	
	public String getSeatNum() {
		return seatNum;
	}

	public String getSeatCheck() {
		return seatCheck;
	}
	
	public void setSeatCheck(String seatCheck) {
		this.seatCheck = seatCheck;
	}
	
	
}
