/*
 * 
 */
package learning.tokioschool.practicaTresControlStock;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Clase ControladorDatos.
 * 
 * En esta clase vamos a simular una conexión a la base de datos de nuestro
 * almacén. En este caso usaremos un Json como base de datos.
 * 
 * Las funciones son:
 * <ul>
 * <li>Cargar los datos</li>
 * <li>Guardar datos. Al tratarse de un Json, guardaremos todos los datos cada
 * vez que modifiquemos algo. Si no encuentra nuestra "base de datos" creará una nueva.</li>
 * </ul>
 */
public class ControladorDatos {

	/**
	 * Atributo estático en el que guardamos la ruta del Json. La ruta es
	 * CarpetaPrincipalDeLProyecto/datos/baseDeDatos.json
	 */
	private static final String FILE_PATH = System.getProperty("user.dir") + "/datos/baseDeDatos.json";

	/** Atributo estatico donde vamos a inicializar un objeto gson. */
	private static final Gson gson = new Gson();

	/**
	 * Método que usaremos para cargar la lista de producto de nuestro archivo Json
	 * (aka base de datos). Si no pudiera localizar el archivo "baseDeDatos.json",
	 * creariamos una lista vacía y trabajariamos sobre eso.
	 *
	 * @return listaProducto. Nos devolverá todos los productos de nuestra base de
	 *         datos.
	 */
	public static List<Producto> cargarProductos() {

	    File archivoDatos = new File(FILE_PATH);

	    if (!archivoDatos.exists()) {
	        return new ArrayList<>();
	    }

	    try (FileReader leerArchivo = new FileReader(archivoDatos)) {

	        Type listType = new TypeToken<List<Producto>>() {}.getType();
	        List<Producto> listaProducto = gson.fromJson(leerArchivo, listType);

	        return listaProducto != null ? listaProducto : new ArrayList<>();

	    } catch (Exception error) {
	        error.printStackTrace();
	        return new ArrayList<>();
	    }
	}

	/**
	 * Método que usaremos para guardar productos. Aunque añadamos un solo objeto,
	 * guardaríamos la lista completa en el Json.
	 * 
	 * En el momento que llamamos a este método, si hubiera ocurrido un error al
	 * cargar el archivo "baseDeDatos.json", crearemos otro, que lo usaríamos como
	 * nuestra nueva base de datos.
	 *
	 * @param productos Se le pasa una lista de productos.
	 */
	public static void guardarProductos(List<Producto> productos) {
		try (FileWriter escribirArchivo = new FileWriter(FILE_PATH)) {
			gson.toJson(productos, escribirArchivo);
		} catch (Exception error) {
			error.printStackTrace();
		}
	}

}
