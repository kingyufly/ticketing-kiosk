
/**
 * SecondLayer.java
 * The 2rd Layer of the GUI
 * Show the movie's screen and time information
 * Check the status of the movie in a set period
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
import java.util.*;
import java.util.Map.Entry;
import javax.swing.*;

/**
 * The 2st Layer GUI class
 */

public class SecondLayer extends JFrame implements ActionListener {
	ArrayList<ArrayList<JButton>> buttonList;
	ArrayList<ArrayList<String>> buttonRemainList;
	ArrayList<JPanel> buttonPanelList;
	ArrayList<JLabel> labelList;
	ArrayList<String> screenList;
	List<Map<String, String>> movieInfoListTrans;

	JFrame thisDup;
	JButton back;
	JLabel title;

	volatile boolean movieBool_soldout[][];
	volatile boolean movieBool_timeout[][];

	Thread thread;
	boolean buttonBool[][];

	/**
	 * Override the default constructor In the second layer, the screen's
	 * information and the remain ticket number and the time will be displayed
	 * 
	 * @param movieInfoList
	 *            The movie's information about the screen, time, ticket number
	 */

	public SecondLayer(List<Map<String, String>> movieInfoList) {
		movieInfoListTrans = movieInfoList;
		thisDup = this;

		int screenSize = movieInfoList.size() - 1;
		this.setTitle(movieInfoList.get(screenSize).get("moviename"));
		int height[] = new int[screenSize];

		buttonList = new ArrayList<ArrayList<JButton>>();

		// ������ʾÿ��ʱ���ʣ��Ʊ��
		// Used to show the remain ticket for each time
		buttonRemainList = new ArrayList<ArrayList<String>>();

		buttonPanelList = new ArrayList<JPanel>();
		labelList = new ArrayList<JLabel>();
		screenList = new ArrayList<String>();

		// ��ӷ��ذ���
		back = new JButton();
		// button�趨ͼƬ
		back.setIcon(new ImageIcon("./movies/info/img/back.jpg"));
		// ���ù̶�layout
		back.setBounds(0, 0, 50, 30);
		back.addActionListener(this);
		this.getContentPane().add(back);

		// �����ʾlabel
		title = new JLabel("Please Select Screen and Time", JLabel.CENTER);
		title.setBounds(0, 0, 400, 30);
		// Ϊlabel��Ӻ�ɫ�߿�
		// Add blackborder to the label
		title.setBorder(BorderFactory.createLineBorder(Color.black));
		this.getContentPane().add(title);

		// ������ͼ�ν���֮ǰ,�ȼ���Ӱ����Ϣ
		// ������ͼ�ν����,���������߳�,��ֹ����exception
		// Check the status of the time in case of any exceptions during the
		// multiThread part

		movieBool_soldout = new MovieCheck().CheckScreenSoldout(movieInfoListTrans);
		movieBool_timeout = new MovieCheck().CheckScreenTime(movieInfoListTrans);
		buttonRemainList = new MovieCheck().CheckScreenTicketRemain(movieInfoListTrans);

		int comHeight = 0;
		int comWidth = 400;

		// ѭ����ȥ���һ��Map<String,String>������Map(List��),��ѭ������screen,���һ��Map�д洢��1."movie":(�ļ�/�ļ�����)
		// 2."moviename":(��Ӱ����)
		// Remove the last map from all the information since it stores the
		// movie's name and its filename
		for (int i = 0; i < screenSize; i++) {
			screenList.add(movieInfoList.get(i).get("screen").toUpperCase());
			// ���Ƴ�screen��ֵ��,�Ա��ں���ѭ����ȡʱ��
			// Remove the screen key-value
			movieInfoList.get(i).remove("screen");
			// ��ȡÿ��panel�ĸ�(��ÿ��screen�г��ε�����)
			// Calculate the height of each panel according to the times value
			height[i] = (int) Math.ceil((movieInfoList.get(i).size()) / 2.0);
		}

		for (int i = 0; i < screenSize; i++) {
			if (i != 0)
				comHeight += height[i - 1] * 45;
			labelList.add(new JLabel(screenList.get(i), JLabel.CENTER));
			labelList.get(i).setBounds(0, comHeight + 30, comWidth, 30);
			// labelList.get(i).setBorder(BorderFactory.createLineBorder(Color.black));
			buttonList.add(new ArrayList<JButton>());

			// HashMap�Ǹ���key��hashֵ�����е�,�����޷�ֱ�ӽ�������
			// ��HashMap��������,ʹ�ð�����ʱ������
			// Hashmap is sort by the has value of the key, which cannot be sort
			// by other method
			// Getting the value of the key-value pair and sort by time
			List<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String, String>>(
					movieInfoList.get(i).entrySet());
			Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
				// Sort descend
				@Override
				public int compare(Entry<String, String> entry1, Entry<String, String> entry2) {
					return entry1.getKey().compareTo(entry2.getKey());
				}
			});

			int j = 0;
			// ���ÿ��screen��Ӧ�ĳ���(ʱ��)
			for (Map.Entry<String, String> mapping : list) {
				String key = mapping.getKey();

				// ���û�г�ʱ/����,����ʾʣ��Ʊ��
				// ����˼·��firstlayerע��
				if (movieBool_soldout[i][j] && movieBool_timeout[i][j]) {
					buttonList.get(i).add(new JButton("<html>" + key.substring(0, 2) + ":" + key.substring(2, 4) + "("
							+ buttonRemainList.get(i).get(j) + " remain)</html>"));
					buttonList.get(i).get(buttonList.get(i).size() - 1).setEnabled(true);
				} else if (!movieBool_soldout[i][j] && movieBool_timeout[i][j]) {
					buttonList.get(i).add(new JButton(
							"<html><s>" + key.substring(0, 2) + ":" + key.substring(2, 4) + "</s><br>SOLD OUT</html>"));
					buttonList.get(i).get(buttonList.get(i).size() - 1).setEnabled(false);
				} else if ((movieBool_soldout[i][j] && !movieBool_timeout[i][j])
						|| (!movieBool_soldout[i][j] && !movieBool_timeout[i][j])) {
					buttonList.get(i).add(new JButton("<html><s>" + key.substring(0, 2) + ":" + key.substring(2, 4)
							+ "</s><br>UNAVAILABLE</html>"));
					buttonList.get(i).get(buttonList.get(i).size() - 1).setEnabled(false);
				}
				buttonList.get(i).get(buttonList.get(i).size() - 1).addActionListener(this);
				j++;
			}

			// ÿ��panel���ù̶���layout
			JPanel tmp = new JPanel();
			comHeight += 30;
			tmp.setBounds(0, comHeight + 30, comWidth, 45 * height[i]);
			for (j = 0; j < buttonList.get(i).size(); j++) {
				// �����е�buttons����gridlayout(����)
				tmp.add(buttonList.get(i).get(j));
				tmp.setLayout(new GridLayout(height[i], 2, 0, 0));
			}

			this.getContentPane().add(labelList.get(i));
			this.getContentPane().add(tmp);
		}

		// �����Ƴ�screen�� ����ӽ�ȥ
		for (int i = 0; i < screenSize; i++) {
			movieInfoList.get(i).put("screen", screenList.get(i).toLowerCase());
		}
		// ���ڲ��ù̶�layout,����Ĭ��panel��layout����Ϊnull
		this.setLayout(null);

		// ���ڵļ����͸�
		// Calculate the height and the width
		int windowWidth = comWidth;
		int windowHeight = screenSize * 30;
		for (int i = 0; i < height.length; i++) {
			windowHeight += height[i] * 45;
		}

		windowHeight += (30 + 30);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(windowWidth, windowHeight);
		int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
		int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
		this.setLocation((screenWidth - windowWidth) / 2, (screenHeight - windowHeight) / 2);
		this.setResizable(false);
		this.setVisible(true);

		// ������ͼ�ν������ʹ�ö��̼߳��status
		thread = new Thread(new CheckThread());
		thread.start();
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
		// �������˷��ذ���,�򷵻�firstlayer
		if (eventSource.equals(back)) {
			this.dispose();
			new FirstLayer();
			// ֹͣ��ǰ���߳�
			thread.interrupt();
		}
		// ѭ���ж��ĸ�button������
		for (int i = 0; i < buttonList.size(); i++) {
			for (int j = 0; j < buttonList.get(i).size(); j++) {
				if (eventSource.equals(buttonList.get(i).get(j))) {
					// ���button�����,�򽫻�õĲ��������ֵ����,������thirdlayer
					// If the button is clicked, generate the corresponding
					// key-value pair and add them to the list
					Map<String, String> tmp = new HashMap<String, String>();
					// ���Ӽ�ֵ��
					// ��Ӱ���ļ���
					tmp.put("movie", movieInfoListTrans.get(movieInfoListTrans.size() - 1).get("movie"));
					// ��Ӱ������(��ͬ���ļ���)
					tmp.put("moviename", this.getTitle());
					// �ĸ�screen
					tmp.put("screen", screenList.get(i).toLowerCase());
					// screen�е��ĸ�����
					tmp.put("time", eventSource.getText().substring(6, 8) + eventSource.getText().substring(9, 11));

					this.dispose();
					new ThirdLayer(tmp, movieInfoListTrans);
					// ��תҳ��ʱ,ʹ�߳��ж�,���жϴ���ʱ�����߳�(thread.stop()���Ƽ�ʹ��)
					thread.interrupt();
				}
			}
		}
	}

	/**
	 * Inner class as a checker to check the remain ticket and whether the movie
	 * is time out or sold out using multithread(implements Runnable)
	 * 
	 */

	public class CheckThread implements Runnable {

		/**
		 * Method run, Override the method run to achieve check the movie's
		 * status in a set period
		 */
		@Override
		public void run() {
			while (true) {
				// ���ɴ���״̬������
				// Stores the status of the screen time
				movieBool_soldout = new MovieCheck().CheckScreenSoldout(movieInfoListTrans);
				movieBool_timeout = new MovieCheck().CheckScreenTime(movieInfoListTrans);
				buttonRemainList = new MovieCheck().CheckScreenTicketRemain(movieInfoListTrans);

				if (buttonList.size() == 0) {
				} else {
					int beginIndex = 0;
					int endIndex = 0;

					// �ж�/���ÿ��screen�Լ����еĳ���/Ʊ����Ϣ,��ȷ��buttons��״̬(��ѡ/����ѡ,�Լ�button�ϵ�������Ϣ)
					// ˼·ͬfirstlayer����
					for (int i = 0; i < movieBool_soldout.length; i++) {
						for (int j = 0; j < movieBool_soldout[i].length; j++) {
							if (movieBool_soldout[i][j] && movieBool_timeout[i][j]) {
								buttonList.get(i).get(j).setEnabled(true);
								if (buttonList.get(i).get(j).getText().indexOf("<s>") == -1) {
								} else {
									beginIndex = buttonList.get(i).get(j).getText().indexOf("<s>");
									endIndex = buttonList.get(i).get(j).getText().indexOf("</s>");
									buttonList.get(i).get(j)
											.setText("<html>"
													+ buttonList.get(i).get(j).getText().substring(beginIndex + 3,
															endIndex)
													+ "(" + buttonRemainList.get(i).get(j) + " remain)</html>");
								}
							} else if (!movieBool_soldout[i][j] && movieBool_timeout[i][j]) { // Ʊ����
								buttonList.get(i).get(j).setEnabled(false);
								if (buttonList.get(i).get(j).getText().indexOf("<s>") == -1) {
									beginIndex = buttonList.get(i).get(j).getText().indexOf("<html>");
									endIndex = buttonList.get(i).get(j).getText().indexOf("(");
									buttonList.get(i).get(j).setText("<html><s>"
											+ buttonList.get(i).get(j).getText().substring(beginIndex + 6, endIndex)
											+ "</s><br>SOLD OUT</html>");
								} else {
									beginIndex = buttonList.get(i).get(j).getText().indexOf("<s>");
									endIndex = buttonList.get(i).get(j).getText().indexOf("</s>");
									buttonList.get(i).get(j).setText("<html><s>"
											+ buttonList.get(i).get(j).getText().substring(beginIndex + 3, endIndex)
											+ "</s><br>SOLD OUT</html>");
								}
							} else if ((movieBool_soldout[i][j] && !movieBool_timeout[i][j])
									|| (!movieBool_soldout[i][j] && !movieBool_timeout[i][j])) { // Ʊ��ʱ
								buttonList.get(i).get(j).setEnabled(false);
								if (buttonList.get(i).get(j).getText().indexOf("<s>") == -1) {
									beginIndex = buttonList.get(i).get(j).getText().indexOf("<html>");
									endIndex = buttonList.get(i).get(j).getText().indexOf("(");
									buttonList.get(i).get(j).setText("<html><s>"
											+ buttonList.get(i).get(j).getText().substring(beginIndex + 6, endIndex)
											+ "</s><br>UNAVAILABLE</html>");
								} else {
									beginIndex = buttonList.get(i).get(j).getText().indexOf("<s>");
									endIndex = buttonList.get(i).get(j).getText().indexOf("</s>");
									buttonList.get(i).get(j).setText("<html><s>"
											+ buttonList.get(i).get(j).getText().substring(beginIndex + 3, endIndex)
											+ "</s><br>UNAVAILABLE</html>");
								}
							} else {
							}
						}
					}
				}

				// �����ѡ��ʱ,ȫ��screen�е�ȫ������sold out��time out,�������ʾ���Զ�����firstlayer
				// �ж�ѡscreen(����)��Ʊȫ�����������ߵ����
				// If the ticket are sold out by other users, the program will
				// inform the user and back to the first layer
				boolean back_flag1 = true;
				for (int i = 0; i < movieBool_soldout.length; i++) {
					for (int j = 0; j < movieBool_soldout[i].length; j++) {
						if (movieBool_soldout[i][j]) {
							back_flag1 = false;
							break;
						}
					}
					if (!back_flag1)
						break;
				}

				// �ж�ѡscreen(����)��ȫ����ʱ�����
				// If the ticket are time out, the program will inform the user
				// and back to the first layer
				boolean back_flag2 = true;
				for (int i = 0; i < movieBool_timeout.length; i++) {
					for (int j = 0; j < movieBool_timeout[i].length; j++) {
						if (movieBool_timeout[i][j]) {
							back_flag2 = false;
							break;
						}
					}
					if (!back_flag2)
						break;
				}

				// ���ѡ��ĵ�Ӱ��2nd Layerѡ��ʱȫ�������ȫ����ʱ,�򷵻�ѡ���Ӱ
				if (back_flag1) {
					JOptionPane.showMessageDialog(null, "Sorry! You are so slow! All movies have been sold out!",
							"Warning", JOptionPane.WARNING_MESSAGE);
					thisDup.dispose();
					new FirstLayer();
					break;
				}

				if (back_flag2) {
					JOptionPane.showMessageDialog(null, "Sorry! You are so slow! All movies have started!", "Warning",
							JOptionPane.WARNING_MESSAGE);
					thisDup.dispose();
					new FirstLayer();
					break;
				}

				try {
					// ÿ0.1����һ��
					// Check the status per 0.1s
					Thread.sleep(100);
				} catch (InterruptedException e) {
					break;
				}
			}
		}
	}
}