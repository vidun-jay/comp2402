package comp2402w23l5;

// You may not import any other classes; if you do, the autograder will fail.
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;
import java.math.*;
import java.util.concurrent.ConcurrentSkipListSet;

import ods.*;


/**
 *  You can run this file from the command line from *outside* the comp2402w23l5 dir:
 *      % java comp2402w23l5.Part5 <input.txt> <output.txt>
 *  will take input from <input.txt> and will print to <output.txt>, or
 *      % java comp2402w23l5.Part5 <input.txt>
 *  will take input from <input.txt> and will print to the terminal, or
 *      % java comp2402w23l5.Part5
 *  will take input from the terminal and will print to the terminal.
 *  You can also run the provided python3 script that runs a suite of local tests:
 *      % python3 run_local_tests.py student_config_lab5p5.json
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
        // TODO(student): Your code goes here.
    }





    // THESE ARE HELPER METHODS
    // You may not want any of these, but I didn't want you to have to
    // go searching for these methods if you wanted them.
    // Use them if you wish, alter them if you wish, look them up if you wish.
    // I did not debug them so hopefully they work!

    // This is a simple O(sqrt(v)) algorithm that determines whether
    // integer v is prime
    // Note: This algorithm is not the most efficient for very large integers,
    // but it works well for small to moderate size integers.
    public static boolean isPrime(int n) {
        if (n <= 1) {
            return false;
        }
        if (n <= 3) {
            return true;
        }
        if (n % 2 == 0 || n % 3 == 0) {
            return false;
        }
        for (int i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }


    // Miller-Rabin primality tester. Probabilistic.
    // If we perform k iterations of the algorithm and obtain a false positive,
    // the probability that n is actually composite is at most (1/4)^k.
    // Returns true if n is probably prime, false if composite
    // RT is O(k log^3 n).
    // In practice, the Miller-Rabin algorithm is very efficient
    // for testing large prime numbers, as it has a small probability of
    // producing a false positive and can be easily parallelized to take
    // advantage of multiple CPU cores. However, for smaller numbers, it
    // may be more efficient to use a simpler algorithm such as trial
    // division or the Sieve of Eratosthenes.
    public static boolean isPrime(BigInteger n, int k) {
        if (n.compareTo(BigInteger.ONE) <= 0) {
            return false;
        }
        if (n.compareTo(BigInteger.valueOf(3)) <= 0) {
            return true;
        }
        int r = 0;
        BigInteger d = n.subtract(BigInteger.ONE);
        while (d.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            r++;
            d = d.divide(BigInteger.TWO);
        }
        for (int i = 0; i < k; i++) {
            BigInteger a = getRandomBase(n);
            BigInteger x = a.modPow(d, n);
            if (x.equals(BigInteger.ONE) || x.equals(n.subtract(BigInteger.ONE))) {
                continue;
            }
            boolean isComposite = true;
            for (int j = 0; j < r - 1; j++) {
                x = x.modPow(BigInteger.TWO, n);
                if (x.equals(BigInteger.ONE)) {
                    return false;
                }
                if (x.equals(n.subtract(BigInteger.ONE))) {
                    isComposite = false;
                    break;
                }
            }
            if (isComposite) {
                return false;
            }
        }
        return true;
    }

    // Returns a random base in the range [2, n - 2]
    private static BigInteger getRandomBase(BigInteger n) {
        Random rand = new Random();
        BigInteger a;
        do {
            a = new BigInteger(n.bitLength(), rand);
        } while (a.compareTo(BigInteger.TWO) < 0 || a.compareTo(n.subtract(BigInteger.TWO)) > 0);
        return a;
    }


    // This uses the Sieve of Eratosthenes to determine whether v is prime.
    public static boolean isPrimeSieve(int v) {
        if (v < 2) {
            return false; // 0 and 1 are not prime
        }

        boolean[] isComposite = new boolean[v + 1]; // Mark all numbers as composite
        for (int i = 2; i * i <= v; i++) {
            if (!isComposite[i]) {
                for (int j = i * i; j <= v; j += i) {
                    isComposite[j] = true; // Mark multiples of i as composite
                }
            }
        }

        return !isComposite[v]; // v is prime if and only if it is not composite
    }


    // This finds and returns the distinct divisors of v in O(sqrt(v)) time.
    public static List<Integer> findDistinctDivisors(int v) {
        List<Integer> divisors = new ArrayList<>();

        // Iterate from 1 to sqrt(v)
        for (int i = 1; i <= Math.sqrt(v); i++) {
            if (v % i == 0) {
                // If i is a divisor, add it to the list
                divisors.add(i);

                // If i is not the square root of v, add v/i to the list
                if (i != Math.sqrt(v)) {
                    divisors.add(v / i);
                }
            }
        }

        // Sort and return the list of divisors
        Collections.sort(divisors);
        return divisors;
    }

    // Returns the prime divisors of v, including duplicates.
    // Takes O(sqrt(v)) time.
    public static List<Integer> getPrimeDivisors(int v) {
        List<Integer> primeDivisors = new ArrayList<Integer>();

        // Find all prime divisors of v by trial division
        for (int i = 2; i * i <= v; i++) {
            while (v % i == 0) {
                primeDivisors.add(i);
                v /= i;
            }
        }
        if (v > 1) {
            primeDivisors.add(v); // v itself is a prime divisor if it is not 1
        }

        return primeDivisors;
    }



    public static List<Integer> getPrimeDivisorsPollardRho(int v) {
        List<Integer> primeDivisors = new ArrayList<Integer>();

        // Perform the Pollard's rho algorithm until all prime factors are found
        while (v > 1) {
            int divisor = pollardRho(v);
            primeDivisors.add(divisor);
            v /= divisor;
        }

        return primeDivisors;
    }

    // Pollard's rho algorithm for finding a non-trivial factor of an integer n
    private static int pollardRho(int v) {
        Random rand = new Random();
        int x = rand.nextInt(v - 2) + 2;
        int y = x;
        int c = rand.nextInt(v - 1) + 1;
        int d = 1;

        while (d == 1) {
            x = (x * x + c) % v;
            y = (y * y + c) % v;
            y = (y * y + c) % v;
            d = gcd(Math.abs(x - y), v);
        }

        if (d == v) {
            return pollardRho(v); // Retry with a different random number
        } else {
            return d; // Return a non-trivial factor of n
        }
    }


    // Euclidean algorithm for finding the greatest common divisor of two
    // integers a and b
    private static int gcd(int a, int b) {
        if (b == 0) {
            return a;
        } else {
            return gcd(b, a % b);
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
