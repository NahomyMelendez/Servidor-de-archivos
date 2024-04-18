/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ejemploproyecto;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Event;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author Personal
 */
/**
 *
 * @author nicos
 */
public class Cliente extends Frame implements ActionListener {

    static Socket sfd = null;
    static DataInputStream EntradaSocket;
    static DataOutputStream SalidaSocket;
    static TextField salida;
    static TextArea entrada;
    Button cargarButton;
    String texto;

    public Cliente() {
        setTitle("Chat");
        setSize(350, 200);

        salida = new TextField(30);
        salida.addActionListener(this);

        entrada = new TextArea();
        entrada.setEditable(false);

        setLayout(new BorderLayout());
        add("South", salida);
        add("Center", entrada);
        add("North", cargarButton);
        setVisible(true);
    }

    public static void main(String[] args) {
        Cliente cliente = new Cliente();
        try {
            sfd = new Socket("localhost", 7000);
            EntradaSocket = new DataInputStream(
                    new BufferedInputStream(sfd.getInputStream()));
            SalidaSocket = new DataOutputStream(
                    new BufferedOutputStream(sfd.getOutputStream()));

            SalidaSocket.writeUTF("Dyan");
            SalidaSocket.flush();
        } catch (UnknownHostException uhe) {
            System.out.println("No se puede acceder al servidor.");
            System.exit(1);
        } catch (IOException ioe) {
            System.out.println("Comunicación rechazada.");
            System.exit(1);
        }
        while (true) {
            try {
                String linea = EntradaSocket.readUTF();
                entrada.append(linea + "\n");
            } catch (IOException ioe) {
                System.exit(1);
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        texto = salida.getText();
        salida.setText("");
        try {
            SalidaSocket.writeUTF(texto);
            SalidaSocket.flush();
        } catch (IOException ioe) {
            System.out.println("Error: " + ioe);
        }
    }

    public void enviarArchivo(String rutaArchivo) {
        try {
            File archivo = new File(rutaArchivo);
            FileInputStream fileInputStream = new FileInputStream(archivo);
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                SalidaSocket.write(buffer, 0, bytesRead);
                SalidaSocket.flush();
            }

            fileInputStream.close();
            System.out.println("Archivo enviado correctamente.");
        } catch (IOException e) {
            System.out.println("Error al enviar el archivo: " + e);
        }
    }

    public boolean handleEvent(Event e) {
        if ((e.target == this) && (e.id == Event.WINDOW_DESTROY)) {
            if (sfd != null) {
                try {
                    sfd.close();
                } catch (IOException ioe) {
                    System.out.println("Error: " + ioe);
                }
                this.dispose();
            }
        }
        return true;
    }

}
