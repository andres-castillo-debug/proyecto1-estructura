/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;
import modelo_grafo.BioGraph;
import vista.MainFrame;
import controlador.Controller;

public class Main {
    public static void main(String[] args) {
        // Configuración para GraphStream
        System.setProperty("org.graphstream.ui", "swing"); 
        
        BioGraph model = new BioGraph();
        MainFrame view = new MainFrame();
        Controller controller = new Controller(model, view);
        
        view.setVisible(true);
    }
}
