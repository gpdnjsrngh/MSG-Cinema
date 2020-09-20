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

import oracle.jdbc.pool.OracleDataSource;

public class SeatPanel extends JPanel {
	private Main mainFrame;
	private JButton screen = new JButton("��ũ��");
	private JLabel seatInfo = new JLabel();
	private JButton button = new JButton("Ȯ��");
	private List<Seat> allSeatList = new ArrayList<>();
	private List<JCheckBox> checkBoxList = new ArrayList<>();
	
	public static List<String> selectedSeatNumList = new ArrayList<>();
	public SeatPanel(Main mainFrame) {
		this.mainFrame = mainFrame;
		setLayout(null);
				
		//[��ũ��]
		screen.setLocation(3, 20);
		screen.setSize(770, 30);
		screen.setEnabled(false);
		add(screen);
		
		//"�ܿ� �¼�"
		seatInfo.setLocation(500, 300);
		seatInfo.setSize(200,20);
		add(seatInfo);
		
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
			
			rs = stmt.executeQuery("select seatnum, seatcheck from seat where schedulenum='"+TicketingPanel.selectedSchedule.getNum()+"' order by seatnum ");
			
			int disabledSeatCount = 0;
			while(rs.next()) {
				allSeatList.add(new Seat(rs.getString(1), rs.getString(2)));
				JCheckBox cb = new JCheckBox(rs.getString(1));
				if(rs.getInt(2)==1) {
					disabledSeatCount++;
					cb.setEnabled(false);
				}
					
				checkBoxList.add(cb);
			}
			int abledSeatCount = checkBoxList.size()-disabledSeatCount;
			seatInfo.setText("�ܿ��¼�("+abledSeatCount+"/"+checkBoxList.size()+")");
			
			int j=1;
			int x=50;
			int y=100;
		
			for(JCheckBox seat:checkBoxList) {
				if(j==10) {
					x=50;
					y=y+20;
					j=1;
				}
				seat.setLocation(x, y);
				seat.setSize(50, 20);
				add(seat);
				x=x+80;
				j++;
				
			}
		} catch(Exception e) {
			System.out.println(e);
		} finally {
			try {
				if(con!=null) con.close();
				if(stmt!=null) stmt.close();
				if(rs!=null) rs.close();				
			} catch(Exception e) {
				System.out.println(e);
			}
		}
		
		button.setLocation(350, 300);
		button.setSize(100, 30);
		add(button);
		
		//[Ȯ��] Ŭ�� �̺�Ʈ
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				int ticketCount = TicketingPanel.tickets.size();
				//List<String> seatNum = new ArrayList<String>();
				
				//1. Ƽ�� ����ŭ �¼��� �����ߴ���
				int checkCount = 0;
				for(JCheckBox cb: checkBoxList) {
					if(cb.isSelected()) {
						checkCount++;
						selectedSeatNumList.add(cb.getText());
					}
							
				}
				
				if(checkCount!=ticketCount) {
					JOptionPane.showMessageDialog(null, "������ �¼� ���� ���� �ο� ���� �����ؾ� �մϴ�.");
					return;
				}
				mainFrame.change("pay");

			}
		});


	}	

}
