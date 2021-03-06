package main.java.ua.com.florin.customlist;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by florin on 7/17/14.
 */
public class CustomList implements List {

    private Node header;
    private int size;

    public CustomList() {
        header = new Node();
        header.setNext(header);
        header.setPrevious(header);
        size = 0;
    }

    @Override
    public List subList(int fromIndex, int toIndex) {
        // throw exception if index is out of range
        if (fromIndex < 0 || toIndex > size() || toIndex < fromIndex) {
            throw new IndexOutOfBoundsException();
        }
        List list = new CustomList();
        Node n = node(fromIndex);

        // if indexes are equal then return empty list
        if (fromIndex == toIndex) {
            return list;
        }

        // copy elements to new list
        for (int i = fromIndex; i < toIndex; i++) {
            list.add(n.getData());
            n = n.getNext();
        }
        return list;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public Iterator iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        // create new array
        Object[] objects = new Object[size()];
        // init iterator
        int i = 0;
        // fill the array with data from the list
        for (Node n = header.getNext(); n != header; n = n.getNext(), i++) {
            objects[i] = n.getData();
        }
        return objects;
    }

    @Override
    public Object[] toArray(Object[] a) {
        return new Object[0];
    }

    @Override
    public boolean add(Object o) {
        // create new node in the end of the list, so that
        // next element is header and previous element is
        // header's previous element (which means list's last element)
        Node newNode = new Node(o, header, header.getPrevious());
        // set link for self in previous element
        newNode.getPrevious().setNext(newNode);
        // set link for self in next element
        newNode.getNext().setPrevious(newNode);
        // increment size
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            for (Node n = header.getNext(); n != header; n = n.getNext()) {
                if (n.getData() == null) {
                    // set "next" link at previous element
                    n.getPrevious().setNext(n.getNext());
                    // set "previous" link at next element
                    n.getNext().setPrevious(n.getPrevious());
                    // remove links to other elements
                    n.setNext(null);
                    n.setPrevious(null);
                    // decrement size
                    size--;
                    return true;
                }
            }
        } else {
            for (Node n = header.getNext(); n != header; n = n.getNext()) {
                if (o.equals(n.getData())) {
                    // set "next" link at previous element
                    n.getPrevious().setNext(n.getNext());
                    // set "previous" link at next element
                    n.getNext().setPrevious(n.getPrevious());
                    // remove links to other elements
                    n.setNext(null);
                    n.setPrevious(null);
                    // remove data
                    n.setData(null);
                    // decrement size
                    size--;
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection c) {
        return false;
    }

    @Override
    public boolean addAll(Collection c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection c) {
        return false;
    }

    @Override
    public void clear() {
        for (Node n = header.getNext(); n != header;) {
            // remember the link to next node
            // because it will be cleared during the iteration
            Node next = n.getNext();
            // clear data and links
            n.setData(null);
            n.setPrevious(null);
            n.setNext(null);
            // set target node to previously remembered link
            n = next;
        }
        // set header links
        header.setNext(header);
        header.setPrevious(header);
        // set size
        size = 0;
    }

    @Override
    public Object get(int index) {
        // throw exception if index is out of range
        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException();
        }
        // use already written method to find node by index
        return node(index).getData();
    }

    @Override
    public Object set(int index, Object element) {
        // throw exception if index is out of range
        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException();
        }
        // use already written method to find node by index
        Node node = node(index);
        // remember previous data to return it later
        Object previousValue = node.getData();
        // set new data value
        node.setData(element);
        return previousValue;
    }

    @Override
    public void add(int index, Object element) {
        // element, before which current element will be added
        Node targetNode = (index == size) ? header : node(index);
        // create new node and set its next element as targetNode and
        // previous element as previous element of targetNode
        Node newNode = new Node(element, targetNode, targetNode.getPrevious());
        // set link for self in previous element
        newNode.getPrevious().setNext(newNode);
        // set link for self in next element
        newNode.getNext().setPrevious(newNode);
        // increment size
        size++;
    }

    @Override
    public Object remove(int index) {
        // find node by index
        Node targetNode = node(index);
        // save data from the node to return it from the method
        Object data = targetNode.getData();
        // set "next" link at previous element
        targetNode.getPrevious().setNext(targetNode.getNext());
        // set "previous" link at next element
        targetNode.getNext().setPrevious(targetNode.getPrevious());
        // remove links to other elements
        targetNode.setNext(null);
        targetNode.setPrevious(null);
        // remove data
        targetNode.setData(null);
        // decrement size
        size--;
        return data;
    }

    @Override
    public int indexOf(Object o) {
        int index = 0;
        // iterate through list from start to end using "next" link of each node
        if (o == null) {
            for (Node n = header.getNext(); n != header; n = n.getNext()) {
                if (n.getData() == null) {
                    return index;
                }
                index++;
            }
        } else {
            for (Node n = header.getNext(); n != header; n = n.getNext()) {
                if (o.equals(n.getData())) {
                    return index;
                }
                index++;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        int index = size();
        // iterate through list from end to start using "previous" link of each node
        if (o == null) {
            for (Node n = header.getPrevious(); n != header; n = n.getPrevious()) {
                index--;
                if (n.getData() == null) {
                    return index;
                }
            }
        } else {
            for (Node n = header.getPrevious(); n != header; n = n.getPrevious()) {
                index--;
                if (o.equals(n.getData())) {
                    return index;
                }
            }
        }
        return -1;
    }

    @Override
    public ListIterator listIterator() {
        return null;
    }

    @Override
    public ListIterator listIterator(int index) {
        return null;
    }

    /**
     * Returns the node at specified index
     */
    private Node node(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }

        Node node = header;
        if (index < (size >> 1)) {
            // if index is less than half size then search from the beginning
            for (int i = 0; i <= index; i++) {
                node = node.getNext();
            }
        } else {
            // search from the end
            for (int i = size; i > index; i--) {
                node = node.getPrevious();
            }
        }
        return node;
    }

    private static class Node {

        // reference to the next node
        private Node next;
        // reference to the previous node
        private Node previous;
        // data of the current node
        private Object data;

        public Node() {
        }

        private Node(Object data, Node next, Node previous) {
            this.next = next;
            this.previous = previous;
            this.data = data;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public Node getPrevious() {
            return previous;
        }

        public void setPrevious(Node previous) {
            this.previous = previous;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }
}
