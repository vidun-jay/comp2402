package comp2402w23l5;

import java.util.*;
import ods.*;

public class MyAdjacencyLists implements UGraph {
	int n;
	
	List<Integer>[] adj;

	@SuppressWarnings("unchecked")
	public MyAdjacencyLists(int n0) {
		n = n0;
		adj = (List<Integer>[])new List[n];
		for (int i = 0; i < n; i++) 
			adj[i] = new ArrayList<Integer>();
	}
	
	public void addEdge(int i, int j) {
		if( !adj[i].contains(j) ) { // doesn't add duplicates
			adj[i].add(j);
			adj[j].add(i);
		}
	}

	public boolean hasEdge(int i, int j) {
		return adj[i].contains(j);
	}

	public int nVertices() {
		return n;
	}

	public int degree(int i) {
		return adj[i].size();
	}

	public List<Integer> edges(int i) {
		return adj[i];
	}

	public void removeEdge(int i, int j) {
		Iterator<Integer> it = adj[i].iterator();
		while (it.hasNext()) {
			if (it.next() == j) {
				it.remove();
				break;
			}
		}
		it = adj[j].iterator();
		while (it.hasNext()) {
			if (it.next() == i) {
				it.remove();
				return;
			}
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for( int i=0; i < nVertices(); i++ ) {
			sb.append( i + ": " + adj[i] + "\n" );
		}
		return sb.toString();
	}



	public int numRegions() {
		// TODO(student): your code goes here.
		return -1;
	}

	public int controlSet(HashSet<Integer> S) {
		// TODO(student): Your code goes here.
		return -1;
	}



	// Creates an nxn "mesh" graph, i.e. an nxn grid with n^2 nodes
	// 0 -- 1 -- 2 -- ... -- (n-1)
	// |    |    |             |
	// n --n+1--n+2 --... --- 2n-1
	// |
	// .
	// .
	// |
	// n(n-1)--n(n-1)+1--...  n^2-1
	public static UGraph mesh(int n) {
		UGraph g = new MyAdjacencyLists(n*n);
		for (int k = 0; k < n*n; k++) {
			if (k % n > 0) 
				g.addEdge(k, k-1);
			if (k >= n)
				g.addEdge(k, k-n);
			if (k % n != n-1)
				g.addEdge(k, k+1);
			if (k < n*(n-1))
				g.addEdge(k, k+n);
		}
		return g;
	}

	// Returns a path on n nodes
	// 0 -- 1 -- 2 -- ... -- (n-1)
	public static UGraph path(int n) {
		UGraph g = new MyAdjacencyLists(n);
		for (int k = 0; k < n; k++) {
			if (k > 0)
				g.addEdge(k, k-1);
			if (k  != n-1)
				g.addEdge(k, k+1);
		}
		return g;
	}

	// produces a graph on n vertices and <= m edges
	// where the m edges are added at random.
	public static UGraph randomGraph(int n, int m) {
		Random r = new Random();
		UGraph g = new MyAdjacencyLists(n);
		for( int i=0; i < m; i++ ) {  // generates a random edge (start, end)
			int start = r.nextInt(n);
			int end = r.nextInt(n);
			while( end == start ) { // we don't want self-loops. not efficient but ok.
				end = r.nextInt(n);
			}
			g.addEdge( start, end ); // doesn't add if duplicate, hence <= m edges
			//System.out.println( "edge (" + start + ", " + end + ")");
		}

		return g;
	}

	public static void main(String[] args) {
		int n = 4;

		UGraph g = mesh(n);

		int num_regions = g.numRegions();
		System.out.println("mesh.num_regions: " + num_regions);
		assert(num_regions==1);
		HashSet<Integer> s = new HashSet<>();
		s.add(0);
		s.add(1);
		int control_size = g.controlSet(s);
		System.out.println("mesh.control_size({0,1}): " + control_size);
		assert(control_size==5);

		g = path(n);
		num_regions = g.numRegions();
		System.out.println("path.num_regions: " + num_regions);
		assert(num_regions==1);
		control_size = g.controlSet(s);
		System.out.println("path.control_size({0,1}): " + control_size);
		assert(control_size==3);

		g = randomGraph(n, 0); // "empty" graph on n nodes
		num_regions = g.numRegions();
		System.out.println("empty.num_regions: " + num_regions);
		assert(num_regions==n);
		control_size = g.controlSet(s);
		System.out.println("empty.control_size({0,1}): " + control_size);
		assert(control_size==2);

		g = randomGraph(n, 1); // n nodes and only one (random) edge
		num_regions = g.numRegions();
		System.out.println("single_edge.num_regions: " + num_regions);
		assert(num_regions==(n-1));
		control_size = g.controlSet(s);
		System.out.println("single_edge.control_size({0,1}): " + control_size);
		assert( (control_size==2) || (control_size == 3) );


		g = randomGraph(n, n*(n-1)/2); // n nodes and up to all possible edges
		System.out.println( g );
		num_regions = g.numRegions();
		System.out.println("lots_of_edges.num_regions: " + num_regions);
		assert(num_regions==(1)); // almost surely it is 1, but with small probability >1
		control_size = g.controlSet(s);
		System.out.println("lots_of_edges.control_size({0,1}): " + control_size);
		assert( (control_size >= 2) || (control_size <= (n-1) ) ); // pretty useless assertion!

	}

}
