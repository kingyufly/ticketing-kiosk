
/**
 * Test.java
 * Used to start the program(both GUI and the generate the report)
 * 
 * @author kingyufly
 * @version 2.2
 * 
 * */

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

// 使用多线程有两种方式,一种是"继承Thread类",另一种为"使用Runnable接口" 此处使用继承Thread来实现多线程
public class Test extends Thread {
	public static void main(String[] args) {
		// Set the default language to English
		Locale.setDefault(Locale.ENGLISH);
		// Start the program (client side for the first frame)
		new FirstLayer();

		/*
		 * The administrator can directly generate the report for today, instead
		 * of wait for the end of the day
		 */

		// Admin admin = new Admin();
		// SimpleDateFormat dateDf = new SimpleDateFormat("yyyyMMdd");
		// admin.genFilmReport(dateDf.format(new Date()));

		/*
		 * Use multithread to check the end of the day in order to generate the
		 * report at 24 o'clock. The program will halt for 1 second so that the
		 * generate of the GUI will not interference the generate of the report
		 * thread
		 */

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		new Test().start();

		/*
		 * The administrator can load the movie's information separately or just
		 * run the program, if movies' information does not exit, the program
		 * will automatically generate the
		 */

		// new Admin().addMoiveInfo();
	}

	/**
	 * Method run, Override the method run to check the current date
	 */
	@Override
	public void run() {
		SimpleDateFormat dateDf = new SimpleDateFormat("yyyyMMdd");
		String date_str = dateDf.format(new Date());
		int date = Integer.parseInt(dateDf.format(new Date()));

		while (true) {
			if (date < Integer.parseInt(dateDf.format(new Date()))) {
				/*
				 * If the date is less than the late check which means the
				 * change of the date and the report will be generated
				 */
				Admin admin = new Admin();
				admin.genFilmReport(date_str);

				/*
				 * After generate the report, the date will update to the latest
				 * date to perform the check the next day
				 */
				date = Integer.parseInt(dateDf.format(new Date()));
			} else {
				// 更新时间可加可不加,因为是一天才才生成一次报告,生成后更新即可
				// date = Integer.parseInt(dateDf.format(new Date()));
			}

			/*
			 * Because of the report is not sensitive to time, checking the date
			 * per minute is acceptable
			 */
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
