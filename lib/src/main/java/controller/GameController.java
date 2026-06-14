package controller;

import java.io.IOException;
import java.net.URL;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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


/**
 * Controlador principal de la pantalla de juego de Terminal Clicker.
 * Gestiona la interacción del usuario con el juego: clics, compra de mejoras,
 * avance de nivel, guardado de partida y navegación de vuelta al menú.
 * Obtiene la partida activa a través del patrón singleton de {@link Partida}.
 *
 * @author Diego, Mateo, Laura
 */

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
	
	
	/** Partida activa obtenida del singleton. */
	private Partida partida;
	/** DAO para guardar y cargar la partida en curso en MongoDB. */
	private PartidaDao partidaDao = new PartidaDao();
	/** DAO para guardar y consultar el ranking en MongoDB. */
	private RankingDao rankingDao = new RankingDao();
	/** DAO para cargar frases aleatorias desde el fichero de texto. */
	private FrasesDao frasesDao = new FrasesDao();
	/** Timeline que gestiona la producción pasiva de DP por segundo. */
	private Timeline produccionPasiva;
	
	/**
     * Método de inicialización llamado automáticamente por JavaFX al cargar la vista.
     * Obtiene la instancia de partida del singleton, inicia el timeline de producción
     * pasiva y actualiza la vista con el estado inicial.
     */

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
		terminalArea.appendText("> Nivel actual: " + partida.getNombreNivel() + "\n");
		actualizarVista();
	}
	
	/**
     * Inicia el timeline de producción pasiva llamando a tickSegundo().
     * Se ejecuta cada segundo y acumula los DP generados por la producción pasiva de las mejoras correspondientes.
     * Cada 10 segundos, si hay producción pasiva activa, muestra una frase
     * aleatoria en el terminal.
     */

	private void iniciarTimeline() {
		produccionPasiva = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
			if (partida != null && !partida.estaTerminado()) {
				partida.tickSegundo();
				actualizarVista();

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
	
	 /**
     * Ejecuta un comando al pulsar el botón de hackear.
     * Suma los DP por clic al total acumulado y registra la acción en el terminal.
     */

	@FXML
	private void clickHack() {
		String msg = partida.click();
		if (msg != null) {
			terminalArea.appendText(msg + "\n");
		}
		actualizarVista();
	}
	
	/**
     * Compra la mejora Raspberry si hay suficientes DP.
     * Aumenta los DP por clic en 1.
     */

	@FXML
	private void comprarRaspberry() {
		terminalArea.appendText(partida.comprarRaspberry() + "\n");
		actualizarVista();
	}

	/**
     * Compra la mejora PC si hay suficientes DP.
     * Aumenta los DP por clic en 5.
     */
	
	@FXML
	private void comprarPc() {
		terminalArea.appendText(partida.comprarPc() + "\n");
		actualizarVista();
	}
	
	/**
     * Compra la mejora Junior si hay suficientes DP.
     * Aumenta la producción pasiva en 10 DP/s.
     */

	@FXML
	private void comprarJunior() {
		terminalArea.appendText(partida.comprarJunior() + "\n");
		actualizarVista();
	}
	
	/**
     * Compra la mejora Senior si hay suficientes DP.
     * Aumenta la producción pasiva en 50 DP/s.
     */

	@FXML
	private void comprarSenior() {
		terminalArea.appendText(partida.comprarSenior() + "\n");
		actualizarVista();
	}
	
	/**
     * Compra la mejora Máquina de Café si hay suficientes DP.
     * Aumenta la producción de Junior y Senior en un 3%.
     */

	@FXML
	private void comprarCafe() {
		terminalArea.appendText(partida.comprarCafe() + "\n");
		actualizarVista();
	}
	
	/**
     * Compra la mejora RGBS si hay suficientes DP.
     * Aumenta la producción de Junior y Senior en un 10%.
     */

	@FXML
	private void comprarRgbs() {
		terminalArea.appendText(partida.comprarRgbs() + "\n");
		actualizarVista();
	}
	
	/**
     * Intenta avanzar al siguiente nivel de acceso.
     * Guarda el estado en el ranking antes de avanzar.
     * Si se alcanza el nivel máximo, finaliza el juego llamando al método terminarJuego.
     */

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
	
	/**
     * Guarda o actualiza el resultado de la partida actual en la colección
     * de ranking de MongoDB mediante upsert por nombre de jugador.
     */

	private void guardarEnRanking() {
		Ranking resultado = new Ranking(partida.getJugador(), partida.getDp(), partida.getNivel(), partida.getMejoras(),
				partida.getTiempoPartida(), partida.getFechaInicio());
		rankingDao.guardarOActualizar(resultado);
	}
	
	/**
     * Finaliza el juego al alcanzar el nivel máximo.
     * Detiene la producción pasiva, muestra el mensaje de victoria,
     * guarda la partida y el ranking, y lanza una cuenta atrás de 5 segundos
     * antes de volver al menú principal.
     */

	private void terminarJuego() {
		produccionPasiva.stop();
		nodoBarra.setProgress(1);
		terminalArea.appendText("\n==============================\n");
		terminalArea.appendText("ACCESO TOTAL CONSEGUIDO\n");
		terminalArea.appendText("Has alcanzado ROOT ABSOLUTO\n");
		terminalArea.appendText("FIN DEL JUEGO\n");
		terminalArea.appendText("==============================\n");
		bloquearMejoras();
		guardar();
		guardarEnRanking();
		terminalArea.appendText("> resultado guardado en ranking\n");
		int[] segundos = { 5 };
		terminalArea.appendText("> Volviendo al menu en " + segundos[0] + "...\n");

		Timeline countdown = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
			segundos[0]--;
			if (segundos[0] > 0) {
				terminalArea.appendText("> Volviendo al menu en " + segundos[0] + "...\n");
			}
		}));
		countdown.setCycleCount(5);
		countdown.setOnFinished(e -> volverAlMenu());
		countdown.play();
	}
	
	/**
     * Bloquea los botones de mejora al terminar el juego,
     * mostrando el texto "FIN" en el estado de cada mejora.
     */

	private void bloquearMejoras() {
		lblRaspberryEstado.setText("FIN");
		lblPcEstado.setText("FIN");
		lblJuniorEstado.setText("FIN");
		lblSeniorEstado.setText("FIN");
		lblCafeEstado.setText("FIN");
		lblRgbsEstado.setText("FIN");
	}

	/**
     * Guarda la partida actual en MongoDB y actualiza el ranking.
     * Vinculado al botón "Guardar" del menú desplegable.
     */
	
	@FXML
	private void guardar() {
		partidaDao.guardar(partida);
		guardarEnRanking();
		terminalArea.appendText("> partida guardada\n");
	}

	/**
     * Guarda la partida actual y vuelve al menú principal.
     * Vinculado al botón "Guardar y salir" del menú desplegable.
     */
	
	@FXML
	private void guardarSalir() {
		partidaDao.guardar(partida);
		guardarEnRanking();
		terminalArea.appendText("> partida guardada. Saliendo...\n");
		volverAlMenu();
	}
	
	/**
     * Muestra un diálogo de confirmación antes de salir sin guardar.
     * Si el usuario confirma, vuelve al menú principal sin guardar los datos.
     */
	
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
	
	/**
     * Navega de vuelta al menú principal.
     * Detiene el timeline de producción pasiva antes de cambiar de escena.
     */

	private void volverAlMenu() {
		if (produccionPasiva != null) {
			produccionPasiva.stop();
		}

		try {
			URL fxml = getClass().getResource("/View/Menu.fxml");
			if (fxml == null) {
				throw new IOException("No se encontró Menu.fxml");
			}

			FXMLLoader loader = new FXMLLoader(fxml);
			Parent root = loader.load();

			Stage stage = (Stage) terminalArea.getScene().getWindow();

			Scene nuevaEscena = new Scene(root);
			nuevaEscena.getStylesheets().add(getClass().getResource("/View/style.css").toExternalForm());

			stage.setScene(nuevaEscena);
			stage.setMaximized(true);

		} catch (IOException e) {
			System.out.println("Error al volver al menú");
		}
	}
	
	/**
     * Actualiza todos los elementos visuales de la vista con el estado actual
     * de la partida: DP, producción, nivel, precios de mejoras y barra de progreso.
     */

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