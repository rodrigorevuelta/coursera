package org.rodrigorr.coursera.algorithms;

import org.rodrigorr.coursera.util.Util;

/**
 * 
 * 
 */
public class MergeSortBU {

    private static Comparable[] aux;

    private static int vueltaMerge = 0;

    private static void merge(final Comparable[] a, final Comparable[] aux, final int lo,
            final int mid, final int hi) {

        // assert isSorted(a, lo, mid); // precondition: a[lo..mid] sorted
        // assert isSorted(a, mid + 1, hi);// precondition: a[mid+1..hi] sorted
        for (int k = lo; k <= hi; k++) {
            aux[k] = a[k];
        }
        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid) {
                a[k] = aux[j++];
            } else if (j > hi) {
                a[k] = aux[i++];
            } else if (less(aux[j], aux[i])) {
                a[k] = aux[j++];
            } else {
                a[k] = aux[i++];
            }
        }
        vueltaMerge++;
        Util.imprimirArray(vueltaMerge,0, a);
        // assert isSorted(a, lo, hi); // postcondition: a[lo..hi] sorted
    }

    private static void sort(final Comparable[] a, final Comparable[] aux, final int lo, final int hi) {
        if (hi <= lo) {
            return;
        }
        final int mid = lo + ((hi - lo) / 2);
        sort(a, aux, lo, mid);
        sort(a, aux, mid + 1, hi);
        merge(a, aux, lo, mid, hi);
    }

    public static void sort(final Comparable[] a)
    {
        final int N = a.length;
        final Comparable[] aux = new Comparable[N];
        for (int sz = 1; sz < N; sz = sz + sz) {
            for (int lo = 0; lo < (N - sz); lo += sz + sz) {
                merge(a, aux, lo, (lo + sz) - 1, Math.min((lo + sz + sz) - 1, N - 1));
            }
        }
    }

    private static boolean less(final Comparable v, final Comparable w) {
        return v.compareTo(w) < 0;
    }

}
