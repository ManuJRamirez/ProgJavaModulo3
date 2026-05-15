package learning.tokioschool.practicaSieteClienteServidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**NOTA IMPORTANTE: FUNCIONES DE LA APLICACION:
 * <ul>
 * <li>RECEPCION DE MENSAJE Y RESPUESTA DEL MISMO CON EL TEXTO EN MAYUSCULAS</li>
 * <li>DESCONEXION INDIVIDUAL DE CADA CLIENTE USANDO EL COMANDO "salir".</li>
 * <li>ESCUCHA ACTIVA POR PARTE DEL SERVIDOR PARA NUEVAS CONEXIONES EN EL PUERTO 4444.</li>
 * <li>ASIGNACION AUTOMATICA Y EXCLUSIVA DE ID A CADA CLIENTE.</li>
 * <li>CREACION DE HILOS DE TRABAJO PARALELOS PARA SOPORTE DE MULTITUD DE CLIENTES.</li>
 * <li>DESCONEXION DEL SERVIDOR, TRAS SOLICITUD DE UN CLIENTE Y VERIFICACIÓN DE LA CONTRASEÑA USANDO EL COMANDO "apagar".</li>
 * <li>DESCONEXION AUTOMATICA DE TODOS LOS CLIENTES CUANDO EL SERVIDOR SE APAGA.</li>
 * <li>LOG EN CONSOLA CON FECHA Y HORA DE EVENTOS.</li>
 * </ul>
 * 
 * 
 * The Class Servidor.
 * 
 * Clase principal del servidor. Se encarga de gestionar el ciclo de vida de
 * todo el servicio. Escucha las nuevas conexiones de clientes y coordina el
 * apagado del servidor y la desconexion forzada de los clientes conectados.
 */
public class Servidor {

	/**
	 * Atributo con el que vamos a controlar cuando parar las escuchas nuevos
	 * clientes del servidor.
	 */
	static boolean servidorActivo = true;

	/**
	 * Atributo donde guardamos el socket del servidor a donde deben conectar los
	 * clientes.
	 */
	static ServerSocket serverSocket;

	/** Lista sincronizada de los clientes conectados al servidor. */
	static List<ClienteManager> clientesConectados = java.util.Collections.synchronizedList(new ArrayList<>());

	/**
	 * Metodo encargado de hacer un apagado del servidor. Además interrumpe todas la
	 * conexiones con los clientes conectados (registrados en la lista de
	 * clientesConectados). Ademas, deja constancia de qué cliente es el que ha
	 * creado la solicitud de desconexion del servidor.
	 *
	 * @param idCliente Se le pasa el id del cliente que solicita el apagado.
	 */
	static public void apagarServidor(int idCliente) {

		try {
			if (serverSocket != null && !serverSocket.isClosed()) {
				serverSocket.close();
				servidorActivo = false;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		synchronized (clientesConectados) {
			for (ClienteManager cliente : clientesConectados) {
				cliente.cerrar();

			}
		}
		System.out.println(
				"[" + ClienteManager.obtenerHoraLog() + "] - Servidor detenido por el cliente " + idCliente + ".");
	}

	/**
	 * Método principal.
	 * 
	 * Se encarga de arrancar el servidor, abrir el socket 4444 para escuchar a los
	 * clientes. Ademas se encarga de aceptar y crear un nuevo hilo a cada uno de
	 * los clientes que se van conectando a su socket.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		Socket socketCliente = null;

		System.out.println("[" + ClienteManager.obtenerHoraLog() + "] - Arrancando servidor...");

		try {
			serverSocket = new ServerSocket(4444);
			System.out.println("[" + ClienteManager.obtenerHoraLog() + "] - Servidor arrancado.");
			while (servidorActivo) {
				try {
					socketCliente = serverSocket.accept();
					ClienteManager nuevoCliente = new ClienteManager(socketCliente);
					Thread hiloNuevoCliente = new Thread(nuevoCliente);
					hiloNuevoCliente.start();
				} catch (SocketException e) {
					if (!servidorActivo) {
						break;
					} else {
						e.printStackTrace();
					}
				}
			}

			System.out.println("[" + ClienteManager.obtenerHoraLog() + "] - Servidor apagado correctamente.");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
