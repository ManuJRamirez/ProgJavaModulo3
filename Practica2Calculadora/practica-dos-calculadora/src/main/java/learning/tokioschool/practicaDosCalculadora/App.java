package learning.tokioschool.practicaDosCalculadora;

import javax.swing.SwingUtilities;

/**
 * Clase App. Clase principal.
 * 
 * FUNCIONALIDADES DE LA CALCULADORA:
 * <ul>
 * <li>Operaciones: suma, resta, multiplicación y división.</li>
 * <li>Entrada mediante botones y teclado.</li>
 * <li>Soporte para punto decimal(teclado).</li>
 * <li>Encadenamiento de operaciones sin necesidad de pulsar "=".</li>
 * <li>Memoria interna: guardar y recuperar valores.</li>
 * <li>Botón de limpieza y tecla SUPR para reiniciar el display.</li>
 * <li>Historial de operaciones con ventana a la derecha y botones para
 * reiniciar historial o cerrar ventana.</li>
 * <li>Prevención de error en la división entre cero.</li>
 * <li>Acciones sobre el display (sustituir resultado, evitar ceros iniciales,
 * etc.).</li>
 * </ul>
 */
public class App {

	/**
	 * Utilizamos "SwingUtilities.invokerLater() para la creacion de la interfaz
	 * gráfica de Swing. Dentro de este bloque inicializamos los componentes del
	 * patron Modelo-Vista-Controlador.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		System.out.println("Arrancando calculadora...");

		SwingUtilities.invokeLater(() -> {
			CalculadoraModelo modelo = new CalculadoraModelo();
			CalculadoraVista vista = new CalculadoraVista();
			@SuppressWarnings("unused")
			CalculadoraControlador controlador = new CalculadoraControlador(modelo, vista);
		});

	}
}
