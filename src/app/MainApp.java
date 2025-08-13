package app;

import src.ui.ResultTableModel;
import src.utils.IPUtils;
import src.utils.FileExporter;
import src.app.PingResult;
import src.app.NetworkScanner;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class MainApp {
    private JFrame frame;
    private JTextField ipInicioField, ipFinField;
    private JTable tablaResultados;
    private JProgressBar barraProgreso;
    private ResultTableModel tableModel;
    private JButton botonEscanear, botonLimpiar, botonGuardar;

    public MainApp() {
        inicializarInterfaz();
    }

    private void inicializarInterfaz() {
        frame = new JFrame("Escáner de Red");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Panel superior
        JPanel panelSuperior = new JPanel(new GridLayout(3, 2));
        ipInicioField = new JTextField("192.168.1.1");
        ipFinField = new JTextField("192.168.1.10");
        panelSuperior.add(new JLabel("IP de inicio:"));
        panelSuperior.add(ipInicioField);
        panelSuperior.add(new JLabel("IP de fin:"));
        panelSuperior.add(ipFinField);

        botonEscanear = new JButton("Escanear");
        botonLimpiar = new JButton("Limpiar");
        panelSuperior.add(botonEscanear);
        panelSuperior.add(botonLimpiar);
        frame.add(panelSuperior, BorderLayout.NORTH);

        // Tabla de resultados
        tableModel = new ResultTableModel();
        tablaResultados = new JTable(tableModel);
        frame.add(new JScrollPane(tablaResultados), BorderLayout.CENTER);

        // Barra de progreso y botón guardar
        JPanel panelInferior = new JPanel(new BorderLayout());
        barraProgreso = new JProgressBar();
        panelInferior.add(barraProgreso, BorderLayout.CENTER);

        botonGuardar = new JButton("Guardar resultados");
        panelInferior.add(botonGuardar, BorderLayout.EAST);

        frame.add(panelInferior, BorderLayout.SOUTH);

        // Acciones
        botonEscanear.addActionListener(this::accionEscanear);
        botonLimpiar.addActionListener(e -> tableModel.limpiar());
        botonGuardar.addActionListener(e -> FileExporter.exportarCSV(tableModel.getResultados()));

        frame.setVisible(true);
    }

    private void accionEscanear(ActionEvent e) {
        String ipInicio = ipInicioField.getText().trim();
        String ipFin = ipFinField.getText().trim();

        if (!IPUtils.validarIP(ipInicio) || !IPUtils.validarIP(ipFin)) {
            JOptionPane.showMessageDialog(frame, "Las direcciones IP no son válidas.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<String> rango = IPUtils.generarRango(ipInicio, ipFin);
        barraProgreso.setMaximum(rango.size());

        tableModel.limpiar();

        new Thread(() -> {
            for (int i = 0; i < rango.size(); i++) {
                String ip = rango.get(i);
                PingResult res = NetworkScanner.escanearIP(ip);
                tableModel.agregarResultado(res);
                barraProgreso.setValue(i + 1);
            }
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainApp::new);
    }
}