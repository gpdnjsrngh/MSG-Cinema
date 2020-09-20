
public class Schedule {
	private String num;
	private String date;
	private String theaterNum;
	private String movieNum;
	
	public Schedule(String num, String date, String theaterNum, String movieNum) {
		this.num = num;
		this.date = date;
		this.theaterNum = theaterNum;
		this.movieNum = movieNum;
	}
	
	public String getNum() {
		return num;
	}
	public String getDate() {
		return date;
	}
	public String getTheaterNum() {
		return theaterNum;
	}
	public String getMovieNum() {
		return movieNum;
	}
	
	
}
