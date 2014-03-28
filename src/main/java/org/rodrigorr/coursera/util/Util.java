package org.rodrigorr.coursera.util;

public final class Util {

    public static void imprimirArray(final int i, final Comparable[] array) {
        System.out.print("values : [");
        for (final Comparable c : array) {
            System.out.print(c.toString() + " ");
        }
        System.out.println("]  " + i);

    }
}
