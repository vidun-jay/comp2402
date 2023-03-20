package comp2402w23l3;


import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Random;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Comparator;

/**
 * An implementation of skiplists for searching
 *
 * @author morin
 *
 * @param <T>
 */
public class MySkiplistSSet<T> implements MySSet<T> {
	protected Comparator<T> c;

	@SuppressWarnings("unchecked")
	protected static class Node<T> {
		T x;
		Node<T>[] next;
		public Node(T ix, int h) {
			x = ix;
			next = (Node<T>[])Array.newInstance(Node.class, h+1);
		}
		public int height() {
			return next.length - 1;
		}
	}

	/**
	 * This node<T> sits on the left side of the skiplist
	 */
	protected Node<T> sentinel;

	/**
	 * The maximum height of any element
	 */
	int h;

	/**
	 * The number of elements stored in the skiplist
	 */
	int n;

	/**
	 * A source of random numbers
	 */
	Random rand;

	/**
	 * Used by add(x) method
	 */
	protected Node<T>[] stack;

	@SuppressWarnings("unchecked")
	public class Finger {
		protected Node<T>[] s;
		public Finger() {
			s = (Node<T>[])Array.newInstance(Node.class, h+1);
			for (int r = 0; r <= h; r++)
				s[r] = sentinel;
		}
	}

	public Finger getFinger() {
		return new Finger();
	}

	public T find(Finger f, T x) {
		int r = 0;
		Node<T> u = f.s[r];
		// find an edge that passes over x
		while (r < h
				&& ((u != sentinel && c.compare(x, u.x) <= 0)
					|| (u.next[r] != null && c.compare(x, u.next[r].x) > 0))) {
			u = f.s[++r];
		}
		r--;
		while (r >= 0) {
			while (u.next[r] != null && c.compare(u.next[r].x,x) < 0)
				u = u.next[r];
			f.s[r] = u;
			r--;
		}
		return (u.next[0] == null) ? null : u.next[0].x;
	}

	@SuppressWarnings("unchecked")
	public MySkiplistSSet(Comparator<T> c) {
		this.c = c;
		n = 0;
		sentinel = new Node<T>(null, 32);
		stack = (Node<T>[])Array.newInstance(Node.class, sentinel.next.length);
		h = 0;
		rand = new Random();
	}

	public MySkiplistSSet() {
		this(new DefaultComparator<T>());
	}

	/**
	 * Find the node<T> u that precedes the value x in the skiplist.
	 *
	 * @param x - the value to search for
	 * @return a node<T> u that maximizes u.x subject to
	 * the constraint that u.x < x --- or sentinel if u.x >= x for
	 * all node<T>s x
	 */
	protected Node<T> findPredNode(T x) {
		Node<T> u = sentinel;
		int r = h;
		while (r >= 0) {
			while (u.next[r] != null && c.compare(u.next[r].x,x) < 0)
				u = u.next[r];   // go right in list r
			r--;               // go down into list r-1
		}
		return u;
	}

	public T find(T x) {
		Node<T> u = findPredNode(x);
		return u.next[0] == null ? null : u.next[0].x;
	}

	public T findGE(T x) {
		if (x == null) {   // return first node<T>
			return sentinel.next[0] == null ? null : sentinel.next[0].x;
		}
		return find(x);
	}

	public T findLT(T x) {
		if (x == null) {  // return last node<T>
			Node<T> u = sentinel;
			int r = h;
			while (r >= 0) {
				while (u.next[r] != null)
					u = u.next[r];
				r--;
			}
			return u.x;
		}
		return findPredNode(x).x;
	}


	public boolean remove(T x) {
		boolean removed = false;
		Node<T> u = sentinel;
		int r = h;
		int comp = 0;
		while (r >= 0) {
			while (u.next[r] != null
			       && (comp = c.compare(u.next[r].x, x)) < 0) {
				u = u.next[r];
			}
			if (u.next[r] != null && comp == 0) {
				removed = true;
				u.next[r] = u.next[r].next[r];
				if (u == sentinel && u.next[r] == null)
					h--;  // height has gone down
			}
			r--;
		}
		if (removed) n--;
		return removed;
	}


	/**
	 * Rebuild the skiplist as a "fixed"/"deterministic" skiplist in O(n) time.
	 *
	 */
	public void rebuild() {
		Node<T> newSentinel = new Node<T>(null, 32);
		Node<T>[] newStack = (Node<T>[])Array.newInstance(Node.class, newSentinel.next.length);
		int newHeight = (int)Math.ceil(Math.log(n+1)/Math.log(2));
		newHeight = Math.max(newHeight, 1);

		for (int r = newHeight-1; r >= 0; r--) {
			Node<T> u = newSentinel;
			Node<T> v = sentinel.next[0];
			while (v != null) {
				while (u.next.length <= r) {
					newStack[u.next.length] = u;
					Node<T> newNode = new Node<T>(null, 0);
					u.next = Arrays.copyOf(u.next, u.next.length+1);
					u.next[u.next.length-1] = newNode;
					u = newNode;
				}
				u = u.next[r];
				v = v.next[0];
				u.next[0] = v;
			}
		}

		sentinel = newSentinel;
		stack = newStack;
		h = newHeight;
	}





	// Helper method. Do not modify.
	// A helper method that creates a skiplist out of the elements elts and given heights
	// Note that if there are duplicate elements in elts, then the heights
	// might not match up in the way you think they do.
	public void createSkiplist(ArrayList<T> elts, int[] heights) {
		for (int i = 0; i < elts.size(); i++) { // O(n) iterations, O(log n) per iteration
			add(elts.get(i), heights[i]); // O(log n)
		}
	}

	// Helper method. Do not modify.
	// Creates a flat skiplist out of the list of elements
	// *assumes elts is already sorted and there are no duplicates*
	// just for testing purposes, please do not modify
	// otherwise your tests might fail.
	public void createFlatSkiplist(ArrayList<T> elts) {
		Node<T> u = sentinel;
		for (int i = 0; i < elts.size(); i++) {
			Node<T> w = new Node<T>(elts.get(i), 0);
			u.next[0] = w;
			u = w;
		}
		h = 0;
		n = elts.size();
	}

	/**
	 * Simulate repeatedly tossing a coin until it comes up tails.
	 * Note, this code will never generate a height greater than 32
	 * @return the number of coin tosses - 1
	 */
	protected int pickHeight() {
		int z = rand.nextInt();
		int k = 0;
		int m = 1;
		while ((z & m) != 0) {
			k++;
			m <<= 1;
		}
		return k;
	}

	public void clear() {
		n = 0;
		h = 0;
		Arrays.fill(sentinel.next, null);
	}

	public int size() {
		return n;
	}

	public Comparator<T> comparator() {
		return c;
	}

	/**
	 * Create a new iterator in which the next value in the iteration is u.next.x
	 * TODO: Constant time removal requires the use of a skiplist finger (a stack)
	 * @param u
	 * @return
	 */
	protected Iterator<T> iterator(Node<T> u) {
		class SkiplistIterator implements Iterator<T> {
			Node<T> u, prev;
			public SkiplistIterator(Node<T> u) {
				this.u = u;
				prev = null;
			}
			public boolean hasNext() {
				return u.next[0] != null;
			}
			public T next() {
				prev = u;
				u = u.next[0];
				return u.x;
			}
			public void remove() {
				// TODO: Not constant time
				MySkiplistSSet.this.remove(prev.x);
			}
		}
		return new SkiplistIterator(u);
	}

	public Iterator<T> iterator() {
		return iterator(sentinel);
	}

	public Iterator<T> iterator(T x) {
		return iterator(findPredNode(x));
	}

	public boolean add(T x) {
		return add(x, pickHeight());
	}

	public boolean add(T x, int height) {
		Node<T> u = sentinel;
		int r = h;
		int comp = 0;
		while (r >= 0) {
			while (u.next[r] != null
					&& (comp = c.compare(u.next[r].x,x)) < 0)
				u = u.next[r];
			if (u.next[r] != null && comp == 0) return false;
			stack[r--] = u;          // going down, store u
		}
		Node<T> w = new Node<T>(x, height);
		while (h < w.height())
			stack[++h] = sentinel;   // height increased
		for (int i = 0; i < w.next.length; i++) {
			w.next[i] = stack[i].next[i];
			stack[i].next[i] = w;
		}
		n++;
		return true;
	}



	// There are 3 possible toString methods depending on the level of detail
	// you want.
	public String toString() {
		return toStringPretty();
		//return toStringBig();
		//return toStringSmall();
	}


	public String toStringSmall() {
		StringBuilder retStr = new StringBuilder();
		retStr.append("[");
		Node<T> u = sentinel.next[0];
		while( u != null ) {
			retStr.append(u.x);
			u = u.next[0];
			if( u != null ) {
				retStr.append(", ");
			}
		}
		retStr.append("]");
		return retStr.toString();
	}

	public String toStringBig() {
		StringBuilder sb = new StringBuilder();
		for (int r = h; r >= 0; r--) {
			int rank = -1;
			Node<T> u = sentinel;
			while (u != null) {
				if( u.x == null ) sb.append("|");
				else              sb.append(u.x);
				sb.append("--");
				u = u.next[r];
			}
			sb.append('\n');
		}
		return sb.toString();
	}

	public String toStringPretty() {
		StringBuilder sb = new StringBuilder();
		StringBuilder[] levels = new StringBuilder[h+1];
		int[] prevIndex = new int[h+1];
		int[] extraDigits = new int[h+1];
		for( int r=0; r < levels.length; r++ ) {
			levels[r] = new StringBuilder();
			levels[r].append("L" + r + ":\t |");
			prevIndex[r] = -1;
			extraDigits[r] = 0;
		}

		int index = -1;
		Node<T> u = sentinel;
		while( u != null ) {
			if( u.x != null ) { // not at the sentinel
				// determine which levels have this particular element
				int u_h = u.height();
				for( int r=0; r <= h; r++ ) {
					levels[r].append("-");
					if( r <= u_h ) {
						levels[r].append(u.x);
					} else {
						if( u.x instanceof Integer) {
							int digits = (int) (Math.log10((Integer)(u.x))); // how many extra digits
							if( ((Integer)(u.x)).equals(0) ) digits = 0;
							for (int i=0; i < digits+1; i++) {
								levels[r].append("-");
							}
						}
					}
					if( u.next[0] == null) {
						levels[r].append("\n");
					}
				}
			}
			u = u.next[0];
			index++;
		}
		for( int r=h; r >= 0; r-- ) {
			sb.append( levels[r].toString() );
		}
		return sb.toString();
	}

	// Helper method for testing; do not modify.
	public void flatten() {
		Node<T> u = sentinel;
		int r = h;
		while (r > 0) {
			sentinel.next[r] = null;
			r--;
		}
		u = sentinel;
		Node<T> unext = u.next[0];
		while (u.next[0] != null) {
			unext = u.next[0].next[0];
			// replace each node u with a node of height 1
			u.next[0] = new Node<T>(u.next[0].x, 0);
			u.next[0].next[0] = unext;
			u = u.next[0];
		}
		h = 0;
	}



	// Helper method that checks whether two MySSets are equal.
	// It first checks their sizes.
	// It then iterates through both lists and checks whether
	// the iterated elements are the same.
	// Note that this does not check whether the structure of the two lists
	// are the same (as in, if they are skiplists, are the heights of the elements
	// the same?)
	public static void checkEquals(MySSet<Integer> expected, MySSet<Integer> actual ) {
		if( expected.size() != actual.size()) {
			System.out.println( "Expected size: " + expected.size() + ", actual size: " + actual.size() );
		}

		Iterator<Integer> it_actual = actual.iterator();
		Iterator<Integer> it_expected = expected.iterator();

		while( it_expected.hasNext() ) {
			if( it_expected.hasNext() != it_actual.hasNext() ) {
				System.out.println( "expected.hasNext() != actual.hasNext()" );
			}
			if( !it_expected.next().equals( it_actual.next() ) ) {
				System.out.println( "expected.next() != actual.next()" );
			}
		}
		if( it_actual.hasNext() ) {
			System.out.println( "#2: expected.hasNext() != actual.hasNext()" );
		}
	}


	// Helper method for testing; do not modify.
	// This checks whether two skiplists are equal including the heights
	// of the elements.
	@SuppressWarnings("unchecked")
	public boolean equals(Object o) {
		if (o == null) return false;
		// check whether o is a MyList
		if (!(o instanceof MySkiplistSSet)) return false;
		MySkiplistSSet<T> that = (MySkiplistSSet<T>) o;
		if (this.size() != that.size()) return false;
		Iterator<T> it = this.iterator();
		Iterator<T> jt = that.iterator();
		while (it.hasNext() && jt.hasNext()) {
			if (!it.next().equals(jt.next())) return false;
		}
		if( it.hasNext() || jt.hasNext() ) return false;
		// Now check the heights of the individual nodes...
		MySkiplistSSet.Node<T> u = sentinel;
		MySkiplistSSet.Node<T> v = that.sentinel;
		while (u != null && v != null) {
			if (u.height() != v.height()) return false;
			u = u.next[0];
			v = v.next[0];
		}
		return true;
	}


	public static void main(String[] args) {
		// These are just some tests to get you started.
		// Add more tests and/or modify these to test your code more thoroughly.
		int n = 24;
		MySkiplistSSet<Integer> sl = new MySkiplistSSet<Integer>();
		System.out.println("Adding " + n + " elements");
		for (int i = 0; i < n; i++)
			sl.add(2*i);
		System.out.println("Searching");
		for (int i = 0; i < 2*n; i++) {
			Integer x = sl.find(i);
		}

		System.out.println("Finding");
		for (int i = 0; i < 2*n; i++) {
			Integer x = sl.find(i);
		}
		System.out.println("size() = " + sl.size());

		// Now let's try out rebuild!
		System.out.println( "before rebuild: ");
		System.out.println( sl );
		sl.rebuild();
		System.out.println( "after rebuild: ");
		System.out.println( sl );
		sl.flatten();
		System.out.println( sl );

		MySkiplistSSet<Integer> sl2 = new MySkiplistSSet<Integer>();
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i =0; i < n; i++) {
			list.add(2*i+1);
		}
		sl2.createFlatSkiplist(list);
		System.out.println( sl2 );

		sl2.rebuild();
		System.out.println( sl2 );

		MySkiplistSSet<Integer> actual = new MySkiplistSSet<Integer>();
		MySkiplistSSet<Integer> expected = new MySkiplistSSet<Integer>();

		ArrayList<Integer> elts = new ArrayList<Integer>();

		n = 3; // change this if you want bigger tests

		int max = 0;
		Random rand = new Random();
		for( int i=0; i < n; i++ ) {
			int x = rand.nextInt(10);
			max = max + x; // guarantees distinct elements
			elts.add(x);
		}

		if (n > 0) {
			actual.createFlatSkiplist(elts);
			expected.createFlatSkiplist(elts);
		}

		checkEquals(expected, actual); // doesn't check heights

		expected.rebuild();
		actual.rebuild();

		if( !expected.equals(actual) ) {
			System.out.println( "expected != actual" );
			System.out.println( "expected: \n" + expected );
			System.out.println( "actual: \n" + actual );
		}

		if( expected.add(elts.get(0)) != actual.add(elts.get(0))) {
			System.out.println( "expected.add(elts.get(0)) != actual.add(elts.get(0))" );
		}
		if( expected.remove(7) != actual.remove(7)) {
			System.out.println( "expected.remove(7) != actual.remove(7)" );
		}
		System.out.println( expected );
		System.out.println( actual );
		if( actual.find(7) != null ) {
			System.out.println( "actual.find(7) != null" );
		}
		if( expected.add(7) != actual.add(7) ) {
			System.out.println( "expected.add(7) != actual.add(7)" );
		}
		if( expected.remove(elts.get(0)) != actual.remove(elts.get(0)) ) {
			System.out.println( "expected.remove(elts.get(0)) != actual.remove(elts.get(0))" );
		}

	}
}
