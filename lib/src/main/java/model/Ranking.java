package model;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Representa una entrada dentro del ranking del juego.
 *
 * Esta clase almacena la información principal de una partida,
 * incluyendo el nombre del jugador, los Data Points obtenidos,
 * el nivel alcanzado, las mejoras adquiridas, el tiempo total
 * de juego y la fecha de inicio de la partida.
 *
 * Además, proporciona métodos para mostrar la fecha
 * y el tiempo de juego en un formato legible para la interfaz.
 *
 * @author Mateo
 */

public class Ranking {
	
	/**
     * Nombre del jugador.
     */
    private String jugador;

    /**
     * Cantidad total de Data Points obtenidos.
     */
    private long dp;

    /**
     * Nivel alcanzado por el jugador.
     */
    private int nivel;

    /**
     * Mejoras adquiridas durante la partida.
     */
    private Mejoras mejoras;

    /**
     * Tiempo total de juego expresado en segundos.
     */
    private long tiempoPartida;

    /**
     * Fecha de inicio de la partida.
     */
    private Date fechaInicio;

    /**
     * Constructor vacío.
     */
    public Ranking() {
    }

    /**
     * Constructor que inicializa todos los atributos del ranking.
     *
     * @param jugador Nombre del jugador.
     * @param dp Data Points acumulados.
     * @param nivel Nivel alcanzado.
     * @param mejoras Mejoras obtenidas.
     * @param tiempoPartida Tiempo total de juego en segundos.
     * @param fechaInicio Fecha de inicio de la partida.
     */
	public Ranking(String jugador, long dp, int nivel, Mejoras mejoras, long tiempoPartida, Date fechaInicio) {
		this.jugador = jugador;
		this.dp = dp;
		this.nivel = nivel;
		this.mejoras = mejoras;
		this.tiempoPartida = tiempoPartida;
		this.fechaInicio = fechaInicio;
	}

	
	/**
     * Obtiene el nombre del jugador.
     *
     * @return Nombre del jugador.
     */
    public String getJugador() {
        return jugador;
    }

    /**
     * Establece el nombre del jugador.
     *
     * @param jugador Nuevo nombre del jugador.
     */
    public void setJugador(String jugador) {
        this.jugador = jugador;
    }

    /**
     * Obtiene los Data Points acumulados.
     *
     * @return Cantidad de Data Points.
     */
    public long getDp() {
        return dp;
    }

    /**
     * Establece los Data Points acumulados.
     *
     * @param dp Nueva cantidad de Data Points.
     */
    public void setDp(long dp) {
        this.dp = dp;
    }

    /**
     * Obtiene el nivel alcanzado por el jugador.
     *
     * @return Nivel actual.
     */
    public int getNivel() {
        return nivel;
    }

    /**
     * Establece el nivel del jugador.
     *
     * @param nivel Nuevo nivel.
     */
    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    /**
     * Obtiene las mejoras asociadas a la partida.
     *
     * @return Objeto que contiene las mejoras.
     */
    public Mejoras getMejoras() {
        return mejoras;
    }

    /**
     * Establece las mejoras de la partida.
     *
     * @param mejoras Nuevas mejoras.
     */
    public void setMejoras(Mejoras mejoras) {
        this.mejoras = mejoras;
    }

    /**
     * Obtiene el tiempo total de juego.
     *
     * @return Tiempo en segundos.
     */
    public long getTiempoPartida() {
        return tiempoPartida;
    }

    /**
     * Establece el tiempo total de juego.
     *
     * @param tiempoPartida Tiempo en segundos.
     */
    public void setTiempoPartida(long tiempoPartida) {
        this.tiempoPartida = tiempoPartida;
    }

    /**
     * Obtiene la fecha de inicio de la partida.
     *
     * @return Fecha de inicio.
     */
    public Date getFechaInicio() {
        return fechaInicio;
    }

    /**
     * Establece la fecha de inicio de la partida.
     *
     * @param fechaInicio Nueva fecha de inicio.
     */
    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /**
     * Devuelve el tiempo de partida en un formato legible.
     *
     * Convierte los segundos almacenados a minutos y segundos
     * para su visualización en el ranking o en la interfaz.
     *
     * Ejemplo: 125 segundos → 2 min 5 s
     *
     * @return Tiempo formateado.
     */
	public String getTiempoFormateado() {
		long minutos = tiempoPartida / 60;
		long seg = tiempoPartida % 60;
		return minutos + " min " + seg + " s";
	}

	/**
     * Devuelve la fecha de inicio en formato legible.
     *
     * El formato utilizado es:
     * DD/MM/YYYY HH:MM
     *
     * @return Fecha formateada o "Sin fecha" si no existe.
     */
	public String getFechaFormateada() {
		if (fechaInicio == null) {
			return "Sin fecha";
		}
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		return formato.format(fechaInicio);
	}
}