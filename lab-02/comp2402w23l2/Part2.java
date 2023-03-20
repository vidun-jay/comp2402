package comp2402w23l2;

// You may not import any other classes; if you do, the autograder will fail.
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.ListIterator;

/**
 *  You can run this file from the command line from *outside* the comp2402w23l2 dir:
 *      % java comp2402w23l2.Part2 <input.txt> <output.txt>
 *  will take input from <input.txt> and will print to <output.txt>, or
 *      % java comp2402w23l2.Part2 <input.txt>
 *  will take input from <input.txt> and will print to the terminal, or
 *      % java comp2402w23l2.Part2
 *  will take input from the terminal and will print to the terminal.
 *  You can also run the provided python3 script that runs a suite of local tests:
 *      % python3 run_local_tests.py student_config_lab2p2.json
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
        RootishArrayStack<Integer> lines = new RootishArrayStack<>();
        String line;
        int v = 0;

        while ((line = r.readLine()) != null)
            lines.add(Integer.parseInt(line));
        r.close();

        // base case (file empty)
        // if (lines.size() == 0) w.println(0);
        int n = lines.size();

        if (n > 0) v = lines.get(lines.size() - 1);
        else w.println(0);
        // set v to the value of the last element in list

        // iterate through the size of array and calculate sum
        int j = 0;
        int sum = 0;
        for (int i = 0; i < n; i++) {
            sum = ((sum % 240223) + (lines.get(j % lines.size()) % 240223)) % 240223;
            j = (j % lines.size() + v % lines.size()) % lines.size();
            w.println(sum);
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
