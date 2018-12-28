
/**
 * Admin.java
 * Used to generate the report for the administrator
 * 
 * @author kingyufly
 * @version 2.2
 * 
 * */

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Admin {
	ArrayList<String> movieList;
	ArrayList<String> movieNameList;
	ArrayList<ArrayList<String>> ticketList;
	String type[] = { "Child Ticket", "Adult Ticket", "Senior Ticket", "Student Ticket" };

	JFrame addMoiveInfoFrame;
	JFrame addScreenInfoFrame;

	public Admin() {
		movieList = new ArrayList<String>();
		movieNameList = new ArrayList<String>();
		ticketList = new ArrayList<ArrayList<String>>();
	}

	// 添加电影信息,包括电影名,时长,电影图片名称
	// Add the movies' name, duration and images' filename

	/**
	 * method addMoiveInfo This method is to generate GUI for the administrator
	 * to add movie information and the screen and time information
	 * 
	 * @see Test.java
	 */
	public void addMoiveInfo() {
		Admin admin = this;
		addMoiveInfoFrame = new JFrame();

		int windowWidth;
		int windowHeight;

		// 文本输入框用于输入电影信息
		// For input the information of the movie
		ArrayList<JTextField> tfArray = new ArrayList<JTextField>();
		// 添加的电影信息会变成label显示
		// The added information will transformed into labels
		ArrayList<ArrayList<JLabel>> movieLabelArray = new ArrayList<ArrayList<JLabel>>();
		// 用于盛放电影信息
		// Stores movies information
		ArrayList<ArrayList<String>> movieInfo = new ArrayList<ArrayList<String>>();
		// 每个电影对应的删除按键
		// Each movie will have a corresponding remove button
		ArrayList<JButton> removeButtonArray = new ArrayList<JButton>();

		// 第一行信息提示
		JLabel movieName = new JLabel("Movie Name", JLabel.CENTER);
		JLabel movieLength = new JLabel("Duration(min)", JLabel.CENTER);
		JLabel moviePic = new JLabel("Picture's filename(.jpg)", JLabel.CENTER);

		// 最后一行按键,用以添加电影信息以及进行下一步添加时间信息
		JButton add = new JButton("Add");
		JButton next = new JButton("Next");

		// 每次点击add键,输入的信息会变成label,后边会跟随remove键用以删除添加的电影
		// Press the add button the input movie's information will be
		// transformed into labels
		add.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// The three parts of the information cannot be empty
				for (int i = 0; i < tfArray.size(); i++) {
					if (tfArray.get(i).getText().equals("") || (tfArray.get(i).getText() == null)) {
						// Show warning information
						JOptionPane.showMessageDialog(null, "Please fill in the information", "Warning",
								JOptionPane.WARNING_MESSAGE);
						// Set the cursor back into the empty textfield
						tfArray.get(i).requestFocus(true);
						return;
					}
				}

				// The time must be digits
				try {
					Integer.parseInt(tfArray.get(1).getText());
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "Duration must be digits", "Warning",
							JOptionPane.WARNING_MESSAGE);
					tfArray.get(1).requestFocus(true);
					return;
				}

				// Judge the duplicate movie name
				for (int i = 0; i < movieLabelArray.size(); i++) {
					if (tfArray.get(0).getText().equals(movieLabelArray.get(i).get(0).getText())) {
						JOptionPane.showMessageDialog(null, "Movie's name cannot be the same as added movies",
								"Warning", JOptionPane.WARNING_MESSAGE);
						tfArray.get(0).requestFocus(true);
						return;
					}
				}

				// 全部符合要求后,填入的信息会变为label,添加到panel上
				movieLabelArray.add(new ArrayList<JLabel>());
				for (int i = 0; i < 3; i++) {
					movieLabelArray.get(movieLabelArray.size() - 1)
							.add(new JLabel(tfArray.get(i).getText(), JLabel.CENTER));
					movieLabelArray.get(movieLabelArray.size() - 1)
							.get(movieLabelArray.get(movieLabelArray.size() - 1).size() - 1)
							.setBorder(BorderFactory.createLineBorder(Color.black));
					;
				}

				// 设置label的size和location
				// Set the lables' size and location
				movieLabelArray.get(movieLabelArray.size() - 1).get(0).setBounds(0,
						30 + (movieLabelArray.size() - 1) * 30, 100, 30);
				movieLabelArray.get(movieLabelArray.size() - 1).get(1).setBounds(100,
						30 + (movieLabelArray.size() - 1) * 30, 100, 30);
				movieLabelArray.get(movieLabelArray.size() - 1).get(2).setBounds(200,
						30 + (movieLabelArray.size() - 1) * 30, 150, 30);

				// 在添加后的电影信息后添加remove键,用来移除添加了的信息
				// Add remove button in the back of the labels to remove the
				// movie
				removeButtonArray.add(new JButton("remove"));
				removeButtonArray.get(removeButtonArray.size() - 1).setBounds(350,
						30 + (movieLabelArray.size() - 1) * 30, 90, 30);
				// 如果点击了remove,首先将label移除,其次其余的部分整体上移
				// If remove button is pressed, the label will be removed and
				// the rest of the labels and the textfield will move up
				removeButtonArray.get(removeButtonArray.size() - 1).addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						JButton eventSource = (JButton) e.getSource();
						for (int i = 0; i < removeButtonArray.size(); i++) {
							if (eventSource.equals(removeButtonArray.get(i))) {
								addMoiveInfoFrame.remove(movieLabelArray.get(i).get(0));
								addMoiveInfoFrame.remove(movieLabelArray.get(i).get(1));
								addMoiveInfoFrame.remove(movieLabelArray.get(i).get(2));
								addMoiveInfoFrame.remove(removeButtonArray.get(i));
								movieLabelArray.remove(i);
								removeButtonArray.remove(i);

								// 将添加了的电影(label)整体上移
								// Move all the labels
								if (i == movieLabelArray.size()) {
								} else if (movieLabelArray.size() != 0) {
									for (int j = i; j < movieLabelArray.size(); j++) {
										movieLabelArray.get(j).get(0).setBounds(
												movieLabelArray.get(j).get(0).getBounds().x,
												movieLabelArray.get(j).get(0).getBounds().y - 30,
												movieLabelArray.get(j).get(0).getBounds().width,
												movieLabelArray.get(j).get(0).getBounds().height);
										movieLabelArray.get(j).get(1).setBounds(
												movieLabelArray.get(j).get(1).getBounds().x,
												movieLabelArray.get(j).get(1).getBounds().y - 30,
												movieLabelArray.get(j).get(1).getBounds().width,
												movieLabelArray.get(j).get(1).getBounds().height);
										movieLabelArray.get(j).get(2).setBounds(
												movieLabelArray.get(j).get(2).getBounds().x,
												movieLabelArray.get(j).get(2).getBounds().y - 30,
												movieLabelArray.get(j).get(2).getBounds().width,
												movieLabelArray.get(j).get(2).getBounds().height);

										removeButtonArray.get(j).setBounds(removeButtonArray.get(j).getBounds().x,
												removeButtonArray.get(j).getBounds().y - 30,
												removeButtonArray.get(j).getBounds().width,
												removeButtonArray.get(j).getBounds().height);
									}
								}

								// 将输入框上移
								// Move all the textfield
								tfArray.get(0).setBounds(tfArray.get(0).getBounds().x,
										tfArray.get(0).getBounds().y - 30, tfArray.get(0).getBounds().width,
										tfArray.get(0).getBounds().height);
								tfArray.get(1).setBounds(tfArray.get(1).getBounds().x,
										tfArray.get(1).getBounds().y - 30, tfArray.get(1).getBounds().width,
										tfArray.get(1).getBounds().height);
								tfArray.get(2).setBounds(tfArray.get(2).getBounds().x,
										tfArray.get(2).getBounds().y - 30, tfArray.get(2).getBounds().width,
										tfArray.get(2).getBounds().height);

								// 将buttons上移
								// Move all the buttons
								add.setBounds(add.getBounds().x, add.getBounds().y - 30, add.getBounds().width,
										add.getBounds().height);

								next.setBounds(next.getBounds().x, next.getBounds().y - 30, next.getBounds().width,
										next.getBounds().height);

								// 重新设置window的大小(即window缩一行)
								// Resize the window
								int windowWidth = 450;
								int windowHeight = 30 + 30 + movieLabelArray.size() * 30 + 60;

								addMoiveInfoFrame.setSize(windowWidth, windowHeight);

								break;
							}
						}
					}
				});

				addMoiveInfoFrame.getContentPane().add(movieLabelArray.get(movieLabelArray.size() - 1).get(0));
				addMoiveInfoFrame.getContentPane().add(movieLabelArray.get(movieLabelArray.size() - 1).get(1));
				addMoiveInfoFrame.getContentPane().add(movieLabelArray.get(movieLabelArray.size() - 1).get(2));
				addMoiveInfoFrame.getContentPane().add(removeButtonArray.get(removeButtonArray.size() - 1));

				// Move downward the textfield
				tfArray.get(0).setBounds(0, 30 + (movieLabelArray.size()) * 30, 100, 30);
				tfArray.get(1).setBounds(100, 30 + (movieLabelArray.size()) * 30, 100, 30);
				tfArray.get(2).setBounds(200, 30 + (movieLabelArray.size()) * 30, 150, 30);

				// Clear all the textfield
				tfArray.get(0).setText("");
				tfArray.get(1).setText("");
				tfArray.get(2).setText("");

				// 将光标选中第一个输入框
				// Focus the first textfield with the cursor
				tfArray.get(0).requestFocus(true);

				// button下移
				// Move the buttons downward
				add.setBounds(5, 30 + 30 + movieLabelArray.size() * 30, 75, 30);
				next.setBounds(85, 30 + 30 + movieLabelArray.size() * 30, 75, 30);

				// window拉大(增加一行的高度)
				// Resize the window
				int windowWidth = 450;
				int windowHeight = 30 + 30 + movieLabelArray.size() * 30 + 60;

				addMoiveInfoFrame.setSize(windowWidth, windowHeight);
			}
		});

		// 如果点击next则将所有label上的信息赋到movieInfo中,并传至下一界面(添加时间场次信息)
		/*
		 * If next is clicked, all the information of the movies in the labels
		 * will be transfered to the next layer in order to add the screen and
		 * time information
		 */
		next.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (movieLabelArray.size() <= 0) {
					JOptionPane.showMessageDialog(null, "No film added! Please at least add one film!", "Warning",
							JOptionPane.WARNING_MESSAGE);
					tfArray.get(0).requestFocus(true);
					return;
				} else {
					addMoiveInfoFrame.dispose();
					for (int i = 0; i < movieLabelArray.size(); i++) {
						movieInfo.add(new ArrayList<String>());
						for (int j = 0; j < movieLabelArray.get(i).size(); j++) {
							movieInfo.get(i).add(movieLabelArray.get(i).get(j).getText());
						}
					}
					admin.addScreenInfo(movieInfo);
				}
			}
		});

		for (int i = 0; i < 3; i++) {
			tfArray.add(new JTextField(10));
		}

		// 设置大小
		movieName.setBounds(0, 0, 100, 30);
		movieLength.setBounds(100, 0, 100, 30);
		moviePic.setBounds(200, 0, 150, 30);

		// 描边
		movieName.setBorder(BorderFactory.createLineBorder(Color.black));
		movieLength.setBorder(BorderFactory.createLineBorder(Color.black));
		moviePic.setBorder(BorderFactory.createLineBorder(Color.black));

		// 设置大小
		tfArray.get(0).setBounds(0, 30 + movieLabelArray.size() * 30, 100, 30);
		tfArray.get(1).setBounds(100, 30 + movieLabelArray.size() * 30, 100, 30);
		tfArray.get(2).setBounds(200, 30 + movieLabelArray.size() * 30, 150, 30);

		add.setBounds(5, 30 + 30 + movieLabelArray.size() * 30, 75, 30);
		next.setBounds(85, 30 + 30 + movieLabelArray.size() * 30, 75, 30);

		addMoiveInfoFrame.getContentPane().add(movieName);
		addMoiveInfoFrame.getContentPane().add(movieLength);
		addMoiveInfoFrame.getContentPane().add(moviePic);

		addMoiveInfoFrame.getContentPane().add(tfArray.get(0));
		addMoiveInfoFrame.getContentPane().add(tfArray.get(1));
		addMoiveInfoFrame.getContentPane().add(tfArray.get(2));

		addMoiveInfoFrame.getContentPane().add(add);
		addMoiveInfoFrame.getContentPane().add(next);

		windowWidth = 350;
		windowHeight = 30 + 30 + movieLabelArray.size() * 30 + 60;

		addMoiveInfoFrame.setTitle("Admin:Add New Movie");
		addMoiveInfoFrame.setLayout(null);
		addMoiveInfoFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addMoiveInfoFrame.setSize(windowWidth, windowHeight);
		int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
		int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
		addMoiveInfoFrame.setLocation((screenWidth - windowWidth) / 2, (screenHeight - windowHeight) / 2);
		addMoiveInfoFrame.setResizable(false);
		addMoiveInfoFrame.setVisible(true);
	}

	/**
	 * method addScreenInfo This method is called by the next button in method
	 * addMoiveInfo to add the screen and time information
	 * 
	 * @param movieInfo
	 *            All movies information including the movie name, duration,
	 *            image filename
	 */
	public void addScreenInfo(ArrayList<ArrayList<String>> movieInfo) {
		addScreenInfoFrame = new JFrame();

		int windowWidth;
		int windowHeight;

		// 电影,屏幕,时间
		ArrayList<ArrayList<ArrayList<JButton>>> screenTime = new ArrayList<ArrayList<ArrayList<JButton>>>();
		ArrayList<ArrayList<JButton>> totalButton = new ArrayList<ArrayList<JButton>>();
		ArrayList<JButton> addButtonArray = new ArrayList<JButton>();
		ArrayList<JPanel> panelArray = new ArrayList<JPanel>();

		// 取出所有的电影名,电影时间
		ArrayList<JLabel> movieLabelArray = new ArrayList<JLabel>();
		ArrayList<JLabel> durationLabelArray = new ArrayList<JLabel>();
		ArrayList<String> durationArray = new ArrayList<String>();

		for (int i = 0; i < movieInfo.size(); i++) {
			screenTime.add(new ArrayList<ArrayList<JButton>>());
			totalButton.add(new ArrayList<JButton>());
			for (int j = 0; j < 3; j++) {
				screenTime.get(i).add(new ArrayList<JButton>());
			}

			addButtonArray.add(new JButton("Add"));
			panelArray.add(new JPanel());
			movieLabelArray.add(new JLabel(movieInfo.get(i).get(0), JLabel.CENTER));
			durationLabelArray.add(new JLabel(movieInfo.get(i).get(1) + "min", JLabel.CENTER));
			durationArray.add(movieInfo.get(i).get(1));

			panelArray.get(panelArray.size() - 1).setBounds(200, 30 + i * 60, 400, 60);
			movieLabelArray.get(movieLabelArray.size() - 1).setBounds(0, 30 + i * 60, 100, 60);
			durationLabelArray.get(durationLabelArray.size() - 1).setBounds(100, 30 + i * 60, 100, 60);

			panelArray.get(panelArray.size() - 1).setBorder(BorderFactory.createLineBorder(Color.black));
			movieLabelArray.get(movieLabelArray.size() - 1).setBorder(BorderFactory.createLineBorder(Color.black));
			durationLabelArray.get(durationLabelArray.size() - 1)
					.setBorder(BorderFactory.createLineBorder(Color.black));

			panelArray.get(panelArray.size() - 1).add(addButtonArray.get(addButtonArray.size() - 1));
			panelArray.get(panelArray.size() - 1).setLayout(null);

			addButtonArray.get(addButtonArray.size() - 1).setBounds(0, 0, 100, 30);

			addScreenInfoFrame.add(panelArray.get(panelArray.size() - 1));
			addScreenInfoFrame.add(movieLabelArray.get(movieLabelArray.size() - 1));
			addScreenInfoFrame.add(durationLabelArray.get(durationLabelArray.size() - 1));
		}

		JLabel movieName = new JLabel("Movie Name", JLabel.CENTER);
		JLabel movieDuration = new JLabel("Duration", JLabel.CENTER);
		JLabel movieScreenTime = new JLabel("Screen & Time", JLabel.CENTER);

		JButton load = new JButton("Load");
		JButton cancel = new JButton("Cancel");

		for (int i = 0; i < addButtonArray.size(); i++) {
			addButtonArray.get(i).addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JButton eventSource = (JButton) e.getSource();
					int i = 0;
					for (int l = 0; l < addButtonArray.size(); l++) {
						if (eventSource.equals(addButtonArray.get(l)))
							i = l;
					}

					String screenOptions[] = { "Screen1", "Screen2", "Screen3" };
					String timeOptions[] = { "08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30",
							"12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30",
							"17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30",
							"22:00", "22:30", "23:00", "23:30" };

					JComboBox screen = new JComboBox(screenOptions);
					JComboBox time = new JComboBox(timeOptions);
					screen.setSelectedIndex(0);
					time.setSelectedIndex(0);
					JPanel jpan = new JPanel();
					jpan.add(new JLabel("Screen:"));
					jpan.add(screen);
					jpan.add(new JLabel(" Time:"));
					jpan.add(time);

					int n = JOptionPane.showOptionDialog(null, jpan, "Admin:Select screen and time",
							JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

					if (n != -1) {
						// 判断时间重复

						for (int timeCounter = 0; timeCounter < screenTime.get(i).get(screen.getSelectedIndex())
								.size(); timeCounter++) {
							if (screenTime.get(i).get(screen.getSelectedIndex()).get(timeCounter).getText()
									.equals("<html><font size=2>" + screen.getSelectedItem() + "<br>&nbsp;&nbsp;"
											+ time.getSelectedItem() + "</font></html>")) {
								JOptionPane.showMessageDialog(null, "The time are duplicate! Please add another time",
										"Warning", JOptionPane.WARNING_MESSAGE);
								return;
							}
						}

						// 判断时间不够
						ArrayList<String> timeArray = new ArrayList<String>();

						for (int timeCounter = 0; timeCounter < screenTime.get(i).get(screen.getSelectedIndex())
								.size(); timeCounter++) {
							String tmpStr = screenTime.get(i).get(screen.getSelectedIndex()).get(timeCounter).getText();
							String hour = tmpStr.split(":")[0];
							String minute = tmpStr.split(":")[1];
							hour = hour.substring(hour.length() - 2, hour.length());
							minute = minute.substring(0, 2);
							timeArray.add("" + (Integer.parseInt(hour) * 60 + Integer.parseInt(minute)));
						}

						// 先对时间进行排序
						Collections.sort(timeArray, new SortByTime());

						int selectTime = Integer.parseInt(("" + time.getSelectedItem()).split(":")[0]) * 60
								+ Integer.parseInt(("" + time.getSelectedItem()).split(":")[1]);

						boolean flag = true;
						for (int timeCounter = 0; timeCounter < screenTime.get(i).get(screen.getSelectedIndex())
								.size(); timeCounter++) {
							if (screenTime.get(i).get(screen.getSelectedIndex()).size() == 1) {
								if (selectTime < (Integer.parseInt(timeArray.get(0))
										+ Integer.parseInt(durationArray.get(i)) + 10)) {
									// System.out.println("wrong1");
									flag = false;
								} else {
									flag = true;
								}
							} else {
								if (selectTime > Integer.parseInt(timeArray.get(timeArray.size() - 1))) {
									// System.out.println("\r\nJUDGE3(end)
									// parameters:");
									// System.out.println("selectTime: " +
									// selectTime);
									// System.out
									// .println("limit1: " +
									// (Integer.parseInt(timeArray.get(timeArray.size()
									// - 1))
									// + Integer.parseInt(durationArray.get(i))
									// + 10));
									if (selectTime < (Integer.parseInt(timeArray.get(timeArray.size() - 1))
											+ Integer.parseInt(durationArray.get(i)) + 10)) {
										// System.out.println("wrong3(end)");
										flag = false;
									} else {
										flag = true;
										break;
									}
								} else {
									if (timeCounter + 1 < screenTime.get(i).get(screen.getSelectedIndex()).size()) {
										// System.out.println("\r\nJUDGE2
										// parameters:");
										// System.out.println("selectTime: " +
										// selectTime);
										// System.out.println("limit1: " +
										// (Integer.parseInt(timeArray.get(timeCounter))
										// +
										// Integer.parseInt(durationArray.get(i))
										// + 10));
										// System.out.println("limit2: "
										// + (selectTime +
										// Integer.parseInt(durationArray.get(i))
										// + 10));
										// System.out.println(
										// "limit3: " +
										// (Integer.parseInt(timeArray.get(timeCounter
										// + 1))));
										if ((selectTime < (Integer.parseInt(timeArray.get(timeCounter))
												+ Integer.parseInt(durationArray.get(i)) + 10))
												|| ((selectTime + Integer.parseInt(durationArray.get(i))
														+ 10) > (Integer.parseInt(timeArray.get(timeCounter + 1))))) {
											// System.out.println("wrong2");
											flag = false;
										} else {
											flag = true;
											break;
										}
									} else {
										// System.out.println("\r\nJUDGE3
										// parameters:");
										// System.out.println("selectTime: " +
										// selectTime);
										// System.out.println("limit1: " +
										// (Integer.parseInt(timeArray.get(timeCounter))
										// +
										// Integer.parseInt(durationArray.get(i))
										// + 10));
										if (selectTime < (Integer.parseInt(timeArray.get(timeCounter))
												+ Integer.parseInt(durationArray.get(i)) + 10)) {
											// System.out.println("wrong3");
											flag = false;
										} else {
											flag = true;
											break;
										}
									}
								}
							}
						}

						// System.out.println(flag);
						if (!flag) {
							JOptionPane.showMessageDialog(null, "The time is overlapped! Please rechoose the time",
									"Warning", JOptionPane.WARNING_MESSAGE);
							return;
						}

						// If the selected time are suitable (not overlapping,
						// duplicate), then add the time into the panel
						JButton tmp = new JButton("<html><font size=2>" + screen.getSelectedItem() + "<br>&nbsp;&nbsp;"
								+ time.getSelectedItem() + "</font></html>");
						tmp.setVisible(true);
						switch (screen.getSelectedItem().toString()) {
						case "Screen1": {
							int buttonCount = screenTime.get(i).get(0).size() + screenTime.get(i).get(1).size()
									+ screenTime.get(i).get(2).size();
							// if the buttons count is not larger than 3, the
							// button will be added in the first line
							if (buttonCount < 3) {
								tmp.setBounds((buttonCount + 1) * 100, 0, 100, 30);
							} else {
								/*
								 * else the button will be added into the next
								 * line, if the next line is full of buttons, it
								 * will increase the size of the panel and the
								 * window, the buttons for other movies will
								 * move lower
								 */
								buttonCount -= 3;
								int lineCount = buttonCount / 4 + 1;
								buttonCount %= 4;
								tmp.setBounds(buttonCount * 100, lineCount * 30, 100, 30);

								/*
								 * Move down 30 pixel than before in y axis, to
								 * move the buttons for other movies, just move
								 * the panel where the buttons are added, the
								 * buttons will automatically move
								 */
								if ((lineCount > 1) && (buttonCount == 0)) {
									movieLabelArray.get(i).setBorder(null);
									durationLabelArray.get(i).setBorder(null);
									panelArray.get(i).setBorder(null);

									movieLabelArray.get(i).setBounds(movieLabelArray.get(i).getBounds().x,
											movieLabelArray.get(i).getBounds().y,
											movieLabelArray.get(i).getBounds().width,
											movieLabelArray.get(i).getBounds().height + 30);
									durationLabelArray.get(i).setBounds(durationLabelArray.get(i).getBounds().x,
											durationLabelArray.get(i).getBounds().y,
											durationLabelArray.get(i).getBounds().width,
											durationLabelArray.get(i).getBounds().height + 30);
									panelArray.get(i).setBounds(panelArray.get(i).getBounds().x,
											panelArray.get(i).getBounds().y, panelArray.get(i).getBounds().width,
											panelArray.get(i).getBounds().height + 30);

									movieLabelArray.get(i).setBorder(BorderFactory.createLineBorder(Color.black));
									durationLabelArray.get(i).setBorder(BorderFactory.createLineBorder(Color.black));
									panelArray.get(i).setBorder(BorderFactory.createLineBorder(Color.black));

									for (int counter = i + 1; counter < movieLabelArray.size(); counter++) {
										movieLabelArray.get(counter).setBorder(null);
										durationLabelArray.get(counter).setBorder(null);
										panelArray.get(counter).setBorder(null);

										movieLabelArray.get(counter).setBounds(
												movieLabelArray.get(counter).getBounds().x,
												movieLabelArray.get(counter).getBounds().y + 30,
												movieLabelArray.get(counter).getBounds().width,
												movieLabelArray.get(counter).getBounds().height);
										durationLabelArray.get(counter).setBounds(
												durationLabelArray.get(counter).getBounds().x,
												durationLabelArray.get(counter).getBounds().y + 30,
												durationLabelArray.get(counter).getBounds().width,
												durationLabelArray.get(counter).getBounds().height);
										panelArray.get(counter).setBounds(panelArray.get(counter).getBounds().x,
												panelArray.get(counter).getBounds().y + 30,
												panelArray.get(counter).getBounds().width,
												panelArray.get(counter).getBounds().height);

										movieLabelArray.get(counter)
												.setBorder(BorderFactory.createLineBorder(Color.black));
										durationLabelArray.get(counter)
												.setBorder(BorderFactory.createLineBorder(Color.black));
										panelArray.get(counter).setBorder(BorderFactory.createLineBorder(Color.black));
									}
									load.setBounds(load.getBounds().x, load.getBounds().y + 30, load.getBounds().width,
											load.getBounds().height);
									cancel.setBounds(cancel.getBounds().x, cancel.getBounds().y + 30,
											cancel.getBounds().width, cancel.getBounds().height);

									addScreenInfoFrame.setSize((int) addScreenInfoFrame.getSize().getWidth(),
											(int) addScreenInfoFrame.getSize().getHeight() + 30);
								}
							}
							// tmp.addActionListener(new ActionListener() {
							//
							// @Override
							// public void actionPerformed(ActionEvent arg0) {
							// int choice = JOptionPane.showConfirmDialog(null,
							// "Are you want to remove this time?", "Delete the
							// time",
							// JOptionPane.YES_NO_OPTION);
							// if (choice == 0) {
							// for (int i = 0; i < totalButton.get(i).size();
							// i++) {
							//
							// }
							// } else {
							// ;
							// }
							// }
							// });
							// Add the new button to the panel
							screenTime.get(i).get(0).add(tmp);
							totalButton.get(i).add(tmp);
							panelArray.get(i).add(tmp);

							SwingUtilities.updateComponentTreeUI(addScreenInfoFrame);

							break;
						}
						case "Screen2": {
							int buttonCount = screenTime.get(i).get(0).size() + screenTime.get(i).get(1).size()
									+ screenTime.get(i).get(2).size();
							if (buttonCount < 3) {
								tmp.setBounds((buttonCount + 1) * 100, 0, 100, 30);
							} else {
								buttonCount -= 3;
								int lineCount = buttonCount / 4 + 1;
								buttonCount %= 4;
								tmp.setBounds(buttonCount * 100, lineCount * 30, 100, 30);

								// 整体下移30
								if ((lineCount > 1) && (buttonCount == 0)) {
									movieLabelArray.get(i).setBorder(null);
									durationLabelArray.get(i).setBorder(null);
									panelArray.get(i).setBorder(null);

									movieLabelArray.get(i).setBounds(movieLabelArray.get(i).getBounds().x,
											movieLabelArray.get(i).getBounds().y,
											movieLabelArray.get(i).getBounds().width,
											movieLabelArray.get(i).getBounds().height + 30);
									durationLabelArray.get(i).setBounds(durationLabelArray.get(i).getBounds().x,
											durationLabelArray.get(i).getBounds().y,
											durationLabelArray.get(i).getBounds().width,
											durationLabelArray.get(i).getBounds().height + 30);
									panelArray.get(i).setBounds(panelArray.get(i).getBounds().x,
											panelArray.get(i).getBounds().y, panelArray.get(i).getBounds().width,
											panelArray.get(i).getBounds().height + 30);

									movieLabelArray.get(i).setBorder(BorderFactory.createLineBorder(Color.black));
									durationLabelArray.get(i).setBorder(BorderFactory.createLineBorder(Color.black));
									panelArray.get(i).setBorder(BorderFactory.createLineBorder(Color.black));

									for (int counter = i + 1; counter < movieLabelArray.size(); counter++) {
										movieLabelArray.get(counter).setBorder(null);
										durationLabelArray.get(counter).setBorder(null);
										panelArray.get(counter).setBorder(null);

										movieLabelArray.get(counter).setBounds(
												movieLabelArray.get(counter).getBounds().x,
												movieLabelArray.get(counter).getBounds().y + 30,
												movieLabelArray.get(counter).getBounds().width,
												movieLabelArray.get(counter).getBounds().height);
										durationLabelArray.get(counter).setBounds(
												durationLabelArray.get(counter).getBounds().x,
												durationLabelArray.get(counter).getBounds().y + 30,
												durationLabelArray.get(counter).getBounds().width,
												durationLabelArray.get(counter).getBounds().height);
										panelArray.get(counter).setBounds(panelArray.get(counter).getBounds().x,
												panelArray.get(counter).getBounds().y + 30,
												panelArray.get(counter).getBounds().width,
												panelArray.get(counter).getBounds().height);

										movieLabelArray.get(counter)
												.setBorder(BorderFactory.createLineBorder(Color.black));
										durationLabelArray.get(counter)
												.setBorder(BorderFactory.createLineBorder(Color.black));
										panelArray.get(counter).setBorder(BorderFactory.createLineBorder(Color.black));
									}
									load.setBounds(load.getBounds().x, load.getBounds().y + 30, load.getBounds().width,
											load.getBounds().height);
									cancel.setBounds(cancel.getBounds().x, cancel.getBounds().y + 30,
											cancel.getBounds().width, cancel.getBounds().height);

									addScreenInfoFrame.setSize((int) addScreenInfoFrame.getSize().getWidth(),
											(int) addScreenInfoFrame.getSize().getHeight() + 30);
								}
							}
							screenTime.get(i).get(1).add(tmp);
							totalButton.get(i).add(tmp);
							panelArray.get(i).add(tmp);

							SwingUtilities.updateComponentTreeUI(addScreenInfoFrame);

							break;
						}
						case "Screen3": {
							int buttonCount = screenTime.get(i).get(0).size() + screenTime.get(i).get(1).size()
									+ screenTime.get(i).get(2).size();
							if (buttonCount < 3) {
								tmp.setBounds((buttonCount + 1) * 100, 0, 100, 30);
							} else {
								buttonCount -= 3;
								int lineCount = buttonCount / 4 + 1;
								buttonCount %= 4;
								tmp.setBounds(buttonCount * 100, lineCount * 30, 100, 30);

								// 整体下移30
								if ((lineCount > 1) && (buttonCount == 0)) {
									movieLabelArray.get(i).setBorder(null);
									durationLabelArray.get(i).setBorder(null);
									panelArray.get(i).setBorder(null);

									movieLabelArray.get(i).setBounds(movieLabelArray.get(i).getBounds().x,
											movieLabelArray.get(i).getBounds().y,
											movieLabelArray.get(i).getBounds().width,
											movieLabelArray.get(i).getBounds().height + 30);
									durationLabelArray.get(i).setBounds(durationLabelArray.get(i).getBounds().x,
											durationLabelArray.get(i).getBounds().y,
											durationLabelArray.get(i).getBounds().width,
											durationLabelArray.get(i).getBounds().height + 30);
									panelArray.get(i).setBounds(panelArray.get(i).getBounds().x,
											panelArray.get(i).getBounds().y, panelArray.get(i).getBounds().width,
											panelArray.get(i).getBounds().height + 30);

									movieLabelArray.get(i).setBorder(BorderFactory.createLineBorder(Color.black));
									durationLabelArray.get(i).setBorder(BorderFactory.createLineBorder(Color.black));
									panelArray.get(i).setBorder(BorderFactory.createLineBorder(Color.black));

									for (int counter = i + 1; counter < movieLabelArray.size(); counter++) {
										movieLabelArray.get(counter).setBorder(null);
										durationLabelArray.get(counter).setBorder(null);
										panelArray.get(counter).setBorder(null);

										movieLabelArray.get(counter).setBounds(
												movieLabelArray.get(counter).getBounds().x,
												movieLabelArray.get(counter).getBounds().y + 30,
												movieLabelArray.get(counter).getBounds().width,
												movieLabelArray.get(counter).getBounds().height);
										durationLabelArray.get(counter).setBounds(
												durationLabelArray.get(counter).getBounds().x,
												durationLabelArray.get(counter).getBounds().y + 30,
												durationLabelArray.get(counter).getBounds().width,
												durationLabelArray.get(counter).getBounds().height);
										panelArray.get(counter).setBounds(panelArray.get(counter).getBounds().x,
												panelArray.get(counter).getBounds().y + 30,
												panelArray.get(counter).getBounds().width,
												panelArray.get(counter).getBounds().height);

										movieLabelArray.get(counter)
												.setBorder(BorderFactory.createLineBorder(Color.black));
										durationLabelArray.get(counter)
												.setBorder(BorderFactory.createLineBorder(Color.black));
										panelArray.get(counter).setBorder(BorderFactory.createLineBorder(Color.black));
									}
									load.setBounds(load.getBounds().x, load.getBounds().y + 30, load.getBounds().width,
											load.getBounds().height);
									cancel.setBounds(cancel.getBounds().x, cancel.getBounds().y + 30,
											cancel.getBounds().width, cancel.getBounds().height);

									addScreenInfoFrame.setSize((int) addScreenInfoFrame.getSize().getWidth(),
											(int) addScreenInfoFrame.getSize().getHeight() + 30);
								}
							}
							screenTime.get(i).get(2).add(tmp);
							totalButton.get(i).add(tmp);
							panelArray.get(i).add(tmp);

							SwingUtilities.updateComponentTreeUI(addScreenInfoFrame);
							break;
						}
						}
						panelArray.get(i).add(tmp);
					} else {
						;
					}
				}
			});
		}

		// load the movies' configuration into the corresponding file
		load.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				String path1 = "./movies/info";
				String path2 = "./screens";
				String file1 = "/movies.txt";
				String file2 = "/screens.txt";
				String contents = "";

				// write movies' name, duration, image filename information into
				// ./movies/info/movies.txt
				for (int i = 0; i < movieInfo.size(); i++) {
					contents += movieInfo.get(i).get(0) + ";" + movieInfo.get(i).get(1) + ";" + movieInfo.get(i).get(2)
							+ "\r\n";
				}

				// First generate the path, then the file
				File tmp = new File(path1);
				if (!tmp.exists()) {
					try {
						tmp.mkdirs();
					} catch (SecurityException e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(null,
								"Error when generate the path! Please check the authority of the current user!",
								"Error", JOptionPane.ERROR_MESSAGE);
					}
				}
				new FileOp().writeFile(contents, path1 + file1, false);

				// write movies' screen, time information into
				// ./movies/info/movies.txt
				contents = "";
				for (int i = 0; i < 3; i++) {
					contents += "screen" + (i + 1) + ";";
					switch (i) {
					case 0: {
						contents += "32\r\n";
						break;
					}
					case 1: {
						contents += "26\r\n";
						break;
					}
					case 2: {
						contents += "32\r\n";
						break;
					}
					}
					for (int j = 0; j < movieInfo.size(); j++) {
						if (screenTime.get(j).get(i).size() != 0) {
							contents += movieInfo.get(j).get(2) + ";";
							for (int l = 0; l < screenTime.get(j).get(i).size(); l++) {
								String hour = screenTime.get(j).get(i).get(l).getText().split(":")[0];
								String minute = screenTime.get(j).get(i).get(l).getText().split(":")[1];

								String time = hour.substring(hour.length() - 2, hour.length()) + minute.substring(0, 2);
								if ((l + 1) == screenTime.get(j).get(i).size())
									contents += time;
								else
									contents += time + ";";
							}
							contents += "\r\n";
						}
					}
				}

				// First generate the path, then the file
				tmp = new File(path2);
				if (!tmp.exists()) {
					try {
						tmp.mkdirs();
					} catch (SecurityException e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(null,
								"Error when generate the path! Please check the authority of the current user!",
								"Error", JOptionPane.ERROR_MESSAGE);
					}
				}
				new FileOp().writeFile(contents, path2 + file2, false);
				addScreenInfoFrame.dispose();
			}
		});

		// Cancel the adding process
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		movieName.setBounds(0, 0, 100, 30);
		movieDuration.setBounds(100, 0, 100, 30);
		movieScreenTime.setBounds(200, 0, 400, 30);

		movieName.setBorder(BorderFactory.createLineBorder(Color.black));
		movieDuration.setBorder(BorderFactory.createLineBorder(Color.black));
		movieScreenTime.setBorder(BorderFactory.createLineBorder(Color.black));

		load.setBounds(100, 30 + movieInfo.size() * 60, 100, 30);
		cancel.setBounds(400, 30 + movieInfo.size() * 60, 100, 30);

		addScreenInfoFrame.add(movieName);
		addScreenInfoFrame.add(movieDuration);
		addScreenInfoFrame.add(movieScreenTime);
		addScreenInfoFrame.add(load);
		addScreenInfoFrame.add(cancel);

		windowWidth = 600;
		windowHeight = 30 + movieInfo.size() * 60 + 30 + 30;

		addScreenInfoFrame.setTitle("Admin:Select screen and time");
		addScreenInfoFrame.setLayout(null);
		addScreenInfoFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addScreenInfoFrame.setSize(windowWidth, windowHeight);
		int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
		int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
		addScreenInfoFrame.setLocation((screenWidth - windowWidth) / 2, (screenHeight - windowHeight) / 2);
		addScreenInfoFrame.setResizable(false);
		addScreenInfoFrame.setVisible(true);
	}

	/**
	 * class SortByTime This internal class is used to sort the added time
	 * ascending
	 */

	class SortByTime implements Comparator {
		public int compare(Object o1, Object o2) {
			String s1 = (String) o1;
			String s2 = (String) o2;
			if (Integer.parseInt(s1) > Integer.parseInt(s2))
				return 1;
			else
				return -1;
		}
	}

	/**
	 * method genFilmReport This method read all informations of the sold ticket
	 * of all movies and then calculate the sales finally generate reports
	 * 
	 * @param date
	 *            The parameter date stand for the report's filename
	 * @see Test.java
	 */

	public void genFilmReport(String date) {
		ArrayList<String> tmp = new FileOp().readFile("./movies/info/movies.txt");
		for (int i = 0; i < tmp.size(); i++) {
			String str[] = tmp.get(i).split(";");
			movieNameList.add(str[0]);
			movieList.add(str[2]);
			ticketList.add(new ArrayList<String>());
		}

		for (int i = 0; i < movieList.size(); i++) {
			ArrayList<String> strList = new FileOp()
					.readFile("./movies/tickets/" + movieList.get(i) + "/ticket_sold.txt");
			if (strList == null) {
				JOptionPane.showMessageDialog(null,
						"Sorry! The system has not been initialized! Please initialize the system first!", "Warning",
						JOptionPane.WARNING_MESSAGE);
				System.exit(0);
			} else {
				for (int j = 0; j < strList.size(); j++) {
					String str[] = strList.get(j).split(";");
					ticketList.get(i).add(str[1]);
				}
			}
		}

		// 整体票的种类数量统计
		// The counter for each kind of ticket (total)
		int childTotal = 0;
		int adultTotal = 0;
		int seniorTotal = 0;
		int studentTotal = 0;

		ArrayList<String> contentList = new ArrayList<String>();
		DecimalFormat fnum = new DecimalFormat("##0.00");
		String contents;
		for (int i = 0; i < ticketList.size(); i++) {
			// 每部电影的票的种类数量统计
			// The counter for each kind of ticket for each movie
			int child = 0;
			int adult = 0;
			int senior = 0;
			int student = 0;

			for (int j = 0; j < ticketList.get(i).size(); j++) {
				switch (ticketList.get(i).get(j)) {
				case "0": {
					child++;
					break;
				}
				case "1": {
					adult++;
					break;
				}
				case "2": {
					senior++;
					break;
				}
				case "3": {
					student++;
					break;
				}
				default: {
					adult++;
					break;
				}
				}

			}
			// 先算出各种票的数量,以方便计算总收入
			// Calculate all count of different kind of tickets
			childTotal += child;
			adultTotal += adult;
			seniorTotal += senior;
			studentTotal += student;

			// 先将总收入/票售出情况写入,在写入各电影的收入/票售出情况
			// Write the statics into the corresponding report
			contents = "Movie: " + movieNameList.get(i) + "\r\nTicket Sold: " + (child + adult + senior + student)
					+ "\r\nSales: "
					+ fnum.format(child * 16 * 0.5 + adult * 16 * 1 + senior * 16 * 0.8 + student * 16 * 0.85)
					+ " pounds\r\nChild Ticket: " + child + "\r\nAdult Ticket: " + adult + "\r\nSenior Ticket: "
					+ senior + "\r\nStudent Ticket: " + student;
			contentList.add(contents);
		}
		contents = "Total Ticket Sold: " + (childTotal + adultTotal + seniorTotal + studentTotal) + "\r\nSales: "
				+ fnum.format(
						childTotal * 16 * 0.5 + adultTotal * 16 * 1 + seniorTotal * 16 * 0.8 + studentTotal * 16 * 0.85)
				+ " pounds\r\nChild Ticket: " + childTotal + "\r\nAdult Ticket: " + adultTotal + "\r\nSenior Ticket: "
				+ seniorTotal + "\r\nStudent Ticket: " + studentTotal;
		contentList.add(contents);

		for (int i = 0; i < contentList.size() - 1; i++)
			contents += "\r\n\r\n" + contentList.get(i);
		
		File reportPath = new File("./reports");
		if (!reportPath.exists()) {
			reportPath.mkdirs();
		}
		new FileOp().writeFile(contents, "./reports/" + date + ".txt", false);
	}
}