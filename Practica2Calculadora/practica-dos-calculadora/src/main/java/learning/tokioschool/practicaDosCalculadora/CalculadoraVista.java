package learning.tokioschool.practicaDosCalculadora;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;



/**
 * Clase CalculadoraVista que extiende JFrame.
 * 
 * En esta clase definimos la interfaz gráfica usando Swing. Unicamente definimos y organizamos los componentes de forma visual.
 */
@SuppressWarnings("serial")
public class CalculadoraVista extends JFrame {


	/** Atributo en el que se guarda el campo de texto que muestra los números y resultados de la calculadora. El display. */
	public JTextField displayCalculadora;
	
	/** Atributo en el que vamos a guardar el menú histórico de las operaciones. */
	public JMenuItem menuHistorico;

	
	/** Botón 0. */
	public JButton btn0;
	
	/**  Botón 1. */
	public JButton btn1;
	
	/** Botón 2. */
	public JButton btn2;
	
	/** Botón 3. */
	public JButton btn3;
	
	/** Botónn 4. */
	public JButton btn4;
	
	/** Botón 5. */
	public JButton btn5;
	
	/** Botón 6. */
	public JButton btn6;
	
	/** Botón 7. */
	public JButton btn7;
	
	/** Botón 8. */
	public JButton btn8;
	
	/** Botón 9. */
	public JButton btn9;

	/** Botón punto. */
	public JButton btnPunto;
	
	
	/** Botón sumar. */
	public JButton btnSumar;
	
	/** Botón restar. */
	public JButton btnRestar;
	
	/** Botón multiplicar. */
	public JButton btnMultiplicar;
	
	/** Botón dividir. */
	public JButton btnDividir;
	
	/** Boton resultado. */
	public JButton btnResultado;

	/** Boton para limpiar el display. */
	public JButton btnLimpiar;
	
	/** Boton guardar en memoria. */
	public JButton btnGuardar;
	
	/** Boton recuperar dato de memoria. */
	public JButton btnRecuperar;

	/** Menu para mostrar la ventana historial. */
	public JPopupMenu menuHistorial = new JPopupMenu();

	/**
	 * Constructor de clase.
	 * 
	 * En este constructor creamos la ventana principal y todos los componentes graficos.
	 * Organizamos los paneles y su diseño de la interfaz.
	 */
	public CalculadoraVista() {
		
		/**
		 * Creación ventana principal
		 * */
		setTitle("Calculadora Tokio: Práctica 2 -> Manuel J. Ramirez");
		setSize(480, 480);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		
		/**
		 * Menu superior. Tenemos un botón "Vista" y dentro un botón que abre la ventana de historicos.
		 * */
		JMenuBar menuSuperior = new JMenuBar();
		JMenu subMenuVista = new JMenu("Vista");
		menuHistorico = new JMenuItem("Historico");
		subMenuVista.add(menuHistorico);
		menuSuperior.add(subMenuVista);
		setJMenuBar(menuSuperior);
		
		/**
		 * Zona del display de la calculadora.
		 * */
		JPanel zonaDisplayCalculadora = new JPanel(new BorderLayout());
		zonaDisplayCalculadora.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		add(zonaDisplayCalculadora, BorderLayout.CENTER);
		
		
		displayCalculadora = new JTextField("0");
		displayCalculadora.setEditable(false);
		displayCalculadora.setFont(new Font("Arial", Font.BOLD, 26));
		displayCalculadora.setHorizontalAlignment(JTextField.RIGHT);
		displayCalculadora.setBackground(java.awt.Color.WHITE);

		
		zonaDisplayCalculadora.add(displayCalculadora, BorderLayout.NORTH);
		add(zonaDisplayCalculadora, BorderLayout.NORTH);
		
		/**
		 * Zona de los botones númericos y de los botones de las operaciones
		 * */
		JPanel zonaBotonesNumerosYOperaciones = new JPanel(new GridLayout(4, 4, 5, 5));
		zonaBotonesNumerosYOperaciones.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		btn0 = new JButton("0");
		btn1 = new JButton("1");
		btn2 = new JButton("2");
		btn3 = new JButton("3");
		btn4 = new JButton("4");
		btn5 = new JButton("5");
		btn6 = new JButton("6");
		btn7 = new JButton("7");
		btn8 = new JButton("8");
		btn9 = new JButton("9");
		
		btnPunto = new JButton(".");
		btnSumar = new JButton("+");
		btnRestar = new JButton("-");
		btnMultiplicar = new JButton("*");
		btnDividir = new JButton("/");
		
		btnResultado = new JButton("=");
		btnResultado.setBackground(new java.awt.Color(0, 120, 215));
		btnResultado.setForeground(java.awt.Color.WHITE);
		btnResultado.setOpaque(true);
		btnResultado.setBorderPainted(true);
		
		
		zonaBotonesNumerosYOperaciones.add(btn1);
		zonaBotonesNumerosYOperaciones.add(btn2);
		zonaBotonesNumerosYOperaciones.add(btn3);
		zonaBotonesNumerosYOperaciones.add(btnSumar);
		
		zonaBotonesNumerosYOperaciones.add(btn4);
		zonaBotonesNumerosYOperaciones.add(btn5);
		zonaBotonesNumerosYOperaciones.add(btn6);
		zonaBotonesNumerosYOperaciones.add(btnRestar);
		
		zonaBotonesNumerosYOperaciones.add(btn7);
		zonaBotonesNumerosYOperaciones.add(btn8);
		zonaBotonesNumerosYOperaciones.add(btn9);
		zonaBotonesNumerosYOperaciones.add(btnMultiplicar);
		
		zonaBotonesNumerosYOperaciones.add(btn0);
		zonaBotonesNumerosYOperaciones.add(btnPunto);
		zonaBotonesNumerosYOperaciones.add(btnResultado);
		zonaBotonesNumerosYOperaciones.add(btnDividir);
		
		
		/**
		 * Zona de los botones de funciones especiales, como "Limpiar", "Guardar" y "Recuperar"
		 * */
		JPanel zonaBotonesEspeciales = new JPanel(new GridLayout(3, 1, 5, 5));
		zonaBotonesEspeciales.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		btnLimpiar = new JButton("Limpiar");
		btnLimpiar.setBackground(new java.awt.Color(240, 120, 120));
		btnLimpiar.setBorderPainted(true);
		
		btnGuardar = new JButton("Guardar");
		btnRecuperar = new JButton("Recuperar");
		
		Font grande = new Font("Arial", Font.BOLD, 18);
		btnLimpiar.setFont(grande);
		btnGuardar.setFont(grande);
		btnRecuperar.setFont(grande);
		
		zonaBotonesEspeciales.add(btnLimpiar);
		zonaBotonesEspeciales.add(btnGuardar);
		zonaBotonesEspeciales.add(btnRecuperar);
		
		
		/**
		 * Zona donde ubicamos los dos conjuntos de botones (numerico y operaciones y los botones especiales)
		 * */
		JPanel zonaDeBotonesPrincipales = new JPanel(new BorderLayout());
		zonaDeBotonesPrincipales.add(zonaBotonesNumerosYOperaciones, BorderLayout.CENTER);
		zonaDeBotonesPrincipales.add(zonaBotonesEspeciales, BorderLayout.EAST);
		add(zonaDeBotonesPrincipales, BorderLayout.CENTER);
		
		/**
		 * Configuraciones para darle foco al display de la calculadora y poder escribir directamente del teclado sin tener que clickar con el ratón.
		 * Para que la ventana sea visible y se coloque en el centro de la pantalla al abrirla.
		 * */
		setFocusable(true);
		setVisible(true);
		setLocationRelativeTo(null);
		displayCalculadora.requestFocusInWindow();

	}

}
