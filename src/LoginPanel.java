import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import oracle.jdbc.pool.OracleDataSource;

public class LoginPanel extends JPanel {
	private JLabel textLabel = new JLabel("[ HMSG 시네마 ]");
	private JLabel l_name = new JLabel("이름");
	private JLabel l_phone = new JLabel("휴대전화번호");
	private JTextField tf_name = new JTextField(10);
	private JTextField tf_phone = new JTextField(20);
	private JButton b_login = new JButton("로그인");
	private JButton b_register = new JButton("회원가입");

	private Main mainFrame;

	// 로그인된 멤버
	public static Member member;

	public LoginPanel(Main mainFrame) {
		this.mainFrame = mainFrame;

		setLayout(null);

		setBackground(Color.WHITE);
		setOpaque(true);

		// "[ HMSG 시네마 ]"
		Font f1 = new Font("맑은 고딕", Font.BOLD, 50);
		textLabel.setFont(f1);
		textLabel.setLocation(220, 40);
		textLabel.setSize(500, 100);
		// textLabel.setFont(textLabel.getFont().deriveFont(50.0f));
		add(textLabel);

		// "이름"
		l_name.setLocation(350, 170);
		l_name.setSize(200, 100);
		add(l_name);

		tf_name.setLocation(390, 210);
		tf_name.setSize(100, 25);
		tf_name.setText("전혜원"); // 테스트용
		add(tf_name);

		// "휴대전화번호"
		l_phone.setLocation(300, 210);
		l_phone.setSize(200, 100);
		add(l_phone);

		tf_phone.setLocation(390, 250);
		tf_phone.setSize(100, 25);
		tf_phone.setText("01091310259"); // 테스트용
		add(tf_phone);

		// [로그인]
		b_login.setLocation(500, 210);
		b_login.setSize(80, 65);
		add(b_login);

		// [회원가입]
		b_register.setLocation(390, 300);
		b_register.setSize(100, 30);
		add(b_register);

		// [로그인] 클릭 이벤트
		b_login.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 로그인 성공 - 영화 선택 창으로 이동
				if (isExist(tf_name.getText(), tf_phone.getText()))
					mainFrame.change("movie");
				// 로그인 실패
				else
					JOptionPane.showMessageDialog(null, "존재하지 않는 회원입니다.");
			}
		});

		// [회원가입] 클릭 이벤트
		b_register.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 회원 가입 창으로 이동
				mainFrame.change("register");
			}
		});

	}

	// 가입된 회원인지 확인하면서 member에 객체 할당
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

			while (rs.next()) {
				member = new Member(rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4));
				return true;
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
		return false;
	}

}
