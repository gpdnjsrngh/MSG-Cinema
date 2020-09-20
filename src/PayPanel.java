import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import oracle.jdbc.pool.OracleDataSource;

public class PayPanel extends JPanel {

	private Main mainFrame;

	private JButton b_ok = new JButton("Ȯ��");
	private JButton b_cancel = new JButton("���");

	private JTextArea ta_ticketing = new JTextArea();
	private String[] payHow = { "���� ���", "����", "ī��", "������" };
	private JComboBox<String> combo = new JComboBox<String>(payHow);

	public PayPanel(Main mainFrame) {
		this.mainFrame = mainFrame;
		setLayout(null);
		
		//[���� ���� â]
		ta_ticketing.setLocation(150, 100);
		ta_ticketing.setSize(300, 300);

		String movieTitle = null;
		switch (MoviePanel.selectedMovie.getTitle()) {
		case "Parasite":
			movieTitle = "[�����]";
			break;
		case "Aladdin":
			movieTitle = "[�˶��]";
			break;
		case "SpiderMan":
			movieTitle = "[�����̴���]";
			break;
		}
		ta_ticketing.setText("������ ��ȭ�� " + movieTitle + " �Դϴ�.\n");
		ta_ticketing.append("Ƽ�� ��: " + TicketingPanel.tickets.size());
		ta_ticketing.append("\n�� ���ž��� " + TicketingPanel.totalPrice + "���Դϴ�.");
		
		JScrollPane scrollPane = new JScrollPane(ta_ticketing);
		scrollPane.setLocation(100, 50);
		scrollPane.setSize(250, 200);
		add(scrollPane);

		//[���� ���] �޺� �ڽ�
		combo.setLocation(450, 50);
		combo.setSize(100, 30);
		add(combo);

		//[Ȯ��]
		b_ok.setLocation(250, 300);
		b_ok.setSize(100, 30);
		add(b_ok);

		//[���]
		b_cancel.setLocation(400, 300);
		b_cancel.setSize(100, 30);
		add(b_cancel);

		// [Ȯ��] Ŭ�� �̺�Ʈ
		b_ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				String selectedPayHow = combo.getSelectedItem().toString();

				// 1. ticket�� seatNum update - db
				OracleDataSource ods = null;
				Connection con = null;
				PreparedStatement pstmt = null;

				try {
					ods = new OracleDataSource();
					ods.setURL("jdbc:oracle:thin:@localhost:1521:orcl");
					ods.setUser("hmsg");
					ods.setPassword("tiger");
					con = ods.getConnection();

					pstmt = con.prepareStatement("update member set stampcount=? where membernum=?");
					int ticketCount = TicketingPanel.tickets.size();
					// 4. ������ ����
					if (selectedPayHow.equals("������")) {
						
						// ������ ���� ���
						int i_stampCount = LoginPanel.member.getStampCount();
						int freeMovieCount = i_stampCount / 5;
						
						
						if(freeMovieCount>=ticketCount) {
							pstmt.setInt(1, i_stampCount-ticketCount*5);
							LoginPanel.member.setStampCount(i_stampCount-ticketCount*5);
							pstmt.setString(2, LoginPanel.member.getNum());
							pstmt.executeUpdate();
						}
						else {
							JOptionPane.showMessageDialog(null, "�������� �����մϴ�. �ٸ� ���� ����� �̿��ϼ���.");
							return;
						}
						
					}

					// 5. �߸��� �Է°�
					else if(selectedPayHow.equals("���� ���")) {
						JOptionPane.showMessageDialog(null, "���� ����� �����ϼ���");
						return;
					}
					
					// 6. ������ ����
					int i_stampCount = LoginPanel.member.getStampCount();
					pstmt.setInt(1, i_stampCount+ticketCount);
					LoginPanel.member.setStampCount(i_stampCount+ticketCount);
					pstmt.setString(2, LoginPanel.member.getNum());

					pstmt.executeUpdate(); // ������Ʈ�� ���ڵ� ��(int) ����
								
					for (Ticket ticket : TicketingPanel.tickets) {
						pstmt = con.prepareStatement("update ticket set seatnum=? where ticketnum=?");

						ticket.setSeatNum(SeatPanel.selectedSeatNumList.get(TicketingPanel.tickets.indexOf(ticket)));
						pstmt.setString(1, ticket.getSeatNum());
						pstmt.setString(2, ticket.getTicketNum());

						pstmt.executeUpdate(); // ������Ʈ�� ���ڵ� ��(int) ����

						// 2. seat�� seatcheck update - db
						pstmt = con.prepareStatement("update seat set seatcheck=? where schedulenum=? and seatnum=?");

						pstmt.setInt(1, 1);
						pstmt.setString(2, TicketingPanel.selectedSchedule.getNum());
						pstmt.setString(3, ticket.getSeatNum());
						pstmt.executeUpdate();

					}

					// 3. pay ���ڵ� ����
					pstmt = con.prepareStatement(
							"insert into pay(paynum, payoption, paydate, bookingnum, totalprice, membernum)"
									+ " values(pay_seq.nextval,?,sysdate,?,?,?)");

					pstmt.setString(1, selectedPayHow);
					pstmt.setString(2, TicketingPanel.booking.getNum());
					pstmt.setInt(3, TicketingPanel.totalPrice);
					pstmt.setString(4, LoginPanel.member.getNum());
					pstmt.executeUpdate(); // ������Ʈ�� ���ڵ� ��(int) ����

					

				} catch (Exception e) {
					System.out.println(e);
				} finally {
					try {
						if (con != null)
							con.close();
						if (pstmt != null)
							pstmt.close();
					} catch (Exception e) {
						System.out.println(e);
					}
				}

				TicketingPanel.tickets.clear();
				JOptionPane.showMessageDialog(null, "������ �Ϸ�Ǿ����ϴ�.");
				mainFrame.change("movie");
			}
		});

		// [���] Ŭ�� �̺�Ʈ
		b_cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				JOptionPane.showMessageDialog(null, "������ ��ҵǾ����ϴ�.");
				// ticket, booking ���ڵ� drop,

				OracleDataSource ods = null;
				Connection con = null;
				PreparedStatement pstmt = null;

				try {
					ods = new OracleDataSource();
					ods.setURL("jdbc:oracle:thin:@localhost:1521:orcl");
					ods.setUser("hmsg");
					ods.setPassword("tiger");
					con = ods.getConnection();
					// 1. booking ����
					pstmt = con.prepareStatement("delete from booking where bookingnum=?");

					pstmt.setString(1, TicketingPanel.booking.getNum());
					pstmt.executeUpdate();
					// 2. ticket ����
					for (Ticket ticket : TicketingPanel.tickets) {
						pstmt = con.prepareStatement("delete from ticket where ticketnum=?");

						pstmt.setString(1, ticket.getTicketNum());
						pstmt.executeUpdate();
					}
				} catch (Exception e) {
					System.out.println(e);
				} finally {
					try {
						if (con != null)
							con.close();
						if (pstmt != null)
							pstmt.close();
					} catch (Exception e) {
						System.out.println(e);
					}
				}
				mainFrame.change("movie");
				TicketingPanel.tickets.clear();
			}
		});
	}

}
