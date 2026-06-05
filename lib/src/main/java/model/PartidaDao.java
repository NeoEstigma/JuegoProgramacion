package model;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;

public class PartidaDao {

	private MongoCollection<Document> coleccion;

	public PartidaDao() {
		MongoDatabase db = Conexion.getDatabase();
		this.coleccion = db.getCollection("partidas");
	}

	public void eliminarUnica() {
		coleccion.deleteMany(new Document());
	}

	// Devuelve la única partida guardada, sin importar el nombre
	public Partida cargarUnica() {
		Document doc = coleccion.find().first();
		if (doc == null) {
			return null;
		}
		return documentToPartida(doc);
	}

	// Guarda o sobreescribe la partida del jugador (upsert)
	public void guardar(Partida partida) {
		Document doc = partidaToDocument(partida);
		coleccion.replaceOne(Filters.eq("jugador", partida.getJugador()), doc, new ReplaceOptions().upsert(true));
	}

	// Carga la partida de un jugador, devuelve null si no existe
	public Partida cargar(String jugador) {
		Document doc = coleccion.find(Filters.eq("jugador", jugador)).first();
		if (doc == null) {
			return null;
		}
		return documentToPartida(doc);
	}

	// Elimina la partida guardada de un jugador
	public void eliminar(String jugador) {
		coleccion.deleteOne(Filters.eq("jugador", jugador));
	}

	// Convierte Partida a Document de MongoDB
	private Document partidaToDocument(Partida p) {
		Mejoras m = p.getMejoras();
		Document mejDoc = new Document().append("numRaspberry", m.getNumRaspberry()).append("numPC", m.getNumPC())
				.append("numJunior", m.getNumJunior()).append("numSenior", m.getNumSenior())
				.append("numMaqCafe", m.getNumMaqCafe()).append("numRGBS", m.getNumRGBS());

		return new Document().append("jugador", p.getJugador()).append("dataPoints", p.getDp())
				.append("dpPorSegundo", p.getDpPorSegundo()).append("nivel", p.getNivel()).append("mejoras", mejDoc)
				.append("tiempoPartida", p.getTiempoPartida()).append("fechaInicio", p.getFechaInicio());
	}

	// Convierte Document de MongoDB a Partida
	private Partida documentToPartida(Document doc) {
		Document mejDoc = (Document) doc.get("mejoras");
		Mejoras mejoras = new Mejoras(mejDoc.getInteger("numRaspberry", 0), mejDoc.getInteger("numPC", 0),
				mejDoc.getInteger("numJunior", 0), mejDoc.getInteger("numSenior", 0),
				mejDoc.getInteger("numMaqCafe", 0), mejDoc.getInteger("numRGBS", 0));

		return new Partida(doc.getString("jugador"), doc.getInteger("dataPoints", 0), doc.getInteger("nivel", 1),
				doc.getInteger("dpPorSegundo", 0), mejoras,
				doc.getLong("tiempoPartida") != null ? doc.getLong("tiempoPartida") : 0L, doc.getDate("fechaInicio"));
	}
}