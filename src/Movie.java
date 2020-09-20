

public class Movie {
	private String num;
	private String level;
	private String director;
	private String genre;
	private String title;
	
	public Movie(String num, String level, String director, String genre, String title) {
		this.num = num;
		this.level = level;
		this.director = director;
		this.genre = genre;
		this.title = title;
	}
	
	public String getNum() {
		return num;
	}
	
	public String getLevel() {
		return level;
	}
	
	public String getDirector() {
		return director;
	}
	
	public String getGenre() {
		return genre;
	}
	
	public String getTitle() {
		return title;
	}
		
}
