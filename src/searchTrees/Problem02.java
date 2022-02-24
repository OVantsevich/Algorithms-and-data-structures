package searchTrees;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Problem02 {
	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
		long removing = 0;

		int size = 0;
		int left[] = null;
		int right[] = null;
		long key[] = null;
		int from[] = null;
		Set<Long> set = new HashSet<Long>();

		BufferedReader br = new BufferedReader(new FileReader("input.txt"));
		String line;
		line = br.readLine();
		removing = Long.parseLong(line);
		line = br.readLine();
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

		line = br.readLine();
		line = br.readLine();
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
		if (rightRemoving(right, left, key, from, removing))
			--size;

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

	public static boolean rightRemoving(int right[], int left[], long key[], int from[], long removingKey) {

		int removing = -1;
		for (int i = 0; i < key.length; i++) {
			if (key[i] == removingKey) {
				removing = i;
				break;
			}
		}

		if (removing == -1)
			return false;

		if (right[removing] == left[removing]) {
			if (left[from[removing]] == removing)
				left[from[removing]] = 0;
			else
				right[from[removing]] = 0;
			return true;
		}

		if (left[removing] == 0) {
			if (removing != 0) {
				if (left[from[removing]] == removing) {
					left[from[removing]] = right[removing];
				} else {
					right[from[removing]] = right[removing];
				}
				from[right[removing]] = from[removing];
			} else {
				if (left[right[0]] != 0) {
					from[left[right[0]]] = 0;
					left[0] = left[right[0]];
				}
				if (right[right[0]] != 0) {
					from[right[right[0]]] = 0;
					right[0] = right[right[0]];
				}
				key[0] = key[1];
			}

			return true;
		}

		if (right[removing] == 0) {
			if (removing != 0) {
				if (left[from[removing]] == removing) {
					left[from[removing]] = left[removing];
				} else {
					right[from[removing]] = left[removing];
				}
				from[left[removing]] = from[removing];
			} else {
				if (right[left[0]] != 0) {
					from[right[left[0]]] = 0;
					right[0] = right[left[0]];
				}
				if (left[left[0]] != 0) {
					from[left[left[0]]] = 0;
					left[0] = left[left[0]];
				}
				key[0] = key[1];
			}

			return true;
		}

		int current = right[removing];
		while (left[current] != 0)
			current = left[current];

		if (right[current] != 0) {
			if (left[from[current]] == current)
				left[from[current]] = right[current];
			else
				right[from[current]] = right[current];
			from[right[current]] = from[current];

			key[removing] = key[current];

		} else {
			if (left[from[current]] == current)
				left[from[current]] = 0;
			else
				right[from[current]] = 0;

			key[removing] = key[current];
		}
		return true;
	}
}
