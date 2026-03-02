/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo_grafo;
import estructuras_primitivas.MyLinkedList;
/**
 *
 * @author ile1
 */
public class Vertex {
    private String name;
    private MyLinkedList<Edge> edges; // Lista de adyacencia

    public Vertex(String name) {
        this.name = name;
        this.edges = new MyLinkedList<>();
    }

    public void addEdge(Vertex dst, int weight) {
        // Validación simple para no duplicar aristas si es necesario
        edges.add(new Edge(dst, weight));
    }

    public String getName() { return name; }
    public MyLinkedList<Edge> getEdges() { return edges; }
    
    // Para identificar Hubs [cite: 37]
    public int getDegree() {
        return edges.size(); 
    }
}