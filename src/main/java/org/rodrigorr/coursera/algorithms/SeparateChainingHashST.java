/**
 * SeparateChainingHashST.java 15/04/2014
 *
 * Copyright 2014 INDITEX.
 * Departamento de Sistemas
 */
package org.rodrigorr.coursera.algorithms;

public class SeparateChainingHashST<Key, Value> {

    private final int M = 3; // number of chains
    private final Node[] st = new Node[this.M]; // array of chains

    private static class Node {
        private final Object key;
        private Object val;
        private final Node next;

        public Node(final Object key, final Object val, final Node next) {
            this.key = key;
            this.val = val;
            this.next = next;
        }
    }

    private int hash(final Key key) {
        return (key.hashCode() & 0x7fffffff) % this.M;
    }

    public Value get(final Key key, final int hash) {
        int i = hash;
        if (hash == -1) {
            i = hash(key);
        }

        for (Node x = this.st[i]; x != null; x = x.next) {
            if (key.equals(x.key)) {
                return (Value) x.val;
            }
        }
        return null;
    }

    public void put(final Key key, final Value val, final int hash) {
        int i = hash;
        if (hash == -1) {
            i = hash(key);
        }
        for (Node x = this.st[i]; x != null; x = x.next) {
            if (key.equals(x.key)) {
                x.val = val;
                return;
            }
        }
        this.st[i] = new Node(key, val, this.st[i]);
    }
}
