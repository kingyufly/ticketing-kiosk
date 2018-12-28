
/**
 * FourthLayer.java
 * The 4th Layer of the GUI
 * Show the screens seat graph so that the user can select the seat
 * 
 * @author kingyufly
 * @version 2.2
 * 
 * */

import java.awt.Color;
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

/**
 * The 4th Layer GUI class It contains three inner class each stand for one
 * screen
 */

public class FourthLayer {
	static final int screenNum = 3;

	/**
	 * Override the default constructor In the fourth layer, and generate the
	 * corresponding screen graph according to the key "screen"
	 * 
	 * @param info
	 *            The mapping of keys and values which contains the user's
	 *            choice
	 * 
	 * @param secondLayerInfo
	 *            Used to return back to the second layer
	 */

	public FourthLayer(Map<String, String> info, List<Map<String, String>> secondLayerInfo) {
		if (info.get("screen").equals("screen1")) {
			new Screen1(info, secondLayerInfo);
		} else if (info.get("screen").equals("screen2")) {
			new Screen2(info, secondLayerInfo);
		} else if (info.get("screen").equals("screen3")) {
			new Screen3(info, secondLayerInfo);
		} else {
		}
	}

	/**
	 * Inner class, used to generate the screen1's seat graph
	 */

	public class Screen1 extends JFrame {
		ArrayList<JButton> buttonList = new ArrayList<JButton>();
		Map<String, String> infoDup;
		// 由于在多线程中使用了removeactionlistener(),所以不能使用addactionlistener(this),必须要生成actionlistener的实例
		// 如果使用jbutton.setenable()则简单得多
		// outerclass同理
		// Use a stand alone action listener since removeactionlistener is used
		// in the multiThread part
		SimpleListener listener = new SimpleListener();
		JFrame outerClass = null;

		Thread thread = null;
		CheckThread checker = new CheckThread();

		JButton back;
		JLabel title;

		boolean seatBool[] = null;
		List<Map<String, String>> secondLayerInfoDup;

		/**
		 * Override the default constructor In the fourth layer, and generate
		 * the screen1's graph
		 * 
		 * @param info
		 *            The mapping of keys and values which contains the user's
		 *            choice
		 * 
		 * @param secondLayerInfo
		 *            Used to return back to the second layer
		 */

		public Screen1(Map<String, String> info, List<Map<String, String>> secondLayerInfo) {
			infoDup = info;
			secondLayerInfoDup = secondLayerInfo;

			outerClass = this; // 由于内部类要调用外部类的实例,所以this无法使用,必须生成变量来调用;
								// 与单独生成actionlistener类相反(两种方法解决内部类调用外部类实例的问题)

			// 添加back以及提示文字
			back = new JButton();
			back.setIcon(new ImageIcon("./movies/info/img/back.jpg"));
			back.setBounds(0, 0, 50, 30);
			back.addActionListener(listener);
			this.getContentPane().add(back);

			title = new JLabel("Please Select Seat", JLabel.CENTER);
			title.setBounds(0, 0, 530 + 10, 30);
			title.setBorder(BorderFactory.createLineBorder(Color.black));
			this.getContentPane().add(title);

			thread = new Thread(checker);

			// 先进行检查,生成图形界面后再启动多线程,以防止延时出现的exception
			// Check the status first and then generate the GUI before start the
			// multiThread checking in order to avoid exceptions
			ArrayList<String> seatList = new FileOp().readFile(
					"./screens/" + info.get("screen") + "/" + info.get("movie") + "/" + info.get("time") + "/seat.txt");

			seatBool = new boolean[seatList.size()];
			for (int i = 0; i < seatList.size(); i++) {
				seatBool[i] = Boolean.parseBoolean(seatList.get(i));
			}

			int j = 0;
			JLabel tmpL = null;
			JButton tmpB = null;
			// 生成座位图标(button)以及对应的行编号(D,C,B,A)
			// Generate the seat label(button) and its corresponding
			// index(D,C,B,A)
			for (int i = 0; i < seatBool.length; i++) {
				j = 8 - i % 8;
				// 如果是最左边的,则添加行编号
				// If the label is on the left or right, then the label is the
				// index label
				if (j == 8) {
					tmpL = new JLabel("" + (char) ('D' - i / 8), JLabel.CENTER);
					tmpL.setBounds(0, 20 + (i / 8) * 90 + 30, 40, 50);
					this.getContentPane().add(tmpL);
				}
				// 添加座位(通过上边获取的seat状态来确定seat的图标颜色,蓝色为未售出,红色为售出(可以点击,但无反应))
				// Add seat buttons, if the status is sold, the seat will become
				// red, if the status is not sold, the seat will become blue
				tmpB = new JButton();
				ImageIcon image = null;
				if (seatBool[i]) {
					image = new ImageIcon("./screens/img/" + j + ".jpg");
					tmpB.setIcon(image);
					if (j >= 5)
						tmpB.setBounds(40 + (8 - j) * 50, 20 + (i / 8) * 90 + 30, 50, 50);
					else
						tmpB.setBounds(40 + 50 + (8 - j) * 50, 20 + (i / 8) * 90 + 30, 50, 50);
					tmpB.addActionListener(listener);
					buttonList.add(tmpB);
				} else {
					image = new ImageIcon("./screens/img/" + j + "_sold.jpg");
					tmpB.setIcon(image);
					if (j >= 5)
						tmpB.setBounds(40 + (8 - j) * 50, 20 + (i / 8) * 90 + 30, 50, 50);
					else
						tmpB.setBounds(40 + 50 + (8 - j) * 50, 20 + (i / 8) * 90 + 30, 50, 50);
					buttonList.add(tmpB);
				}
				this.getContentPane().add(buttonList.get(i));
				// 如果是最右边的,则添加行编号
				if (j == 1) {
					tmpL = new JLabel("" + (char) ('D' - i / 8), JLabel.CENTER);
					tmpL.setBounds(490, 20 + (i / 8) * 90 + 30, 40, 50);
					this.getContentPane().add(tmpL);
				}
			}

			// 生成screen(荧幕)的图标,背景色设为灰色
			// Generate label stand for the screen in color green
			tmpL = new JLabel("SCREEN", JLabel.CENTER);
			// 必须先设定不透明,才能设定背景色
			tmpL.setOpaque(true);
			// 设定为灰色(153,153,153)
			tmpL.setBackground(new Color(153, 153, 153));
			// 这么写也可以
			// tmpL.setBackground(Color.GRAY);
			tmpL.setBounds(90, 380 + 30, 350, 20);
			this.getContentPane().add(tmpL);
			this.setLayout(null);

			int windowWidth = 530 + 10;
			int windowHeight = 420 + 30 + 30;

			this.setTitle("SCREEN1");
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setSize(windowWidth, windowHeight);
			int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
			int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
			this.setLocation((screenWidth - windowWidth) / 2, (screenHeight - windowHeight) / 2);
			this.setResizable(false);
			this.setVisible(true);
			// 生成多线程,更新seat的状态
			// Start the multiThread to check the status of the seat
			thread.start();
		}

		/**
		 * Inner class, used to generate stand alone action listener which will
		 * used because of the multithread
		 * 
		 */
		// 单独的actionlistener类
		public class SimpleListener implements ActionListener {
			/**
			 * Method actionPerformed, Override the method to handle the clicked
			 * action from the button
			 * 
			 * @param e
			 *            the ActionEvent cause from the Panel
			 */
			@Override

			public void actionPerformed(ActionEvent e) {
				// 如果点击了座位,则停止多线程以免造成图标的更换
				// 可用synchronized wait notify解决(高级方法),但出现问题,等待解决(已解决)
				// If the seat is clicked, then stop the multiThread in order to
				// avoid the changing of the seat's image during the confirm of
				// the
				// seat
				checker.setFlag(false);

				// 如果点了返回,则移除添加的third中添加的type(如果是student ticket还有id),再返回第三层
				// If back button is clicked, then return to third layer
				JButton eventSource = (JButton) e.getSource();

				if (eventSource.equals(back)) {
					// 因为actionlistener使用了单独的类,所以要外部类(GUI).dispose
					outerClass.dispose();
					// 停止多线程
					thread.interrupt();
					if (infoDup.containsKey("id"))
						infoDup.remove("id");
					infoDup.remove("type");
					new ThirdLayer(infoDup, secondLayerInfoDup);
				}

				// 如果点击的是座位的按键
				// if the seat's button is clicked, then ask the user to confirm
				// the seat
				ImageIcon image = null;
				for (int i = 0; i < buttonList.size(); i++) {
					if (eventSource.equals(buttonList.get(i))) {
						// 如果点击了,则先将坐位变红,停止监听事件
						// If the seat is clicked, change the seat to red and
						// remove the action listener
						image = new ImageIcon("./screens/img/" + (8 - i % 8) + "_sold.jpg");

						buttonList.get(i).setIcon(image);
						buttonList.get(i).removeActionListener(this);

						// 弹出提示框提示用户是否确认购买
						// Ask the user to confirm the selection of the seat
						int choice = JOptionPane.showConfirmDialog(null,
								"<html>Your choice is: Seat " + (char) ('D' - i / 8) + (8 - i % 8)
										+ "<br>Confirm to purchase?</html>",
								"Confirm to purchase", JOptionPane.YES_NO_OPTION);

						// 如果是的话进入fifthlayer确认付款
						// no:1 yes:0 cancel:-1
						if (choice == 0) {
							infoDup.put("seat", "" + i);
							new FifthLayer(infoDup, secondLayerInfoDup);
							outerClass.dispose();
							thread.interrupt();
						} else {
							// 如果取消的话,则恢复蓝色图标,增加监听,开启多线程
							// If the confirm is canceled, then change the color
							// back to blue and add action listener to the
							// button
							image = new ImageIcon("./screens/img/" + (8 - i % 8) + ".jpg");
							buttonList.get(i).setIcon(image);
							buttonList.get(i).addActionListener(this);
							checker.setFlag(true);
							break;
						}
						// buttonList.get(i).setEnabled(false);
					}
				}
			}
		}

		/**
		 * Inner class as a checker to check the remain seats
		 * 
		 */

		public class CheckThread implements Runnable {
			private boolean flag = true;

			/**
			 * Method setFlag, It is the setter of variable "flag"
			 * 
			 * @param flag
			 *            It is to set the value of the flag
			 */

			public void setFlag(boolean flag) {
				this.flag = flag;
			}

			/**
			 * Method run, Override the method run to achieve check the movie's
			 * status in a set period
			 */

			public void run() {
				while (true) {
					// 先读取seat文档用来确定座位是否售出
					// Read the seat file to confirm whether the seat has been
					// sold out
					ArrayList<String> seatList = new FileOp().readFile("./screens/" + infoDup.get("screen") + "/"
							+ infoDup.get("movie") + "/" + infoDup.get("time") + "/seat.txt");

					seatBool = new boolean[seatList.size()];
					for (int i = 0; i < seatList.size(); i++) {
						seatBool[i] = Boolean.parseBoolean(seatList.get(i));
					}

					int j = 0;
					ImageIcon image = null;
					if (flag) {
						for (int i = 0; i < seatBool.length; i++) {
							j = 8 - i % 8;
							if (seatBool[i]) {
								// 如果未售出(true),则变蓝,并添加actionlistener(以防手动调整座位数)
								// If the seat has not been sold, change the
								// color to blue and add actionlistener to it
								image = new ImageIcon("./screens/img/" + j + ".jpg");
								buttonList.get(i).setIcon(image);
								// 在手动调整票数时,用以更新座位图
								if (buttonList.get(i).getActionListeners() == null)
									buttonList.get(i).addActionListener(listener);
							} else {
								// 如果售出(false),则变红,并移除actionlistener
								image = new ImageIcon("./screens/img/" + j + "_sold.jpg");
								buttonList.get(i).setIcon(image);
								buttonList.get(i).removeActionListener(listener);
							}
						}
					}

					// 判断在选择座位时 所有座位被别人选走, 返回第二层
					// If all the seat has been chose by other user, it will
					// return the secondlayer
					boolean back_flag = true;
					for (int i = 0; i < seatBool.length; i++) {
						if (seatBool[i]) {
							back_flag = false;
							break;
						}
						if (!back_flag)
							break;
					}

					if (back_flag) {
						JOptionPane.showMessageDialog(null, "Sorry! You are so slow! All seats have been sold out!",
								"Warning", JOptionPane.WARNING_MESSAGE);
						outerClass.dispose();
						if (infoDup.containsKey("studentId"))
							infoDup.remove("studentId");
						infoDup.remove("type");
						new SecondLayer(secondLayerInfoDup);
						break;
					}

					try {
						// 每一秒检查一次
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						break;
						// e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * Inner class, used to generate the screen2's seat graph
	 */

	public class Screen2 extends JFrame {
		ArrayList<JButton> buttonList = new ArrayList<JButton>();
		Map<String, String> infoDup;
		SimpleListener listener = new SimpleListener();
		Thread thread = null;
		CheckThread checker = new CheckThread();
		JFrame outerClass = null;
		JButton back;
		JLabel title;

		boolean seatBool[] = null;
		List<Map<String, String>> secondLayerInfoDup;

		/**
		 * Override the default constructor In the fourth layer, and generate
		 * the screen2's graph
		 * 
		 * @param info
		 *            The mapping of keys and values which contains the user's
		 *            choice
		 * 
		 * @param secondLayerInfo
		 *            Used to return back to the second layer
		 */

		public Screen2(Map<String, String> info, List<Map<String, String>> secondLayerInfo) {
			infoDup = info;
			secondLayerInfoDup = secondLayerInfo;
			outerClass = this; // 由于内部类要调用外部类的实例,所以this无法使用,必须生成变量来调用;
								// 与单独生成actionlistener类相反(两种方法解决内部类调用外部类实例的问题)

			back = new JButton();
			back.setIcon(new ImageIcon("./movies/info/img/back.jpg"));
			back.setBounds(0, 0, 50, 30);
			back.addActionListener(listener);
			this.getContentPane().add(back);

			title = new JLabel("Please Select Seat", JLabel.CENTER);
			title.setBounds(0, 0, 530 + 10, 30);
			title.setBorder(BorderFactory.createLineBorder(Color.black));
			this.getContentPane().add(title);

			thread = new Thread(checker);

			// 先进行检查,生成图形界面后再启动多线程,以防止延时出现的exception
			ArrayList<String> seatList = new FileOp().readFile(
					"./screens/" + info.get("screen") + "/" + info.get("movie") + "/" + info.get("time") + "/seat.txt");

			seatBool = new boolean[seatList.size()];
			for (int i = 0; i < seatList.size(); i++) {
				seatBool[i] = Boolean.parseBoolean(seatList.get(i));
			}

			JLabel tmpL = null;
			JButton tmpB = null;

			// 先生成最上面一排(8个座位)
			for (int i = 8; i > 0; i--) {
				if (i == 8) {
					tmpL = new JLabel("D", JLabel.CENTER);
					tmpL.setBounds(0, 20 + 30, 40, 50);
					this.getContentPane().add(tmpL);
				}
				tmpB = new JButton();
				ImageIcon image = null;
				if (seatBool[8 - i]) {
					image = new ImageIcon("./screens/img/" + i + ".jpg");
					tmpB.setIcon(image);
					if (i >= 5)
						tmpB.setBounds(40 + (8 - i) * 50, 20 + 30, 50, 50);
					else
						tmpB.setBounds(40 + 50 + (8 - i) * 50, 20 + 30, 50, 50);
					tmpB.addActionListener(listener);
					buttonList.add(tmpB);
				} else {
					image = new ImageIcon("./screens/img/" + i + "_sold.jpg");
					tmpB.setIcon(image);
					if (i >= 5)
						tmpB.setBounds(40 + (8 - i) * 50, 20 + 30, 50, 50);
					else
						tmpB.setBounds(40 + 50 + (8 - i) * 50, 20 + 30, 50, 50);
					buttonList.add(tmpB);
				}
				this.getContentPane().add(buttonList.get(8 - i));
				if (i == 1) {
					tmpL = new JLabel("D", JLabel.CENTER);
					tmpL.setBounds(490, 20 + 30, 40, 50);
					this.getContentPane().add(tmpL);
				}
			}

			// 再生成其他三排(6个座位)
			int j = 0;
			for (int i = 0; i < seatBool.length - 8; i++) {
				j = 6 - i % 6;
				if (j == 6) {
					tmpL = new JLabel("" + (char) ('C' - i / 6), JLabel.CENTER);
					tmpL.setBounds(0, 20 + 90 + (i / 6) * 90 + 30, 40, 50);
					this.getContentPane().add(tmpL);
				}
				tmpB = new JButton();
				ImageIcon image = null;
				if (seatBool[i + 8]) {
					image = new ImageIcon("./screens/img/" + j + ".jpg");
					tmpB.setIcon(image);
					if (j >= 4)
						tmpB.setBounds(40 + 50 + (6 - j) * 50, 20 + 90 + (i / 6) * 90 + 30, 50, 50);
					else
						tmpB.setBounds(40 + 50 * 2 + (6 - j) * 50, 20 + 90 + (i / 6) * 90 + 30, 50, 50);
					tmpB.addActionListener(listener);
					buttonList.add(tmpB);
				} else {
					image = new ImageIcon("./screens/img/" + j + "_sold.jpg");
					tmpB.setIcon(image);
					if (j >= 4)
						tmpB.setBounds(40 + 50 + (6 - j) * 50, 20 + 90 + (i / 6) * 90 + 30, 50, 50);
					else
						tmpB.setBounds(40 + 50 * 2 + (6 - j) * 50, 20 + 90 + (i / 6) * 90 + 30, 50, 50);
					buttonList.add(tmpB);
				}
				this.getContentPane().add(buttonList.get(i + 8));
				if (j == 1) {
					tmpL = new JLabel("" + (char) ('C' - i / 6), JLabel.CENTER);
					tmpL.setBounds(490, 20 + 90 + (i / 6) * 90 + 30, 40, 50);
					this.getContentPane().add(tmpL);
				}
			}

			tmpL = new JLabel("SCREEN", JLabel.CENTER);
			tmpL.setOpaque(true);
			tmpL.setBackground(new Color(153, 153, 153));
			// 这么写也可以
			// tmpL.setBackground(Color.GRAY);
			tmpL.setBounds(90, 380 + 30, 350, 20);
			this.getContentPane().add(tmpL);
			this.setLayout(null);

			int windowWidth = 530 + 10;
			int windowHeight = 420 + 30 + 30;

			this.setTitle("SCREEN2");
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setSize(windowWidth, windowHeight);
			int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
			int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
			this.setLocation((screenWidth - windowWidth) / 2, (screenHeight - windowHeight) / 2);
			this.setResizable(false);
			this.setVisible(true);

			thread.start();
		}

		/**
		 * Inner class, used to generate stand alone action listener which will
		 * used because of the multithread
		 * 
		 */
		// 单独的actionlistener类
		public class SimpleListener implements ActionListener {
			/**
			 * Method actionPerformed, Override the method to handle the clicked
			 * action from the button
			 * 
			 * @param e
			 *            the ActionEvent cause from the Panel
			 */
			@Override
			public void actionPerformed(ActionEvent e) {

				// 如果点击了座位,则停止多线程以免造成图标的更换
				// 可用synchronized wait notify解决(高级方法),但出现问题,等待解决
				checker.setFlag(false);

				JButton eventSource = (JButton) e.getSource();

				if (eventSource.equals(back)) {
					outerClass.dispose();
					thread.interrupt();
					if (infoDup.containsKey("id"))
						infoDup.remove("id");
					infoDup.remove("type");
					new ThirdLayer(infoDup, secondLayerInfoDup);
				}

				ImageIcon image = null;
				for (int i = 0; i < buttonList.size(); i++) {
					if (eventSource.equals(buttonList.get(i))) {
						if (i < 8)
							image = new ImageIcon("./screens/img/" + (8 - i % 8) + "_sold.jpg");
						else
							image = new ImageIcon("./screens/img/" + (6 - (i - 8) % 6) + "_sold.jpg");
						buttonList.get(i).setIcon(image);
						buttonList.get(i).removeActionListener(this);

						String msg = "";
						if (i < 8)
							msg = "<html>Your choice is: Seat " + (char) ('D' - i / 8) + (8 - i % 8)
									+ "<br>Confirm to purchase?</html>";
						else
							msg = "<html>Your choice is: Seat " + (char) ('C' - (i - 8) / 6) + (6 - (i - 8) % 6)
									+ "<br>Confirm to purchase?</html>";

						int choice = JOptionPane.showConfirmDialog(null, msg, "Confirm to purchase",
								JOptionPane.YES_NO_OPTION);

						// no:1 yes:0 cancel:-1
						if (choice == 0) {
							infoDup.put("seat", "" + i);
							new FifthLayer(infoDup, secondLayerInfoDup);
							outerClass.dispose();
							thread.interrupt();
						} else {
							if (i < 8)
								image = new ImageIcon("./screens/img/" + (8 - i % 8) + ".jpg");
							else
								image = new ImageIcon("./screens/img/" + (6 - (i - 8) % 6) + ".jpg");
							buttonList.get(i).setIcon(image);
							buttonList.get(i).addActionListener(this);
							checker.setFlag(true);
							break;
						}
						// buttonList.get(i).setEnabled(false);
					}
				}
			}
		}

		/**
		 * Inner class as a checker to check the remain seats
		 * 
		 */

		public class CheckThread implements Runnable {
			private boolean flag = true;

			/**
			 * Method setFlag, It is the setter of variable "flag"
			 * 
			 * @param flag
			 *            It is to set the value of the flag
			 */

			public void setFlag(boolean flag) {
				this.flag = flag;
			}

			/**
			 * Method run, Override the method run to achieve check the movie's
			 * status in a set period
			 */

			public void run() {
				while (true) {
					ArrayList<String> seatList = new FileOp().readFile("./screens/" + infoDup.get("screen") + "/"
							+ infoDup.get("movie") + "/" + infoDup.get("time") + "/seat.txt");

					seatBool = new boolean[seatList.size()];
					for (int i = 0; i < seatList.size(); i++) {
						seatBool[i] = Boolean.parseBoolean(seatList.get(i));
					}

					int j = 0;
					ImageIcon image = null;
					if (flag) {

						for (int i = 8; i > 0; i--) {

							if (seatBool[8 - i]) {
								image = new ImageIcon("./screens/img/" + i + ".jpg");
								buttonList.get(8 - i).setIcon(image);
							} else {
								image = new ImageIcon("./screens/img/" + i + "_sold.jpg");
								buttonList.get(8 - i).setIcon(image);
								buttonList.get(8 - i).removeActionListener(listener);
							}
						}

						for (int i = 0; i < seatBool.length - 8; i++) {
							j = 6 - i % 6;
							if (seatBool[i + 8]) {
								image = new ImageIcon("./screens/img/" + j + ".jpg");
								buttonList.get(i + 8).setIcon(image);

								if (buttonList.get(i).getActionListeners() == null)
									buttonList.get(i).addActionListener(listener);
							} else {
								image = new ImageIcon("./screens/img/" + j + "_sold.jpg");
								buttonList.get(i + 8).setIcon(image);
								buttonList.get(i + 8).removeActionListener(listener);
							}
						}
					}

					// 判断在选择座位时 所有座位被别人选走, 返回第二层
					// If all the seat has been chose by other user, it will
					// return the secondlayer
					boolean back_flag = true;
					for (int i = 0; i < seatBool.length; i++) {
						if (seatBool[i]) {
							back_flag = false;
							break;
						}
						if (!back_flag)
							break;
					}

					if (back_flag) {
						JOptionPane.showMessageDialog(null, "Sorry! You are so slow! All seats have been sold out!",
								"Warning", JOptionPane.WARNING_MESSAGE);
						outerClass.dispose();
						if (infoDup.containsKey("studentId"))
							infoDup.remove("studentId");
						infoDup.remove("type");
						new SecondLayer(secondLayerInfoDup);
						break;
					}

					try {
						// 每一秒检查一次
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						break;
						// e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * Inner class, used to generate the screen3's seat graph
	 */

	public class Screen3 extends JFrame {
		ArrayList<JButton> buttonList = new ArrayList<JButton>();
		Map<String, String> infoDup;
		SimpleListener listener = new SimpleListener();
		Thread thread = null;
		CheckThread checker = new CheckThread();
		JFrame outerClass = null;
		JButton back;
		JLabel title;

		boolean seatBool[] = null;
		List<Map<String, String>> secondLayerInfoDup;

		/**
		 * Override the default constructor In the fourth layer, and generate
		 * the screen3's graph
		 * 
		 * @param info
		 *            The mapping of keys and values which contains the user's
		 *            choice
		 * 
		 * @param secondLayerInfo
		 *            Used to return back to the second layer
		 */

		public Screen3(Map<String, String> info, List<Map<String, String>> secondLayerInfo) {
			infoDup = info;
			secondLayerInfoDup = secondLayerInfo;
			outerClass = this; // 由于内部类要调用外部类的实例,所以this无法使用,必须生成变量来调用;
								// 与单独生成actionlistener类相反(两种方法解决内部类调用外部类实例的问题)

			back = new JButton();
			back.setIcon(new ImageIcon("./movies/info/img/back.jpg"));
			back.setBounds(0, 0, 50, 30);
			back.addActionListener(listener);
			this.getContentPane().add(back);

			title = new JLabel("Please Select Seat", JLabel.CENTER);
			title.setBounds(0, 0, 500 + 10, 30);
			title.setBorder(BorderFactory.createLineBorder(Color.black));
			this.getContentPane().add(title);

			thread = new Thread(checker);

			// 先进行检查,生成图形界面后再启动多线程,以防止延时出现的exception
			ArrayList<String> seatList = new FileOp().readFile(
					"./screens/" + info.get("screen") + "/" + info.get("movie") + "/" + info.get("time") + "/seat.txt");

			seatBool = new boolean[seatList.size()];
			for (int i = 0; i < seatList.size(); i++) {
				seatBool[i] = Boolean.parseBoolean(seatList.get(i));
			}

			JLabel tmpL = null;
			JButton tmpB = null;

			// 最上面一排
			for (int i = 8; i > 0; i--) {
				if (i == 8) {
					tmpL = new JLabel("E", JLabel.CENTER);
					tmpL.setBounds(0, 20 + 30, 40, 50);
					this.getContentPane().add(tmpL);
				}
				tmpB = new JButton();
				ImageIcon image = null;
				if (seatBool[8 - i]) {
					image = new ImageIcon("./screens/img/" + i + ".jpg");
					tmpB.setIcon(image);
					tmpB.setBounds(40 + 10 + (8 - i) * 50, 20 + 30, 50, 50);
					tmpB.addActionListener(listener);
					buttonList.add(tmpB);
				} else {
					image = new ImageIcon("./screens/img/" + i + "_sold.jpg");
					tmpB.setIcon(image);
					tmpB.setBounds(40 + 10 + (8 - i) * 50, 20 + 30, 50, 50);
					buttonList.add(tmpB);
				}
				this.getContentPane().add(buttonList.get(8 - i));
				if (i == 1) {
					tmpL = new JLabel("E", JLabel.CENTER);
					tmpL.setBounds(460, 20 + 30, 40, 50);
					this.getContentPane().add(tmpL);
				}
			}

			// 其他三排
			int j = 0;

			for (int i = 0; i < seatBool.length - 8; i++) {
				j = 6 - i % 6;
				if (j == 6) {
					tmpL = new JLabel("" + (char) ('D' - i / 6), JLabel.CENTER);
					tmpL.setBounds(0, 20 + 90 + (i / 6) * 90 + 30, 40, 50);
					this.getContentPane().add(tmpL);
				}
				tmpB = new JButton();
				ImageIcon image = null;
				if (seatBool[i + 8]) {
					image = new ImageIcon("./screens/img/" + j + ".jpg");
					tmpB.setIcon(image);
					if (j >= 5)
						tmpB.setBounds(40 + (6 - j) * 50, 20 + 90 + (i / 6) * 90 + 30, 50, 50);
					else if (j >= 3)
						tmpB.setBounds(40 + 60 + (6 - j) * 50, 20 + 90 + (i / 6) * 90 + 30, 50, 50);
					else
						tmpB.setBounds(40 + 60 * 2 + (6 - j) * 50, 20 + 90 + (i / 6) * 90 + 30, 50, 50);
					tmpB.addActionListener(listener);
					buttonList.add(tmpB);
				} else {
					image = new ImageIcon("./screens/img/" + j + "_sold.jpg");
					tmpB.setIcon(image);
					if (j >= 5)
						tmpB.setBounds(40 + (6 - j) * 50, 20 + 90 + (i / 6) * 90 + 30, 50, 50);
					else if (j >= 3)
						tmpB.setBounds(40 + 60 + (6 - j) * 50, 20 + 90 + (i / 6) * 90 + 30, 50, 50);
					else
						tmpB.setBounds(40 + 60 * 2 + (6 - j) * 50, 20 + 90 + (i / 6) * 90 + 30, 50, 50);
					buttonList.add(tmpB);
				}
				this.getContentPane().add(buttonList.get(i + 8));
				if (j == 1) {
					tmpL = new JLabel("" + (char) ('D' - i / 6), JLabel.CENTER);
					tmpL.setBounds(460, 20 + 90 + (i / 6) * 90 + 30, 40, 50);
					this.getContentPane().add(tmpL);
				}
			}

			tmpL = new JLabel("SCREEN", JLabel.CENTER);
			tmpL.setOpaque(true);
			tmpL.setBackground(new Color(153, 153, 153));
			// 这么写也可以
			// tmpL.setBackground(Color.GRAY);
			tmpL.setBounds(90, 470 + 30, 320, 20);
			this.getContentPane().add(tmpL);
			this.setLayout(null);

			int windowWidth = 500 + 10;
			int windowHeight = 510 + 30 + 30;

			this.setTitle("SCREEN3");
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setSize(windowWidth, windowHeight);
			int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
			int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
			this.setLocation((screenWidth - windowWidth) / 2, (screenHeight - windowHeight) / 2);
			this.setResizable(false);
			this.setVisible(true);

			// thread.start();
		}

		/**
		 * Inner class, used to generate stand alone action listener which will
		 * used because of the multithread
		 * 
		 */

		// 单独的actionlistener类
		public class SimpleListener implements ActionListener {
			/**
			 * Method actionPerformed, Override the method to handle the clicked
			 * action from the button
			 * 
			 * @param e
			 *            the ActionEvent cause from the Panel
			 */
			@Override
			public void actionPerformed(ActionEvent e) {

				// 如果点击了座位,则停止多线程以免造成图标的更换
				// 可用synchronized wait notify解决(高级方法),但出现问题,等待解决
				checker.setFlag(false);

				JButton eventSource = (JButton) e.getSource();

				if (eventSource.equals(back)) {
					outerClass.dispose();
					thread.interrupt();
					if (infoDup.containsKey("id"))
						infoDup.remove("id");
					infoDup.remove("type");
					new ThirdLayer(infoDup, secondLayerInfoDup);
				}

				ImageIcon image = null;
				for (int i = 0; i < buttonList.size(); i++) {
					if (eventSource.equals(buttonList.get(i))) {
						if (i < 8)
							image = new ImageIcon("./screens/img/" + (8 - i % 8) + "_sold.jpg");
						else
							image = new ImageIcon("./screens/img/" + (6 - (i - 8) % 6) + "_sold.jpg");
						buttonList.get(i).setIcon(image);
						buttonList.get(i).removeActionListener(this);

						String msg = "";
						if (i < 8)
							msg = "<html>Your choice is: Seat " + (char) ('E' - i / 8) + (8 - i % 8)
									+ "<br>Confirm to purchase?</html>";
						else
							msg = "<html>Your choice is: Seat " + (char) ('D' - (i - 8) / 6) + (6 - (i - 8) % 6)
									+ "<br>Confirm to purchase?</html>";
						int choice = JOptionPane.showConfirmDialog(null, msg, "Confirm to purchase",
								JOptionPane.YES_NO_OPTION);

						// no:1 yes:0 cancel:-1
						if (choice == 0) {
							infoDup.put("seat", "" + i);
							new FifthLayer(infoDup, secondLayerInfoDup);
							outerClass.dispose();
							thread.interrupt();
						} else {
							if (i < 8)
								image = new ImageIcon("./screens/img/" + (8 - i % 8) + ".jpg");
							else
								image = new ImageIcon("./screens/img/" + (6 - (i - 8) % 6) + ".jpg");
							buttonList.get(i).setIcon(image);
							buttonList.get(i).addActionListener(this);
							checker.setFlag(true);
							break;
						}
						// buttonList.get(i).setEnabled(false);
					}
				}
			}
		}

		/**
		 * Inner class as a checker to check the remain seats
		 * 
		 */

		public class CheckThread implements Runnable {
			private boolean flag = true;

			/**
			 * Method setFlag, It is the setter of variable "flag"
			 * 
			 * @param flag
			 *            It is to set the value of the flag
			 */

			public void setFlag(boolean flag) {
				this.flag = flag;
			}

			/**
			 * Method run, Override the method run to achieve check the movie's
			 * status in a set period
			 */

			public void run() {
				while (true) {
					ArrayList<String> seatList = new FileOp().readFile("./screens/" + infoDup.get("screen") + "/"
							+ infoDup.get("movie") + "/" + infoDup.get("time") + "/seat.txt");

					seatBool = new boolean[seatList.size()];
					for (int i = 0; i < seatList.size(); i++) {
						seatBool[i] = Boolean.parseBoolean(seatList.get(i));
					}

					int j = 0;
					ImageIcon image = null;
					if (flag) {
						for (int i = 8; i > 0; i--) {
							if (seatBool[8 - i]) {
								image = new ImageIcon("./screens/img/" + i + ".jpg");
								buttonList.get(8 - i).setIcon(image);

								if (buttonList.get(i).getActionListeners() == null)
									buttonList.get(i).addActionListener(listener);
							} else {
								image = new ImageIcon("./screens/img/" + i + "_sold.jpg");
								buttonList.get(8 - i).setIcon(image);
								buttonList.get(8 - i).removeActionListener(listener);
							}
						}

						for (int i = 0; i < seatBool.length - 8; i++) {
							j = 6 - i % 6;
							if (seatBool[i + 8]) {
								image = new ImageIcon("./screens/img/" + j + ".jpg");
								buttonList.get(i + 8).setIcon(image);
							} else {
								image = new ImageIcon("./screens/img/" + j + "_sold.jpg");
								buttonList.get(i + 8).setIcon(image);
								buttonList.get(i + 8).removeActionListener(listener);
							}
						}
					}

					// 判断在选择座位时 所有座位被别人选走, 返回第二层
					// If all the seat has been chose by other user, it will
					// return the secondlayer
					boolean back_flag = true;
					for (int i = 0; i < seatBool.length; i++) {
						if (seatBool[i]) {
							back_flag = false;
							break;
						}
						if (!back_flag)
							break;
					}

					if (back_flag) {
						JOptionPane.showMessageDialog(null, "Sorry! You are so slow! All seats have been sold out!",
								"Warning", JOptionPane.WARNING_MESSAGE);
						outerClass.dispose();
						if (infoDup.containsKey("studentId"))
							infoDup.remove("studentId");
						infoDup.remove("type");
						new SecondLayer(secondLayerInfoDup);
						break;
					}

					try {
						// 每一秒检查一次
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						break;
						// e.printStackTrace();
					}
				}
			}
		}
	}
}
