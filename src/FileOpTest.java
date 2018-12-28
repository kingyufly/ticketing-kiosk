import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class FileOpTest {
	FileOp fo = null;
	ArrayList<String> al = null;

	@Before
	public void setUp() throws Exception {
		fo = new FileOp();
		al = new ArrayList<String>();
		for (int i = 1; i < 5; i++)
			al.add("" + i);
	}

	@Test
	public void testReadFile() {
		assertEquals(al, fo.readFile("./junit_test_file/junit_test_file.txt"));
	}

}
