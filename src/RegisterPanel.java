import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import oracle.jdbc.pool.OracleDataSource;

public class RegisterPanel extends JPanel {
	private Main mainFrame;

	private JLabel titleLabel = new JLabel("회원가입");
	private JLabel l_name = new JLabel("이름");
	private JLabel l_phone = new JLabel("휴대전화번호");
	private JTextField tf_name = new JTextField(10);
	private JTextField tf_phone = new JTextField(20);

	private JButton b_register = new JButton("가입");

	public RegisterPanel(Main mainFrame) {
		this.mainFrame = mainFrame;
		setLayout(null);

		// "회원가입"
		Font f1 = new Font("맑은 고딕", Font.BOLD, 25);
		titleLabel.setFont(f1);
		titleLabel.setLocation(320, 20);
		titleLabel.setSize(500, 100);
		add(titleLabel);

		// "이름"
		l_name.setLocation(300, 100);
		l_name.setSize(200, 100);
		add(l_name);

		tf_name.setLocation(340, 140);
		tf_name.setSize(100, 25);
		add(tf_name);

		// "휴대전화번호"
		l_phone.setLocation(250, 140);
		l_phone.setSize(200, 100);
		add(l_phone);

		tf_phone.setLocation(340, 180);
		tf_phone.setSize(100, 25);
		add(tf_phone);

		// [가입]
		b_register.setLocation(340, 230);
		b_register.setSize(100, 30);
		add(b_register);

		// [가입] 클릭 이벤트
		b_register.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				// 1. 회원중복확인
				if (isExist(tf_name.getText(), tf_phone.getText()))
					JOptionPane.showMessageDialog(null, "이미 가입된 회원입니다.");

				// 2. member 레코드 삽입
				else {
					OracleDataSource ods = null;
					Connection con = null;
					PreparedStatement pstmt = null;

					try {
						ods = new OracleDataSource();
						ods.setURL("jdbc:oracle:thin:@localhost:1521:orcl");
						ods.setUser("hmsg");
						ods.setPassword("tiger");
						con = ods.getConnection();
						pstmt = con.prepareStatement("insert into member(membernum, name, phonenum, stampcount)"
								+ " values(member_seq.nextval,?,?,0)");

						pstmt.setString(1, tf_name.getText());
						pstmt.setString(2, tf_phone.getText());

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
					JOptionPane.showMessageDialog(null, "가입되었습니다.");
					mainFrame.change("login");
				}

			}
		});

	}

	public boolean isExist(String name, String phone) {
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
			rs = stmt.executeQuery("select * from member where name='" + name + "' and phonenum='" + phone + "'");

			while (rs.next())
				return true;

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
		return false;
	}

}
