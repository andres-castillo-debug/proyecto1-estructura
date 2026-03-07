package modelo_grafo;

import estructuras_primitivas.MyLinkedList;

/**
 * Clase que representa un vértice (nodo) dentro del grafo biológico.
 * En el contexto de este proyecto, cada vértice modela una proteína específica.
 * Contiene el nombre de la proteína y su lista de adyacencia (conexiones/aristas).
 */
public class Vertex {
    private String name;
    private MyLinkedList<Edge> edges;

    /**
     * Constructor que inicializa el vértice con el nombre dado y sin conexiones.
     * @param name El nombre identificador de la proteína (ej. "P1", "P4").
     */
    public Vertex(String name) {
        this.name = name;
        this.edges = new MyLinkedList<>();
    }

    /**
     * Agrega una arista desde este vértice hacia el vértice destino con el peso dado.
     * Verifica que no exista ya una arista al mismo destino para evitar duplicados.
     * @param dst   El vértice destino de la conexión.
     * @param weight El costo/resistencia de la interacción.
     */
    public void addEdge(Vertex dst, int weight) {
        // Validar duplicados antes de agregar
        for (int i = 0; i < edges.size(); i++) {
            if (edges.get(i).getTarget().getName().equals(dst.getName())) {
                return; // La arista ya existe, no duplicar
            }
        }
        edges.add(new Edge(dst, weight));
    }

    /**
     * Elimina la arista que apunta al vértice con el nombre indicado.
     * Usado al eliminar una proteína del grafo para mantener consistencia.
     * @param targetName El nombre del vértice destino cuya arista se desea eliminar.
     */
    public void removeEdgeTo(String targetName) {
        for (int i = 0; i < edges.size(); i++) {
            Edge e = edges.get(i);
            if (e.getTarget().getName().equals(targetName)) {
                edges.remove(e);
                return;
            }
        }
    }

    /**
     * Retorna el nombre de esta proteína.
     * @return El nombre del vértice.
     */
    public String getName() { return name; }

    /**
     * Retorna la lista de adyacencia de este vértice (sus conexiones).
     * @return Lista enlazada de aristas.
     */
    public MyLinkedList<Edge> getEdges() { return edges; }

    /**
     * Calcula el grado del vértice (número de conexiones directas).
     * Usado para identificar Hubs (proteínas más conectadas = dianas terapéuticas).
     * @return El número de aristas de este vértice.
     */
    public int getDegree() {
        return edges.size();
    }
}
