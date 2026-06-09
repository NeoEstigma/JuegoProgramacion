package model;

public class Mejoras {

	private int numRaspberry;
	private int numPC;
	private int numJunior;
	private int numSenior;
	private int numMaqCafe;
	private int numRGBS;
	private long precioRaspberry = 50;
	private long precioPc = 200;
	private long precioJunior = 500;
	private long precioSenior = 1000;
	private long precioCafe = 2500;
	private long precioRgbs = 5000;
	private long progresoMaximo = 100000;

	public Mejoras() {
	}

	public Mejoras(int numRaspberry, int numPC, int numJunior, int numSenior, int numMaqCafe, int numRGBS) {
		this.numRaspberry = numRaspberry;
		this.numPC = numPC;
		this.numJunior = numJunior;
		this.numSenior = numSenior;
		this.numMaqCafe = numMaqCafe;
		this.numRGBS = numRGBS;
	}

	public int getNumRaspberry() {
		return numRaspberry;
	}

	public void setNumRaspberry(int numRaspberry) {
		this.numRaspberry = numRaspberry;
	}

	public int getNumPC() {
		return numPC;
	}

	public void setNumPC(int numPC) {
		this.numPC = numPC;
	}

	public int getNumJunior() {
		return numJunior;
	}

	public void setNumJunior(int numJunior) {
		this.numJunior = numJunior;
	}

	public int getNumSenior() {
		return numSenior;
	}

	public void setNumSenior(int numSenior) {
		this.numSenior = numSenior;
	}

	public int getNumMaqCafe() {
		return numMaqCafe;
	}

	public void setNumMaqCafe(int numMaqCafe) {
		this.numMaqCafe = numMaqCafe;
	}

	public int getNumRGBS() {
		return numRGBS;
	}

	public void setNumRGBS(int numRGBS) {
		this.numRGBS = numRGBS;
	}

	public long getPrecioRaspberry() {
		return precioRaspberry;
	}

	public void setPrecioRaspberry(long precioRaspberry) {
		this.precioRaspberry = precioRaspberry;
	}

	public long getPrecioPc() {
		return precioPc;
	}

	public void setPrecioPc(long precioPc) {
		this.precioPc = precioPc;
	}

	public long getPrecioJunior() {
		return precioJunior;
	}

	public void setPrecioJunior(long precioJunior) {
		this.precioJunior = precioJunior;
	}

	public long getPrecioSenior() {
		return precioSenior;
	}

	public void setPrecioSenior(long precioSenior) {
		this.precioSenior = precioSenior;
	}

	public long getPrecioCafe() {
		return precioCafe;
	}

	public void setPrecioCafe(long l) {
		this.precioCafe = l;
	}

	public long getPrecioRgbs() {
		return precioRgbs;
	}

	public void setPrecioRgbs(long l) {
		this.precioRgbs = l;
	}

	public long getProgresoMaximo() {
		return progresoMaximo;
	}

	public void setProgresoMaximo(int progresoMaximo) {
		this.progresoMaximo = progresoMaximo;
	}

}
