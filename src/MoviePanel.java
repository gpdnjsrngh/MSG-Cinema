import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import oracle.jdbc.pool.OracleDataSource;

public class MoviePanel extends JPanel implements ActionListener {
	private Main mainFrame;
	private JLabel welcomeLabel = new JLabel();
	private JLabel movieLabel = new JLabel("<���� ������ ��ȭ>");

	private JButton b_parasite = new JButton("����");
	private JButton b_aladdin = new JButton("����");
	private JButton b_spiderMan = new JButton("����");

	private JLabel theater1 = new JLabel("1�� Gold Class");
	private JLabel theater2 = new JLabel("2�� 2D");
	private JLabel theater3 = new JLabel("3�� 4DX");

	private JButton b_myPage = new JButton("My Page");

	public static Movie selectedMovie;
	private List<Movie> movies = new ArrayList<Movie>();
	OracleDataSource ods = null;
	Connection con = null;
	Statement stmt = null;
	ResultSet rs = null;

	public MoviePanel(Main mainFrame) {
		this.mainFrame = mainFrame;
		setLayout(null);

		// "000�� �ȳ��ϼ���"
		Font f1 = new Font("���� ���", Font.BOLD, 20);
		welcomeLabel.setFont(f1);
		welcomeLabel.setText(LoginPanel.member.getName() + "��, �ȳ��ϼ��� :)");
		welcomeLabel.setLocation(30, 20);
		welcomeLabel.setSize(300, 20);
		add(welcomeLabel);

		// [My Page]
		b_myPage.setLocation(620, 20);
		b_myPage.setSize(100, 30);
		add(b_myPage);

		// [My Page] Ŭ�� �̺�Ʈ
		b_myPage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainFrame.change("my");
			}
		});

		// "<���� ������ ��ȭ>"
		Font f2 = new Font("���� ���", Font.PLAIN, 15);
		movieLabel.setFont(f2);
		movieLabel.setLocation(30, 50);
		movieLabel.setSize(200, 20);
		add(movieLabel);

		// ��ȭ ������ �̹��� ��� + ��ȭ ���� ��������
		selectMovieInfo();

		// "1�� Gold Class"
		theater1.setLocation(70, 320);
		theater1.setSize(90, 30);
		add(theater1);

		// "3�� 4DX"
		theater3.setLocation(353, 320);
		theater3.setSize(80, 30);
		add(theater3);

		// "2�� 2D"
		theater2.setLocation(623, 320);
		theater2.setSize(80, 30);
		add(theater2);

		// [����]-�����
		b_parasite.setLocation(75, 350);
		b_parasite.setSize(80, 30);
		b_parasite.setName("Parasite");
		b_parasite.addActionListener(this);
		add(b_parasite);

		// [����]-�˶��
		b_aladdin.setLocation(340, 350);
		b_aladdin.setSize(80, 30);
		b_aladdin.setName("Aladdin");
		b_aladdin.addActionListener(this);
		add(b_aladdin);

		// [����]-�����̴���
		b_spiderMan.setLocation(600, 350);
		b_spiderMan.setSize(80, 30);
		b_spiderMan.setName("SpiderMan");
		b_spiderMan.addActionListener(this);
		add(b_spiderMan);
	}

	public void selectMovieInfo() {

		try {
			ods = new OracleDataSource();
			ods.setURL("jdbc:oracle:thin:@localhost:1521:orcl");
			ods.setUser("hmsg");
			ods.setPassword("tiger");
			con = ods.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery("select * from movie");

			int x = 40;
			while (rs.next()) {

				Movie movie = new Movie(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5));
				movies.add(movie);
				String movieTitle = movie.getTitle();

				ImageIcon icon = new ImageIcon(movieTitle + ".jpg"); // �̹��� ������ ��ü ����

				// �̹����� ������ ���� �ִ� Image��ü �̾ƿ���
				Image im = icon.getImage(); // �̾ƿ� �̹��� ��ü ����� ���Ӱ� �����!
				Image im2 = im.getScaledInstance(160, 230, Image.SCALE_DEFAULT);

				// ���� ������ �������� �̹���(im2)�� ������ ImageIcon ��ü�� �ٽ� ����
				ImageIcon icon2 = new ImageIcon(im2);
				JLabel img = new JLabel(icon2);

				img.setName(movie.getTitle());
				img.setLocation(x, 80);
				img.setSize(160, 230);
				add(img);

				// �̹��� Ŭ�� �̺�Ʈ - ��ȭ �� ���� â
				img.addMouseListener(new MouseListener() {

					@Override
					public void mouseReleased(MouseEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public void mousePressed(MouseEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public void mouseExited(MouseEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public void mouseEntered(MouseEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public void mouseClicked(MouseEvent e) {
						String message = null;
						JLabel img = (JLabel) e.getSource();

						if (img.getName().equals("Parasite")) {
							message = "����: �����\n";
							message += "�帣: ���\n";
							message += "����: ����ȣ\n";
							message += "�������: 15��\n";
							message += "�󿵽ð�: 131��";
						} else if (img.getName().equals("Aladdin")) {
							message = "����: �˶��\n";
							message += "�帣: ��Ÿ��\n";
							message += "����: ���̸�ġ\n";
							message += "�������: ��ü\n";
							message += "�󿵽ð�: 128��";
						} else {
							message = "����: �����̴���\n";
							message += "�帣: �׼�\n";
							message += "����: ������\n";
							message += "�������: 12��\n";
							message += "�󿵽ð�: 120��";
						}

						JOptionPane.showMessageDialog(null, message);
					}
				});

				x = x + 260;
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

	}

	// [����] Ŭ�� �̺�Ʈ
	@Override
	public void actionPerformed(ActionEvent event) {
		JButton clicked = (JButton) event.getSource();

		for (Movie movie : movies) {
			if (movie.getTitle().equals(clicked.getName())) {
				selectedMovie = movie;
				break;
			}
		}

		mainFrame.change("ticketing");

	}

}
