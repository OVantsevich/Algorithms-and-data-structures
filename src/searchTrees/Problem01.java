package searchTrees;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Problem01 {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
		int size = 0;
		int left[] = null;
		int right[] = null;
		long key[] = null;
		int from[] = null;
		Set<Long> set = new HashSet<Long>();

		BufferedReader br = new BufferedReader(new FileReader("input.txt"));
		String line;
		while ((line = br.readLine()) != null) {
			if (set.contains(Long.parseLong(line)))
				continue;
			++size;
			set.add(Long.parseLong(line));
		}
		br = new BufferedReader(new FileReader("input.txt"));
		set = new HashSet<Long>();

		left = new int[size];
		key = new long[size];
		from = new int[size];
		right = new int[size];
		long current;
		int iterator = 0;

		while ((line = br.readLine()) != null) {
			current = Long.parseLong(line);
			if (set.contains(current))
				continue;
			key[iterator] = current;
			set.add(Long.parseLong(line));

			int i = 0;
			while (true) {
				if (current > key[i]) {
					if (right[i] == 0) {
						right[i] = iterator;
						from[iterator++] = i;
						break;
					} else {
						i = right[i];
					}
				} else {
					if (left[i] == 0) {
						left[i] = iterator;
						from[iterator++] = i;
						break;
					} else {
						i = left[i];
					}
				}
			}
		}
		long[] result = leftBypass(right, left, key, from, size);

		FileWriter myWriter = new FileWriter("output.txt");
		for (long i : result) {
			myWriter.write("" + i);
			myWriter.write("\n");
		}
		myWriter.close();
	}

	public static long[] leftBypass(int right[], int left[], long key[], int from[], int size) {
		long leftBypass[] = new long[size];

		int current = 0;
		boolean FLAG_UP = false;
		int last = 0;

		for (int i = 0; i < size; i++) {
			if (FLAG_UP) {
				if (right[current] == 0 || left[current] == 0) {
					last = current;
					current = from[current];
				} else {
					if (left[current] == last) {
						FLAG_UP = false;
						current = right[current];
					} else {
						last = current;
						current = from[current];
					}
				}
				--i;
			} else {
				leftBypass[i] = key[current];
				if (left[current] != 0) {
					current = left[current];
				} else if (right[current] != 0) {
					current = right[current];
				} else {
					FLAG_UP = true;
					last = current;
					current = from[current];
				}
			}
		}

		return leftBypass;
	}

}
