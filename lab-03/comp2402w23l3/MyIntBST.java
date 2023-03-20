package comp2402w23l3;

import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;


public class MyIntBST extends BinarySearchTree<BinarySearchTree.BSTEndNode<Integer>,Integer> {

    /**
     * Constructor
     */
    public MyIntBST(BinarySearchTree.BSTEndNode<Integer> sampleNode, BinarySearchTree.BSTEndNode<Integer> nil) {
        super(sampleNode);
        this.c = new DefaultComparator<Integer>();
    }

    public MyIntBST(BinarySearchTree.BSTEndNode<Integer> sampleNode) {
        super(sampleNode);
        this.c = new DefaultComparator<Integer>();
    }

    public int trim() {
        // TODO(students): Your code goes here.
        return 0;
    }


    // Helper method, please do not modify.
    public static MyIntBST emptyBST() {
        return randomBST(0);
    }


    // Helper method, please do not modify.
    // Constructs and returns a random BST on <= n elements, by
    // randomly generating n values between 0 and 3n, and inserting
    // them into the tree. If there are duplicate random values
    // then there will be < n elements in the tree.
    public static MyIntBST randomBST(int n) {
        BSTEndNode<Integer> sample = new BSTEndNode<Integer>();
        MyIntBST t = new MyIntBST(sample);
        Random rand = new Random();
        for( int i=0; i < n; i++ ) {
            int value = rand.nextInt(3*n);
            t.add(value);
        }

        return t;
    }

    // Helper method, please do not modify.
    public void addLongPath( int add ) {
        if( add == 0 ) return;
        BinarySearchTree.BSTEndNode<Integer> u = r, prev = nil;
        while( u != nil ) {
            prev = u;
            u = u.right;
        }
        int lastVal = 0;
        if( prev != nil ) lastVal = prev.x;
        for( int i=0; i < add; i++ ) {
            // add here as a right child of prev
            if (prev == nil) {
                r = newNode(lastVal);
                r.parent = nil;
                prev = r;
            } else {
                prev.right = newNode(lastVal+i+1);
                prev.right.parent = prev;
                prev = prev.right;
            }
        }
    }

    // A helper method that creates an IntBST out of the elements elts.
    // This assumes the elements are already in in-order so that this construction
    // happens in O(n) time.
    public static MyIntBST createTree(ArrayList<Integer> elts) {
        BSTEndNode<Integer> sample = new BSTEndNode<Integer>();
        MyIntBST t = new MyIntBST(sample);
        if (elts.size() == 0) return t;

        t.r = t.newNode(elts.get(0));
        t.r.left = createTree( elts, 1 );
        if( t.r.left != t.nil ) t.r.left.parent = t.r;
        t.r.right = createTree( elts, 2 );
        if( t.r.right != t.nil ) t.r.right.parent = t.r;
        t.r.parent = t.nil;
        return t;
    }

    private static BinarySearchTree.BSTEndNode<Integer> createTree(ArrayList<Integer> elts, int start) {
        BSTEndNode<Integer> sample = new BSTEndNode<Integer>();
        MyIntBST t = new MyIntBST(sample);

        // if start is out of bounds, return nil
        if( start >= elts.size() ) return t.nil;
        // otherwise, create a new node and recurse
        t.r = t.newNode(elts.get(start));
        t.r.left = createTree( elts, 2*start+1 );
        if( t.r.left != t.nil ) t.r.left.parent = t.r;
        t.r.right = createTree( elts, 2*start+2 );
        if( t.r.right != t.nil ) t.r.right.parent = t.r;
        return t.r;
    }



    // Not a great print method, but it gives you some information about
    // the structure of the tree.
    // Feel free to change this method (and the other print method).
    public void print() {
        if( r != nil ) System.out.println( " " + r.x );
        print(r);
    }

    // Feel free to change this.
    public void print( BinarySearchTree.BSTEndNode<Integer> r ) {
        if (r != nil) {
            if (r.left != nil) {
                System.out.println("  " + r.x + "L -> " + r.left.x); // Left branch
                print(r.left);
            }
            if (r.right != nil) {
                System.out.println("  " + r.x + "R -> " + r.right.x); // Right branch
                print(r.right);
            }
        }
    }



    // This main method is provided for you for testing purposes.
    // You will want to add to this for local testing.
    public static void main(String[] args) {
        // These are just examples of local tests, they are not sufficient.
        // One of your tasks is to add and modify these tests to test
        // your code more thoroughly here before submitting to the server.
        int n = 10;
        MyIntBST bst = randomBST(n);

        // This is just one example of a test for trim. Add more!
        System.out.println( bst );
        bst.print(); // another way of printing the tree
        int t = bst.trim();
        System.out.println( bst );
        System.out.println( t ); // the number of nodes trimmed

        // Let's try another tree with 3 as the root, 1 and 6 as children
        bst = emptyBST();
        bst.add(3);
        bst.add(1);
        bst.add(6);
        System.out.println( "before trim: " );
        System.out.println( bst );
        t = bst.trim();
        System.out.println( "after trim: " );
        System.out.println( bst );
        bst.print();
        System.out.println( t );

        // From the specifications
        bst = emptyBST();
        int[] a = {30, 10, 100, 3, 50, 130, 1, 40, 60, 170};
        for( int i=0; i < a.length; i++ ) {
            bst.add(a[i]);
        }
        System.out.println( "before trim: " );
        System.out.println( bst );
        t = bst.trim();
        System.out.println( "after trim: " );
        System.out.println( bst );
        System.out.println( t );
    }

}
