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
 *      % java comp2402w23l4.Part4 <input.txt> <output.txt>
 *  will take input from <input.txt> and will print to <output.txt>, or
 *      % java comp2402w23l4.Part4 <input.txt>
 *  will take input from <input.txt> and will print to the terminal, or
 *      % java comp2402w23l4.Part4
 *  will take input from the terminal and will print to the terminal.
 *  You can also run the provided python3 script that runs a suite of local tests:
 *      % python3 run_local_tests.py student_config_lab4p4.json
 *  which compiles and runs this program on the files in the tests/ directory and
 *  compares the output to the expected output in the tests/ directory.
 */


public class Part4 {

    /**
     * Read lines one at a time from r.  Outputs to w according to the
     * lab specifications.
     * Assumes every line is an integer; otherwise it throws a NumberFormatException.
     * @param r the reader to read from
     * @param w the writer to write to
     * @throws IOException, NumberFormatException
     */
    public static void execute(BufferedReader r, PrintWriter w) throws IOException, NumberFormatException {
        HashMap<Integer, Integer> numbers = new HashMap<>(); // use deque instead of stack to allow for efficient removal of duplicates
        ArrayStack<Integer> order = new ArrayStack<>();
        String line = r.readLine();
        int prev = 0;

        if (line != null)
            prev = Integer.parseInt(line);

        // populate the hashmap and set the value to the frequency of each
        while ((line = r.readLine()) != null) {
            int curr = Integer.parseInt(line);
            if (curr % prev == 0) {
                if (numbers.containsKey(curr)) {
                    int count = numbers.get(curr);
                    numbers.put(curr, count + 1);
                } else {
                    numbers.put(curr, 1);
                    // keep track of the correct order
                    order.add(curr);
                }
            } prev = curr;
        }

        // print out in correct order at desired frequencies
        for (int number : order) {
            int count = numbers.get(number);
            if (count > 0) {
                for (int i = 0; i < count; ++i)
                    w.println(number);
            }
            // set frequency to 0 (all have been outputted)
            numbers.put(number, 0);
            numbers.remove(number);
        }
    }



    // YOU SHOULD NOT NEED TO MODIFY BELOW THIS LINE

    /**
     * The driver.  Open a BufferedReader and a PrintWriter, either from System.in
     * and System.out or from filenames specified on the command line, then call doIt.
     * @param args
     */
    public static void main(String[] args) {
        try {
            BufferedReader r;
            PrintWriter w;
            if (args.length == 0) {
                r = new BufferedReader(new InputStreamReader(System.in));
                w = new PrintWriter(System.out);
            } else if (args.length == 1) {
                r = new BufferedReader(new FileReader(args[0]));
                w = new PrintWriter(System.out);
            } else {
                r = new BufferedReader(new FileReader(args[0]));
                w = new PrintWriter(new FileWriter(args[1]));
            }
            long start = System.nanoTime();
            try {
                execute(r, w);
            } catch (NumberFormatException e) {
                System.err.println( "Your input must be integer only");
                System.err.println(e);
                System.exit(-1);
            }
            w.flush();
            long stop = System.nanoTime();
            System.out.println("Execution time: " + 1e-9 * (stop-start));
        } catch (IOException e) {
            System.err.println(e);
            System.exit(-2);
        }
    }
}
