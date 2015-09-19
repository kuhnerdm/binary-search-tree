import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * 
 * Implementation of most of the Set interface operations using a Binary Search
 * Tree
 * 
 * @author Matt Boutell and kuhnerdm.
 * @param <T>
 */

public class BinarySearchTree<T extends Comparable<T>> {

	// Instance variables
	private BinaryNode root;
	private final BinaryNode NULL_NODE = new BinaryNode();
	private boolean iteratorRunning;
	private boolean modified;

	/**
	 * Constructs a new BST with no given values
	 */
	public BinarySearchTree() {
		root = NULL_NODE;
		iteratorRunning = false;
		modified = false;
	}

	/**
	 * Returns true if the tree is empty
	 * 
	 * @return whether the tree is empty
	 */
	public boolean isEmpty() {
		return this.root == NULL_NODE;
	}

	/**
	 * Returns the size of the tree
	 * 
	 * @return size
	 */
	public int size() {
		return this.root.size();
	}

	/**
	 * Returns the height of the tree
	 * 
	 * @return height
	 */
	public int height() {
		return this.root.height();
	}

	/**
	 * Returns whether the item exists in the tree, inefficiently
	 * 
	 * @param item
	 *            the item to search for
	 * @return whether the tree contains the item
	 */
	public boolean containsNonBST(T item) {
		return this.root.containsNonBST(item);
	}

	/**
	 * Returns a string representing the tree as an in-order traversal
	 * 
	 * @return string representing the tree
	 */
	public String toString() {
		if (this.size() == 0) {
			return "[]";
		}
		return "["
				+ this.root.toString().substring(0,
						this.root.toString().length() - 2) + "]";
	}

	/**
	 * Returns an ArrayList of type T with all the elements in the tree
	 * 
	 * @return ArrayList
	 */
	public ArrayList<T> toArrayList() {
		ArrayList<T> list = new ArrayList<T>();
		this.root.toAL(list);
		return list;
	}

	/**
	 * Returns an array of type Object with all the elements in the tree
	 * 
	 * @return Object[]
	 */
	public Object[] toArray() {
		Object[] array = new Object[this.size()];
		array = this.toArrayList().toArray();
		return array;
	}

	/**
	 * Returns an inefficient iterator for the tree
	 * 
	 * @return iterator
	 */
	public Iterator<T> inefficientIterator() {
		return new ArrayListIterator(this.root);

	}

	/**
	 * Returns a pre-order iterator for the tree
	 * 
	 * @return iterator
	 */
	public Iterator<T> preOrderIterator() {
		return new PreOrderIterator(this);
	}

	/**
	 * Returns an in-order iterator for the tree
	 * 
	 * @return iterator
	 */
	public Iterator<T> inOrderIterator() {
		return new InOrderIterator(this);
	}

	/**
	 * Inserts a new element into the tree
	 * 
	 * @param i
	 *            the element to insert
	 * @return false if the element already exists
	 */
	public boolean insert(T i) {

		if (iteratorRunning == true) {
			modified = true;
		}

		// Catch case where they insert nothing
		if (i == null) {
			throw new IllegalArgumentException();
		}

		// Catch case where tree is empty
		if (this.root == NULL_NODE) {
			this.root = new BinaryNode(i);
			return true;
		}

		// Recursive cases
		return root.insert(i);

	}

	/**
	 * Returns true if the tree contains the given element i
	 * 
	 * @param i
	 *            the element to search for
	 * @return true if the tree contains i
	 */
	public boolean contains(T i) {

		// Catch case where they ask for nothing
		if (i == null) {
			throw new IllegalArgumentException();
		}

		// Catch case where tree is empty
		if (this.root == NULL_NODE) {
			return false;
		}

		// Recursive cases
		return root.contains(i);
	}

	/**
	 * Deletes an element from the tree, returning false if the element does not
	 * exist
	 * 
	 * @param i
	 *            the element to delete
	 * @return false if the element does not exist
	 */
	public boolean remove(T i) {

		if (iteratorRunning == true) {
			modified = true;
		}

		// Handle simple cases
		if (this.isEmpty()) {
			return false;
		}

		if (this.root.left == NULL_NODE && this.root.right == NULL_NODE) {
			if (this.root.data.equals(i)) {
				this.root = NULL_NODE;
				return true;
			}
		}

		if (this.root.data.equals(i)) {
			if (this.root.right == NULL_NODE) {
				this.root = this.root.left;
				return true;
			} else {
				BinaryNode smallestInRightTree = root.right;
				while (smallestInRightTree.left.left != NULL_NODE
						&& smallestInRightTree.left != NULL_NODE) {
					smallestInRightTree = smallestInRightTree.left;
				}
				if (smallestInRightTree.left == NULL_NODE) {
					BinaryNode newRoot = root.right;
					BinaryNode oldLeft = root.left;
					root.right = NULL_NODE;
					this.root = newRoot;
					this.root.setLeft(oldLeft);
					return true;
				}
				smallestInRightTree.left.remove(smallestInRightTree);
				this.root = smallestInRightTree;
				return true;
			}
		}

		return this.root.remove(null, null, i);

	}

	// For manual tests only
	void setRoot(BinaryNode n) {
		this.root = n;
	}

	/**
	 * Contains information for a node in the tree
	 * 
	 * @author kuhnerdm
	 */
	class BinaryNode {

		// Instance variables
		private T data;
		private BinaryNode left;
		private BinaryNode right;

		/**
		 * Constructs a new node with no given element
		 */
		public BinaryNode() {
			this.data = null;
			this.left = null;
			this.right = null;
		}

		/**
		 * Constructs a new node with the given element
		 * 
		 * @param element
		 *            the element for the node
		 */
		public BinaryNode(T element) {
			this.data = element;
			this.left = NULL_NODE;
			this.right = NULL_NODE;
		}

		/**
		 * Returns the size of the subtree with this as root
		 * 
		 * @return size
		 */
		public int size() {
			if (this == NULL_NODE) {
				return 0;
			}
			int s = 1;
			s += this.left.size();
			s += this.right.size();
			return s;
		}

		/**
		 * Returns the height of the node
		 * 
		 * @return height
		 */
		public int height() {
			if (this == NULL_NODE) {
				return -1;
			}
			return Math.max(left.height(), right.height()) + 1;
		}

		/**
		 * Returns whether the item exists in the subtree with this as root,
		 * inefficiently
		 * 
		 * @param item
		 *            the item to search for
		 * @return whether the subtree contains the item
		 */
		public boolean containsNonBST(T item) {
			if (this == NULL_NODE) {
				return false;
			}
			return this.data == item || this.left.containsNonBST(item)
					|| this.right.containsNonBST(item);
		}

		/**
		 * Returns the element of the node
		 * 
		 * @return data
		 */
		public T getData() {
			return this.data;
		}

		/**
		 * Returns the left child of the node
		 * 
		 * @return left
		 */
		public BinaryNode getLeft() {
			return this.left;
		}

		/**
		 * Returns the right child of the node
		 * 
		 * @return right
		 */
		public BinaryNode getRight() {
			return this.right;
		}

		/**
		 * Returns a string representing the subtree with this as node, as an
		 * in-order traversal
		 * 
		 * @return string representing the subtree
		 */
		public String toString() {
			if (this == NULL_NODE) {
				return "";
			}
			return this.left.toString() + this.data.toString() + ", "
					+ this.right.toString() + "";
		}

		/**
		 * Helper method for BinarySearchTree.toArrayList()
		 * 
		 * @param list
		 *            the list to add to
		 */
		public void toAL(ArrayList<T> list) {
			if (this != NULL_NODE) {
				this.left.toAL(list);
				list.add(this.data);
				this.right.toAL(list);
			}
		}

		/**
		 * Inserts an element into the subtree with this as root
		 * 
		 * @param i
		 *            the element to insert
		 * @return false if the element already exists
		 */
		public boolean insert(T i) {
			int c = i.compareTo(this.data);
			if (c == 0) {
				return false;
			}
			if (c < 0) {
				if (this.left == NULL_NODE) {
					this.left = new BinaryNode(i);
					return true;
				}
				return this.left.insert(i);
			}
			if (c > 0) {
				if (this.right == NULL_NODE) {
					this.right = new BinaryNode(i);
					return true;
				}
				return this.right.insert(i);
			}

			return false;
		}

		/**
		 * Returns true if the subtree with this as root contains the element i
		 * 
		 * @param i
		 *            the element to search for
		 * @return true if the subtree contains i
		 */
		public boolean contains(T i) {
			int c = i.compareTo(this.data);
			if (c == 0) {
				return true;
			}
			if (c < 0) {
				if (this.left == NULL_NODE) {
					return false;
				}
				return this.left.contains(i);
			}
			if (c > 0) {
				if (this.right == NULL_NODE) {
					return false;
				}
				return this.right.contains(i);
			}

			return false;
		}

		/**
		 * Deletes an element from the subtree with this as root, returning
		 * false if the element does not exist
		 * 
		 * @param parentOfNode
		 *            the parent of this node
		 * @param whichChild
		 *            which child this node is
		 * @param i
		 *            the element to be deleted
		 * @return false if the element does not exist
		 */
		public boolean remove(BinaryNode parentOfNode, String whichChild, T i) {
			if (this.left == NULL_NODE && this.right == NULL_NODE) {
				if (this.data.equals(i)) {
					if (whichChild == "left") {
						parentOfNode.left = NULL_NODE;
					} else {
						parentOfNode.right = NULL_NODE;
					}
					return true;
				} else {
					return false;
				}
			}
			if (this.data.equals(i)) {
				if (this.right == NULL_NODE) {
					if (whichChild == "left") {
						parentOfNode.left = this.left;
						return true;
					} else {
						parentOfNode.right = this.left;
						return true;
					}
				} else {
					BinaryNode smallestInRightTree = root.right;
					while (smallestInRightTree.left.left != NULL_NODE) {
						smallestInRightTree = smallestInRightTree.left;
					}
					smallestInRightTree.left.remove(smallestInRightTree);
					if (whichChild == "left") {
						parentOfNode.left = smallestInRightTree.left;
						return true;
					} else {
						parentOfNode.right = smallestInRightTree.left;
						return true;
					}
				}
			}
			int c = this.data.compareTo(i);
			if (c < 0) {
				return this.right.remove(this, "right", i);
			}
			if (c > 0) {
				return this.left.remove(this, "left", i);
			}
			return false;
		}

		/**
		 * Used for base-case removal
		 * 
		 * @param parentOfNode
		 *            the parent of this node
		 */
		public void remove(BinaryNode parentOfNode) {
			parentOfNode.left = NULL_NODE;
		}

		// For manual testing
		public void setLeft(BinaryNode left) {
			this.left = left;
		}

		public void setRight(BinaryNode right) {
			this.right = right;
		}

	}

	/**
	 * An inefficient iterator for the tree
	 * 
	 * @author kuhnerdm
	 */
	public class ArrayListIterator implements Iterator<T> {

		// Instance variables
		private ArrayList<T> list;
		private int currentPos;

		/**
		 * Constructs a new iterator with the given root node
		 * 
		 * @param root
		 */
		public ArrayListIterator(BinarySearchTree<T>.BinaryNode root) {
			ArrayList<T> tempList = new ArrayList<T>();
			root.toAL(tempList);
			list = tempList;
			currentPos = 0;
		}

		@Override
		public boolean hasNext() {
			if (this.currentPos == this.list.size()) {
				return false;
			}
			return true;
		}

		@Override
		public T next() {
			if (modified == true) {
				throw new ConcurrentModificationException();
			}
			if (!this.hasNext()) {
				throw new NoSuchElementException(
						"There are no more elements in the tree.");
			}
			T item = this.list.get(this.currentPos);
			currentPos++;
			if (this.hasNext()) {
				iteratorRunning = true;
			} else {
				iteratorRunning = false;
			}
			return item;
		}

	}

	/**
	 * A pre-order iterator for the tree, using a stack to keep track of the
	 * traversal
	 * 
	 * @author kuhnerdm
	 */
	public class PreOrderIterator implements Iterator<T> {

		// Instance variables
		private Stack<BinaryNode> stack;
		private T lastReturned;
		private BinarySearchTree<T> tree;
		private boolean nextCalled;

		/**
		 * Constructs a new pre-order iterator with the given tree
		 * 
		 * @param root
		 */
		public PreOrderIterator(BinarySearchTree<T> tree) {
			modified = false;
			this.tree = tree;
			BinaryNode root = tree.root;
			this.stack = new Stack<BinaryNode>();
			if (root != NULL_NODE) {
				this.stack.push(root);
			}
			lastReturned = null;
			this.nextCalled = false;
		}

		@Override
		public boolean hasNext() {
			while (!this.stack.isEmpty() && this.stack.peek() == NULL_NODE) {
				this.stack.pop();
			}
			return !this.stack.isEmpty();
		}

		@Override
		public T next() {
			if (modified == true) {
				throw new ConcurrentModificationException();
			}
			if (!this.hasNext()) {
				throw new NoSuchElementException("No more nodes in tree");
			}
			this.nextCalled = true;
			BinaryNode root = this.stack.pop();
			this.stack.push(root.right);
			this.stack.push(root.left);
			if (this.hasNext()) {
				iteratorRunning = true;
			} else {
				iteratorRunning = false;
			}
			lastReturned = root.data;
			return root.data;
		}

		@Override
		public void remove() {
			if (modified == true) {
				throw new IllegalStateException();
			}
			if (this.nextCalled == false) {
				throw new IllegalStateException();
			}
			tree.remove(lastReturned);
		}

	}

	/**
	 * An in-order iterator for the tree, using a stack to keep track of the
	 * traversal
	 * 
	 * @author kuhnerdm
	 */
	public class InOrderIterator implements Iterator<T> {

		// Instance variables
		private Stack<BinaryNode> stack;
		private BinaryNode currentNode;
		private T lastReturned;
		private BinarySearchTree<T> tree;
		private boolean nextCalled;

		/**
		 * Constructs a new in-order iterator with the given tree
		 * 
		 * @param tree
		 */
		public InOrderIterator(BinarySearchTree<T> tree) {
			this.tree = tree;
			BinaryNode root = tree.root;
			this.stack = new Stack<BinaryNode>();
			currentNode = root;
			while (currentNode != NULL_NODE) {
				stack.push(currentNode);
				currentNode = currentNode.left;
			}
			this.nextCalled = false;
		}

		@Override
		public boolean hasNext() {
			return !(currentNode == NULL_NODE && stack.isEmpty());
		}

		@Override
		public T next() {
			if (modified == true) {
				throw new ConcurrentModificationException();
			}
			if (!hasNext()) {
				throw new NoSuchElementException("No more nodes in tree");
			}
			this.nextCalled = true;
			BinaryNode toReturn = stack.pop();
			currentNode = toReturn.right;
			while (currentNode != NULL_NODE) {
				stack.push(currentNode);
				currentNode = currentNode.left;
			}
			if (this.hasNext()) {
				iteratorRunning = true;
			} else {
				iteratorRunning = false;
			}
			lastReturned = toReturn.data;
			return toReturn.data;
		}

		@Override
		public void remove() {
			if (modified == true) {
				throw new IllegalStateException();
			}
			if (this.nextCalled == false) {
				throw new IllegalStateException();
			}
			tree.remove(lastReturned);
		}

	}

}
