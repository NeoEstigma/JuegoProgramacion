package controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.util.Duration;

public class GameController {

	@FXML
	private Label lblDataPoints, lblDpSegundo, lblNivel, lblDpPorClick;

	@FXML
	private Label lblRaspberryValor, lblRaspberryPrecio, lblRaspberryEstado;
	@FXML
	private Label lblPcValor, lblPcPrecio, lblPcEstado;
	@FXML
	private Label lblJuniorValor, lblJuniorPrecio, lblJuniorEstado;
	@FXML
	private Label lblSeniorValor, lblSeniorPrecio, lblSeniorEstado;
	@FXML
	private Label lblCafeValor, lblCafePrecio, lblCafeEstado;
	@FXML
	private Label lblRgbsValor, lblRgbsPrecio, lblRgbsEstado;

	@FXML
	private Label lblProgresoActual, lblProgresoMaximo;

	@FXML
	private TextArea terminalArea;
	@FXML
	private ProgressBar nodoBarra;

	private long dataPoints = 0;
	private long dpPorClick = 1;
	private long dpSegundo = 0;

	private int nivel = 1;
	private static final int NIVEL_MAXIMO = 10;
	private boolean juegoTerminado = false;

	private final String[] nombresNiveles = { "Script Kiddie", "Escaner de Red", "Analista de Sistemas",
			"Operador Encubierto", "Especialista en Intrusion", "Arquitecto Digital", "Maestro del Backdoor",
			"Fantasma de la Red", "Overmind", "ROOT ABSOLUTO - FINAL" };

	private long precioRaspberry = 50;
	private long precioPc = 200;
	private long precioJunior = 500;
	private long precioSenior = 1000;
	private long precioCafe = 2500;
	private long precioRgbs = 5000;

	private long progresoMaximo = 100000;

	private Timeline produccionPasiva;

	@FXML
	private void initialize() {
		terminalArea.appendText("Sistema iniciado...\n");
		terminalArea.appendText("> Rango actual: " + obtenerNombreNivel() + "\n");

		produccionPasiva = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
			if (!juegoTerminado) {
				dataPoints += dpSegundo;
				actualizarVista();
			}
		}));

		produccionPasiva.setCycleCount(Timeline.INDEFINITE);
		produccionPasiva.play();
	}

	@FXML
	private void clickHack() {
		if (juegoTerminado) {
			return;
		}

		dataPoints += dpPorClick;
		terminalArea.appendText("> comando ejecutado +" + dpPorClick + " DP\n");
		actualizarVista();
	}

	@FXML
	private void comprarRaspberry() {
		if (juegoTerminado) {
			return;
		}

		if (dataPoints >= precioRaspberry) {
			dataPoints -= precioRaspberry;
			dpPorClick += 1;
			precioRaspberry = aumentarPrecio(precioRaspberry, 1.05);
			terminalArea.appendText("> Raspberry comprada. +1 DP por clic\n");
		} else {
			terminalArea.appendText("> DP insuficientes para Raspberry\n");
		}

		actualizarVista();
	}

	@FXML
	private void comprarPc() {
		if (juegoTerminado) {
			return;
		}

		if (dataPoints >= precioPc) {
			dataPoints -= precioPc;
			dpPorClick += 5;
			precioPc = aumentarPrecio(precioPc, 1.12);
			terminalArea.appendText("> PC comprado. +5 DP por clic\n");
		} else {
			terminalArea.appendText("> DP insuficientes para PC\n");
		}

		actualizarVista();
	}

	@FXML
	private void comprarJunior() {
		if (juegoTerminado) {
			return;
		}

		if (dataPoints >= precioJunior) {
			dataPoints -= precioJunior;
			dpSegundo += 10;
			precioJunior = aumentarPrecio(precioJunior, 1.08);
			terminalArea.appendText("> Junior comprado. +10 DP/s\n");
		} else {
			terminalArea.appendText("> DP insuficientes para Junior\n");
		}

		actualizarVista();
	}

	@FXML
	private void comprarSenior() {
		if (juegoTerminado) {
			return;
		}

		if (dataPoints >= precioSenior) {
			dataPoints -= precioSenior;
			dpSegundo += 50;
			precioSenior = aumentarPrecio(precioSenior, 1.10);
			terminalArea.appendText("> Senior comprado. +50 DP/s\n");
		} else {
			terminalArea.appendText("> DP insuficientes para Senior\n");
		}

		actualizarVista();
	}

	@FXML
	private void comprarCafe() {
		if (juegoTerminado) {
			return;
		}

		if (dataPoints >= precioCafe) {
			dataPoints -= precioCafe;

			long aumento = Math.max(1, Math.round(dpSegundo * 0.03));
			dpSegundo += aumento;

			precioCafe = aumentarPrecio(precioCafe, 1.35);
			terminalArea.appendText("> Coffee comprado. +3% produccion. +" + aumento + " DP/s\n");
		} else {
			terminalArea.appendText("> DP insuficientes para Coffee\n");
		}

		actualizarVista();
	}

	@FXML
	private void comprarRgbs() {
		if (juegoTerminado) {
			return;
		}

		if (dataPoints >= precioRgbs) {
			dataPoints -= precioRgbs;

			long aumento = Math.max(1, Math.round(dpSegundo * 0.10));
			dpSegundo += aumento;

			precioRgbs = aumentarPrecio(precioRgbs, 1.50);
			terminalArea.appendText("> RGBS comprado. +10% produccion. +" + aumento + " DP/s\n");
		} else {
			terminalArea.appendText("> DP insuficientes para RGBS\n");
		}

		actualizarVista();
	}

	@FXML
	private void avanzarProgreso() {
		if (juegoTerminado) {
			return;
		}

		if (dataPoints >= progresoMaximo) {
			dataPoints -= progresoMaximo;
			nivel++;

			if (nivel >= NIVEL_MAXIMO) {
				terminarJuego();
				return;
			}

			progresoMaximo = aumentarPrecio(progresoMaximo, 2.2);

			terminalArea.appendText("> Nuevo rango desbloqueado: " + obtenerNombreNivel() + "\n");
		} else {
			terminalArea.appendText("> Necesitas " + progresoMaximo + " DP para avanzar\n");
		}

		actualizarVista();
	}

	private String obtenerNombreNivel() {
		return nombresNiveles[nivel - 1];
	}

	private long aumentarPrecio(long precioActual, double multiplicador) {
		long nuevoPrecio = Math.round(precioActual * multiplicador);

		if (nuevoPrecio <= precioActual) {
			nuevoPrecio = precioActual + 1;
		}

		return nuevoPrecio;
	}

	private void terminarJuego() {
		juegoTerminado = true;

		if (produccionPasiva != null) {
			produccionPasiva.stop();
		}

		nivel = NIVEL_MAXIMO;

		nodoBarra.setProgress(1);
		lblNivel.setText(obtenerNombreNivel());
		lblProgresoActual.setText(String.valueOf(progresoMaximo));
		lblProgresoMaximo.setText(String.valueOf(progresoMaximo));

		terminalArea.appendText("\n==============================\n");
		terminalArea.appendText("ACCESO TOTAL CONSEGUIDO\n");
		terminalArea.appendText("Has alcanzado ROOT ABSOLUTO\n");
		terminalArea.appendText("FIN DEL JUEGO\n");
		terminalArea.appendText("==============================\n");

		bloquearMejoras();
	}

	private void bloquearMejoras() {
		lblRaspberryEstado.setText("FIN");
		lblPcEstado.setText("FIN");
		lblJuniorEstado.setText("FIN");
		lblSeniorEstado.setText("FIN");
		lblCafeEstado.setText("FIN");
		lblRgbsEstado.setText("FIN");
	}

	private void actualizarVista() {
		lblDataPoints.setText(String.valueOf(dataPoints));
		lblDpSegundo.setText(String.valueOf(dpSegundo));
		lblDpPorClick.setText(String.valueOf(dpPorClick));
		lblNivel.setText(obtenerNombreNivel());

		lblRaspberryPrecio.setText(String.valueOf(precioRaspberry));
		lblPcPrecio.setText(String.valueOf(precioPc));
		lblJuniorPrecio.setText(String.valueOf(precioJunior));
		lblSeniorPrecio.setText(String.valueOf(precioSenior));
		lblCafePrecio.setText(String.valueOf(precioCafe));
		lblRgbsPrecio.setText(String.valueOf(precioRgbs));

		lblRaspberryValor.setText("1");
		lblPcValor.setText("5");
		lblJuniorValor.setText("10");
		lblSeniorValor.setText("50");
		lblCafeValor.setText("3");
		lblRgbsValor.setText("10");

		lblProgresoActual.setText(String.valueOf(dataPoints));
		lblProgresoMaximo.setText(String.valueOf(progresoMaximo));

		double progreso = (double) dataPoints / progresoMaximo;

		if (progreso > 1) {
			progreso = 1;
		}

		nodoBarra.setProgress(progreso);
	}

	@FXML
	private void guardar() {
		terminalArea.appendText("> partida guardada\n");
	}

	@FXML
	private void guardarSalir() {

		terminalArea.appendText("> partida guardada. Saliendo...\n");
	}

	@FXML
	private void salir() {
		terminalArea.appendText("> saliendo...\n");
	}
}