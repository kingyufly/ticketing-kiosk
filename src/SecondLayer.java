
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

		// 用于显示每个时间的剩余票数
		// Used to show the remain ticket for each time
		buttonRemainList = new ArrayList<ArrayList<String>>();

		buttonPanelList = new ArrayList<JPanel>();
		labelList = new ArrayList<JLabel>();
		screenList = new ArrayList<String>();

		// 添加返回按键
		back = new JButton();
		// button设定图片
		back.setIcon(new ImageIcon("./movies/info/img/back.jpg"));
		// 采用固定layout
		back.setBounds(0, 0, 50, 30);
		back.addActionListener(this);
		this.getContentPane().add(back);

		// 添加提示label
		title = new JLabel("Please Select Screen and Time", JLabel.CENTER);
		title.setBounds(0, 0, 400, 30);
		// 为label添加黑色边框
		// Add blackborder to the label
		title.setBorder(BorderFactory.createLineBorder(Color.black));
		this.getContentPane().add(title);

		// 在生成图形界面之前,先检查电影的信息
		// 在生成图形界面后,再启动多线程,防止出现exception
		// Check the status of the time in case of any exceptions during the
		// multiThread part

		movieBool_soldout = new MovieCheck().CheckScreenSoldout(movieInfoListTrans);
		movieBool_timeout = new MovieCheck().CheckScreenTime(movieInfoListTrans);
		buttonRemainList = new MovieCheck().CheckScreenTicketRemain(movieInfoListTrans);

		int comHeight = 0;
		int comWidth = 400;

		// 循环除去最后一个Map<String,String>的所有Map(List中),即循环所有screen,最后一个Map中存储着1."movie":(文件/文件夹名)
		// 2."moviename":(电影名称)
		// Remove the last map from all the information since it stores the
		// movie's name and its filename
		for (int i = 0; i < screenSize; i++) {
			screenList.add(movieInfoList.get(i).get("screen").toUpperCase());
			// 先移除screen键值对,以便于后面循环获取时间
			// Remove the screen key-value
			movieInfoList.get(i).remove("screen");
			// 获取每个panel的高(即每个screen中场次的行数)
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

			// HashMap是根据key的hash值来排列的,所以无法直接进行排序
			// 对HashMap进行排序,使得按键按时间排列
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
			// 添加每个screen对应的场次(时间)
			for (Map.Entry<String, String> mapping : list) {
				String key = mapping.getKey();

				// 如果没有超时/售罄,则显示剩余票数
				// 具体思路见firstlayer注释
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

			// 每个panel采用固定的layout
			JPanel tmp = new JPanel();
			comHeight += 30;
			tmp.setBounds(0, comHeight + 30, comWidth, 45 * height[i]);
			for (j = 0; j < buttonList.get(i).size(); j++) {
				// 但其中的buttons采用gridlayout(方便)
				tmp.add(buttonList.get(i).get(j));
				tmp.setLayout(new GridLayout(height[i], 2, 0, 0));
			}

			this.getContentPane().add(labelList.get(i));
			this.getContentPane().add(tmp);
		}

		// 上面移除screen后 再添加进去
		for (int i = 0; i < screenSize; i++) {
			movieInfoList.get(i).put("screen", screenList.get(i).toLowerCase());
		}
		// 由于采用固定layout,所以默认panel的layout必须为null
		this.setLayout(null);

		// 窗口的计算宽和高
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

		// 在生成图形界面后再使用多线程检查status
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
		// 如果点击了返回按键,则返回firstlayer
		if (eventSource.equals(back)) {
			this.dispose();
			new FirstLayer();
			// 停止当前多线程
			thread.interrupt();
		}
		// 循环判断哪个button被点了
		for (int i = 0; i < buttonList.size(); i++) {
			for (int j = 0; j < buttonList.get(i).size(); j++) {
				if (eventSource.equals(buttonList.get(i).get(j))) {
					// 如果button被点击,则将获得的参数放入键值对中,传递至thirdlayer
					// If the button is clicked, generate the corresponding
					// key-value pair and add them to the list
					Map<String, String> tmp = new HashMap<String, String>();
					// 增加键值对
					// 电影的文件名
					tmp.put("movie", movieInfoListTrans.get(movieInfoListTrans.size() - 1).get("movie"));
					// 电影的名称(不同于文件名)
					tmp.put("moviename", this.getTitle());
					// 哪个screen
					tmp.put("screen", screenList.get(i).toLowerCase());
					// screen中的哪个场次
					tmp.put("time", eventSource.getText().substring(6, 8) + eventSource.getText().substring(9, 11));

					this.dispose();
					new ThirdLayer(tmp, movieInfoListTrans);
					// 跳转页面时,使线程中断,在中断处理时结束线程(thread.stop()不推荐使用)
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
				// 生成储存状态的数组
				// Stores the status of the screen time
				movieBool_soldout = new MovieCheck().CheckScreenSoldout(movieInfoListTrans);
				movieBool_timeout = new MovieCheck().CheckScreenTime(movieInfoListTrans);
				buttonRemainList = new MovieCheck().CheckScreenTicketRemain(movieInfoListTrans);

				if (buttonList.size() == 0) {
				} else {
					int beginIndex = 0;
					int endIndex = 0;

					// 判断/检查每个screen以及其中的场次/票数信息,以确定buttons的状态(可选/不可选,以及button上的文字信息)
					// 思路同firstlayer部分
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
							} else if (!movieBool_soldout[i][j] && movieBool_timeout[i][j]) { // 票售罄
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
									|| (!movieBool_soldout[i][j] && !movieBool_timeout[i][j])) { // 票超时
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

				// 如果在选择时,全部screen中的全部场次sold out或time out,则进行提示并自动返回firstlayer
				// 判断选screen(场次)的票全部被别人买走的情况
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

				// 判断选screen(场次)的全部超时的情况
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

				// 如果选择的电影在2nd Layer选择时全部售完或全部超时,则返回选择电影
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
					// 每0.1秒检查一次
					// Check the status per 0.1s
					Thread.sleep(100);
				} catch (InterruptedException e) {
					break;
				}
			}
		}
	}
}