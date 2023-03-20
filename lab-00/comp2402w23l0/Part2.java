package comp2402w23l0;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;
// import java.util.ArrayDeque; // TODO(Student): This import will give an error; remove it


public class Part2 {

    /**
     * Read lines one at a time from r. Prints the line number for each
     * line of the input file.
     * This assumes every line is an integer. If not, it
     * throws a NumberFormatException.
     * @param r the reader to read from
     * @param w the writer to write to
     * @throws IOException, NumberFormatException
     */
    public static void execute(BufferedReader r, PrintWriter w) throws IOException, NumberFormatException {
        List<Integer> l = new ArrayList<>();

        for (String line = r.readLine(); line != null; line = r.readLine()) {
            l.add(Integer.parseInt(line));
        }

        // This is an infinite loop
        // TODO(student): Fix this so it only loops once for every element in the list.
        // for( int i=0; ; i++ ) {
        for( int i=0; i < l.size(); i++ ) {
            w.println(i);
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
                System.out.println( "Your input must be integer only");
                System.out.println(e);
            }
            w.flush();
            long stop = System.nanoTime();
            System.out.println("Execution time: " + 1e-9 * (stop-start));
        } catch (IOException e) {
            System.err.println(e);
            System.exit(-1);
        }
    }
}
