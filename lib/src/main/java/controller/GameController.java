package controller;

import java.io.IOException;
import java.net.URL;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Partida;
import model.PartidaDao;

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

	private Partida partida;
	private PartidaDao partidaDao = new PartidaDao();
	private Timeline produccionPasiva;

	@FXML
	public void initialize() {
		partida = new Partida();
		iniciarTimeline();
		terminalArea.appendText("Sistema iniciado...\n");
		terminalArea.appendText("> Rango actual: " + partida.getNombreNivel() + "\n");
		actualizarVista();
	}

	public void setJugador(String nombre) {
		partida.setJugador(nombre);
		terminalArea.appendText("> Operador: " + nombre + "\n");
	}

	public void cargarPartida(Partida partidaGuardada) {
		this.partida = partidaGuardada;
		terminalArea.appendText("> Partida cargada. Bienvenido, " + partida.getJugador() + "\n");
		actualizarVista();
	}

	private void iniciarTimeline() {
		produccionPasiva = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
			if (!partida.estaTerminado()) {
				partida.tickSegundo();
				actualizarVista();
			}
		}));
		produccionPasiva.setCycleCount(Timeline.INDEFINITE);
		produccionPasiva.play();
	}

	@FXML
	private void clickHack() {
		String msg = partida.click();
		if (msg != null) {
			terminalArea.appendText(msg + "\n");
		}
		actualizarVista();
	}

	@FXML
	private void comprarRaspberry() {
		terminalArea.appendText(partida.comprarRaspberry() + "\n");
		actualizarVista();
	}

	@FXML
	private void comprarPc() {
		terminalArea.appendText(partida.comprarPc() + "\n");
		actualizarVista();
	}

	@FXML
	private void comprarJunior() {
		terminalArea.appendText(partida.comprarJunior() + "\n");
		actualizarVista();
	}

	@FXML
	private void comprarSenior() {
		terminalArea.appendText(partida.comprarSenior() + "\n");
		actualizarVista();
	}

	@FXML
	private void comprarCafe() {
		terminalArea.appendText(partida.comprarCafe() + "\n");
		actualizarVista();
	}

	@FXML
	private void comprarRgbs() {
		terminalArea.appendText(partida.comprarRgbs() + "\n");
		actualizarVista();
	}

	@FXML
	private void avanzarProgreso() {
		String msg = partida.avanzarProgreso();
		if ("FIN".equals(msg)) {
			terminarJuego();
		} else {
			terminalArea.appendText(msg + "\n");
		}
		actualizarVista();
	}

	private void terminarJuego() {
		produccionPasiva.stop();
		nodoBarra.setProgress(1);
		terminalArea.appendText("\n==============================\n");
		terminalArea.appendText("ACCESO TOTAL CONSEGUIDO\n");
		terminalArea.appendText("Has alcanzado ROOT ABSOLUTO\n");
		terminalArea.appendText("FIN DEL JUEGO\n");
		terminalArea.appendText("==============================\n");
		bloquearMejoras();
		// Guardar en ranking automáticamente
		// rankingDao.insertar(...) ← lo añadimos cuando conectemos RankingDao
	}

	private void bloquearMejoras() {
		lblRaspberryEstado.setText("FIN");
		lblPcEstado.setText("FIN");
		lblJuniorEstado.setText("FIN");
		lblSeniorEstado.setText("FIN");
		lblCafeEstado.setText("FIN");
		lblRgbsEstado.setText("FIN");
	}

	@FXML
	private void guardar() {
		partidaDao.guardar(partida);
		terminalArea.appendText("> partida guardada\n");
	}

	@FXML
	private void guardarSalir() {
		partidaDao.guardar(partida);
		terminalArea.appendText("> partida guardada. Saliendo...\n");
		volverAlMenu();
	}

	@FXML
	private void salir() {
		terminalArea.appendText("> saliendo sin guardar\n");
		volverAlMenu();
	}

	private void volverAlMenu() {
		produccionPasiva.stop();
		try {
			URL fxml = getClass().getResource("/View/Menu.fxml");
			Parent root = FXMLLoader.load(fxml);
			Stage stage = (Stage) terminalArea.getScene().getWindow();
			stage.setScene(new Scene(root));
		} catch (IOException e) {
			System.out.println("Error al volver al menú: " + e.getMessage());
		}
	}

	private void actualizarVista() {
		lblDataPoints.setText(String.valueOf(partida.getDp()));
		lblDpSegundo.setText(String.valueOf(partida.getDpSegundo()));
		lblDpPorClick.setText(String.valueOf(partida.getDpPorClick()));
		lblNivel.setText(partida.getNombreNivel());

		lblRaspberryPrecio.setText(String.valueOf(partida.getPrecioRaspberry()));
		lblPcPrecio.setText(String.valueOf(partida.getPrecioPc()));
		lblJuniorPrecio.setText(String.valueOf(partida.getPrecioJunior()));
		lblSeniorPrecio.setText(String.valueOf(partida.getPrecioSenior()));
		lblCafePrecio.setText(String.valueOf(partida.getPrecioCafe()));
		lblRgbsPrecio.setText(String.valueOf(partida.getPrecioRgbs()));

		lblRaspberryValor.setText("1");
		lblPcValor.setText("5");
		lblJuniorValor.setText("10");
		lblSeniorValor.setText("50");
		lblCafeValor.setText("3");
		lblRgbsValor.setText("10");

		lblProgresoActual.setText(String.valueOf(partida.getDp()));
		lblProgresoMaximo.setText(String.valueOf(partida.getProgresoMaximo()));

		double progreso = (double) partida.getDp() / partida.getProgresoMaximo();
		nodoBarra.setProgress(Math.min(progreso, 1));
	}
}