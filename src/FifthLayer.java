
/**
 * FifthLayer.java
 * The 5th Layer of the GUI
 * Show the information of the ticket and wait for user to pay or back to rechoose or exit the software 
 * 
 * @author kingyufly
 * @version 2.2
 * 
 * */

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * The 5th Layer GUI class It contains the information of the ticket(unpaid)
 */

public class FifthLayer extends JFrame implements ActionListener {
	JButton confirm;
	JButton cancel;
	JButton back;
	Map<String, String> infoTrans;
	ArrayList<JLabel> labelList;
	List<Map<String, String>> secondLayerInfoDup;

	/**
	 * Override the default constructor In the fifth layer, and generate the
	 * ticket information according to user's choose
	 * 
	 * @param info
	 *            The mapping of keys and values which contains the user's
	 *            choice
	 * 
	 * @param secondLayerInfo
	 *            Used to return back to the second layer
	 */

	public FifthLayer(Map<String, String> info, List<Map<String, String>> secondLayerInfo) {
		infoTrans = info;
		secondLayerInfoDup = secondLayerInfo;

		labelList = new ArrayList<JLabel>();

		// Used to get the first and second column's width
		int width1 = 0;
		int width2 = 0;
		// Get all key and the corresponding value
		for (String key : info.keySet()) {
			// movie is the key for the image, so beside "movie" all keys will
			// be get
			if (!key.equals("movie")) {
				String tmp = "";
				switch (key) {
				case "movie": {
					tmp = "Movie: ";
					break;
				}
				case "screen": {
					tmp = "Screen: ";
					break;
				}
				case "time": {
					tmp = "Time: ";
					break;
				}
				case "seat": {
					tmp = "Seat: ";
					break;
				}
				case "type": {
					tmp = "Type: ";
					break;
				}
				case "studentId": {
					tmp = "Student ID: ";
					break;
				}
				}

				// Decide the width of the key and use the largest as the
				// column's width
				if (width1 <= tmp.length() * 7)
					width1 = tmp.length() * 7;

				// 有时key并不是真正显示的,所以要switch(key),用真正要现实的string去判断长度
				// Sometimes, the key and its value is not readable for human
				if (key.equals("type")) {
					switch (info.get(key)) {
					case "0": {
						if (width2 <= "Child Ticket".length() * 7)
							width2 = "Child Ticket".length() * 7;
						break;
					}
					case "1": {
						if (width2 <= "Adult Ticket".length() * 7)
							width2 = "Adult Ticket".length() * 7;
						break;
					}
					case "2": {
						if (width2 <= "Senoir Ticket".length() * 7)
							width2 = "Senoir Ticket".length() * 7;
						break;
					}
					case "3": {
						if (width2 <= "Student Ticket".length() * 7)
							width2 = "Student Ticket".length() * 7;
						if (width2 <= info.get("studentId").length() * 7)
							width2 = info.get("studentId").length() * 7;
						break;
					}
					default: {
						if (width2 <= "Adult Ticket".length() * 7)
							width2 = "Adult Ticket".length() * 7;
						break;
					}
					}
				} else {
					if (width2 <= info.get(key).length() * 7)
						width2 = info.get(key).length() * 7;
				}

			}
		}

		// price并不在key里所以要单独判断
		// Price is not in the list so it should be judge separately
		if (width1 <= "Price: ".length() * 7)
			width1 = "Price: ".length() * 7;

		// 确保最低宽度(即要保证宽度要大于等于三个button的总宽度)
		// Ensure the minimum width of the column (that is the width is larger
		// than the width of three buttons)
		if (width2 < (225 - 113 - width1))
			width2 = (225 - 113 - width1);

		// 当前默认panel上添加电影名label
		// JLable.RIGHT为右对齐; JLable.LEFT为左对齐; JLable.CENTER为居中
		// Add the movie name label
		labelList.add(new JLabel("Movie: ", JLabel.RIGHT));
		labelList.add(new JLabel(info.get("moviename")));

		// 通过判断screen的值来判断座位号/screen名称,并添加时间
		// Decide the screen's string and add time to the panel
		switch (info.get("screen")) {
		case "screen1": {
			labelList.add(new JLabel("Screen: ", JLabel.RIGHT));
			labelList.add(new JLabel("No.1"));
			labelList.add(new JLabel("Time: ", JLabel.RIGHT));
			labelList.add(new JLabel(info.get("time").substring(0, 2) + ":" + info.get("time").substring(2, 4)));
			int seat = Integer.parseInt(info.get("seat"));
			labelList.add(new JLabel("Seat: ", JLabel.RIGHT));
			labelList.add(new JLabel("" + (char) ('D' - seat / 8) + (8 - seat % 8)));
			break;
		}
		case "screen2": {
			labelList.add(new JLabel("Screen: ", JLabel.RIGHT));
			labelList.add(new JLabel("No.2"));
			labelList.add(new JLabel("Time: ", JLabel.RIGHT));
			labelList.add(new JLabel(info.get("time").substring(0, 2) + ":" + info.get("time").substring(2, 4)));
			int seat = Integer.parseInt(info.get("seat"));
			labelList.add(new JLabel("Seat: ", JLabel.RIGHT));
			if (seat < 8) {
				labelList.add(new JLabel("D" + (8 - seat)));
			} else {
				seat -= 8;
				labelList.add(new JLabel("" + (char) ('C' - seat / 6) + (6 - seat % 6)));
			}
			break;
		}
		case "screen3": {
			labelList.add(new JLabel("Screen: ", JLabel.RIGHT));
			labelList.add(new JLabel("No.3"));
			labelList.add(new JLabel("Time: ", JLabel.RIGHT));
			labelList.add(new JLabel(info.get("time").substring(0, 2) + ":" + info.get("time").substring(2, 4)));
			int seat = Integer.parseInt(info.get("seat"));
			labelList.add(new JLabel("Seat: ", JLabel.RIGHT));
			if (seat < 8) {
				labelList.add(new JLabel("E" + (8 - seat)));
			} else {
				seat -= 8;
				labelList.add(new JLabel("" + (char) ('D' - seat / 6) + (6 - seat % 6)));
			}
			break;
		}
		default: {
		}
		}

		// 生成价格,并显示对应的优惠政策
		// Add price and the corresponding discount according to the ticket type
		float price = 16f;
		String discountStr = "";

		switch (info.get("type")) {
		case "0": {
			price *= 0.5;
			discountStr = " &#42;50% off";
			labelList.add(new JLabel("Type: ", JLabel.RIGHT));
			labelList.add(new JLabel("Child Ticket"));
			break;
		}
		case "1": {
			price *= 1.0;
			labelList.add(new JLabel("Type: ", JLabel.RIGHT));
			labelList.add(new JLabel("Adult Ticket"));
			break;
		}
		case "2": {
			price *= 0.8;
			discountStr = " &#42;20% off";
			labelList.add(new JLabel("Type: ", JLabel.RIGHT));
			labelList.add(new JLabel("Senoir Ticket"));
			break;
		}
		case "3": {
			price *= 0.85;
			discountStr = " &#42;15% off";
			labelList.add(new JLabel("Type: ", JLabel.RIGHT));
			labelList.add(new JLabel("Student Ticket"));

			// 如果是学生票还要显示student id
			if (info.get("studentId") != null) {
				labelList.add(new JLabel("Student ID: ", JLabel.RIGHT));
				labelList.add(new JLabel(info.get("studentId")));
			}
			break;
		}
		default: {
			price *= 1.0;
		}
		}

		// 将价格取两位小数
		DecimalFormat fnum = new DecimalFormat("##0.00");
		labelList.add(new JLabel("Price: ", JLabel.RIGHT));
		labelList.add(new JLabel(
				"<html>£" + fnum.format(price) + "<font color=\"red\" size=2>" + discountStr + "</font><html>"));

		int height = 150 / (info.size() - 1);

		// 设定各个label/image/button的位置
		// Set the location of labels, images and buttons
		for (int i = 0; i < labelList.size() - 2; i += 2) {
			labelList.get(i).setBounds(0, i / 2 * height, width1, height);
			labelList.get(i + 1).setBounds(width1, i / 2 * height, width2, height);
			this.getContentPane().add(labelList.get(i));
			this.getContentPane().add(labelList.get(i + 1));
		}

		int windowWidth = width1 + width2 + 113;
		int windowHeight = 30 + 50 + 150 + 30;

		// 一次添加一行(一行有两个label,左边为key,右边为key对应的vakue)
		// Add the key and the corresponding value to the panel, te key is on
		// the left and the value is on the right
		labelList.get(labelList.size() - 2).setBounds(0, 150, width1, 30);
		labelList.get(labelList.size() - 1).setBounds(width1, 150, (windowWidth - width1), 30);
		this.getContentPane().add(labelList.get(labelList.size() - 2));
		this.getContentPane().add(labelList.get(labelList.size() - 1));

		// 添加电影图片(大图 150X113)
		// Add the movie's picture in 150x113 resolution
		JLabel tmp = new JLabel(new ImageIcon("./movies/info/img/" + info.get("movie") + "_big.jpg"));
		tmp.setBounds(width1 + width2, 0, 113, 150);
		this.getContentPane().add(tmp);

		// 增加付款(模拟),取消,返回按键
		// Add "pay" button, "cancel" button and "back" button
		confirm = new JButton("Pay");
		cancel = new JButton("Cancel");
		back = new JButton("Back");

		// 设定位置,size
		// Set the buttons' location
		confirm.setBounds((windowWidth - 225) / 4, 190, 75, 30);
		cancel.setBounds((windowWidth - 75) / 2, 190, 75, 30);
		back.setBounds((windowWidth * 3 - 75) / 4, 190, 75, 30);

		// 添加监听
		// Add listeners to the buttons
		confirm.addActionListener(this);
		cancel.addActionListener(this);
		back.addActionListener(this);

		// 添加buttons至当前默认panel
		// Add buttons to the panel
		this.getContentPane().add(confirm);
		this.getContentPane().add(cancel);
		this.getContentPane().add(back);

		// 因为是固定layout所以要设定为null
		// Set the layout to null since the layout is set by our own
		this.setLayout(null);
		this.setTitle("Confirm to Pay");
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
		// Judge the source of the buttons
		if (eventSource.equals(confirm)) {
			// Decide the location of the price string(which is between
			// "<html>price<font ....>")
			int beginIndex = labelList.get(labelList.size() - 1).getText().indexOf("<html>");
			int endIndex = labelList.get(labelList.size() - 1).getText().indexOf("<font");
			// Judge the whether the ticket is student ticket and generate the
			// corresponding object
			if (infoTrans.get("studentId") == null) { // Not student ticket
				new SixthLayer(new Ticket(infoTrans, labelList.get(1).getText(), labelList.get(3).getText(),
						labelList.get(5).getText(), labelList.get(7).getText(), labelList.get(9).getText(),
						labelList.get(11).getText().substring(beginIndex + 7, endIndex)));
			} else { // Student ticket
				// 如果是学生票,则生成对应的ticket实例(有student id传参)
				new SixthLayer(new Ticket(infoTrans, labelList.get(1).getText(), labelList.get(3).getText(),
						labelList.get(5).getText(), labelList.get(7).getText(), labelList.get(9).getText(),
						labelList.get(11).getText(), labelList.get(13).getText().substring(beginIndex + 7, endIndex)));
			}
			this.dispose();
		} else if (eventSource.equals(cancel)) {
			// If the user press "cancel" button, it will exit the system
			System.exit(0);
		} else if (eventSource.equals(back)) {
			// If the user press the "back" button, the program will back to the
			// fourth layer which is choosing the seat.
			this.dispose();
			infoTrans.remove("seat");
			new FourthLayer(infoTrans, secondLayerInfoDup);
		} else {
		}
	}
}
