package learning.tokioschool.practicaTresControlStock;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Clase principal App. Extiende de "Application" para poder usar el método
 * {@code launch()}, {@link #start(Stage)} y arrancar javaFx.
 * 
 * Esta clase se encarga de iniciar la interfaz grafica, cargando el archivo
 * FXML.
 * 
 */
public class App extends Application {

	/**
	 * En el método Main llamaremos a {@code launch()} para iniciar el ciclo de
	 * javaFx.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Cargando...");
		launch(args);
	}

	/**
	 * Método Start.
	 * 
	 * La función de este método sera de cargar el archivo fxml y a partir de ahí
	 * crearemos la escena y se mostrará la ventana principal de nuestra aplicacion.
	 *
	 * @param primaryStage ventana principal.
	 * @throws Exception En caso de que no pueda cargar la interfaz gráfica, saltará una excepcion.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {

		FXMLLoader cargaVentana = new FXMLLoader(getClass().getResource("/vistaControlStock.fxml"));
		Parent root;

		try {
			root = cargaVentana.load();

		} catch (IOException error) {
			System.err.println("Error al cargar la ventana FXML");
			error.printStackTrace();
			return;
		}

		Scene ventana = new Scene(root);
		primaryStage.setScene(ventana);
		primaryStage.setTitle("Practica 3: Control de Stock -> Manuel J. Ramirez (Ejemplo -> Papelería)");
		primaryStage.setResizable(false);
		primaryStage.show();

	}
}
