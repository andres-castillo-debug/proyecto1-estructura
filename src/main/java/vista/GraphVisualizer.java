package vista;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.swing_viewer.ViewPanel;
import org.graphstream.ui.view.Viewer;
import modelo_grafo.BioGraph;
import modelo_grafo.Vertex;
import modelo_grafo.Edge;
import estructuras_primitivas.MyLinkedList;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;

/**
 * Clase encargada de la representación visual del grafo biológico usando GraphStream.
 *
 * <p>Convierte el modelo interno (BioGraph con MyLinkedList) a un grafo de GraphStream
 * para su visualización en un JPanel de Swing. Los complejos proteicos (componentes
 * conexos) se colorean con distintos colores para facilitar su identificación visual.</p>
 */
public class GraphVisualizer {

    // Paleta de colores para distinguir complejos proteicos
    private static final String[] COLORS = {
        "#e63946", "#2a9d8f", "#e9c46a", "#457b9d",
        "#f4a261", "#6a4c93", "#52b788", "#ff6b6b"
    };

    /**
     * Actualiza la visualización del grafo en el JPanel dado.
     * Colorea cada componente conexo (complejo proteico) con un color distinto.
     *
     * @param modelGraph El grafo biológico con los datos actuales.
     * @param panel      El JPanel donde se renderizará el grafo.
     */
    public void updateGraph(BioGraph modelGraph, JPanel panel) {
        System.setProperty("org.graphstream.ui", "swing");

        Graph gsGraph = new SingleGraph("BioGraphView");

        // Estilo base: nodos grandes con etiqueta, aristas con etiqueta de peso
        gsGraph.setAttribute("ui.stylesheet",
            "node { " +
            "   size: 28px; " +
            "   text-size: 13px; " +
            "   text-style: bold; " +
            "   text-color: white; " +
            "   stroke-mode: plain; " +
            "   stroke-color: #ffffff; " +
            "   stroke-width: 2px; " +
            "} " +
            "edge { " +
            "   text-size: 11px; " +
            "   text-color: #333333; " +
            "   text-background-mode: rounded-box; " +
            "   text-background-color: #ffffffcc; " +
            "   text-padding: 2px; " +
            "}"
        );

        MyLinkedList<Vertex> vertices = modelGraph.getVertices();
        int n = vertices.size();
        if (n == 0) return;

        // --- Asignar colores por componente conexo (BFS) ---
        boolean[] visited = new boolean[n];
        int[] colorIdx = new int[n];
        int componentCount = 0;

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                String color = COLORS[componentCount % COLORS.length];
                // BFS para colorear este componente
                MyLinkedList<Integer> queue = new MyLinkedList<>();
                queue.add(i);
                visited[i] = true;
                colorIdx[i] = componentCount;

                while (!queue.isEmpty()) {
                    int u = queue.get(0);
                    queue.remove(queue.get(0));
                    MyLinkedList<Edge> edges = vertices.get(u).getEdges();
                    for (int j = 0; j < edges.size(); j++) {
                        int v = indexOf(vertices, edges.get(j).getTarget());
                        if (v != -1 && !visited[v]) {
                            visited[v] = true;
                            colorIdx[v] = componentCount;
                            queue.add(v);
                        }
                    }
                }
                componentCount++;
            }
        }

        // --- Agregar nodos con sus colores de componente ---
        for (int i = 0; i < n; i++) {
            Vertex v = vertices.get(i);
            String color = COLORS[colorIdx[i] % COLORS.length];
            org.graphstream.graph.Node gsNode = gsGraph.addNode(v.getName());
            gsNode.setAttribute("ui.label", v.getName());
            gsNode.setAttribute("ui.style", "fill-color: " + color + ";");
        }

        // --- Agregar aristas evitando duplicados visuales ---
        for (int i = 0; i < n; i++) {
            Vertex v = vertices.get(i);
            MyLinkedList<Edge> edges = v.getEdges();
            for (int j = 0; j < edges.size(); j++) {
                Edge e = edges.get(j);
                String idEdge    = v.getName() + "-" + e.getTarget().getName();
                String idEdgeRev = e.getTarget().getName() + "-" + v.getName();

                if (gsGraph.getEdge(idEdge) == null && gsGraph.getEdge(idEdgeRev) == null) {
                    gsGraph.addEdge(idEdge, v.getName(), e.getTarget().getName())
                           .setAttribute("ui.label", String.valueOf(e.getWeight()));
                }
            }
        }

        // --- Incrustar en el JPanel ---
        panel.removeAll();
        SwingViewer viewer = new SwingViewer(gsGraph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        viewer.enableAutoLayout();
        ViewPanel viewPanel = (ViewPanel) viewer.addDefaultView(false);
        viewPanel.setPreferredSize(new Dimension(panel.getWidth(), panel.getHeight()));

        panel.setLayout(new BorderLayout());
        panel.add(viewPanel, BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();
    }

    /**
     * Índice de un vértice dentro de la lista de vértices del grafo.
     * Método auxiliar para la asignación de colores por componente.
     */
    private int indexOf(MyLinkedList<Vertex> vertices, Vertex target) {
        for (int i = 0; i < vertices.size(); i++) {
            if (vertices.get(i) == target) return i;
        }
        return -1;
    }
}
