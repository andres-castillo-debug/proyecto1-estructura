package estructuras_primitivas;

/**
 * Clase genérica que representa un nodo individual en la lista enlazada simple.
 * Almacena un dato de tipo genérico T y una referencia al siguiente nodo.
 *
 * @param <T> Tipo de dato almacenado en el nodo.
 */
public class Node<T> {
    private T data;
    private Node<T> next;

    /**
     * Constructor que inicializa el nodo con el dato proporcionado.
     * @param data El dato a almacenar en el nodo.
     */
    public Node(T data) {
        this.data = data;
        this.next = null;
    }

    /** @return El dato almacenado en el nodo. */
    public T getData() { return data; }

    /** @param data El nuevo dato del nodo. */
    public void setData(T data) { this.data = data; }

    /** @return Referencia al siguiente nodo, o null si es el último. */
    public Node<T> getNext() { return next; }

    /** @param next El nodo siguiente en la lista. */
    public void setNext(Node<T> next) { this.next = next; }
}
