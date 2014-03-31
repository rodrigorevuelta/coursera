package org.rodrigorr.coursera.main;

import org.rodrigorr.coursera.algorithms.QuickSort;

public class Main {

	/**
	 * @param args
	 */
	public static void main(final String[] args) {

		// final Integer[] elements = new Integer[] { 58, 93, 25, 78, 75, 52,
		// 73, 68, 98, 36, 79, 45 };
		// final Integer[] elements = new Integer[] { 65, 55, 70, 68, 43, 74,
		// 47, 88, 40, 75 };
		String valuesStr = new String("52 13 86 68 49 33 57 23 39 26 69 47");
//		String valuesStr = new String("54 54 80 68 74 54 76 64 84 39");
		String[] values = valuesStr.split(" ");
		// MergeSort.sort(elements);
		// MergeSortBU.sort(elements);
		QuickSort.sort(values);
//		 QuickSort3.sort(values);
		// Util.imprimirArray(0, elements);
		// Util.imprimirArray(0,0, values);

	}

}
