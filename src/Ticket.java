
public class Ticket {
	private String ticketNum;
	private String scheduleNum;
	private String ticketPrice;
	private String seatNum;
	
	public Ticket(String ticketNum, String scheduleNum, String ticketPrice, String seatNum) {
		this.ticketNum = ticketNum;
		this.scheduleNum = scheduleNum;
		this.ticketPrice = ticketPrice;
		this.seatNum = seatNum;
	}
	
	public String getTicketNum() {
		return ticketNum;
	}
	public String getScheduleNum() {
		return scheduleNum;
	}
	public String getTicketPrice() {
		return ticketPrice;
	}
	public String getSeatNum() {
		return seatNum;
	}
	
	public void setTicketNum(String ticketNum) {
		this.ticketNum = ticketNum;
	}
	
	public void setSeatNum(String seatNum) {
		this.seatNum = seatNum;
	}
	
}
