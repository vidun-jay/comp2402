package comp2402w23l5;

import java.util.List;
import java.util.HashSet;


/**
 * This interface represents a directed graph whose vertices are
 * indexed by 0,...,nVertices()-1
 * @author morin
 *
 */
public interface UGraph {
	public int nVertices();
	public void addEdge(int i, int j);
	public void removeEdge(int i, int j);
	public boolean hasEdge(int i, int j);
	public List<Integer> edges(int i);
	public int degree(int i);
	public int numRegions();
	public int controlSet(HashSet<Integer> S);
}
