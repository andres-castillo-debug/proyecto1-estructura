/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo_grafo;
import estructuras_primitivas.MyLinkedList;
/**
 * Clase que representa la estructura del grafo biológico.
 * Gestiona los vértices (proteínas) y sus conexiones.
 */
public class BioGraph {
    private MyLinkedList<Vertex> vertices;

    public BioGraph() {
        this.vertices = new MyLinkedList<>();
    }

    /**
 * Busca una proteína dentro del grafo usando su nombre.
 * @param name El nombre de la proteína a buscar.
 * @return El vértice correspondiente o null si no se encuentra.
 */
    public Vertex getProtein(String name) {
        for (int i = 0; i < vertices.size(); i++) {
            Vertex v = vertices.get(i);
            if (v.getName().equals(name)) return v;
        }
        return null;
    }

    public void addProtein(String name) {
        if (getProtein(name) == null) {
            vertices.add(new Vertex(name));
        }
    }

    // Requerimiento: Grafo no dirigido (agrega ida y vuelta) 
    public void addInteraction(String src, String dst, int weight) {
        Vertex v1 = getProtein(src);
        Vertex v2 = getProtein(dst);
        
        if (v1 != null && v2 != null) {
            v1.addEdge(v2, weight);
            v2.addEdge(v1, weight); // Grafo no dirigido
        }
    }

    // Algoritmo Dijkstra (Requerimiento )
    public String dijkstra(String startName, String endName) {
        // Implementación manual requerida sin PriorityQueue de Java.
        // Lógica: 
        // 1. Crear arreglo de distancias inicializado en infinito.
        // 2. Crear lista de visitados.
        // 3. Iterar seleccionando el nodo con menor distancia no visitado.
        return "Ruta más corta calculada...";
    }

    // Algoritmo BFS para componentes conexos (Requerimiento)
    public String bfs_ComponentesConexos() {
        // Usar MyLinkedList como una cola (queue).
        return "Complejos proteicos identificados...";
    }
    
    public MyLinkedList<Vertex> getVertices() {
        return vertices;
    }
}
