/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ejemploproyecto;

import static ejemploproyecto.Cliente.EntradaSocket;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

/**
 *
 * @author Personal
 */
public class Servidor extends Thread {

    public static Vector usuarios = new Vector();

    public static void main(String args[]) {
        ServerSocket sfd = null;
        try {
            sfd = new ServerSocket(7000);

        } catch (IOException ioe) {
            System.out.println("Comunicación rechazada." + ioe);
            System.exit(1);
        }
        while (true) {
            try {
                Socket nsfd = sfd.accept();
                EntradaSocket = new DataInputStream(
                        new BufferedInputStream(nsfd.getInputStream()));
                String linea = EntradaSocket.readUTF();
                System.out.println("Conexion aceptada de: " + linea);
                Flujo flujo = new Flujo(nsfd, linea);
                Thread t = new Thread(flujo);
                t.start();
                menu();
            } catch (IOException ioe) {
                System.out.println("Error: " + ioe);
            }
        }
    }

    private static void menu() {

        System.out.println("Menu del Servidor del Chat");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("1. Mostrar Usuarios");
        try {
            int opc = Integer.parseInt(br.readLine());

            switch (opc) {

                case 1: {

                    if (!usuarios.isEmpty()) {
                        for (int i = 0; i < usuarios.size(); i++) {

                            Flujo f = (Flujo) usuarios.get(i);
                            System.out.println(f.user);
                        }

                    } else {
                        System.out.println("No hay usuarios");

                    }

                }

                case 2: {

                    for (int i = 0; i < usuarios.size(); i++) {

                        Flujo f = (Flujo) usuarios.get(i);
                        System.out.println(f.user);
                    }

                    System.out.println("¿ Cual quiere eliminar");
                }

            }

        } catch (IOException ex) {

        }

    }
}
