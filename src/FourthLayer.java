
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
		// �����ڶ��߳���ʹ����removeactionlistener(),���Բ���ʹ��addactionlistener(this),����Ҫ����actionlistener��ʵ��
		// ���ʹ��jbutton.setenable()��򵥵ö�
		// outerclassͬ��
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

			outerClass = this; // �����ڲ���Ҫ�����ⲿ���ʵ��,����this�޷�ʹ��,�������ɱ���������;
								// �뵥������actionlistener���෴(���ַ�������ڲ�������ⲿ��ʵ��������)

			// ���back�Լ���ʾ����
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

			// �Ƚ��м��,����ͼ�ν�������������߳�,�Է�ֹ��ʱ���ֵ�exception
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
			// ������λͼ��(button)�Լ���Ӧ���б��(D,C,B,A)
			// Generate the seat label(button) and its corresponding
			// index(D,C,B,A)
			for (int i = 0; i < seatBool.length; i++) {
				j = 8 - i % 8;
				// ���������ߵ�,������б��
				// If the label is on the left or right, then the label is the
				// index label
				if (j == 8) {
					tmpL = new JLabel("" + (char) ('D' - i / 8), JLabel.CENTER);
					tmpL.setBounds(0, 20 + (i / 8) * 90 + 30, 40, 50);
					this.getContentPane().add(tmpL);
				}
				// �����λ(ͨ���ϱ߻�ȡ��seat״̬��ȷ��seat��ͼ����ɫ,��ɫΪδ�۳�,��ɫΪ�۳�(���Ե��,���޷�Ӧ))
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
				// ��������ұߵ�,������б��
				if (j == 1) {
					tmpL = new JLabel("" + (char) ('D' - i / 8), JLabel.CENTER);
					tmpL.setBounds(490, 20 + (i / 8) * 90 + 30, 40, 50);
					this.getContentPane().add(tmpL);
				}
			}

			// ����screen(ӫĻ)��ͼ��,����ɫ��Ϊ��ɫ
			// Generate label stand for the screen in color green
			tmpL = new JLabel("SCREEN", JLabel.CENTER);
			// �������趨��͸��,�����趨����ɫ
			tmpL.setOpaque(true);
			// �趨Ϊ��ɫ(153,153,153)
			tmpL.setBackground(new Color(153, 153, 153));
			// ��ôдҲ����
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
			// ���ɶ��߳�,����seat��״̬
			// Start the multiThread to check the status of the seat
			thread.start();
		}

		/**
		 * Inner class, used to generate stand alone action listener which will
		 * used because of the multithread
		 * 
		 */
		// ������actionlistener��
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
				// ����������λ,��ֹͣ���߳��������ͼ��ĸ���
				// ����synchronized wait notify���(�߼�����),����������,�ȴ����(�ѽ��)
				// If the seat is clicked, then stop the multiThread in order to
				// avoid the changing of the seat's image during the confirm of
				// the
				// seat
				checker.setFlag(false);

				// ������˷���,���Ƴ���ӵ�third����ӵ�type(�����student ticket����id),�ٷ��ص�����
				// If back button is clicked, then return to third layer
				JButton eventSource = (JButton) e.getSource();

				if (eventSource.equals(back)) {
					// ��Ϊactionlistenerʹ���˵�������,����Ҫ�ⲿ��(GUI).dispose
					outerClass.dispose();
					// ֹͣ���߳�
					thread.interrupt();
					if (infoDup.containsKey("id"))
						infoDup.remove("id");
					infoDup.remove("type");
					new ThirdLayer(infoDup, secondLayerInfoDup);
				}

				// ������������λ�İ���
				// if the seat's button is clicked, then ask the user to confirm
				// the seat
				ImageIcon image = null;
				for (int i = 0; i < buttonList.size(); i++) {
					if (eventSource.equals(buttonList.get(i))) {
						// ��������,���Ƚ���λ���,ֹͣ�����¼�
						// If the seat is clicked, change the seat to red and
						// remove the action listener
						image = new ImageIcon("./screens/img/" + (8 - i % 8) + "_sold.jpg");

						buttonList.get(i).setIcon(image);
						buttonList.get(i).removeActionListener(this);

						// ������ʾ����ʾ�û��Ƿ�ȷ�Ϲ���
						// Ask the user to confirm the selection of the seat
						int choice = JOptionPane.showConfirmDialog(null,
								"<html>Your choice is: Seat " + (char) ('D' - i / 8) + (8 - i % 8)
										+ "<br>Confirm to purchase?</html>",
								"Confirm to purchase", JOptionPane.YES_NO_OPTION);

						// ����ǵĻ�����fifthlayerȷ�ϸ���
						// no:1 yes:0 cancel:-1
						if (choice == 0) {
							infoDup.put("seat", "" + i);
							new FifthLayer(infoDup, secondLayerInfoDup);
							outerClass.dispose();
							thread.interrupt();
						} else {
							// ���ȡ���Ļ�,��ָ���ɫͼ��,���Ӽ���,�������߳�
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
					// �ȶ�ȡseat�ĵ�����ȷ����λ�Ƿ��۳�
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
								// ���δ�۳�(true),�����,�����actionlistener(�Է��ֶ�������λ��)
								// If the seat has not been sold, change the
								// color to blue and add actionlistener to it
								image = new ImageIcon("./screens/img/" + j + ".jpg");
								buttonList.get(i).setIcon(image);
								// ���ֶ�����Ʊ��ʱ,���Ը�����λͼ
								if (buttonList.get(i).getActionListeners() == null)
									buttonList.get(i).addActionListener(listener);
							} else {
								// ����۳�(false),����,���Ƴ�actionlistener
								image = new ImageIcon("./screens/img/" + j + "_sold.jpg");
								buttonList.get(i).setIcon(image);
								buttonList.get(i).removeActionListener(listener);
							}
						}
					}

					// �ж���ѡ����λʱ ������λ������ѡ��, ���صڶ���
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
						// ÿһ����һ��
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
			outerClass = this; // �����ڲ���Ҫ�����ⲿ���ʵ��,����this�޷�ʹ��,�������ɱ���������;
								// �뵥������actionlistener���෴(���ַ�������ڲ�������ⲿ��ʵ��������)

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

			// �Ƚ��м��,����ͼ�ν�������������߳�,�Է�ֹ��ʱ���ֵ�exception
			ArrayList<String> seatList = new FileOp().readFile(
					"./screens/" + info.get("screen") + "/" + info.get("movie") + "/" + info.get("time") + "/seat.txt");

			seatBool = new boolean[seatList.size()];
			for (int i = 0; i < seatList.size(); i++) {
				seatBool[i] = Boolean.parseBoolean(seatList.get(i));
			}

			JLabel tmpL = null;
			JButton tmpB = null;

			// ������������һ��(8����λ)
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

			// ��������������(6����λ)
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
			// ��ôдҲ����
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
		// ������actionlistener��
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

				// ����������λ,��ֹͣ���߳��������ͼ��ĸ���
				// ����synchronized wait notify���(�߼�����),����������,�ȴ����
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

					// �ж���ѡ����λʱ ������λ������ѡ��, ���صڶ���
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
						// ÿһ����һ��
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
			outerClass = this; // �����ڲ���Ҫ�����ⲿ���ʵ��,����this�޷�ʹ��,�������ɱ���������;
								// �뵥������actionlistener���෴(���ַ�������ڲ�������ⲿ��ʵ��������)

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

			// �Ƚ��м��,����ͼ�ν�������������߳�,�Է�ֹ��ʱ���ֵ�exception
			ArrayList<String> seatList = new FileOp().readFile(
					"./screens/" + info.get("screen") + "/" + info.get("movie") + "/" + info.get("time") + "/seat.txt");

			seatBool = new boolean[seatList.size()];
			for (int i = 0; i < seatList.size(); i++) {
				seatBool[i] = Boolean.parseBoolean(seatList.get(i));
			}

			JLabel tmpL = null;
			JButton tmpB = null;

			// ������һ��
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

			// ��������
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
			// ��ôдҲ����
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

		// ������actionlistener��
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

				// ����������λ,��ֹͣ���߳��������ͼ��ĸ���
				// ����synchronized wait notify���(�߼�����),����������,�ȴ����
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

					// �ж���ѡ����λʱ ������λ������ѡ��, ���صڶ���
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
						// ÿһ����һ��
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
