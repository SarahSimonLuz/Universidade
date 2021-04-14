public class DoubleLinkedList<T> implements Iterable<T>, Lists<T> {

    private DoubleNode<T> header, tail;
    private int size;

    public DoubleLinkedList() {
        header = tail = new DoubleNode<>();
        size = 0;
    }

    @Override
    public java.util.Iterator<T> iterator() {
        return new LinkedListIterator<>(header.getNext());
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private DoubleNode<T> header() {
        return header;
    }

    private DoubleNode<T> getNode(int i) {
        int index = -1;
        DoubleNode<T> s = header();
        while (index++ < i) //same as while(index < i){<...body...>; index++;}
            s = s.getNext();
        return s;
    }

    private void removeNode(DoubleNode<T> prev) {
        prev.setNext(prev.getNext().getNext());
        if(prev.getNext() == null)
            tail = prev;
        else
            prev.getNext().setPrev(prev);
        size--;
    }

    @Override
    public void removeIndex(int i) {
        removeNode(getNode(i - 1));
    }

    @Override
    public void remove(T x) {
        DoubleNode<T> r = header();
        for (T v : this) {
            if (v.equals(x)) {
                removeNode(r);
            } else {
                r = r.getNext();
            }
        }
    }

    @Override
    public void add(int i, T x) {
        if (i == size)
            add(x);
        else {
            DoubleNode<T> prev = getNode(i - 1);
            DoubleNode<T> newNode = new DoubleNode<>(x, prev , prev.getNext());
            prev.setNext(newNode);
            size++;
        }
    }

    private void add(DoubleNode<T> prev, T x) {
        DoubleNode<T> newNode = new DoubleNode<>(x, prev, prev.getNext());
        prev.setNext(newNode);
        tail = newNode;
        size++;
    }

    @Override
    public void add(T x) {
        add(tail, x);
    }

    @Override
    public T get(int i) throws IndexOutOfBoundsException {
        if (i >= 0 && i <= size() - 1)
            return getNode(i).element();
        else
            throw new IndexOutOfBoundsException("Index: " + i + " > Size: " + size());
    }

    @Override
    public void set(int i, T y) {
        getNode(i).setElement(y);
    }

    public String toString() {
        String s = "[";
        for (T x : this)
            s += x + ", ";
        return s.substring(0, s.length() - 2) + "]";
    }

    public String toStringBack() {
        DoubleNode<T> current = tail;
        String s = "[";
        for(int i = 0; i < size; i++){
            s += current.element() + ", ";
            current = current.getPrev();
        }
        return s.substring(0, s.length() - 2) + "]";
    }
}
