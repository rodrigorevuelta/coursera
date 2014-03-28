package org.rodrigorr.coursera.main;

import org.rodrigorr.coursera.algorithms.MergeSortBU;
import org.rodrigorr.coursera.util.Util;

public class Main {

    /**
     * @param args
     */
    public static void main(final String[] args) {

        // final Integer[] elements = new Integer[] { 58, 93, 25, 78, 75, 52, 73, 68, 98, 36, 79, 45 };
        final Integer[] elements = new Integer[] { 65, 55, 70, 68, 43, 74, 47, 88, 40, 75 };

        // MergeSort.sort(elements);
        MergeSortBU.sort(elements);

        Util.imprimirArray(0, elements);

    }

}
