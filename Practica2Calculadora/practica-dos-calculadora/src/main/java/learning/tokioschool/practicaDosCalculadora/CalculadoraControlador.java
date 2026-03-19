package learning.tokioschool.practicaDosCalculadora;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Clase CalculadoraControlador.
 * 
 * Aquí vamos a gestionar los eventos, organizar las operaciones(modelo) y
 * actualizar la interfaz gráfica(vista)
 * 
 */

public class CalculadoraControlador {

	/** Atributo final donde guardaremos el modelo de nuestra aplicacion. */
	private final CalculadoraModelo modelo;

	/** Atributo final donde guardaremos la interfaz gráfica de la calculadora. */
	private final CalculadoraVista vista;

	/** Atributo donde guardaremos la configuracion de la ventana del historico. */
	private JDialog ventanaHistorial;

	/**
	 * Atributo donde iremos insertando dinámicamente las operaciones realizadas.
	 */
	private JPanel panelContenidoHistorial;

	/** Barra de desplazamiento del historial. */
	private JScrollPane scrollHistorial;

	/**
	 * Atributo donde vamos a almacenar si hay una operacion pendiente de introducir
	 * el segundo número
	 */
	private boolean hayOperacionPendiente = false;

	/**
	 * Atributo que detecta si ya hemos introducido el segundo número en la
	 * calculadora. Será usado para cambiar el tipo de operación antes de introducir
	 * el segundo número.
	 */
	private boolean escribiendoSegundoNumero = false;

	/**
	 * Atributo que usaremos para saber cuando se ha dado un resultado y al escribir
	 * un número nuevo, no lo concadene con el resultado, sino que lo sustituya.
	 */
	private boolean resultadoMostrado = false;

	/**
	 * Constructor de clase, al que le pasamos un parámetro modelo de tipo
	 * CalculadoraModelo y un parámetro vista de tipo CalculadoraVista.
	 *
	 * @param modelo Modelo de la calculador.
	 * @param vista  Vista o interfaz gráfica de la calculadora.
	 */
	public CalculadoraControlador(CalculadoraModelo modelo, CalculadoraVista vista) {
		this.modelo = modelo;
		this.vista = vista;

		registrarEventos();
	}

	/**
	 * Método en el que vamos a registrar los eventos listeners de botones, teclado
	 * y menus.
	 * 
	 * Gestiona:
	 * <ul>
	 * <li>Entrada de números</li>
	 * <li>Operaciones aritméticas</li>
	 * <li>Punto decimal</li>
	 * <li>Igual (=)</li>
	 * <li>Limpiar, guardar y recuperar memoria</li>
	 * <li>Historial</li>
	 * <li>Teclado físico</li>
	 * </ul>
	 */
	private void registrarEventos() {

		/** Listeners para los números. */

		ActionListener numeros = evento -> {
			JButton boton = (JButton) evento.getSource();
			String actual = vista.displayCalculadora.getText();
			String numero = boton.getText();

			/**
			 * Si tenemos un resultado en el display de la calculadora, sustituimos el
			 * número del display por el nuevo introducido.
			 */

			if (resultadoMostrado) {
				vista.displayCalculadora.setText(numero);
				resultadoMostrado = false;
			}
			/** Si el display está en 0, sustituimos el 0 por el número nuevo */
			else if (actual.equals("0")) {
				vista.displayCalculadora.setText(numero);
			}

			else {
				vista.displayCalculadora.setText(actual + numero);
			}

			escribiendoSegundoNumero = true;
			vista.displayCalculadora.requestFocusInWindow();
		};

		vista.btn0.addActionListener(numeros);
		vista.btn1.addActionListener(numeros);
		vista.btn2.addActionListener(numeros);
		vista.btn3.addActionListener(numeros);
		vista.btn4.addActionListener(numeros);
		vista.btn5.addActionListener(numeros);
		vista.btn6.addActionListener(numeros);
		vista.btn7.addActionListener(numeros);
		vista.btn8.addActionListener(numeros);
		vista.btn9.addActionListener(numeros);

		/** Boton decimal */

		vista.btnPunto.addActionListener(evento -> {
			String actual = vista.displayCalculadora.getText();
			if (!actual.contains(".")) {
				vista.displayCalculadora.setText(actual.equals("0") ? "0." : actual + ".");
			}
			escribiendoSegundoNumero = true;
			vista.displayCalculadora.requestFocusInWindow();
		});

		/** Operaciones */

		vista.btnSumar.addActionListener(evento -> {
			setOperacion("+");
			vista.displayCalculadora.requestFocusInWindow();
		});
		vista.btnRestar.addActionListener(evento -> {
			setOperacion("-");
			vista.displayCalculadora.requestFocusInWindow();
		});
		vista.btnMultiplicar.addActionListener(evento -> {
			setOperacion("*");
			vista.displayCalculadora.requestFocusInWindow();
		});
		vista.btnDividir.addActionListener(evento -> {
			setOperacion("/");
			vista.displayCalculadora.requestFocusInWindow();
		});

		/** Boton resultado (=) */

		vista.btnResultado.addActionListener(evento -> {
			calcular();
			vista.displayCalculadora.requestFocusInWindow();
		});

		/** Botón Limpiar */

		vista.btnLimpiar.addActionListener(evento -> {
			vista.displayCalculadora.setText("0");
			hayOperacionPendiente = false;
			escribiendoSegundoNumero = false;
			vista.displayCalculadora.requestFocusInWindow();
		});

		/** Boton Guardar */

		vista.btnGuardar.addActionListener(evento -> {
			if (!vista.displayCalculadora.getText().isEmpty()) {
				modelo.guardar(Double.parseDouble(vista.displayCalculadora.getText()));
			}
			vista.displayCalculadora.requestFocusInWindow();
		});

		/** Botón Recuperar. */

		vista.btnRecuperar.addActionListener(evento -> {
			vista.displayCalculadora.setText(String.valueOf(modelo.recuperar()));
			vista.displayCalculadora.requestFocusInWindow();
		});

		vista.menuHistorico.addActionListener(evento -> mostrarDesplegableHistorial());

		/** Listener para el teclado. */

		vista.displayCalculadora.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent eventoTeclado) {

				String actual = vista.displayCalculadora.getText();

				/** Borrar/Retroceder */
				if (eventoTeclado.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
					if (actual.length() > 1) {
						vista.displayCalculadora.setText(actual.substring(0, actual.length() - 1));
					} else {
						vista.displayCalculadora.setText("0");
					}
					return;
				}

				/** Boton SUPR = Limpiar. */

				if (eventoTeclado.getKeyCode() == KeyEvent.VK_DELETE) {
					vista.displayCalculadora.setText("0");
					hayOperacionPendiente = false;
					escribiendoSegundoNumero = false;
					resultadoMostrado = false;
					return;
				}

				/** Numeros */

				char caracter = eventoTeclado.getKeyChar();
				if (Character.isDigit(caracter)) {

					if (resultadoMostrado) {
						vista.displayCalculadora.setText(String.valueOf(caracter));
						resultadoMostrado = false;
					} else if (actual.equals("0")) {
						vista.displayCalculadora.setText(String.valueOf(caracter));
					} else {
						vista.displayCalculadora.setText(actual + caracter);
					}

					escribiendoSegundoNumero = true;
					return;
				}

				/** Punto decimal */

				if (caracter == '.' && !actual.contains(".")) {
					vista.displayCalculadora.setText(actual.equals("0") ? "0." : actual + ".");
					escribiendoSegundoNumero = true;
					return;
				}

				/** Tipo operaciones */

				if ("+-*/".indexOf(caracter) >= 0) {
					setOperacion(String.valueOf(caracter));
					return;
				}

				/** Key intro para resultado. */

				if (eventoTeclado.getKeyCode() == KeyEvent.VK_ENTER) {
					calcular();
				}
			}
		});
	}

	/**
	 * Gestión una operacion matemática pasada por parámetros.
	 * 
	 * Nos encontraremos con 3 casos. El caso en el no haya ninguna operación
	 * pendiente por lo que se guarda la operacion. El caso en el que haya una
	 * operación pendiente y aún no se haya introducido el segundo número. En este
	 * caso se podría cambiar el tipo de operación. Y el caso en el que ya se haya
	 * escrito el segundo número. En este último caso se prodece a calcular el
	 * resultado que si se pulsa un nuevo boton de operacion (+ - * /) el resultado
	 * pasaría a ser el primer valor de la siguiente operacion.
	 *
	 * @param operacion tipo de operación (+ - * / ).
	 */
	private void setOperacion(String operacion) {
		String actual = vista.displayCalculadora.getText();
		double valorActual = Double.parseDouble(actual);

		if (!hayOperacionPendiente) {
			modelo.setOperacion(operacion, Double.parseDouble(actual));
			vista.displayCalculadora.setText("0");
			hayOperacionPendiente = true;
			escribiendoSegundoNumero = false;
			return;
		}

		if (hayOperacionPendiente && !escribiendoSegundoNumero) {
			modelo.cambiarOperacion(operacion);
			return;
		}

		double resultado = modelo.calcular(valorActual);
		vista.displayCalculadora.setText(String.valueOf(resultado));
		modelo.setOperacion(operacion, resultado);
		resultadoMostrado = true;
		escribiendoSegundoNumero = false;
	}

	/**
	 * Realiza el calculo usando el método calcular() de la clase CalculadoraModelo
	 * y además actualiza el display y el historial de operaciones.
	 */
	private void calcular() {
		if (!vista.displayCalculadora.getText().isEmpty()) {
			try {
				double valor = Double.parseDouble(vista.displayCalculadora.getText());
				double resultado = modelo.calcular(valor);
				vista.displayCalculadora.setText(String.valueOf(resultado));
				resultadoMostrado = true;
				hayOperacionPendiente = false;
				escribiendoSegundoNumero = false;

				if (ventanaHistorial != null && ventanaHistorial.isVisible()) {
					actualizarHistorial();
				}

			} catch (ArithmeticException error) {
				vista.displayCalculadora.setText("Error: No se puede dividir entre 0");
			}
		}
	}

	/**
	 * Método que muesstra el desplegable historial. Además añade los botones de
	 * borrar historial y cerrar ventana.
	 */
	private void mostrarDesplegableHistorial() {

		if (ventanaHistorial == null) {

			ventanaHistorial = new JDialog(vista, "Historial de operaciones", false);
			ventanaHistorial.setSize(350, 300);
			ventanaHistorial.setLayout(new BorderLayout());

			ventanaHistorial.setLocation(vista.getX() + vista.getWidth(), vista.getY());

			panelContenidoHistorial = new JPanel();
			panelContenidoHistorial.setLayout(new BoxLayout(panelContenidoHistorial, BoxLayout.Y_AXIS));

			scrollHistorial = new JScrollPane(panelContenidoHistorial);
			scrollHistorial.setPreferredSize(new Dimension(300, 200));

			JButton btnBorrar = new JButton("Borrar historial");
			btnBorrar.addActionListener(e -> {
				modelo.getHistorico().clear();
				actualizarHistorial();
			});

			JButton btnCerrar = new JButton("Cerrar");
			btnCerrar.addActionListener(e -> ventanaHistorial.setVisible(false));

			JPanel panelBotones = new JPanel();
			panelBotones.add(btnBorrar);
			panelBotones.add(btnCerrar);

			ventanaHistorial.add(scrollHistorial, BorderLayout.CENTER);
			ventanaHistorial.add(panelBotones, BorderLayout.SOUTH);
		}

		actualizarHistorial();
		ventanaHistorial.setVisible(true);
	}

	/**
	 * Método que se encarga de actualizar el historial.
	 */
	private void actualizarHistorial() {

		panelContenidoHistorial.removeAll();

		if (modelo.getHistorico().isEmpty()) {
			JLabel lbl = new JLabel("Sin operaciones aún");
			lbl.setEnabled(false);
			panelContenidoHistorial.add(lbl);
		} else {
			for (String operacion : modelo.getHistorico()) {
				JLabel lbl = new JLabel("<html><span style='font-size:10px;'>" + operacion + "</span></html>");
				panelContenidoHistorial.add(lbl);
			}
		}

		panelContenidoHistorial.revalidate();
		panelContenidoHistorial.repaint();
	}
}
