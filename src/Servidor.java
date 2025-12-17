import java.net.*;
import java.io.*;
import java.util.*;

public class Servidor {

    public static void main(String[] args) throws IOException {

        // se crea el socket UDP en el puerto 6000
        DatagramSocket socket = new DatagramSocket(6000);
        System.out.println("Servidor UDP listo en puerto 6000");

        // el servidor permanece en escucha constante
        while (true) {

            // buffer para recibir datos
            byte[] buffer = new byte[1024];
            DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);

            // recibe un respuesta del cliente
            socket.receive(paquete);

            // se crea un hilo
            new Thread(() -> procesarCliente(socket, paquete)).start();
        }
    }

    private static void procesarCliente(DatagramSocket socket, DatagramPacket paquete) {
        try {
            // se obtiene el mensaje recibido
            String numero_introducido_string = new String(
                    paquete.getData(), 0, paquete.getLength()).trim();

            int numero_a_buscar;
            try {
                numero_a_buscar = Integer.parseInt(numero_introducido_string);
                if (numero_a_buscar <= 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                enviarRespuesta(socket, paquete, "Número incorrecto");
                return;
            }

            // cálculo de números primos
            String resultado = calcularPrimos(numero_a_buscar);

            // envío de la respuesta al cliente
            enviarRespuesta(socket, paquete, resultado);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // enviamos la respuesta del cliente
    private static void enviarRespuesta(
            DatagramSocket socket, DatagramPacket paquete, String respuesta)
            throws IOException {

        byte[] datos = respuesta.getBytes();

        DatagramPacket respuestaPaquete = new DatagramPacket(
                datos,
                datos.length,
                paquete.getAddress(),
                paquete.getPort()
        );

        socket.send(respuestaPaquete);
    }

    // calculamos los primos
    private static String calcularPrimos(int numero_recibido) {
        StringBuilder sb = new StringBuilder();

        for (int i = 2; i <= numero_recibido; i++) {
            if (esPrimo(i)) {
                if (sb.length() > 0) sb.append(",");
                sb.append(i);
            }
        }
        return sb.toString();
    }

    // comprobación de número primo
    private static boolean esPrimo(int num) {
        if (num < 2) return false;
        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0) return false;
        }
        return true;
    }
}
//servidor