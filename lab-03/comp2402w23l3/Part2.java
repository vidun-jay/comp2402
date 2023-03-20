package comp2402w23l3;

// You may not import any other classes; if you do, the autograder will fail.
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
 *      % java comp2402w23l3.Part2 <input.txt> <output.txt>
 *  will take input from <input.txt> and will print to <output.txt>, or
 *      % java comp2402w23l3.Part2 <input.txt>
 *  will take input from <input.txt> and will print to the terminal, or
 *      % java comp2402w23l3.Part2
 *  will take input from the terminal and will print to the terminal.
 *  You can also run the provided python3 script that runs a suite of local tests:
 *      % python3 run_local_tests.py student_config_lab3p2.json
 *  which compiles and runs this program on the files in the tests/ directory and
 *  compares the output to the expected output in the tests/ directory.
 */


public class Part2 {

    /**
     * Read lines one at a time from r.  Outputs to w according to the
     * lab specifications.
     * Assumes every line is an integer; otherwise it throws a NumberFormatException.
     * @param r the reader to read from
     * @param w the writer to write to
     * @throws IOException, NumberFormatException
     */
    public static void execute(BufferedReader r, PrintWriter w) throws IOException, NumberFormatException {
        // TODO(student): Your code goes here.
        SkiplistSSet<Integer> numbers = new SkiplistSSet<>();
        String line = r.readLine();
        int prev = 0;

        // if on the first line, keep prev = 0 but otherwise change to value of previous line
        if (line != null)
            prev = Integer.parseInt(line);

        // while the current line is not empty
        while ((line = r.readLine()) != null) {
            // convert current line from String to Int
            int curr = Integer.parseInt(line);
            // if current is a multiple of previous
            if (curr % prev == 0)
                // add to list (SkiplistSSet add will check for duplicates)
                numbers.add(curr);
            prev = curr; // traverse the list
        }

        // output the numbers
        for (Integer number : numbers)
            w.println(number);
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
