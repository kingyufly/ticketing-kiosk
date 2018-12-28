
/**
 * ThirdLayer.java
 * The 3rd Layer of the GUI
 * Show all types of ticket that the user can buy
 * 
 * @author kingyufly
 * @version 2.2
 * 
 * */

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * The 3rd Layer GUI class
 */

public class ThirdLayer extends JFrame implements ActionListener {
	ArrayList<JButton> buttonList;
	Map<String, String> infoTrans;
	List<Map<String, String>> secondLayerInfoTrans;

	JPanel mainPanel;
	JButton back;
	JLabel title;

	int windowHeight = 430 + 30;
	int windowWidth = 400;

	// String���鴢��button�ϵ�����
	/*
	 * String array stores the content on the button which is the ticket type
	 * and the discount information
	 */
	String buttonText[] = { "<html>Child Ticket<br><font size=2 color=\"red\"> &#42;2 to 17 years old</font></html>",
			"<html>Adult Ticket<br><font size=2 color=\"red\"> &#42;18 years and older</font></html>",
			"<html>Senior Ticket<br><font size=2 color=\"red\"> &#42;55 years and older</font></html>",
			"<html>Student Ticket<br><font size=2 color=\"red\"> &#42;18 years and older in full time education<br>&#42;Student ID required</font></html>" };

	/**
	 * Override the default constructor In the third layer, the four types of
	 * ticket will be displayed
	 * 
	 * @param info
	 *            The mapping of keys and values which contains the user's
	 *            choice
	 * 
	 * @param secondLayerInfo
	 *            Used to return back to the second layer
	 */

	public ThirdLayer(Map<String, String> info, List<Map<String, String>> secondLayerInfo) {
		infoTrans = info;
		// secondLayerInfoTrans is used for return back to secondlayer
		secondLayerInfoTrans = secondLayerInfo; // ���صڶ���ʱʹ��

		// ͬsecondlayer����,���ɷ��ذ�������ʾ��Ϣ�ı�ǩ
		// Generate the back button with a picture as the content of the button
		buttonList = new ArrayList<JButton>();
		back = new JButton();
		back.setIcon(new ImageIcon("./movies/info/img/back.jpg"));
		back.setBounds(0, 0, 50, 30);
		back.addActionListener(this);
		this.getContentPane().add(back);

		title = new JLabel("Please Ticket Type", JLabel.CENTER);
		title.setBounds(0, 0, 400, 30);
		title.setBorder(BorderFactory.createLineBorder(Color.black));
		this.getContentPane().add(title);

		// ����Ĭ��panelʹ�ù̶�layout,���Խ��ĸ���ʾƱ�����button����һ���µ�panel��ʹ��gridlayout
		// Due to there are only four types of tickets, the layout is set to gridlayout which is
		mainPanel = new JPanel();

		for (int i = 0; i < 4; i++) {
			buttonList.add(new JButton(buttonText[i]));
			buttonList.get(i).addActionListener(this);
			mainPanel.add(buttonList.get(i));
		}
		mainPanel.setLayout(new GridLayout(4, 1, 5, 5));
		mainPanel.setBounds(0, 30, 400, 400);
		this.getContentPane().add(mainPanel);

		// title��ʾ��Ӱ����,��ѡscreen�Լ�����ʱ��
		// The title shows the movies name, screen number and time
		this.setTitle(info.get("moviename") + "-" + info.get("screen").toUpperCase() + "-"
				+ info.get("time").substring(0, 2) + ":" + info.get("time").substring(2, 4));
		this.setLayout(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(windowWidth, windowHeight);
		int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
		int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
		this.setLocation((screenWidth - windowWidth) / 2, (screenHeight - windowHeight) / 2);
		this.setResizable(false);
		this.setVisible(true);
	}

	/**
	 * Method actionPerformed, Override the method to handle the clicked action
	 * from the button
	 * 
	 * @param e
	 *            the ActionEvent cause from the Panel
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton eventSource = (JButton) e.getSource();

		// ��jlabel(java �κ�GUI)��ʹ��html���������ֵı���(����,��ɫ,��С,����,б���0
		String str = "Please input your student ID:</html>";
		String warning = "<html>";

		if (eventSource.equals(back)) {
			this.dispose();
			new SecondLayer(secondLayerInfoTrans);
		}

		// ѭ���ж��Ǹ�����������
		for (int i = 0; i < buttonList.size(); i++) {
			if (eventSource.equals(buttonList.get(i))) {
				String studentId = "";
				if (i == (buttonList.size() - 1)) {
					// �����������ʾ����ѧ��id
					// Generate the input dialog ask the user to input the student ID
					studentId = JOptionPane.showInputDialog(warning + str);
					// ���Խ�idת��Ϊ����,�������Ҫ��,�������һgui����
					// ���������Ҫ�������exception,��catch exception������
					// Try to transform the ID into numbers to decide the ID's validity
					try {
						// ��ֵû��,ֻ��������֤��ʽ
						Double.parseDouble(studentId);
					} catch (NumberFormatException | NullPointerException exception) {
						// �����������ֻ��,�����ѭ�������,����ʾ��ش���
						// if the ID is empty or not a number, it will warn the user
						if (exception.getClass().equals(new NumberFormatException().getClass())) {
							if (studentId.length() == 0) {
								i--;
								warning = "<html>��δ����,������!<br>";
								continue;
							} else {
								i--;
								warning = "<html>�������˷������ַ�,������!<br>";
								continue;
							}
						} else if (exception.getClass().equals(new NullPointerException().getClass())) {
							// �������˹رհ���(���ϵ�x),��ֱ������(����������ΰ�����action)
							// To cancel the input of student ID, click the close button of the window
							break;
						}
					}
				}

				// ���ֵ�������type(Ʊ������)
				// 0Ϊ��ͯƱ,1Ϊ����Ʊ,2Ϊ����Ʊ,3Ϊѧ��Ʊ(�����ѧ��Ʊ,������student id)
				// Add "type" and its value to the maplist
				infoTrans.put("type", "" + i);

				if (i == 3)
					infoTrans.put("studentId", "" + studentId);

				this.dispose();
				new FourthLayer(infoTrans, secondLayerInfoTrans);
				// thread.interrupt();
			}
		}
	}
}