
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

		// ������һ��hashsetȡ��ȡ������ɵ�id
		// Use a empty hashset to store the ID
		HashSet<Integer> set = new HashSet<Integer>();
		randomSet(num, set);
		// ��hashset�������ֵȡ�����ŵ�arraylist����
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
		// ��Ϊhashset��һ���������ʾ��ǲ������ظ���ֵ,��������ظ���ֵ�ǷŲ���ȥ��
		// �Ȱ���ԭ����������id,������hashset
		for (int i = 0; i < n; i++) {
			// ����Math.random()����
			int num = 0;
			// ����8λ,ÿλ1-4����
			for (int j = 0; j < 8; j++) {
				num = (int) (num + ((int) (Math.random() * 4) + 1) * Math.pow(10, j));
			}
			set.add(num);// ����ͬ��������HashSet��
		}
		// ��������ѭ��,������ظ�,��size��С���趨������
		int setSize = set.size();
		// ����������С��ָ�����ɵĸ���������õݹ�������ʣ�����������������ѭ����ֱ���ﵽָ����С
		if (setSize < n) {
			randomSet(n - setSize, set);// �ݹ�
		}
	}
}
