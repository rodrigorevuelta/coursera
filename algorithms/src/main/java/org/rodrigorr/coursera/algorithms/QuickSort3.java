package org.rodrigorr.coursera.algorithms;

import org.rodrigorr.coursera.util.StdRandom;
import org.rodrigorr.coursera.util.Util;

public class QuickSort3 {

	public static void sort(Comparable[] a) {
		Util.imprimirArray(0, 9, a);
		StdRandom.shuffle(a);
		sort(a, 0, a.length - 1);
	}

	private static void sort(Comparable[] a, int lo, int hi) {
		if (hi <= lo)
			return;
		int lt = lo, gt = hi;
		Comparable v = a[lo];
		
		int i = lo;
		while (i <= gt) {
			
			int cmp = a[i].compareTo(v);
			if (cmp < 0){
				exch(a, lt++, i++);
			}else if (cmp > 0){
				exch(a, i, gt--);
			}else
				i++;
		}
		
		sort(a, lo, lt - 1);
		sort(a, gt + 1, hi);
		Util.imprimirArray(lt, gt, a);
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
