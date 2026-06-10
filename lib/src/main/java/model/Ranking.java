package model;

import java.util.Date;

public class Ranking {
	private String jugador;
	private long dp;
	private int nivel;

	private Mejoras mejoras;

	private long tiempoPartida;
	private Date fechaInicio;

	public Ranking() {
	}

	public Ranking(String jugador, long dp, int nivel, Mejoras mejoras, long tiempoPartida, Date fechaInicio) {
		this.jugador = jugador;
		this.dp = dp;
		this.nivel = nivel;
		this.mejoras = mejoras;
		this.tiempoPartida = tiempoPartida;
		this.fechaInicio = fechaInicio;
	}

	public String getJugador() {
		return jugador;
	}

	public void setJugador(String jugador) {
		this.jugador = jugador;
	}

	public long getDp() {
		return dp;
	}

	public void setDp(long dp) {
		this.dp = dp;
	}

	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	public Mejoras getMejoras() {
		return mejoras;
	}

	public void setMejoras(Mejoras mejoras) {
		this.mejoras = mejoras;
	}

	public long getTiempoPartida() {
		return tiempoPartida;
	}

	public void setTiempoPartida(long tiempoPartida) {
		this.tiempoPartida = tiempoPartida;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

}
