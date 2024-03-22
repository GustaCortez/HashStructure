/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.registrovacuna;

import javax.swing.SwingUtilities;

/**
 *
 * @author MAX JALAPA
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                RegistroVacuna registro = new RegistroVacuna();
                registro.setVisible(true);
            }
        });
    }
    
}
