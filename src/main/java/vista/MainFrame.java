/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import javax.swing.*;
import java.awt.*;
/**
 * Clase que representa la interfaz gráfica principal (Vista) de la aplicación BioGraph.
 * Contiene los botones, paneles y áreas de texto necesarios para interactuar con el usuario
 * y visualizar el grafo de interacciones proteicas.
 */
public class MainFrame extends JFrame {
    
    // --- BOTONES (Públicos para el Controller) ---
    public JButton btnLoad;
    public JButton btnSave;        // Nuevo: Para persistencia
    public JButton btnAddNode;
    public JButton btnAddEdge;
    public JButton btnRemoveNode;
    public JButton btnCalcRoute;
    public JButton btnShowHubs;
    public JButton btnFindComplex;
    public JButton btnClearGraph; // Opcional: Limpiar todo

    // --- PANELES Y TEXTO ---
    public JPanel graphPanel;      // Aquí se dibuja el grafo
    public JTextArea txtOutput;    // Aquí se escriben los resultados (Rutas, Errores)

    public MainFrame() {
        initUI();
    }

    private void initUI() {
        setTitle("BioGraph - Análisis de Interacciones Proteicas");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 1. PANEL SUPERIOR (Botones)
        JPanel toolbar = new JPanel();
        toolbar.setLayout(new FlowLayout(FlowLayout.LEFT));
        toolbar.setBackground(new Color(230, 230, 230));

        // Inicializar botones
        btnLoad = new JButton("Cargar");
        btnSave = new JButton("Guardar");
        btnAddNode = new JButton("+ Prot");
        btnAddEdge = new JButton("+ Interacción");
        btnRemoveNode = new JButton("- Prot");
        btnCalcRoute = new JButton("Dijkstra");
        btnShowHubs = new JButton("Hubs");
        btnFindComplex = new JButton("Complejos");
        btnClearGraph = new JButton("Limpiar");

        // Agregar al panel
        toolbar.add(btnLoad);
        toolbar.add(btnSave);
        toolbar.add(new JSeparator(SwingConstants.VERTICAL)); // Separador visual
        toolbar.add(btnAddNode);
        toolbar.add(btnAddEdge);
        toolbar.add(btnRemoveNode);
        toolbar.add(new JSeparator(SwingConstants.VERTICAL));
        toolbar.add(btnCalcRoute);
        toolbar.add(btnShowHubs);
        toolbar.add(btnFindComplex);
        toolbar.add(btnClearGraph);

        add(toolbar, BorderLayout.NORTH);

        // 2. PANEL CENTRAL (Grafo)
        graphPanel = new JPanel();
        graphPanel.setLayout(new BorderLayout());
        graphPanel.setBackground(Color.WHITE);
        graphPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        add(graphPanel, BorderLayout.CENTER);

        // 3. PANEL INFERIOR (Resultados / Log)
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.setPreferredSize(new Dimension(100, 150)); // Alto del panel de texto
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Resultados y Mensajes"));

        txtOutput = new JTextArea();
        txtOutput.setEditable(false); // Que el usuario no pueda borrar
        txtOutput.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtOutput.setText("Bienvenido a BioGraph. Cargue un archivo para comenzar...\n");
        
        // ScrollPane por si el texto es muy largo
        JScrollPane scroll = new JScrollPane(txtOutput);
        bottomPanel.add(scroll, BorderLayout.CENTER);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Método helper para imprimir mensajes en el área de texto
    public void printLog(String msg) {
        txtOutput.append("> " + msg + "\n");
        // Hacer scroll automático hacia abajo
        txtOutput.setCaretPosition(txtOutput.getDocument().getLength());
    }
    
    // Método para alertas urgentes (Pop-up)
    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }
}