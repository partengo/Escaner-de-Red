package app;

import src.ui.ResultTableModel;
import src.utils.IPUtils;
import src.utils.FileExporter;
import src.utils.NetstatUtils;
import src.app.NetworkScanner;
import src.app.PingResult;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainApp {

    private ResultTableModel tableModel;
    private JProgressBar progressBar; // Barra de progreso

    private void inicializarInterfaz() {
        JFrame frame = new JFrame("Escáner de Red en Java");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1300, 600);

        // --- Panel superior con controles ---
        JPanel panelSuperior = new JPanel();
        JTextField campoInicio = new JTextField(10);
        JTextField campoFin = new JTextField(10);
        JButton botonEscanear = new JButton("Escanear");
        JButton botonGuardar = new JButton("Guardar resultados");
        JButton botonNetstatA = new JButton("Ver Conexiones (-a)");
        JButton botonNetstatN = new JButton("Ver Conexiones Numéricas (-n)");
        JButton botonNetstatProc = new JButton("Ver Conexiones + PID");

        panelSuperior.add(botonNetstatProc);
        panelSuperior.add(new JLabel("IP inicio:"));
        panelSuperior.add(campoInicio);
        panelSuperior.add(new JLabel("IP fin:"));
        panelSuperior.add(campoFin);
        panelSuperior.add(botonEscanear);
        panelSuperior.add(botonGuardar);
        panelSuperior.add(botonNetstatA);
        panelSuperior.add(botonNetstatN);

        // --- Barra de progreso ---
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);

        // --- Tabla de resultados ---
        tableModel = new ResultTableModel();
        JTable tabla = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabla);

        frame.add(panelSuperior, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(progressBar, BorderLayout.SOUTH);

        // Acción Guardar
        botonGuardar.addActionListener(e ->
                FileExporter.exportarCSV(tableModel.getResultados())
        );

        // Acción Escanear (con progreso)
        botonEscanear.addActionListener(e -> {
            String ipInicio = campoInicio.getText().trim();
            String ipFin = campoFin.getText().trim();

            if (!IPUtils.validarIP(ipInicio) || !IPUtils.validarIP(ipFin)) {
                JOptionPane.showMessageDialog(frame, "Rango de IP inválido");
                return;
            }

            List<String> rango = IPUtils.generarRango(ipInicio, ipFin);
            progressBar.setValue(0);

            // Usamos SwingWorker para no congelar la interfaz
            SwingWorker<Void, Integer> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() {
                    int total = rango.size();
                    int procesadas = 0;

                    for (String ip : rango) {
                        PingResult res = NetworkScanner.escanearIP(ip);
                        tableModel.agregarResultado(res);

                        procesadas++;
                        int progreso = (int) ((procesadas / (double) total) * 100);
                        publish(progreso);
                    }
                    return null;
                }

                @Override
                protected void process(List<Integer> chunks) {
                    progressBar.setValue(chunks.get(chunks.size() - 1));
                }

                @Override
                protected void done() {
                    JOptionPane.showMessageDialog(frame, "Escaneo finalizado.");
                }
            };
            worker.execute();
        });

        // Acción Netstat -a
        botonNetstatA.addActionListener(e -> {
            JDialog dialogo = mostrarDialogoCargando(frame, "Analizando conexiones...");
            SwingWorker<String, Void> worker = new SwingWorker<>() {
                @Override
                protected String doInBackground() {
                    return NetstatUtils.mostrarConexiones(); // netstat -a
                }

                @Override
                protected void done() {
                    dialogo.dispose();
                    try {
                        String salida = get();
                        JTextArea textArea = new JTextArea(salida, 20, 60);
                        textArea.setEditable(false);
                        JOptionPane.showMessageDialog(frame, new JScrollPane(textArea),
                                "Resultado de netstat -a", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
                    }
                }
            };
            worker.execute();
        });

        // Acción Netstat -n
        botonNetstatN.addActionListener(e -> {
            JDialog dialogo = mostrarDialogoCargando(frame, "Analizando conexiones numericas...");
            SwingWorker<String, Void> worker = new SwingWorker<>() {
                @Override
                protected String doInBackground() {
                    return NetstatUtils.mostrarConexionesNumericas(); // netstat -n
                }

                @Override
                protected void done() {
                    dialogo.dispose();
                    try {
                        String salida = get();
                        JTextArea textArea = new JTextArea(salida, 20, 60);
                        textArea.setEditable(false);
                        JOptionPane.showMessageDialog(frame, new JScrollPane(textArea),
                                "Resultado de netstat -n", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
                    }
                }
            };
            worker.execute();
        });
        // Acción Netstat -o / -p
        botonNetstatProc.addActionListener(e -> {
            JDialog dialogo = mostrarDialogoCargando(frame, "Ejecutando netstat -o / -p...");
            SwingWorker<String, Void> worker = new SwingWorker<>() {
                @Override
                protected String doInBackground() {
                    return NetstatUtils.mostrarConexionesConProcesos();
                }

                @Override
                protected void done() {
                    dialogo.dispose();
                    try {
                        String salida = get();
                        JTextArea textArea = new JTextArea(salida, 20, 60);
                        textArea.setEditable(false);
                        JOptionPane.showMessageDialog(frame, new JScrollPane(textArea),
                                "Resultado de netstat con procesos", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
                    }
                }
            };
            worker.execute();
        });
        frame.setVisible(true);
    }

    // --- Ventana de "Cargando..." ---
    private JDialog mostrarDialogoCargando(JFrame frame, String mensaje) {
        JDialog dialog = new JDialog(frame, "Cargando", true);
        JLabel label = new JLabel(mensaje, SwingConstants.CENTER);
        dialog.add(label);
        dialog.setSize(300, 100);
        dialog.setLocationRelativeTo(frame);

        // Mostrar en otro hilo para no bloquear
        SwingUtilities.invokeLater(() -> dialog.setVisible(true));

        return dialog;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainApp().inicializarInterfaz());
    }
}