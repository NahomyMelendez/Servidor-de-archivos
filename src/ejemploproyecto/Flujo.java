/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ejemploproyecto;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Enumeration;

/**
 *
 * @author Personal
 */
import java.net.*;
import java.io.*;
import java.util.*;

public class Flujo extends Thread {

    Socket nsfd;
    DataInputStream FlujoLectura;
    DataOutputStream FlujoEscritura;
    String user = "";

    public Flujo(Socket sfd, String user) {
        nsfd = sfd;
        this.user = user;
        try {
            FlujoLectura = new DataInputStream(
                    new BufferedInputStream(sfd.getInputStream()));
            FlujoEscritura = new DataOutputStream(
                    new BufferedOutputStream(sfd.getOutputStream()));
        } catch (IOException ioe) {
            System.out.println("IOException(Flujo): " + ioe);
        }
    }

    public void run() {
        broadcast(user + "> se ha conectado");
        Servidor.usuarios.add((Object) this);

        while (true) {
            try {

                int longitudArchivo = FlujoLectura.readInt();

                byte[] archivoBytes = new byte[longitudArchivo];
                FlujoLectura.readFully(archivoBytes);

                String textoArchivo = new String(archivoBytes);

                if (!textoArchivo.isEmpty()) {
                    textoArchivo = user + "> Archivo recibido:\n"+ textoArchivo;
                    broadcast(textoArchivo);
                }
            } catch (IOException ioe) {
                Servidor.usuarios.removeElement(this);
                broadcast(user + "> se ha desconectado");
                break;
            }
        }
    }

    public void broadcast(String mensaje) {
        synchronized (Servidor.usuarios) {
            Enumeration e = Servidor.usuarios.elements();
            while (e.hasMoreElements()) {
                Flujo f = (Flujo) e.nextElement();
                try {
                    synchronized (f.FlujoEscritura) {
                        f.FlujoEscritura.writeUTF(mensaje);
                        f.FlujoEscritura.flush();
                    }
                } catch (IOException ioe) {
                    System.out.println("Error: " + ioe);
                }
            }
        }
    }

    public byte[] leerBloque(int longitud) {
        byte[] bloque = new byte[longitud];
        try {
            FlujoLectura.readFully(bloque);
        } catch (IOException ioe) {
            System.out.println("Error al leer el bloque: " + ioe);
        }
        return bloque;
    }
}
