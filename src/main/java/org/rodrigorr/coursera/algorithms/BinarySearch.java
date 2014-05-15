package org.rodrigorr.coursera.algorithms;


public class BinarySearch<Key extends Comparable<Key>,Value> {
	
	Key[] keys;
	Value[] vals;
	int N;
	
	public BinarySearch(Key[] keys,Value[] values){
		this.keys = keys;
		this.vals = values;
		N = keys.length;
	}

	public Value get(Key key) {
		if (isEmpty())
			return null;
		int i = rank(key);
		if (i < N && keys[i].compareTo(key) == 0)
			return vals[i];
		else
			return null;
	}

	private int rank(Key key) {
		int lo = 0, hi = N - 1;
		while (lo <= hi) {
			int mid = lo + (hi - lo) / 2;
			int cmp = key.compareTo(keys[mid]);
			if (cmp < 0)
				hi = mid - 1;
			else if (cmp > 0)
				lo = mid + 1;
			else if (cmp == 0)
				return mid;
		}
		return lo;
	}
	
	public boolean isEmpty(){
		return false;
	}
}
