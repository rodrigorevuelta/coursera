package org.rodrigorr.coursera.algorithms;

import org.rodrigorr.coursera.util.StdRandom;
import org.rodrigorr.coursera.util.Util;

public class QuickSort {

	private static int partition(Comparable[] a, int lo, int hi) {
		int i = lo, j = hi + 1;
		Util.imprimirArray(i, j, a);
		while (true) {
			while (less(a[++i], a[lo]))
				if (i == hi)
					break;
			while (less(a[lo], a[--j]))
				if (j == lo)
					break;
			if (i >= j)
				break;
			exch(a, i, j);
			Util.imprimirArray(i, j, a);
			
		}
		exch(a, lo, j);
		Util.imprimirArray(i, j, a);
		return j;
	}

	public static void sort(Comparable[] a) {
		//StdRandom.shuffle(a);
		sort(a, 0, a.length - 1);
	}

	private static void sort(Comparable[] a, int lo, int hi) {
		
		if (hi <= lo)
			return;
		int j = partition(a, lo, hi);
		sort(a, lo, j - 1);
		sort(a, j + 1, hi);

	}

	private static boolean less(final Comparable v, final Comparable w) {
		return v.compareTo(w) < 0;
	}

	private static void exch(Object[] a, int i, int j) {
		Object swap = a[i];
		a[i] = a[j];
		a[j] = swap;
	}

}
