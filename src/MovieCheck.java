
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
			// ��ȡ./movie/tickets/��Ӧ�ĵ�Ӱ��/ticket_remain.txt�е�ֵ
			// ���С�ڵ�������Ϊ����,�趨Ϊfalse
			// Read the value in the ticket_remain.txt if the size of read file
			// is not larger than 0, the ticket has been sold out and set the
			// corresponding flag into false
			if (new FileOp().readFile("./movies/tickets/" + movieArr[i] + "/ticket_remain.txt").size() <= 0) {
				tmp[i] = false;
			} else {
				// ���������,��Ϊδ����,�趨Ϊtrue
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
		// screenList ���һ��List������movie��moviename
		// The last list stores the movie name and its image file name
		boolean tmp[][] = new boolean[screenList.size() - 1][];
		String movieName = screenList.get(screenList.size() - 1).get("movie");
		for (int i = 0; i < screenList.size() - 1; i++) {
			String screenName = screenList.get(i).get("screen");
			tmp[i] = new boolean[screenList.get(i).size() - 1];

			// �Գ��ν�������(ʱ�����ȵ�������),�Ӷ����ص�statusҲ�ǺϺ�˳���
			// Sort the time, so that the value of the status are reliable
			List<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String, String>>(
					screenList.get(i).entrySet());
			Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
				// ��������
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
			SimpleDateFormat df = new SimpleDateFormat("HHmm"); // ����ʱ���ʽ
			// ����ȡ�����һ����ʱ���뵱ǰʱ����Ա�
			// Compare the last movie's time with the current time
			if (movieArr[i] >= Integer.parseInt(df.format(new Date()))) { // newDate()Ϊ��ȡ��ǰϵͳʱ��
				// If the current time is not larger than the last time, it will set the flag to true
				// Which means the movie can be sold
				// ���δ����������ʱ��,���趨Ϊtrue(���Լ�������)
				tmp[i] = true;
			} else {
				// �����ǰʱ�䳬��������ʱ��,���趨Ϊfalse(�����Լ�������)
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

		// Ҫ��ʱ���������
		// Sort the time first
		boolean tmp[][] = new boolean[screenList.size() - 1][];
		for (int i = 0; i < screenList.size() - 1; i++) {
			tmp[i] = new boolean[screenList.get(i).size() - 1];

			List<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String, String>>(
					screenList.get(i).entrySet());
			Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
				// ��������
				// Sort the time in descending
				@Override
				public int compare(Entry<String, String> entry1, Entry<String, String> entry2) {
					// return o1.getValue().compareTo(o2.getValue());
					return entry1.getKey().compareTo(entry2.getKey());
				}
			});

			int j = 0;
			for (Map.Entry<String, String> mapping : list) {
				// key Ϊ���ε�ʱ��
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

	// ������2nd Layer��button��ʣ��Ʊ����ѡȡ
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
				// ��������
				@Override
				public int compare(Entry<String, String> entry1, Entry<String, String> entry2) {
					// return o1.getValue().compareTo(o2.getValue());
					return entry1.getKey().compareTo(entry2.getKey());
				}
			});

			for (Map.Entry<String, String> mapping : list) {
				// key Ϊ���ε�ʱ��
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