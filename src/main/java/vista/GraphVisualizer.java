/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package vista;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;
import modelo_grafo.BioGraph;
import modelo_grafo.Vertex;
import modelo_grafo.Edge;
import estructuras_primitivas.MyLinkedList;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.swing_viewer.ViewPanel;

/**
 * Clase encargada de la representación visual del grafo biológico.
 * y sus interacciones (aristas) de forma gráfica en la pantalla.
 */

public class GraphVisualizer {
    
    // Método para convertir tu BioGraph a un grafo de GraphStream [cite: 44]
    public void updateGraph(BioGraph modelGraph, JPanel panel) {
        
    // ESTA ES LA LÍNEA CLAVE:
   System.setProperty("org.graphstream.ui", "swing");

    Graph gsGraph = new SingleGraph("BioGraphView");
    // ... el resto de tu código sigue igual ...
        
        
        // Configuración de estilo CSS para nodos y aristas
        gsGraph.setAttribute("ui.stylesheet", "node { fill-color: red; size: 20px; text-size: 15; }");

        // 1. Agregar Nodos
        MyLinkedList<Vertex> vertices = modelGraph.getVertices();
        for(int i=0; i<vertices.size(); i++) {
            Vertex v = vertices.get(i);
            if (gsGraph.getNode(v.getName()) == null) {
                gsGraph.addNode(v.getName()).setAttribute("ui.label", v.getName());
            }
        }

        // 2. Agregar Aristas
        // Nota: GraphStream requiere IDs únicos para aristas.
        for(int i=0; i<vertices.size(); i++) {
            Vertex v = vertices.get(i);
            MyLinkedList<Edge> edges = v.getEdges();
            for(int j=0; j<edges.size(); j++) {
                Edge e = edges.get(j);
                String idEdge = v.getName() + "-" + e.getTarget().getName();
                String idEdgeRev = e.getTarget().getName() + "-" + v.getName();
                
                // Evitar duplicados visuales en grafo no dirigido
                if(gsGraph.getEdge(idEdge) == null && gsGraph.getEdge(idEdgeRev) == null) {
                    gsGraph.addEdge(idEdge, v.getName(), e.getTarget().getName())
                           .setAttribute("ui.label", String.valueOf(e.getWeight()));
                }
            }
        }

          // 3. Incrustar en el JPanel
    System.setProperty("org.graphstream.ui", "swing");
    
    // Usamos 'v' para el Viewer y 'viewPanel' para el componente
    org.graphstream.ui.view.Viewer v = new org.graphstream.ui.swing_viewer.SwingViewer(gsGraph, org.graphstream.ui.view.Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
    v.enableAutoLayout();
    
    org.graphstream.ui.swing_viewer.ViewPanel viewPanel = (org.graphstream.ui.swing_viewer.ViewPanel) v.addDefaultView(false);
    viewPanel.setPreferredSize(new java.awt.Dimension(panel.getWidth(), panel.getHeight()));
    
    panel.setLayout(new java.awt.BorderLayout());
    panel.add(viewPanel, java.awt.BorderLayout.CENTER);
    
    panel.revalidate();
    panel.repaint();
}
}