package org.rodrigorr.coursera.util;

public final class Util {

    public static void imprimirArray(final int i, final int j,final Comparable[] array) {
        System.out.print("values : "+ i + " "+ j +"[");
        for (final Comparable c : array) {
            System.out.print(c.toString() + " ");
        }
        System.out.println("]");
    }  
}
