/**
 * Util.java 27/03/2014
 *
 * Copyright 2014 INDITEX.
 * Departamento de Sistemas
 */
package org.rodrigorr.coursera.util;

/**
 * 
 * @author <a href="mailto:rodrigorr@servicioexterno.inditex.com">Rodrigo Revuelta Roca</a>
 * 
 */
public final class Util {

    public static void imprimirArray(final int i, final Comparable[] array) {
        System.out.print("values : [");
        for (final Comparable c : array) {
            System.out.print(c.toString() + ", ");
        }
        System.out.println("] + i");

    }
}
