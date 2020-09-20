
public class Booking {
	private String num;
	private String ticketNum1;
	private String ticketNum2;
	private String ticketNum3;
	
	
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getTicketNum1() {
		return ticketNum1;
	}
	public void setTicketNum1(String ticketNum1) {
		this.ticketNum1 = ticketNum1;
		
	}
	public String getTicketNum2() {
		return ticketNum2;
	}
	public void setTicketNum2(String ticketNum2) {
		this.ticketNum2 = ticketNum2;
		
	}
	public String getTicketNum3() {
		return ticketNum3;
	}
	public void setTicketNum3(String ticketNum3) {
		this.ticketNum3 = ticketNum3;
		
	}
	
	public int getTicketCount() {
		if(getTicketNum3()!=null)
			return 3;
		if(getTicketNum2()!=null)
			return 2;
		if(getTicketNum1()!=null)
			return 1;
		return 0;
	}
	
	
}
