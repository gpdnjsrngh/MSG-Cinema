import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;

import oracle.jdbc.pool.OracleDataSource;

public class TicketingPanel extends JPanel {
	private Main mainFrame;

	private JLabel countLabel = new JLabel("Ƽ�� �ż��� �����ϼ���.");
	private JLabel scheduleLabel = new JLabel("ȸ���� �����ϼ���.");
	private JLabel adultLabel = new JLabel("�");
	private JLabel teenLabel = new JLabel("û�ҳ�");
	private JLabel childLabel = new JLabel("���");
	private JSpinner sp_adult;
	private JSpinner sp_teen;
	private JSpinner sp_child;
	// ������ ��ȣ, Ƽ�� ��

	private JButton button = new JButton("Ȯ��");

	private String[] count = { "0", "1", "2", "3" };
	private String schedule[] = new String[50];
	private JComboBox<String> combo;

	private String scheduleNum;
	public static List<Ticket> tickets = new ArrayList<Ticket>();
	public static Schedule selectedSchedule;
	public static Booking booking=new Booking();
	
	// �� ����, PayPanel���� ���� 
	public static int totalPrice;
		
	public TicketingPanel(Main mainFrame) {
		this.mainFrame = mainFrame;
		setLayout(null);

		// 5. �ܿ� �¼� ����
		// "Ƽ�� �ż��� �����ϼ���."
		countLabel.setLocation(30, 20);
		countLabel.setSize(150, 20);
		add(countLabel);

		// "�"
		adultLabel.setLocation(30, 50);
		adultLabel.setSize(100, 20);
		add(adultLabel);

		SpinnerListModel listModel1 = new SpinnerListModel(count);
		sp_adult = new JSpinner(listModel1);
		sp_adult.setLocation(100, 50);
		sp_adult.setSize(50, 30);
		add(sp_adult);

		// "û�ҳ�"
		teenLabel.setLocation(200, 50);
		teenLabel.setSize(100, 20);
		add(teenLabel);

		SpinnerListModel listModel2 = new SpinnerListModel(count);
		sp_teen = new JSpinner(listModel2);
		sp_teen.setLocation(270, 50);
		sp_teen.setSize(50, 30);
		add(sp_teen);

		// "���"
		childLabel.setLocation(370, 50);
		childLabel.setSize(100, 20);
		add(childLabel);

		SpinnerListModel listModel3 = new SpinnerListModel(count);
		sp_child = new JSpinner(listModel3);
		sp_child.setLocation(440, 50);
		sp_child.setSize(50, 30);
		add(sp_child);

		// "ȸ���� �����ϼ���."
		scheduleLabel.setLocation(30, 100);
		scheduleLabel.setSize(150, 20);
		add(scheduleLabel);

		button.setLocation(400, 200);
		button.setSize(100, 30);
		add(button);

		OracleDataSource ods = null;
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			ods = new OracleDataSource();
			ods.setURL("jdbc:oracle:thin:@localhost:1521:orcl");
			ods.setUser("hmsg");
			ods.setPassword("tiger");
			con = ods.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery("select scheduledate from schedule where movienum='" + MoviePanel.selectedMovie.getNum() + "'");
			int i = 0;
			while (rs.next()) {
				schedule[i] = rs.getString(1);
				i++;
			}

			for (int j = 0; j < 50; j++) {
				System.out.println(schedule[j]);
			}

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				if (con != null)
					con.close();
				if (stmt != null)
					stmt.close();
				if (rs != null)
					rs.close();
			} catch (Exception e) {
				System.out.println(e);
			}
		}

		// ȸ�� ���� �޺� �ڽ�
		combo = new JComboBox<String>(schedule);
		combo.setLocation(30, 130);
		combo.setSize(150, 30);
		add(combo);

		// [Ȯ��] Ŭ�� �̺�Ʈ
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				System.out.println(combo.getSelectedItem());
				int adultCount = Integer.parseInt(listModel1.getValue().toString());
				int teenCount = Integer.parseInt(listModel2.getValue().toString());
				int childCount = Integer.parseInt(listModel3.getValue().toString());

				int totalCount = adultCount + teenCount + childCount;
				if (totalCount > 3 || totalCount <= 0) {
					JOptionPane.showMessageDialog(null, "Ƽ���� �ѹ��� �ּ� 1����� �ִ� 3����� ������ �� �ֽ��ϴ�.");
				}

				else {
					// 1. schedule ���ڵ� ��ȸ
					OracleDataSource ods = null;
					Connection con = null;
					Statement stmt = null;
					ResultSet rs = null;
					try {
						ods = new OracleDataSource();
						ods.setURL("jdbc:oracle:thin:@localhost:1521:orcl");
						ods.setUser("hmsg");
						ods.setPassword("tiger");
						con = ods.getConnection();
						stmt = con.createStatement();
						rs = stmt.executeQuery("select schedulenum from schedule where scheduledate='"
								+ combo.getSelectedItem() + "' and movienum='" + MoviePanel.selectedMovie.getNum() + "'");

						while (rs.next()) {
							scheduleNum = rs.getString(1);
							System.out.println(scheduleNum);
						}

						rs = stmt.executeQuery("select * from schedule where schedulenum='"+scheduleNum+"'");
						while(rs.next()) {
							selectedSchedule = new Schedule(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
						}
					} catch (Exception e) {
						System.out.println(e);
					} finally {
						try {
							if (con != null)
								con.close();
							if (stmt != null)
								stmt.close();
							if (rs != null)
								rs.close();
							
						} catch (Exception e) {
							System.out.println(e);
						}
					}

					// 2. Ƽ�� ��ü ����
								

					switch (MoviePanel.selectedMovie.getNum()) {
					case "111":
						totalPrice = adultCount * 28000 + teenCount * 25000 + childCount * 20000;
						
						for (int i = 0; i < adultCount; i++) {
							Ticket ticket = new Ticket(null, scheduleNum, "15000", null);
							tickets.add(ticket);
						}
						for (int i = 0; i < teenCount; i++) {
							Ticket ticket = new Ticket(null, scheduleNum, "12000", null);
							tickets.add(ticket);
						}
						for (int i = 0; i < childCount; i++) {
							Ticket ticket = new Ticket(null, scheduleNum, "7000", null);
							tickets.add(ticket);
						}
						break;
					case "222":
						totalPrice = adultCount * 22000 + teenCount * 19000 + childCount * 11000;
						for (int i = 0; i < adultCount; i++) {
							Ticket ticket = new Ticket(null, scheduleNum, "22000", null);
							tickets.add(ticket);
						}
						for (int i = 0; i < teenCount; i++) {
							Ticket ticket = new Ticket(null, scheduleNum, "19000", null);
							tickets.add(ticket);
						}
						for (int i = 0; i < childCount; i++) {
							Ticket ticket = new Ticket(null, scheduleNum, "11000", null);
							tickets.add(ticket);
						}
						break;
					case "333":
						totalPrice = adultCount * 15000 + teenCount * 12000 + childCount * 7000;
						for (int i = 0; i < adultCount; i++) {
							Ticket ticket = new Ticket(null, scheduleNum, "28000", null);
							tickets.add(ticket);
						}
						for (int i = 0; i < teenCount; i++) {
							Ticket ticket = new Ticket(null, scheduleNum, "25000", null);
							tickets.add(ticket);
						}
						for (int i = 0; i < childCount; i++) {
							Ticket ticket = new Ticket(null, scheduleNum, "20000", null);
							tickets.add(ticket);
						}
						break;
					}

					// 3. Ƽ�� ���ڵ� ����
					PreparedStatement pstmt = null;

					try {
						for (Ticket ticket : tickets) {
							con = ods.getConnection();
							//���Ⱑ ����
							stmt = con.createStatement();
														
							pstmt = con.prepareStatement("insert into ticket(ticketnum, schedulenum, ticketprice)"
											+ " values(ticket_seq.nextval,?,?)");

							pstmt.setString(1, ticket.getScheduleNum());
							pstmt.setString(2, ticket.getTicketPrice());
							
							pstmt.executeUpdate(); // ������Ʈ�� ���ڵ� ��(int) ����
							
							rs = stmt.executeQuery("SELECT LAST_NUMBER FROM USER_SEQUENCES WHERE SEQUENCE_NAME = 'TICKET_SEQ'");

							while (rs.next()) {
								int n = rs.getInt(1)-1;
								ticket.setTicketNum(n+"");
								System.out.println("b "+ticket.getTicketNum());
							}
							
						}

					} catch (Exception e) {
						System.out.println(e);
					} finally {
						try {
							if (con != null)
								con.close();
							if (pstmt != null)
								pstmt.close();
							if (stmt != null)
								stmt.close();
							if (rs != null)
								rs.close();
						} catch (Exception e) {
							System.out.println(e);
						}
					}
					
					for(Ticket ticket:tickets)
						System.out.println("a "+ticket.getTicketNum());
								
					System.out.println("total: "+totalCount);
					// 4. booking ���ڵ� ����
					try {

						con = ods.getConnection();
						stmt = con.createStatement();
						if(totalCount==1) {
							pstmt = con.prepareStatement("insert into booking(bookingnum, ticketnum1)"
									+ " values(booking_seq.nextval,?)");
							pstmt.setString(1, tickets.get(0).getTicketNum());
							pstmt.executeUpdate(); // ������Ʈ�� ���ڵ� ��(int) ����
							
							rs = stmt.executeQuery("SELECT LAST_NUMBER FROM USER_SEQUENCES WHERE SEQUENCE_NAME = 'BOOKING_SEQ'");

							while (rs.next()) {
								int n = rs.getInt(1)-1;
								booking.setNum(n+"");
								booking.setTicketNum1(tickets.get(0).getTicketNum());
							}
							
						}
						
						else if(totalCount==2) {
							pstmt = con.prepareStatement("insert into booking(bookingnum, ticketnum1, ticketnum2)"
									+ " values(booking_seq.nextval,?,?)");
							pstmt.setString(1, tickets.get(0).getTicketNum());
							pstmt.setString(2, tickets.get(1).getTicketNum());	
							pstmt.executeUpdate(); // ������Ʈ�� ���ڵ� ��(int) ����
							
							rs = stmt.executeQuery("SELECT LAST_NUMBER FROM USER_SEQUENCES WHERE SEQUENCE_NAME = 'BOOKING_SEQ'");

							while (rs.next()) {
								int n = rs.getInt(1)-1;
								booking.setNum(n+"");
								booking.setTicketNum1(tickets.get(0).getTicketNum());
								booking.setTicketNum2(tickets.get(1).getTicketNum());
							}
						}
						
						else if(totalCount==3) {
							pstmt = con.prepareStatement("insert into booking(bookingnum, ticketnum1, ticketnum2, ticketnum3)"
									+ " values(booking_seq.nextval,?,?,?)");
							pstmt.setString(1, tickets.get(0).getTicketNum());	
							pstmt.setString(2, tickets.get(1).getTicketNum());	
							pstmt.setString(3, tickets.get(2).getTicketNum());	
							pstmt.executeUpdate(); // ������Ʈ�� ���ڵ� ��(int) ����
							
							rs = stmt.executeQuery("SELECT LAST_NUMBER FROM USER_SEQUENCES WHERE SEQUENCE_NAME = 'BOOKING_SEQ'");

							while (rs.next()) {
								int n = rs.getInt(1)-1;
								booking.setNum(n+"");
								System.out.println(n+"");
								booking.setTicketNum1(tickets.get(0).getTicketNum());
								booking.setTicketNum2(tickets.get(1).getTicketNum());
								booking.setTicketNum3(tickets.get(2).getTicketNum());
							}
						}
											
						

					} catch (Exception e) {
						System.out.println(e);
					} finally {
						try {
							if (con != null)
								con.close();
							if (pstmt != null)
								pstmt.close();
							if (stmt != null)
								stmt.close();
							if (rs != null)
								rs.close();
						} catch (Exception e) {
							System.out.println(e);
						}
					}
					mainFrame.change("seat");
				}

			}

		});

	}

}
