/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;
import modelo_grafo.BioGraph;
import vista.MainFrame;
import controlador.Controller;
/**
 * Clase principal que arranca la aplicación BioGraph.
 * Inicializa el modelo, la vista y el controlador.
 */

public class Main {
     /**
     * Método principal que ejecuta el programa.
     * @param args Argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        // Configuración para GraphStream
        System.setProperty("org.graphstream.ui", "swing"); 
        
        BioGraph model = new BioGraph();
        MainFrame view = new MainFrame();
        Controller controller = new Controller(model, view);
        
        view.setVisible(true);
    }
}
