
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

	// String数组储存button上的文字
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
		secondLayerInfoTrans = secondLayerInfo; // 返回第二层时使用

		// 同secondlayer部分,生成返回按键和显示信息的标签
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

		// 由于默认panel使用固定layout,所以将四个显示票种类的button放入一个新的panel中使用gridlayout
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

		// title显示电影名称,所选screen以及场次时间
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

		// 在jlabel(java 任何GUI)中使用html有利于文字的编排(换行,颜色,大小,划线,斜体等0
		String str = "Please input your student ID:</html>";
		String warning = "<html>";

		if (eventSource.equals(back)) {
			this.dispose();
			new SecondLayer(secondLayerInfoTrans);
		}

		// 循环判断那个按键被按了
		for (int i = 0; i < buttonList.size(); i++) {
			if (eventSource.equals(buttonList.get(i))) {
				String studentId = "";
				if (i == (buttonList.size() - 1)) {
					// 生成输入框提示输入学生id
					// Generate the input dialog ask the user to input the student ID
					studentId = JOptionPane.showInputDialog(warning + str);
					// 尝试将id转换为数字,如果符合要求,则进入下一gui界面
					// 如果不符合要求则产生exception,并catch exception并处理
					// Try to transform the ID into numbers to decide the ID's validity
					try {
						// 数值没用,只是用来验证格式
						Double.parseDouble(studentId);
					} catch (NumberFormatException | NullPointerException exception) {
						// 如果输入非数字或空,则继续循环输入框,并提示相关错误
						// if the ID is empty or not a number, it will warn the user
						if (exception.getClass().equals(new NumberFormatException().getClass())) {
							if (studentId.length() == 0) {
								i--;
								warning = "<html>您未输入,请重试!<br>";
								continue;
							} else {
								i--;
								warning = "<html>您输入了非数字字符,请重试!<br>";
								continue;
							}
						} else if (exception.getClass().equals(new NullPointerException().getClass())) {
							// 如果点击了关闭按键(右上的x),则直接跳过(即不处理这次按键的action)
							// To cancel the input of student ID, click the close button of the window
							break;
						}
					}
				}

				// 向键值对中添加type(票的种类)
				// 0为儿童票,1为成人票,2为老人票,3为学生票(如果是学生票,会增加student id)
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