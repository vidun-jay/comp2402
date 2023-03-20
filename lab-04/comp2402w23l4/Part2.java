package comp2402w23l4;

// You may not import any other classes; if you do, the autograder will fail.
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Dictionary;
import ods.ArrayDeque;
import ods.ArrayQueue;
import ods.ArrayStack;
import ods.BinaryHeap;
import ods.DLList;
import ods.HashTable;
import ods.MeldableHeap;
import ods.MultiplicativeHashSet;
import ods.RedBlackTree;
import ods.RootishArrayStack;
import ods.SEList;
import ods.SkiplistList;
import ods.SLList;
import ods.SSet;
import ods.ScapegoatTree;
import ods.Treap;




/**
 *  You can run this file from the command line from *outside* the comp2402w23l4 dir:
 *      % java comp2402w23l4.Part2 <x-value> <input.txt> <output.txt>
 *  will take input from <input.txt> and will print to <output.txt>, or
 *      % java comp2402w23l4.Part2 <x-value> <input.txt>
 *  will take input from <input.txt> and will print to the terminal, or
 *      % java comp2402w23l4.Part2 <x-value>
 *  will take input from the terminal and will print to the terminal.
 *  You can also run the provided python3 script that runs a suite of local tests:
 *      % python3 run_local_tests.py student_config_lab4p2.json
 *  which compiles and runs this program on the files in the tests/ directory and
 *  compares the output to the expected output in the tests/ directory.
 */


public class Part2 {

    /**
     * Read lines one at a time from r.  Outputs to w according to the
     * lab specifications.
     * Assumes every line is an integer; otherwise it throws a NumberFormatException.
     * @param x the input variable x
     * @param r the reader to read from
     * @param w the writer to write to
     * @throws IOException, NumberFormatException
     */
    public static void execute(int x, BufferedReader r, PrintWriter w) throws IOException, NumberFormatException  {
        // TODO(Student): implement this method according to the specifications.
        BinaryHeap<Integer> numbers = new BinaryHeap<>();
        String line;
        int two_x = (int) Math.pow(2, x);

        // while the current line is not empty
        while ((line = r.readLine()) != null) {
            int curr = Integer.parseInt(line);

            /* each time you see a multiple of x^2, print
            out the smallest multiple of x you still have */
            if (curr % (x * x) == 0 && numbers.peek() != null)
                w.println(numbers.peek());

            /* by using the ScapegoatTree add function, we can
            maintain the sorted order of the elements */
            if (curr % x == 0)
                numbers.add(curr);

            /* each time you see a multiple of 2^x you get
             rid of the smallest multiple of x you still have */
            if (curr % two_x == 0 && numbers.peek() != null)
                numbers.poll();
        }
    }




    // YOU SHOULD NOT NEED TO MODIFY BELOW THIS LINE


    /**
     * The driver.  Open a BufferedReader and a PrintWriter, either from System.in
     * and System.out or from filenames specified on the command line, then call doIt.
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            BufferedReader r;
            PrintWriter w;
            int x;
            if (args.length == 0) {
                x = 2402;
                r = new BufferedReader(new InputStreamReader(System.in));
                w = new PrintWriter(System.out);
            } else if (args.length == 1) {
                x = Integer.parseInt(args[0]);
                r = new BufferedReader(new InputStreamReader(System.in));
                w = new PrintWriter(System.out);
            } else if (args.length == 2) {
                x = Integer.parseInt(args[0]);
                r = new BufferedReader(new FileReader(args[1]));
                w = new PrintWriter(System.out);
            } else {
                x = Integer.parseInt(args[0]);
                r = new BufferedReader(new FileReader(args[1]));
                w = new PrintWriter(new FileWriter(args[2]));
            }
            long start = System.nanoTime();
            try {
                execute(x, r, w);
            } catch (NumberFormatException e) {
                System.err.println("Your input must be integer only");
                System.err.println(e);
                System.exit(-1);
            }
            w.flush();
            long stop = System.nanoTime();
            System.out.println("Execution time: " + 1e-9 * (stop - start));
        } catch (IOException e) {
            System.err.println(e);
            System.exit(-2);
        }
    }
}
