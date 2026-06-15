package model;

import java.util.Date;

import controller.SoundController;

/**
 * Clase principal del modelo de Terminal Clicker. Representa el estado completo
 * de una partida en curso e implementa toda la lógica del juego: acumulación de
 * Data Points, compra de mejoras, producción pasiva y avance de nivel.
 * Implementa el patrón Singleton para que el controlador pueda acceder a la
 * partida activa desde cualquier punto de la aplicación.
 *
 * @author Mateo
 */

public class Partida {

	/** Nombre del jugador operador. */
	private String jugador;

	/** Data Points acumulados actualmente. */
	private long dp;

	/** Data Points que se suman por cada clic. */
	private long dpPorClick;

	/** Data Points que se generan automáticamente cada segundo. */
	private long dpSegundo;

	/** Nivel de acceso actual del jugador (1 al máximo). */
	private int nivel;

	/** Indica si la partida ha finalizado. */
	private boolean terminado;

	/** DP necesarios para avanzar al siguiente nivel. */
	private long progresoMaximo;

	/** Objeto que contiene las mejoras compradas y sus precios actuales. */
	private Mejoras mejoras;

	/** Tiempo transcurrido en la partida en segundos. */
	private long tiempoPartida;

	/** Fecha y hora en que se inició la partida. */
	private Date fechaInicio;

	/** Número máximo de niveles disponibles en el juego. */
	private static final int NIVEL_MAXIMO = 5;

	/** Nombres de cada nivel de acceso. */
	private static final String[] NOMBRES_NIVELES = { "1: Script Kiddie", "2: Pen Tester", "3: Operador Encubierto",
			"4: Especialista en Intrusion", "5: Maestro del Backdoor" };

	/*
	 * Otros niveles temporalmente deshabilitados: "Escaner de Red",
	 * "Arquitecto Digital", "Overmind", "Fantasma de la Red",
	 * "ROOT ABSOLUTO - FINAL"
	 */

	/** Instancia única de la partida activa. Patrón Singleton. */
	private static Partida instancia;

	/**
	 * Devuelve la instancia activa de la partida.
	 *
	 * @return instancia de {@link Partida} activa, o null si no hay ninguna
	 */
	public static Partida getInstancia() {
		return instancia;
	}

	/**
	 * Crea una nueva partida con el nombre del jugador dado y la establece como
	 * instancia activa del singleton.
	 *
	 * @param jugador nombre del operador introducido en el menú
	 * @return nueva instancia de {@link Partida}
	 */
	public static Partida nuevaPartida(String jugador) {
		instancia = new Partida(jugador);
		return instancia;
	}

	/**
	 * Establece una partida existente como instancia activa del singleton. Se usa
	 * al cargar una partida guardada desde MongoDB.
	 *
	 * @param p partida a establecer como activa
	 */
	public static void setInstancia(Partida p) {
		instancia = p;
	}

	/**
	 * Constructor por defecto. Inicializa todos los valores de una partida nueva
	 * desde cero.
	 */
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

	/**
	 * Constructor con nombre de jugador. Inicializa la partida desde cero y asigna
	 * el nombre del operador.
	 *
	 * @param jugador nombre del operador
	 */
	public Partida(String jugador) {
		this();
		this.jugador = jugador;
	}

	/**
	 * Constructor completo para reconstruir una partida desde MongoDB. Usado por
	 * {@link PartidaDao} al cargar una partida guardada.
	 *
	 * @param jugador        nombre del operador
	 * @param dp             Data Points acumulados
	 * @param dpPorClick     DP por clic
	 * @param dpSegundo      DP por segundo
	 * @param nivel          nivel de acceso actual
	 * @param terminado      si la partida ha finalizado
	 * @param progresoMaximo DP necesarios para el siguiente nivel
	 * @param mejoras        mejoras compradas
	 * @param tiempoPartida  tiempo transcurrido en segundos
	 * @param fechaInicio    fecha de inicio de la partida
	 */
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

	/**
	 * Recalcula los precios de todas las mejoras según las unidades compradas. Se
	 * llama al cargar una partida guardada para restaurar los precios correctos, ya
	 * que MongoDB solo almacena las cantidades, no los precios actuales.
	 */
	public void recalcularPrecios() {
		mejoras.setPrecioRaspberry(mejoras.calcularPrecio(50, 1.05, mejoras.getNumRaspberry()));
		mejoras.setPrecioPc(mejoras.calcularPrecio(200, 1.12, mejoras.getNumPC()));
		mejoras.setPrecioJunior(mejoras.calcularPrecio(500, 1.08, mejoras.getNumJunior()));
		mejoras.setPrecioSenior(mejoras.calcularPrecio(1000, 1.10, mejoras.getNumSenior()));
		mejoras.setPrecioCafe(mejoras.calcularPrecio(2500, 1.35, mejoras.getNumMaqCafe()));
		mejoras.setPrecioRgbs(mejoras.calcularPrecio(5000, 1.50, mejoras.getNumRGBS()));
	}

	/**
	 * Ejecuta un clic del jugador sumando los DP por clic al total.
	 *
	 * @return mensaje para mostrar en el terminal, o null si la partida ha
	 *         terminado
	 */
	public String click() {
		if (terminado) {
			return "Has ganado el juego, esta acción ya no se puede realizar";
		}

		dp += dpPorClick;
		return "> comando ejecutado +" + dpPorClick + " DP";
	}

	/**
	 * Compra la mejora Raspberry si hay suficientes DP. Aumenta los DP por clic en
	 * 1 y el precio un 5%.
	 *
	 * @return mensaje para mostrar en el terminal
	 */
	public String comprarRaspberry() {
		if (terminado) {
			return "Has ganado el juego, esta acción ya no se puede realizar";
		}

		if (dp >= mejoras.getPrecioRaspberry()) {
			dp -= mejoras.getPrecioRaspberry();
			dpPorClick += 1;

			mejoras.setNumRaspberry(mejoras.getNumRaspberry() + 1);
			mejoras.setPrecioRaspberry(aumentarPrecio(mejoras.getPrecioRaspberry(), 1.05));
			 SoundController.playMejora();
			return "> Raspberry comprada. +1 DP por clic";
		}
		 SoundController.playError();
		return "> DP insuficientes para Raspberry";
	}

	/**
	 * Compra la mejora PC si hay suficientes DP. Aumenta los DP por clic en 5 y el
	 * precio un 6%.
	 *
	 * @return mensaje para mostrar en el terminal
	 */
	public String comprarPc() {
		if (terminado) {
			return "Has ganado el juego, esta acción ya no se puede realizar";
		}

		if (dp >= mejoras.getPrecioPc()) {
			dp -= mejoras.getPrecioPc();
			dpPorClick += 5;

			mejoras.setNumPC(mejoras.getNumPC() + 1);
			mejoras.setPrecioPc(aumentarPrecio(mejoras.getPrecioPc(), 1.06));
			SoundController.playMejora();
			return "> PC comprado. +5 DP por clic";
		}
		 SoundController.playError();
		return "> DP insuficientes para PC";
	}

	/**
	 * Compra la mejora Junior si hay suficientes DP. Aumenta la producción pasiva
	 * en 10 DP/s y el precio un 8%.
	 *
	 * @return mensaje para mostrar en el terminal
	 */
	public String comprarJunior() {
		if (terminado) {
			return "Has ganado el juego, esta acción ya no se puede realizar";
		}

		if (dp >= mejoras.getPrecioJunior()) {
			dp -= mejoras.getPrecioJunior();
			dpSegundo += 10;

			mejoras.setNumJunior(mejoras.getNumJunior() + 1);
			mejoras.setPrecioJunior(aumentarPrecio(mejoras.getPrecioJunior(), 1.08));
			SoundController.playMejora();
			return "> Junior comprado. +10 DP/s";
		}
		 SoundController.playError();
		return "> DP insuficientes para Junior";
	}

	/**
	 * Compra la mejora Senior si hay suficientes DP. Aumenta la producción pasiva
	 * en 50 DP/s y el precio un 10%.
	 *
	 * @return mensaje para mostrar en el terminal
	 */
	public String comprarSenior() {
		if (terminado) {
			return "Has ganado el juego, esta acción ya no se puede realizar";
		}

		if (dp >= mejoras.getPrecioSenior()) {
			dp -= mejoras.getPrecioSenior();
			dpSegundo += 50;

			mejoras.setNumSenior(mejoras.getNumSenior() + 1);
			mejoras.setPrecioSenior(aumentarPrecio(mejoras.getPrecioSenior(), 1.10));
			SoundController.playMejora();
			return "> Senior comprado. +50 DP/s";
		}
		 SoundController.playError();
		return "> DP insuficientes para Senior";
	}

	/**
	 * Compra la mejora Máquina de Café si hay suficientes DP. Aumenta la producción
	 * pasiva en un 10% del total actual y el precio un 35%.
	 *
	 * @return mensaje para mostrar en el terminal
	 */
	public String comprarCafe() {
		if (terminado) {
			return "Has ganado el juego, esta acción ya no se puede realizar";
		}

		if (dp >= mejoras.getPrecioCafe()) {
			dp -= mejoras.getPrecioCafe();

			long aumento = Math.max(1, Math.round(dpSegundo * 0.1));
			dpSegundo += aumento;

			mejoras.setNumMaqCafe(mejoras.getNumMaqCafe() + 1);
			mejoras.setPrecioCafe(aumentarPrecio(mejoras.getPrecioCafe(), 1.35));
			SoundController.playMejora();
			return "> Coffee comprado. +3% produccion. +" + aumento + " DP/s";
		}
		 SoundController.playError();
		return "> DP insuficientes para Coffee";
	}

	/**
	 * Compra la mejora RGBS si hay suficientes DP. Aumenta la producción pasiva en
	 * un 50% del total actual y el precio un 50%.
	 *
	 * @return mensaje para mostrar en el terminal
	 */
	public String comprarRgbs() {
		if (terminado) {
			return "Has ganado el juego, esta acción ya no se puede realizar";
		}

		if (dp >= mejoras.getPrecioRgbs()) {
			dp -= mejoras.getPrecioRgbs();

			long aumento = Math.max(1, Math.round(dpSegundo * 0.50));
			dpSegundo += aumento;

			mejoras.setNumRGBS(mejoras.getNumRGBS() + 1);
			mejoras.setPrecioRgbs(aumentarPrecio(mejoras.getPrecioRgbs(), 1.50));
			SoundController.playMejora();
			return "> RGBS comprado. +10% produccion. +" + aumento + " DP/s";
		}
		 SoundController.playError();
		return "> DP insuficientes para RGBS";
	}

	/**
	 * Intenta avanzar al siguiente nivel de acceso consumiendo los DP requeridos.
	 * Si se alcanza el nivel máximo, marca la partida como terminada.
	 *
	 * @return "FIN" si se ha completado el juego, mensaje de avance de nivel, o
	 *         mensaje indicando los DP necesarios si no hay suficientes.
	 */
	public String avanzarProgreso() {
		if (terminado) {
			return "Has ganado el juego, esta acción ya no se puede realizar";
		}

		if (dp >= progresoMaximo) {
			dp -= progresoMaximo;

			if (nivel >= NIVEL_MAXIMO) {
				terminado = true;
				nivel = NIVEL_MAXIMO;
				return "FIN";
			}

			nivel++;

			progresoMaximo = aumentarPrecio(progresoMaximo, 2.2);
			
			SoundController.playFinal1();
			SoundController.playFinal2();
			return "> Nuevo rango desbloqueado: " + getNombreNivel();
		}
		 SoundController.playError();
		return "> Necesitas " + progresoMaximo + " DP para avanzar";
	}

	/**
	 * Acumula la producción pasiva de un segundo e incrementa el tiempo de partida.
	 * Llamado por el Timeline del {@link controller.GameController} cada segundo.
	 */
	public void tickSegundo() {
		if (!terminado) {
			dp += dpSegundo;
			tiempoPartida++;
		}
	}

	/**
	 * Aumenta un precio aplicando un multiplicador. Garantiza que el nuevo precio
	 * siempre sea mayor que el anterior.
	 *
	 * @param precio precio actual
	 * @param mult   multiplicador a aplicar
	 * @return nuevo precio incrementado
	 */
	private long aumentarPrecio(long precio, double mult) {
		long nuevo = Math.round(precio * mult);
		return nuevo <= precio ? precio + 1 : nuevo;
	}

	/**
	 * Devuelve el nombre del nivel de acceso actual. Corrige el valor de nivel si
	 * está fuera de rango.
	 *
	 * @return nombre del nivel actual
	 */
	public String getNombreNivel() {
		if (nivel < 1) {
			nivel = 1;
		}
		if (nivel > NIVEL_MAXIMO) {
			nivel = NIVEL_MAXIMO;
		}

		return NOMBRES_NIVELES[nivel - 1];
	}

	/**
	 * Indica si la partida ha finalizado.
	 *
	 * @return true si el juego ha terminado, false en caso contrario.
	 */
	public boolean estaTerminado() {
		return terminado;
	}

	/** @return nombre del jugador */
	public String getJugador() {
		return jugador;
	}

	/** @param jugador nombre del jugador */
	public void setJugador(String jugador) {
		this.jugador = jugador;
	}

	/** @return Data Points acumulados */
	public long getDp() {
		return dp;
	}

	/** @param dp Data Points a establecer */
	public void setDp(long dp) {
		this.dp = dp;
	}

	/** @return DP que se suman por cada clic */
	public long getDpPorClick() {
		return dpPorClick;
	}

	/** @param dpPorClick DP por clic a establecer */
	public void setDpPorClick(long dpPorClick) {
		this.dpPorClick = dpPorClick;
	}

	/** @return DP generados automáticamente por segundo */
	public long getDpSegundo() {
		return dpSegundo;
	}

	/** @param dpSegundo DP por segundo a establecer */
	public void setDpSegundo(long dpSegundo) {
		this.dpSegundo = dpSegundo;
	}

	/** @return nivel de acceso actual */
	public int getNivel() {
		return nivel;
	}

	/**
	 * Establece el nivel de acceso validando que esté dentro del rango permitido.
	 *
	 * @param nivel nivel a establecer
	 */
	public void setNivel(int nivel) {
		if (nivel < 1) {
			this.nivel = 1;
		} else if (nivel > NIVEL_MAXIMO) {
			this.nivel = NIVEL_MAXIMO;
		} else {
			this.nivel = nivel;
		}
	}

	/**
	 * Establece si la partida ha terminado. Usado por {@link PartidaDao} al
	 * reconstruir la partida desde MongoDB.
	 *
	 * @param terminado true si la partida ha finalizado
	 */
	public void setTerminado(boolean terminado) {
		this.terminado = terminado;
	}

	/** @return DP necesarios para avanzar al siguiente nivel */
	public long getProgresoMaximo() {
		return progresoMaximo;
	}

	/** @param progresoMaximo nuevo umbral de progreso */
	public void setProgresoMaximo(long progresoMaximo) {
		this.progresoMaximo = progresoMaximo;
	}

	/** @return objeto con las mejoras compradas y sus precios */
	public Mejoras getMejoras() {
		return mejoras;
	}

	/** @param mejoras objeto de mejoras a establecer */
	public void setMejoras(Mejoras mejoras) {
		this.mejoras = mejoras != null ? mejoras : new Mejoras();
	}

	/** @return tiempo transcurrido en la partida en segundos */
	public long getTiempoPartida() {
		return tiempoPartida;
	}

	/** @param tiempoPartida tiempo en segundos a establecer */
	public void setTiempoPartida(long tiempoPartida) {
		this.tiempoPartida = tiempoPartida;
	}

	/** @return fecha y hora de inicio de la partida */
	public Date getFechaInicio() {
		return fechaInicio;
	}

	/** @param fechaInicio fecha de inicio a establecer */
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	/** @return precio actual de la mejora Raspberry */
	public long getPrecioRaspberry() {
		return mejoras.getPrecioRaspberry();

	}

	/** @return precio actual de la mejora PC */
	public long getPrecioPc() {
		return mejoras.getPrecioPc();
	}

	/** @return precio actual de la mejora Junior */
	public long getPrecioJunior() {
		return mejoras.getPrecioJunior();
	}

	/** @return precio actual de la mejora Senior */
	public long getPrecioSenior() {
		return mejoras.getPrecioSenior();
	}

	/** @return precio actual de la mejora Máquina de Café */
	public long getPrecioCafe() {
		return mejoras.getPrecioCafe();
	}

	/** @return precio actual de la mejora RGBS */
	public long getPrecioRgbs() {
		return mejoras.getPrecioRgbs();
	}
}