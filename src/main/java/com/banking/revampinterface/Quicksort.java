package com.banking.revampinterface;

/**
 * Modified implementation of Quicksort.
 * @author Patryk Dziedzic, Aveesh Patel
 */
public class Quicksort {

    /**
     * Check if v is 'less than' w
     * @param v the first object to compare
     * @param w the second object to compare
     * @return true if v is 'less than' w
     */
    private static boolean less(Account v, Account w) {
        if (v != null && w == null)
            return true;
        else if (v == null)
            return false;
        else
            return v.compareTo(w) < 0;
    }

    /**
     * Exchange two elements of the given array
     * @param a the given array of object
     * @param i element 1
     * @param j element 2
     */
    private static void exch(Account[] a, int i, int j) {
        Account temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    /**
     * Partition the array as in Quicksort
     * @param a the objects array
     * @param lo the lower index
     * @param hi the higher index
     * @return position
     */
    private static int partition(Account[] a, int lo, int hi) {
        int i = lo, j = hi + 1;
        while (true) {
            while (less(a[++i], a[lo])) //while loop for i
                if (i == hi) break;

            while (less(a[lo], a[--j])) //while loop for j
                if (j == lo) break;

            if (i >= j) break;
            exch(a, i, j);
        }
        exch(a, lo, j);
        return j;
    }

    /**
     * Sort the given array using Quicksort
     * @param a given array of objects
     */
    public static void sort(Account[] a) {
        sort(a, 0, a.length - 1);
    }

    /**
     * Sort the given array using Quicksort and two initial positions
     * @param a the given array of objects
     * @param lo the low position
     * @param hi the high position
     */
    private static void sort(Account[] a, int lo, int hi) {
        if (hi <= lo) return;
        int j = partition(a, lo, hi);
        sort(a, lo, j - 1);
        sort(a, j + 1, hi);
    }
}
