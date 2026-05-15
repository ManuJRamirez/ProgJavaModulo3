package learning.tokioschool.practicaSieteClienteServidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The Class ClienteManager.
 * 
 * Esta clase hará la función de gestor individual para cada conexión de cada
 * uno de los clientes. Es el responsable de que cada cliente se le atienda de
 * forma paralela, sin interferir ni bloquear los demás clientes.
 * 
 *
 */
public class ClienteManager implements Runnable {

	/**
	 * CLAVE SECRETA que será necesaria para confirmar la solicitud de desconexión
	 * del servidor.
	 */
	private final String CONTRASENIA = "123AbC";

	/**
	 * Contador autoincremental de hilos. Esto hará que cada cliente tenga un ID
	 * diferente.
	 */
	private static final AtomicInteger CONTADORCLIENTES = new AtomicInteger(0);

	/** Socket con el que nos comunicaremos con el cliente. */
	private Socket socketCliente;

	/** Número id que identifica al cliente. */
	private int idCliente;

	/** Formateador de fecha usado para el log en consola. */
	private static final DateTimeFormatter formato_Log = DateTimeFormatter.ofPattern("dd/MM/yyyy => HH:mm");

	/**
	 * Constructor de clase.
	 * 
	 * Inicializa una nueva instancia del gestor del clientes. Registra la sesión en
	 * la lista del servidor de los clientes conectados y asigna un unico id a dicho
	 * cliente.
	 *
	 * @param socket the socket
	 */
	public ClienteManager(Socket socket) {
		socketCliente = socket;
		idCliente = CONTADORCLIENTES.incrementAndGet();
		Servidor.clientesConectados.add(this);

		System.out.println(
				"[" + obtenerHoraLog() + "] - Nuevo intento de conexión entrante de cliente con id " + idCliente + ".");

	}

	/**
	 * Run.
	 * 
	 * Metodo run (de Runnable) en el que inicializamos los flujos de entrada y
	 * salida de datos. También llamamos al bucle encargado de gestinar la
	 * comunicacion con el cliente. Además garantizamos un cierre de recursos usando
	 * un try con recursos.
	 * 
	 */
	@Override
	public void run() {

		try (Socket socket = socketCliente;
				ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream())) {

			salida.flush();

			System.out.println("[" + obtenerHoraLog() + "] - Cliente con id " + idCliente + " conectado al servidor.");

			bucleDeComunicacion(salida, entrada, socket);

		} catch (IOException e) {
			System.out.println("[" + obtenerHoraLog() + "] - Error en la conexion con el cliente " + idCliente + ".");
		} finally {
			System.out.println("[" + obtenerHoraLog() + "] - Finalizando hilo del cliente " + idCliente + ".");
		}

	}

	/**
	 * Bucle de comunicacion.
	 * 
	 * Mantiene la escucha activa de mensajes desde el cliente. Además procesa y
	 * gestiona los comandos normales, editando el texto y enviando la respuesta y
	 * los comandos especiales como el de "salir" o "apagar".
	 *
	 * @param salidaMensaje  canal de salida para el envio de mensajes.
	 * @param llegadaMensaje canal de llegada para la recepcion de mensajes.
	 * @param socket         socket/puerto por el que entran y salen los mensajes.
	 */
	public void bucleDeComunicacion(ObjectOutputStream salidaMensaje, ObjectInputStream llegadaMensaje, Socket socket) {
		boolean seguirBucle = true;

		while (seguirBucle) {
			Mensaje mensajeRecibido = null;

			try {
				mensajeRecibido = recibirMensaje(llegadaMensaje);
			} catch (IOException e) {
				System.out.println(
						"[" + obtenerHoraLog() + "] - Error al recibir mensaje del Cliente " + idCliente + ".");
				break;
			}

			if (mensajeRecibido == null) {
				System.out.println("[" + obtenerHoraLog() + "] - Error con el cliente " + idCliente + ".");
				seguirBucle = false;
				break;
			}

			String textoRecibido = mensajeRecibido.getTexto();

			if (textoRecibido.equals("salir")) {
				System.out.println("[" + obtenerHoraLog() + "] - Cliente " + idCliente + " desconectado del servidor.");
				seguirBucle = false;

			} else if (textoRecibido.equals("apagar")) {
				enviarMensaje(salidaMensaje, new Mensaje(
						"Introduce la contraseña para confirmar: (Para cancelar \"apagado\" inserte una contraseña incorrecta.)"));

				try {
					Mensaje contraseniaRecibida = recibirMensaje(llegadaMensaje);

					if (contraseniaRecibida != null && contraseniaRecibida.getTexto().equals(CONTRASENIA)) {
						enviarMensaje(salidaMensaje, new Mensaje("Servidor apagandose..."));
						try {
							Thread.sleep(150);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Servidor.apagarServidor(idCliente);
						System.out.println("[" + obtenerHoraLog() + "] - Deteniendo servidor solicitado por el cliente "
								+ idCliente + "...");
						seguirBucle = false;
					} else {
						enviarMensaje(salidaMensaje, new Mensaje("Contraseña incorrecta."));
					}

				} catch (IOException e) {
					System.out.println(
							"[" + obtenerHoraLog() + "] - Error al verificar contraseña del cliente " + idCliente);
					break;
				}

			} else {
				Mensaje mensajeEditado = editarMensaje(mensajeRecibido);
				enviarMensaje(salidaMensaje, mensajeEditado);
			}
		}
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
	public Mensaje recibirMensaje(ObjectInputStream llegadaMensaje) throws IOException {

		try {
			Object objetoEntrada = llegadaMensaje.readObject();
			if (objetoEntrada instanceof Mensaje) {
				Mensaje mensajeEntrada = (Mensaje) objetoEntrada;
				if (mensajeEntrada != null && mensajeEntrada.getTexto() != null) {
					System.out.println("[" + obtenerHoraLog() + "] - Mensaje recibido del cliente " + idCliente + ": "
							+ mensajeEntrada.getTexto());
				}
				return mensajeEntrada;
			} else {
				throw new IOException("[" + obtenerHoraLog() + "] - Objeto recibido no es del tipo Mensaje");
			}

		} catch (ClassNotFoundException e) {
			throw new IOException("[" + obtenerHoraLog() + "] - Error al recibir el mensaje. ", e);
		}
	}

	/**
	 * Editar mensaje.
	 * 
	 * Método que realiza la operación básica solicitada en la práctica. Se le pasa
	 * un Mensaje como parámetro, lo convierte en mayúsculas y lo devuelve.
	 *
	 * @param mensaje Se le pasa un mensaje.
	 * @return Devuelve el mensaje pasado por parámetros, en mayúsculas.
	 */
	public Mensaje editarMensaje(Mensaje mensaje) {
		if (mensaje == null || mensaje.getTexto() == null) {
			return new Mensaje("");
		}

		String textoEditado = mensaje.getTexto().toUpperCase();
		return new Mensaje(textoEditado);
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
	public boolean enviarMensaje(ObjectOutputStream salidaMensaje, Mensaje mensaje) {

		if (salidaMensaje == null) {
			System.out.println(
					"[" + obtenerHoraLog() + "] - Stream de salida no disponible para el cliente " + idCliente);
			return false;
		}

		try {
			salidaMensaje.writeObject(mensaje);
			salidaMensaje.flush();
			System.out.println(
					"[" + obtenerHoraLog() + "] - Mensaje enviado al cliente " + idCliente + ": " + mensaje.getTexto());
			return true;
		} catch (IOException e) {
			System.out.println("[" + obtenerHoraLog() + "] - Error al enviar mensaje al cliente " + idCliente);
			return false;
		}
	}

	/**
	 * Método con el que provocamos el cierre inmediato del socket asociado al cliente actual. Lo usa el Servidor cuando el comando "apagar" se haya activado.
	 */
	public void cerrar() {
		try {
			socketCliente.close();
		} catch (IOException e) {
		}
	}

	/**
	 * Método para obtener la hora, dar el formato definido en el atributo estático "formato_Log".
	 *
	 * @return El String con la hora formateada.
	 */
	public static String obtenerHoraLog() {
		return LocalDateTime.now().format(formato_Log);
	}

}
