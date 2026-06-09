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

    public Partida cargarUnica() {
        Document doc = coleccion.find().first();
        if (doc == null) {
            return null;
        }
        return documentToPartida(doc);
    }

    public void guardar(Partida partida) {
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
                .append("dpPorSegundo", p.getDpSegundo())
                .append("nivel", p.getNivel())
                .append("mejoras", mejDoc)
                .append("tiempoPartida", p.getTiempoPartida())
                .append("fechaInicio", p.getFechaInicio());
    }

    private Partida documentToPartida(Document doc) {
        Document mejDoc = (Document) doc.get("mejoras");

        if (mejDoc == null) {
            mejDoc = new Document();
        }

        Mejoras mejoras = new Mejoras(
                getInt(mejDoc, "numRaspberry", 0),
                getInt(mejDoc, "numPC", 0),
                getInt(mejDoc, "numJunior", 0),
                getInt(mejDoc, "numSenior", 0),
                getInt(mejDoc, "numMaqCafe", 0),
                getInt(mejDoc, "numRGBS", 0)
        );

        return new Partida(
                doc.getString("jugador"),
                getInt(doc, "dataPoints", 0),
                getInt(doc, "nivel", 1),
                getInt(doc, "dpPorSegundo", 0),
                mejoras,
                getLong(doc, "tiempoPartida", 0L),
                doc.getDate("fechaInicio")
        );
    }

    private int getInt(Document doc, String campo, int valorDefecto) {
        Object valor = doc.get(campo);

        if (valor == null) {
            return valorDefecto;
        }

        if (valor instanceof Number) {
            return ((Number) valor).intValue();
        }

        return valorDefecto;
    }

    private long getLong(Document doc, String campo, long valorDefecto) {
        Object valor = doc.get(campo);

        if (valor == null) {
            return valorDefecto;
        }

        if (valor instanceof Number) {
            return ((Number) valor).longValue();
        }

        return valorDefecto;
    }
}