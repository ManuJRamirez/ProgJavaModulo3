package learning.tokioschool.practicaSieteClienteServidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The Class Cliente.
 * 
 * Clase principal del Cliente. Gestiona la interfaz de usuario via consola.
 * Dispone de un bucle principal y de un hilo que evalua el estado del servidor
 * y se entera de forma inmedita si hubiera una desconexión del servidor. Ademas
 * hay una cola de mensajes que sincroniza el bucle principal con el hilo de
 * estado del servidor.
 */
public class Cliente {

	/** The ip servidor. */
	private final String IP_SERVIDOR = "localhost";

	/** The puerto. */
	private final int PUERTO = 4444;

	/** The cola mensajes. */
	private final BlockingQueue<Mensaje> colaMensajes = new LinkedBlockingQueue<>();

	/** Formateador de fecha usado para el log en consola. */
	private static final DateTimeFormatter formato_Log = DateTimeFormatter.ofPattern("dd/MM/yyyy => HH:mm");

	/**
	 * Método main.
	 * 
	 * Crea un nuevo cliente y llama al método iniciar.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {

		Cliente cliente = new Cliente();
		cliente.iniciar();
	}

	/**
	 * Método que inicializa la estructura de la red del cliente. Crea los Stream de
	 * flujo de mensajes, a traves del try con recursos, arranca el hilo que escucha
	 * el estado del servidor y gestiona el bucle principal que controla el menú
	 * interactivo de la consola para el envío de comandos al servidor.
	 */
	public void iniciar() {

		System.out.println("[" + obtenerHoraLog() + "] - Conectando con el servidor....");
		try (Socket socketCliente = new Socket(IP_SERVIDOR, PUERTO);
				ObjectOutputStream salidaMensaje = new ObjectOutputStream(socketCliente.getOutputStream());
				ObjectInputStream llegadaMensaje = new ObjectInputStream(socketCliente.getInputStream());
				Scanner escanner = new Scanner(System.in)) {

			salidaMensaje.flush();
			System.out.println("[" + obtenerHoraLog() + "] - Conectado.");

			Thread hiloEstadoServidor = new Thread(() -> {
				boolean estado = true;
				try {
					while (estado) {
						Mensaje mensaje = recibirMensaje(llegadaMensaje);

						if (mensaje == null) {
							System.out.println("\n[" + obtenerHoraLog()
									+ "] - [Alerta] El servidor se ha desconectado de forma remota.");
							System.exit(0);
							return;
						}
						colaMensajes.put(mensaje);
					}
				} catch (Exception e) {
					System.out.println("[" + obtenerHoraLog() + "] - [Alerta] Conexión perdida con el servidor.");
					System.exit(0);
				}
			});

			hiloEstadoServidor.setDaemon(true);
			hiloEstadoServidor.start();

			boolean seguirBucle = true;
			String ultimoComando = null;

			while (seguirBucle) {

				if (colaMensajes.peek() == null && !colaMensajes.isEmpty()) {
					System.out.println(
							"[" + obtenerHoraLog() + "]\n - [Alerta] El servidor cerró la conexión inesperadamente.");
					break;
				}

				System.out.println("==============================================================================");
				System.out.println("");
				System.out.println("[" + obtenerHoraLog()
						+ "] - Introduzca el texto que desea enviar al servidor.\nNOTA: Introduzca \"salir\" para cerrar la conexion o \"apagar\" para apagar el servidor si dispones de la contraseña (No añada espacios delante o detrás).");

				String textoParaEnviar;
				try {
					textoParaEnviar = escanner.nextLine();
					ultimoComando = textoParaEnviar;
				} catch (NoSuchElementException e) {
					System.out.println("[" + obtenerHoraLog() + "] - Entrada. Finalianzo cliente.");
					break;
				}

				if (colaMensajes.peek() == null && !colaMensajes.isEmpty()) {
					System.out.println("[" + obtenerHoraLog()
							+ "]\n - [Alerta] Conexión perdida mientras escribía. Abortando envío.");
					break;
				}

				Mensaje mensajeParaEnviar = new Mensaje(textoParaEnviar);
				if (!enviarMensaje(salidaMensaje, mensajeParaEnviar)) {
					System.out.println("[" + obtenerHoraLog() + "] - No se puedo enviar el mensaje.");
				}

				if (textoParaEnviar.equals("salir")) {
					System.out.println("[" + obtenerHoraLog() + "] - Desconectado del servidor.");
					seguirBucle = false;
					continue;
				}

				if ("apagar".equals(ultimoComando)) {
					ultimoComando = null;

					Mensaje respuestaApagar = colaMensajes.take();

					if (respuestaApagar == null) {
						System.out.println("[" + obtenerHoraLog() + "] - Servidor desconectado.");
						break;
					}

					System.out.println("[" + obtenerHoraLog() + "] - Mensaje recibido del servidor: "
							+ respuestaApagar.getTexto());

					System.out.println("[" + obtenerHoraLog() + "] - Contraseña: ");
					String contrasenia = "";

					try {
						contrasenia = escanner.nextLine();
					} catch (NoSuchElementException e) {
						System.out.println("[" + obtenerHoraLog() + "] - Entrada teclado cerrada.");
					}

					enviarMensaje(salidaMensaje, new Mensaje(contrasenia));

					Mensaje respuestaFinal = colaMensajes.take();

					if (respuestaFinal == null) {
						System.out.println("[" + obtenerHoraLog() + "] - Servidor desconectado.");
						break;
					}

					System.out.println(
							"[" + obtenerHoraLog() + "] - Mensaje recibido del servidor: " + respuestaFinal.getTexto());

					if (respuestaFinal.getTexto().toLowerCase().contains("apagandose")) {
						System.out.println("[" + obtenerHoraLog() + "] - Servidor desconectado.");
						break;
					}

					continue;
				}

				Mensaje respuestaNormal = colaMensajes.take();
				if (respuestaNormal == null) {
					System.out.println("[" + obtenerHoraLog() + "] - Servidor desconectado.");
					break;
				}
				System.out.println(
						"[" + obtenerHoraLog() + "] - Mensaje recibido del servidor: " + respuestaNormal.getTexto());

			}
		} catch (IOException e) {
			System.out.println("[" + obtenerHoraLog() + "] - No se pudo establecer la conexión con el servidor.");
		} catch (InterruptedException ex) {
			System.out.println("[" + obtenerHoraLog() + "] - Error interno.");
		}

		System.out.println("[" + obtenerHoraLog() + "] - Cliente finalizado.");
	}

	/**
	 * Captura y valida los datos de entrada. Se asegura que el objeto recibido
	 * corresponda a un Mensaje .
	 *
	 * @param llegadaMensaje Se le pasa como parámetro el Stream de entrada de
	 *                       objetos de donde va a leer el mensaje.
	 * @return Devuelve el mensaje.
	 * @throws IOException Salta una IOException si el canal de comunicación se
	 *                     cierra de forma inesperada..
	 */
	public Mensaje recibirMensaje(ObjectInputStream entrada) throws IOException {

		if (entrada == null) {
			throw new IOException("[" + obtenerHoraLog() + "] - Stream de llegada vacio.");
		}

		try {
			Object objetoEntrada = entrada.readObject();
			if (objetoEntrada instanceof Mensaje) {
				Mensaje mensajeRecibido = (Mensaje) objetoEntrada;

				return mensajeRecibido;

			} else {
				throw new IOException("[" + obtenerHoraLog() + "] - Objeto recibido no es del mismo tipo Mensaje");
			}

		} catch (ClassNotFoundException e) {
			throw new IOException("[" + obtenerHoraLog() + "] - Error al recibir el mensaje del servidor:", e);
		}
	}

	/**
	 * Enviar mensaje.
	 * 
	 * Método que se encarga de enviar un mensaje, es decir, dar una respuesta al mensaje recibido por parte del cliente. Realza un vaciado del buffer (flush).
	 *
	 * @param salidaMensaje Stream que usaremos como canal de salida para enviar el mensaje al cliente.
	 * @param mensaje       Mensaje que queremos mandar.
	 * @return true, Devuelve true si consigue enviar el mensaje sin problemas.
	 */
	public boolean enviarMensaje(ObjectOutputStream salida, Mensaje mensaje) {

		if (salida == null) {
			System.out.println("[" + obtenerHoraLog() + "] - Problema con el stream de salida.");
			return false;
		}

		try {
			salida.writeObject(mensaje);
			salida.flush();
			System.out.println("[" + obtenerHoraLog() + "] - Mensaje enviado al servidor: " + mensaje.getTexto());

			return true;

		} catch (IOException e) {
			System.out
					.println("[" + obtenerHoraLog() + "] - Error al enviar el mensaje al servidor: " + e.getMessage());
			return false;
		}
	}

	/**
	 * Método para obtener la hora, dar el formato definido en el atributo estático
	 * "formato_Log".
	 *
	 * @return El String con la hora formateada.
	 */
	public static String obtenerHoraLog() {
		return LocalDateTime.now().format(formato_Log);
	}

}
