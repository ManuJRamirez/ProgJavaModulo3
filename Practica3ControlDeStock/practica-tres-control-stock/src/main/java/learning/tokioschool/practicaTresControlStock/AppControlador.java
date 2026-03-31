/*
 * 
 */
package learning.tokioschool.practicaTresControlStock;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Clase AppControlador.
 * 
 * Controlador de la interfaz grafica de nuestra aplicación.
 * 
 * FUNCIONES DEL CONTROLADOR:
 * <ul>
 * <li>Cargar y mostrar todos los productos almacenados en nuestra
 * "baseDeDatos.json" y si no pudiera cargarlo, crearia uno nuevo.</li>
 * <li>Guardar los cambios realizados en nuestra "baseDeDatos.json".
 * <li>Agregar productos nuevos con un identificador generado de forma
 * automatica.</li>
 * <li>Modificar productos que ya existen clickando dos veces sobre ellos en la
 * lista o con el boton "Modificar". Cuando hacemos esto, los datos se volcaran
 * a sus campos de texto correspondientes para poder modificarlos. El campo de
 * texto del ID se desabilita.</li>
 * <li>Cuando intentamos Modificar un producto, una vez lo seleccionamos
 * apareceran dos nuevos botones: "Aceptar" (la modificación) o "Cancelar".</li>
 * <li>Buscar los productos usando el Identificador o el nombre. Se podria usar
 * clickando en el boton "Buscar" o pulsando "Enter" del teclado. Filtra por
 * todos los productos que contengan el "ID" o la palabra que escribamos.</li>
 * <li>Boton "Borrar filtros" para restaurar la tabla. Tambien se puede usar
 * para vaciar los campos de texto y reiniciar los botones. Es como si
 * pulsaramos el boton HOME y volvieramos al menu principal.</li>
 * <li>No podremos escribir letras en el campo de texto de "Stock".</li>
 * <li>Tendremos meensajes dee confirmación para crear un producto y
 * advertencias si ocurriera algo como por ejemplo, intentar crear un producto
 * sin poner el nombre o el stock.</li>
 * <li>Dentro de la tabla podremos ordenar de mayor a menor, de la A - Z y
 * viceversa en las 3 columnas correspondientes. Clickando en la cabecera de la
 * columna (se activará la "mano" en el cursor).</li>
 * <li>Boton de Historico añadido para tener control del movimiento del stock de
 * todos los productos</li>
 * </ul>
 */
public class AppControlador {

	/** The gridpanel. */
	@FXML
	private GridPane gridpanel;

	/** The btn aceptar. */
	@FXML
	private Button btnAceptar;

	/** The btn agregar. */
	@FXML
	private Button btnAgregar;

	/** The btn buscar. */
	@FXML
	private Button btnBuscar;

	/** The btn cancelar. */
	@FXML
	private Button btnCancelar;

	/** The btn modificar. */
	@FXML
	private Button btnModificar;

	/** The btn mostrar todo. */
	@FXML
	private Button btnBorrarFiltros;

	/** The btn historico. */
	@FXML
	private Button btnHistorico;

	/** The label identificador. */
	@FXML
	private Label labelIdentificador;

	/** The label nombre producto. */
	@FXML
	private Label labelNombreProducto;

	/** The label stock. */
	@FXML
	private Label labelStock;

	/** The text identificador. */
	@FXML
	private TextField textIdentificador;

	/** The text nombre producto. */
	@FXML
	private TextField textNombreProducto;

	/** The text stock. */
	@FXML
	private TextField textStock;

	/** The tabla productos. */
	@FXML
	private TableView<Producto> tablaProductos;

	/** The columna identificador. */
	@FXML
	private TableColumn<Producto, String> columnaIdentificador;

	/** The columna nombre producto. */
	@FXML
	private TableColumn<Producto, String> columnaNombreProducto;

	/** The columna stock. */
	@FXML
	private TableColumn<Producto, Integer> columnaStock;

	/**
	 * lista de productos. Usamos ObservableList para que actualice de forma
	 * inmediata la tabla cuando añadimos un producto sin tener que recargar la
	 * aplicación.
	 */
	private ObservableList<Producto> listaProductos;

	/**
	 * Constante en la que se define el formato de fecha y hora usado para el
	 * historico.
	 */
	private static final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy <--> HH:mm");

	/**
	 * Initialize.
	 * 
	 * Metodo en el que se configuramos las columnas de la tabla, cargamos los
	 * productos, aplicamos la validacion al campo "Stock" y añadimos el doble click
	 * a la tabla, para seleccionar un producto.
	 */
	@FXML
	public void initialize() {
		columnaIdentificador.setCellValueFactory(new PropertyValueFactory<>("identificador"));
		columnaNombreProducto.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
		columnaStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

		listaProductos = FXCollections.observableArrayList(ControladorDatos.cargarProductos());
		tablaProductos.setItems(listaProductos);

		textStock.setTextFormatter(new TextFormatter<>(dato -> {
			if (dato.getControlNewText().matches("\\d*")) {
				return dato;
			}
			return null;
		}));

		tablaProductos.setOnMouseClicked(evento -> {
			if (evento.getClickCount() == 2) {
				Producto productoSeleccionado = tablaProductos.getSelectionModel().getSelectedItem();
				if (productoSeleccionado != null) {
					mostrarDatosEnTextFields(productoSeleccionado);
					textIdentificador.setDisable(true);

					btnAceptar.setVisible(true);
					btnCancelar.setVisible(true);
					btnAgregar.setDisable(true);
					btnModificar.setDisable(true);
				}
			}
		});

		btnAceptar.setVisible(false);
		btnCancelar.setVisible(false);
	}

	/**
	 * Método donde definimos la acción del boton "Aceptar".
	 * 
	 * Guarda los cambios realizados, en el Json, despues de haber seleccionado y
	 * modificado un Producto. Además refresca la tabla, limpia los campos y vuelve
	 * a poner los dos botones principales (Agregar y Modificar).
	 * 
	 * También añade al historico cualquier movimiento que se haga sobre el stock.
	 *
	 * @param event acción del boton.
	 */
	@FXML
	void btnAceptarAction(ActionEvent event) {

		Producto productoSeleccionado = tablaProductos.getSelectionModel().getSelectedItem();
		if (productoSeleccionado == null) {
			return;
		}

		int stockAnterior = productoSeleccionado.getStock();

		String nuevoNombre = textNombreProducto.getText();
		int nuevoStock = Integer.parseInt(textStock.getText());

		if (nuevoStock != stockAnterior) {
			String fecha = LocalDateTime.now().format(FORMATO_FECHA_HORA);
			String tipo = nuevoStock > stockAnterior ? "Aumento" : "Reduccion";

			productoSeleccionado.getHistorico().add(new Historico(fecha, nuevoStock, tipo));
		}

		productoSeleccionado.setNombreProducto(nuevoNombre);
		productoSeleccionado.setStock(nuevoStock);

		tablaProductos.refresh();
		ControladorDatos.guardarProductos(listaProductos);

		limpiarCampos();
		restaurarBotones();
	}

	/**
	 * Método donde definimos la acción del boton "Agregar".
	 * 
	 * Este método valida el los campos de texto, genera un nuevo identificador,
	 * crea una ventana de confirmacion en la que si clickamos en "Sí", añadirá el
	 * producto a la lista, guardara la lista actualizada en el json y limpiará los
	 * campos.
	 * 
	 * A la hora de crear un nuevo producto, guardaremos tambien el primer registro
	 * del historico y lo definiremos como "Nuevo Producto".
	 *
	 * @param event acción del boton.
	 */
	@FXML
	void btnAgregarAction(ActionEvent event) {
		if (textNombreProducto.getText().isEmpty() || textStock.getText().isEmpty()) {
			mostrarAlertaGenerica("Debes rellenar los campos de nombre y stock para agregar el producto.");
			return;
		}

		if (isNombreDuplicado(textNombreProducto.getText())) {
			mostrarAlertaGenerica("Ya existe un producto con ese nombre.");
			return;
		}

		String nuevoID = generarNuevoIdentificiador();

		Alert ventanaConfirmacion = new Alert(Alert.AlertType.CONFIRMATION);
		ventanaConfirmacion.initOwner(gridpanel.getScene().getWindow());
		ventanaConfirmacion.setTitle("Confirmar: ");
		ventanaConfirmacion.setHeaderText(null);
		ventanaConfirmacion.setContentText("Se va a añadir este producto a la base de datos. Confirmar:"
				+ "\n IMPORTANTE: El identificador del producto se asignará de forma automática.");

		ButtonType btnSi = new ButtonType("Sí");
		ButtonType btnNo = new ButtonType("No");
		ventanaConfirmacion.getButtonTypes().setAll(btnSi, btnNo);

		if (ventanaConfirmacion.showAndWait().get() == btnSi) {

			int stockInicial = Integer.parseInt(textStock.getText());
			Producto prod = new Producto(nuevoID, textNombreProducto.getText(), stockInicial);
			String fechaHora = LocalDateTime.now().format(FORMATO_FECHA_HORA);
			Historico primeraMovimientoAgregarProducto = new Historico(fechaHora, stockInicial, "Nuevo Producto");
			prod.getHistorico().add(primeraMovimientoAgregarProducto);

			listaProductos.add(prod);
			ControladorDatos.guardarProductos(listaProductos);

			limpiarCampos();
		}
	}

	/**
	 * Método donde definimos la acción del boton "Buscar".
	 * 
	 * Filtra la lista de productos usando el identificador y/o nombre del producto.
	 * El filtro mostrará el producto que contenga el ID y/o la palabra que se
	 * escriba en nombre. Si escribimos una cosa solo buscará por una. Si escribimos
	 * las dos mostrara
	 *
	 * @param event acción del boton.
	 */
	@FXML
	void btnBuscarAction(ActionEvent event) {
		String id = textIdentificador.getText().trim().toLowerCase();
		String nombre = textNombreProducto.getText().trim().toLowerCase();

		if (id.isEmpty() && nombre.isEmpty()) {
			mostrarAlertaGenerica("Introduce un identificador o un nombre del producto.");
			return;
		}

		ObservableList<Producto> resultado = listaProductos.stream().filter(p -> {
			boolean coincideID = id.isEmpty() || p.getIdentificador().toLowerCase().contains(id);
			boolean coincideNombre = nombre.isEmpty() || p.getNombreProducto().toLowerCase().contains(nombre);
			return coincideID && coincideNombre;
		}).collect(FXCollections::observableArrayList, ObservableList::add, ObservableList::addAll);

		if (resultado.isEmpty()) {
			mostrarMensajeInfo("No existe ningún producto que cumpla los requisitos.");
			return;
		}

		tablaProductos.setItems(resultado);

	}

	/**
	 * Método donde definimos la acción del boton "Cancelar".
	 * 
	 * Cancela el modo Modificar, restaura los botones principales y limpia los
	 * campos.
	 *
	 * @param event acción del boton.
	 */
	@FXML
	void btnCancelarAction(ActionEvent event) {
		limpiarCampos();
		restaurarBotones();
		tablaProductos.getSelectionModel().clearSelection();
	}

	/**
	 * Método donde definimos la acción del boton "Modificar".
	 * 
	 * Permite editar un producto seleccionandolo en la lista. Una vez seleccionado
	 * hará visible los botones de "Aceptar" y "Cancelar".
	 *
	 * @param event acción del boton.
	 */
	@FXML
	void btnModificarAction(ActionEvent event) {
		Producto productoSeleccionado = tablaProductos.getSelectionModel().getSelectedItem();

		if (productoSeleccionado == null) {
			mostrarAlertaGenerica("Debes seleccionar un producto de la tabla");
			return;
		}

		mostrarDatosEnTextFields(productoSeleccionado);
		textIdentificador.setDisable(true);

		btnAceptar.setVisible(true);
		btnCancelar.setVisible(true);
		btnAgregar.setDisable(true);
		btnModificar.setDisable(true);

	}

	/**
	 * Método donde definimos la acción del boton "BorrarFiltros".
	 * 
	 * Volvemos a mostrar la lista de producto completa, limpiamos los campos y
	 * restauramos los botones principales.
	 *
	 * @param event acción del boton.
	 */
	@FXML
	void btnBorrarFiltrosAction(ActionEvent event) {
		tablaProductos.setItems(listaProductos);
		limpiarCampos();
		restaurarBotones();
	}

	/**
	 * Metodo que define la acción del botón Historico.
	 * 
	 * Abre la ventana del Historico del Producto seleccionado.
	 *
	 * @param event the event
	 */
	@FXML
	void btnHistoricoAction(ActionEvent event) {
		Producto producto = tablaProductos.getSelectionModel().getSelectedItem();

		if (producto == null) {
			mostrarAlertaGenerica("Debes seleccionar un producto para ver su histórico");
			return;
		}

		try {
			FXMLLoader cargaVentana = new FXMLLoader(getClass().getResource("/vistaHistorico.fxml"));
			Parent root = cargaVentana.load();

			HistoricoControlador controladorHistorico = cargaVentana.getController();

			Stage ventana = new Stage();
			ventana.setTitle(producto.getIdentificador() + " -> " + producto.getNombreProducto());
			ventana.setScene(new Scene(root));
			ventana.initOwner(gridpanel.getScene().getWindow());
			ventana.initModality(Modality.WINDOW_MODAL);

			controladorHistorico.setStage(ventana);
			controladorHistorico.cargarHistorico(producto.getHistorico());
			ventana.setResizable(false);

			ventana.show();

		} catch (Exception error) {
			error.printStackTrace();
		}

	}

	/**
	 * Generar nuevo identificiador.
	 * 
	 * Identifica cual es el ID mayor y crea uno nuevo sumandole 1. Esto nos evita
	 * tener que comprar si existe un ID en la base de datos.
	 *
	 * @return the string
	 */
	private String generarNuevoIdentificiador() {
		int idMax = listaProductos.stream().mapToInt(p -> Integer.parseInt(p.getIdentificador().substring(1))).max()
				.orElse(0);

		return "P" + (idMax + 1);
	}

	/**
	 * Este método comprueba si existe un producto con el mismo nombre.
	 *
	 * @param nombreProducto nombre producto.
	 * @return true, Si existe ya un nombre.
	 */
	private boolean isNombreDuplicado(String nombreProducto) {
		return listaProductos.stream().anyMatch(p -> p.getNombreProducto().equalsIgnoreCase(nombreProducto));
	}

	/**
	 * Limpia todos campos (textFields).
	 */
	private void limpiarCampos() {
		textIdentificador.clear();
		textNombreProducto.clear();
		textStock.clear();
	}

	/**
	 * Restaura botones principales (Agregar y Modificar).
	 */
	private void restaurarBotones() {
		btnAceptar.setVisible(false);
		btnCancelar.setVisible(false);
		btnAgregar.setDisable(false);
		btnModificar.setDisable(false);
		textIdentificador.setDisable(false);
	}

	/**
	 * Este método vuelca los datos de un producto en los textFields. Cada atributo
	 * del producto va a su correspondiente campo.
	 *
	 * @param p de tipo Producto.
	 */
	private void mostrarDatosEnTextFields(Producto p) {
		textIdentificador.setText(p.getIdentificador());
		textNombreProducto.setText(p.getNombreProducto());
		textStock.setText(String.valueOf(p.getStock()));
		tablaProductos.getSelectionModel().select(p);
	}

	/**
	 * Mostrar alerta generica.
	 *
	 * @param mensaje Se le pasa el mensaje que se desea mostrar en la ventana
	 *                emergente.
	 */
	private void mostrarAlertaGenerica(String mensaje) {
		Alert alerta = new Alert(Alert.AlertType.WARNING);
		alerta.setHeaderText(null);
		alerta.setContentText(mensaje);
		alerta.initOwner(gridpanel.getScene().getWindow());
		alerta.showAndWait();
	}

	/**
	 * Mostrar mensaje info.
	 *
	 * @param mensaje Se le pasa el mensaje que se desea mostrar en la ventana
	 *                emergente.
	 */
	private void mostrarMensajeInfo(String mensaje) {
		Alert alerta = new Alert(Alert.AlertType.INFORMATION);
		alerta.setHeaderText(null);
		alerta.setContentText(mensaje);
		alerta.initOwner(gridpanel.getScene().getWindow());
		alerta.showAndWait();
	}

}
