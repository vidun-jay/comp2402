package comp2402w23l3;

// You may not import any other classes; if you do, the autograder will fail.
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.ListIterator;
import ods.ArrayDeque;
import ods.ArrayQueue;
import ods.ArrayStack;
import ods.DLList;
import ods.RootishArrayStack;
import ods.SEList;
import ods.SkiplistList;
import ods.SkiplistSSet;
import ods.SLList;
import ods.SSet;
import ods.ScapegoatTree;
import ods.Treap;

/**
 *  You can run this file from the command line from *outside* the comp2402w23l3 dir:
 *      % java comp2402w23l3.Part5 <input.txt> <output.txt>
 *  will take input from <input.txt> and will print to <output.txt>, or
 *      % java comp2402w23l3.Part5 <input.txt>
 *  will take input from <input.txt> and will print to the terminal, or
 *      % java comp2402w23l3.Part5
 *  will take input from the terminal and will print to the terminal.
 *  You can also run the provided python3 script that runs a suite of local tests:
 *      % python3 run_local_tests.py student_config_lab3p5.json
 *  which compiles and runs this program on the files in the tests/ directory and
 *  compares the output to the expected output in the tests/ directory.
 */


public class Part5 {

    /**
     * Read lines one at a time from r.  Outputs to w according to the
     * lab specifications.
     * Assumes every line is an integer; otherwise it throws a NumberFormatException.
     * @param r the reader to read from
     * @param w the writer to write to
     * @throws IOException, NumberFormatException
     */
    public static void execute(BufferedReader r, PrintWriter w) throws IOException, NumberFormatException {
        int root = 0;
        int left_child = 0;
        int right_child = 0;
        int violations = 0;

        ArrayDeque<Integer> numbers = new ArrayDeque<>();
        String line = r.readLine();
        String current_line, next_line;

        // first line of the file is the root
        if (line != null)
            root = Integer.parseInt(line);

        // loop through file
        while ((current_line = r.readLine()) != null) {

            // curent line will be the left child
            left_child = Integer.parseInt(current_line);
            // read next line (either null or a right child)
            next_line = r.readLine();

            // if not at a stem node,
            if (next_line != null) {
                // set right child to next line
                right_child = Integer.parseInt(next_line);
                // add both to numbers
                numbers.add(left_child);
                numbers.add(right_child);

                // check for violations of BST property
                if (right_child < root || left_child > root)
                    violations++;

                // update root node while simultaneously deleting it
                root = numbers.remove(0);
            } else { // if at a stem node
                numbers.add(left_child);
                // check BST violation
                if (left_child > root) violations++;
                break;
            }
        } w.println(violations);
    }

        /*
        // traverse the binary tree
        for (int i = 0; i < numbers.size(); ++i) {
            // System.out.println("Current Node: " + numbers.get(i));

            // if twice the currrent index + 1 (left child) is bigger than array, we are at a leaf node so break
            if ((i * 2) + 1 >= numbers.size()) break;

            // same thing for right node
            else if ((i * 2) + 2 >= numbers.size()) {
                // even if there's no right child, check left child for validity
                if (numbers.get((i * 2) + 1) > numbers.get(i)) {
                    violations++;
                } break;
            }

            // check children validity
            if (numbers.get((i * 2) + 1) > numbers.get(i)) {
                // System.out.println(numbers.get(index) + " > " + numbers.get(parent_index));
                violations++;
            } else if (numbers.get((i * 2) + 2) < numbers.get(i)) {
                // System.out.println(numbers.get(index) + " < " + numbers.get(parent_index));
                violations++;
            }
            */
        // }

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
