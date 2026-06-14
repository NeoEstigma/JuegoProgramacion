package model;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 * Clase de conexión a la base de datos MongoDB local.
 * Implementa el patrón Singleton para garantizar que solo existe
 * una conexión activa durante toda la ejecución de la aplicación.
 * La conexión se crea la primera vez que se solicita y se reutiliza
 * en todas las llamadas posteriores.
 *
 * @author Laura
 */

public class Conexion {

	 /** URI de conexión a MongoDB local en el puerto por defecto. */
	private static final String URI = "mongodb://localhost:27017";
	
	/** Nombre de la base de datos del juego. */
	private static final String DATABASE = "terminal_clicker";

	/** Cliente de MongoDB. Se inicializa una sola vez. */
	private static MongoClient client;
	
	/** Referencia a la base de datos activa. */
	private static MongoDatabase database;

	/**
     * Devuelve la instancia de la base de datos MongoDB.
     * Si la conexión no existe todavía, la crea y la almacena para
     * reutilizarla en futuras llamadas.
     *
     * @return instancia de {@link MongoDatabase} conectada a terminal_clicker.
     */
	
	public static MongoDatabase getDatabase() {
		if (client == null) {
			client = MongoClients.create(URI);
			database = client.getDatabase(DATABASE);
		}
		return database;
	}
	
	/**
     * Cierra la conexión con MongoDB y libera los recursos.
     * Se llama desde {@link application.Main} al cerrar la ventana principal
     * para garantizar que la conexión se cierra correctamente.
     */

	public static void close() {
		if (client != null) {
			client.close();
			client = null;
		}
	}
}