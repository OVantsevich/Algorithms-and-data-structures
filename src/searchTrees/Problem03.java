package searchTrees;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StreamTokenizer;

public class Problem03 {
	static class Node {
		public long val;
		public long lastMinValue;
		public long lastMaxValue;
		public Node left;
		public Node right;
		public Node from;
		
		Node() {
		}

		Node(long val) {
			this.val = val;
		}

		Node(long val, Node left, Node right) {
			this.val = val;
			this.left = left;
			this.right = right;
		}
	}

	public static void main(String args[]) throws NumberFormatException, IOException {
		FileWriter myWriter = new FileWriter("bst.out");
		StreamTokenizer st = new StreamTokenizer(new BufferedReader(new FileReader("bst.in")));

		st.nextToken();
		int n = (int) st.nval;

		Node[] nodes = new Node[n];

		st.nextToken();
		nodes[0] = new Node((int) st.nval);
		nodes[0].lastMinValue = Integer.MIN_VALUE;
		nodes[0].lastMaxValue = Integer.MAX_VALUE;
		
		boolean flag = true;
		int index = 1;
		while (st.nextToken() == StreamTokenizer.TT_NUMBER) {
			long key = (int) st.nval;
			st.nextToken();
			int position = (int) st.nval - 1;
			st.nextToken();
			char from = st.sval.charAt(0);

			if (from == 'L') {
				nodes[position].left = new Node(key);
				nodes[index] = nodes[position].left;
				nodes[index].from = nodes[position];
				nodes[index].lastMaxValue = nodes[position].val - 1;
				nodes[index].lastMinValue = nodes[position].lastMinValue;
			} else {
				nodes[position].right = new Node(key);
				nodes[index] = nodes[position].right;
				nodes[index].from = nodes[position];
				nodes[index].lastMinValue = nodes[position].val;
				nodes[index].lastMaxValue = nodes[position].lastMaxValue;
			}
			if(nodes[index].val < nodes[index].lastMinValue || nodes[index].val > nodes[index].lastMaxValue) {
				flag = false;
				myWriter.write("NO");
				break;
			}
			index++;
		}
		

		if (flag)
			myWriter.write("YES");
			

		myWriter.close();
	}
}
