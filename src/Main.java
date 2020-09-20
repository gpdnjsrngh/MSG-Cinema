import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.swing.BorderFactory;
import javax.swing.JFrame;

import oracle.jdbc.pool.OracleDataSource;

public class Main extends JFrame {
	static Main mainFrame = new Main();

	public LoginPanel loginPanel = null;
	public RegisterPanel registerPanel = null;
	public MoviePanel moviePanel = null;
	public TicketingPanel ticketingPanel = null;
	public SeatPanel seatPanel = null;
	public PayPanel payPanel = null;
	public MyPagePanel myPagePanel = null;

	// 패널 교체
	public void change(String panelName) {

		if (panelName.equals("login")) {
			getContentPane().removeAll();
			getContentPane().add(loginPanel);
			revalidate();
			repaint();
		} else if (panelName.equals("register")) {
			getContentPane().removeAll();
			getContentPane().add(registerPanel);
			revalidate();
			repaint();
		} else if (panelName.equals("movie")) {
			mainFrame.moviePanel = new MoviePanel(mainFrame);
			getContentPane().removeAll();
			getContentPane().add(moviePanel);
			revalidate();
			repaint();
		} else if (panelName.equals("ticketing")) {
			mainFrame.ticketingPanel = new TicketingPanel(mainFrame);
			getContentPane().removeAll();
			getContentPane().add(ticketingPanel);
			revalidate();
			repaint();

		} else if (panelName.equals("seat")) {
			mainFrame.seatPanel = new SeatPanel(mainFrame);
			getContentPane().removeAll();
			getContentPane().add(seatPanel);
			revalidate();
			repaint();
		} else if (panelName.equals("pay")) {
			mainFrame.payPanel = new PayPanel(mainFrame);
			getContentPane().removeAll();
			getContentPane().add(payPanel);
			revalidate();
			repaint();
		} else if (panelName.equals("my")) {
			mainFrame.myPagePanel = new MyPagePanel(mainFrame);
			getContentPane().removeAll();
			getContentPane().add(myPagePanel);
			revalidate();
			repaint();
		}

	}

	public static void main(String[] args) {
		mainFrame.getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.DARK_GRAY));
		/*
		 * OracleDataSource ods = null;
		 * Connection con = null;
		 * Statement stmt = null;
		 * ResultSet rs = null;
		 * try {
		 * ods = new OracleDataSource();
		 * ods.setURL("jdbc:oracle:thin:@localhost:1521:orcl");
		 * ods.setUser("hansung");
		 * ods.setPassword("tiger");
		 * con = ods.getConnection();
		 * stmt = con.createStatement(); rs = stmt.executeQuery("select * from student");
		 * System.out.println("학번\t주민등록번호\t이름"); while(rs.next()) { String s_id =
		 * rs.getString(1); String r_id = rs.getString(2); String name =
		 * rs.getString(3); System.out.println(s_id+"\t"+r_id+"\t"+name);; } }
		 * catch(Exception e) { System.out.println(e); } finally { try { if(con!=null)
		 * con.close(); if(stmt!=null) stmt.close(); if(rs!=null) rs.close(); }
		 * catch(Exception e) { System.out.println(e); } }
		 */

		mainFrame.loginPanel = new LoginPanel(mainFrame);
		mainFrame.registerPanel = new RegisterPanel(mainFrame);

		mainFrame.add(mainFrame.loginPanel);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setTitle("HMSG 시네마");
		mainFrame.setSize(800, 500);
		mainFrame.setVisible(true);
		
	}
}
