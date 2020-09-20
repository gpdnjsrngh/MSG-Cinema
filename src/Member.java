

public class Member {
	private String num;
	private String name;
	private String phoneNum;
	private int stampCount;
	
	public Member(String num, String name, String phoneNum, int stampCount) {
		this.num = num;
		this.name = name;
		this.phoneNum = phoneNum;
		this.stampCount = stampCount;
	}
	
	public String getNum() {
		return num;
	}
	public String getName() {
		return name;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public int getStampCount() {
		return stampCount;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public void setStampCount(int stampCount) {
		this.stampCount = stampCount;
	}
	
}
