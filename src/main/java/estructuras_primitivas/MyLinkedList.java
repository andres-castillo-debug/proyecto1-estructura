package estructuras_primitivas;

/**
 * Estructura de datos propia: lista enlazada simple genérica.
 * Construida sin librerías externas para cumplir la restricción del proyecto
 * de implementar el TDA desde cero.
 *
 * <p>Soporta operaciones de inserción al final, obtención por índice,
 * eliminación por objeto y vaciado completo.</p>
 *
 * @param <T> Tipo de dato que almacena la lista.
 */
public class MyLinkedList<T> {
    private Node<T> head;
    private int size;

    /**
     * Constructor que inicializa la lista vacía.
     */
    public MyLinkedList() {
        this.head = null;
        this.size = 0;
    }

    /**
     * Agrega un elemento al final de la lista.
     * @param data El dato a agregar.
     */
    public void add(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) {
            head = newNode;
        } else {
            Node<T> current = head;
            while (current.getNext() != null) {
                current = current.getNext();
            }
            current.setNext(newNode);
        }
        size++;
    }

    /**
     * Retorna el elemento en la posición indicada.
     * @param index Índice del elemento (base 0).
     * @return El dato en esa posición, o null si el índice está fuera de rango.
     */
    public T get(int index) {
        if (index < 0 || index >= size) return null;
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }
        return current.getData();
    }

    /**
     * Elimina la primera ocurrencia del dato indicado usando equals().
     * @param data El dato a eliminar.
     * @return true si se encontró y eliminó, false en caso contrario.
     */
    public boolean remove(T data) {
        if (head == null) return false;
        if (head.getData().equals(data)) {
            head = head.getNext();
            size--;
            return true;
        }
        Node<T> current = head;
        while (current.getNext() != null) {
            if (current.getNext().getData().equals(data)) {
                current.setNext(current.getNext().getNext());
                size--;
                return true;
            }
            current = current.getNext();
        }
        return false;
    }

    /**
     * Elimina todos los elementos de la lista, dejándola vacía.
     */
    public void clear() {
        head = null;
        size = 0;
    }

    /**
     * Verifica si un dato está contenido en la lista usando equals().
     * @param data El dato a buscar.
     * @return true si existe, false si no.
     */
    public boolean contains(T data) {
        Node<T> current = head;
        while (current != null) {
            if (current.getData().equals(data)) return true;
            current = current.getNext();
        }
        return false;
    }

    /** @return La cantidad de elementos en la lista. */
    public int size() { return size; }

    /** @return true si la lista no contiene elementos. */
    public boolean isEmpty() { return size == 0; }
}
