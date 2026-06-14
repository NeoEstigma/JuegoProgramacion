package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;


/**
 * DAO encargado de gestionar las operaciones de persistencia
 * relacionadas con el ranking de jugadores.
 *
 * Permite almacenar, actualizar, eliminar y recuperar registros
 * del ranking desde MongoDB, así como obtener las mejores
 * puntuaciones ordenadas por el filtro elegido.
 *
 * Implementa la conversión entre objetos Ranking y documentos BSON.
 *
 * @author Laura
 */
public class RankingDao {

	/**
	 * Colección MongoDB donde se almacenan los registros del ranking.
	 */
	private MongoCollection<Document> coleccion;

	/**
	 * Inicializa la conexión con MongoDB y obtiene la colección
	 * utilizada para almacenar el ranking de jugadores.
	 */
	public RankingDao() {
		MongoDatabase db = Conexion.getDatabase();
		this.coleccion = db.getCollection("ranking");
	}

	/**
	 * Inserta un nuevo registro en el ranking.
	 *
	 * @param ranking Información del jugador que se desea almacenar.
	 */
	public void insertar(Ranking ranking) {
		if (ranking == null) {
			return;
		}

		Document doc = rankingToDocument(ranking);
		coleccion.insertOne(doc);
	}

	/**
	 * Elimina todos los registros almacenados en el ranking.
	 *
	 * Puede utilizarse para reiniciar completamente la clasificación.
	 */
	public void eliminarTodo() {
		coleccion.deleteMany(new Document());
	}

	/**
	 * Guarda un jugador en el ranking o actualiza sus datos
	 * si ya existe un registro asociado al mismo nombre.
	 *
	 * @param ranking Datos del jugador.
	 */
	public void guardarOActualizar(Ranking ranking) {
		if (ranking == null) {
			return;
		}

		Document doc = rankingToDocument(ranking);
		coleccion.replaceOne(com.mongodb.client.model.Filters.eq("jugador", ranking.getJugador()), doc,
				new com.mongodb.client.model.ReplaceOptions().upsert(true));
	}

	/**
	 * Obtiene los diez mejores jugadores ordenados
	 * de mayor a menor cantidad de Data Points.
	 *
	 * @return Lista con los diez primeros clasificados.
	 */
	public List<Ranking> obtenerTop10() {
		List<Ranking> lista = new ArrayList<>();

		coleccion.find().sort(Sorts.descending("dataPoints")).limit(10)
				.forEach(doc -> lista.add(documentToRanking(doc)));

		return lista;
	}

	/**
	 * Recupera todos los registros almacenados en el ranking,
	 * ordenados descendentemente según los Data Points obtenidos.
	 *
	 * @return Lista completa del ranking.
	 */
	public List<Ranking> obtenerTodos() {
		List<Ranking> lista = new ArrayList<>();

		coleccion.find().sort(Sorts.descending("dataPoints")).forEach(doc -> lista.add(documentToRanking(doc)));

		return lista;
	}

	/**
	 * Convierte un objeto Ranking en un documento BSON
	 * para su almacenamiento en MongoDB.
	 *
	 * También serializa la información de las mejoras
	 * adquiridas por el jugador.
	 *
	 * @param r Registro del ranking.
	 * @return Documento MongoDB equivalente.
	 */
	private Document rankingToDocument(Ranking r) {
		Mejoras m = r.getMejoras();

		if (m == null) {
			m = new Mejoras();
		}

		Document mejDoc = new Document().append("numRaspberry", m.getNumRaspberry()).append("numPC", m.getNumPC())
				.append("numJunior", m.getNumJunior()).append("numSenior", m.getNumSenior())
				.append("numMaqCafe", m.getNumMaqCafe()).append("numRGBS", m.getNumRGBS());

		return new Document().append("jugador", r.getJugador()).append("dataPoints", r.getDp())
				.append("nivel", r.getNivel()).append("mejoras", mejDoc).append("tiempoPartida", r.getTiempoPartida())
				.append("fechaInicio", r.getFechaInicio() != null ? r.getFechaInicio() : new Date());
	}

	
	/**
	 * Reconstruye un objeto Ranking a partir de un documento
	 * recuperado de MongoDB.
	 *
	 * Este método restaura los datos del jugador, las mejoras
	 * adquiridas, el tiempo de partida y la fecha de inicio.
	 *
	 * @param doc Documento BSON obtenido de la base de datos.
	 * @return Objeto Ranking reconstruido.
	 */
	private Ranking documentToRanking(Document doc) {
		Document mejDoc = doc.get("mejoras", Document.class);

		if (mejDoc == null) {
			mejDoc = new Document();
		}

		Mejoras mejoras = new Mejoras();
		mejoras.setNumRaspberry(mejDoc.getInteger("numRaspberry", 0));
		mejoras.setNumPC(mejDoc.getInteger("numPC", 0));
		mejoras.setNumJunior(mejDoc.getInteger("numJunior", 0));
		mejoras.setNumSenior(mejDoc.getInteger("numSenior", 0));
		mejoras.setNumMaqCafe(mejDoc.getInteger("numMaqCafe", 0));
		mejoras.setNumRGBS(mejDoc.getInteger("numRGBS", 0));

		Ranking ranking = new Ranking();
		ranking.setJugador(doc.getString("jugador"));
		ranking.setDp(getLongSeguro(doc, "dataPoints", 0));
		ranking.setNivel(getIntegerSeguro(doc, "nivel", 0));
		ranking.setMejoras(mejoras);
		ranking.setTiempoPartida(getLongSeguro(doc, "tiempoPartida", 0L));
		ranking.setFechaInicio(doc.getDate("fechaInicio"));

		return ranking;
	}

	/**
	 * Recupera un valor numérico de tipo long de forma segura.
	 *
	 * Permite leer valores almacenados como Integer, Long o Double,
	 * evitando errores de conversión al reconstruir los datos
	 * procedentes de MongoDB.
	 *
	 * @param doc Documento que contiene el campo.
	 * @param campo Nombre del campo.
	 * @param valorDefecto Valor alternativo si no existe un número válido.
	 * @return Valor convertido a long o el valor por defecto.
	 */
	private long getLongSeguro(Document doc, String campo, long valorDefecto) {
		Object valor = doc.get(campo);

		if (valor instanceof Integer) {
			return ((Integer) valor).longValue();
		}

		if (valor instanceof Long) {
			return (Long) valor;
		}

		if (valor instanceof Double) {
			return ((Double) valor).longValue();
		}

		return valorDefecto;
	}

	/**
	 * Recupera un valor numérico de tipo int de forma segura.
	 *
	 * Se utiliza para garantizar la compatibilidad con distintos
	 * tipos numéricos almacenados en MongoDB.
	 *
	 * @param doc Documento que contiene el campo.
	 * @param campo Nombre del campo.
	 * @param valorDefecto Valor alternativo si el campo no existe
	 *                     o no contiene un número válido.
	 * @return Valor convertido a entero o el valor por defecto.
	 */
	private int getIntegerSeguro(Document doc, String campo, int valorDefecto) {
		Object valor = doc.get(campo);

		if (valor instanceof Integer) {
			return (Integer) valor;
		}

		if (valor instanceof Long) {
			return ((Long) valor).intValue();
		}

		if (valor instanceof Double) {
			return ((Double) valor).intValue();
		}

		return valorDefecto;
	}
}