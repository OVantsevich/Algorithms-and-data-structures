package searchTrees;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Problem00 {
	@SuppressWarnings("resource")
	public static void main(String args[]) throws NumberFormatException, IOException {
		long result = 0;
		Set<Long> set = new HashSet<Long>();

		BufferedReader br = new BufferedReader(new FileReader("input.txt"));
		String line;
		while ((line = br.readLine()) != null) {
			if(!set.contains(Long.parseLong(line))) {
				result += Long.parseLong(line);
				set.add(Long.parseLong(line));
			}
		}

		FileWriter myWriter = new FileWriter("output.txt");
		myWriter.write("" + result);
		myWriter.close();
	}
}