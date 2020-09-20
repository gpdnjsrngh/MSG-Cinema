import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import oracle.jdbc.pool.OracleDataSource;

public class MyPagePanel extends JPanel {

	private Main mainFrame;

	private JLabel titleLabel = new JLabel("����������");
	private JButton b_back = new JButton("��������");
	private JLabel l_name = new JLabel("�̸�");
	private JLabel l_phone = new JLabel("�޴���ȭ��ȣ");
	private JTextField tf_name = new JTextField(10);
	private JTextField tf_phone = new JTextField(20);
	private JButton b_correct = new JButton("����");

	private JLabel l_stamp = new JLabel("������");
	private JLabel l_stampCount = new JLabel();

	private JLabel l_history = new JLabel("���� ����");
	private List<String> msgList = new ArrayList<String>();

	private List<JCheckBox> historyList = new ArrayList<>();
	private JButton b_cancel = new JButton("������ ���� ���");

	private List<Booking> bookingHistory = new ArrayList<Booking>();
	private List<String> payNumHistory = new ArrayList<String>();
	private List<String> cancelCheckList = new ArrayList<String>();

	public MyPagePanel(Main mainFrame) {
		this.mainFrame = mainFrame;
		setLayout(null);

		// "����������"
		Font f1 = new Font("���� ���", Font.BOLD, 25);
		titleLabel.setFont(f1);
		titleLabel.setLocation(320, 10);
		titleLabel.setSize(500, 100);
		add(titleLabel);

		// [��������]
		b_back.setLocation(500, 50);
		b_back.setSize(100, 30);
		add(b_back);

		// "�̸�"
		l_name.setLocation(250, 70);
		l_name.setSize(200, 100);
		add(l_name);

		tf_name.setLocation(350, 105);
		tf_name.setSize(100, 25);
		tf_name.setText(LoginPanel.member.getName());
		add(tf_name);

		// "�޴���ȭ��ȣ"
		l_phone.setLocation(200, 105);
		l_phone.setSize(200, 100);
		add(l_phone);

		tf_phone.setLocation(350, 140);
		tf_phone.setSize(100, 25);
		tf_phone.setText(LoginPanel.member.getPhoneNum());
		add(tf_phone);

		// [����]
		b_correct.setLocation(500, 130);
		b_correct.setSize(80, 30);
		add(b_correct);

		// "������"
		l_stamp.setLocation(230, 140);
		l_stamp.setSize(200, 100);
		add(l_stamp);

		// ������ ���� ���
		int i_stampCount = LoginPanel.member.getStampCount();
		int freeMovieCount = i_stampCount / 5;

		// "0�� (��ȭ 0�� ����)"
		l_stampCount.setText(i_stampCount + "��   (��ȭ " + freeMovieCount + "�� ����)");
		l_stampCount.setLocation(350, 140);
		l_stampCount.setSize(200, 100);
		add(l_stampCount);

		// "���ų���"
		l_history.setLocation(220, 170);
		l_history.setSize(200, 100);
		add(l_history);

		JPanel historyPanel = new JPanel();
		historyPanel.setPreferredSize(new Dimension(200, 500));

		// 1. ���ų��� ��ȸ
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

			rs = stmt.executeQuery("select bookingnum, paynum from pay where membernum ='" + LoginPanel.member.getNum()
					+ "' order by paynum");

			while (rs.next()) {
				Booking booking = new Booking();
				booking.setNum(rs.getString(1));
				bookingHistory.add(booking);
				payNumHistory.add(rs.getString(2));

			}

			for (Booking b : bookingHistory) {
				rs = stmt.executeQuery("select ticketnum1 from booking where bookingnum ='" + b.getNum() + "'");

				while (rs.next()) {
					b.setTicketNum1(rs.getString(1));
					System.out.println("��ŷ��" + rs.getString(1));
				}

				rs = stmt.executeQuery("select ticketnum2 from booking where bookingnum ='" + b.getNum() + "'");

				while (rs.next()) {
					b.setTicketNum2(rs.getString(1));

				}

				rs = stmt.executeQuery("select ticketnum3 from booking where bookingnum ='" + b.getNum() + "'");

				while (rs.next()) {
					b.setTicketNum3(rs.getString(1));

				}
			}

			for (Booking b : bookingHistory) {
				rs = stmt.executeQuery("select schedulenum from ticket where ticketnum ='" + b.getTicketNum1() + "'");

				String scheduleNum = null;
				while (rs.next()) {
					scheduleNum = rs.getString(1);
				}

				rs = stmt.executeQuery("select movienum, scheduledate from schedule where schedulenum='" + scheduleNum + "'");

				String movieNum = null;
				String scheduleDate = null;
				while (rs.next()) {
					movieNum = rs.getString(1);
					scheduleDate = rs.getString(2);
				}
				String msg = null;
				System.out.println(movieNum);
				switch (movieNum) {
				case "111":
					msg = "�����/1�� Gold Class/" + scheduleDate + "/";
					break;
				case "222":
					msg = "�˶��/3�� 4DX/" + scheduleDate + "/";
					break;
				case "333":
					msg = "�����̴���/2�� 2D/" + scheduleDate + "/";
					break;
				}

				msg += b.getTicketCount() + "�� ����";
				System.out.println(msg);
				msgList.add(msg);

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

		// ��ȭ��, �� �̸�, �Ͻ�(������ �ð�), 0�� ����
		for (String m : msgList) {
			JCheckBox cb_history = new JCheckBox(m);
			cb_history.setName(msgList.indexOf(m) + "");
			historyList.add(cb_history);
			historyPanel.add(cb_history);
		}

		JScrollPane scroll = new JScrollPane(historyPanel);
		historyPanel.setAutoscrolls(true);
		scroll.setLocation(220, 230);
		scroll.setSize(400, 150);
		add(scroll);

		// [������ ���� ���]
		b_cancel.setLocation(300, 390);
		b_cancel.setSize(150, 30);
		add(b_cancel);

		// 2. ���ų��� ����
		b_cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				for (JCheckBox c : historyList) {
					if (c.isSelected()) {
						String cbNum = c.getName();
						String cancelPayNum = payNumHistory.get(Integer.parseInt(cbNum));

						OracleDataSource ods = null;
						Connection con = null;
						PreparedStatement pstmt = null;
						Statement stmt = null;
						ResultSet rs = null;

						try {
							
							ods = new OracleDataSource();
							ods.setURL("jdbc:oracle:thin:@localhost:1521:orcl");
							ods.setUser("hmsg");
							ods.setPassword("tiger");
							con = ods.getConnection();

							// 2. ������ ����
							int i_stampCount = LoginPanel.member.getStampCount();
							int freeMovieCount;
							
							stmt = con.createStatement();
							System.out.println(cancelPayNum);
							// 3. seat update + null ������ ����
							rs = stmt.executeQuery("select bookingnum from pay where paynum ='" + cancelPayNum + "'");
							System.out.println("����1");
							Booking booking = new Booking();
							
							while (rs.next()) {
								System.out.println("����2");
								System.out.println("��ŷ��" + rs.getString(1));
								booking.setNum(rs.getString(1));
								// bookingHistory.add(booking);
								// payNumHistory.add(rs.getString(2));

							}

							rs = stmt.executeQuery(
									"select ticketnum1 from booking where bookingnum ='" + booking.getNum() + "'");

							while (rs.next()) {
								booking.setTicketNum1(rs.getString(1));
								
							}

							rs = stmt.executeQuery(
									"select ticketnum2 from booking where bookingnum ='" + booking.getNum() + "'");
							
							while (rs.next()) {
								if(rs.getString(1)!=null)
									booking.setTicketNum2(rs.getString(1));

							}

							rs = stmt.executeQuery(
									"select ticketnum3 from booking where bookingnum ='" + booking.getNum() + "'");

							while (rs.next()) {
								if(rs.getString(1)!=null)
									booking.setTicketNum3(rs.getString(1));

							}

							rs = stmt.executeQuery("select schedulenum, seatnum from ticket where ticketnum ='"
									+ booking.getTicketNum1() + "'");
							System.out.println("Ƽ�ϳ�"+booking.getTicketNum1());
							String scheduleNum = null;
							String seatNum = null;
							while (rs.next()) {
								scheduleNum = rs.getString(1);
								seatNum = rs.getString(2);
								System.out.println(scheduleNum+",,,"+seatNum);
								pstmt = con.prepareStatement(
										"update seat set seatcheck=? where schedulenum=? and seatnum=?");

								pstmt.setInt(1, 0);
								pstmt.setString(2, scheduleNum);
								pstmt.setString(3, seatNum);
								pstmt.executeUpdate();
								
								i_stampCount--;
								if (i_stampCount > 0) {
									freeMovieCount = i_stampCount / 5;
								} else {
									freeMovieCount = 0;
								}

								pstmt = con.prepareStatement("update member set stampcount=? where membernum=?");
								pstmt.setInt(1, i_stampCount);
								LoginPanel.member.setStampCount(i_stampCount);
								pstmt.setString(2, LoginPanel.member.getNum());
								pstmt.executeUpdate();

							}
							if(booking.getTicketNum2()!=null) {
								System.out.println("null�� �ƴϴ�?");
								rs = stmt.executeQuery("select schedulenum, seatnum from ticket where ticketnum ='"
										+ booking.getTicketNum2() + "'");

								while (rs.next()) {

									seatNum = rs.getString(2);
									pstmt = con.prepareStatement(
											"update seat set seatcheck=? where schedulenum=? and seatnum=?");

									pstmt.setInt(1, 0);
									pstmt.setString(2, scheduleNum);
									pstmt.setString(3, seatNum);
									pstmt.executeUpdate();
									
									i_stampCount--;
									if (i_stampCount > 0) {
										freeMovieCount = i_stampCount / 5;
									} else {
										freeMovieCount = 0;
									}

									pstmt = con.prepareStatement("update member set stampcount=? where membernum=?");
									pstmt.setInt(1, i_stampCount);
									LoginPanel.member.setStampCount(i_stampCount);
									pstmt.setString(2, LoginPanel.member.getNum());
									pstmt.executeUpdate();

								}
							}
							
							if(booking.getTicketNum3()!=null) {
								rs = stmt.executeQuery("select schedulenum, seatnum from ticket where ticketnum ='"
										+ booking.getTicketNum3() + "'");

								while (rs.next()) {

									seatNum = rs.getString(2);
									pstmt = con.prepareStatement(
											"update seat set seatcheck=? where schedulenum=? and seatnum=?");

									pstmt.setInt(1, 0);
									pstmt.setString(2, scheduleNum);
									pstmt.setString(3, seatNum);
									pstmt.executeUpdate();
									
									i_stampCount--;
									if (i_stampCount > 0) {
										freeMovieCount = i_stampCount / 5;
									} else {
										freeMovieCount = 0;
									}

									pstmt = con.prepareStatement("update member set stampcount=? where membernum=?");
									pstmt.setInt(1, i_stampCount);
									LoginPanel.member.setStampCount(i_stampCount);
									pstmt.setString(2, LoginPanel.member.getNum());
									pstmt.executeUpdate();

								}
							}
							
							// 1. pay ����
							pstmt = con.prepareStatement("delete from pay where paynum=?");

							pstmt.setString(1, cancelPayNum);
							pstmt.executeUpdate();
							mainFrame.change("my");
							
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
					}

				}
			}
		});

		// [��������] Ŭ�� �̺�Ʈ
		b_back.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				mainFrame.change("movie");
			}

		});

		// [����] Ŭ�� �̺�Ʈ
		b_correct.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				OracleDataSource ods = null;
				Connection con = null;
				PreparedStatement pstmt = null;

				try {
					ods = new OracleDataSource();
					ods.setURL("jdbc:oracle:thin:@localhost:1521:orcl");
					ods.setUser("hmsg");
					ods.setPassword("tiger");
					con = ods.getConnection();
					pstmt = con.prepareStatement("update member set name=?, phonenum=? where membernum='"
							+ LoginPanel.member.getNum() + "'");

					pstmt.setString(1, tf_name.getText());
					pstmt.setString(2, tf_phone.getText());

					pstmt.executeUpdate(); // ������Ʈ�� ���ڵ� ��(int) ����
					LoginPanel.member.setName(tf_name.getText());
					LoginPanel.member.setPhoneNum(tf_phone.getText());
					JOptionPane.showMessageDialog(null, "�����Ǿ����ϴ�.");
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

			}
		});
	}

}
