/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo_grafo;
/**
 *
 * "Clase que representa una arista o conexión entre dos proteínas, incluyendo su peso/resistencia."
 */
public class Edge {
    private Vertex target;
    private int weight; // Resistencia

    public Edge(Vertex target, int weight) {
        this.target = target;
        this.weight = weight;
    }

    public Vertex getTarget() { return target; }
    public int getWeight() { return weight; }
}

