package controlador;

import modelo_grafo.BioGraph;
import vista.MainFrame;
import vista.GraphVisualizer;
import utilidades.FileUtils;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Controlador principal de la aplicación BioGraph (patrón MVC).
 *
 * <p>Conecta la Vista ({@link MainFrame}) con el Modelo ({@link BioGraph}),
 * registrando los ActionListeners de todos los botones y coordinando las
 * operaciones de carga, guardado, modificación del grafo y ejecución de algoritmos.</p>
 */
public class Controller {

    private BioGraph model;
    private MainFrame view;
    private GraphVisualizer visualizer;
    private String currentFilePath; // Ruta del archivo actualmente cargado

    /**
     * Constructor que inicializa el controlador y registra todos los listeners.
     * @param model El modelo del grafo biológico.
     * @param view  La vista principal de la aplicación.
     */
    public Controller(BioGraph model, MainFrame view) {
        this.model      = model;
        this.view       = view;
        this.visualizer = new GraphVisualizer();
        this.currentFilePath = null;
        initController();
    }

    /**
     * Registra los ActionListeners de todos los botones de la interfaz.
     */
    private void initController() {

        // ── 1. CARGAR ARCHIVO ─────────────────────────────────────────────
        view.btnLoad.addActionListener(e -> {
            // Requerimiento A: advertir al usuario antes de reemplazar datos
            if (model.getVertices().size() > 0) {
                int opt = JOptionPane.showConfirmDialog(view,
                    "Ya hay un grafo cargado en memoria.\n¿Desea guardar los datos actuales antes de cargar un nuevo archivo?",
                    "Advertencia — Datos no guardados",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE);

                if (opt == JOptionPane.CANCEL_OPTION) return;
                if (opt == JOptionPane.YES_OPTION && currentFilePath != null) {
                    String saveResult = FileUtils.saveGraph(currentFilePath, model);
                    view.printLog(saveResult);
                }
            }

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Seleccionar archivo de datos de proteínas");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos CSV/TXT", "csv", "txt"));

            if (fileChooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
                currentFilePath = fileChooser.getSelectedFile().getPath();
                String result = FileUtils.loadGraph(currentFilePath, model);
                view.printLog(result);
                refreshGraph();
            }
        });

        // ── 2. GUARDAR ARCHIVO ────────────────────────────────────────────
        view.btnSave.addActionListener(e -> {
            if (model.getVertices().isEmpty()) {
                view.showError("No hay datos en memoria para guardar.");
                return;
            }
            if (currentFilePath == null) {
                // Si no hay archivo cargado, pedir dónde guardar
                JFileChooser fc = new JFileChooser();
                fc.setDialogTitle("Guardar grafo como...");
                fc.setFileFilter(new FileNameExtensionFilter("Archivo CSV", "csv"));
                if (fc.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
                    currentFilePath = fc.getSelectedFile().getPath();
                    if (!currentFilePath.endsWith(".csv")) currentFilePath += ".csv";
                } else return;
            }
            String result = FileUtils.saveGraph(currentFilePath, model);
            view.printLog(result);
        });

        // ── 3. AGREGAR PROTEÍNA ───────────────────────────────────────────
        view.btnAddNode.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(view,
                "Ingrese el nombre de la nueva proteína:", "Agregar Proteína",
                JOptionPane.PLAIN_MESSAGE);

            if (name == null || name.trim().isEmpty()) return;
            name = name.trim().toUpperCase();

            if (model.getProtein(name) != null) {
                view.showError("La proteína '" + name + "' ya existe en el grafo.");
                return;
            }
            model.addProtein(name);
            view.printLog("Proteína agregada: " + name);
            refreshGraph();
        });

        // ── 4. AGREGAR INTERACCIÓN ────────────────────────────────────────
        view.btnAddEdge.addActionListener(e -> {
            if (model.getVertices().size() < 2) {
                view.showError("Se necesitan al menos 2 proteínas en el grafo para agregar una interacción.");
                return;
            }

            String src = JOptionPane.showInputDialog(view,
                "Proteína ORIGEN:", "Agregar Interacción", JOptionPane.PLAIN_MESSAGE);
            if (src == null || src.trim().isEmpty()) return;

            String dst = JOptionPane.showInputDialog(view,
                "Proteína DESTINO:", "Agregar Interacción", JOptionPane.PLAIN_MESSAGE);
            if (dst == null || dst.trim().isEmpty()) return;

            String weightStr = JOptionPane.showInputDialog(view,
                "Costo de la interacción (número entero):", "Agregar Interacción", JOptionPane.PLAIN_MESSAGE);
            if (weightStr == null || weightStr.trim().isEmpty()) return;

            try {
                int weight = Integer.parseInt(weightStr.trim());
                if (weight <= 0) { view.showError("El costo debe ser un número entero positivo."); return; }

                src = src.trim().toUpperCase();
                dst = dst.trim().toUpperCase();

                if (model.getProtein(src) == null) { view.showError("La proteína '" + src + "' no existe."); return; }
                if (model.getProtein(dst) == null) { view.showError("La proteína '" + dst + "' no existe."); return; }

                model.addInteraction(src, dst, weight);
                view.printLog("Interacción agregada: " + src + " ↔ " + dst + " (costo: " + weight + ")");
                refreshGraph();

            } catch (NumberFormatException ex) {
                view.showError("El costo debe ser un número entero válido.");
            }
        });

        // ── 5. ELIMINAR PROTEÍNA ──────────────────────────────────────────
        view.btnRemoveNode.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(view,
                "Ingrese el nombre de la proteína a eliminar\n(simula el efecto de un fármaco):",
                "Eliminar Proteína", JOptionPane.PLAIN_MESSAGE);

            if (name == null || name.trim().isEmpty()) return;
            name = name.trim().toUpperCase();

            int confirm = JOptionPane.showConfirmDialog(view,
                "¿Confirma eliminar la proteína '" + name + "' y todas sus interacciones?",
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirm != JOptionPane.YES_OPTION) return;

            if (model.removeProtein(name)) {
                view.printLog("Proteína '" + name + "' eliminada del grafo (simulación de fármaco).");
                refreshGraph();
            } else {
                view.showError("La proteína '" + name + "' no existe en el grafo.");
            }
        });

        // ── 6. DIJKSTRA — Ruta Metabólica más Corta ──────────────────────
        view.btnCalcRoute.addActionListener(e -> {
            if (model.getVertices().isEmpty()) {
                view.showError("El grafo está vacío. Cargue un archivo primero.");
                return;
            }
            String start = JOptionPane.showInputDialog(view,
                "Ingrese la Proteína ORIGEN:", "Dijkstra — Ruta más Corta", JOptionPane.PLAIN_MESSAGE);
            if (start == null || start.trim().isEmpty()) return;

            String end = JOptionPane.showInputDialog(view,
                "Ingrese la Proteína DESTINO:", "Dijkstra — Ruta más Corta", JOptionPane.PLAIN_MESSAGE);
            if (end == null || end.trim().isEmpty()) return;

            String resultado = model.dijkstra(start.trim(), end.trim());
            view.printResult(resultado);
        });

        // ── 7. HUBS — Centralidad de Grado ───────────────────────────────
        view.btnShowHubs.addActionListener(e -> {
            if (model.getVertices().isEmpty()) {
                view.showError("El grafo está vacío. Cargue un archivo primero.");
                return;
            }
            String resultado = model.getHubs();
            view.printResult(resultado);
        });

        // ── 8. COMPLEJOS PROTEICOS — BFS ──────────────────────────────────
        view.btnFindComplex.addActionListener(e -> {
            if (model.getVertices().isEmpty()) {
                view.showError("El grafo está vacío. Cargue un archivo primero.");
                return;
            }
            String resultado = model.bfsComponentesConexos();
            view.printResult(resultado);
        });

        // ── 9. LIMPIAR GRAFO ──────────────────────────────────────────────
        view.btnClearGraph.addActionListener(e -> {
            if (model.getVertices().isEmpty()) return;
            int opt = JOptionPane.showConfirmDialog(view,
                "¿Desea guardar los datos antes de limpiar el grafo?",
                "Limpiar Grafo", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

            if (opt == JOptionPane.CANCEL_OPTION) return;
            if (opt == JOptionPane.YES_OPTION && currentFilePath != null) {
                view.printLog(FileUtils.saveGraph(currentFilePath, model));
            }
            model.clear();
            currentFilePath = null;
            view.graphPanel.removeAll();
            view.graphPanel.revalidate();
            view.graphPanel.repaint();
            view.printLog("Grafo limpiado. Cargue un nuevo archivo para comenzar.");
        });
    }

    /**
     * Refresca la visualización del grafo en el panel de la vista.
     * Se llama después de cualquier operación que modifique el grafo.
     */
    private void refreshGraph() {
        if (model.getVertices().size() > 0) {
            visualizer.updateGraph(model, view.graphPanel);
        }
    }
}
