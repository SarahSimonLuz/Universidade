public class DoubleNode<T> {
    private T element;
    private DoubleNode<T> next;
    private DoubleNode<T> prev;

    DoubleNode(T x) {
        element = x;
        next = null;
        prev = null;
    }

    //construtor vazio
    DoubleNode() {
        this(null);
    }

    //construtor com elemento e next
    DoubleNode(T x, DoubleNode<T> prev) {
        element = x;
        this.prev = prev;
        next = null;
    }

    DoubleNode(T x, DoubleNode<T> prev, DoubleNode<T> next) {
        element = x;
        this.prev = prev;
        this.next = next;
    }

    T element() {
        return element;
    }

    DoubleNode<T> getNext() {
        return next;
    }

    DoubleNode<T> getPrev() {
        return prev;
    }

    void setElement(T x) {
        element = x;
    }

    void setNext(DoubleNode<T> n) {
        next = n;
    }

    void setPrev(DoubleNode<T> p) {
        prev = p;
    }
}
