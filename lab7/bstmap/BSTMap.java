package bstmap;

import java.util.*;

/**
 * a binary search tree as a map to map key and value
 * @param <K> the key of the node
 * @param <V> the value of the node.
 */
public class BSTMap<K extends Comparable<K>,V> implements Map61B<K,V> {
    private Node sentinel_node;
    private int size = 0;

    public BSTMap() {
        sentinel_node = new Node();
        sentinel_node.left = null;
    }

    /**
     * a node containing the key and the value
     */
    private class Node {
        K key;
        V value;
        Node left;
        Node right;
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        sentinel_node = new Node();
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        Node curr_node = sentinel_node.left;
        while (true) {
            if (curr_node == null){
                return false;
            }
            if (curr_node.key.equals(key)) {
                return true;
            }
            // if the key is smaller than the current node's key, go left
            else if (curr_node.key.compareTo(key) > 0) {
                curr_node = curr_node.left;
            }
            // if the key is smaller than the current node's key, go right.
            else if (curr_node.key.compareTo(key) < 0) {
                curr_node = curr_node.right;
            }
        }
    }

    @Override
    public V get(K key) {
        Node curr_node = sentinel_node.left;
        while (true) {
            if (curr_node == null ){
                return null;
            }
            if (curr_node.key.equals(key) ) {
                return curr_node.value;
            }
            // if the key is smaller than the current node's key, go left
            else if (curr_node.key.compareTo(key) > 0) {
                curr_node = curr_node.left;
            }
            // if the key is smaller than the current node's key, go right.
            else if (curr_node.key.compareTo(key) < 0) {
                curr_node = curr_node.right;
            }
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value){
        Node insert_node = new Node();
        insert_node.key = key;
        insert_node.value = value;
        insert_node.left = null;
        insert_node.right = null;
        //if the BST already contains the key, update it
        put_helper(insert_node, sentinel_node.left);
    }

    private Node put_helper(Node insert_node, Node curr_node) {

        if (size == 0) {
            sentinel_node.left = insert_node;
            size++;
            return insert_node;
        }
        if (curr_node == null ){
            size++;
            return insert_node;
        }
        K ky = curr_node.key;
        // if the key is smaller than the current node's key, go left
        if (ky.compareTo(insert_node.key) > 0) {
            curr_node.left = put_helper(insert_node, curr_node.left);
        }
        // if the key is smaller than the current node's key, go right.
        else if (ky.compareTo(insert_node.key) < 0) {
            curr_node.right = put_helper(insert_node, curr_node.right);
        }
        else {
            curr_node.value = insert_node.value;
        }
        return curr_node;
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    public void printInOrder() {
        print_helper(sentinel_node.left);
    }

    private void print_helper(Node curr_node) {
        if (curr_node != null) {
            print_helper(curr_node.left);
            System.out.println(curr_node.value);
            print_helper(curr_node.right);
        }
    }
}
