
/**
 * SixthLayer.java
 * The 6th Layer of the GUI
 * Show the ticket(including the ticket id) and wait for user continue to buy ticket or exit the software
 * 
 * @author kingyufly
 * @version 2.2
 * 
 * */

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * The 6th Layer GUI class
 * 
 */

// 具体内容同fifthlayer,只不过没有图片,按键变为两个
public class SixthLayer extends JFrame implements ActionListener {
	JButton cont;
	JButton cancel;

	ArrayList<String> keyList;
	ArrayList<String> valueList;
	ArrayList<JLabel> labelList;

	public SixthLayer(Ticket ticket) {
		// 生成电影票文件
		// generate the file for the movie
		ticket.genTicket();

		// 生成类似FiftLayer的图形界面
		// Generate the GUI like the fifth layer
		keyList = new ArrayList<String>();
		valueList = new ArrayList<String>();
		labelList = new ArrayList<JLabel>();

		keyList.add("ID:");
		keyList.add("Movie:");
		keyList.add("Screen:");
		keyList.add("Time:");
		keyList.add("Seat:");
		keyList.add("Type:");
		if (ticket.getStudentIdStr() != null)
			keyList.add("Student ID:");
		keyList.add("Price:");

		valueList.add(" " + ticket.getTicketId());
		valueList.add(" " + ticket.getMovieStr());
		valueList.add(" " + ticket.getScreenStr());
		valueList.add(" " + ticket.getTimeStr());
		valueList.add(" " + ticket.getSeatStr());
		valueList.add(" " + ticket.getTypeStr());
		if (ticket.getStudentIdStr() != null)
			valueList.add(" " + ticket.getStudentIdStr());
		valueList.add(" £" + ticket.getPriceStr());

		int width1 = 0;
		int width2 = 0;
		for (int i = 0; i < keyList.size(); i++) {
			if (width1 <= keyList.get(i).length() * 7)
				width1 = keyList.get(i).length() * 7;
			if (width2 <= valueList.get(i).length() * 7)
				width2 = valueList.get(i).length() * 7;

			labelList.add(new JLabel(keyList.get(i), JLabel.RIGHT));
			labelList.add(new JLabel(valueList.get(i)));
		}

		// Ensure the minimum of the width of the window
		if (width2 < (180 - width1))
			width2 = (180 - width1);

		for (int i = 0; i < labelList.size(); i += 2) {
			labelList.get(i).setBounds(0, i / 2 * 25, width1, 25);
			labelList.get(i + 1).setBounds(width1, i / 2 * 25, width2, 25);
			this.getContentPane().add(labelList.get(i));
			this.getContentPane().add(labelList.get(i + 1));
		}

		int windowWidth = width1 + width2 + 30;
		int windowHeight = keyList.size() * 25 + 20 + 10 + 30 + 10 + 30;

		// 生成按键的提示(即按"continue"会返回继续购票,按"canael"会退出程序)
		// Generate the notification of the buttons
		JLabel tmp = new JLabel(
				"<html><font size=1>   &#42;Press <i>CONTINUE</i> to buy more<br>   &#42;Press <i>CANCEL</i> to exit</font></html>");
		cont = new JButton("Continue");
		cancel = new JButton("Cancel");

		tmp.setBounds(10, keyList.size() * 25, windowWidth, 20);
		cont.setBounds((windowWidth - 180) / 4, keyList.size() * 25 + 30, 90, 30);
		cancel.setBounds((windowWidth * 3 - 180) / 4, keyList.size() * 25 + 30, 90, 30);

		cont.addActionListener(this);
		cancel.addActionListener(this);

		this.getContentPane().add(tmp);
		this.getContentPane().add(cont);
		this.getContentPane().add(cancel);

		this.setLayout(null);
		this.setTitle("Ticket Info");
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
		// 如果点击继续,则返回firstlayer
		// If the button is clicked, then return to the first layer continue to buy tickets
		if (eventSource.equals(cont)) {
			this.dispose();
			new FirstLayer();
		} else if (eventSource.equals(cancel)) {
			// 如果点击cancel则退出程序
			// If cancel is clicked, then exit the program
			System.exit(0);
		} else {
		}
	}
}
