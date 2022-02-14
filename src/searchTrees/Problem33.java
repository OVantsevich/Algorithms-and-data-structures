package searchTrees;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Problem33 {

	public static void main(String[] args) {

		int size = 0;
		int left[] = null;
		int right[] = null;
		int key[] = null;
		int from[] = null;
		int deep[] = null;

		try {
			File myObj = new File("input.txt");
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextInt()) {
				myReader.nextInt();
				++size;
			}
			myReader.close();
			myReader = new Scanner(myObj);

			left = new int[size];
			key = new int[size];
			from = new int[size];
			right = new int[size];
			deep = new int[size];
			int current;
			int iterator = 0;

			while (myReader.hasNextInt()) {
				current = myReader.nextInt();
				key[iterator] = current;

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

			myReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		int last[] = new int[size];
		int index = 0;

		for (int i = 0; i < size; i++) {
			if (right[i] == 0 && left[i] == 0)
				last[index++] = i;
		}

		int length = 0;
		int saveA = 0, saveB = 0;

		if(index == 1) {
			last[index++] = 0;
		}
		
		int result[] = maxWayCalculation(deep, last, key, from, index);
		saveA = result[0];
		saveB = result[1];
		length = result[2];
		

		int removing = -1;
		if (length != 0 && length % 2 == 0) {
			int way[] = new int[length + 1];

			if (deep[saveA] < deep[saveB]) {
				int tmp = saveA;
				saveA = saveB;
				saveB = tmp;
			}

			int k = 0;
			while(deep[saveA] != deep[saveB]) {
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

			for (int i = 0; i < size; i++)
				if (key[i] == way[length / 2])
					removing = i;

		}

		if (removing != -1) {
			if (right[removing] == 0) {
				if (left[removing] == 0) {
					if (left[from[removing]] == removing)
						left[from[removing]] = 0;
					else
						right[from[removing]] = 0;
				} else {
					from[left[removing]] = from[removing];
					if (left[from[removing]] == removing)
						left[from[removing]] = left[removing];
					else
						right[from[removing]] = left[removing];

				}
			} else {
				if (left[removing] == 0) {
					from[right[removing]] = from[removing];
					if (right[from[removing]] == removing)
						right[from[removing]] = right[removing];
					else
						left[from[removing]] = right[removing];
				} else {

					int current = right[removing];
					while (left[current] != 0)
						current = left[current];

					if (right[current] != 0) {
						if (right[from[current]] == current)
							right[from[current]] = right[current];
						else
							left[from[current]] = right[current];
						from[right[current]] = from[current];

						key[removing] = key[current];

					} else {
						if (right[from[current]] == current)
							right[from[current]] = 0;
						else
							left[from[current]] = 0;
						key[removing] = key[current];
					}
				}
			}
		}

		if (removing == -1) {
			size++;
		}
		int[] answer = leftBypass(right, left, key, from, size - 1);

		try {
			FileWriter myWriter = new FileWriter("output.txt");
			for (int i : answer) {
				myWriter.write(i + "\r");
			}
			myWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int[] leftBypass(int right[], int left[], int key[], int from[], int size) {
		int leftBypass[] = new int[size];

		int current = 0;
		boolean FLAG_UP = false;
		boolean FLAG_FROM_LEFT = true;

		for (int i = 0; i < size; i++) {
			if (FLAG_UP) {
				if (right[current] == 0 || left[current] == 0) {
					current = from[current];
				} else {
					if (FLAG_FROM_LEFT) {
						FLAG_UP = false;
						current = right[current];
						FLAG_FROM_LEFT = false;
					} else {
						current = from[current];
						FLAG_FROM_LEFT = true;
					}
				}
				--i;
			} else {
				leftBypass[i] = key[current];
				if (left[current] != 0) {
					if(right[current] != 0)
						FLAG_FROM_LEFT = true;
					current = left[current];
				} else {
					if (right[current] == 0) {
						FLAG_UP = true;
						current = from[current];
					} else {
						current = right[current];
					}
				}
			}
		}

		return leftBypass;
	}
	
	public static int[] maxWayCalculation(int deep[], int last[], int key[], int from[], int index) {
		int result[] = new int[3];
		int saveA = 0, saveB = 0;
		int length = 0;
		int sum = Integer.MAX_VALUE;
		for (int i = 0; i < index; i++) {
			for (int j = i + 1; j < index; j++) {

				int currentSum = key[last[i]] + key[last[j]];
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
							saveA = last[i];
							saveB = last[j];
						} else {
							saveB = last[i];
							saveA = last[j];
						}
					}
					length = currentLength;
				}
			}
		}
		
		result[0] = saveA;
		result[1] = saveB;
		result[2] = length;
		return result;
	}

}
