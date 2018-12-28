
/**
 * FileOp.java
 * This class contains all file operations which will be used
 * 
 * @author kingyufly
 * @version 2.2
 * 
 * */

import java.io.*;
import java.util.ArrayList;

public class FileOp {

	/**
	 * method writeFile This method is to write the content to the file
	 * 
	 * @param contents
	 *            The content that want to write to the file
	 * @param fileName
	 *            The file that need to be written
	 * @param flag
	 *            True: continue write at the end of the file, False: overwrite
	 *            the file
	 */

	public void writeFile(String contents, String fileName, boolean flag) {
		try {
			FileWriter fileWriter = new FileWriter(fileName, flag);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(contents);
			bufferedWriter.flush();
			bufferedWriter.close();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * method readFile This method is to read all the information in file
	 * 
	 * @param fileName
	 *            The file that stores the information of all routes
	 * @return ArrayList return all the information in the file
	 */

	public ArrayList<String> readFile(String fileName) {
		ArrayList<String> contents = new ArrayList<String>();
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String str = bufferedReader.readLine();
			while (str != null) {
				contents.add(str);
				str = bufferedReader.readLine();
			}
			bufferedReader.close();
			fileReader.close();
		} catch (IOException e) {
			//e.printStackTrace();
			return null;
		}
		return contents;
	}
}