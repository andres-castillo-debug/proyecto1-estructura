/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilidades;
import modelo_grafo.BioGraph;
import java.io.*;

public class FileUtils {
    
    // Cargar grafo desde CSV 
    public static void loadGraph(String path, BioGraph graph) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Formato: ProteinaA, ProteinaB, Peso 
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String p1 = parts[0].trim();
                    String p2 = parts[1].trim();
                    int w = Integer.parseInt(parts[2].trim());
                    
                    graph.addProtein(p1);
                    graph.addProtein(p2);
                    graph.addInteraction(p1, p2, w);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Guardar grafo (Persistencia )
    public static void saveGraph(String path, BioGraph graph) {
        // Recorrer vértices y escribir en el archivo
    }
}
