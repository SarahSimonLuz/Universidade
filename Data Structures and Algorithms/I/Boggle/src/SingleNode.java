public class SingleNode<T> {
    private T element;
    private SingleNode<T> next;

    //construtor
    SingleNode(T x) {
        element = x;
        next = null;
    }

    //construtor vazio
    SingleNode() {
        this(null);
    }

    //construtor com elemento e next
    SingleNode(T x, SingleNode<T> n) {
        element = x;
        next = n;
    }

    T element() {
        return element;
    }

    SingleNode<T> getNext() {
        return next;
    }

    void setElement(T x) {
        element = x;
    }

    void setNext(SingleNode<T> n) {
        next = n;
    }
}
