package model;

/**
 * Clase que representa las mejoras disponibles en el juego Terminal Clicker.
 * Almacena la cantidad comprada de cada mejora y su precio actual.
 * Los precios se inicializan con los valores base y se actualizan
 * con cada compra aplicando un multiplicador incremental.
 *
 * @author Mateo
 */

public class Mejoras {

	/** Número de unidades compradas de cada mejora. */
	private int numRaspberry;
	private int numPC;
	private int numJunior;
	private int numSenior;
	private int numMaqCafe;
	private int numRGBS;
	
	/** Precio actual de cada mejora en Data Points. */
	private long precioRaspberry;
	private long precioPc;
	private long precioJunior;
	private long precioSenior;
	private long precioCafe;
	private long precioRgbs;

	/**
     * Constructor por defecto.
     * Inicializa todos los contadores a cero y los precios a sus valores base:
     * Raspberry 50 DP, PC 200 DP, Junior 500 DP, Senior 1000 DP,
     * Café 2500 DP y RGBS 5000 DP.
     */
	
	public Mejoras() {
		this.numRaspberry = 0;
		this.numPC = 0;
		this.numJunior = 0;
		this.numSenior = 0;
		this.numMaqCafe = 0;
		this.numRGBS = 0;
		this.precioRaspberry = 50;
		this.precioPc = 200;
		this.precioJunior = 500;
		this.precioSenior = 1000;
		this.precioCafe = 2500;
		this.precioRgbs = 5000;
	}
	
	/**
     * Calcula el precio actual de una mejora según las unidades ya compradas.
     * Aplica la fórmula: precioBase * multiplicador^cantidad.
     * Se usa al cargar una partida guardada para recalcular los precios correctos.
     *
     * @param precioBase     precio inicial de la mejora en DP
     * @param multiplicador  factor de incremento por cada compra (por ejemplo 1.05)
     * @param cantidad       número de unidades ya compradas
     * @return precio actual de la mejora redondeado al entero más cercano.
     */
	public long calcularPrecio(long precioBase, double multiplicador, int cantidad) {
		return Math.round(precioBase * Math.pow(multiplicador, cantidad));
	}
	
	/** @return número de Raspberry compradas */
	public int getNumRaspberry() {
		return numRaspberry;
	}

	/** @param numRaspberry número de Raspberry compradas */
	public void setNumRaspberry(int numRaspberry) {
		this.numRaspberry = numRaspberry;
	}
	
	/** @return número de PC comprados */
	public int getNumPC() {
		return numPC;
	}

	/** @param numPC número de PC comprados */
	public void setNumPC(int numPC) {
		this.numPC = numPC;
	}
	
	 /** @return número de Junior comprados */
	public int getNumJunior() {
		return numJunior;
	}

	 /** @param numJunior número de Junior comprados */
	public void setNumJunior(int numJunior) {
		this.numJunior = numJunior;
	}
	
	 /** @return número de Senior comprados */
	public int getNumSenior() {
		return numSenior;
	}
	
	/** @param numSenior número de Senior comprados */
	public void setNumSenior(int numSenior) {
		this.numSenior = numSenior;
	}

	  /** @return número de Máquinas de Café compradas */
	public int getNumMaqCafe() {
		return numMaqCafe;
	}
	
	/** @param numMaqCafe número de Máquinas de Café compradas */
	public void setNumMaqCafe(int numMaqCafe) {
		this.numMaqCafe = numMaqCafe;
	}

	
    /** @return número de RGBS comprados */
	public int getNumRGBS() {
		return numRGBS;
	}
	
	/** @param numRGBS número de RGBS comprados */
	public void setNumRGBS(int numRGBS) {
		this.numRGBS = numRGBS;
	}

	 /** @return precio actual de Raspberry en DP */
	public long getPrecioRaspberry() {
		return precioRaspberry;
	}

	 /** @param precioRaspberry nuevo precio de Raspberry */
	public void setPrecioRaspberry(long precioRaspberry) {
		this.precioRaspberry = precioRaspberry;
	}

	 /** @return precio actual de PC en DP */
	public long getPrecioPc() {
		return precioPc;
	}

	/** @param precioPc nuevo precio de PC */
	public void setPrecioPc(long precioPc) {
		this.precioPc = precioPc;
	}

	 /** @return precio actual de Junior en DP */
	public long getPrecioJunior() {
		return precioJunior;
	}
	
	/** @param precioJunior nuevo precio de Junior */
	public void setPrecioJunior(long precioJunior) {
		this.precioJunior = precioJunior;
	}

	/** @return precio actual de Senior en DP */
	public long getPrecioSenior() {
		return precioSenior;
	}
	
	/** @param precioSenior nuevo precio de Senior */
	public void setPrecioSenior(long precioSenior) {
		this.precioSenior = precioSenior;
	}
	
	/** @return precio actual de Máquina de Café en DP */
	public long getPrecioCafe() {
		return precioCafe;
	}

	/** @param precio nuevo precio de Máquina de Café */
	public void setPrecioCafe(long precio) {
		this.precioCafe = precio;
	}
	
	/** @return precio actual de RGBS en DP */
	public long getPrecioRgbs() {
		return precioRgbs;
	}

	/** @param precio nuevo precio de RGBS */
	public void setPrecioRgbs(long precio) {
		this.precioRgbs = precio;
	}

}
