package model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

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

	private static final int NIVEL_MAXIMO = 5;

	private static final String[] NOMBRES_NIVELES = { "Script Kiddie", "Pen Tester", "Operador Encubierto",
			"Especialista en Intrusion", "Maestro del Backdoor" };

	/*
	 * Otros niveles temporalmente deshabilitados: "Escaner de Red",
	 * "Arquitecto Digital", "Overmind", "Fantasma de la Red",
	 * "ROOT ABSOLUTO - FINAL"
	 */

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
		this.jugador = "";
		this.dp = 0;
		this.dpPorClick = 1;
		this.dpSegundo = 0;
		this.nivel = 1;
		this.terminado = false;
		this.progresoMaximo = 100000;
		this.mejoras = new Mejoras();
		this.tiempoPartida = 0;
		this.fechaInicio = new Date();
	}

	public Partida(String jugador) {
		this();
		this.jugador = jugador;
	}

	public Partida(String jugador, long dp, long dpPorClick, long dpSegundo, int nivel, boolean terminado,
			long progresoMaximo, Mejoras mejoras, long tiempoPartida, Date fechaInicio) {

		this.jugador = jugador;
		this.dp = dp;
		this.dpPorClick = dpPorClick;
		this.dpSegundo = dpSegundo;
		this.nivel = nivel;
		this.terminado = terminado;
		this.progresoMaximo = progresoMaximo;
		this.mejoras = mejoras != null ? mejoras : new Mejoras();
		this.tiempoPartida = tiempoPartida;
		this.fechaInicio = fechaInicio != null ? fechaInicio : new Date();
	}

	// ── Aumenta todos los precios al cargar partida ──
	public void comprarTodos() {

		mejoras.setPrecioRaspberry(mejoras.calcularPrecio(50, 1.05, mejoras.getNumRaspberry()));
		mejoras.setPrecioPc(mejoras.calcularPrecio(200, 1.12, mejoras.getNumPC()));
		mejoras.setPrecioJunior(mejoras.calcularPrecio(500, 1.08, mejoras.getNumJunior()));
		mejoras.setPrecioSenior(mejoras.calcularPrecio(1000, 1.10, mejoras.getNumSenior()));
		mejoras.setPrecioCafe(mejoras.calcularPrecio(2500, 1.35, mejoras.getNumMaqCafe()));
		mejoras.setPrecioRgbs(mejoras.calcularPrecio(5000, 1.50, mejoras.getNumRGBS()));
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

			long aumento = Math.max(1, Math.round(dpSegundo * 0.1));
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

			long aumento = Math.max(1, Math.round(dpSegundo * 0.50));
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

			if (nivel >= NIVEL_MAXIMO) {
				terminado = true;
				nivel = NIVEL_MAXIMO;
				return "FIN";
			}

			nivel++;

			if (nivel == NIVEL_MAXIMO) {
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
			tiempoPartida++;
		}
	}

	private long aumentarPrecio(long precio, double mult) {
		long nuevo = Math.round(precio * mult);
		return nuevo <= precio ? precio + 1 : nuevo;
	}

	public String getNombreNivel() {
		if (nivel < 1) {
			nivel = 1;
		}
		if (nivel > NIVEL_MAXIMO) {
			nivel = NIVEL_MAXIMO;
		}

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

	public void setDp(long dp) {
		this.dp = dp;
	}

	public long getDpPorClick() {
		return dpPorClick;
	}

	public void setDpPorClick(long dpPorClick) {
		this.dpPorClick = dpPorClick;
	}

	public long getDpSegundo() {
		return dpSegundo;
	}

	public void setDpSegundo(long dpSegundo) {
		this.dpSegundo = dpSegundo;
	}

	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		if (nivel < 1) {
			this.nivel = 1;
		} else if (nivel > NIVEL_MAXIMO) {
			this.nivel = NIVEL_MAXIMO;
		} else {
			this.nivel = nivel;
		}
	}

	public String obtenerFrases() {

		Random random = new Random();
		String lineaSeleccionada = null;
		int contador = 0;

		try (BufferedReader br = new BufferedReader(new FileReader("/lib/src/main/resources/documents/texto.txt"))) {
			String linea;

			while ((linea = br.readLine()) != null) {
				contador++;

				if (random.nextInt(contador) == 0) {
					lineaSeleccionada = linea;
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Archivo no encontrado");
		} catch (IOException e) {
			System.out.println("No se ha podido leer el archivo");
		}

		return lineaSeleccionada;
	}

	public void setTerminado(boolean terminado) {
		this.terminado = terminado;
	}

	public long getProgresoMaximo() {
		return progresoMaximo;
	}

	public void setProgresoMaximo(long progresoMaximo) {
		this.progresoMaximo = progresoMaximo;
	}

	public Mejoras getMejoras() {
		return mejoras;
	}

	public void setMejoras(Mejoras mejoras) {
		this.mejoras = mejoras != null ? mejoras : new Mejoras();
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