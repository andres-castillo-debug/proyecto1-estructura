package main;

import modelo_grafo.BioGraph;
import vista.MainFrame;
import controlador.Controller;

/**
 * Clase principal que arranca la aplicación BioGraph.
 *
 * <p>Inicializa el modelo ({@link BioGraph}), la vista ({@link MainFrame})
 * y el controlador ({@link Controller}) siguiendo el patrón MVC.
 * Configura GraphStream para usar el renderizador Swing antes de crear la interfaz.</p>
 */
public class Main {

    /**
     * Punto de entrada de la aplicación.
     * @param args Argumentos de línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        // Configuración obligatoria para que GraphStream use el backend Swing
        System.setProperty("org.graphstream.ui", "swing");

        // Lanzar la interfaz gráfica en el hilo de despacho de eventos (EDT)
        javax.swing.SwingUtilities.invokeLater(() -> {
            BioGraph model  = new BioGraph();
            MainFrame view  = new MainFrame();
            Controller ctrl = new Controller(model, view);
            view.setVisible(true);
        });
    }
}
