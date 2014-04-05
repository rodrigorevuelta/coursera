package org.rodrigorr.coursera.main;

import java.util.Iterator;

import org.rodrigorr.coursera.algorithms.BST;
import org.rodrigorr.coursera.algorithms.MaxPQ;
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

		// String valuesStr = new String("54 54 80 68 74 54 76 64 84 39");
		String valuesStr = new String("86 11 94 36 98 34 46 16 60 12 61 78");
		String[] values = valuesStr.split(" ");

		BST<String, String> bst = new BST<String, String>();
		for (String value : values) {
			bst.put(value, value);
		}
		bst.delete("12");
		bst.delete("46");
		bst.delete("36");
		Iterable<String> ts = bst.keys();
		Iterator it = ts.iterator();
		while (it.hasNext()){
			System.out.print(" "+it.next());
		}

	}

	private void sort() {
		// MergeSort.sort(elements);
		// MergeSortBU.sort(elements);
		// QuickSort.sort(values);
		// QuickSort3.sort(values);
		// Util.imprimirArray(0, elements);
		// Util.imprimirArray(0,0, values);
	}

}
