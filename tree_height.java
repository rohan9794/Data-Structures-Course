package DS;

import java.util.*;
import java.io.*;

public class tree_height {
	class FastScanner {
		StringTokenizer tok = new StringTokenizer("");
		BufferedReader in;

		FastScanner() {
			in = new BufferedReader(new InputStreamReader(System.in));
		}

		String next() throws IOException {
			while (!tok.hasMoreElements())
				tok = new StringTokenizer(in.readLine());
			return tok.nextToken();
		}

		int nextInt() throws IOException {
			return Integer.parseInt(next());
		}
	}

	public class TreeHeight {
		int n;
		int parents[];
		Node nodes[];
		Linked head;
		Linked tail;
		int level = 1;

		void read() throws IOException {
			FastScanner in = new FastScanner();
			n = in.nextInt();
			if (n >= 0 && n <= Math.pow(10, 5)) {
				parents = new int[n];
				for (int i = 0; i < n; i++) {
					parents[i] = in.nextInt();
				}
			} else {
				// if the input constraint is not satisfied.
				System.exit(0);
			}

		}

		int computeHeight() {
			nodes=new Node[n];
			for (int i = 0; i < n; i++) {
				nodes[i] = new Node(i, parents[i]);
			}

			int root = -1;
			for (int child_index = 0; child_index < n; child_index++) {
				int parent_index = parents[child_index];
				if (parent_index == -1) {
					root = child_index;
				} else {
					nodes[parent_index].addChild(nodes[child_index]);
				}
			}

			Node lastLeaf = levelTraversal(nodes[root]);
			int height = traverseParents(lastLeaf);

			return height;
		}

		private Node levelTraversal(Node n) {

			Linked q = new Linked();
			Node lastLeaf = null;
			q.enqueue(n);
			while (!q.isEmpty()) {
				Node temp = q.dequeue();
				if (!(temp.children.isEmpty())) {
					// Iterator itr=temp.children.iterator();
					ListIterator<Node> itr = temp.children.listIterator();
					while (itr.hasNext()) {
						q.enqueue(itr.next());
					}
				} else {
					lastLeaf = temp;
				}
			}
			return lastLeaf;

		}

		private int traverseParents(Node n) {
			if (n.parent == -1) {
				return 1;
			} else {
				// level+=1;
				return (1 + traverseParents(nodes[n.parent]));
				// return level;
			}
		}

		class Node {
			int key;
			int parent;
			ArrayList<Node> children = new ArrayList<Node>();

			Node(int d, int parent) {
				this.key = d;
				this.parent = parent;
			}

			public void addChild(Node node) {
				children.add(node);
			}
		}

		class Linked {
			Node n;
			Linked next;

			Linked() {

			}

			Linked(Node n) {
				this.n = n;
				next = null;
			}

			private boolean isEmpty() {
				if (head == null) {
					return true;
				} else {
					return false;
				}
			}

			private void enqueue(Node n) {
				Linked n1 = new Linked(n);
				if (head == null) {
					head = n1;
					tail = head;
					return;
				} else {
					tail.next = n1;
					tail = n1;
					return;
				}
			}
 
			private Node dequeue() {
				Linked temp = head;
				head = temp.next;
				return temp.n;
			}
		}

	}

	static public void main(String[] args) throws IOException {
		new Thread(null, new Runnable() {
			public void run() {
				try {
					new tree_height().run();
				} catch (IOException e) {
				}
			}
		}, "1", 1 << 26).start();
	}

	public void run() throws IOException {
		TreeHeight tree = new TreeHeight();
		tree.read();
		System.out.println(tree.computeHeight());
	}
}
