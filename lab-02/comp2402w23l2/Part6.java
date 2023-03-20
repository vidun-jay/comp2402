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
 * You can run this file from the command line from *outside* the comp2402w23l2
 * dir:
 * % java comp2402w23l2.Part6 <input.txt> <output.txt>
 * will take input from <input.txt> and will print to <output.txt>, or
 * % java comp2402w23l2.Part6 <input.txt>
 * will take input from <input.txt> and will print to the terminal, or
 * % java comp2402w23l2.Part6
 * will take input from the terminal and will print to the terminal.
 * You can also run the provided python3 script that runs a suite of local
 * tests:
 * % python3 run_local_tests.py student_config_lab2p6.json
 * which compiles and runs this program on the files in the tests/ directory and
 * compares the output to the expected output in the tests/ directory.
 */

public class Part6 {

    /**
     * Read lines one at a time from r. Outputs to w according to the
     * lab specifications.
     * Assumes every line is an integer; otherwise it throws a
     * NumberFormatException.
     *
     * @param x the input variable x
     * @param r the reader to read from
     * @param w the writer to write to
     * @throws IOException, NumberFormatException
     */
    public static void execute(int x, BufferedReader r, PrintWriter w) throws IOException, NumberFormatException {
        // TODO(student): Your code goes here.
        String line;
        ArrayStack<Integer> temp = new ArrayStack<Integer>();
        ArrayStack<ArrayDeque<Integer>> numbers = new ArrayStack<ArrayDeque<Integer>>();
        numbers.add(new ArrayDeque<Integer>());

        int n = 0, num_elements = 0, index = 0;
        int v = 0;
        int left_v = 0;
        int right_v = 0;
        int command = 0;

        while ((line = r.readLine()) != null) {
            numbers.get(index).add(Integer.parseInt(line));
            num_elements++;
            n++;

            if (num_elements == x) {
                numbers.add(new ArrayDeque<Integer>());
                index++;
                num_elements = 0;
            }
        }

        if (numbers.get(numbers.size() - 1).size() == 0)
            numbers.remove(numbers.size() - 1);

        if (n == 0) return;

        for (int i = 0; i < numbers.get(numbers.size() - 1).size(); i++)
            temp.add(numbers.get(numbers.size() - 1).get(i));

        for (int k = 0; k < temp.size(); k++) {
            command = temp.get(k) % 2;
            v = (temp.get(k) / 2) % (numbers.size());

            if (command == 0) {
                right_v = (v + 1) % numbers.size();
                numbers.get(right_v).add(0, numbers.get(v).remove(numbers.get(v).size() - 1));
            } else if (command == 1) {
                left_v = ((v - 1) + (numbers.size())) % (numbers.size());
                numbers.get(left_v).add((numbers.get(left_v).size()), numbers.get(v).remove(0));
            }
        }

        for (int i = 0; i < numbers.size(); i++) {
            if (numbers.get(i).size() > 0) {
                for (int k = (numbers.get(i).size() - 1); k >= 0; k--)
                    w.println(numbers.get(i).get(k));
                w.println("****");
            }
        }
    }

    // YOU SHOULD NOT NEED TO MODIFY BELOW THIS LINE

    /**
     * The driver. Open a BufferedReader and a PrintWriter, either from System.in
     * and System.out or from filenames specified on the command line, then call
     * doIt.
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
