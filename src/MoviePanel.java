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
	private JLabel movieLabel = new JLabel("<현재 상영중인 영화>");

	private JButton b_parasite = new JButton("선택");
	private JButton b_aladdin = new JButton("선택");
	private JButton b_spiderMan = new JButton("선택");

	private JLabel theater1 = new JLabel("1관 Gold Class");
	private JLabel theater2 = new JLabel("2관 2D");
	private JLabel theater3 = new JLabel("3관 4DX");

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

		// "000님 안녕하세요"
		Font f1 = new Font("맑은 고딕", Font.BOLD, 20);
		welcomeLabel.setFont(f1);
		welcomeLabel.setText(LoginPanel.member.getName() + "님, 안녕하세요 :)");
		welcomeLabel.setLocation(30, 20);
		welcomeLabel.setSize(300, 20);
		add(welcomeLabel);

		// [My Page]
		b_myPage.setLocation(620, 20);
		b_myPage.setSize(100, 30);
		add(b_myPage);

		// [My Page] 클릭 이벤트
		b_myPage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainFrame.change("my");
			}
		});

		// "<현재 상영중인 영화>"
		Font f2 = new Font("맑은 고딕", Font.PLAIN, 15);
		movieLabel.setFont(f2);
		movieLabel.setLocation(30, 50);
		movieLabel.setSize(200, 20);
		add(movieLabel);

		// 영화 포스터 이미지 출력 + 영화 정보 가져오기
		selectMovieInfo();

		// "1관 Gold Class"
		theater1.setLocation(70, 320);
		theater1.setSize(90, 30);
		add(theater1);

		// "3관 4DX"
		theater3.setLocation(353, 320);
		theater3.setSize(80, 30);
		add(theater3);

		// "2관 2D"
		theater2.setLocation(623, 320);
		theater2.setSize(80, 30);
		add(theater2);

		// [선택]-기생충
		b_parasite.setLocation(75, 350);
		b_parasite.setSize(80, 30);
		b_parasite.setName("Parasite");
		b_parasite.addActionListener(this);
		add(b_parasite);

		// [선택]-알라딘
		b_aladdin.setLocation(340, 350);
		b_aladdin.setSize(80, 30);
		b_aladdin.setName("Aladdin");
		b_aladdin.addActionListener(this);
		add(b_aladdin);

		// [선택]-스파이더맨
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

				ImageIcon icon = new ImageIcon(movieTitle + ".jpg"); // 이미지 아이콘 객체 생성

				// 이미지를 실제로 갖고 있는 Image객체 뽑아오기
				Image im = icon.getImage(); // 뽑아온 이미지 객체 사이즈를 새롭게 만들기!
				Image im2 = im.getScaledInstance(160, 230, Image.SCALE_DEFAULT);

				// 새로 조절된 사이즈의 이미지(im2)를 가지는 ImageIcon 객체를 다시 생성
				ImageIcon icon2 = new ImageIcon(im2);
				JLabel img = new JLabel(icon2);

				img.setName(movie.getTitle());
				img.setLocation(x, 80);
				img.setSize(160, 230);
				add(img);

				// 이미지 클릭 이벤트 - 영화 상세 정보 창
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
							message = "제목: 기생충\n";
							message += "장르: 드라마\n";
							message += "감독: 봉준호\n";
							message += "관람등급: 15세\n";
							message += "상영시간: 131분";
						} else if (img.getName().equals("Aladdin")) {
							message = "제목: 알라딘\n";
							message += "장르: 판타지\n";
							message += "감독: 가이리치\n";
							message += "관람등급: 전체\n";
							message += "상영시간: 128분";
						} else {
							message = "제목: 스파이더맨\n";
							message += "장르: 액션\n";
							message += "감독: 존왓츠\n";
							message += "관람등급: 12세\n";
							message += "상영시간: 120분";
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

	// [선택] 클릭 이벤트
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
