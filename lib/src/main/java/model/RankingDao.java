package model;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;

public class RankingDao {

	private MongoCollection<Document> coleccion;

	public RankingDao() {
		MongoDatabase db = Conexion.getDatabase();
		this.coleccion = db.getCollection("ranking");
	}

	// Inserta un nuevo resultado al terminar la partida
	public void insertar(Ranking ranking) {
		Document doc = rankingToDocument(ranking);
		coleccion.insertOne(doc);
	}

	// Devuelve el top 10 ordenado por dp de mayor a menor
	public List<Ranking> obtenerTop10() {
		List<Ranking> lista = new ArrayList<>();
		coleccion.find().sort(Sorts.descending("dataPoints")).limit(10)
				.forEach(doc -> lista.add(documentToRanking(doc)));
		return lista;
	}

	// Devuelve todos los resultados ordenados por dp de mayor a menor
	public List<Ranking> obtenerTodos() {
		List<Ranking> lista = new ArrayList<>();
		coleccion.find().sort(Sorts.descending("dataPoints")).forEach(doc -> lista.add(documentToRanking(doc)));
		return lista;
	}

	// Convierte Ranking a Document de MongoDB
	private Document rankingToDocument(Ranking r) {
		Mejoras m = r.getMejoras();
		Document mejDoc = new Document().append("numRaspberry", m.getNumRaspberry()).append("numPC", m.getNumPC())
				.append("numJunior", m.getNumJunior()).append("numSenior", m.getNumSenior())
				.append("numMaqCafe", m.getNumMaqCafe()).append("numRGBS", m.getNumRGBS());

		return new Document().append("jugador", r.getJugador()).append("dataPoints", r.getDp())
				.append("nivel", r.getNivel()).append("mejoras", mejDoc).append("tiempoPartida", r.getTiempoPartida())
				.append("fechaInicio", r.getFechaInicio());
	}

	// Convierte Document de MongoDB a Ranking
	private Ranking documentToRanking(Document doc) {
		Document mejDoc = (Document) doc.get("mejoras");
		Mejoras mejoras = new Mejoras(mejDoc.getInteger("numRaspberry", 0), mejDoc.getInteger("numPC", 0),
				mejDoc.getInteger("numJunior", 0), mejDoc.getInteger("numSenior", 0),
				mejDoc.getInteger("numMaqCafe", 0), mejDoc.getInteger("numRGBS", 0));

		return new Ranking(doc.getString("jugador"), doc.getInteger("dataPoints", 0), doc.getInteger("nivel", 1),
				mejoras, doc.getLong("tiempoPartida") != null ? doc.getLong("tiempoPartida") : 0L,
				doc.getDate("fechaInicio"));
	}
}