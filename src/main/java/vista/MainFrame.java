package vista;

import javax.swing.*;
import java.awt.*;

/**
 * Clase que representa la interfaz gráfica principal (Vista) de la aplicación BioGraph.
 *
 * <p>Implementa una ventana Swing con tres zonas principales:</p>
 * <ul>
 *   <li>Barra de herramientas superior con todos los botones de acción.</li>
 *   <li>Panel central para la visualización del grafo con GraphStream.</li>
 *   <li>Panel inferior con área de texto para resultados y mensajes al usuario.</li>
 * </ul>
 *
 * <p>Todos los botones son públicos para que el {@link controlador.Controller}
 * pueda registrar sus ActionListeners siguiendo el patrón MVC.</p>
 */
public class MainFrame extends JFrame {

    // --- BOTONES (públicos para el Controller) ---
    /** Botón para cargar un archivo CSV mediante JFileChooser. */
    public JButton btnLoad;
    /** Botón para guardar los datos actuales en el archivo de origen. */
    public JButton btnSave;
    /** Botón para agregar una nueva proteína al grafo. */
    public JButton btnAddNode;
    /** Botón para agregar una nueva interacción entre proteínas. */
    public JButton btnAddEdge;
    /** Botón para eliminar una proteína (simular efecto de un fármaco). */
    public JButton btnRemoveNode;
    /** Botón para ejecutar el algoritmo de Dijkstra (ruta más corta). */
    public JButton btnCalcRoute;
    /** Botón para mostrar los Hubs (proteínas con mayor centralidad de grado). */
    public JButton btnShowHubs;
    /** Botón para detectar complejos proteicos mediante BFS. */
    public JButton btnFindComplex;
    /** Botón para limpiar el grafo en memoria y la visualización. */
    public JButton btnClearGraph;

    // --- PANELES Y ÁREA DE RESULTADOS ---
    /** Panel central donde GraphStream renderiza el grafo. */
    public JPanel graphPanel;
    /** Área de texto donde se muestran los resultados de los algoritmos. */
    public JTextArea txtOutput;

    /**
     * Constructor que inicializa y construye la interfaz gráfica.
     */
    public MainFrame() {
        initUI();
    }

    /**
     * Construye todos los componentes Swing y los organiza en la ventana.
     */
    private void initUI() {
        setTitle("BioGraph — Análisis de Interacciones Proteicas para el Descubrimiento de Fármacos");
        setSize(1200, 800);
        setMinimumSize(new Dimension(900, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ── 1. BARRA DE HERRAMIENTAS SUPERIOR ──────────────────────────────
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 8));
        toolbar.setBackground(new Color(30, 41, 59));
        toolbar.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        btnLoad       = makeButton("📂 Cargar",       new Color(69, 123, 157));
        btnSave       = makeButton("💾 Guardar",       new Color(42, 157, 143));
        btnAddNode    = makeButton("＋ Proteína",      new Color(82, 183, 136));
        btnAddEdge    = makeButton("＋ Interacción",   new Color(82, 183, 136));
        btnRemoveNode = makeButton("✕ Eliminar Prot.", new Color(230, 57, 70));
        btnCalcRoute  = makeButton("⚡ Dijkstra",      new Color(244, 162, 97));
        btnShowHubs   = makeButton("★ Hubs",           new Color(233, 196, 106));
        btnFindComplex= makeButton("⬡ Complejos",     new Color(106, 76, 147));
        btnClearGraph = makeButton("🗑 Limpiar",        new Color(100, 116, 139));

        toolbar.add(btnLoad);
        toolbar.add(btnSave);
        toolbar.add(makeSep());
        toolbar.add(btnAddNode);
        toolbar.add(btnAddEdge);
        toolbar.add(btnRemoveNode);
        toolbar.add(makeSep());
        toolbar.add(btnCalcRoute);
        toolbar.add(btnShowHubs);
        toolbar.add(btnFindComplex);
        toolbar.add(makeSep());
        toolbar.add(btnClearGraph);

        add(toolbar, BorderLayout.NORTH);

        // ── 2. PANEL CENTRAL (Visualización del grafo) ─────────────────────
        graphPanel = new JPanel(new BorderLayout());
        graphPanel.setBackground(new Color(15, 23, 42));
        graphPanel.setBorder(BorderFactory.createLineBorder(new Color(30, 41, 59), 2));

        // Mensaje inicial centrado
        JLabel lbEmpty = new JLabel("Cargue un archivo CSV para visualizar el grafo", SwingConstants.CENTER);
        lbEmpty.setForeground(new Color(100, 116, 139));
        lbEmpty.setFont(new Font("SansSerif", Font.ITALIC, 14));
        graphPanel.add(lbEmpty, BorderLayout.CENTER);

        add(graphPanel, BorderLayout.CENTER);

        // ── 3. PANEL INFERIOR (Área de resultados) ─────────────────────────
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setPreferredSize(new Dimension(100, 180));
        bottomPanel.setBackground(new Color(15, 23, 42));
        bottomPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(30, 41, 59)),
            "  Resultados y Mensajes  ",
            0, 0,
            new Font("SansSerif", Font.BOLD, 12),
            new Color(148, 163, 184)
        ));

        txtOutput = new JTextArea();
        txtOutput.setEditable(false);
        txtOutput.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtOutput.setBackground(new Color(15, 23, 42));
        txtOutput.setForeground(new Color(226, 232, 240));
        txtOutput.setCaretColor(Color.WHITE);
        txtOutput.setMargin(new Insets(6, 10, 6, 10));
        txtOutput.setText("Bienvenido a BioGraph. Cargue un archivo CSV para comenzar.\n");

        JScrollPane scroll = new JScrollPane(txtOutput);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(new Color(15, 23, 42));
        bottomPanel.add(scroll, BorderLayout.CENTER);

        add(bottomPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null); // Centrar en pantalla
    }

    /**
     * Crea un botón con estilo consistente para la barra de herramientas.
     * @param text  Texto (puede incluir emoji) del botón.
     * @param color Color de fondo del botón.
     * @return El JButton configurado.
     */
    private JButton makeButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        return btn;
    }

    /**
     * Crea un separador vertical para la barra de herramientas.
     */
    private JSeparator makeSep() {
        JSeparator sep = new JSeparator(SwingConstants.VERTICAL);
        sep.setPreferredSize(new Dimension(1, 28));
        sep.setForeground(new Color(51, 65, 85));
        return sep;
    }

    /**
     * Agrega un mensaje con prefijo "▶ " al área de resultados y hace scroll hacia abajo.
     * @param msg El mensaje a mostrar.
     */
    public void printLog(String msg) {
        txtOutput.append("▶ " + msg + "\n");
        txtOutput.setCaretPosition(txtOutput.getDocument().getLength());
    }

    /**
     * Muestra un resultado multilínea (como salida de algoritmos) en el área de texto.
     * @param result El texto completo a mostrar.
     */
    public void printResult(String result) {
        txtOutput.append("\n" + result + "\n");
        txtOutput.setCaretPosition(txtOutput.getDocument().getLength());
    }

    /**
     * Muestra un cuadro de diálogo de alerta (pop-up) al usuario.
     * @param msg El mensaje de la alerta.
     */
    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg, "BioGraph", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Muestra un cuadro de diálogo de error al usuario.
     * @param msg El mensaje de error.
     */
    public void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error — BioGraph", JOptionPane.ERROR_MESSAGE);
    }
}
