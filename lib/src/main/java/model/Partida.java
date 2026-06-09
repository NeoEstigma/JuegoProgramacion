package model;

import java.util.Date;

public class Partida {

	private String jugador;
	private long dp;
	private long dpPorClick;
	private long dpSegundo;
	private int nivel;
	private boolean terminado;

	private long progresoMaximo;

	private Mejoras mejoras;
	private long tiempoPartida;
	private Date fechaInicio;

	private static final int NIVEL_MAXIMO = 10;
	private static final String[] NOMBRES_NIVELES = { "Script Kiddie", "Escaner de Red", "Analista de Sistemas",
			"Operador Encubierto", "Especialista en Intrusion", "Arquitecto Digital", "Maestro del Backdoor",
			"Fantasma de la Red", "Overmind", "ROOT ABSOLUTO - FINAL" };

	private static Partida instancia;

	public static Partida getInstancia() {
		return instancia;
	}

	public static Partida nuevaPartida(String jugador) {
		instancia = new Partida(jugador);
		return instancia;
	}

	public static void setInstancia(Partida p) {
		instancia = p;
	}

	public Partida() {
		this.dp = 0;
		this.dpPorClick = 1;
		this.dpSegundo = 0;
		this.nivel = 1;
		this.terminado = false;
		this.progresoMaximo = 100000;
	}

	public Partida(String jugador) {
		this();
		this.jugador = jugador;
		this.fechaInicio = new Date();
		this.mejoras = new Mejoras(0, 0, 0, 0, 0, 0);
	}

	public Partida(String jugador, int dp, int nivel, int dpPorSegundo, Mejoras mejoras, long tiempoPartida,
			Date fechaInicio) {
		this();
		this.jugador = jugador;
		this.dp = dp;
		this.nivel = nivel;
		this.dpSegundo = dpPorSegundo;
		this.mejoras = mejoras;
		this.tiempoPartida = tiempoPartida;
		this.fechaInicio = fechaInicio;
	}

	// ── Acciones del juego ──

	public String click() {
		if (terminado) {
			return null;
		}
		dp += dpPorClick;
		return "> comando ejecutado +" + dpPorClick + " DP";
	}

	public String comprarRaspberry() {
		if (terminado) {
			return null;
		}
		if (dp >= mejoras.getPrecioRaspberry()) {
			dp -= mejoras.getPrecioRaspberry();
			dpPorClick += 1;
			mejoras.setNumRaspberry(mejoras.getNumRaspberry() + 1);
			mejoras.setPrecioRaspberry(aumentarPrecio(mejoras.getPrecioRaspberry(), 1.05));
			return "> Raspberry comprada. +1 DP por clic";
		}
		return "> DP insuficientes para Raspberry";
	}

	public String comprarPc() {
		if (terminado) {
			return null;
		}
		if (dp >= mejoras.getPrecioPc()) {
			dp -= mejoras.getPrecioPc();
			dpPorClick += 5;
			mejoras.setNumPC(mejoras.getNumPC() + 1);
			mejoras.setPrecioPc(aumentarPrecio(mejoras.getPrecioPc(), 1.12));
			return "> PC comprado. +5 DP por clic";
		}
		return "> DP insuficientes para PC";
	}

	public String comprarJunior() {
		if (terminado) {
			return null;
		}
		if (dp >= mejoras.getPrecioJunior()) {
			dp -= mejoras.getPrecioJunior();
			dpSegundo += 10;
			mejoras.setNumJunior(mejoras.getNumJunior() + 1);
			mejoras.setPrecioJunior(aumentarPrecio(mejoras.getPrecioJunior(), 1.08));
			return "> Junior comprado. +10 DP/s";
		}
		return "> DP insuficientes para Junior";
	}

	public String comprarSenior() {
		if (terminado) {
			return null;
		}
		if (dp >= mejoras.getPrecioSenior()) {
			dp -= mejoras.getPrecioSenior();
			dpSegundo += 50;
			mejoras.setNumSenior(mejoras.getNumSenior() + 1);
			mejoras.setPrecioSenior(aumentarPrecio(mejoras.getPrecioSenior(), 1.10));
			return "> Senior comprado. +50 DP/s";
		}
		return "> DP insuficientes para Senior";
	}

	public String comprarCafe() {
		if (terminado) {
			return null;
		}
		if (dp >= mejoras.getPrecioCafe()) {
			dp -= mejoras.getPrecioCafe();
			long aumento = Math.max(1, Math.round(dpSegundo * 0.03));
			dpSegundo += aumento;
			mejoras.setNumMaqCafe(mejoras.getNumMaqCafe() + 1);
			mejoras.setPrecioCafe(aumentarPrecio(mejoras.getPrecioCafe(), 1.35));
			return "> Coffee comprado. +3% produccion. +" + aumento + " DP/s";
		}
		return "> DP insuficientes para Coffee";
	}

	public String comprarRgbs() {
		if (terminado) {
			return null;
		}
		if (dp >= mejoras.getPrecioRgbs()) {
			dp -= mejoras.getPrecioRgbs();
			long aumento = Math.max(1, Math.round(dpSegundo * 0.10));
			dpSegundo += aumento;
			mejoras.setNumRGBS(mejoras.getNumRGBS() + 1);
			mejoras.setPrecioRgbs(aumentarPrecio(mejoras.getPrecioRgbs(), 1.50));
			return "> RGBS comprado. +10% produccion. +" + aumento + " DP/s";
		}
		return "> DP insuficientes para RGBS";
	}

	public String avanzarProgreso() {
		if (terminado) {
			return null;
		}
		if (dp >= progresoMaximo) {
			dp -= progresoMaximo;
			nivel++;
			if (nivel >= NIVEL_MAXIMO) {
				terminado = true;
				return "FIN";
			}
			progresoMaximo = aumentarPrecio(progresoMaximo, 2.2);
			return "> Nuevo rango desbloqueado: " + getNombreNivel();
		}
		return "> Necesitas " + progresoMaximo + " DP para avanzar";
	}

	public void tickSegundo() {
		if (!terminado) {
			dp += dpSegundo;
		}
	}

	private long aumentarPrecio(long precio, double mult) {
		long nuevo = Math.round(precio * mult);
		return nuevo <= precio ? precio + 1 : nuevo;
	}

	public String getNombreNivel() {
		return NOMBRES_NIVELES[nivel - 1];
	}

	public boolean estaTerminado() {
		return terminado;
	}

	// ── Getters y setters ──

	public String getJugador() {
		return jugador;
	}

	public void setJugador(String jugador) {
		this.jugador = jugador;
	}

	public long getDp() {
		return dp;
	}

	public void setDp(int dp) {
		this.dp = dp;
	}

	public long getDpPorClick() {
		return dpPorClick;
	}

	public long getDpSegundo() {
		return dpSegundo;
	}

	public void setDpPorSegundo(int dps) {
		this.dpSegundo = dps;
	}

	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	public long getProgresoMaximo() {
		return progresoMaximo;
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

	public void setTiempoPartida(long t) {
		this.tiempoPartida = t;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date f) {
		this.fechaInicio = f;
	}

	public long getPrecioRaspberry() {
		return mejoras.getPrecioRaspberry();
	}

	public long getPrecioPc() {
		return mejoras.getPrecioPc();
	}

	public long getPrecioJunior() {
		return mejoras.getPrecioJunior();
	}

	public long getPrecioSenior() {
		return mejoras.getPrecioSenior();
	}

	public long getPrecioCafe() {
		return mejoras.getPrecioCafe();
	}

	public long getPrecioRgbs() {
		return mejoras.getPrecioRgbs();
	}
}