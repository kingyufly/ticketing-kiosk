
/**
 * FirstLayer.java
 * The 1st Layer of the GUI
 * Show all movies in the configuration file
 * Allow user to select the movie
 * Check the status of the movie in a set period
 * 
 * @author kingyufly
 * @version 2.2
 * 
 * */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;

/**
 * The 1st Layer GUI class
 */

public class FirstLayer extends JFrame implements ActionListener {
	ArrayList<List<Map<String, String>>> movieInfoList;
	ArrayList<JLabel> labelList;
	ArrayList<ImageIcon> imageList;
	ArrayList<JButton> buttonList;
	ArrayList<String> infoList;

	volatile boolean movieBool_soldout[];
	volatile boolean movieBool_timeout[];

	int movieArr[]; // The time array for movies
	Thread thread;
	String moviePath = "./movies";
	String screenPath = "./screens";

	/**
	 * Overwrite the default constructor to show the information of the movie in
	 * GUI
	 * 
	 */

	public FirstLayer() {
		// 判断是否第一次运行,如果为第一次则生成所依赖的文件/文件夹
		// 若存在,则直接pass
		/*
		 * Judge the existence of the movie's data, if it is the first time, it
		 * will generate the administartor's interface to load data to the
		 * system
		 */
		movieInfoList = this.FileExist(moviePath, screenPath);

		labelList = new ArrayList<JLabel>();
		imageList = new ArrayList<ImageIcon>();
		buttonList = new ArrayList<JButton>();

		// 先读取movies文档中数据,并确定窗口的大小
		// Read the data in the movie information's file to decide the size of
		// the windows
		infoList = new FileOp().readFile(moviePath + "/info/movies.txt");
		int windowHeight = infoList.size() * 110 + (infoList.size() + 1) * 5 + 30;
		int windowWidth = 325 + 2 * 5 + 10;

		// 严谨的话需要先读取一遍依赖文件,在刚打开的时候就判断电影的状态
		movieBool_soldout = new MovieCheck().CheckMovieSoldout(infoList);
		movieBool_timeout = new MovieCheck().CheckMovieTime(movieArr);

		// Adding all of the movies' information to the panel
		for (int i = 0; i < infoList.size(); i++) {
			String x[] = infoList.get(i).split(";");

			JPanel tmpPanel = new JPanel();
			tmpPanel.setLayout(new BorderLayout());

			JLabel tmpLabel = null;

			// 先判断是否售罄/超过时间,并根据结果决定label上的信息
			// Decide the whether the movie has been sold out or not to
			// determine the label's information
			if (movieBool_soldout[i] && movieBool_timeout[i]) {
				// 没有售罄也没有超过最晚上映时间
				// Not soldout and timeout
				tmpLabel = new JLabel("<html>" + x[0] + "</html>");
			} else if (!movieBool_soldout[i] && movieBool_timeout[i]) {
				// 如果售罄
				// Sold out
				tmpLabel = new JLabel("<html><s>" + x[0] + "</s><br><font color=\"red\">SOLD OUT</font></html>");
			} else if ((movieBool_soldout[i] && !movieBool_timeout[i])
					|| (!movieBool_soldout[i] && !movieBool_timeout[i])) {
				// 如果超过上映时间或超过上映时间且售罄
				// Time out(Including time out and sold out at the same time)
				tmpLabel = new JLabel("<html><s>" + x[0] + "</s><br><font color=\"red\">UNAVAILABLE</font></html>");
			} else {
			}

			// 添加电影名的label
			// Add the label for movie name
			labelList.add(tmpLabel);
			tmpLabel.setPreferredSize(new Dimension(150, 75));
			tmpPanel.add(tmpLabel, BorderLayout.WEST);

			// 添加电影时长的label
			// Add the label for movie duration
			JPanel tmpRightPanel = new JPanel();
			tmpRightPanel.setLayout(new BorderLayout());
			tmpLabel = new JLabel(x[1] + "min");
			tmpLabel.setPreferredSize(new Dimension(55, 75));
			tmpRightPanel.add(tmpLabel, BorderLayout.WEST);

			// 添加电影的图片信息(根据售罄/超时来决定图片的种类)
			// Add the movie's image (change the image according to the state of
			// the movie)
			ImageIcon tmpIcon = null;
			if (movieBool_soldout[i] && movieBool_timeout[i]) {
				// 没有售罄也没有超过最晚上映时间
				tmpIcon = new ImageIcon(moviePath + "/info/img/" + x[2] + ".jpg");
			} else if (!movieBool_soldout[i] && movieBool_timeout[i]) {
				// 如果售罄
				tmpIcon = new ImageIcon(moviePath + "/info/img/" + x[2] + "_soldout.jpg");
			} else if ((movieBool_soldout[i] && !movieBool_timeout[i])
					|| (!movieBool_soldout[i] && !movieBool_timeout[i])) {
				// 如果超过上映时间或超过上映时间且售罄
				tmpIcon = new ImageIcon(moviePath + "/info/img/" + x[2] + "_timeout.jpg");
			} else {
			}

			// imagelist用于多线程检查时图片的更换
			/*
			 * imageList is used for changing the image when using the
			 * multithread to check the state of the movie
			 */
			imageList.add(tmpIcon);
			tmpRightPanel.add(new JLabel(tmpIcon), BorderLayout.EAST);
			tmpPanel.add(tmpRightPanel, BorderLayout.EAST);

			// 将电影的两个label,一个image添加到button上
			// Add the two labels and one image to the button
			JButton tmpButton = new JButton();
			tmpButton.add(tmpPanel);
			tmpButton.addActionListener(this);

			if (movieBool_soldout[i] && movieBool_timeout[i]) {
				// 没有售罄也没有超过最晚上映时间启用button
				// Enable the button when the movie is available
				tmpButton.setEnabled(true);
			} else {
				// 其他情况禁用button
				// Disable the button when the movie is unavailable
				tmpButton.setEnabled(false);
			}

			buttonList.add(tmpButton);
			this.getContentPane().add(tmpButton);
		}
		this.setLayout(new GridLayout(infoList.size(), 1, 0, 5));

		// 设定窗口的size位置等
		// Set the size, location and etc. for the window
		this.setTitle("Ticketing Kiosk");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(windowWidth, windowHeight);
		// 窗口屏幕居中
		// Set the window at the centre of the screen
		int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
		int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
		this.setLocation((screenWidth - windowWidth) / 2, (screenHeight - windowHeight) / 2);
		this.setVisible(true);

		// 在生成图形界面后,声称子线程用来检查movies的实时状态
		// After generate the GUI, then start checking the state of the movie
		// using multiThread
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
		for (int i = 0; i < buttonList.size(); i++) {
			if (eventSource.equals(buttonList.get(i))) {

				Map<String, String> tmp = new HashMap<String, String>();
				tmp.put("movie", infoList.get(i).split(";")[2]);
				tmp.put("moviename", infoList.get(i).split(";")[0]);
				movieInfoList.get(i).add(tmp);

				this.dispose();
				new SecondLayer(movieInfoList.get(i));
				thread.interrupt();
			}
		}
	}

	// 检查两个必要的配置文件
	// 1. "./screens/screens.txt"
	// 2. "./movies/info/movies.txt"
	// 检查其他记录性质文件
	// 1. screens目录下文件
	// 2. movies目录下文件

	/**
	 * Method FileExist, First check the existence of the two configuration
	 * file, if not exist, the exit the program if the corresponding file and
	 * folders not exist, then generate them. Secondly, read the configuration
	 * file and return the information.
	 * 
	 * @param moviePath
	 *            The movies' configuration file's path
	 * 
	 * @param screenPath
	 *            The screens' configuration file's path
	 * 
	 * @return returns all the information of the screens(the screen name, seat
	 *         number, time, movie name and etc.)
	 * 
	 * @see method getMovieInfo
	 */

	public ArrayList<List<Map<String, String>>> FileExist(String moviePath, String screenPath) {
		File movieCfg = new File(moviePath + "/info/movies.txt");
		File screenCfg = new File(screenPath + "/screens.txt");

		// 收先读取movie和screen的配置文件
		// First read the movie and screen's information
		ArrayList<String> movieInfo = null;
		ArrayList<String> screenInfo = null;
		
		movieInfo = new FileOp().readFile(movieCfg.getAbsolutePath());
		screenInfo = new FileOp().readFile(screenCfg.getAbsolutePath());

		// 如果两个配置文件不存在的话,就直接结束掉整个程序
		// if the file does not exist, it will terminate the program or let the
		// administrator to load the data
		if (!(movieCfg.exists() && screenCfg.exists())) {
			JOptionPane.showMessageDialog(null, "The configuraton file does not exist, please load the file!", "Error",
					JOptionPane.ERROR_MESSAGE);

			// System.exit(0);
			new Admin().addMoiveInfo();
			while(!(movieCfg.exists() && screenCfg.exists())){
				;
			}
			movieInfo = new FileOp().readFile(movieCfg.getAbsolutePath());
			screenInfo = new FileOp().readFile(screenCfg.getAbsolutePath());
		} else if (!((movieInfo.size() >= 1) && (screenInfo.size() >= 2))) {
			// 如果配置文件没有内容的话,说明配置文件有问题,结束程序
			// if the file have no content, it will terminate the program or let
			// the administrator to load the data
			JOptionPane.showMessageDialog(null, "The configuration file is broken, please reload the file!", "Error",
					JOptionPane.ERROR_MESSAGE);
			System.exit(0);
			// new Admin().addMoiveInfo();
		}

		// 能获取电影名("电影对应的文件名"不同于"电影名")
		// Get all the movies' name (different from the image name of the movie)
		ArrayList<String> movieList = new ArrayList<String>();
		ArrayList<List<Map<String, String>>> movieInfoList = new ArrayList<List<Map<String, String>>>();
		for (int i = 0; i < movieInfo.size(); i++) {
			movieList.add(movieInfo.get(i).split(";")[2]);
			movieInfoList.add(this.getMovieInfo(movieList.get(i), screenCfg.getAbsolutePath()));
		}

		// 每部电影每个影厅每个时间的票数
		// The ticket number for each screen
		ArrayList<ArrayList<ArrayList<String>>> ticketNum = new ArrayList<ArrayList<ArrayList<String>>>();
		// 每部电影的最末时间
		// The array stores the latest time of the movie
		movieArr = new int[movieList.size()];
		// 每部电影的电影票ID
		// The array stores the IDs of the movies
		ArrayList<String> ticketNo;
		// 总票数
		// Total ticket number
		int ticketAllNum = 0;

		// 将每个电影的每个影厅的每个时间的 票数取出
		// 并取出最末场次时间用以判断能否购票

		// 循环每个电影
		for (int i = 0; i < movieInfoList.size(); i++) {
			// 循环每个screen
			ticketNum.add(new ArrayList<ArrayList<String>>());
			int movieTime = 0;
			for (int j = 0; j < movieInfoList.get(i).size(); j++) {
				// 循环每个时间
				ticketNum.get(i).add(new ArrayList<String>());
				int movieTime1 = 0;
				for (String key : movieInfoList.get(i).get(j).keySet()) {
					if (!key.equals("screen")) {
						// 获取每部电影每个screen每个时间的票数
						// Get each movie's each screen's each time's ticket
						// number
						ticketNum.get(i).get(j).add(movieInfoList.get(i).get(j).get(key));
						// 获取总票数
						// Get total ticket number in order to generate the ID
						ticketAllNum += Integer.parseInt(movieInfoList.get(i).get(j).get(key));
						// 如果时间小于新的放映时间则赋值新的电影时间,否则不变
						// If the time is less than the last one, the movieTime
						// will be the new time
						movieTime1 = (movieTime1 <= Integer.parseInt(key)) ? (Integer.parseInt(key)) : (movieTime1);
					}
				}
				movieTime = (movieTime <= movieTime1) ? (movieTime1) : (movieTime);
			}
			movieArr[i] = movieTime;
		}

		// 生成票的ID(先生成全部票的ID再根据不同的场次/不同的电影进行分配)
		// Generate all the ticket(ID)
		ticketNo = new MathOp().genTicket(ticketAllNum);

		// System.exit(0);

		// ArrayList<String> pathList = new ArrayList<String>();
		// ArrayList<String> fileList = new ArrayList<String>();
		// 可以增加更极端的情况:比如部分文件/文件夹确实
		// 可以提示用户手动修复/自动修复/直接关闭

		File tmp = new File(moviePath + "/tickets");
		// 如果相关的依赖文件不存在,则生成(第一次打开)
		// 如果存在,则pass(不是第一次打开,直接读取原有记录)
		// If the configuration file does not exist, it will be automatically
		// generate
		if (!tmp.exists()) {
			try {
				tmp.mkdirs();
			} catch (SecurityException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null,
						"Error when generate the path! Please check the authority of the current user!", "Error",
						JOptionPane.ERROR_MESSAGE);
			}

			// 此路径是用于放置生成的电影票文件
			// The path is to store the tickets
			tmp = new File("./tickets");
			try {
				tmp.mkdirs();
			} catch (SecurityException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null,
						"Error when generate the path! Please check the authority of the current user!", "Error",
						JOptionPane.ERROR_MESSAGE);
			}

			// 此路径是用于放置生成的报告文件
			// The path is used to store the reports
			tmp = new File("./reports");
			try {
				tmp.mkdirs();
			} catch (SecurityException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null,
						"Error when generate the path! Please check the authority of the current user!", "Error",
						JOptionPane.ERROR_MESSAGE);
			}

			int movieCurrentNum = 0;
			String contents = "";
			// 循环每个电影
			// For each movie
			for (int i = 0; i < movieInfoList.size(); i++) {
				// 每部电影的总票数
				int screenCurrentNum = 0;
				// For each screen
				for (int j = 0; j < movieInfoList.get(i).size(); j++) {
					// 生成screen文件夹
					// Generate the folder for screens
					tmp = new File(screenPath + "/" + movieInfoList.get(i).get(j).get("screen"));
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

					// 生成每个screen中movie的文件夹
					// generate the folder for each movie in each screen
					try {
						new File(screenPath + "/" + movieInfoList.get(i).get(j).get("screen") + "/" + movieList.get(i))
								.mkdirs();
					} catch (SecurityException e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(null,
								"Error when generate the path! Please check the authority of the current user!",
								"Error", JOptionPane.ERROR_MESSAGE);
					}
					// 循环每个场次
					// For each time
					for (String key : movieInfoList.get(i).get(j).keySet()) {
						if (!key.equals("screen")) {
							// 生成场次文件夹(时间)
							// Generate the folder for each time
							tmp = new File(screenPath + "/" + movieInfoList.get(i).get(j).get("screen") + "/"
									+ movieList.get(i) + "/" + key);
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

							// 生成三个对应的文件
							// seat.txt 用来放每个screen中座位的情况(true为未售出,false为售出)
							// ticket_remain.txt 用来放未卖出的票(ticket id)
							// ticket_sold.txt 用来放卖出的票(ticket id以及票的具体信息)

							/*
							 * seat.txt is used for the seat information of each
							 * movies' each screen and each time
							 * ticket_remain.txt is used for the ticket (ID)
							 * remained ticket_sold.txt is used for the sold
							 * ticket
							 */
							try {
								new File(screenPath + "/" + movieInfoList.get(i).get(j).get("screen") + "/"
										+ movieList.get(i) + "/" + key + "/seat.txt").createNewFile();
								new File(screenPath + "/" + movieInfoList.get(i).get(j).get("screen") + "/"
										+ movieList.get(i) + "/" + key + "/ticket_remain.txt").createNewFile();
								new File(screenPath + "/" + movieInfoList.get(i).get(j).get("screen") + "/"
										+ movieList.get(i) + "/" + key + "/ticket_sold.txt").createNewFile();
							} catch (IOException e) {
								e.printStackTrace();
							}

							// 写入seat.txt(根据座位数写入等量的true)
							// Write in the corresponding ticket ID in to the
							// file
							contents = "";
							for (int k = 0; k < Integer.parseInt(movieInfoList.get(i).get(j).get(key)); k++) {
								contents += "true\r\n";
							}
							new FileOp().writeFile(contents,
									screenPath + "/" + movieInfoList.get(i).get(j).get("screen") + "/"
											+ movieList.get(i) + "/" + key + "/seat.txt",
									false);

							// 写入ticket_remain.txt(根据座位数写入对应量的ticket ID)
							// Write in the corresponding ticket ID in to the
							// file
							contents = "";
							for (int k = (screenCurrentNum + movieCurrentNum); k < (screenCurrentNum + movieCurrentNum
									+ Integer.parseInt(movieInfoList.get(i).get(j).get(key))); k++) {
								contents += ticketNo.get(k) + "\r\n";
							}
							new FileOp().writeFile(contents,
									screenPath + "/" + movieInfoList.get(i).get(j).get("screen") + "/"
											+ movieList.get(i) + "/" + key + "/ticket_remain.txt",
									false);
							// 对写入量进行计数,一遍分配ticket id
							// Count the number of the ticket ID in order to
							// allocate the ticket IDs
							screenCurrentNum += Integer.parseInt(movieInfoList.get(i).get(j).get(key));
						}
					}
				}

				// 生成电影的ticket以及其文件等(不同于screen)
				// ticket_remain.txt 未售出的ticket id
				// ticket_sold.txt 售出的ticket id以及票的种类,screen名,电影名等
				tmp = new File(moviePath + "/tickets/" + movieList.get(i));
				try {
					tmp.mkdirs();
				} catch (SecurityException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null,
							"Error when generate the path! Please check the authority of the current user!", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
				tmp = new File(moviePath + "/tickets/" + movieList.get(i) + "/ticket_remain.txt");
				try {
					tmp.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null,
							"Error when generate the file! Please check the authority of the current user!", "Error",
							JOptionPane.ERROR_MESSAGE);
				}

				contents = "";
				for (int j = movieCurrentNum; j < (movieCurrentNum + screenCurrentNum); j++) {
					contents += ticketNo.get(j) + "\r\n";
				}
				// 计数以用来分配ticket id
				// Counter to allocate the ticket IDs
				movieCurrentNum += screenCurrentNum;
				new FileOp().writeFile(contents, tmp.getAbsolutePath(), false);

				// Generate the file to store the sold ticket
				tmp = new File(moviePath + "/tickets/" + movieList.get(i) + "/ticket_sold.txt");
				try {
					tmp.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null,
							"Error when generate the file! Please check the authority of the current user!", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		} else {
			;
		}
		return movieInfoList;
	}

	/**
	 * Method getMovieInfo, Read the movie's information from the screens'
	 * configuration file
	 * 
	 * @param movieName
	 *            The movie's name
	 * 
	 * @param screenPath
	 *            The screens' configuration file's path
	 * 
	 * @return returns all the information of the corresponding movie(the screen
	 *         name, seat number, time and etc.)
	 * 
	 * @see method FileExist
	 */

	public List<Map<String, String>> getMovieInfo(String movieName, String screenPath) {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		// 获取screen配置文件内的所有内容
		// Get all the informations from the movie configuration file
		ArrayList<String> screenInfoList = new FileOp().readFile(screenPath);

		String num = null;
		Map<String, String> map = null;
		// 循环取出所有对应的值(screen名,对应的座位数,对应电影的场次信息)
		// Get all information of the movie
		for (int i = 0; i < screenInfoList.size(); i++) {
			// 添加screen名
			// Add screen name
			if (screenInfoList.get(i).indexOf("screen") != -1) {
				num = screenInfoList.get(i).split(";")[1];
				String screenNo = screenInfoList.get(i).split(";")[0];

				map = new HashMap<String, String>();
				map.put("screen", screenNo);
			} else if (screenInfoList.get(i).indexOf(movieName) != -1) {
				// 添加电影的场次(时间),座位数
				// Add time and seat number
				String movieTime[] = screenInfoList.get(i).split(";");
				for (int j = 1; j < movieTime.length; j++) {
					map.put(movieTime[j], num);
				}
				result.add(map);
			} else {
				;
			}
		}
		return result;
	}

	/**
	 * Inner class as a checker to check the remain ticket and whether the movie
	 * is time out using multithread(implements Runnable)
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
				// 两个用来放movie的检查结果,一个为是否售罄(sold out),另一个为是否超过上映时间(time
				// out),TRUE为可以继续售卖,FALSE为不能继续售卖
				/*
				 * Use two boolean array to store the result of the check
				 * result. The first is to check whether the ticket is sold out,
				 * The second is to check whether the time is over the schedule
				 */
				movieBool_soldout = new MovieCheck().CheckMovieSoldout(infoList);
				movieBool_timeout = new MovieCheck().CheckMovieTime(movieArr);

				// 如果GUI中没有添加button则不进行检查
				// If there is no button in the panel, the checking process will
				// not process
				if (buttonList.size() == 0) {
				} else {
					int beginIndex = 0;
					int endIndex = 0;

					// 循环检查GUI中的所有button
					// Check all buttons(movies) in the list
					for (int i = 0; i < movieBool_soldout.length; i++) {
						String movieName = infoList.get(i).split(";")[2];

						// 分为三种情况,一为可以售卖,二为售罄了不能售卖,三为超过开始时间不能售卖以及同时售罄/超时不能售卖
						// It has three status, the first is available, the
						// second is sold out, the third is timeout
						if ((movieBool_soldout[i] == true) && (movieBool_timeout[i] == true)) {
							// 如果label上的text没有<s>(即划线),则不更新状态(因为原来就是可以售卖的)
							// If the text on the button does not contain <s>,
							// it means the movie is still avaiable, the state
							// will not update
							if (labelList.get(i).getText().indexOf("<s>") == -1) {
							} else {
								// 如果原来划线了(即不能售卖),则截取<s></s>中的内容,重新构建label上的text
								// 并启用button(划线=禁用button)
								// If the text on the button contain <s>, then
								// extract the content in between the <s>
								buttonList.get(i).setEnabled(true);
								// 同时更换电影的图片(变为正常的彩色图片)
								// At the same time change the picture of the
								// movie
								imageList.get(i).setImage(
										new ImageIcon(moviePath + "/info/img/" + movieName + ".jpg").getImage());

								beginIndex = labelList.get(i).getText().indexOf("<s>");
								endIndex = labelList.get(i).getText().indexOf("</s>");
								labelList.get(i).setText("<html>"
										+ labelList.get(i).getText().substring(beginIndex + 3, endIndex) + "</html>");
							}
						} else if ((movieBool_soldout[i] == false) && (movieBool_timeout[i] == true)) {
							// 如果原来没划线,则截取label上<html></html>中的部分重新构建text
							if (labelList.get(i).getText().indexOf("<s>") == -1) {
								// 先enable button才能进行更改button上的图片
								buttonList.get(i).setEnabled(true);
								imageList.get(i)
										.setImage(new ImageIcon(moviePath + "/info/img/" + movieName + "_soldout.jpg")
												.getImage());
								// 最后禁用button(因为售罄了)
								// Disable the button
								buttonList.get(i).setEnabled(false);
								beginIndex = labelList.get(i).getText().indexOf("<html>");
								endIndex = labelList.get(i).getText().indexOf("</html>");
								labelList.get(i)
										.setText("<html><s>"
												+ labelList.get(i).getText().substring(beginIndex + 6, endIndex)
												+ "</s><br><font color=\"red\">SOLD OUT</font></html>");
							} else {
								// 如果原来划线了,则截取label上<s></s>中的部分重新构建text
								buttonList.get(i).setEnabled(true);
								imageList.get(i)
										.setImage(new ImageIcon(moviePath + "/info/img/" + movieName + "_soldout.jpg")
												.getImage());
								buttonList.get(i).setEnabled(false);
								beginIndex = labelList.get(i).getText().indexOf("<s>");
								endIndex = labelList.get(i).getText().indexOf("</s>");
								labelList.get(i)
										.setText("<html><s>"
												+ labelList.get(i).getText().substring(beginIndex + 3, endIndex)
												+ "</s><br><font color=\"red\">SOLD OUT</font></html>");
							}
						} else if (((movieBool_soldout[i] == true) && (movieBool_timeout[i] == false))
								|| ((movieBool_soldout[i] == false) && (movieBool_timeout[i] == false))) { // 超时
							if (labelList.get(i).getText().indexOf("<s>") == -1) {
								buttonList.get(i).setEnabled(true);
								imageList.get(i)
										.setImage(new ImageIcon(moviePath + "/info/img/" + movieName + "_timeout.jpg")
												.getImage());
								buttonList.get(i).setEnabled(false);
								beginIndex = labelList.get(i).getText().indexOf("<html>");
								endIndex = labelList.get(i).getText().indexOf("</html>");
								labelList.get(i)
										.setText("<html><s>"
												+ labelList.get(i).getText().substring(beginIndex + 6, endIndex)
												+ "</s><br><font color=\"red\">UNAVAILABLE</font></html>");
							} else {
								buttonList.get(i).setEnabled(true);
								imageList.get(i)
										.setImage(new ImageIcon(moviePath + "/info/img/" + movieName + "_timeout.jpg")
												.getImage());
								buttonList.get(i).setEnabled(false);
								beginIndex = labelList.get(i).getText().indexOf("<s>");
								endIndex = labelList.get(i).getText().indexOf("</s>");
								labelList.get(i)
										.setText("<html><s>"
												+ labelList.get(i).getText().substring(beginIndex + 3, endIndex)
												+ "</s><br><font color=\"red\">UNAVAILABLE</font></html>");
							}
						} else {
						}
					}
				}
				// Check the status of all movies per 100ms, and catch the
				// InterruptedException and handle it when the thread is
				// interrupted(used to finish the thread)
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					break;
				}
			}
		}
	}
}