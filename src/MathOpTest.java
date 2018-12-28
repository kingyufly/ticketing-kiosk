import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MathOpTest {
	MathOp mo = null;

	@Before
	public void setUp() throws Exception {
		mo = new MathOp();
	}

	@Test
	public void testGenTicket() {
		System.out.println(mo.genTicket(10));
		assertEquals(1, 1);
	}

}
