package model;

import java.util.Date;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;


/**
 * Clase encargada de gestionar la persistencia de las partidas
 * en MongoDB mediante el patrón DAO (Data Access Object).
 *
 * Permite guardar, cargar y eliminar partidas, además de realizar
 * la conversión entre objetos Partida y documentos BSON.
 *
 * @author Laura
 */
public class PartidaDao {

	/**
     * Colección MongoDB donde se almacenan las partidas.
     */
    private MongoCollection<Document> coleccion;

    /**
     * Constructor de la clase.
     * Inicializa la conexión con MongoDB y obtiene la colección
     * de partidas.
     */
    public PartidaDao() {
        MongoDatabase db = Conexion.getDatabase();
        this.coleccion = db.getCollection("partidas");
    }

    /**
     * Elimina la partida almacenada en la colección.
     * Se utiliza para reiniciar completamente el progreso del juego.
     */
    public void eliminarUnica() {
        coleccion.deleteMany(new Document());
    }

    /**
     * Carga la partida encontrada en la base de datos.
     *
     * @return objeto Partida si existe; null en caso contrario.
     */
    public Partida cargarUnica() {
        Document doc = coleccion.find().first();

        if (doc == null) {
            return null;
        }

        return documentToPartida(doc);
    }

    /**
     * Guarda una partida en MongoDB.
     * Si el jugador ya existe, sus datos serán actualizados.
     * En caso contrario se creará un nuevo documento.
     *
     * @param partida Partida que se desea almacenar.
     */
    public void guardar(Partida partida) {
        if (partida == null || partida.getJugador() == null) {
            return;
        }

        Document doc = partidaToDocument(partida);

        coleccion.replaceOne(
                Filters.eq("jugador", partida.getJugador()),
                doc,
                new ReplaceOptions().upsert(true)
        );
    }

    /**
     * Recupera una partida asociada a un jugador.
     *
     * @param jugador Nombre del jugador.
     * @return Partida encontrada o null si no existe.
     */
    public Partida cargar(String jugador) {
        Document doc = coleccion.find(Filters.eq("jugador", jugador)).first();

        if (doc == null) {
            return null;
        }

        return documentToPartida(doc);
    }

    /**
     * Elimina una partida de la base de datos.
     *
     * @param jugador Nombre del jugador cuya partida será eliminada.
     */
    public void eliminar(String jugador) {
        coleccion.deleteOne(Filters.eq("jugador", jugador));
    }

    /**
     * Convierte un objeto Partida en un documento MongoDB.
     *
     * Este método serializa toda la información de la partida,
     * incluyendo las mejoras adquiridas por el jugador, para
     * que pueda almacenarse en la colección de MongoDB.
     *
     * @param p Partida que se desea convertir.
     * @return Documento MongoDB con los datos de la partida.
     */
    private Document partidaToDocument(Partida p) {
        Mejoras m = p.getMejoras();

        if (m == null) {
            m = new Mejoras();
        }

        Document mejDoc = new Document()
                .append("numRaspberry", m.getNumRaspberry())
                .append("numPC", m.getNumPC())
                .append("numJunior", m.getNumJunior())
                .append("numSenior", m.getNumSenior())
                .append("numMaqCafe", m.getNumMaqCafe())
                .append("numRGBS", m.getNumRGBS());

        return new Document()
                .append("jugador", p.getJugador())
                .append("dataPoints", p.getDp())
                .append("dpPorClick", p.getDpPorClick())
                .append("dpPorSegundo", p.getDpSegundo())
                .append("nivel", p.getNivel())
                .append("terminado", p.estaTerminado())
                .append("progresoMaximo", p.getProgresoMaximo())
                .append("mejoras", mejDoc)
                .append("tiempoPartida", p.getTiempoPartida())
                .append("fechaInicio", p.getFechaInicio() != null ? p.getFechaInicio() : new Date());
    }

    /**
     * Convierte un documento MongoDB en un objeto Partida.
     *
     * Reconstruye el estado completo de la partida a partir de
     * los datos almacenados en la base de datos, incluyendo las
     * mejoras, estadísticas y progreso del jugador.
     *
     * @param doc Documento obtenido de MongoDB.
     * @return Objeto Partida reconstruido.
     */
    private Partida documentToPartida(Document doc) {

        Document mejDoc = (Document) doc.get("mejoras");

        if (mejDoc == null) {
            mejDoc = new Document();
        }

        Mejoras mejoras = new Mejoras();

        mejoras.setNumRaspberry(getInt(mejDoc, "numRaspberry", 0));
        mejoras.setNumPC(getInt(mejDoc, "numPC", 0));
        mejoras.setNumJunior(getInt(mejDoc, "numJunior", 0));
        mejoras.setNumSenior(getInt(mejDoc, "numSenior", 0));
        mejoras.setNumMaqCafe(getInt(mejDoc, "numMaqCafe", 0));
        mejoras.setNumRGBS(getInt(mejDoc, "numRGBS", 0));

        Partida partida = new Partida();

        partida.setJugador(doc.getString("jugador"));
        partida.setDp(getLong(doc, "dataPoints", 0L));
        partida.setDpPorClick(getLong(doc, "dpPorClick", 1L));
        partida.setDpSegundo(getLong(doc, "dpPorSegundo", 0L));
        partida.setNivel(getInt(doc, "nivel", 1));
        partida.setTerminado(getBoolean(doc, "terminado", false));
        partida.setProgresoMaximo(getLong(doc, "progresoMaximo", 100000L));
        partida.setMejoras(mejoras);
        partida.setTiempoPartida(getLong(doc, "tiempoPartida", 0L));
        partida.setFechaInicio(doc.getDate("fechaInicio"));

        return partida;
    }

    /**
     * Obtiene un valor entero de un documento MongoDB.
     *
     * Si el campo existe y contiene un valor numérico,
     * se devuelve convertido a entero. En caso contrario
     * se devuelve el valor por defecto indicado.
     *
     * @param doc Documento que contiene el campo.
     * @param campo Nombre del campo a consultar.
     * @param valorDefecto Valor que se devolverá si el campo no existe
     *                     o no es numérico.
     * @return Valor entero almacenado o el valor por defecto.
     */
    private int getInt(Document doc, String campo, int valorDefecto) {
        Object valor = doc.get(campo);

        if (valor instanceof Number) {
            return ((Number) valor).intValue();
        }

        return valorDefecto;
    }

    /**
     * Obtiene un valor long de un documento MongoDB.
     *
     * Si el campo existe y contiene un valor numérico,
     * se devuelve convertido a long. En caso contrario
     * se devuelve el valor por defecto indicado.
     *
     * @param doc Documento que contiene el campo.
     * @param campo Nombre del campo a consultar.
     * @param valorDefecto Valor que se devolverá si el campo no existe
     *                     o no es numérico.
     * @return Valor long almacenado o el valor por defecto.
     */
    private long getLong(Document doc, String campo, long valorDefecto) {
        Object valor = doc.get(campo);

        if (valor instanceof Number) {
            return ((Number) valor).longValue();
        }

        return valorDefecto;
    }

    /**
     * Obtiene un valor booleano de un documento MongoDB.
     *
     * Si el campo existe y es de tipo Boolean, se devuelve
     * su valor. En caso contrario se devuelve el valor
     * por defecto indicado.
     *
     * @param doc Documento que contiene el campo.
     * @param campo Nombre del campo a consultar.
     * @param valorDefecto Valor que se devolverá si el campo no existe
     *                     o no es booleano.
     * @return Valor booleano almacenado o el valor por defecto.
     */
    private boolean getBoolean(Document doc, String campo, boolean valorDefecto) {
        Object valor = doc.get(campo);

        if (valor instanceof Boolean) {
            return (Boolean) valor;
        }

        return valorDefecto;
    }
}