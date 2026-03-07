package modelo_grafo;

/**
 * Clase que representa una arista (conexión) entre dos proteínas en el grafo biológico.
 * Cada arista contiene una referencia al vértice destino y un peso que modela
 * la resistencia de la interacción proteica (menor valor = mejor conexión).
 */
public class Edge {
    private Vertex target;
    private int weight;

    /**
     * Constructor de una arista dirigida hacia un vértice destino con un costo dado.
     * @param target El vértice (proteína) destino de esta conexión.
     * @param weight El costo/resistencia de la interacción. Menor = mejor conexión.
     */
    public Edge(Vertex target, int weight) {
        this.target = target;
        this.weight = weight;
    }

    /**
     * Retorna el vértice destino de esta arista.
     * @return El vértice destino.
     */
    public Vertex getTarget() { return target; }

    /**
     * Retorna el peso (resistencia) de esta interacción proteica.
     * @return El peso de la arista.
     */
    public int getWeight() { return weight; }
}
