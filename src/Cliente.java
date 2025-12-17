import java.net.*;
import java.io.*;

public class Cliente {

    public static void main(String[] args) throws IOException {

        // creación del socket UDP del cliente
        DatagramSocket socket = new DatagramSocket();

        BufferedReader numero_teclado = new BufferedReader(
                new InputStreamReader(System.in));

        System.out.print("Introduce un número entero positivo: ");
        String numero = numero_teclado.readLine();

        // se envía el número al servidor
        byte[] datos = numero.getBytes();
        InetAddress servidor = InetAddress.getByName("localhost");

        DatagramPacket paquete = new DatagramPacket(
                datos, datos.length, servidor, 6000);

        socket.send(paquete);

        // se recibe la respuesta del servidor, definimos el tamaño del buffer
        byte[] buffer = new byte[4096];
        DatagramPacket respuesta = new DatagramPacket(buffer, buffer.length);
        socket.receive(respuesta);

        String resultado = new String(
                respuesta.getData(), 0, respuesta.getLength());

        System.out.println("Primos: " + resultado);

        socket.close();
    }
}
