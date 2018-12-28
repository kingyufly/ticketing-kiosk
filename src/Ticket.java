
/**
 * Ticket.java
 * This class contains all information of the user's choice
 * 
 * @author kingyufly
 * @version 2.2
 * 
 * */

import java.util.ArrayList;
import java.util.Map;

public class Ticket {
	private Map<String, String> info;

	private String movieStr;
	private String screenStr;
	private String timeStr;
	private String typeStr;
	private String studentIdStr;
	private String seatStr;
	private String ticketId;
	private String priceStr;

	/**
	 * This constructor is to generate the student ticket object
	 * 
	 * @param info
	 *            The user's choice mapping
	 * @param movieStr
	 *            The movie's name(can be easily read by human-beings)
	 * @param screenStr
	 *            The screen's name
	 * @param timeStr
	 *            The time's string
	 * @param seatStr
	 *            The seat's number(e.g. D3,B2...)
	 * @param typeStr
	 *            The ticket type
	 * @param studentIdStr
	 *            The student id
	 * @param priceStr
	 *            The price in pound
	 */
	// 学生票
	public Ticket(Map<String, String> info, String movieStr, String screenStr, String timeStr, String seatStr,
			String typeStr, String studentIdStr, String priceStr) {
		this.setInfo(info);
		this.setMovieStr(movieStr);
		this.setScreenStr(screenStr);
		this.setTimeStr(timeStr);
		this.setSeatStr(seatStr);
		this.setTypeStr(typeStr);
		this.setStudentIdStr(studentIdStr);
		this.setPriceStr(priceStr);
	}

	/**
	 * This constructor is to generate the child/adult/senior ticket object
	 * 
	 * @param info
	 *            The user's choice mapping
	 * @param movieStr
	 *            The movie's name(can be easily read by human-beings)
	 * @param screenStr
	 *            The screen's name
	 * @param timeStr
	 *            The time's string
	 * @param seatStr
	 *            The seat's number(e.g. D3,B2...)
	 * @param typeStr
	 *            The ticket type
	 * @param priceStr
	 *            The price in pound
	 */
	// 成人票,儿童票,老年票
	public Ticket(Map<String, String> info, String movieStr, String screenStr, String timeStr, String seatStr,
			String typeStr, String priceStr) {
		this.setInfo(info);
		this.setMovieStr(movieStr);
		this.setScreenStr(screenStr);
		this.setTimeStr(timeStr);
		this.setSeatStr(seatStr);
		this.setTypeStr(typeStr);
		this.setPriceStr(priceStr);
	}

	/**
	 * method genTicket This method is to write all informations that can be
	 * used by human-beings(MovieStr,ScreenStr,TimeStr...) to the ticket file.
	 * And it also change the ticket's status in the file system.
	 */

	public void genTicket() {
		String filePath[] = { "./movies/tickets/" + info.get("movie") + "/ticket_remain.txt",
				"./movies/tickets/" + info.get("movie") + "/ticket_sold.txt",
				"./screens/" + info.get("screen") + "/" + info.get("movie") + "/" + info.get("time")
						+ "/ticket_remain.txt",
				"./screens/" + info.get("screen") + "/" + info.get("movie") + "/" + info.get("time")
						+ "/ticket_sold.txt",
				"./screens/" + info.get("screen") + "/" + info.get("movie") + "/" + info.get("time") + "/seat.txt" };
		FileOp fo = new FileOp();
		String contents = "";

		// 修改screen中数据(修改ticket_remain.txt)
		/*
		 * modify the data for the screen which is ticket_remain.txt
		 */
		// 读取ticket id储存文档
		// Get a ID for the ticket form the corresponding ID database
		ArrayList<String> idList = fo.readFile(filePath[2]);

		this.setTicketId(idList.get(0));
		idList.remove(0);
		contents = "";
		for (int i = 0; i < idList.size(); i++) {
			contents += idList.get(i) + "\r\n";
		}
		// 修改ticket id文档(从screen和movie中去掉刚才取出来的那个id)
		// Delete the selected ID from the screen's ID base
		fo.writeFile(contents, filePath[2], false); // 修改screen中数据

		// 修改movie中数据(修改ticket_remain.txt)
		// Delete the selected ID from the movie's ID base
		idList = fo.readFile(filePath[0]);

		idList.remove(this.getTicketId());
		contents = "";
		for (int i = 0; i < idList.size(); i++) {
			contents += idList.get(i) + "\r\n";
		}
		// 修改ticket id文档(从screen和movie中去掉刚才取出来的那个id)
		// Modify the ticket_id file (remove the added ID from the screen and movie file)
		fo.writeFile(contents, filePath[0], false); // 修改movie中数据

		/*
		 * 修改screen中数据(增加到ticket_sold.txt)
		 * Modify the data for the screen
		 */

		contents = this.getTicketId() + ";" + info.get("type") + "\r\n";
		fo.writeFile(contents, filePath[3], true); // 修改movie中数据

		/*
		 * 修改movie中数据(修改ticket_sold.txt)
		 * Modify the data for the movie
		 */

		contents = this.getTicketId() + ";" + info.get("type") + ";" + info.get("screen") + "\r\n";
		fo.writeFile(contents, filePath[1], true); // 修改movie中数据

		// 修改seat文件(将坐位变false)
		// seat为顺序排列,seat编号是通过screen计算出来的,所以可以直接进行修改
		// Modify the seat file
		idList = fo.readFile(filePath[4]);

		contents = "";
		for (int i = 0; i < idList.size(); i++) {
			if (i == (Integer.parseInt(info.get("seat")))) {
				contents += "false" + "\r\n";
			} else {
				contents += idList.get(i) + "\r\n";
			}
		}
		fo.writeFile(contents, filePath[4], false); // 修改seat中数据

		// 生成电影票文档
		fo.writeFile(this.toString(), "./tickets/" + this.getTicketId() + ".txt", false);
	}

	/**
	 * method toString This method is override the default method, and it
	 * returns all information of the ticket so that the ticket object can be
	 * write to files
	 * 
	 * @return String returns the content of the ticket in String format
	 */
	@Override
	public String toString() {
		if (this.getStudentIdStr() != null)
			return "Ticket ID:" + this.getTicketId() + "\r\nMovie Name:" + this.getMovieStr() + "\r\nScreen:"
					+ this.getScreenStr() + "\r\nTime:" + this.getTimeStr() + "\r\nSeat:" + this.getSeatStr()
					+ "\r\nTicket Type:" + this.getTypeStr() + "\r\nStudent ID:" + this.getStudentIdStr() + "\r\nPrice:"
					+ this.getPriceStr() + "pounds";
		else
			return "Ticket ID:" + this.getTicketId() + "\r\nMovie Name:" + this.getMovieStr() + "\r\nScreen:"
					+ this.getScreenStr() + "\r\nTime:" + this.getTimeStr() + "\r\nSeat:" + this.getSeatStr()
					+ "\r\nTicket Type:" + this.getTypeStr() + "\r\nPrice:" + this.getPriceStr() + "pounds";
	}

	/**
	 * method getInfo This method is to get the info
	 * 
	 * @return Map<String, String> the info
	 */

	public Map<String, String> getInfo() {
		return info;
	}

	/**
	 * method setInfo This method is to set the info
	 * 
	 * @param info
	 *            The value to be set
	 */

	public void setInfo(Map<String, String> info) {
		this.info = info;
	}

	/**
	 * method getMovieStr This method is to get the MovieStr
	 * 
	 * @return String the MovieStr
	 */

	public String getMovieStr() {
		return movieStr;
	}

	/**
	 * method setMovieStr This method is to set the MovieStr
	 * 
	 * @param movieStr
	 *            The value to be set
	 */

	public void setMovieStr(String movieStr) {
		this.movieStr = movieStr;
	}

	/**
	 * method getScreenStr This method is to get the ScreenStr
	 * 
	 * @return String the ScreenStr
	 */

	public String getScreenStr() {
		return screenStr;
	}

	/**
	 * method setScreenStr This method is to set the ScreenStr
	 * 
	 * @param screenStr
	 *            The value to be set
	 */

	public void setScreenStr(String screenStr) {
		this.screenStr = screenStr;
	}

	/**
	 * method getTimeStr This method is to get the TimeStr
	 * 
	 * @return String the TimeStr
	 */

	public String getTimeStr() {
		return timeStr;
	}

	/**
	 * method setTimeStr This method is to set the TimeStr
	 * 
	 * @param timeStr
	 *            The value to be set
	 */

	public void setTimeStr(String timeStr) {
		this.timeStr = timeStr;
	}

	/**
	 * method getTypeStr This method is to get the TypeStr
	 * 
	 * @return String the TypeStr
	 */

	public String getTypeStr() {
		return typeStr;
	}

	/**
	 * method setTypeStr This method is to set the TypeStr
	 * 
	 * @param typeStr
	 *            The value to be set
	 */

	public void setTypeStr(String typeStr) {
		this.typeStr = typeStr;
	}

	/**
	 * method getStudentIdStr This method is to get the StudentIdStr
	 * 
	 * @return String the StudentIdStr
	 */

	public String getStudentIdStr() {
		return studentIdStr;
	}

	/**
	 * method setStudentIdStr This method is to set the StudentIdStr
	 * 
	 * @param studentIdStr
	 *            The value to be set
	 */

	public void setStudentIdStr(String studentIdStr) {
		this.studentIdStr = studentIdStr;
	}

	/**
	 * method getSeatStr This method is to get the SeatStr
	 * 
	 * @return String the SeatStr
	 */

	public String getSeatStr() {
		return seatStr;
	}

	/**
	 * method setSeatStr This method is to set the SeatStr
	 * 
	 * @param seatStr
	 *            The value to be set
	 */

	public void setSeatStr(String seatStr) {
		this.seatStr = seatStr;
	}

	/**
	 * method getTicketId This method is to get the TicketId
	 * 
	 * @return String the TicketId
	 */

	public String getTicketId() {
		return ticketId;
	}

	/**
	 * method setTicketId This method is to set the TicketId
	 * 
	 * @param ticketId
	 *            The value to be set
	 */

	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}

	/**
	 * method getPriceStr This method is to get the PriceStr
	 * 
	 * @return String the PriceStr
	 */

	public String getPriceStr() {
		return priceStr;
	}

	/**
	 * method setPriceStr This method is to set the PriceStr
	 * 
	 * @param priceStr
	 *            The value to be set
	 */

	public void setPriceStr(String priceStr) {
		this.priceStr = priceStr;
	}
}