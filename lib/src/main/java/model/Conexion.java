package model;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class Conexion {

	private static final String URI = "mongodb://localhost:27017";
	private static final String DATABASE = "terminal_clicker";

	private static MongoClient client;
	private static MongoDatabase database;

	public static MongoDatabase getDatabase() {
		if (client == null) {
			client = MongoClients.create(URI);
			database = client.getDatabase(DATABASE);
		}
		return database;
	}

	public static void close() {
		if (client != null) {
			client.close();
			client = null;
		}
	}
}