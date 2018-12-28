import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class TicketTest {
	Ticket studentTicket = null;
	Map<String, String> info = null;
	String movieStr = null;
	String screenStr = null;
	String timeStr = null;
	String seatStr = null;
	String typeStr = null;
	String studentIdStr = null;
	String priceStr = null;
	String ticketId = null;

	@Before
	public void setUp() throws Exception {
		info = new HashMap<String, String>();
		for (int i = 0; i < 5; i++)
			info.put("key" + i, "value" + i);
		movieStr = "movieName";
		screenStr = "screen1";
		timeStr = "10:00";
		seatStr = "A1";
		typeStr = "Student Ticket";
		studentIdStr = "1234";
		priceStr = "20";
		ticketId = "12345678";
		studentTicket = new Ticket(info, movieStr, screenStr, timeStr, seatStr, typeStr, studentIdStr, priceStr);
		studentTicket.setTicketId(ticketId);
	}

	@Test
	public void testToString() {
		assertEquals(
				"Ticket ID:12345678\r\nMovie Name:movieName\r\nScreen:screen1\r\nTime:10:00\r\nSeat:A1\r\nTicket Type:Student Ticket\r\nStudent ID:1234\r\nPrice:20pounds",
				studentTicket.toString());

	}

	@Test
	public void testGetInfo() {
		assertEquals(info, studentTicket.getInfo());
	}

	@Test
	public void testGetMovieStr() {
		assertEquals(movieStr, studentTicket.getMovieStr());
	}

	@Test
	public void testGetScreenStr() {
		assertEquals(screenStr, studentTicket.getScreenStr());
	}

	@Test
	public void testGetTimeStr() {
		assertEquals(timeStr, studentTicket.getTimeStr());
	}

	@Test
	public void testGetTypeStr() {
		assertEquals(typeStr, studentTicket.getTypeStr());
	}

	@Test
	public void testGetStudentIdStr() {
		assertEquals(studentIdStr, studentTicket.getStudentIdStr());
	}

	@Test
	public void testGetSeatStr() {
		assertEquals(seatStr, studentTicket.getSeatStr());
	}

	@Test
	public void testGetTicketId() {
		assertEquals(ticketId, studentTicket.getTicketId());
	}

	@Test
	public void testGetPriceStr() {
		assertEquals(priceStr, studentTicket.getPriceStr());
	}

}
