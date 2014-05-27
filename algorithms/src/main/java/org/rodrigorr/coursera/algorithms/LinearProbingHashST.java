/**
 * LinearProbingHashST.java 15/04/2014
 *
 * Copyright 2014 INDITEX.
 * Departamento de Sistemas
 */
package org.rodrigorr.coursera.algorithms;

public class LinearProbingHashST<Key, Value>
{
    private final int M = 7;
    @SuppressWarnings("unchecked")
    private final Value[] vals = (Value[]) new Object[this.M];
    @SuppressWarnings("unchecked")
    private final Key[] keys = (Key[]) new Object[this.M];

    private int hash(final Key key) {
        return (key.hashCode() & 0x7fffffff) % this.M;
    }

    public void put(final Key key, final Value val, final Integer hash) {
        final int j;
        if (hash == null) {
            j = hash(key);
        } else {
            j = hash;
        }
        int i;
        for (i = j; this.keys[i] != null; i = (i + 1) % this.M) {
            if (this.keys[i].equals(key)) {
                break;
            }
        }
        this.keys[i] = key;
        this.vals[i] = val;
    }

    public Value get(final Key key, final Integer hash) {
        final int j;
        if (hash == null) {
            j = hash(key);
        } else {
            j = hash;
        }
        for (int i = j; this.keys[i] != null; i = (i + 1) % this.M) {
            if (key.equals(this.keys[i])) {
                return this.vals[i];
            }
        }
        return null;
    }

    public void printArray() {
        for (final Key i : this.keys) {
            System.out.print(i + " ");
        }
    }
}