package utilidades;

import modelo_grafo.BioGraph;
import modelo_grafo.Vertex;
import modelo_grafo.Edge;
import estructuras_primitivas.MyLinkedList;
import java.io.*;

/**
 * Clase de utilidades para la gestión de persistencia del grafo biológico.
 *
 * <p>Provee métodos estáticos para cargar el grafo desde un archivo CSV y
 * para guardar (actualizar) el grafo en el mismo archivo, reflejando todos
 * los cambios realizados en memoria (altas y bajas de proteínas e interacciones).</p>
 *
 * <p>Formato del archivo CSV: {@code Proteina_A,Proteina_B,Costo_Interaccion}</p>
 */
public class FileUtils {

    /**
     * Carga un grafo desde un archivo CSV/TXT con el formato:
     * {@code Proteina_Origen,Proteina_Destino,Peso}
     *
     * <p>Limpia el grafo actual antes de cargar para evitar datos residuales.
     * Omite la primera línea si contiene una cabecera no numérica.
     * Ignora líneas malformadas sin interrumpir la carga.</p>
     *
     * @param path  Ruta absoluta del archivo a cargar.
     * @param graph El objeto BioGraph donde se almacenarán los datos cargados.
     * @return Mensaje de resultado indicando cuántas interacciones se cargaron, o error.
     */
    public static String loadGraph(String path, BioGraph graph) {
        graph.clear(); // Limpiar datos anteriores antes de cargar
        int linesLoaded = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length != 3) continue;

                String p1 = parts[0].trim();
                String p2 = parts[1].trim();

                // Saltar cabecera: si el tercer campo no es numérico, es encabezado
                if (firstLine) {
                    firstLine = false;
                    try {
                        Integer.parseInt(parts[2].trim());
                    } catch (NumberFormatException nfe) {
                        continue; // Es encabezado, saltarlo
                    }
                }

                try {
                    int w = Integer.parseInt(parts[2].trim());
                    graph.addProtein(p1);
                    graph.addProtein(p2);
                    graph.addInteraction(p1, p2, w);
                    linesLoaded++;
                } catch (NumberFormatException nfe) {
                    // Línea con dato inválido: ignorar y continuar
                }
            }

            return "Grafo cargado exitosamente. " + linesLoaded + " interacciones leídas.";

        } catch (FileNotFoundException e) {
            return "Error: No se encontró el archivo en la ruta indicada.";
        } catch (IOException e) {
            return "Error al leer el archivo: " + e.getMessage();
        }
    }

    /**
     * Guarda el estado actual del grafo en el archivo indicado, sobreescribiendo su contenido.
     *
     * <p>Recorre todos los vértices y sus aristas, escribiendo cada interacción
     * en formato {@code Proteina_A,Proteina_B,Peso}. Para el grafo no dirigido,
     * solo escribe cada arista una vez (evita duplicar A→B y B→A).</p>
     *
     * @param path  Ruta del archivo donde se guardará el grafo.
     * @param graph El objeto BioGraph con los datos actuales en memoria.
     * @return Mensaje de resultado indicando éxito o error.
     */
    public static String saveGraph(String path, BioGraph graph) {
        MyLinkedList<Vertex> vertices = graph.getVertices();
        // Llevar registro de aristas ya escritas para no duplicar en grafo no dirigido
        MyLinkedList<String> writtenEdges = new MyLinkedList<>();

        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
            for (int i = 0; i < vertices.size(); i++) {
                Vertex v = vertices.get(i);
                MyLinkedList<Edge> edges = v.getEdges();
                for (int j = 0; j < edges.size(); j++) {
                    Edge e = edges.get(j);
                    String edgeKey     = v.getName() + "-" + e.getTarget().getName();
                    String edgeKeyRev  = e.getTarget().getName() + "-" + v.getName();

                    // Escribir solo si esta arista (en ninguna dirección) fue escrita ya
                    if (!writtenEdges.contains(edgeKey) && !writtenEdges.contains(edgeKeyRev)) {
                        pw.println(v.getName() + "," + e.getTarget().getName() + "," + e.getWeight());
                        writtenEdges.add(edgeKey);
                    }
                }
            }
            return "Grafo guardado exitosamente en: " + path;
        } catch (IOException e) {
            return "Error al guardar el archivo: " + e.getMessage();
        }
    }
}
