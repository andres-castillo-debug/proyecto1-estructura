/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package controlador;

import modelo_grafo.BioGraph;
import vista.MainFrame;
import vista.GraphVisualizer;
import utilidades.FileUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JOptionPane;

public class Controller {
    private BioGraph model;
    private MainFrame view;
    private GraphVisualizer visualizer;

    public Controller(BioGraph model, MainFrame view) {
        this.model = model;
        this.view = view;
        this.visualizer = new GraphVisualizer();
        initController();
    }

    private void initController() {
        
        // --- 1. BOTÓN CARGAR ---
        view.btnLoad.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos CSV/TXT", "csv", "txt");
            fileChooser.setFileFilter(filter);
            
            int result = fileChooser.showOpenDialog(view);
            if (result == JFileChooser.APPROVE_OPTION) {
                String path = fileChooser.getSelectedFile().getPath();
                // Asegúrate que FileUtils.loadGraph sea static
                FileUtils.loadGraph(path, model); 
                visualizer.updateGraph(model, view.graphPanel);
                view.showMessage("Grafo cargado exitosamente.");
            }
        });

        // --- 2. BOTÓN DIJKSTRA (Ruta) ---
        view.btnCalcRoute.addActionListener(e -> {
            // Pedimos las proteínas al usuario con input dialogs
            String start = JOptionPane.showInputDialog(view, "Ingrese nombre de Proteína Origen:");
            String end = JOptionPane.showInputDialog(view, "Ingrese nombre de Proteína Destino:");
            
            if(start != null && end != null) {
                // Llamamos al algoritmo del modelo
                String resultado = model.dijkstra(start.trim(), end.trim());
                view.showMessage(resultado);
            }
        });

        // --- 3. BOTÓN HUBS (Mostrar proteínas importantes) ---
        view.btnShowHubs.addActionListener(e -> {
            // Asumimos que hiciste un método para listar hubs o mostrarlos
            // Si quieres solo resaltarlos visualmente:
            // visualizer.highlightHubs(model, view.graphPanel); 
            // O mostrar mensaje:
            view.showMessage("Funcionalidad de Hubs (Implementar lógica en BioGraph para listar)");
        });

        // --- 4. BOTÓN COMPLEJOS (BFS) ---
        view.btnFindComplex.addActionListener(e -> {
            String resultado = model.bfs_ComponentesConexos();
            view.showMessage(resultado);
        });
    }
}