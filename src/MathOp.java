
/**
 * MathOp.java
 * This class contains all math operations which will be used
 * 
 * @author kingyufly
 * @version 2.2
 * 
 * */

import java.util.ArrayList;
import java.util.HashSet;

public class MathOp {

	/**
	 * method randomSet This method is to generate different ticket id according
	 * to the volume
	 * 
	 * @param num
	 *            The numbers of the random ticket id
	 * @return ArrayList<String> The output of the different ticket id
	 * @see FirstLayer.java
	 */

	public ArrayList<String> genTicket(int num) {
		ArrayList<String> result = new ArrayList<String>();

		// 先生成一个hashset取获取随机生成的id
		// Use a empty hashset to store the ID
		HashSet<Integer> set = new HashSet<Integer>();
		randomSet(num, set);
		// 把hashset里的所有值取出来放到arraylist返回
		for (int j : set) {
			result.add("" + j);
		}

		return result;
	}

	/**
	 * method randomSet This method is to generate ticket id by using hashset
	 * and recursion to eliminate the duplicate id them
	 * 
	 * @param n
	 *            The numbers of the random ticket id
	 * @param set
	 *            The HashSet that contains the result
	 */

	public void randomSet(int n, HashSet<Integer> set) {
		// 因为hashset有一个特殊性质就是不能有重复的值,所以如果重复的值是放不进去的
		// 先按照原定数量生成id,并存入hashset
		for (int i = 0; i < n; i++) {
			// 调用Math.random()方法
			int num = 0;
			// 生成8位,每位1-4数字
			for (int j = 0; j < 8; j++) {
				num = (int) (num + ((int) (Math.random() * 4) + 1) * Math.pow(10, j));
			}
			set.add(num);// 将不同的数存入HashSet中
		}
		// 经过上面循环,如果有重复,则size会小于设定的数量
		int setSize = set.size();
		// 如果存入的数小于指定生成的个数，则调用递归再生成剩余个数的随机数，如此循环，直到达到指定大小
		if (setSize < n) {
			randomSet(n - setSize, set);// 递归
		}
	}
}
