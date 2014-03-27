package org.rodrigorr.coursera.main;

import org.rodrigorr.coursera.algorithms.MergeSortBU;
import org.rodrigorr.coursera.util.Util;

public class Main {

    /**
     * @param args
     */
    public static void main(final String[] args) {

        final Integer[] lista = new Integer[] { 65, 55, 70, 68, 43, 74, 47, 88, 40, 75 };

        MergeSortBU.sort(lista);

        Util.imprimirArray(0, lista);

    }

}
