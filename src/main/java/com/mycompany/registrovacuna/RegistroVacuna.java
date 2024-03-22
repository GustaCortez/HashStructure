/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.registrovacuna;

/**
 *
 * @author MAX JALAPA
 */
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class RegistroVacuna extends JFrame implements ActionListener {
    private JTextField txtCUI;
    private JTextArea txtAreaRegistro;
    private JButton btnRegistrar, btnBuscar;
    private HashMap<String, ArrayList<Vacuna>> registroVacunas;

    public RegistroVacuna() {
        super("Registro de Vacunas COVID-19");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new BorderLayout());

        // Crear componentes
        JPanel panelEntrada = new JPanel(new FlowLayout());
        JLabel lblCUI = new JLabel("CUI: ");
        txtCUI = new JTextField(15);
        btnRegistrar = new JButton("Registrar");
        btnBuscar = new JButton("Buscar");
        panelEntrada.add(lblCUI);
        panelEntrada.add(txtCUI);
        panelEntrada.add(btnRegistrar);
        panelEntrada.add(btnBuscar);

        txtAreaRegistro = new JTextArea(10, 30);
        JScrollPane scrollPane = new JScrollPane(txtAreaRegistro);

        // Agregar componentes al marco
        add(panelEntrada, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Agregar listeners
        btnRegistrar.addActionListener(this);
        btnBuscar.addActionListener(this);

        registroVacunas = new HashMap<>();
        cargarRegistros();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cui = txtCUI.getText().trim();
        if (e.getSource() == btnRegistrar) {
            registrarVacuna(cui);
        } else if (e.getSource() == btnBuscar) {
            buscarRegistro(cui);
        }
        txtCUI.setText("");
    }

    private void registrarVacuna(String cui) {
        String input = JOptionPane.showInputDialog(this, "Ingrese la vacuna y fecha (vacuna, fecha):");
        if (input != null && !input.isEmpty()) {
            String[] partes = input.split(",");
            if (partes.length == 2) {
                String vacuna = partes[0].trim();
                String fecha = partes[1].trim();
                Vacuna nuevaVacuna = new Vacuna(vacuna, fecha);
                if (registroVacunas.containsKey(cui)) {
                    registroVacunas.get(cui).add(nuevaVacuna);
                } else {
                    ArrayList<Vacuna> vacunas = new ArrayList<>();
                    vacunas.add(nuevaVacuna);
                    registroVacunas.put(cui, vacunas);
                }
                guardarRegistros();
                JOptionPane.showMessageDialog(this, "Vacuna registrada correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "Formato de entrada inválido. Debe ser: vacuna, fecha");
            }
        }
    }

    private void buscarRegistro(String cui) {
        if (registroVacunas.containsKey(cui)) {
            ArrayList<Vacuna> vacunas = registroVacunas.get(cui);
            StringBuilder sb = new StringBuilder();
            sb.append("Registro de vacunas para CUI: ").append(cui).append("\n");
            for (Vacuna vacuna : vacunas) {
                sb.append("- ").append(vacuna.getNombre()).append(", ").append(vacuna.getFecha()).append("\n");
            }
            txtAreaRegistro.setText(sb.toString());
        } else {
            JOptionPane.showMessageDialog(this, "No existe registro para el CUI ingresado.");
            txtAreaRegistro.setText("");
        }
    }

    private void cargarRegistros() {
        try {
            FileReader fr = new FileReader("vacunas.txt");
            BufferedReader br = new BufferedReader(fr);
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.isEmpty()) {
                    String[] partes = linea.split(":\\[");
                    String cui = partes[0];
                    String[] vacunas = partes[1].substring(0, partes[1].length() - 1).split("\\},\\{");
                    ArrayList<Vacuna> registroPersona = new ArrayList<>();
                    for (String vacuna : vacunas) {
                        vacuna = "{" + vacuna + "}";
                        String[] detalles = vacuna.replaceAll("[{}]", "").split(",");
                        String nombreVacuna = detalles[0].split(":")[1].replaceAll("\"", "");
                        String fecha = detalles[1].split(":")[1].replaceAll("\"", "");
                        registroPersona.add(new Vacuna(nombreVacuna, fecha));
                    }
                    registroVacunas.put(cui, registroPersona);
                }
            }
            br.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void guardarRegistros() {
        try {
            FileWriter fw = new FileWriter("vacunas.txt");
            BufferedWriter bw = new BufferedWriter(fw);
            for (Map.Entry<String, ArrayList<Vacuna>> entrada : registroVacunas.entrySet()) {
                String cui = entrada.getKey();
                ArrayList<Vacuna> vacunas = entrada.getValue();
                StringBuilder sb = new StringBuilder();
                sb.append(cui).append(":[");
                for (int i = 0; i < vacunas.size(); i++) {
                    Vacuna vacuna = vacunas.get(i);
                    sb.append("{\"vacuna\":\"").append(vacuna.getNombre()).append("\",\"fecha\":\"").append(vacuna.getFecha()).append("\"}");
                    if (i < vacunas.size() - 1) {
                        sb.append(",");
                    }
                }
                sb.append("]");
                bw.write(sb.toString());
                bw.newLine();
            }
            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
}


