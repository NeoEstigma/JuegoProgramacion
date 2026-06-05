package model;

import java.util.Date;

public class Partida {

	private String jugador;
	private int dp;
	private int nivel;
	private int dpPorSegundo;

	private Mejoras mejoras;

	private long tiempoPartida;
	private Date fechaInicio;

	public Partida() {
	}

	public Partida(String jugador, int dp, int nivel, int dpPorSegundo, Mejoras mejoras, long tiempoPartida,
			Date fechaInicio) {
		this.jugador = jugador;
		this.dp = dp;
		this.nivel = nivel;
		this.dpPorSegundo = dpPorSegundo;
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

	public int getDp() {
		return dp;
	}

	public void setDp(int dp) {
		this.dp = dp;
	}

	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	public int getDpPorSegundo() {
		return dpPorSegundo;
	}

	public void setDpPorSegundo(int dpPorSegundo) {
		this.dpPorSegundo = dpPorSegundo;
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
