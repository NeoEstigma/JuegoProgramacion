package model;

import java.util.Date;

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

    public Partida cargarUnica() {
        Document doc = coleccion.find().first();

        if (doc == null) {
            return null;
        }

        return documentToPartida(doc);
    }

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

    public Partida cargar(String jugador) {
        Document doc = coleccion.find(Filters.eq("jugador", jugador)).first();

        if (doc == null) {
            return null;
        }

        return documentToPartida(doc);
    }

    public void eliminar(String jugador) {
        coleccion.deleteOne(Filters.eq("jugador", jugador));
    }

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

    private int getInt(Document doc, String campo, int valorDefecto) {
        Object valor = doc.get(campo);

        if (valor instanceof Number) {
            return ((Number) valor).intValue();
        }

        return valorDefecto;
    }

    private long getLong(Document doc, String campo, long valorDefecto) {
        Object valor = doc.get(campo);

        if (valor instanceof Number) {
            return ((Number) valor).longValue();
        }

        return valorDefecto;
    }

    private boolean getBoolean(Document doc, String campo, boolean valorDefecto) {
        Object valor = doc.get(campo);

        if (valor instanceof Boolean) {
            return (Boolean) valor;
        }

        return valorDefecto;
    }
}