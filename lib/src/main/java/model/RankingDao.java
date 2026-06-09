package model;

import java.util.ArrayList;
import java.util.Date;
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

    public void insertar(Ranking ranking) {
        if (ranking == null) {
            return;
        }

        Document doc = rankingToDocument(ranking);
        coleccion.insertOne(doc);
    }

    public List<Ranking> obtenerTop10() {
        List<Ranking> lista = new ArrayList<>();

        coleccion.find()
                .sort(Sorts.descending("dataPoints"))
                .limit(10)
                .forEach(doc -> lista.add(documentToRanking(doc)));

        return lista;
    }

    public List<Ranking> obtenerTodos() {
        List<Ranking> lista = new ArrayList<>();

        coleccion.find()
                .sort(Sorts.descending("dataPoints"))
                .forEach(doc -> lista.add(documentToRanking(doc)));

        return lista;
    }

    private Document rankingToDocument(Ranking r) {
        Mejoras m = r.getMejoras();

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
                .append("jugador", r.getJugador())
                .append("dataPoints", r.getDp())
                .append("nivel", r.getNivel())
                .append("mejoras", mejDoc)
                .append("tiempoPartida", r.getTiempoPartida())
                .append("fechaInicio", r.getFechaInicio() != null ? r.getFechaInicio() : new Date());
    }

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
        ranking.setDp(getIntegerSeguro(doc, "dataPoints", 0));        ranking.setMejoras(mejoras);
        ranking.setTiempoPartida(getLongSeguro(doc, "tiempoPartida", 0L));
        ranking.setFechaInicio(doc.getDate("fechaInicio"));

        return ranking;
    }

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