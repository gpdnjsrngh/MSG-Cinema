import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;

import oracle.jdbc.pool.OracleDataSource;

public class AdminPanel extends JPanel {

	private Main mainFrame;

	private JButton button = new JButton("��������");
	
	public AdminPanel(Main mainFrame) {
		setLayout(null);
		this.mainFrame = mainFrame;
			
		//1. ���ų��� ��ȸ
		//2. ���ų��� ����
		//3. ������ ����
		
		
		button.setLocation(400, 200);
		button.setSize(100, 30);
		add(button);
		
		
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				mainFrame.change("movie");
			}
			
		});
		
		
	}

	

}
