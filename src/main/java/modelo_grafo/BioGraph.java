package modelo_grafo;

import estructuras_primitivas.MyLinkedList;

/**
 * Clase principal que representa el grafo biológico de interacciones proteicas (PPIN).
 *
 * <p>Modela una Red de Interacción Proteína-Proteína como un <b>grafo no dirigido</b>
 * implementado mediante lista de adyacencia. Cada vértice es una proteína y cada
 * arista es una interacción con un costo/resistencia asociado.</p>
 *
 * <p>Incluye los siguientes algoritmos:</p>
 * <ul>
 *   <li>BFS para detección de complejos proteicos (componentes conexos)</li>
 *   <li>Dijkstra para ruta metabólica más corta entre dos proteínas</li>
 *   <li>Centralidad de grado para identificación de Hubs terapéuticos</li>
 * </ul>
 */
public class BioGraph {

    private MyLinkedList<Vertex> vertices;

    /**
     * Constructor que inicializa el grafo vacío.
     */
    public BioGraph() {
        this.vertices = new MyLinkedList<>();
    }

    // =========================================================================
    // GESTIÓN DE VÉRTICES Y ARISTAS
    // =========================================================================

    /**
     * Busca y retorna el vértice (proteína) con el nombre dado.
     * @param name El nombre de la proteína a buscar.
     * @return El vértice encontrado, o null si no existe en el grafo.
     */
    public Vertex getProtein(String name) {
        for (int i = 0; i < vertices.size(); i++) {
            Vertex v = vertices.get(i);
            if (v.getName().equalsIgnoreCase(name)) return v;
        }
        return null;
    }

    /**
     * Agrega una nueva proteína al grafo si no existe ya una con ese nombre.
     * @param name El nombre de la nueva proteína.
     */
    public void addProtein(String name) {
        if (getProtein(name) == null) {
            vertices.add(new Vertex(name));
        }
    }

    /**
     * Agrega una interacción bidireccional (grafo no dirigido) entre dos proteínas.
     * Si alguna de las proteínas no existe en el grafo, la operación no tiene efecto.
     * @param src    Nombre de la proteína origen.
     * @param dst    Nombre de la proteína destino.
     * @param weight Costo/resistencia de la interacción.
     */
    public void addInteraction(String src, String dst, int weight) {
        Vertex v1 = getProtein(src);
        Vertex v2 = getProtein(dst);
        if (v1 != null && v2 != null) {
            v1.addEdge(v2, weight);
            v2.addEdge(v1, weight); // Grafo no dirigido: conexión en ambas direcciones
        }
    }

    /**
     * Elimina una proteína del grafo y todas las aristas que la referencian.
     * Simula el efecto de un fármaco que "apaga" una proteína del patógeno.
     * @param name El nombre de la proteína a eliminar.
     * @return true si la proteína existía y fue eliminada; false si no se encontró.
     */
    public boolean removeProtein(String name) {
        Vertex target = getProtein(name);
        if (target == null) return false;

        // Eliminar todas las aristas de los demás vértices que apunten a esta proteína
        for (int i = 0; i < vertices.size(); i++) {
            vertices.get(i).removeEdgeTo(name);
        }
        // Eliminar el vértice de la lista principal
        vertices.remove(target);
        return true;
    }

    /**
     * Vacía el grafo por completo, eliminando todos los vértices y aristas.
     * Se llama antes de cargar un nuevo archivo para evitar datos residuales.
     */
    public void clear() {
        vertices.clear();
    }

    // =========================================================================
    // ALGORITMO: DIJKSTRA — Ruta Metabólica más Corta (Requerimiento F)
    // =========================================================================

    /**
     * Calcula la ruta de menor costo (menor resistencia) entre dos proteínas
     * usando el algoritmo de Dijkstra implementado manualmente sin PriorityQueue.
     *
     * <p>Biológicamente, esto representa la cadena de interacciones de menor
     * resistencia para transmitir una señal entre la proteína A y la proteína B.</p>
     *
     * @param startName Nombre de la proteína origen.
     * @param endName   Nombre de la proteína destino.
     * @return String con la ruta encontrada y su costo total, o mensaje de error.
     */
    public String dijkstra(String startName, String endName) {
        int n = vertices.size();
        if (n == 0) return "Error: El grafo está vacío.";

        Vertex startV = getProtein(startName);
        Vertex endV   = getProtein(endName);

        if (startV == null) return "Error: La proteína origen '" + startName + "' no existe en el grafo.";
        if (endV == null)   return "Error: La proteína destino '" + endName + "' no existe en el grafo.";
        if (startName.equalsIgnoreCase(endName)) return "Origen y destino son la misma proteína: " + startName;

        // --- Inicialización ---
        int[] dist    = new int[n];       // Distancias mínimas desde el origen
        boolean[] vis = new boolean[n];   // Nodos ya procesados
        int[] prev    = new int[n];       // Índice del nodo anterior en la ruta óptima

        final int INF = Integer.MAX_VALUE / 2;
        int startIdx = -1, endIdx = -1;

        for (int i = 0; i < n; i++) {
            dist[i] = INF;
            prev[i] = -1;
            vis[i]  = false;
            if (vertices.get(i) == startV) startIdx = i;
            if (vertices.get(i) == endV)   endIdx   = i;
        }
        dist[startIdx] = 0;

        // --- Iteración principal de Dijkstra ---
        for (int iter = 0; iter < n; iter++) {
            // Seleccionar el nodo no visitado con menor distancia
            int u = -1;
            for (int i = 0; i < n; i++) {
                if (!vis[i] && (u == -1 || dist[i] < dist[u])) u = i;
            }
            if (u == -1 || dist[u] == INF) break; // Nodos restantes inaccesibles
            vis[u] = true;

            // Relajar aristas del nodo u
            Vertex vU = vertices.get(u);
            MyLinkedList<Edge> edges = vU.getEdges();
            for (int j = 0; j < edges.size(); j++) {
                Edge e = edges.get(j);
                int v = indexOf(e.getTarget());
                if (v == -1 || vis[v]) continue;
                int newDist = dist[u] + e.getWeight();
                if (newDist < dist[v]) {
                    dist[v] = newDist;
                    prev[v] = u;
                }
            }
        }

        // --- Verificar si el destino es alcanzable ---
        if (dist[endIdx] == INF) {
            return "No existe ruta entre '" + startName + "' y '" + endName + "' (proteínas en componentes distintos).";
        }

        // --- Reconstruir la ruta usando el arreglo prev[] ---
        MyLinkedList<String> path = new MyLinkedList<>();
        int current = endIdx;
        while (current != -1) {
            path.add(vertices.get(current).getName());
            current = prev[current];
        }

        // La ruta está en orden inverso; invertirla
        StringBuilder sb = new StringBuilder();
        sb.append("=== Ruta Metabólica más Corta ===\n");
        sb.append("Origen: ").append(startName).append("  →  Destino: ").append(endName).append("\n\n");
        sb.append("Ruta: ");
        for (int i = path.size() - 1; i >= 0; i--) {
            sb.append(path.get(i));
            if (i > 0) sb.append(" → ");
        }
        sb.append("\nCosto total (resistencia): ").append(dist[endIdx]);
        return sb.toString();
    }

    /**
     * Retorna el índice de un vértice en la lista de vértices.
     * Método auxiliar usado por Dijkstra.
     * @param v El vértice a buscar.
     * @return Su índice, o -1 si no se encuentra.
     */
    private int indexOf(Vertex v) {
        for (int i = 0; i < vertices.size(); i++) {
            if (vertices.get(i) == v) return i;
        }
        return -1;
    }

    // =========================================================================
    // ALGORITMO: BFS — Detección de Complejos Proteicos (Requerimiento E)
    // =========================================================================

    /**
     * Detecta los complejos proteicos (componentes conexos) del grafo
     * usando Búsqueda en Anchura (BFS) con MyLinkedList como cola.
     *
     * <p>Biológicamente, un componente conexo representa un grupo de proteínas
     * que trabajan juntas de forma aislada del resto del interactoma.</p>
     *
     * @return String con la descripción de todos los complejos proteicos encontrados.
     */
    public String bfsComponentesConexos() {
        int n = vertices.size();
        if (n == 0) return "El grafo está vacío. Cargue un archivo primero.";

        boolean[] visited = new boolean[n];
        StringBuilder sb = new StringBuilder("=== Complejos Proteicos Detectados (BFS) ===\n\n");
        int numComplejos = 0;

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                numComplejos++;
                MyLinkedList<String> complejo = new MyLinkedList<>();

                // BFS: usar MyLinkedList como cola (enqueue al final, dequeue del frente)
                MyLinkedList<Integer> queue = new MyLinkedList<>();
                queue.add(i);
                visited[i] = true;

                while (!queue.isEmpty()) {
                    // Dequeue: extraer el primer elemento (índice 0)
                    int u = queue.get(0);
                    queue.remove(queue.get(0));
                    complejo.add(vertices.get(u).getName());

                    // Encolar todos los vecinos no visitados
                    MyLinkedList<Edge> edges = vertices.get(u).getEdges();
                    for (int j = 0; j < edges.size(); j++) {
                        int v = indexOf(edges.get(j).getTarget());
                        if (v != -1 && !visited[v]) {
                            visited[v] = true;
                            queue.add(v);
                        }
                    }
                }

                // Formatear resultado del complejo
                sb.append("Complejo #").append(numComplejos).append(": [");
                for (int k = 0; k < complejo.size(); k++) {
                    sb.append(complejo.get(k));
                    if (k < complejo.size() - 1) sb.append(", ");
                }
                sb.append("]  (").append(complejo.size()).append(" proteínas)\n");
            }
        }

        sb.append("\nTotal de complejos proteicos: ").append(numComplejos);
        return sb.toString();
    }

    // =========================================================================
    // ALGORITMO: HUBS — Centralidad de Grado (Requerimiento G)
    // =========================================================================

    /**
     * Calcula la centralidad de grado de todas las proteínas e identifica los Hubs.
     *
     * <p>Los Hubs son las proteínas con mayor número de conexiones directas.
     * Son las dianas terapéuticas primarias: al ser anuladas por un fármaco,
     * podrían colapsar la red biológica del patógeno.</p>
     *
     * @return String con el listado ordenado de proteínas por grado y el Hub principal.
     */
    public String getHubs() {
        int n = vertices.size();
        if (n == 0) return "El grafo está vacío. Cargue un archivo primero.";

        // Ordenar por grado descendente (burbuja sobre arreglo de índices)
        int[] indices = new int[n];
        for (int i = 0; i < n; i++) indices[i] = i;

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                if (vertices.get(indices[j]).getDegree() < vertices.get(indices[j + 1]).getDegree()) {
                    int tmp = indices[j];
                    indices[j] = indices[j + 1];
                    indices[j + 1] = tmp;
                }
            }
        }

        StringBuilder sb = new StringBuilder("=== Identificación de Hubs (Centralidad de Grado) ===\n\n");
        sb.append(String.format("%-15s %s\n", "Proteína", "Conexiones (Grado)"));
        sb.append("-------------------------------\n");

        for (int i = 0; i < n; i++) {
            Vertex v = vertices.get(indices[i]);
            String marker = (i == 0) ? "  ← HUB PRINCIPAL ★" : "";
            sb.append(String.format("%-15s %d%s\n", v.getName(), v.getDegree(), marker));
        }

        Vertex hub = vertices.get(indices[0]);
        sb.append("\n→ Diana terapéutica primaria: ").append(hub.getName())
          .append(" (").append(hub.getDegree()).append(" conexiones)");
        sb.append("\n  Al ser anulada por un fármaco, afectaría directamente a ")
          .append(hub.getDegree()).append(" proteína(s).");

        return sb.toString();
    }

    /**
     * Retorna la lista de vértices (proteínas) del grafo.
     * @return MyLinkedList con todos los vértices.
     */
    public MyLinkedList<Vertex> getVertices() {
        return vertices;
    }
}
