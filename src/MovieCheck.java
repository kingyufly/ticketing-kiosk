
/**
 * MovieCheck.java
 * This class contains all check operations that will be used in the multithread
 * 
 * @author kingyufly
 * @version 2.2
 * 
 * */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class MovieCheck {

	/**
	 * method CheckMovieSoldout This method read the movie's ticket file to
	 * decide whether the movie has been sold out
	 * 
	 * @param movieList
	 *            The arraylist of the movies that want to be checked
	 * @return boolean[] returns a list of boolean that contains the status of
	 *         the movie
	 * @see FirstLayer.java
	 */

	public boolean[] CheckMovieSoldout(ArrayList<String> movieList) {
		boolean tmp[] = new boolean[movieList.size()];
		String movieArr[] = new String[movieList.size()];
		for (int i = 0; i < movieList.size(); i++) {
			movieArr[i] = movieList.get(i).split(";")[2];
			// 读取./movie/tickets/对应的电影名/ticket_remain.txt中的值
			// 如果小于等于零则为售罄,设定为false
			// Read the value in the ticket_remain.txt if the size of read file
			// is not larger than 0, the ticket has been sold out and set the
			// corresponding flag into false
			if (new FileOp().readFile("./movies/tickets/" + movieArr[i] + "/ticket_remain.txt").size() <= 0) {
				tmp[i] = false;
			} else {
				// 如果大于零,则为未售罄,设定为true
				// If the size is larger than 0, it will set the flag to true
				tmp[i] = true;
			}
		}
		return tmp;
	}

	/**
	 * method CheckScreenSoldout This method read the information list which
	 * contains all informations about all movies and screens and check whether
	 * each time of each screen of one movie is sold out or not
	 * 
	 * @param screenList
	 *            The list of mappings which contains the information about each
	 *            screen and each time
	 * @return boolean[][] returns the the movie's status of each screen and
	 *         time
	 * @see SecondLayer.java
	 */

	public boolean[][] CheckScreenSoldout(List<Map<String, String>> screenList) {
		// screenList 最后一个List储存着movie和moviename
		// The last list stores the movie name and its image file name
		boolean tmp[][] = new boolean[screenList.size() - 1][];
		String movieName = screenList.get(screenList.size() - 1).get("movie");
		for (int i = 0; i < screenList.size() - 1; i++) {
			String screenName = screenList.get(i).get("screen");
			tmp[i] = new boolean[screenList.get(i).size() - 1];

			// 对场次进行排序(时间由先到后排序),从而返回的status也是合乎顺序的
			// Sort the time, so that the value of the status are reliable
			List<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String, String>>(
					screenList.get(i).entrySet());
			Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
				// 降序排序
				@Override
				public int compare(Entry<String, String> entry1, Entry<String, String> entry2) {
					// return o1.getValue().compareTo(o2.getValue());
					return entry1.getKey().compareTo(entry2.getKey());
				}
			});

			int j = 0;
			for (Map.Entry<String, String> mapping : list) {
				String key = mapping.getKey();
				if (!key.equals("screen")) {
					if (new FileOp()
							.readFile("./screens/" + screenName + "/" + movieName + "/" + key + "/ticket_remain.txt")
							.size() <= 0) {
						tmp[i][j] = false;
					} else {
						tmp[i][j] = true;
					}
					j++;
				}
			}
		}
		return tmp;
	}

	/**
	 * method CheckMovieTime This method is to check the latest movie's time to
	 * decide whether the movie has started by comparing the current with the
	 * last timeF
	 * 
	 * @param movieArr[]
	 *            The array of the latest time of each movie
	 * @return boolean[] return an array contains the status of the each movie
	 * @see FirstLayer.java
	 */

	public boolean[] CheckMovieTime(int movieArr[]) {
		boolean tmp[] = new boolean[movieArr.length];
		for (int i = 0; i < movieArr.length; i++) {
			SimpleDateFormat df = new SimpleDateFormat("HHmm"); // 设置时间格式
			// 将获取的最后一场的时间与当前时间相对比
			// Compare the last movie's time with the current time
			if (movieArr[i] >= Integer.parseInt(df.format(new Date()))) { // newDate()为获取当前系统时间
				// If the current time is not larger than the last time, it will set the flag to true
				// Which means the movie can be sold
				// 如果未超过最晚场次时间,则设定为true(可以继续销售)
				tmp[i] = true;
			} else {
				// 如果当前时间超过最晚场次时间,则设定为false(不可以继续销售)
				// The current time is larger than the last time, which means the movie can not be sold
				tmp[i] = false;
			}
		}
		return tmp;
	}

	/**
	 * method CheckScreenTime This method read the information list which
	 * contains all informations about all movies and screens and check whether
	 * each time of each screen of one movie is time out or not
	 * 
	 * @param screenList
	 *            The list of mappings which contains the information about each
	 *            screen and each time
	 * @return boolean[][] returns the the movie's status of each screen and
	 *         time
	 * @see SecondLayer.java
	 */

	public boolean[][] CheckScreenTime(List<Map<String, String>> screenList) {

		// 要对时间进行排序
		// Sort the time first
		boolean tmp[][] = new boolean[screenList.size() - 1][];
		for (int i = 0; i < screenList.size() - 1; i++) {
			tmp[i] = new boolean[screenList.get(i).size() - 1];

			List<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String, String>>(
					screenList.get(i).entrySet());
			Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
				// 降序排序
				// Sort the time in descending
				@Override
				public int compare(Entry<String, String> entry1, Entry<String, String> entry2) {
					// return o1.getValue().compareTo(o2.getValue());
					return entry1.getKey().compareTo(entry2.getKey());
				}
			});

			int j = 0;
			for (Map.Entry<String, String> mapping : list) {
				// key 为场次的时间
				String key = mapping.getKey();
				if (!key.equals("screen")) {
					SimpleDateFormat df = new SimpleDateFormat("HHmm");
					// System.out.println("key: " + key);
					// System.out.println("time: " + df.format(new Date()));
					if (Integer.parseInt(key) >= Integer.parseInt(df.format(new Date()))) {
						tmp[i][j] = true;
					} else {
						tmp[i][j] = false;
					}
					j++;
				}
			}
		}
		return tmp;
	}

	/**
	 * method CheckScreenTicketRemain This method is to check the remain ticket
	 * number that will be used in the SecondLayer
	 * 
	 * @param screenList
	 *            The list of mappings which contains the information about each
	 *            screen and each time
	 * @return ArrayList<ArrayList<String>> returns each screen's each time's
	 *         remain tickets
	 * @see SecondLayer.java
	 */

	// 用于在2nd Layer中button上剩余票数的选取
	public ArrayList<ArrayList<String>> CheckScreenTicketRemain(List<Map<String, String>> screenList) {
		ArrayList<ArrayList<String>> tmp = new ArrayList<>();

		String movieName = screenList.get(screenList.size() - 1).get("movie");
		for (int i = 0; i < screenList.size() - 1; i++) {
			String screenName = screenList.get(i).get("screen");
			screenList.get(i).remove("screen");

			tmp.add(new ArrayList<String>());

			List<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String, String>>(
					screenList.get(i).entrySet());
			Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
				// 降序排序
				@Override
				public int compare(Entry<String, String> entry1, Entry<String, String> entry2) {
					// return o1.getValue().compareTo(o2.getValue());
					return entry1.getKey().compareTo(entry2.getKey());
				}
			});

			for (Map.Entry<String, String> mapping : list) {
				// key 为场次的时间
				String key = mapping.getKey();
				tmp.get(i)
						.add("" + new FileOp()
								.readFile(
										"./screens/" + screenName + "/" + movieName + "/" + key + "/ticket_remain.txt")
								.size());
			}
			screenList.get(i).put("screen", screenName);
		}
		return tmp;
	}
}