package controller;

import java.io.IOException;
import java.net.URL;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.FrasesDao;
import model.Partida;
import model.PartidaDao;
import model.Ranking;
import model.RankingDao;

public class GameController {

	@FXML
	private Label lblDataPoints, lblDpSegundo, lblNivel, lblDpPorClick, lblOperador;
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
	private RankingDao rankingDao = new RankingDao();
	private FrasesDao frasesDao = new FrasesDao();
	private Timeline produccionPasiva;

	@FXML
	public void initialize() {
		partida = Partida.getInstancia();

		if (partida == null) {
			terminalArea.appendText("Error: no hay partida iniciada\n");
			return;
		}

		iniciarTimeline();
		lblOperador.setText(partida.getJugador());
		terminalArea.appendText("Sistema iniciado...\n");
		terminalArea.appendText("> Operador: " + partida.getJugador() + "\n");
		terminalArea.appendText("> Rango actual: " + partida.getNombreNivel() + "\n");
		actualizarVista();
	}

	private void iniciarTimeline() {
		produccionPasiva = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
			if (partida != null && !partida.estaTerminado()) {
				partida.tickSegundo();
				actualizarVista();

				// Muestra frase cada 10 segundos si hay producción pasiva
				if (partida.getDpSegundo() > 0 && partida.getTiempoPartida() % 10 == 0) {
					String frase = frasesDao.getFraseAleatoria();
					if (!frase.isEmpty()) {
						terminalArea.appendText(frase + "\n");
					}
				}
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
		guardarEnRanking();
		String msg = partida.avanzarProgreso();
		if ("FIN".equals(msg)) {
			terminarJuego();
		} else {
			terminalArea.appendText(msg + "\n");
		}
		actualizarVista();
	}

	private void guardarEnRanking() {
		Ranking resultado = new Ranking(partida.getJugador(), partida.getDp(), partida.getNivel(), partida.getMejoras(),
				partida.getTiempoPartida(), partida.getFechaInicio());
		rankingDao.guardarOActualizar(resultado);
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
		guardarEnRanking();
		terminalArea.appendText("> resultado guardado en ranking\n");
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
		guardarEnRanking();
		terminalArea.appendText("> partida guardada\n");
	}

	@FXML
	private void guardarSalir() {
		partidaDao.guardar(partida);
		guardarEnRanking();
		terminalArea.appendText("> partida guardada. Saliendo...\n");
		volverAlMenu();
	}

	@FXML
	private void salir() {
		Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
		alerta.setTitle("Salir");
		alerta.setHeaderText(null);
		alerta.setGraphic(null);
		alerta.setContentText("¿Seguro que quieres salir? Los cambios no guardados se perderán.");

		alerta.getDialogPane().getStylesheets().add(getClass().getResource("/View/style.css").toExternalForm());

		alerta.getDialogPane().getStyleClass().add("terminal-dialog");
		alerta.showAndWait().ifPresent(respuesta -> {
			if (respuesta == ButtonType.OK) {
				terminalArea.appendText("> saliendo sin guardar\n");
				volverAlMenu();
			}
		});
	}

	private void volverAlMenu() {
		if (produccionPasiva != null) {
			produccionPasiva.stop();
		}

		try {
			URL fxml = getClass().getResource("/View/Menu.fxml");

			if (fxml == null) {
				throw new IOException("No se encontró Menu.fxml");
			}

			Parent root = FXMLLoader.load(fxml);

			Stage stage = (Stage) terminalArea.getScene().getWindow();

			stage.getScene().setRoot(root);
			stage.setMaximized(true);

		} catch (IOException e) {
			System.out.println("Error al volver al menú: " + e.getMessage());
			e.printStackTrace();
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