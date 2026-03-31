package learning.tokioschool.practicaTresControlStock;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Clase HistoricoControlador.
 * 
 * Se encarga de gestionar la ventana del historico de operaciones del Stock.
 * Ademas aplica colores al texto en funcion del tipo de movimiento que se
 * aplique.
 * 
 * <ul>
 * <li>Azul -> Nuevo Producto.</li>
 * <li>Verde -> Incrementar stock.</li>
 * <li>Rojo -> Retirada de stock.</li>
 * </ul>
 * 
 * 
 */
public class HistoricoControlador {

	/** The btn cerrar historico. */
	@FXML
	private Button btnCerrarHistorico;

	/** The columna fecha historico. */
	@FXML
	private TableColumn<Historico, String> columnaFechaHistorico;

	/** The columna stock historico. */
	@FXML
	private TableColumn<Historico, Integer> columnaStockHistorico;

	/** The columna tipo historico. */
	@FXML
	private TableColumn<Historico, String> columnaTipoHistorico;

	/** The historico grid panel. */
	@FXML
	private GridPane historicoGridPanel;

	/** Tabla donde se van a guardar los registros del historico. */
	@FXML
	private TableView<Historico> tablaHistorico;

	/** Ventana asocia al controlador. */
	private Stage stage;

	/**
	 * Initialize.
	 * 
	 * Metodo en el que configuraremos las columnas de las tabla y aplicamos los
	 * colores al texto en funcion de la operacion
	 */
	@FXML
	public void initialize() {
		columnaFechaHistorico.setCellValueFactory(new PropertyValueFactory<>("fecha"));
		columnaStockHistorico.setCellValueFactory(new PropertyValueFactory<>("stock"));
		columnaTipoHistorico.setCellValueFactory(new PropertyValueFactory<>("tipo"));

		aplicarColor(columnaFechaHistorico);
		aplicarColor(columnaStockHistorico);
		aplicarColor(columnaTipoHistorico);

	}

	/**
	 * Action del botón cerrar historico.
	 * 
	 * Cierra la ventana.
	 *
	 * @param event the event
	 */
	@FXML
	void btnCerrarHistoricoAction(ActionEvent event) {
		stage.close();
	}

	/**
	 * Método cargarHistorico.
	 * 
	 * Carga los datos en la ventana.
	 *
	 * @param historico the historico
	 */
	public void cargarHistorico(List<Historico> historico) {
		tablaHistorico.setItems(FXCollections.observableArrayList(historico));
	}

	/**
	 * Establece la nueva ventana.
	 *
	 * @param stage nueva ventana.
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	/**
	 * Aplicar color.
	 *
	 * @param <T>     Tipo de dato generico que varia en funcion del dato que maneja la columna.
	 * @param columna Columna a la que se le va a aplicar el color.
	 */
	private <T> void aplicarColor(TableColumn<Historico, T> columna) {
		columna.setCellFactory(col -> new TableCell<Historico, T>() {
			@Override
			protected void updateItem(T valor, boolean empty) {
				super.updateItem(valor, empty);

				if (empty || valor == null) {
					setText(null);
					setStyle("");
					return;
				}

				setText(valor.toString());

				Historico h = getTableView().getItems().get(getIndex());

				switch (h.getTipo()) {
				case "Nuevo Producto":
					setStyle("-fx-text-fill: blue;");
					break;
				case "Aumento":
					setStyle("-fx-text-fill: green;");
					break;
				case "Reduccion":
					setStyle("-fx-text-fill: red;");
					break;
				default:
					setStyle("");
				}
			}
		});
	}

}
