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

	private JButton b_ok = new JButton("확인");
	private JButton b_cancel = new JButton("취소");

	private JTextArea ta_ticketing = new JTextArea();
	private String[] payHow = { "결제 방법", "현금", "카드", "스탬프" };
	private JComboBox<String> combo = new JComboBox<String>(payHow);

	public PayPanel(Main mainFrame) {
		this.mainFrame = mainFrame;
		setLayout(null);
		
		//[결제 정보 창]
		ta_ticketing.setLocation(150, 100);
		ta_ticketing.setSize(300, 300);

		String movieTitle = null;
		switch (MoviePanel.selectedMovie.getTitle()) {
		case "Parasite":
			movieTitle = "[기생충]";
			break;
		case "Aladdin":
			movieTitle = "[알라딘]";
			break;
		case "SpiderMan":
			movieTitle = "[스파이더맨]";
			break;
		}
		ta_ticketing.setText("구매한 영화는 " + movieTitle + " 입니다.\n");
		ta_ticketing.append("티켓 수: " + TicketingPanel.tickets.size());
		ta_ticketing.append("\n총 구매액은 " + TicketingPanel.totalPrice + "원입니다.");
		
		JScrollPane scrollPane = new JScrollPane(ta_ticketing);
		scrollPane.setLocation(100, 50);
		scrollPane.setSize(250, 200);
		add(scrollPane);

		//[결제 방법] 콤보 박스
		combo.setLocation(450, 50);
		combo.setSize(100, 30);
		add(combo);

		//[확인]
		b_ok.setLocation(250, 300);
		b_ok.setSize(100, 30);
		add(b_ok);

		//[취소]
		b_cancel.setLocation(400, 300);
		b_cancel.setSize(100, 30);
		add(b_cancel);

		// [확인] 클릭 이벤트
		b_ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				String selectedPayHow = combo.getSelectedItem().toString();

				// 1. ticket에 seatNum update - db
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
					// 4. 스탬프 결제
					if (selectedPayHow.equals("스탬프")) {
						
						// 스탬프 개수 계산
						int i_stampCount = LoginPanel.member.getStampCount();
						int freeMovieCount = i_stampCount / 5;
						
						
						if(freeMovieCount>=ticketCount) {
							pstmt.setInt(1, i_stampCount-ticketCount*5);
							LoginPanel.member.setStampCount(i_stampCount-ticketCount*5);
							pstmt.setString(2, LoginPanel.member.getNum());
							pstmt.executeUpdate();
						}
						else {
							JOptionPane.showMessageDialog(null, "스탬프가 부족합니다. 다른 결제 방법을 이용하세요.");
							return;
						}
						
					}

					// 5. 잘못된 입력값
					else if(selectedPayHow.equals("결제 방법")) {
						JOptionPane.showMessageDialog(null, "결제 방법을 선택하세요");
						return;
					}
					
					// 6. 스탬프 적립
					int i_stampCount = LoginPanel.member.getStampCount();
					pstmt.setInt(1, i_stampCount+ticketCount);
					LoginPanel.member.setStampCount(i_stampCount+ticketCount);
					pstmt.setString(2, LoginPanel.member.getNum());

					pstmt.executeUpdate(); // 업데이트된 레코드 수(int) 리턴
								
					for (Ticket ticket : TicketingPanel.tickets) {
						pstmt = con.prepareStatement("update ticket set seatnum=? where ticketnum=?");

						ticket.setSeatNum(SeatPanel.selectedSeatNumList.get(TicketingPanel.tickets.indexOf(ticket)));
						pstmt.setString(1, ticket.getSeatNum());
						pstmt.setString(2, ticket.getTicketNum());

						pstmt.executeUpdate(); // 업데이트된 레코드 수(int) 리턴

						// 2. seat에 seatcheck update - db
						pstmt = con.prepareStatement("update seat set seatcheck=? where schedulenum=? and seatnum=?");

						pstmt.setInt(1, 1);
						pstmt.setString(2, TicketingPanel.selectedSchedule.getNum());
						pstmt.setString(3, ticket.getSeatNum());
						pstmt.executeUpdate();

					}

					// 3. pay 레코드 생성
					pstmt = con.prepareStatement(
							"insert into pay(paynum, payoption, paydate, bookingnum, totalprice, membernum)"
									+ " values(pay_seq.nextval,?,sysdate,?,?,?)");

					pstmt.setString(1, selectedPayHow);
					pstmt.setString(2, TicketingPanel.booking.getNum());
					pstmt.setInt(3, TicketingPanel.totalPrice);
					pstmt.setString(4, LoginPanel.member.getNum());
					pstmt.executeUpdate(); // 업데이트된 레코드 수(int) 리턴

					

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
				JOptionPane.showMessageDialog(null, "결제가 완료되었습니다.");
				mainFrame.change("movie");
			}
		});

		// [취소] 클릭 이벤트
		b_cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				JOptionPane.showMessageDialog(null, "결제가 취소되었습니다.");
				// ticket, booking 레코드 drop,

				OracleDataSource ods = null;
				Connection con = null;
				PreparedStatement pstmt = null;

				try {
					ods = new OracleDataSource();
					ods.setURL("jdbc:oracle:thin:@localhost:1521:orcl");
					ods.setUser("hmsg");
					ods.setPassword("tiger");
					con = ods.getConnection();
					// 1. booking 삭제
					pstmt = con.prepareStatement("delete from booking where bookingnum=?");

					pstmt.setString(1, TicketingPanel.booking.getNum());
					pstmt.executeUpdate();
					// 2. ticket 삭제
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
