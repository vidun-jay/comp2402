package comp2402w23l4;

import java.util.Set;
import java.util.HashSet;
import java.util.Date;
import java.util.Random;

public class HCPractice {
    public static class Flight {
        String number;      // flight number
        String origin;      // code of origin airport
        String dest;        // code of destination airport
        Date date;          // the originally scheduled date of departure.
        int status;         // the flight's current status.

        public Flight(String number, String origin, String dest, Date date, int status) {
            this.number = number;
            this.origin = origin;
            this.dest = dest;
            this.date = date;
            this.status = status;
        }

        public String toString() {
            return "(" + number + ", " + origin + ", " + dest + ", " + date + ", " + status + ")";
        }

        // TODO(Student): Implement the equals method
        public boolean equals( Object o ) {
            if( !(o instanceof Flight) ) {
                return false;
            }

            Flight other = (Flight) o;

            return this.number.equals(other.number) &&
            this.origin.equals(other.origin) &&
            this.dest.equals(other.dest) &&
            this.date.equals(other.date);
        }

        // TODO(Student): Implement the hashCode method
        public int hashCode() {
            /* start with a prime number, continually
            multiply by another prime number times the hash code
            of the last field, return the result. 17 and 31 can be changed,
            but are used commonly in hashing because they are both powers of 2-1.
            not sure if this is what we were supposed to do but I learned hashing
            as a part of my CCNA training and I'm applying that here. */
            int hash = 17;

            hash = 31 * hash + number.hashCode();
            hash = 31 * hash + origin.hashCode();
            hash = 31 * hash + dest.hashCode();
            hash = 31 * hash + date.hashCode();
            return hash;
        }
    }


    public static void main(String[] args) {

        System.out.println("HashSet on Flights...");
        Set<Flight> hs1 = new HashSet<>();

        hs1.add(new Flight("Num" + 1, "Or" + 3, "Dest", new Date(2023, 2, 15), 0));
        hs1.add(new Flight("Num" + 1, "Or" + 3, "Dest", new Date(2023, 2, 15), 0));
        Flight f1 = new Flight("Num" + 2, "Or" + 4,  "Dest", new Date(2023, 2, 15), 0);
        Flight f2 = new Flight("Num" + 2, "Or" + 4, "Dest",  new Date(2023, 2, 15),1);
        hs1.add(f1);
        hs1.add(f1);
        hs1.add(f2);
        System.out.println(hs1);
        /*
        assert(hs1.size()==2);
        assert(hs1.contains(f1));
        assert(hs1.contains(f2)); */

        for (int i = 0; i < 10000; i++) {
            hs1.add(new Flight("Num" + 1, "Or" + 3, "Dest", new Date(2022, 1, 20), 2));
        }
        System.out.println(hs1.size());

        assert(hs1.size() == 3);
        System.out.println("Done adding");
    }

}
