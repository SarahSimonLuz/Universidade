public class LinkedListIterator<T> implements java.util.Iterator<T> {

    private SingleNode<T> currentSingle;
    private DoubleNode<T> currentDouble;
    private boolean single;

    LinkedListIterator(SingleNode<T> c) {
        currentSingle = c;
        single = true;
    }

    LinkedListIterator(DoubleNode<T> c) {
        currentDouble = c;
        single = false;
    }

    public boolean hasNext() {
        if(currentDouble == null)
            return currentSingle != null;
        return currentDouble != null;
    }

    public T next() {
        T nextItem;
        if (!hasNext())
            throw new java.util.NoSuchElementException();
        if(single) {
            nextItem = currentSingle.element();
            currentSingle = currentSingle.getNext();
        }else{
            nextItem = currentDouble.element();
            currentDouble = currentDouble.getNext();
        }
        return nextItem;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }


}