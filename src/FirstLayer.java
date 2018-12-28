
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
		// �ж��Ƿ��һ������,���Ϊ��һ�����������������ļ�/�ļ���
		// ������,��ֱ��pass
		/*
		 * Judge the existence of the movie's data, if it is the first time, it
		 * will generate the administartor's interface to load data to the
		 * system
		 */
		movieInfoList = this.FileExist(moviePath, screenPath);

		labelList = new ArrayList<JLabel>();
		imageList = new ArrayList<ImageIcon>();
		buttonList = new ArrayList<JButton>();

		// �ȶ�ȡmovies�ĵ�������,��ȷ�����ڵĴ�С
		// Read the data in the movie information's file to decide the size of
		// the windows
		infoList = new FileOp().readFile(moviePath + "/info/movies.txt");
		int windowHeight = infoList.size() * 110 + (infoList.size() + 1) * 5 + 30;
		int windowWidth = 325 + 2 * 5 + 10;

		// �Ͻ��Ļ���Ҫ�ȶ�ȡһ�������ļ�,�ڸմ򿪵�ʱ����жϵ�Ӱ��״̬
		movieBool_soldout = new MovieCheck().CheckMovieSoldout(infoList);
		movieBool_timeout = new MovieCheck().CheckMovieTime(movieArr);

		// Adding all of the movies' information to the panel
		for (int i = 0; i < infoList.size(); i++) {
			String x[] = infoList.get(i).split(";");

			JPanel tmpPanel = new JPanel();
			tmpPanel.setLayout(new BorderLayout());

			JLabel tmpLabel = null;

			// ���ж��Ƿ�����/����ʱ��,�����ݽ������label�ϵ���Ϣ
			// Decide the whether the movie has been sold out or not to
			// determine the label's information
			if (movieBool_soldout[i] && movieBool_timeout[i]) {
				// û������Ҳû�г���������ӳʱ��
				// Not soldout and timeout
				tmpLabel = new JLabel("<html>" + x[0] + "</html>");
			} else if (!movieBool_soldout[i] && movieBool_timeout[i]) {
				// �������
				// Sold out
				tmpLabel = new JLabel("<html><s>" + x[0] + "</s><br><font color=\"red\">SOLD OUT</font></html>");
			} else if ((movieBool_soldout[i] && !movieBool_timeout[i])
					|| (!movieBool_soldout[i] && !movieBool_timeout[i])) {
				// ���������ӳʱ��򳬹���ӳʱ��������
				// Time out(Including time out and sold out at the same time)
				tmpLabel = new JLabel("<html><s>" + x[0] + "</s><br><font color=\"red\">UNAVAILABLE</font></html>");
			} else {
			}

			// ��ӵ�Ӱ����label
			// Add the label for movie name
			labelList.add(tmpLabel);
			tmpLabel.setPreferredSize(new Dimension(150, 75));
			tmpPanel.add(tmpLabel, BorderLayout.WEST);

			// ��ӵ�Ӱʱ����label
			// Add the label for movie duration
			JPanel tmpRightPanel = new JPanel();
			tmpRightPanel.setLayout(new BorderLayout());
			tmpLabel = new JLabel(x[1] + "min");
			tmpLabel.setPreferredSize(new Dimension(55, 75));
			tmpRightPanel.add(tmpLabel, BorderLayout.WEST);

			// ��ӵ�Ӱ��ͼƬ��Ϣ(��������/��ʱ������ͼƬ������)
			// Add the movie's image (change the image according to the state of
			// the movie)
			ImageIcon tmpIcon = null;
			if (movieBool_soldout[i] && movieBool_timeout[i]) {
				// û������Ҳû�г���������ӳʱ��
				tmpIcon = new ImageIcon(moviePath + "/info/img/" + x[2] + ".jpg");
			} else if (!movieBool_soldout[i] && movieBool_timeout[i]) {
				// �������
				tmpIcon = new ImageIcon(moviePath + "/info/img/" + x[2] + "_soldout.jpg");
			} else if ((movieBool_soldout[i] && !movieBool_timeout[i])
					|| (!movieBool_soldout[i] && !movieBool_timeout[i])) {
				// ���������ӳʱ��򳬹���ӳʱ��������
				tmpIcon = new ImageIcon(moviePath + "/info/img/" + x[2] + "_timeout.jpg");
			} else {
			}

			// imagelist���ڶ��̼߳��ʱͼƬ�ĸ���
			/*
			 * imageList is used for changing the image when using the
			 * multithread to check the state of the movie
			 */
			imageList.add(tmpIcon);
			tmpRightPanel.add(new JLabel(tmpIcon), BorderLayout.EAST);
			tmpPanel.add(tmpRightPanel, BorderLayout.EAST);

			// ����Ӱ������label,һ��image��ӵ�button��
			// Add the two labels and one image to the button
			JButton tmpButton = new JButton();
			tmpButton.add(tmpPanel);
			tmpButton.addActionListener(this);

			if (movieBool_soldout[i] && movieBool_timeout[i]) {
				// û������Ҳû�г���������ӳʱ������button
				// Enable the button when the movie is available
				tmpButton.setEnabled(true);
			} else {
				// �����������button
				// Disable the button when the movie is unavailable
				tmpButton.setEnabled(false);
			}

			buttonList.add(tmpButton);
			this.getContentPane().add(tmpButton);
		}
		this.setLayout(new GridLayout(infoList.size(), 1, 0, 5));

		// �趨���ڵ�sizeλ�õ�
		// Set the size, location and etc. for the window
		this.setTitle("Ticketing Kiosk");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(windowWidth, windowHeight);
		// ������Ļ����
		// Set the window at the centre of the screen
		int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
		int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
		this.setLocation((screenWidth - windowWidth) / 2, (screenHeight - windowHeight) / 2);
		this.setVisible(true);

		// ������ͼ�ν����,�������߳��������movies��ʵʱ״̬
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

	// ���������Ҫ�������ļ�
	// 1. "./screens/screens.txt"
	// 2. "./movies/info/movies.txt"
	// ���������¼�����ļ�
	// 1. screensĿ¼���ļ�
	// 2. moviesĿ¼���ļ�

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

		// ���ȶ�ȡmovie��screen�������ļ�
		// First read the movie and screen's information
		ArrayList<String> movieInfo = null;
		ArrayList<String> screenInfo = null;
		
		movieInfo = new FileOp().readFile(movieCfg.getAbsolutePath());
		screenInfo = new FileOp().readFile(screenCfg.getAbsolutePath());

		// ������������ļ������ڵĻ�,��ֱ�ӽ�������������
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
			// ��������ļ�û�����ݵĻ�,˵�������ļ�������,��������
			// if the file have no content, it will terminate the program or let
			// the administrator to load the data
			JOptionPane.showMessageDialog(null, "The configuration file is broken, please reload the file!", "Error",
					JOptionPane.ERROR_MESSAGE);
			System.exit(0);
			// new Admin().addMoiveInfo();
		}

		// �ܻ�ȡ��Ӱ��("��Ӱ��Ӧ���ļ���"��ͬ��"��Ӱ��")
		// Get all the movies' name (different from the image name of the movie)
		ArrayList<String> movieList = new ArrayList<String>();
		ArrayList<List<Map<String, String>>> movieInfoList = new ArrayList<List<Map<String, String>>>();
		for (int i = 0; i < movieInfo.size(); i++) {
			movieList.add(movieInfo.get(i).split(";")[2]);
			movieInfoList.add(this.getMovieInfo(movieList.get(i), screenCfg.getAbsolutePath()));
		}

		// ÿ����Ӱÿ��Ӱ��ÿ��ʱ���Ʊ��
		// The ticket number for each screen
		ArrayList<ArrayList<ArrayList<String>>> ticketNum = new ArrayList<ArrayList<ArrayList<String>>>();
		// ÿ����Ӱ����ĩʱ��
		// The array stores the latest time of the movie
		movieArr = new int[movieList.size()];
		// ÿ����Ӱ�ĵ�ӰƱID
		// The array stores the IDs of the movies
		ArrayList<String> ticketNo;
		// ��Ʊ��
		// Total ticket number
		int ticketAllNum = 0;

		// ��ÿ����Ӱ��ÿ��Ӱ����ÿ��ʱ��� Ʊ��ȡ��
		// ��ȡ����ĩ����ʱ�������ж��ܷ�Ʊ

		// ѭ��ÿ����Ӱ
		for (int i = 0; i < movieInfoList.size(); i++) {
			// ѭ��ÿ��screen
			ticketNum.add(new ArrayList<ArrayList<String>>());
			int movieTime = 0;
			for (int j = 0; j < movieInfoList.get(i).size(); j++) {
				// ѭ��ÿ��ʱ��
				ticketNum.get(i).add(new ArrayList<String>());
				int movieTime1 = 0;
				for (String key : movieInfoList.get(i).get(j).keySet()) {
					if (!key.equals("screen")) {
						// ��ȡÿ����Ӱÿ��screenÿ��ʱ���Ʊ��
						// Get each movie's each screen's each time's ticket
						// number
						ticketNum.get(i).get(j).add(movieInfoList.get(i).get(j).get(key));
						// ��ȡ��Ʊ��
						// Get total ticket number in order to generate the ID
						ticketAllNum += Integer.parseInt(movieInfoList.get(i).get(j).get(key));
						// ���ʱ��С���µķ�ӳʱ����ֵ�µĵ�Ӱʱ��,���򲻱�
						// If the time is less than the last one, the movieTime
						// will be the new time
						movieTime1 = (movieTime1 <= Integer.parseInt(key)) ? (Integer.parseInt(key)) : (movieTime1);
					}
				}
				movieTime = (movieTime <= movieTime1) ? (movieTime1) : (movieTime);
			}
			movieArr[i] = movieTime;
		}

		// ����Ʊ��ID(������ȫ��Ʊ��ID�ٸ��ݲ�ͬ�ĳ���/��ͬ�ĵ�Ӱ���з���)
		// Generate all the ticket(ID)
		ticketNo = new MathOp().genTicket(ticketAllNum);

		// System.exit(0);

		// ArrayList<String> pathList = new ArrayList<String>();
		// ArrayList<String> fileList = new ArrayList<String>();
		// �������Ӹ����˵����:���粿���ļ�/�ļ���ȷʵ
		// ������ʾ�û��ֶ��޸�/�Զ��޸�/ֱ�ӹر�

		File tmp = new File(moviePath + "/tickets");
		// �����ص������ļ�������,������(��һ�δ�)
		// �������,��pass(���ǵ�һ�δ�,ֱ�Ӷ�ȡԭ�м�¼)
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

			// ��·�������ڷ������ɵĵ�ӰƱ�ļ�
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

			// ��·�������ڷ������ɵı����ļ�
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
			// ѭ��ÿ����Ӱ
			// For each movie
			for (int i = 0; i < movieInfoList.size(); i++) {
				// ÿ����Ӱ����Ʊ��
				int screenCurrentNum = 0;
				// For each screen
				for (int j = 0; j < movieInfoList.get(i).size(); j++) {
					// ����screen�ļ���
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

					// ����ÿ��screen��movie���ļ���
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
					// ѭ��ÿ������
					// For each time
					for (String key : movieInfoList.get(i).get(j).keySet()) {
						if (!key.equals("screen")) {
							// ���ɳ����ļ���(ʱ��)
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

							// ����������Ӧ���ļ�
							// seat.txt ������ÿ��screen����λ�����(trueΪδ�۳�,falseΪ�۳�)
							// ticket_remain.txt ������δ������Ʊ(ticket id)
							// ticket_sold.txt ������������Ʊ(ticket id�Լ�Ʊ�ľ�����Ϣ)

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

							// д��seat.txt(������λ��д�������true)
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

							// д��ticket_remain.txt(������λ��д���Ӧ����ticket ID)
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
							// ��д�������м���,һ�����ticket id
							// Count the number of the ticket ID in order to
							// allocate the ticket IDs
							screenCurrentNum += Integer.parseInt(movieInfoList.get(i).get(j).get(key));
						}
					}
				}

				// ���ɵ�Ӱ��ticket�Լ����ļ���(��ͬ��screen)
				// ticket_remain.txt δ�۳���ticket id
				// ticket_sold.txt �۳���ticket id�Լ�Ʊ������,screen��,��Ӱ����
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
				// ��������������ticket id
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
		// ��ȡscreen�����ļ��ڵ���������
		// Get all the informations from the movie configuration file
		ArrayList<String> screenInfoList = new FileOp().readFile(screenPath);

		String num = null;
		Map<String, String> map = null;
		// ѭ��ȡ�����ж�Ӧ��ֵ(screen��,��Ӧ����λ��,��Ӧ��Ӱ�ĳ�����Ϣ)
		// Get all information of the movie
		for (int i = 0; i < screenInfoList.size(); i++) {
			// ���screen��
			// Add screen name
			if (screenInfoList.get(i).indexOf("screen") != -1) {
				num = screenInfoList.get(i).split(";")[1];
				String screenNo = screenInfoList.get(i).split(";")[0];

				map = new HashMap<String, String>();
				map.put("screen", screenNo);
			} else if (screenInfoList.get(i).indexOf(movieName) != -1) {
				// ��ӵ�Ӱ�ĳ���(ʱ��),��λ��
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
				// ����������movie�ļ����,һ��Ϊ�Ƿ�����(sold out),��һ��Ϊ�Ƿ񳬹���ӳʱ��(time
				// out),TRUEΪ���Լ�������,FALSEΪ���ܼ�������
				/*
				 * Use two boolean array to store the result of the check
				 * result. The first is to check whether the ticket is sold out,
				 * The second is to check whether the time is over the schedule
				 */
				movieBool_soldout = new MovieCheck().CheckMovieSoldout(infoList);
				movieBool_timeout = new MovieCheck().CheckMovieTime(movieArr);

				// ���GUI��û�����button�򲻽��м��
				// If there is no button in the panel, the checking process will
				// not process
				if (buttonList.size() == 0) {
				} else {
					int beginIndex = 0;
					int endIndex = 0;

					// ѭ�����GUI�е�����button
					// Check all buttons(movies) in the list
					for (int i = 0; i < movieBool_soldout.length; i++) {
						String movieName = infoList.get(i).split(";")[2];

						// ��Ϊ�������,һΪ��������,��Ϊ�����˲�������,��Ϊ������ʼʱ�䲻�������Լ�ͬʱ����/��ʱ��������
						// It has three status, the first is available, the
						// second is sold out, the third is timeout
						if ((movieBool_soldout[i] == true) && (movieBool_timeout[i] == true)) {
							// ���label�ϵ�textû��<s>(������),�򲻸���״̬(��Ϊԭ�����ǿ���������)
							// If the text on the button does not contain <s>,
							// it means the movie is still avaiable, the state
							// will not update
							if (labelList.get(i).getText().indexOf("<s>") == -1) {
							} else {
								// ���ԭ��������(����������),���ȡ<s></s>�е�����,���¹���label�ϵ�text
								// ������button(����=����button)
								// If the text on the button contain <s>, then
								// extract the content in between the <s>
								buttonList.get(i).setEnabled(true);
								// ͬʱ������Ӱ��ͼƬ(��Ϊ�����Ĳ�ɫͼƬ)
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
							// ���ԭ��û����,���ȡlabel��<html></html>�еĲ������¹���text
							if (labelList.get(i).getText().indexOf("<s>") == -1) {
								// ��enable button���ܽ��и���button�ϵ�ͼƬ
								buttonList.get(i).setEnabled(true);
								imageList.get(i)
										.setImage(new ImageIcon(moviePath + "/info/img/" + movieName + "_soldout.jpg")
												.getImage());
								// ������button(��Ϊ������)
								// Disable the button
								buttonList.get(i).setEnabled(false);
								beginIndex = labelList.get(i).getText().indexOf("<html>");
								endIndex = labelList.get(i).getText().indexOf("</html>");
								labelList.get(i)
										.setText("<html><s>"
												+ labelList.get(i).getText().substring(beginIndex + 6, endIndex)
												+ "</s><br><font color=\"red\">SOLD OUT</font></html>");
							} else {
								// ���ԭ��������,���ȡlabel��<s></s>�еĲ������¹���text
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
								|| ((movieBool_soldout[i] == false) && (movieBool_timeout[i] == false))) { // ��ʱ
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