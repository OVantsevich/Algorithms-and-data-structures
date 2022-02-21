package searchTrees;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Problem33 {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws NumberFormatException, IOException {

		int size = 0;
		int left[] = null;
		int right[] = null;
		long key[] = null;
		int from[] = null;
		int deep[] = null;
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
		deep = new int[size];
		long current;
		int iterator = 0;

		while ((line = br.readLine()) != null) {
			current = Long.parseLong(line);
			if (set.contains(current))
				continue;
			key[iterator] = current;
			set.add(Long.parseLong(line));

			int i = 0;
			int currentDepp = 0;
			while (true) {
				if (current > key[i]) {
					if (right[i] == 0) {
						++currentDepp;
						right[i] = iterator;
						deep[iterator] = currentDepp;
						from[iterator++] = i;
						break;
					} else {
						++currentDepp;
						i = right[i];
					}
				} else {
					if (left[i] == 0) {
						++currentDepp;
						left[i] = iterator;
						deep[iterator] = currentDepp;
						from[iterator++] = i;
						break;
					} else {
						++currentDepp;
						i = left[i];
					}
				}
			}
		}
		--deep[0];

		int last[] = new int[size];
		int index = 0;

		for (int i = 0; i < size; i++) {
			if (right[i] == 0 && left[i] == 0)
				last[index++] = i;
		}
		if (left[0] == 0 || right[0] == 0) {
			last[index++] = 0;
		}

		int result[] = maxWayCalculation(deep, last, key, from, index);
		int saveA = result[0];
		int saveB = result[1];
		int length = result[2];
		
		
		if (length != 0 && length % 2 == 0) {
			long removing = -1;
			long way[] = new long[length + 1];

			if (deep[saveA] < deep[saveB]) {
				int tmp = saveA;
				saveA = saveB;
				saveB = tmp;
			}

			int k = 0;
			while (deep[saveA] != deep[saveB]) {
				way[k++] = key[saveA];
				saveA = from[saveA];
			}

			while (saveA != saveB) {
				way[k] = key[saveA];
				way[k + 1] = key[saveB];
				saveA = from[saveA];
				saveB = from[saveB];
				k += 2;
			}
			way[k] = key[saveA];

			Arrays.sort(way);

			removing = way[length / 2];
			if(rightRemoving(right, left, key, from, removing))
				--size;
		}		

		long[] answer = leftBypass(right, left, key, from, size);

		FileWriter myWriter = new FileWriter("output.txt");
		for (long i : answer) {
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
	
	public static int[] maxWayCalculation(int deep[], int last[], long key[], int from[], int index) {
		int result[] = new int[3];
		int length = 0;
		long sum = Long.MAX_VALUE;
		for (int i = 0; i < index; i++) {
			for (int j = i + 1; j < index; j++) {

				long currentSum = key[last[i]] + key[last[j]];
				int currentLength = 0;
				int a, b;
				if (deep[last[i]] > deep[last[j]]) {
					a = last[i];
					b = last[j];
				} else {
					b = last[i];
					a = last[j];
				}

				for (int k = 0; k < Math.abs(deep[last[i]] - deep[last[j]]); k++) {
					a = from[a];
					++currentLength;
				}

				while (a != b) {
					currentLength += 2;
					a = from[a];
					b = from[b];
				}

				if (currentLength >= length) {
					if (currentSum < sum || currentLength > length) {
						sum = currentSum;
						if (deep[last[i]] > deep[last[j]]) {
							result[0] = last[i];
							result[1] = last[j];
						} else {
							result[1] = last[i];
							result[0] = last[j];
						}
					}
					length = currentLength;
				}
			}
		}

		result[2] = length;
		return result;
	}

}
