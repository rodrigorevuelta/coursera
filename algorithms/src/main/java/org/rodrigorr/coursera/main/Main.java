package org.rodrigorr.coursera.main;

import java.util.Iterator;

import org.rodrigorr.coursera.algorithms.BST;
import org.rodrigorr.coursera.algorithms.BinarySearch;
import org.rodrigorr.coursera.algorithms.LinearProbingHashST;
import org.rodrigorr.coursera.algorithms.SeparateChainingHashST;

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

		// final Integer[] elements = new Integer[] { 58, 93, 25, 78, 75, 52,
		// 73, 68, 98, 36, 79, 45 };
		// final Integer[] elements = new Integer[] { 65, 55, 70, 68, 43, 74,
		// 47, 88, 40, 75 };
		// String valuesStr = new String("52 13 86 68 49 33 57 23 39 26 69 47");
		// String valuesStr = new String("54 54 80 68 74 54 76 64 84 39");
		// String[] values = valuesStr.split(" ");
		// MergeSort.sort(elements);
		// MergeSortBU.sort(elements);
		// QuickSort.sort(values);
		// QuickSort3.sort(values);
		// Util.imprimirArray(0, elements);
		// Util.imprimirArray(0,0, values);

		// final Integer[] elements = new Integer[] { 58, 93, 25, 78, 75, 52,
		// 73, 68, 98, 36, 79, 45 };
		// final Integer[] elements = new Integer[] { 65, 55, 70, 68, 43, 74,
		// 47, 88, 40, 75 };

		// MergeSort.sort(elements);
		// MergeSortBU.sort(elements);

		// Util.imprimirArray(0, elements);

		separateChainingHash();
		// linearProbingHash();
		// linearProbingHash2();
		Integer[] keys = { 10, 15, 20, 22, 24, 26, 27, 29, 30, 39, 50, 53, 57,
				63, 97 };
		//Integer[] values = { 10, 15, 20, 22, 24, 26, 27, 29, 30, 39, 50, 53,
		//		57, 63, 97 };
		//BinarySearch<Integer,Integer> bs = 
		//		new BinarySearch<Integer, Integer>(keys, values);
		//bs.get(21);

	}

	private void sort() {
		// MergeSort.sort(elements);
		// MergeSortBU.sort(elements);
		// QuickSort.sort(values);
		// QuickSort3.sort(values);
		// Util.imprimirArray(0, elements);
		// Util.imprimirArray(0,0, values);
	}

private static void separateChainingHash() {
		final SeparateChainingHashST<String, String> sc = new SeparateChainingHashST<String, String>();
		sc.put("Y", "Y", 2);
		sc.put("R", "R", 1);
		sc.put("N", "N", 0);
		sc.put("B", "B", 0);
		sc.put("Z", "Z", 0);
		sc.put("I", "I", 1);
		sc.put("V", "V", 2);
		sc.put("O", "O", 1);
		sc.put("T", "T", 0);
		sc.put("X", "X", 1);
		sc.put("U", "U", 1);
		sc.put("F", "F", 1);
		sc.get("U", 1);
	}

	private static void linearProbingHash() {
		final LinearProbingHashST<String, String> sc = new LinearProbingHashST<String, String>();
		sc.put("K", "K", 5);
		sc.put("C", "C", 7);
		sc.put("P", "P", 0);
		sc.put("E", "E", 9);
		sc.put("Y", "Y", 9);
		sc.put("V", "V", 6);
		sc.put("N", "N", 8);
		sc.put("F", "F", 0);
		sc.put("T", "T", 4);
		sc.put("B", "B", 6);
		sc.printArray();
	}

	private static void linearProbingHash2() {
		final LinearProbingHashST<String, String> sc = new LinearProbingHashST<String, String>();

		sc.put("B", "B", 3);
		sc.put("A", "A", 2);
		sc.put("N", "N", 1);
		sc.put("Q", "Q", 4);
		sc.put("O", "O", 2);

		sc.put("D", "D", 5);

		sc.put("M", "M", 0);
		sc.printArray();
	}

}
